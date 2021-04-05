package sample.controllers.resident;

import com.jfoenix.controls.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import sample.UserId;
import sample.controllers.manager.complaintbox.ManagerCustomComplaintRowController;
import sample.controllers.manager.noticeboard.NoticeRowController;
import sample.database.DatabaseHandler;
import sample.models.ComplainBox;
import sample.models.Notice;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ComplaintBoxController implements Initializable {

    @FXML private AnchorPane complaintBoxPane;

    @FXML private JFXComboBox<ComplainBox> categoryComboBox;

    @FXML private JFXTextField filteredComplaints;

    @FXML private JFXButton submitComplainButton;

    @FXML private JFXButton refreshButton;

    @FXML private JFXDatePicker fromDatePicker;

    @FXML private JFXDatePicker toDatePicker;

    @FXML private JFXButton findButton;

    @FXML private JFXListView<ComplainBox> complaintBoxListView;

    @FXML private JFXButton searchButton;

    private String query;
    private DatabaseHandler databaseHandler;
    private ObservableList<ComplainBox> complainBoxObservableList;
    private ObservableList<ComplainBox> categoryList;
    private ObservableList<ComplainBox> filteredByDateList;
    UserId mUserId = UserId.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        fromDatePicker.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
        toDatePicker.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });

        Timeline refreshTableTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        event -> {
                            complainBoxObservableList = FXCollections.observableArrayList();
                            categoryList = FXCollections.observableArrayList();
                            filteredByDateList = FXCollections.observableArrayList();
                            try {
                                loadFromDB();
                            } catch (SQLException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            }

                            complaintBoxListView.setItems(complainBoxObservableList);
                            complaintBoxListView.setCellFactory(CustomRowComplaintController
                                    -> new CustomRowComplaintController());

                        }));

        refreshTableTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTableTimeline.play();

        searchButton.setOnAction(actionEvent -> {
            refreshTableTimeline.stop();
            if(filteredComplaints.getText() != null) {
                try {
                    filterBySearch(filteredComplaints.getText());
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
                complaintBoxListView.setItems(null);
                complaintBoxListView.setItems(complainBoxObservableList);
            } else {
                AlertDialog("Search field is blank!");
            }
        });

        findButton.setOnAction(actionEvent -> {
            if (fromDatePicker.getValue() != null && toDatePicker.getValue() != null) {
                refreshTableTimeline.stop();
                String fDate = fromDatePicker.getValue().toString();
                String tDate = toDatePicker.getValue().toString();
                try {
                    filterByDate(fDate,tDate);
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
                complaintBoxListView.setItems(null);
                complaintBoxListView.setItems(filteredByDateList);
            } else {
                AlertDialog("Select all the properties");
            }
        });

        refreshButton.setOnAction(actionEvent -> {
            refreshTableTimeline.play();
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
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        categoryComboBox.setOnAction((event) -> {
            refreshTableTimeline.stop();
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

        submitComplainButton.setOnAction(actionEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/sample/views/resident/add_complaint.fxml"));
                loader.load();
                AddComplaintController addComplaintController = loader.getController();
                Scene scene = new Scene(loader.getRoot());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Submit Your Complain");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void filterBySearch(String searchedTerm) throws SQLException, ClassNotFoundException {
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
                    "ON cc.CategoryId = c.CategoryId " +
                "WHERE FlatNumber LIKE '%"+searchedTerm+"%' " +
                    "OR ComplainBoxTitle LIKE '%"+searchedTerm+"%'";

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            complainBoxObservableList.add(new ComplainBox(rs.getInt("ComplainBoxId"),rs.getString("FlatNumber"),
                    rs.getString("ComplainBoxStatus"),rs.getString("ComplainBoxTitle"),
                    rs.getString("ComplainBoxDescription"), rs.getString("DateAdded"),
                    rs.getInt("VoteUp"), rs.getInt("VoteDown"),rs.getString("CategoryName")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void filterByDate(String fDate, String tDate) throws SQLException, ClassNotFoundException {
        filteredByDateList.clear();
        Statement st = databaseHandler.getDbConnection().createStatement();
        query = "SELECT c.ComplainBoxId, c.FlatNumber, c.ComplainBoxStatus, c.ComplainBoxTitle, cc.CategoryName, c.ComplainBoxDescription, c.DateAdded," +
                    "(SELECT COUNT(*) from ComplainBoxVote where VoteFlag = '1' and ComplainBoxId = cv.ComplainBoxId) as VoteUp," +
                    "(SELECT COUNT(*) from ComplainBoxVote where VoteFlag = '0' and ComplainBoxId = cv.ComplainBoxId) as VoteDown " +
                "FROM ComplainBoxBoard as c " +
                "LEFT JOIN (SELECT DISTINCT ComplainBoxId FROM ComplainBoxVote) as cv " +
                    "ON c.ComplainBoxId = cv.ComplainBoxId " +
                "INNER JOIN ComplainCategory as cc " +
                    "ON cc.CategoryId = c.CategoryId " +
                "WHERE DateAdded BETWEEN '"+fDate+"'AND '"+tDate+"' ORDER BY ComplainBoxId desc";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            filteredByDateList.add(new ComplainBox(rs.getInt("ComplainBoxId"),rs.getString("FlatNumber"),
                    rs.getString("ComplainBoxStatus"),rs.getString("ComplainBoxTitle"),
                    rs.getString("ComplainBoxDescription"), rs.getString("DateAdded"),
                    rs.getInt("VoteUp"), rs.getInt("VoteDown"),rs.getString("CategoryName")));
        }
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

    void AlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
