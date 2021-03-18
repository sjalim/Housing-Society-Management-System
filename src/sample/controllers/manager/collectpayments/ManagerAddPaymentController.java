package sample.controllers.manager.collectpayments;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.models.FlatList;
import sample.models.FlatOwner;
import sample.models.Payments;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ManagerAddPaymentController implements Initializable {

    @FXML private JFXComboBox<Payments> newPaymentTypeCombo;

    @FXML private ImageView addNewTypeButton;

    @FXML private JFXButton flatListButton;

    @FXML private JFXTextField amountField;

    @FXML private JFXButton publishButton;

    String pTypeId = null;
    String query = null;
    private DatabaseHandler databaseHandler;
    private ObservableList<Payments> paymentTypeObsList;
    private ObservableList<FlatList> newPaymentsList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        paymentTypeObsList = FXCollections.observableArrayList();
        newPaymentsList = FXCollections.observableArrayList();
        try {
            initPaymentType();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        flatListButton.setOnAction(actionEvent -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/sample/views/manager/collectpayments/manager_payment_flatTableView.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ManagerPaymentFlatListController controller = loader.getController();
            controller.setManagerAddPaymentController(this); // Pass this controller to ManagerPaymentFlatListController
            Scene scene = new Scene(loader.getRoot());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Payment Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        });

        addNewTypeButton.setOnMouseClicked((mouseEvent) -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/sample/views/manager/collectpayments/payment_addNewType.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Scene scene = new Scene(loader.getRoot());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Payment Details");
            stage.show();
        });

        newPaymentTypeCombo.setOnMouseClicked((mouseEvent) -> {
            try {
                initPaymentType();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        publishButton.setOnAction(actionEvent -> {
            System.out.println("publishButton clicked");
            try {
                getPtypeId();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(newPaymentsList==null) {
                AlertDialog("No flat is selected");
            } else if(amountField.getText()==null) {
                AlertDialog("Amount is not set");
            } else if(pTypeId==null) {
                AlertDialog("Payment type is not set");
            }
            else {
                System.out.println("FROM AddPayment:  "+newPaymentsList.toString());
                try {
                    insertPaymentIntoDB();
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
            Stage stage = (Stage) publishButton.getScene().getWindow();
            stage.close();
        });
    }

    private void insertPaymentIntoDB() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        String insertPayment = "INSERT INTO CollectPayments (PtypeId,Amount,FlatNumber) " +
                "VALUES(?,?,?)";
        PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(insertPayment);
        for(FlatList flatList : newPaymentsList) {
            ps.setString(1,pTypeId);
            ps.setString(2,amountField.getText());
            ps.setString(3,flatList.getFlatNumber());
            ps.executeUpdate();
        }
        databaseHandler.getDbConnection().close();
    }

    //Getting PaymentTypeID and Type. Pass them to next Controller
    private void getPtypeId() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();
        query = "SELECT PtypeId FROM PaymentType WHERE Ptype='"+newPaymentTypeCombo.getValue()+"'";
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            pTypeId = String.valueOf(rs.getInt("PtypeId"));
        }
        databaseHandler.getDbConnection().close();
    }

    public void initPaymentType() throws SQLException, ClassNotFoundException {
        paymentTypeObsList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT PtypeId, Ptype FROM PaymentType";

        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            paymentTypeObsList.add(new Payments(rs.getInt("PtypeId"),rs.getString("Ptype")));
        }
        databaseHandler.getDbConnection().close();
        newPaymentTypeCombo.setItems(paymentTypeObsList);
    }

    public void loadFlatList(ObservableList<FlatList> list) {
        newPaymentsList = list;
    }

    void AlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
