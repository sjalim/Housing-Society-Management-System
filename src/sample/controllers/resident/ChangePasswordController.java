package sample.controllers.resident;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.UserId;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML private JFXPasswordField oldPassField;

    @FXML private JFXPasswordField newPassFIeld;

    @FXML private JFXButton updatePassButton;

    private DatabaseHandler databaseHandler;
    String query = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserId mUserId = UserId.getInstance();

        updatePassButton.setOnAction(actionEvent -> {
            try {
                updatePassword(mUserId.mId);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            Stage stage = (Stage) updatePassButton.getScene().getWindow();
            stage.close();
        });
    }

    private void updatePassword(String mId) throws SQLException, ClassNotFoundException {
        String password = null;
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();
        query = "SELECT * FROM FlatLogin WHERE FlatNumber='"+mId+"'";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            password = rs.getString("Password");
        }

        if(oldPassField.getText()==null || newPassFIeld.getText()==null) {
            AlertDialog("Fill both fields");
        } else {
            if(oldPassField.getText().equals(password)){
                query = "UPDATE FlatLogin SET Password = '"+newPassFIeld.getText()+"' WHERE FlatNumber = '"+mId+"'";

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
