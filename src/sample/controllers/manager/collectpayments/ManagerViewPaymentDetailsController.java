package sample.controllers.manager.collectpayments;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManagerViewPaymentDetailsController implements Initializable {

    private final static String PAYMENT_DUE = "Due";
    private final static String PAYMENT_RECEIVED = "Received";

    @FXML public Label paymentId_details;

    @FXML public Label flatNumber_details;

    @FXML public Label paymentType_details;

    @FXML public Label amount_details;

    @FXML public Label addedOn_details;

    @FXML public Label paymentStatus_details;

    @FXML public Label paymentMethod_details;

    @FXML public Label paidDate_details;

    @FXML public Label transId_details;

    @FXML public ImageView despositSlipImageView;

    @FXML public JFXButton verifyButton;

    @FXML public JFXButton notVerifyButton;

    private DatabaseHandler databaseHandler;
    String query = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void markAsVerified() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        //Query
        query = "UPDATE CollectPayments SET PaymentStatus ='"+PAYMENT_RECEIVED+"' WHERE CollectPayments.PaymentId ="+Integer.parseInt(paymentId_details.getText())+";";

        PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(query);
        ps.executeUpdate();
        databaseHandler.getDbConnection().close();
    }

    public void markAsNotVerified() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        //Query
        query = "UPDATE CollectPayments SET PaymentStatus ='"+PAYMENT_DUE+"' WHERE CollectPayments.PaymentId ="+Integer.parseInt(paymentId_details.getText())+";";

        PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(query);
        ps.executeUpdate();
        databaseHandler.getDbConnection().close();
    }
}
