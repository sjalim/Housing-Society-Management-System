package sample.controllers.resident;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.UserId;
import sample.database.DatabaseHandler;
import sample.models.ComplainBox;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AddComplaintController implements Initializable {

    @FXML
    private JFXTextArea addComplaintDes;

    @FXML
    private JFXTextField addComplaintTitle;

    @FXML
    private JFXButton postComplaintButton;

    @FXML
    private JFXComboBox<ComplainBox> categoryComboBox;

    private final static String UNSOLVED = "Unsolved";
    private String query=null;
    private DatabaseHandler databaseHandler;
    private ObservableList<ComplainBox> categoryList;
    UserId mUserId = UserId.getInstance();
    ComplainBox selectedCategory;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        categoryList = FXCollections.observableArrayList();

        categoryComboBox.setOnMouseClicked((mouseEvent) -> {
            try {
                initCategory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        categoryComboBox.setOnAction(actionEvent -> {
            selectedCategory = categoryComboBox.getSelectionModel().getSelectedItem();
        });

        postComplaintButton.setOnAction(actionEvent -> {
            if(selectedCategory==null){
                return;
            }else {
                try {
                    postComplaint(selectedCategory);
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
            Stage stage = (Stage) postComplaintButton.getScene().getWindow();
            stage.close();
        });
    }

    private void postComplaint(ComplainBox selectedCategory) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        if (addComplaintTitle.getText() == null ||
                addComplaintDes.getText() == null || categoryComboBox.getValue() == null){
            AlertDialog("Fill all the fields");
        } else {
            String insert = "INSERT INTO ComplainBoxBoard (ComplainBoxStatus,ComplainBoxTitle,ComplainBoxDescription,FlatNumber,CategoryId) " +
                    "VALUES(?,?,?,?,?)";
            PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(insert);
            ps.setString(1,UNSOLVED);
            ps.setString(2,addComplaintTitle.getText());
            ps.setString(3,addComplaintDes.getText());
            ps.setString(4,mUserId.mId);
            ps.setInt(5,selectedCategory.getCategoryId());
            ps.executeUpdate();
            ps.close();
            databaseHandler.getDbConnection().close();
        }
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

    void AlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
