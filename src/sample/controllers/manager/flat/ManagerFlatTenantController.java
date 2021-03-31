package sample.controllers.manager.flat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import sample.database.DatabaseHandler;
import sample.models.FlatOwner;
import sample.models.Tenant;

import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerFlatTenantController implements Initializable {

    @FXML
    private AnchorPane tenant_pane;

    @FXML private JFXButton addButton;

    @FXML private JFXButton editButton;

    @FXML private JFXTextField filteredTenant;

    @FXML private JFXButton refreshButton;

    @FXML private TableView<Tenant> tenant_tableView;

    @FXML private TableColumn<Integer, Tenant> table_tenantId;

    @FXML private TableColumn<String, Tenant> table_flatNo;

    @FXML private TableColumn<String, Tenant> table_tenantName;

    @FXML private TableColumn<String, Tenant> table_mobile;

    @FXML private TableColumn<String, Tenant> table_presAdd;

    @FXML private TableColumn<String, Tenant> table_permAdd;

    @FXML private TableColumn<String, Tenant> table_nid;

    @FXML private TableColumn<String, Tenant> table_occupation;

    @FXML private TableColumn<Integer, Tenant> table_familyMember;

    @FXML private TableColumn<Date, Tenant> table_moveIn;

    @FXML private TableColumn<Date, Tenant> table_moveOut;

    @FXML private JFXDatePicker fromDate, toDate;

    @FXML private JFXButton findButton;

    @FXML private JFXComboBox<String> movingTypeComboBox;

    @FXML private JFXComboBox<String> searchComboBox;

    @FXML private JFXButton searchButton;

    private final String[] search_options = {"Flat No","Name","Mobile","NID"};
    private final String[] movingType = {"MoveIn","MoveOut"};
    private String query;
    private boolean isRowSelected = false;
    private DatabaseHandler databaseHandler = new DatabaseHandler();
    private ObservableList<Tenant> tenantObservableList;
    private ObservableList<Tenant> typeObservableList;
    private ObservableList<Tenant> searchList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initSearchOptions();
        initMovingType();
        fromDate.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
        toDate.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });

        Timeline refreshTableTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            System.out.println("this is called every 1 seconds on Tenant thread");
                            tenantObservableList = FXCollections.observableArrayList();
                            typeObservableList = FXCollections.observableArrayList();
                            searchList = FXCollections.observableArrayList();
                            initTable();
                            try {
                                loadFromDatabase();
                            } catch (SQLException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            }
                            tenant_tableView.setItems(tenantObservableList);
                        }));
        refreshTableTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTableTimeline.play();

        filteredTenant.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                refreshTableTimeline.stop();
                System.out.println("Search bar clicked");
            }
        });

        tenant_tableView.setRowFactory( tv -> {
            TableRow<Tenant> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                refreshTableTimeline.stop();
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    refreshTableTimeline.stop();
                    System.out.println("SELECTED");
                    isRowSelected = true;
                }
            });
            return row ;
        });

        findButton.setOnAction(actionEvent -> {
            if (fromDate.getValue() != null && toDate.getValue() != null && movingTypeComboBox.getSelectionModel().getSelectedItem() != null) {
                refreshTableTimeline.stop();
                String mType = movingTypeComboBox.getSelectionModel().getSelectedItem();
                String fDate = fromDate.getValue().toString();
                String tDate = toDate.getValue().toString();

                try {
                    filterByDate(fDate,tDate,mType);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                tenant_tableView.setItems(null);
                tenant_tableView.setItems(typeObservableList);
            } else {
                AlertDialog("Select all the properties");
            }
        });

        refreshButton.setOnAction(actionEvent -> {
            try {
                loadFromDatabase();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            refreshTableTimeline.play();
        });

        editButton.setOnAction(actionEvent -> {
            if(isRowSelected){
                showTenantEditView(refreshTableTimeline);
            }else {
                AlertDialog("Select a row");
            }
        });

        addButton.setOnAction(actionEvent -> {
            showTenantAddView();
        });

        searchButton.setOnAction(actionEvent -> {
            refreshTableTimeline.stop();
            try {
                if(searchComboBox.getValue().equals("Flat No")) {
                    searchByFlat();
                    tenant_tableView.setItems(null);
                    tenant_tableView.setItems(searchList);
                } else if (searchComboBox.getValue().equals("Name")) {
                    searchByName();
                    tenant_tableView.setItems(null);
                    tenant_tableView.setItems(searchList);
                } else if (searchComboBox.getValue().equals("Mobile")) {
                    searchByMobile();
                    tenant_tableView.setItems(null);
                    tenant_tableView.setItems(searchList);
                } else if (searchComboBox.getValue().equals("NID")) {
                    searchByNid();
                    tenant_tableView.setItems(null);
                    tenant_tableView.setItems(searchList);
                } else {
                    AlertDialog("Select search by option");
                }
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }

        });
    }

    private void searchByFlat() throws SQLException, ClassNotFoundException {
        searchList.clear();
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();

        query = "SELECT * FROM Tenant as t " +
                "INNER JOIN Tenant_Info as tInfo ON t.tenantId = tInfo.tenantId " +
                "WHERE t.FlatNumber='"+filteredTenant.getText()+"'";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            searchList.add(new Tenant(rs.getInt("TenantId"),rs.getString("FlatNumber"),
                    rs.getString("TenantName"), rs.getString("Mobile"),
                    rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getString("Occupation"),rs.getInt("TotalFamilyMembers"),
                    rs.getDate("MoveIn"),rs.getDate("MoveOut")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void searchByName() throws SQLException, ClassNotFoundException {
        searchList.clear();
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();

        query = "SELECT * FROM Tenant as t " +
                "INNER JOIN Tenant_Info as tInfo ON t.tenantId = tInfo.tenantId " +
                "WHERE t.TenantName='"+filteredTenant.getText()+"'";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            searchList.add(new Tenant(rs.getInt("TenantId"),rs.getString("FlatNumber"),
                    rs.getString("TenantName"), rs.getString("Mobile"),
                    rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getString("Occupation"),rs.getInt("TotalFamilyMembers"),
                    rs.getDate("MoveIn"),rs.getDate("MoveOut")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void searchByMobile() throws SQLException, ClassNotFoundException {
        searchList.clear();
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();

        query = "SELECT * FROM Tenant as t " +
                "INNER JOIN Tenant_Info as tInfo ON t.tenantId = tInfo.tenantId " +
                "WHERE tInfo.Mobile='"+filteredTenant.getText()+"'";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            searchList.add(new Tenant(rs.getInt("TenantId"),rs.getString("FlatNumber"),
                    rs.getString("TenantName"), rs.getString("Mobile"),
                    rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getString("Occupation"),rs.getInt("TotalFamilyMembers"),
                    rs.getDate("MoveIn"),rs.getDate("MoveOut")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void searchByNid() throws SQLException, ClassNotFoundException {
        searchList.clear();
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();

        query = "SELECT * FROM Tenant as t " +
                "INNER JOIN Tenant_Info as tInfo ON t.tenantId = tInfo.tenantId " +
                "WHERE tInfo.Nid='"+filteredTenant.getText()+"'";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            searchList.add(new Tenant(rs.getInt("TenantId"),rs.getString("FlatNumber"),
                    rs.getString("TenantName"), rs.getString("Mobile"),
                    rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getString("Occupation"),rs.getInt("TotalFamilyMembers"),
                    rs.getDate("MoveIn"),rs.getDate("MoveOut")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void filterByDate(String fDate, String tDate, String mType) throws SQLException, ClassNotFoundException {
        typeObservableList.clear();
        Statement st = databaseHandler.getDbConnection().createStatement();
        query = "SELECT * FROM Tenant as t INNER JOIN Tenant_Info as tInfo ON t.tenantId = tInfo.tenantId " +
                "WHERE tInfo."+mType+" BETWEEN '"+fDate+"'AND '"+tDate+"';";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            System.out.println(rs.getString("TenantName"));
            typeObservableList.add(new Tenant(rs.getInt("TenantId"),rs.getString("FlatNumber"),
                    rs.getString("TenantName"), rs.getString("Mobile"),
                    rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getString("Occupation"),rs.getInt("TotalFamilyMembers"),
                    rs.getDate("MoveIn"),rs.getDate("MoveOut")));
        }
    }

    private void initSearchOptions() {
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, search_options);
        //convert list to observable list
        ObservableList observableList = FXCollections.observableArrayList(list);
        searchComboBox.setItems(observableList);
    }

    private void initTable() {
        table_tenantId.setCellValueFactory(new PropertyValueFactory<>("tenantId"));
        table_flatNo.setCellValueFactory(new PropertyValueFactory<>("flatNumber"));
        table_tenantName.setCellValueFactory(new PropertyValueFactory<>("tenantName"));
        table_mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        table_presAdd.setCellValueFactory(new PropertyValueFactory<>("presentAdd"));
        table_permAdd.setCellValueFactory(new PropertyValueFactory<>("permanentAdd"));
        table_nid.setCellValueFactory(new PropertyValueFactory<>("nid"));
        table_occupation.setCellValueFactory(new PropertyValueFactory<>("occupation"));
        table_familyMember.setCellValueFactory(new PropertyValueFactory<>("totalFamilyMember"));
        table_moveIn.setCellValueFactory(new PropertyValueFactory<>("moveIn"));
        table_moveOut.setCellValueFactory(new PropertyValueFactory<>("moveOut"));
    }

    private void loadFromDatabase() throws SQLException, ClassNotFoundException {
        tenantObservableList.clear();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT * FROM Tenant as t INNER JOIN Tenant_Info as tInfo ON t.tenantId = tInfo.tenantId";

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            tenantObservableList.add(new Tenant(rs.getInt("TenantId"),rs.getString("FlatNumber"),
                    rs.getString("TenantName"), rs.getString("Mobile"),
                    rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getString("Occupation"),rs.getInt("TotalFamilyMembers"),
                    rs.getDate("MoveIn"),rs.getDate("MoveOut")));
        }
    }

    private void initMovingType(){
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, movingType);
        //convert list to observable list
        ObservableList observableList = FXCollections.observableArrayList(list);
        movingTypeComboBox.setItems(observableList);
    }

    private void showTenantEditView(Timeline refreshTableTimeline) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/sample/views/manager/flat/manager_Tenant_Edit.fxml"));
            loader.load();
            ManagerTenantEditController managerTenantEditController = loader.getController();
            editSelectedRow(managerTenantEditController); //Edit table row
            Scene scene = new Scene(loader.getRoot());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Tenant Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            stage.setOnCloseRequest(e -> {
                System.out.println("Edited Window Disconnected");
                refreshTableTimeline.play();
                isRowSelected = false;
            });
            managerTenantEditController.cancel_EditButton.setOnAction(actionEvent -> {
                System.out.println("Edited Window Disconnected");
                refreshTableTimeline.play();
                stage.close();
                isRowSelected = false;
            });
            managerTenantEditController.save_EditButton.setOnAction(actionEvent -> {
                try {
                    managerTenantEditController.updateTenant();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                refreshTableTimeline.play();
                stage.close();
                isRowSelected = false;
                System.out.println("Data Saved");
            });

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editSelectedRow(ManagerTenantEditController managerTenantEditController) {
        Tenant selectedRow = tenant_tableView.getSelectionModel().getSelectedItem();

        managerTenantEditController.tenantId_EditField.setText(Integer.toString(selectedRow.getTenantId()));
        managerTenantEditController.flatNo_EditField.setText(selectedRow.getFlatNumber());
        managerTenantEditController.tenantName_EditField.setText(selectedRow.getTenantName());
        managerTenantEditController.nid_EditField.setText(selectedRow.getNid());
        managerTenantEditController.mobile_EditField.setText(selectedRow.getMobile());
        managerTenantEditController.prestAdd_EditField.setText(selectedRow.getPresentAdd());
        managerTenantEditController.permaAdd_EditField.setText(selectedRow.getPermanentAdd());
        managerTenantEditController.familyMember_EditField.setText(Integer.toString(selectedRow.getTotalFamilyMember()));
        managerTenantEditController.occupation_EditField.setText(selectedRow.getOccupation());
        managerTenantEditController.movedIn_DatePicker.setValue(selectedRow.getMoveIn().toLocalDate());
        if(selectedRow.getMoveOut() != null) {
            managerTenantEditController.movedOut_DatePicker.setValue(selectedRow.getMoveOut().toLocalDate());
        }
    }

    private void showTenantAddView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/sample/views/manager/flat/manager_Tenant_Add.fxml"));
            loader.load();
            Scene scene = new Scene(loader.getRoot());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Tenant Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        }catch (Exception e) {
            e.printStackTrace();
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
