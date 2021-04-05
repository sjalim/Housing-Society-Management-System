package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class GuardChangePasswordController implements Initializable {

    @FXML
    private JFXButton updatePassButton;

    @FXML
    private JFXPasswordField oldPassField;

    @FXML
    private JFXPasswordField newPassField;

    private DatabaseHandler databaseHandler;
    String query = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Preferences userPreferences = Preferences.userRoot();
        String userId = userPreferences.get(LoginPageController.USER_ID, "root");
        updatePassButton.setOnAction(actionEvent -> {
            try {
                updatePassword(userId);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            Stage stage = (Stage) updatePassButton.getScene().getWindow();
            stage.close();
        });
    }

    private void updatePassword(String mId) throws SQLException, ClassNotFoundException {
        int guardId = 0;
        String password = null;
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();
        query = "SELECT * FROM Guard WHERE Mobile='"+mId+"'";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            guardId = rs.getInt("GuardId");
        }

        System.out.println(guardId);
        Statement statement2 = databaseHandler.getDbConnection().createStatement();
        query = "SELECT Password FROM GuardLogin WHERE GuardId='"+guardId+"'";
        ResultSet rs2 = statement2.executeQuery(query);
        while(rs2.next()) {
            password = rs2.getString("Password");
        }

        if(oldPassField.getText()==null || newPassField.getText()==null) {
            AlertDialog("Fill both fields");
        } else {
            if(oldPassField.getText().equals(password)){
                query = "UPDATE GuardLogin SET Password = '"+newPassField.getText()+"' WHERE GuardId = '"+guardId+"'";

                PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(query);
                ps.executeUpdate();
                ps.close();
                databaseHandler.getDbConnection().close();
            } else {
                AlertDialog("Old password is wrong");
            }
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
