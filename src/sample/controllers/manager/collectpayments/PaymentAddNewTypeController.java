package sample.controllers.manager.collectpayments;

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

public class PaymentAddNewTypeController implements Initializable {

    @FXML private JFXTextField newPaymentTypeField;

    @FXML private JFXButton addPaymentType;

    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addPaymentType.setOnAction(actionEvent -> {
            try {
                insertPaymentType();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            Stage stage = (Stage) addPaymentType.getScene().getWindow();
            stage.close();
        });
    }

    private void insertPaymentType() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        if (newPaymentTypeField.getText() == null){
            AlertDialog("No input found");
        } else {
            String insertPaymentType = "INSERT INTO PaymentType (Ptype) " +
                    "VALUES(?)";
            PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(insertPaymentType);
            ps.setString(1,newPaymentTypeField.getText());
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
