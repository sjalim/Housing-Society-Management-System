package sample.controllers.manager.collectpayments;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.models.Payments;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class PaymentAddNewMethodController implements Initializable {

    @FXML
    private JFXTextField newPaymentMethodField;

    @FXML
    private JFXButton addPaymentMethod;

    @FXML
    private JFXComboBox<Payments> paymentMethodComboBox;

    private DatabaseHandler databaseHandler;
    private ObservableList<Payments> methodList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        methodList = FXCollections.observableArrayList();
        addPaymentMethod.setOnAction(actionEvent -> {
            try {
                insertPaymentMethod();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        paymentMethodComboBox.setOnMouseClicked((mouseEvent) -> {
            try {
                initPaymentMethodComboBox();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    private void initPaymentMethodComboBox() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        methodList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        String query = "SELECT * FROM PaymentMethod";

        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            methodList.add(new Payments(rs.getInt("PmId"),rs.getString("Pmethod")));
        }
        databaseHandler.getDbConnection().close();
        paymentMethodComboBox.setItems(methodList);
    }

    private void insertPaymentMethod() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        if (newPaymentMethodField.getText() == null){
            AlertDialog("No input found");
        } else {
            String query = "INSERT INTO PaymentMethod (Pmethod) " +
                    "VALUES(?)";
            PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(query);
            ps.setString(1,newPaymentMethodField.getText());
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
