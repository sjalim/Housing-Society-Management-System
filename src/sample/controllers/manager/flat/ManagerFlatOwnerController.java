package sample.controllers.manager.flat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.UserId;
import sample.database.DatabaseHandler;
import sample.models.FlatOwner;
import sample.models.Payments;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerFlatOwnerController implements Initializable{

    @FXML private TableView<FlatOwner> flatOwner_tableView;

    @FXML private TableColumn<FlatOwner, String> table_flatNo;

    @FXML private TableColumn<FlatOwner, String> table_ownerName;

    @FXML private TableColumn<FlatOwner, String> table_mobile;

    @FXML private TableColumn<FlatOwner, String> table_presAdd;

    @FXML private TableColumn<FlatOwner, String> table_permAdd;

    @FXML private TableColumn<FlatOwner, String> table_nid;

    @FXML private TableColumn<FlatOwner, Integer> table_allocatedParkNo;

    @FXML private TableColumn<FlatOwner, String> table_allocateStatus;

    @FXML private TableColumn<FlatOwner, Integer> table_vehicleOwned;

    @FXML private JFXButton addButton, editButton, refreshButton;
    @FXML private JFXTextField filteredFlatOwner;

    @FXML private JFXComboBox<String> searchComboBox;

    @FXML private JFXButton searchButton;

    private final String[] search_options = {"Flat No","Name","Mobile","NID","Flat Status"};
    private String query;
    private boolean isRowSelected = false;
    private DatabaseHandler databaseHandler;
    private ObservableList<FlatOwner> flatOwnerObservableList;
    private ObservableList<FlatOwner> searchList;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserId mUserId = UserId.getInstance();
        System.out.println(mUserId.mId+"  manager logged IN");
        initSearchOptions();
         Timeline refreshTableTimeline = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        event -> {
                            System.out.println("this is called every 1 seconds on UI thread");
                            flatOwnerObservableList = FXCollections.observableArrayList();
                            searchList = FXCollections.observableArrayList();
                            initTable();
                            try {
                                loadFromDatabase();
                            } catch (SQLException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            }
                            flatOwner_tableView.setItems(flatOwnerObservableList);
                        }));

        refreshTableTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTableTimeline.play();

        filteredFlatOwner.setOnMouseClicked((MouseEvent event) -> {
            if(event.getButton().equals(MouseButton.PRIMARY)){
                refreshTableTimeline.stop();
                System.out.println("Search bar clicked");
            }
        });

        flatOwner_tableView.setRowFactory( tv -> {
            TableRow<FlatOwner> row = new TableRow<>();
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

        refreshButton.setOnAction(actionEvent -> {
            refreshTableTimeline.play();
        });

        editButton.setOnAction(actionEvent -> {
            if(isRowSelected){
                showFlatEditView(refreshTableTimeline);
            }else {
                AlertDialog("Select a row");
            }
        });

        addButton.setOnAction(actionEvent -> {
            showFlatAddView();
        });

        searchButton.setOnAction(actionEvent -> {
            refreshTableTimeline.stop();
            try {
                if(searchComboBox.getValue().equals("Flat No")) {
                    searchByFlat();
                    flatOwner_tableView.setItems(null);
                    flatOwner_tableView.setItems(searchList);
                } else if (searchComboBox.getValue().equals("Name")) {
                    searchByName();
                    flatOwner_tableView.setItems(null);
                    flatOwner_tableView.setItems(searchList);
                } else if (searchComboBox.getValue().equals("Mobile")) {
                    searchByMobile();
                    flatOwner_tableView.setItems(null);
                    flatOwner_tableView.setItems(searchList);
                } else if (searchComboBox.getValue().equals("NID")) {
                    searchByNid();
                    flatOwner_tableView.setItems(null);
                    flatOwner_tableView.setItems(searchList);
                } else if (searchComboBox.getValue().equals("Flat Status")) {
                    searchByStatus();
                    flatOwner_tableView.setItems(null);
                    flatOwner_tableView.setItems(searchList);
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

        query = "SELECT f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                    "f2.PermanentAddress, COUNT(v.FlatNumber) as 'VehicleOwned' " +
                "FROM FlatOwner as f1 " +
                "INNER JOIN FlatOwner_Info as f2 " +
                    "ON f1.FlatNumber = f2.FlatNumber " +
                "LEFT JOIN Vehicle as v " +
                    "ON f1.FlatNumber = v.FlatNumber " +
                "WHERE f1.FlatNumber= '"+filteredFlatOwner.getText()+"'" +
                "GROUP BY f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                    "f2.PermanentAddress " +
                "ORDER BY f1.FlatNumber";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            searchList.add(new FlatOwner(rs.getString("FlatNumber"),rs.getString("OwnerName"),
                    rs.getString("Mobile"), rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getInt("AllocatedParkingSlot"),
                    rs.getString("AllocationStatus"),rs.getInt("VehicleOwned")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void searchByName() throws SQLException, ClassNotFoundException {
        searchList.clear();
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();

        query = "SELECT f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                "f2.PermanentAddress, COUNT(v.FlatNumber) as 'VehicleOwned' " +
                "FROM FlatOwner as f1 " +
                "INNER JOIN FlatOwner_Info as f2 " +
                "ON f1.FlatNumber = f2.FlatNumber " +
                "LEFT JOIN Vehicle as v " +
                "ON f1.FlatNumber = v.FlatNumber " +
                "WHERE f1.OwnerName= '"+filteredFlatOwner.getText()+"'" +
                "GROUP BY f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                "f2.PermanentAddress " +
                "ORDER BY f1.FlatNumber";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            searchList.add(new FlatOwner(rs.getString("FlatNumber"),rs.getString("OwnerName"),
                    rs.getString("Mobile"), rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getInt("AllocatedParkingSlot"),
                    rs.getString("AllocationStatus"),rs.getInt("VehicleOwned")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void searchByMobile() throws SQLException, ClassNotFoundException {
        searchList.clear();
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();

        query = "SELECT f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                "f2.PermanentAddress, COUNT(v.FlatNumber) as 'VehicleOwned' " +
                "FROM FlatOwner as f1 " +
                "INNER JOIN FlatOwner_Info as f2 " +
                "ON f1.FlatNumber = f2.FlatNumber " +
                "LEFT JOIN Vehicle as v " +
                "ON f1.FlatNumber = v.FlatNumber " +
                "WHERE f2.Mobile = '"+filteredFlatOwner.getText()+"'" +
                "GROUP BY f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                "f2.PermanentAddress " +
                "ORDER BY f1.FlatNumber";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            searchList.add(new FlatOwner(rs.getString("FlatNumber"),rs.getString("OwnerName"),
                    rs.getString("Mobile"), rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getInt("AllocatedParkingSlot"),
                    rs.getString("AllocationStatus"),rs.getInt("VehicleOwned")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void searchByNid() throws SQLException, ClassNotFoundException {
        searchList.clear();
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();

        query = "SELECT f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                "f2.PermanentAddress, COUNT(v.FlatNumber) as 'VehicleOwned' " +
                "FROM FlatOwner as f1 " +
                "INNER JOIN FlatOwner_Info as f2 " +
                "ON f1.FlatNumber = f2.FlatNumber " +
                "LEFT JOIN Vehicle as v " +
                "ON f1.FlatNumber = v.FlatNumber " +
                "WHERE f2.nid= '"+filteredFlatOwner.getText()+"'" +
                "GROUP BY f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                "f2.PermanentAddress " +
                "ORDER BY f1.FlatNumber";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            searchList.add(new FlatOwner(rs.getString("FlatNumber"),rs.getString("OwnerName"),
                    rs.getString("Mobile"), rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getInt("AllocatedParkingSlot"),
                    rs.getString("AllocationStatus"),rs.getInt("VehicleOwned")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void searchByStatus() throws SQLException, ClassNotFoundException {
        searchList.clear();
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();

        query = "SELECT f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                "f2.PermanentAddress, COUNT(v.FlatNumber) as 'VehicleOwned' " +
                "FROM FlatOwner as f1 " +
                "INNER JOIN FlatOwner_Info as f2 " +
                "ON f1.FlatNumber = f2.FlatNumber " +
                "LEFT JOIN Vehicle as v " +
                "ON f1.FlatNumber = v.FlatNumber " +
                "WHERE f1.AllocationStatus= '"+filteredFlatOwner.getText().toUpperCase()+"'" +
                "GROUP BY f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                "f2.PermanentAddress " +
                "ORDER BY f1.FlatNumber";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            searchList.add(new FlatOwner(rs.getString("FlatNumber"),rs.getString("OwnerName"),
                    rs.getString("Mobile"), rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getInt("AllocatedParkingSlot"),
                    rs.getString("AllocationStatus"),rs.getInt("VehicleOwned")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void initSearchOptions() {
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, search_options);
        //convert list to observable list
        ObservableList observableList = FXCollections.observableArrayList(list);
        searchComboBox.setItems(observableList);
    }

    private void initTable() {
        table_flatNo.setCellValueFactory(new PropertyValueFactory<>("flatNumber"));
        table_ownerName.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        table_mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        table_presAdd.setCellValueFactory(new PropertyValueFactory<>("presentAdd"));
        table_permAdd.setCellValueFactory(new PropertyValueFactory<>("permanentAdd"));
        table_nid.setCellValueFactory(new PropertyValueFactory<>("nid"));
        table_allocatedParkNo.setCellValueFactory(new PropertyValueFactory<>("allocatedParkingNo"));
        table_allocateStatus.setCellValueFactory(new PropertyValueFactory<>("allocationStatus"));
        table_vehicleOwned.setCellValueFactory(new PropertyValueFactory<>("vehicleOwned"));
    }

    private void loadFromDatabase() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                    "f2.PermanentAddress, COUNT(v.FlatNumber) as 'VehicleOwned' " +
                "FROM FlatOwner as f1 " +
                "INNER JOIN FlatOwner_Info as f2 " +
                    "ON f1.FlatNumber = f2.FlatNumber " +
                "LEFT JOIN Vehicle as v " +
                    "ON f1.FlatNumber = v.FlatNumber " +
                "GROUP BY f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                    "f2.PermanentAddress " +
                "ORDER BY f1.FlatNumber";

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            flatOwnerObservableList.add(new FlatOwner(rs.getString("FlatNumber"),rs.getString("OwnerName"),
                    rs.getString("Mobile"), rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getInt("AllocatedParkingSlot"),
                    rs.getString("AllocationStatus"),rs.getInt("VehicleOwned")));
        }
    }

    private void showFlatEditView(Timeline refreshTableTimeline) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/sample/views/manager/flat/manager_FlatOwner_Edit.fxml"));
            loader.load();
            ManagerFlatOwnerEditController managerFlatOwnerEditController = loader.getController();
            editSelectedRow(managerFlatOwnerEditController); //Edit table row
            Scene scene = new Scene(loader.getRoot());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Edit Flat Owner Details");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
            stage.setOnCloseRequest(e -> {
                System.out.println("Edited Window Disconnected");
                refreshTableTimeline.play();
                isRowSelected = false;
            });
            managerFlatOwnerEditController.cancel_EditButton.setOnAction(actionEvent -> {
                System.out.println("Edited Window Disconnected");
                refreshTableTimeline.play();
                stage.close();
                isRowSelected = false;
            });
            managerFlatOwnerEditController.save_EditButton.setOnAction(actionEvent -> {
                try {
                    managerFlatOwnerEditController.updateFlatOwner();
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

    private void editSelectedRow(ManagerFlatOwnerEditController managerFlatOwnerEditController) {
        FlatOwner selectedRow = flatOwner_tableView.getSelectionModel().getSelectedItem();

        managerFlatOwnerEditController.flatNo_EditField.setText(selectedRow.getFlatNumber());
        managerFlatOwnerEditController.ownerName_EditField.setText(selectedRow.getOwnerName());
        managerFlatOwnerEditController.nid_EditField.setText(selectedRow.getNid());
        managerFlatOwnerEditController.mobile_EditField.setText(selectedRow.getMobile());
        managerFlatOwnerEditController.prestAdd_EditField.setText(selectedRow.getPresentAdd());
        managerFlatOwnerEditController.permaAdd_EditField.setText(selectedRow.getPermanentAdd());
        managerFlatOwnerEditController.allocatedParking_EditField.setText(Integer.toString(selectedRow.getAllocatedParkingNo()));
        managerFlatOwnerEditController.allocStatus_EditComboBox.setValue(selectedRow.getAllocationStatus());
    }

    private void showFlatAddView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/sample/views/manager/flat/manager_FlatOwner_Add.fxml"));
            loader.load();
            Scene scene = new Scene(loader.getRoot());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Add Flat Owner Details");
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
