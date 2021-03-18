package sample.controllers.manager.complaintbox;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddCategoryController implements Initializable {

    @FXML private JFXTextField newCategoryField;

    @FXML private JFXButton addButton;

    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addButton.setOnAction(actionEvent -> {
            try {
                insertCategory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
        });
    }

    private void insertCategory() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        if (newCategoryField.getText() == null){
            AlertDialog("No input found");
        } else {
            String insertPaymentType = "INSERT INTO ComplainCategory (CategoryName) " +
                    "VALUES(?)";
            PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(insertPaymentType);
            ps.setString(1,newCategoryField.getText());
            ps.executeUpdate();
            ps.close();
            databaseHandler.getDbConnection().close();
        }
    }

    void AlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
