package sample.controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalTime;
import java.util.Optional;
import java.util.ResourceBundle;

public class GuardUpdateController implements Initializable {


    public static final String STAFF_TAB =
            "staffTab";
    public static final String PARKING_TAB =
            "parkingTab";
    String curTab = "";

    ObservableList staffTableList =
            FXCollections.observableArrayList();
    ObservableList parkingTableList =
            FXCollections.observableArrayList();

    DatabaseHandler databaseHandler =
            new DatabaseHandler();


    @FXML
    private TableView<ParkingVehicle> parking_table;

    @FXML
    private Tab parking_tab;

    @FXML
    private TableColumn<Integer, ParkingVehicle> track_id_col_parking;

    @FXML
    private TableColumn<Date, ParkingVehicle> date_col_parking;

    @FXML
    private TableColumn<String, ParkingVehicle> car_num_col_parking;

    @FXML
    private TableColumn<String, ParkingVehicle> car_model_col_parking;

    @FXML
    private TableColumn<String, ParkingVehicle> in_time_col_parking;

    @FXML
    private TableColumn<String, ParkingVehicle> out_time_col_parking;

    @FXML
    private TableColumn<String, ParkingVehicle> allocation_flat_col_parking;

//    @FXML
//    private TableColumn<?, ?> parking_slot_col;

    @FXML
    private Tab staff_tab;


    @FXML
    private TableView<StaffAttendance> staff_table;

    @FXML
    private TableColumn<Integer, StaffAttendance> track_id_col_staff;

    @FXML
    private TableColumn<Date, StaffAttendance> date_col_staff;

    @FXML
    private TableColumn<Integer,
            StaffAttendance> staff_id_col;

    @FXML
    private TableColumn<String,
            StaffAttendance> staff_name_col;




    @FXML
    private TableColumn<String,
            StaffAttendance> in_time_col_staff;

    @FXML
    private TableColumn<String,
            StaffAttendance> out_time_col_staff;

    @FXML
    private TableColumn<String,
            StaffAttendance> staff_type_col;

    @FXML
    private TableColumn<String,
            StaffAttendance> staff_mobile_col;


    @FXML
    private JFXTextField search;

    @FXML
    private TabPane tab_pane;


    @FXML
    void handleRefresh(ActionEvent event) {

        parkingTableList.clear();
        loadParking();
        parking_table.setItems(parkingTableList);

        staffTableList.clear();
        loadStaff();
        staff_table.setItems(staffTableList);

    }

    @FXML
    void handleUpdate(ActionEvent event) {

        if (curTab.equals(STAFF_TAB)) {

            StaffAttendance staffAttendance =
                    staff_table
                            .getSelectionModel()
                            .getSelectedItem();

            if (!staff_table.getSelectionModel
                    ().isEmpty()) {

                FXMLLoader fxmlLoader =
                        new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/views/guard/update_record.fxml"));

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GuardUpdateRecordController controller =
                        fxmlLoader.getController();
                controller.setTextFields(staffAttendance);
                Parent parent = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.initStyle(StageStyle.UTILITY);
                stage.show();


            } else {
                FXMLLoader fxmlLoader =
                        new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/views/utils/NoRowSelected.fxml"));
                DialogPane dialogPane =
                        null;
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

            }


        } else if (curTab.equals(PARKING_TAB)) {

            ParkingVehicle parkingVehicle =
                    parking_table
                            .getSelectionModel()
                            .getSelectedItem();
            if (!parking_table
                    .getSelectionModel().isEmpty()) {

                FXMLLoader fxmlLoader =
                        new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/views/guard/update_record.fxml"));

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                GuardUpdateRecordController controller =
                        fxmlLoader.getController();
                controller.setTextFields(parkingVehicle);
                Parent parent = fxmlLoader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(parent));
                stage.initStyle(StageStyle.UTILITY);
                stage.show();

            } else {
                FXMLLoader fxmlLoader =
                        new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/views/utils/NoRowSelected.fxml"));
                DialogPane dialogPane =
                        null;
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

            }

        } else {
            //error
        }

    }

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {

        initParkingTable();
        loadParking();
        parking_table.setItems(parkingTableList);

        initStaffTable();
        loadStaff();
        staff_table.setItems(staffTableList);

        curTab = PARKING_TAB;

        parking_tab.selectedProperty().addListener((observableValue, aBoolean, t1) -> {

            if (t1) {
                curTab = PARKING_TAB;
            }
        });

        staff_tab.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<?
                    extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {

                    curTab = STAFF_TAB;
                }

            }
        });

        search.textProperty().addListener((observableValue, s, t1) -> {

            if (!t1.isEmpty()) {

                if (curTab.equals(STAFF_TAB)) {

                    staffTableList.clear();

                    loadSearchResStaff(t1);

                } else if (curTab.equals(PARKING_TAB)) {

                    parkingTableList.clear();
                    loadSearchResParking(t1);

                } else {

                }


            } else {

                if (curTab.equals(STAFF_TAB)) {
                    staffTableList.clear();
                    loadStaff();

                } else if (curTab.equals(PARKING_TAB)) {

                    parkingTableList.clear();
                    loadParking();
                } else {

                }

            }
        });


    }


    private void loadSearchResStaff(String key) {

        String query = "select st.TrackId, st" +
                ".WorkDate" +
                " as " +
                "WorkDate, st" +
                ".InTime as InTime, st.OutTime " +
                "as OutTime" +
                ", st" +
                ".StaffId as StaffId, s.Name " +
                "as Name, s.Mobile as Mobile,s" +
                ".StaffType as StaffType" +
                " " +
                "from StaffAttTrack as st left " +
                "join Staff as s " +
                "on st.StaffId = s.StaffId " +
                "where WorkDate like '%" + key +
                "%' " +
                "or " +
                "InTime like '%" + key +
                "%' " +
                "or " +
                "OutTime like '%" + key +
                "%' " +
                "or " +
                "st.StaffId like '%" + key +
                "%' " +
                "or " +
                "Name like '%" + key +
                "%' " +
                "or " +
                "Mobile like '%" + key +
                "%' " +
                "or " +
                "StaffType like '%" + key +
                "%' " + ";";

        PreparedStatement preparedStatement =
                null;
        try {
            preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();


            while (resultSet.next()) {

                Time in = resultSet.getTime(
                        "InTime");
                Time out = resultSet.getTime(
                        "OutTime");


                if (in == null && out != null) {
                    staffTableList.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getInt(
                                    "TrackId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            null,
                            resultSet.getTime(
                                    "OutTime")));

                } else if (in != null && out == null) {
                    staffTableList.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getInt(
                                    "TrackId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            resultSet.getTime(
                                    "InTime"),
                            null));

                } else if (in != null && out != null) {
                    staffTableList.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getInt(
                                    "TrackId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            resultSet.getTime(
                                    "InTime"),
                            resultSet.getTime(
                                    "OutTime")));

                } else {
                    staffTableList.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getInt(
                                    "TrackId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            null,
                            null));

                }

            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void loadSearchResParking(String key) {

        String query = "select p.TrackId as " +
                "TrackId, v.CarId as " +
                "CarId, p" +
                ".FlatNumber as FlatNumber," +
                " " +
                "p" +
                ".Date as Date, p.InTime as " +
                "InTime, p" +
                ".OutTime as OutTime, v" +
                ".CarNumber as CarNumber, v" +
                ".ParkingSlotNumber as " +
                "ParkingSlotNumber, v" +
                ".CarModel as CarModel" +
                " from " +
                "ParkingSlotTrack as p join " +
                "Vehicle as v " +
                "on v.FlatNumber = p.FlatNumber" +
                " and v.CarNumber = p.CarNumber" +
                " where " +
                "p.FlatNumber like '%" + key +
                "%' " +
                "or " +
                "p.Date like '%" + key +
                "%' " +
                "or " +
                "InTime like '%" + key +
                "%' " +
                "or " +
                "OutTime like '%" + key +
                "%' " +
                "or " +
                "v.CarNumber like '%" + key +
                "%' " +
                "or " +
                "v.CarModel like '%" + key +
                "%'" +
                " ;";
        try {

            PreparedStatement preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Time in = resultSet.getTime(
                        "InTime");
                Time out = resultSet.getTime(
                        "OutTime");


                if (in != null && out != null) {

                    parkingTableList.add(new ParkingVehicle(resultSet.getInt("CarId"), resultSet.getInt("TrackId"), resultSet.getString("FlatNumber"), resultSet.getString("CarModel")
                            , resultSet.getString(
                            "CarNumber"),
                            resultSet.getDate(
                                    "Date"
                            ), in,
                            out));

                } else if (in != null && out == null) {
                    parkingTableList.add(new ParkingVehicle(resultSet.getInt("CarId"), resultSet.getInt("TrackId"), resultSet.getString("FlatNumber"), resultSet.getString("CarModel")
                            , resultSet.getString(
                            "CarNumber"),
                            resultSet.getDate(
                                    "Date"
                            ), in,
                            null));
                } else if (in == null && out != null) {
                    parkingTableList.add(new ParkingVehicle(resultSet.getInt("CarId"), resultSet.getInt("TrackId"), resultSet.getString("FlatNumber"), resultSet.getString("CarModel")
                            , resultSet.getString(
                            "CarNumber"),
                            resultSet.getDate(
                                    "Date"
                            ), null,
                            out));
                } else {

                }


            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void loadStaff() {

        String query = "select st.TrackId, st" +
                ".WorkDate as " +
                "WorkDate, st" +
                ".InTime as InTime, st.OutTime " +
                "as OutTime" +
                ", st" +
                ".StaffId as StaffId, s.Name " +
                "as Name, s.Mobile as Mobile,s" +
                ".StaffType as StaffType" +
                " " +
                "from StaffAttTrack as st left " +
                "join Staff as s " +
                "on st.StaffId = s.StaffId " +
                ";";

        PreparedStatement preparedStatement =
                null;
        try {
            preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();


            while (resultSet.next()) {

                Time in = resultSet.getTime(
                        "InTime");
                Time out = resultSet.getTime(
                        "OutTime");


                if (in == null && out != null) {
                    staffTableList.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getInt(
                                    "TrackId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            null,
                            resultSet.getTime(
                                    "OutTime")));

                } else if (in != null && out == null) {
                    staffTableList.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getInt(
                                    "TrackId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            resultSet.getTime(
                                    "InTime"),
                            null));

                } else if (in != null && out != null) {
                    staffTableList.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getInt(
                                    "TrackId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            resultSet.getTime(
                                    "InTime"),
                            resultSet.getTime(
                                    "OutTime")));

                } else {
                    staffTableList.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getInt(
                                    "TrackId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            null,
                            null));

                }

            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void loadParking() {

        System.out.println("check");


        String query = "select p.TrackId as " +
                "TrackId, v.CarId as " +
                "CarId, p" +
                ".FlatNumber as FlatNumber," +
                " " +
                "p" +
                ".Date as Date, p.InTime as " +
                "InTime, p" +
                ".OutTime as OutTime, v" +
                ".CarNumber as CarNumber, v" +
                ".ParkingSlotNumber as " +
                "ParkingSlotNumber, v" +
                ".CarModel as CarModel" +
                " from " +
                "ParkingSlotTrack as p join " +
                "Vehicle as v " +
                "on v.FlatNumber = p.FlatNumber" +
                " and v.CarNumber = p.CarNumber" +
                " " +
                ";";
        try {

            PreparedStatement preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                Time in = resultSet.getTime(
                        "InTime");
                Time out = resultSet.getTime(
                        "OutTime");


                if (in != null && out != null) {

                    parkingTableList.add(new ParkingVehicle(resultSet.getInt("CarId"), resultSet.getInt("TrackId"), resultSet.getString("FlatNumber"), resultSet.getString("CarModel")
                            , resultSet.getString(
                            "CarNumber"),
                            resultSet.getDate(
                                    "Date"
                            ), in,
                            out));

                } else if (in != null && out == null) {
                    parkingTableList.add(new ParkingVehicle(resultSet.getInt("CarId"), resultSet.getInt("TrackId"), resultSet.getString("FlatNumber"), resultSet.getString("CarModel")
                            , resultSet.getString(
                            "CarNumber"),
                            resultSet.getDate(
                                    "Date"
                            ), in,
                            null));
                } else if (in == null && out != null) {
                    parkingTableList.add(new ParkingVehicle(resultSet.getInt("CarId"), resultSet.getInt("TrackId"), resultSet.getString("FlatNumber"), resultSet.getString("CarModel")
                            , resultSet.getString(
                            "CarNumber"),
                            resultSet.getDate(
                                    "Date"
                            ), null,
                            out));
                } else {

                }


            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    private void initParkingTable() {
        track_id_col_parking.setCellValueFactory(new PropertyValueFactory<>("trackId"));
        car_num_col_parking.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        date_col_parking.setCellValueFactory(new PropertyValueFactory<>("date"));
        allocation_flat_col_parking.setCellValueFactory(new PropertyValueFactory<>("flatNumber"));
        in_time_col_parking.setCellValueFactory(new PropertyValueFactory<>("timeIn"));
        out_time_col_parking.setCellValueFactory(new PropertyValueFactory<>("timeOut"));
        car_model_col_parking.setCellValueFactory(new PropertyValueFactory<>("carModel"));

    }

    //
    private void initStaffTable() {
        track_id_col_staff.setCellValueFactory(new PropertyValueFactory<>("trackId"));
        staff_id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        staff_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        staff_mobile_col.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        staff_type_col.setCellValueFactory(new PropertyValueFactory<>("type"));
        date_col_staff.setCellValueFactory(new PropertyValueFactory<>("date"));
        out_time_col_staff.setCellValueFactory(new PropertyValueFactory<>("outTime"));
        in_time_col_staff.setCellValueFactory(new PropertyValueFactory<>("inTime"));
    }
}
