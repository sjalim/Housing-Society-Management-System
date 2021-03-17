package sample.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManagerParkingAllocationController implements Initializable {


    DatabaseHandler databaseHandler =
            new DatabaseHandler();

    ObservableList<Vehicle> list;
    ArrayList<String> searchMenuList;
    ArrayList<String> qsnList;
    ArrayList<String> subQsnListCarModel;
    ArrayList<String> subQsnListOwnerType;

    LocalDate fromDate = null, toDate = null;


    public static final String PARKING_SLOT_NUMBER = "Parking Slot Number";
    public static final String FLAT_NUMBER =
            "FLat Number";
    public static final String CAR_REGISTRATION_NUMBER = "Car registration number";
    public static final String CAR_MODEL = "Car" +
            " Model";
    public static final String QSN_1 =
            "Allocated " +
                    "car between?";
    public static final String QSN_2 = "Car " +
            "Model?";
    public static final String QSN_3 = "Owner " +
            "Type?";
    public static final String QSN_4 = "vacancy?";

    public static final String SUB_QSN_1 =
            "Flat Owner";
    public static final String SUB_QSN_2 =
            "Tenant";

    String curSearch = null;
    StringBuilder searchKey;
    int totalVacancy;

    RequiredFieldValidator validator =
            new RequiredFieldValidator();
    RequiredFieldValidator invalidator =
            new RequiredFieldValidator();

    String selectedQsn, selectedSubQsn;


    @FXML
    private TextField search_text_field;

    @FXML
    private ImageView search_image_view;

    @FXML
    private JFXComboBox<String> qsn_combo_box;

    @FXML
    private ComboBox<String> sub_qsn_combo_box;

    @FXML
    private TableView<Vehicle> table;

    @FXML
    private TableColumn<String, Vehicle> flat_number_table_col;

    @FXML
    private TableColumn<String, Vehicle> parking_slot_number_table_col;

    @FXML
    private TableColumn<String, Vehicle> car_reg_table_col;

    @FXML
    private TableColumn<String, Vehicle> car_model_table_col;

    @FXML
    private TableColumn<String, Vehicle> owner_type_table_col;

    @FXML
    private TableColumn<Date, Vehicle> allocated_date_table_col;

    @FXML
    private JFXComboBox<String> search_cat_combo_box;

    @FXML
    private JFXDatePicker from_date_date_picker;

    @FXML
    private JFXDatePicker to_date_date_picker;


    @FXML
    void handleMakeVacant(ActionEvent event) {
        Vehicle vehicle =
                table.getSelectionModel().getSelectedItem();


        if (vehicle != null) {
            FXMLLoader fxmlLoader =
                    new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/views/utils/ConfirmDialog.fxml"));
            DialogPane dialogPane = null;
            try {
                dialogPane = fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Dialog<ButtonType> dialog =
                    new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Confirm");

            Optional<ButtonType> clickedButton
                    = dialog.showAndWait();
            System.out.println("i am here");


            if (clickedButton.get() == ButtonType.YES) {

                String query = "update Vehicle " +
                        "set Vehicle.CarNumber " +
                        "= ?, " +
                        "Vehicle.FlatNumber = " +
                        "?, " +
                        "Vehicle.CarModel = ?, " +
                        "Vehicle.AllocationDate" +
                        " = ?" +
                        " where Vehicle.CarId =" +
                        " " + vehicle.getCarId() + ";";

                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement =
                            databaseHandler.getDbConnection().prepareStatement(query);
                    preparedStatement.setString(
                            1, null);
                    preparedStatement.setString(2, null);
                    preparedStatement.setString(3, null);
                    preparedStatement.setDate(4,
                            null);

                    preparedStatement.executeUpdate();


                    query = "update " +
                            "VehicleOwnership " +
                            "set " +
                            "VehicleOwnership" +
                            ".OwnerShipType = ?" +
                            " where " +
                            "VehicleOwnership" +
                            ".CarId = " + vehicle.getCarId() + ";";

                    preparedStatement =
                            databaseHandler.getDbConnection().prepareStatement(query);
                    preparedStatement.setString(1, null);

                    preparedStatement.executeUpdate();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    @FXML
    void handleSearch(MouseEvent event) {
        searchKey = new StringBuilder();

        if (curSearch != null) {

            searchKey.append(search_text_field.getText());

            list.clear();


            if (curSearch.equals(PARKING_SLOT_NUMBER)) {
                loadNum("ParkingSlotNumber",
                        searchKey.toString());
                table.setItems(list);


            } else {

                searchKey.append("%");
                searchKey.insert(0, "%");
                if (curSearch.equals(FLAT_NUMBER)) {

                    loadLike("FlatNumber",
                            searchKey.toString());
                    table.setItems(list);
                } else if (curSearch.equals(CAR_MODEL)) {
                    loadLike("CarModel",
                            searchKey.toString());
                    table.setItems(list);
                } else if (curSearch.equals(CAR_REGISTRATION_NUMBER)) {
                    loadLike("CarNumber",
                            searchKey.toString());
                    table.setItems(list);
                } else {
                    System.out.println("error " +
                            "occurred at " +
                            "search!");
                }

            }


        } else {
            search_cat_combo_box.getValidators().add(validator);
            search_cat_combo_box.validate();
        }

    }


    @FXML
    void handleRefresh(ActionEvent event) {
        list.clear();
        load();
        table.setItems(list);

    }


    @FXML
    void handleQsn(ActionEvent event) {

        selectedQsn =
                qsn_combo_box.getValue();

        ObservableList subQsnList =
                FXCollections.observableArrayList();

        if (selectedQsn.equals(QSN_1)) {
            from_date_date_picker.setVisible(true);
            to_date_date_picker.setVisible(true);
            sub_qsn_combo_box.setVisible(false);


        } else if (selectedQsn.equals(QSN_2)) {

            from_date_date_picker.setVisible(false);
            to_date_date_picker.setVisible(false);
            sub_qsn_combo_box.setVisible(true);
            sub_qsn_combo_box.setPromptText(
                    "Select Model");

            subQsnList.addAll(subQsnListCarModel);
            sub_qsn_combo_box.setItems(subQsnList);

        } else if (selectedQsn.equals(QSN_3)) {

            from_date_date_picker.setVisible(false);
            to_date_date_picker.setVisible(false);
            sub_qsn_combo_box.setVisible(true);
            subQsnList.addAll(subQsnListOwnerType);
            sub_qsn_combo_box.setItems(subQsnList);
            sub_qsn_combo_box.setPromptText(
                    "Select Owner Type");


        } else if (selectedQsn.equals(QSN_4)) {
            from_date_date_picker.setVisible(false);
            to_date_date_picker.setVisible(false);
            sub_qsn_combo_box.setVisible(false);

            String query = "select COUNT(t1" +
                    ".CarId) as vacancy" +
                    " from Vehicle as t1 inner " +
                    "join " +
                    " VehicleOwnership as t2 on" +
                    " t1.CarId = t2.CarId " +
                    " where t1.AllocationDate " +
                    "is NULL " +
                    " and t1.CarModel is NULL " +
                    " and t1.CarNumber is NULL " +
                    " and t1.FlatNumber is NULL" +
                    " and t2.OwnerShipType is " +
                    "NULL;";

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement =
                        databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet =
                        preparedStatement.executeQuery();
                if (resultSet.next()) {
                    totalVacancy =
                            resultSet.getInt(1);
                }

                System.out.println(
                        "totalVacancy :" + totalVacancy);

                list.add(new Vehicle("Total " +
                        "Vacancy",
                        String.valueOf(totalVacancy)));
                table.setItems(list);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                e.printStackTrace();
            }


        }


    }

    @FXML
    void handleSubQsn(ActionEvent event) {

        list.clear();
        selectedSubQsn =
                sub_qsn_combo_box.getValue();


        if (selectedQsn.equals(QSN_2)) {

            loadLike("CarModel",selectedSubQsn);
            table.setItems(list);

        } else if (selectedQsn.equals(QSN_3)) {
            loadLike("OwnerShipType",selectedSubQsn);
            table.setItems(list);
        } else {
            System.out.println("f*** filter");
        }
    }

    @FXML
    void handleFromDate(ActionEvent event) {

        fromDate =
                from_date_date_picker.getValue();

        list.clear();

        if (fromDate != null && toDate != null) {

            loadByDate(fromDate, toDate);
            table.setItems(list);
        } else if (fromDate != null && toDate == null) {

            String query = "select * from " +
                    "Vehicle " +
                    "full outer join  " +
                    "VehicleOwnership on " +
                    "VehicleOwnership.CarId =" +
                    " Vehicle.CarId " +
                    "where AllocationDate " +
                    ">= '" + Date.valueOf(fromDate) + "';";
            System.out.println("from date: " + fromDate
                    .toString());

            PreparedStatement preparedStatement =
                    null;
            try {
                preparedStatement =
                        databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet =
                        preparedStatement.executeQuery();

                while (resultSet.next()) {
                    list.add(new Vehicle(resultSet.getString("FlatNumber"), resultSet.getString("ParkingSlotNumber")
                            , resultSet.getString(
                            "CarNumber"),
                            resultSet.getString(
                                    "CarModel"),
                            resultSet.getString(
                                    "OwnerShipType"),
                            resultSet.getDate(
                                    "AllocationDate"),
                            resultSet.getInt(
                                    "CarId"
                            )));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            System.out.println("error at from " +
                    "date");
        }

    }


    @FXML
    void handleToDate(ActionEvent event) {
        toDate =
                to_date_date_picker.getValue();

        list.clear();

        if (fromDate != null && toDate != null) {

            loadByDate(fromDate, toDate);
            table.setItems(list);
        } else if (fromDate == null && toDate != null) {

            String query = "select * from " +
                    "Vehicle " +
                    "full outer join  " +
                    "VehicleOwnership on " +
                    "VehicleOwnership.CarId =" +
                    " Vehicle.CarId " +
                    "where AllocationDate " +
                    "<= '" + Date.valueOf(toDate) +
                    "';";
            System.out.println("from date: " + toDate
                    .toString());

            PreparedStatement preparedStatement =
                    null;
            try {
                preparedStatement =
                        databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet =
                        preparedStatement.executeQuery();

                while (resultSet.next()) {
                    list.add(new Vehicle(resultSet.getString("FlatNumber"), resultSet.getString("ParkingSlotNumber")
                            , resultSet.getString(
                            "CarNumber"),
                            resultSet.getString(
                                    "CarModel"),
                            resultSet.getString(
                                    "OwnerShipType"),
                            resultSet.getDate(
                                    "AllocationDate"),
                            resultSet.getInt(
                                    "CarId"
                            )));
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        } else {
            System.out.println("error at from " +
                    "date");
        }


    }


    private void loadByDate(LocalDate fromDate,
                            LocalDate toDate) {

        String query = "select * from " +
                "Vehicle " +
                "full outer join  " +
                "VehicleOwnership on " +
                "VehicleOwnership.CarId =" +
                " Vehicle.CarId " +
                "where AllocationDate " +
                ">= '" + Date.valueOf(fromDate) +
                "' and AllocationDate <= '" + Date.valueOf(toDate) + "';";
        System.out.println("from date: " + toDate
                .toString());

        PreparedStatement preparedStatement =
                null;
        try {
            preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(new Vehicle(resultSet.getString("FlatNumber"), resultSet.getString("ParkingSlotNumber")
                        , resultSet.getString(
                        "CarNumber"),
                        resultSet.getString(
                                "CarModel"),
                        resultSet.getString(
                                "OwnerShipType"),
                        resultSet.getDate(
                                "AllocationDate"),
                        resultSet.getInt(
                                "CarId"
                        )));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleUpdate(ActionEvent event) {

        Vehicle vehicle =
                table.getSelectionModel().getSelectedItem();


        if (vehicle != null) {

            FXMLLoader fxmlLoader =
                    new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/views/manager/manager_vehicle_update.fxml"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ManagerVehicleUpdateController controller =
                    fxmlLoader.getController();
            controller.setTextData(vehicle);
            Parent parent = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

        }

    }

    @FXML
    void handleSearchCategory(ActionEvent event) {

        curSearch =
                search_cat_combo_box.getValue();
        if (curSearch != null) {
            search_cat_combo_box.getValidators().add(invalidator);
            search_cat_combo_box.validate();
        }

    }

    void initTable() {
        parking_slot_number_table_col.setCellValueFactory(new PropertyValueFactory<>("parkingSlotNum"));
        flat_number_table_col.setCellValueFactory(new PropertyValueFactory<>("flatNumber"));
        car_reg_table_col.setCellValueFactory(new PropertyValueFactory<>("carRegNum"));
        car_model_table_col.setCellValueFactory(new PropertyValueFactory<>("carModel"));
        allocated_date_table_col.setCellValueFactory(new PropertyValueFactory<>("allocationDate"));
        owner_type_table_col.setCellValueFactory(new PropertyValueFactory<>("ownerType"));

    }

    void loadLike(String col, String key) {

        String query = "select * from Vehicle " +
                "full outer join  " +
                "VehicleOwnership on " +
                "VehicleOwnership.CarId =" +
                " Vehicle.CarId " +
                "where " + col + " like '" + key.toUpperCase() +
                "'" +
                "" +
                " or " + col + " like '" + key.toLowerCase() +
                "';";

        System.out.println("key :" + key + " "
                + key.toUpperCase() + " " + key.toLowerCase());

        PreparedStatement preparedStatement =
                null;
        try {
            preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(new Vehicle(resultSet.getString("FlatNumber"), resultSet.getString("ParkingSlotNumber")
                        , resultSet.getString(
                        "CarNumber"),
                        resultSet.getString(
                                "CarModel"),
                        resultSet.getString(
                                "OwnerShipType"),
                        resultSet.getDate(
                                "AllocationDate"),
                        resultSet.getInt("CarId"
                        )));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void loadNum(String col, String key) {

        String query = "select * from Vehicle " +
                "full outer join  " +
                "VehicleOwnership on " +
                "VehicleOwnership.CarId = " +
                "Vehicle.CarId " +
                "where " + col + " = " + Integer.parseInt(
                key) +
                " ;";

        System.out.println("key :" + key + " "
                + key.toUpperCase() + " " + key.toLowerCase());

        PreparedStatement preparedStatement =
                null;
        try {
            preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(new Vehicle(resultSet.getString("FlatNumber"), resultSet.getString("ParkingSlotNumber")
                        , resultSet.getString(
                        "CarNumber"),
                        resultSet.getString(
                                "CarModel"),
                        resultSet.getString(
                                "OwnerShipType"),
                        resultSet.getDate(
                                "AllocationDate"),
                        resultSet.getInt("CarId"
                        )));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void load() {
        String query = "select * from Vehicle " +
                "full outer join  " +
                "VehicleOwnership on " +
                "VehicleOwnership.CarId =" +
                " Vehicle.CarId";

        PreparedStatement preparedStatement =
                null;
        try {
            preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(new Vehicle(resultSet.getString("FlatNumber"), resultSet.getString("ParkingSlotNumber")
                        , resultSet.getString(
                        "CarNumber"),
                        resultSet.getString(
                                "CarModel"),
                        resultSet.getString(
                                "OwnerShipType"),
                        resultSet.getDate(
                                "AllocationDate"),
                        resultSet.getInt("CarId"
                        )));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {


        list =
                FXCollections.observableArrayList();
        initTable();
        load();
        table.setItems(list);

        searchMenuList = new ArrayList<>();
        subQsnListOwnerType = new ArrayList<>();
        subQsnListCarModel = new ArrayList<>();
        qsnList = new ArrayList<>();

        searchMenuList.add(PARKING_SLOT_NUMBER);
        searchMenuList.add(FLAT_NUMBER);
        searchMenuList.add(CAR_MODEL);
        searchMenuList.add(CAR_REGISTRATION_NUMBER);


        ObservableList menuList =
                FXCollections.observableArrayList(searchMenuList);

        search_cat_combo_box.setItems(menuList);

        validator.setMessage("Fill the field!");

        invalidator.setMessage("");

        String query = "select distinct " +
                "CarModel from Vehicle";
        try {
            PreparedStatement preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();
            while (resultSet.next()) {
                subQsnListCarModel.add(resultSet.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        qsnList.add(QSN_1);
        qsnList.add(QSN_2);
        qsnList.add(QSN_3);
        qsnList.add(QSN_4);

        ObservableList listOfQsn =
                FXCollections.observableArrayList(qsnList);
        qsn_combo_box.setItems(listOfQsn);

        from_date_date_picker.setVisible(false);
        to_date_date_picker.setVisible(false);
        sub_qsn_combo_box.setVisible(false);

        subQsnListOwnerType.add(SUB_QSN_1);
        subQsnListOwnerType.add((SUB_QSN_2));


    }
}
