package sample.controllers;

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

    private final String[] movingType = {"MoveIn","MoveOut"};
    private String query;
    private boolean isRowSelected = false;
    private DatabaseHandler databaseHandler = new DatabaseHandler();
    private ObservableList<Tenant> tenantObservableList;
    private ObservableList<Tenant> typeObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

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
                            initTable();
                            try {
                                loadFromDatabase();
                            } catch (SQLException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            }
                            FilteredList<Tenant> filteredData = new FilteredList<>(tenantObservableList, b -> true);

                            // 2. Set the filter Predicate whenever the filter changes.
                            filteredTenant.textProperty().addListener((observable, oldValue, newValue) -> {
                                filteredData.setPredicate(tenant -> {
                                    // If filter text is empty, display all persons.
                                    if (newValue == null || newValue.isEmpty()) {
                                        return true;
                                    }
                                    // Compare first name and last name of every person with filter text.
                                    String lowerCaseFilter = newValue.toLowerCase();
                                    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
//                                    String text = df.format(date);

                                    if (tenant.getFlatNumber().toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                                        return true;
                                    } else if (String.valueOf(tenant.getTotalFamilyMember()).indexOf(lowerCaseFilter) != -1){
                                        return true;
                                    } else if (tenant.getMobile().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                        return true;
                                    } else if (tenant.getPresentAdd().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                        return true;
                                    } else if (tenant.getPermanentAdd().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                        return true;
                                    } else if (tenant.getNid().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                        return true;
                                    } else if (tenant.getTenantName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                        return true;
                                    } else if (tenant.getOccupation().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                        return true;
                                    }
                                    else
                                        return false; // Does not match.
                                });
                            });
                            // 3. Wrap the FilteredList in a SortedList.
                            SortedList<Tenant> sortedData = new SortedList<>(filteredData);
                            // 4. Bind the SortedList comparator to the TableView comparator.
                            // 	  Otherwise, sorting the TableView would have no effect.
                            sortedData.comparatorProperty().bind(tenant_tableView.comparatorProperty());
                            // 5. Add sorted (and filtered) data to the table.
                            tenant_tableView.setItems(sortedData);
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

        addButton.setOnAction(actionEvent -> {
            showTenantAddView();
        });
    }

    private void filterByDate(String fDate, String tDate, String mType) throws SQLException, ClassNotFoundException {
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

//    private void showFlatEditView(Timeline refreshTableTimeline) {
//        try {
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(this.getClass().getResource("/sample/views/manager/manager_FlatOwner_Edit.fxml"));
//            loader.load();
//            ManagerFlatOwnerEditController managerFlatOwnerEditController = loader.getController();
//            editSelectedRow(managerFlatOwnerEditController); //Edit table row
//            Scene scene = new Scene(loader.getRoot());
//            Stage stage = new Stage();
//            stage.setScene(scene);
//            stage.setTitle("Edit Flat Owner Details");
//            stage.initModality(Modality.APPLICATION_MODAL);
//            stage.show();
//            stage.setOnCloseRequest(e -> {
//                System.out.println("Edited Window Disconnected");
//                refreshTableTimeline.play();
//                isRowSelected = false;
//            });
//            managerFlatOwnerEditController.cancel_EditButton.setOnAction(actionEvent -> {
//                System.out.println("Edited Window Disconnected");
//                refreshTableTimeline.play();
//                stage.close();
//                isRowSelected = false;
//            });
//            managerFlatOwnerEditController.save_EditButton.setOnAction(actionEvent -> {
//                try {
//                    managerFlatOwnerEditController.updateFlatOwner();
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                refreshTableTimeline.play();
//                stage.close();
//                isRowSelected = false;
//                System.out.println("Data Saved");
//            });
//
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void editSelectedRow(ManagerFlatOwnerEditController managerFlatOwnerEditController) {
//        Tenant selectedRow = tenant_tableView.getSelectionModel().getSelectedItem();
//
//        managerFlatOwnerEditController.flatNo_EditField.setText(selectedRow.getFlatNumber());
//        managerFlatOwnerEditController.ownerName_EditField.setText(selectedRow.getOwnerName());
//        managerFlatOwnerEditController.nid_EditField.setText(selectedRow.getNid());
//        managerFlatOwnerEditController.mobile_EditField.setText(selectedRow.getMobile());
//        managerFlatOwnerEditController.prestAdd_EditField.setText(selectedRow.getPresentAdd());
//        managerFlatOwnerEditController.permaAdd_EditField.setText(selectedRow.getPermanentAdd());
//        managerFlatOwnerEditController.allocatedParking_EditField.setText(Integer.toString(selectedRow.getAllocatedParkingNo()));
//        managerFlatOwnerEditController.allocStatus_EditComboBox.setValue(selectedRow.getAllocationStatus());
//    }

    private void showTenantAddView() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(this.getClass().getResource("/sample/views/manager/manager_Tenant_Add.fxml"));
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
