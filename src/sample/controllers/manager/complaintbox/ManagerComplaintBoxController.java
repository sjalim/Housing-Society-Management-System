package sample.controllers.manager.complaintbox;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.models.ComplainBox;
import sample.models.Payments;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ManagerComplaintBoxController implements Initializable {

    @FXML private AnchorPane complaintBoxPane;

    @FXML private JFXButton refreshButton, addNewCatButton;

    @FXML private JFXListView<ComplainBox> complaintBoxListView;

    @FXML private JFXTextField filteredComplaints;

    @FXML private JFXComboBox<ComplainBox> categoryComboBox;
    String query;
    private DatabaseHandler databaseHandler;
    private ObservableList<ComplainBox> complainBoxObservableList;
    private ObservableList<ComplainBox> categoryList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        complainBoxObservableList = FXCollections.observableArrayList();
        categoryList = FXCollections.observableArrayList();
        try {
            loadFromDB();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        FilteredList<ComplainBox> filteredData = new FilteredList<>(complainBoxObservableList, b -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filteredComplaints.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(complainBox -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (complainBox.getFlatNumber().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true;
                } else if (complainBox.getComplainTitle().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false; // Does not match.
            });
        });
        complaintBoxListView.setItems(filteredData);
        complaintBoxListView.setCellFactory(ManagerCustomComplaintRowController
                -> new ManagerCustomComplaintRowController());

        refreshButton.setOnAction(actionEvent -> {
            try {
                loadFromDB();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        categoryComboBox.setOnMouseClicked((mouseEvent) -> {
            try {
                initCategory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        addNewCatButton.setOnMouseClicked((mouseEvent) -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/sample/views/manager/complaintbox/add_category.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(loader.getRoot());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Category");
            stage.show();
        });

        categoryComboBox.setOnAction((event) -> {
            ComplainBox selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
            if(selectedCategory==null){
                return;
            } else {
                try {
                    filterByCategory(selectedCategory);
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
//                complaintBoxListView.setItems(null);
                complaintBoxListView.setItems(complainBoxObservableList);
            }
        });
    }

    private void filterByCategory(ComplainBox selectedCategory) throws SQLException, ClassNotFoundException {
        complainBoxObservableList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT c.ComplainBoxId, c.FlatNumber, c.ComplainBoxStatus, c.ComplainBoxTitle, cc.CategoryName, c.ComplainBoxDescription, c.DateAdded," +
                "(SELECT COUNT(*) from ComplainBoxVote where VoteFlag = '1' and ComplainBoxId = cv.ComplainBoxId) as VoteUp," +
                "(SELECT COUNT(*) from ComplainBoxVote where VoteFlag = '0' and ComplainBoxId = cv.ComplainBoxId) as VoteDown " +
                "FROM ComplainBoxBoard as c " +
                "LEFT JOIN (SELECT DISTINCT ComplainBoxId FROM ComplainBoxVote) as cv " +
                "ON c.ComplainBoxId = cv.ComplainBoxId " +
                "INNER JOIN ComplainCategory as cc " +
                "ON cc.CategoryId = c.CategoryId WHERE c.CategoryId = '"+selectedCategory.getCategoryId()+"'";

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            complainBoxObservableList.add(new ComplainBox(rs.getInt("ComplainBoxId"),rs.getString("FlatNumber"),
                    rs.getString("ComplainBoxStatus"),rs.getString("ComplainBoxTitle"),
                    rs.getString("ComplainBoxDescription"), rs.getString("DateAdded"),
                    rs.getInt("VoteUp"), rs.getInt("VoteDown"),rs.getString("CategoryName")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void initCategory() throws SQLException, ClassNotFoundException {
        categoryList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT CategoryId, CategoryName FROM ComplainCategory";

        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            categoryList.add(new ComplainBox(rs.getInt("CategoryId"),rs.getString("CategoryName")));
        }
        databaseHandler.getDbConnection().close();
        categoryComboBox.setItems(categoryList);
    }

    private void loadFromDB() throws SQLException, ClassNotFoundException {
        complainBoxObservableList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT c.ComplainBoxId, c.FlatNumber, c.ComplainBoxStatus, c.ComplainBoxTitle, cc.CategoryName, c.ComplainBoxDescription, c.DateAdded," +
                    "(SELECT COUNT(*) from ComplainBoxVote where VoteFlag = '1' and ComplainBoxId = cv.ComplainBoxId) as VoteUp," +
                    "(SELECT COUNT(*) from ComplainBoxVote where VoteFlag = '0' and ComplainBoxId = cv.ComplainBoxId) as VoteDown " +
                "FROM ComplainBoxBoard as c " +
                "LEFT JOIN (SELECT DISTINCT ComplainBoxId FROM ComplainBoxVote) as cv " +
                    "ON c.ComplainBoxId = cv.ComplainBoxId " +
                "INNER JOIN ComplainCategory as cc " +
                    "ON cc.CategoryId = c.CategoryId";

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            complainBoxObservableList.add(new ComplainBox(rs.getInt("ComplainBoxId"),rs.getString("FlatNumber"),
                    rs.getString("ComplainBoxStatus"),rs.getString("ComplainBoxTitle"),
                    rs.getString("ComplainBoxDescription"), rs.getString("DateAdded"),
                    rs.getInt("VoteUp"), rs.getInt("VoteDown"),rs.getString("CategoryName")));
        }
        databaseHandler.getDbConnection().close();
    }
}
