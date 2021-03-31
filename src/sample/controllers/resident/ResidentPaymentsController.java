package sample.controllers.resident;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.UserId;
import sample.controllers.manager.collectpayments.ManagerViewPaymentDetailsController;
import sample.database.DatabaseHandler;
import sample.models.Payments;

import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ResidentPaymentsController implements Initializable {

    @FXML private JFXComboBox<String> searchComboBox;

    @FXML private JFXButton searchButton;

    @FXML private JFXComboBox<Payments> typeComboBox;

    @FXML private JFXComboBox<Payments> yearComboBox;

    @FXML private JFXComboBox<Payments> monthComboBox;

    @FXML private JFXButton findHistoryButton;

    @FXML private JFXButton refreshButton;

    @FXML private TableView<Payments> paymentTableView;

    @FXML private TableColumn<Payments, Integer> table_paymentId;

    @FXML private TableColumn<Payments, String> table_type;

    @FXML private TableColumn<Payments, String> table_flat;

    @FXML private TableColumn<Payments, Double> table_amount;

    @FXML private TableColumn<Payments, Date> table_addedOn;

    @FXML private TableColumn<Payments, String> table_paymentMethod;

    @FXML private TableColumn<Payments, String> table_paymentStatus;

    @FXML private TableColumn<Payments, Date> table_paidDate;

    private final static String PAYMENT_DUE = "Due";
    private final static String PAYMENT_RECEIVED = "Received";
    private final static String DEPOSIT = "Deposit";
    private final static String NOT_PAID = "Not Paid Yet";
    UserId mUserId = UserId.getInstance();
    private String query=null;
    private DatabaseHandler databaseHandler;
    private ObservableList<Payments> paymentsObservableList;
    private ObservableList<Payments> typeList;
    private ObservableList<Payments> monthList;
    private ObservableList<Payments> yearList;
    private ObservableList<Payments> paymentByHistoryList;
    private ObservableList<Payments> searchList;
    private final String[] search_options = {"Due","Received"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSearchOptions();
        Timeline refreshTableTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            paymentsObservableList = FXCollections.observableArrayList();
                            typeList = FXCollections.observableArrayList();
                            monthList = FXCollections.observableArrayList();
                            yearList = FXCollections.observableArrayList();
                            paymentByHistoryList = FXCollections.observableArrayList();
                            searchList = FXCollections.observableArrayList();

                            try {
                                loadFromDatabase();
                                initTable();
                            } catch (SQLException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            }
                            paymentTableView.setItems(paymentsObservableList);
                        }));

        refreshTableTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTableTimeline.play();

        paymentTableView.setOnMouseClicked((MouseEvent event) -> {
            Payments row = paymentTableView.getSelectionModel().getSelectedItem();
            if(event.getClickCount() == 2) {
                if(row==null)
                    return;
                try {
                    showPaymentDetails(refreshTableTimeline);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        paymentTableView.setRowFactory(tv -> new TableRow<Payments>() {
            @Override
            protected void updateItem(Payments payments, boolean empty) {
                super.updateItem(payments, empty);
                if(payments == null) {
                    setStyle("");
                } else {
                    //Initiated
                    if(payments.getPaidDate() == null && payments.getPaymentMethod() == null
                            && payments.getPaymentStatus().equals(PAYMENT_DUE)) {
                        setStyle("-fx-background-color: #ecc67f;");
                        //On Hold
                    } else if (payments.getPaidDate() != null && payments.getPaymentMethod() != null
                            && payments.getPaymentStatus().equals(PAYMENT_DUE)) {
                        setStyle("-fx-background-color: #b3533a;");
                        //Completed
                    } else if (payments.getPaidDate() != null && payments.getPaymentMethod() != null
                            && payments.getPaymentStatus().equals(PAYMENT_RECEIVED)) {
                        setStyle("-fx-background-color: #4d97d6;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        searchButton.setOnAction(actionEvent -> {
            refreshTableTimeline.stop();
            if(searchComboBox.getValue()==null){
                AlertDialog("Select Payment Status");
            } else {
                try {
                    searchByStatus();
                    paymentTableView.setItems(null);
                    paymentTableView.setItems(searchList);
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }

        });

        typeComboBox.setOnMouseClicked((mouseEvent) -> {
            try {
                initTypeComboBox();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        yearComboBox.setOnMouseClicked((mouseEvent) -> {
            if(typeComboBox.getValue()==null){
                AlertDialog("Select type");
            } else {
                try {
                    initYearComboBox(typeComboBox.getValue().toString());
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        monthComboBox.setOnMouseClicked((mouseEvent) -> {
            if(yearComboBox.getValue()==null){
                AlertDialog("Select year");
            } else {
                try {
                    initMonthComboBox(yearComboBox.getValue().toString());
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        findHistoryButton.setOnAction(actionEvent -> {
            refreshTableTimeline.stop();
            if(typeComboBox.getValue() == null || yearComboBox.getValue() == null
                    || monthComboBox.getValue() == null) {
                AlertDialog("Select all the fields");
            } else {
                try {
                    filteredByHistory();
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
                paymentTableView.setItems(null);
                paymentTableView.setItems(paymentByHistoryList);
            }
        });

        refreshButton.setOnAction(actionEvent -> {
            refreshTableTimeline.play();
        });

    }
    private void showPaymentDetails(Timeline refreshTableTimeline) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(this.getClass().getResource("/sample/views/resident/resident_view_payments.fxml"));
        loader.load();
        ResidentViewPaymentsController residentViewPaymentsController = loader.getController();
        editSelectedRow(residentViewPaymentsController); //Edit table row
        Scene scene = new Scene(loader.getRoot());
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Payment Details");
//        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.setOnCloseRequest(e -> {
            System.out.println("Payment Window Disconnected");
            refreshTableTimeline.play();
        });
    }

    private void editSelectedRow(ResidentViewPaymentsController residentViewPaymentsController) throws IOException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Payments selectedRow = paymentTableView.getSelectionModel().getSelectedItem();

//        managerViewPaymentDetailsController.despositSlipImageView.setVisible(false);

        residentViewPaymentsController.paymentId_details.setText(String.valueOf(selectedRow.getPaymentId()));
        residentViewPaymentsController.flatNumber_details.setText(selectedRow.getFlatNumber());
        residentViewPaymentsController.paymentType_details.setText(selectedRow.getPaymentType());
        residentViewPaymentsController.amount_details.setText(String.valueOf(selectedRow.getAmount()));
        residentViewPaymentsController.addedOn_details.setText(dateFormat.format(selectedRow.getDateAdded()));
        residentViewPaymentsController.paymentStatus_details.setText(selectedRow.getPaymentStatus());

        if(selectedRow.getPaidDate() != null) {
            residentViewPaymentsController.confirmPaymentButton.setDisable(true);
            residentViewPaymentsController.paymentMethod_ComboBox.setVisible(false);
            residentViewPaymentsController.paymentMethod_details.setText(selectedRow.getPaymentMethod());
            residentViewPaymentsController.paidDate_details.setText(dateFormat.format(selectedRow.getPaidDate()));
        } else {
            residentViewPaymentsController.paymentMethod_details.setVisible(false);
            residentViewPaymentsController.paymentMethod_ComboBox.setVisible(true);
        }

        if(selectedRow.getPaymentMethod() != null && selectedRow.getPaymentMethod().equals(DEPOSIT)) {
            residentViewPaymentsController.paymentMethod_details.setText(selectedRow.getPaymentMethod());
            residentViewPaymentsController.despositSlipImageView.setVisible(true);
            residentViewPaymentsController.selectImage.setDisable(true);
            residentViewPaymentsController.enterTransactionId.setDisable(true);

            BufferedImage img = ImageIO.read(new ByteArrayInputStream(selectedRow.getDepositSlip()));
            Image image = SwingFXUtils.toFXImage(img, null);
            residentViewPaymentsController.despositSlipImageView.setImage(image);

        } else if(selectedRow.getPaymentMethod() != null && !selectedRow.getPaymentMethod().equals(DEPOSIT)) {
            residentViewPaymentsController.enterTransactionId.setText(selectedRow.getTransactionId());
            residentViewPaymentsController.enterTransactionId.setEditable(false);
            residentViewPaymentsController.selectImage.setDisable(true);
        } else {
            residentViewPaymentsController.enterTransactionId.setEditable(true);
            residentViewPaymentsController.selectImage.setDisable(false);
        }

    }

    private void searchByStatus() throws SQLException, ClassNotFoundException {
        searchList.clear();
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();
        query = "SELECT ct.PaymentId, pt.Ptype, ct.FlatNumber, ct.Amount, ct.DateAdded, ct.PaymentStatus, " +
                    "pm.Pmethod, ct.PaidDate, onTran.TransactionId, offTran.DepositSlip " +
                "FROM CollectPayments as ct " +
                "INNER JOIN PaymentType as pt " +
                    "ON ct.PtypeId = pt.PtypeId " +
                "LEFT JOIN PaymentMethod as pm " +
                    "ON ct.PmId = pm.PmId " +
                "LEFT JOIN OnlineTransaction as onTran " +
                    "ON ct.PaymentId = onTran.PaymentId " +
                "LEFT JOIN OfflineTransaction as offTran " +
                    "ON ct.PaymentId = offTran.PaymentId " +
                "WHERE ct.PaymentStatus='"+searchComboBox.getValue()+"' " +
                    "AND ct.FlatNumber='"+mUserId.mId+"'";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            searchList.add(new Payments(rs.getInt("PaymentId"),rs.getString("Ptype"),
                    rs.getString("FlatNumber"), rs.getDouble("Amount"),rs.getDate("DateAdded"),
                    rs.getString("PaymentStatus"),rs.getString("Pmethod"),
                    rs.getDate("PaidDate"),rs.getString("TransactionId"),rs.getBytes("DepositSlip")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void filteredByHistory() throws SQLException, ClassNotFoundException {
        String type = typeComboBox.getValue().toString();
        String year = yearComboBox.getValue().toString();
        String month = String.valueOf(getMonthNumber(monthComboBox.getValue().toString()));
        paymentByHistoryList.clear();
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();
        query = "SELECT ct.PaymentId, pt.Ptype, ct.FlatNumber, ct.Amount, ct.DateAdded, ct.PaymentStatus, " +
                "pm.Pmethod, ct.PaidDate, onTran.TransactionId, offTran.DepositSlip " +
                "FROM CollectPayments as ct " +
                "INNER JOIN PaymentType as pt " +
                "ON ct.PtypeId = pt.PtypeId " +
                "LEFT JOIN PaymentMethod as pm " +
                "ON ct.PmId = pm.PmId " +
                "LEFT JOIN OnlineTransaction as onTran " +
                "ON ct.PaymentId = onTran.PaymentId " +
                "LEFT JOIN OfflineTransaction as offTran " +
                "ON ct.PaymentId = offTran.PaymentId " +
                "WHERE MONTH(ct.DateAdded) ='"+month+"' " +
                "AND YEAR(ct.DateAdded) ='"+year+"' AND pt.Ptype='"+type+"' " +
                "AND ct.FlatNumber='"+mUserId.mId+"'";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            paymentByHistoryList.add(new Payments(rs.getInt("PaymentId"),rs.getString("Ptype"),
                    rs.getString("FlatNumber"), rs.getDouble("Amount"),rs.getDate("DateAdded"),
                    rs.getString("PaymentStatus"),rs.getString("Pmethod"),
                    rs.getDate("PaidDate"),rs.getString("TransactionId"),rs.getBytes("DepositSlip")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void initMonthComboBox(String year) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        monthList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT DISTINCT DATEPART(MM, cp.DateAdded) as 'Month' " +
                "FROM CollectPayments as cp " +
                "WHERE DATEPART(yyyy, cp.DateAdded) = DATEPART(yyyy, '"+year+"') " +
                "AND cp.FlatNumber='"+mUserId.mId+"'";

        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            monthList.add(new Payments(getMonth(Integer.parseInt(rs.getString("Month")))));
        }
        databaseHandler.getDbConnection().close();
        monthComboBox.setItems(monthList);
    }

    private void initYearComboBox(String type) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        yearList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT DISTINCT DATEPART(yyyy, cp.DateAdded) as 'Year' " +
                "FROM CollectPayments as cp " +
                "INNER JOIN PaymentType as pt " +
                "ON cp.PtypeId = pt.PtypeId " +
                "WHERE pt.Ptype ='"+type+"' AND cp.FlatNumber='"+mUserId.mId+"'";

        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            yearList.add(new Payments(rs.getString("Year")));
        }
        databaseHandler.getDbConnection().close();
        yearComboBox.setItems(yearList);
    }

    private void initTypeComboBox() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        typeList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT DISTINCT pt.PtypeId, pt.Ptype " +
                "FROM CollectPayments as cp " +
                "INNER JOIN PaymentType as pt " +
                "ON cp.PtypeId = pt.PtypeId WHERE cp.FlatNumber='"+mUserId.mId+"'";

        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            typeList.add(new Payments(rs.getInt("PtypeId"),rs.getString("Ptype")));
        }
        databaseHandler.getDbConnection().close();
        typeComboBox.setItems(typeList);
    }

    private void initTable() {
        table_paymentId.setCellValueFactory(new PropertyValueFactory<>("paymentId"));
        table_type.setCellValueFactory(new PropertyValueFactory<>("paymentType"));
        table_flat.setCellValueFactory(new PropertyValueFactory<>("flatNumber"));
        table_amount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        table_addedOn.setCellValueFactory(new PropertyValueFactory<>("dateAdded"));
        table_paymentMethod.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        table_paymentStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
        table_paidDate.setCellValueFactory(new PropertyValueFactory<>("paidDate"));
    }

    private void initSearchOptions() {
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, search_options);
        //convert list to observable list
        ObservableList observableList = FXCollections.observableArrayList(list);
        searchComboBox.setItems(observableList);
    }

    private void loadFromDatabase() throws SQLException, ClassNotFoundException {
        paymentsObservableList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT ct.PaymentId, pt.Ptype, ct.FlatNumber, ct.Amount, ct.DateAdded, ct.PaymentStatus, " +
                    "pm.Pmethod, ct.PaidDate, onTran.TransactionId, offTran.DepositSlip " +
                "FROM CollectPayments as ct " +
                "INNER JOIN PaymentType as pt " +
                    "ON ct.PtypeId = pt.PtypeId " +
                "LEFT JOIN PaymentMethod as pm " +
                    "ON ct.PmId = pm.PmId " +
                "LEFT JOIN OnlineTransaction as onTran " +
                    "ON ct.PaymentId = onTran.PaymentId " +
                "LEFT JOIN OfflineTransaction as offTran " +
                    "ON ct.PaymentId = offTran.PaymentId " +
                "WHERE DATEPART(mm, ct.DateAdded) = DATEPART(mm, GETDATE()) " +
                    "AND DATEPART(yyyy, ct.DateAdded) = DATEPART(yyyy, GETDATE()) " +
                    "AND ct.FlatNumber='"+mUserId.mId+"'";

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            paymentsObservableList.add(new Payments(rs.getInt("PaymentId"),rs.getString("Ptype"),
                    rs.getString("FlatNumber"), rs.getDouble("Amount"),rs.getDate("DateAdded"),
                    rs.getString("PaymentStatus"),rs.getString("Pmethod"),
                    rs.getDate("PaidDate"),rs.getString("TransactionId"),rs.getBytes("DepositSlip")));
        }
        databaseHandler.getDbConnection().close();
    }

    private String getMonth(int month) {
        return new DateFormatSymbols().getMonths()[month-1];
    }
    private int getMonthNumber(String monthName) {
        return Month.valueOf(monthName.toUpperCase()).getValue();
    }

    void AlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
