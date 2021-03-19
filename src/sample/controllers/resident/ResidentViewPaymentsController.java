package sample.controllers.resident;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.UserId;
import sample.database.DatabaseHandler;
import sample.models.Payments;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ResidentViewPaymentsController {

    @FXML public Label paymentId_details;

    @FXML public Label flatNumber_details;

    @FXML public Label paymentType_details;

    @FXML public Label amount_details;

    @FXML public Label addedOn_details;

    @FXML public Label paymentStatus_details;

    @FXML public JFXComboBox<Payments> paymentMethod_ComboBox;

    @FXML public Label paidDate_details,paymentMethod_details;

    @FXML public JFXTextField enterTransactionId;

    @FXML public JFXButton selectImage;

    @FXML public ImageView despositSlipImageView;

    @FXML public JFXButton confirmPaymentButton;

    private final static String DEPOSIT = "Deposit";
    UserId mUserId = UserId.getInstance();
    private DatabaseHandler databaseHandler;
    private String query = null;
    private Payments payments;
    private ObservableList<Payments> methodList;

    private FileInputStream fis;
    private File file;
    private Image image;

    @FXML
    public void initialize() {
        methodList = FXCollections.observableArrayList();

        paymentMethod_ComboBox.setOnMouseClicked((mouseEvent) -> {
            try {
                initMethodComboBox();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
        paymentMethod_ComboBox.setOnAction(actionEvent -> {
            payments = paymentMethod_ComboBox.getSelectionModel().getSelectedItem();
            if(payments != null) {
                if (payments.getPaymentType().equals(DEPOSIT)) {
                    System.out.println(payments.getPaymentMethod()+"  "+payments.getpType());
                enterTransactionId.setDisable(true);
                selectImage.setDisable(false);
                } else {
                enterTransactionId.setDisable(false);
                selectImage.setDisable(true);
                    System.out.println(payments.getPaymentType()+"  "+payments.getpType());
                }
            }
        });

        confirmPaymentButton.setOnAction(actionEvent -> {
            String paymentMethod = paymentMethod_ComboBox.getValue().toString();
            if(paymentMethod.equals(DEPOSIT)) {
                //Offline
                try {
                    offlineTransaction();
                } catch (SQLException | ClassNotFoundException | FileNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                //Online
                try {
                    onlineTransaction();
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
            Stage stage = (Stage) confirmPaymentButton.getScene().getWindow();
            stage.close();
        });
    }

    private void onlineTransaction() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        if (enterTransactionId.getText() == null){
            AlertDialog("Enter Transaction ID");
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            query = "INSERT INTO OnlineTransaction (PaymentId,TransactionId) " +
                    "VALUES(?,?)";
            PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(query);
            ps.setString(1,paymentId_details.getText());
            ps.setString(2,enterTransactionId.getText());
            ps.executeUpdate();
            ps.close();

            Statement statement = databaseHandler.getDbConnection().createStatement();
            int onlineId = 0;
            String query = "SELECT * FROM OnlineTransaction WHERE PaymentId='"+paymentId_details.getText()+"'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                onlineId = rs.getInt("OnlineId");
            }

            query = "UPDATE CollectPayments SET OnlineId='"+onlineId+"'," +
                        "PaidDate='"+dtf.format(now)+"',PmId='"+payments.getpType()+"' " +
                    "WHERE PaymentId='"+paymentId_details.getText()+"'";
            PreparedStatement ps1 = databaseHandler.getDbConnection().prepareStatement(query);
            ps1.executeUpdate();
            ps1.close();
            databaseHandler.getDbConnection().close();
        }
    }

    private void offlineTransaction() throws SQLException, ClassNotFoundException, FileNotFoundException {

        databaseHandler = new DatabaseHandler();
        if (file == null){
            AlertDialog("Select Deposit Slip");
        } else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            fis = new FileInputStream(file);

            query = "INSERT INTO OfflineTransaction (PaymentId,DepositSlip) " +
                    "VALUES(?,?)";
            PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(query);
            ps.setString(1,paymentId_details.getText());
            ps.setBinaryStream(2,(InputStream)fis,(int)file.length());
            ps.executeUpdate();
            ps.close();

            Statement statement = databaseHandler.getDbConnection().createStatement();
            int offlineId = 0;
            String query = "SELECT * FROM OfflineTransaction WHERE PaymentId='"+paymentId_details.getText()+"'";
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                offlineId = rs.getInt("OfflineId");
            }

            query = "UPDATE CollectPayments SET OfflineId='"+offlineId+"'," +
                    "PaidDate='"+dtf.format(now)+"',PmId='"+payments.getpType()+"' " +
                    "WHERE PaymentId='"+paymentId_details.getText()+"'";
            PreparedStatement ps1 = databaseHandler.getDbConnection().prepareStatement(query);
            ps1.executeUpdate();
            ps1.close();
            databaseHandler.getDbConnection().close();
        }

    }

    @FXML
    void selectImage(ActionEvent event) {
        FileChooser fc = new FileChooser();
        file = fc.showOpenDialog(null);
        if (file != null) {
            image = new Image(file.toURI().toString(),200,200,true,true);
            despositSlipImageView.setImage(image);
        }

    }

    private void initMethodComboBox() throws SQLException, ClassNotFoundException {
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
        paymentMethod_ComboBox.setItems(methodList);
    }

    void AlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
