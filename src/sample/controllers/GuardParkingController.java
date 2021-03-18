package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

public class GuardParkingController implements Initializable {


    String flatNumber, ownerName, carModel,
            carNumber;
    Date date=null;
    Time inTime=null, outTime=null;


    DatabaseHandler databaseHandler =
            new DatabaseHandler();


    ObservableList list =
            FXCollections.observableArrayList();

    @FXML
    private JFXButton filter_button;

    @FXML
    private JFXTimePicker in_time_timepicker;

    @FXML
    private JFXTimePicker out_time_timepicker;

    @FXML
    private JFXDatePicker tracker_date_datepicker;

    @FXML
    private TableView<ParkingVehicle> table;

    @FXML
    private JFXTextField search_text_field;


    @FXML
    private TableColumn<Date, ParkingVehicle> date_col;

    @FXML
    private TableColumn<String, ParkingVehicle> car_num_col;

    @FXML
    private TableColumn<String, ParkingVehicle> car_model_coi;

    @FXML
    private TableColumn<String, ParkingVehicle> in_time_col;

    @FXML
    private TableColumn<String, ParkingVehicle> out_time_col;

    @FXML
    private TableColumn<String, ParkingVehicle> allocation_flat_col;


    @FXML
    void handleTrackerDate(ActionEvent event) {
        date =
                Date.valueOf(tracker_date_datepicker.getValue());
    }

    @FXML
    void handleOutTIme(ActionEvent event) {
        outTime =
                Time.valueOf(out_time_timepicker.getValue());
    }

    @FXML
    void handleInTime(ActionEvent event) {
        inTime =
                Time.valueOf(in_time_timepicker.getValue());

    }

    @FXML
    void handleFilter(ActionEvent event) {

        System.out.println(inTime + " "+ outTime + " " + date);

        String query = null;
        list.clear();

        if(inTime==null && outTime==null && date==null)
        {

        }else if(inTime==null && outTime==null && date!=null){

          query =  "select p.TrackId as " +
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
                  "where p.Date = '" + date+ "';";


        }else if(inTime==null && outTime!=null && date==null){
            query =  "select p.TrackId as " +
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
                    "where p.OutTime <= '" + outTime +
                    "';";

        }else if(inTime==null && outTime!=null && date!=null){
            query =  "select p.TrackId as " +
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
                    "where p.OutTime <= '" + outTime +
                    "' and p.Date = '"+ date +
                    "';";

        }else if(inTime!= null && outTime==null && date==null){
            query =  "select p.TrackId as " +
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
                    "where p.InTime >= '"  + inTime +
                    "';";

        }else if(inTime!=null && outTime==null && date!=null){
            query =  "select p.TrackId as " +
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
                    "where p.Intime >= '" +inTime +
                    "' and p.Date = '" + date +
                    "' ;";

        }else if(inTime!=null && outTime!=null && date==null){
            query =  "select p.TrackId as " +
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
                    "where p.InTime >= '" +inTime +
                    "' and p.OutTime <= '" + outTime +
                    "';";

        }else if(inTime!=null && outTime!=null && date!=null){
            query =  "select p.TrackId as " +
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
                    "where p.Intime >= '"+ inTime +
                    "' and p.OutTime <= '" + outTime +
                    "' and p.Date = '" + date +
                    "';";

        }else {

        }


        try {

            PreparedStatement preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(new ParkingVehicle(resultSet.getInt("CarId"), resultSet.getInt("TrackId"), resultSet.getString("FlatNumber"), resultSet.getString("CarModel")
                        ,
                        resultSet.getString("CarNumber"), resultSet.getDate("Date"), resultSet.getTime("InTime"), resultSet.getTime("OutTime")));

            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }


    @FXML
    void handleRefresh(ActionEvent event) {

        list.clear();
        load();
        table.setItems(list);

    }

    @FXML
    void handleDelete(ActionEvent event) throws IOException {

        ObservableList<ParkingVehicle> parkingVehicles = null;

        if (table.getSelectionModel().getSelectedItems().size() > 0) {
            parkingVehicles =
                    table.getSelectionModel().getSelectedItems();


            FXMLLoader fxmlLoader =
                    new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/views/guard/guard_parking_delete.fxml"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GuardParkingDeleteController controller =
                    fxmlLoader.getController();
            controller.setVehiclesList(parkingVehicles);
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
                    fxmlLoader.load();


            Dialog<ButtonType> dialog =
                    new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Confirm");

            Optional<ButtonType> clickedButton
                    = dialog.showAndWait();

        }


    }


    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {


        initTable();
        load();
        table.setItems(list);


        search_text_field.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                list.clear();

                String query = "select p" +
                        ".TrackId as TrackId, v" +
                        ".CarId as " +
                        "CarId, p" +
                        ".FlatNumber as " +
                        "FlatNumber," +
                        " " +
                        "p" +
                        ".Date as Date, p" +
                        ".InTime as " +
                        "InTime, p" +
                        ".OutTime as OutTime, v" +
                        ".CarNumber as " +
                        "CarNumber, v" +
                        ".ParkingSlotNumber as " +
                        "ParkingSlotNumber, v" +
                        ".CarModel as CarModel" +
                        " from " +
                        "ParkingSlotTrack as p " +
                        "join " +
                        "Vehicle as v " +
                        "on v.FlatNumber = p" +
                        ".FlatNumber" +
                        " and v.CarNumber = p" +
                        ".CarNumber " +
                        "where p.FlatNumber " +
                        "like " +
                        "'%" + t1 + "%' " +
                        "or " +
                        "v.CarNumber like  '%" + t1 +
                        "%' " +
                        "or " +
                        "v.CarModel like '%" + t1 +
                        "%'" +
                        ";";

                try {

                    PreparedStatement preparedStatement =
                            databaseHandler.getDbConnection().prepareStatement(query);
                    ResultSet resultSet =
                            preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        list.add(new ParkingVehicle(resultSet.getInt("CarId"), resultSet.getInt("TrackId"), resultSet.getString("FlatNumber"), resultSet.getString("CarModel")
                                ,
                                resultSet.getString("CarNumber"), resultSet.getDate("Date"), resultSet.getTime("InTime"), resultSet.getTime("OutTime")));

                    }


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void initTable() {
        car_num_col.setCellValueFactory(new PropertyValueFactory<>("carNumber"));
        date_col.setCellValueFactory(new PropertyValueFactory<>("date"));
        allocation_flat_col.setCellValueFactory(new PropertyValueFactory<>("flatNumber"));
        in_time_col.setCellValueFactory(new PropertyValueFactory<>("timeIn"));
        out_time_col.setCellValueFactory(new PropertyValueFactory<>("timeOut"));
        car_model_coi.setCellValueFactory(new PropertyValueFactory<>("carModel"));
    }


    void load() {

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
                list.add(new ParkingVehicle(resultSet.getInt("CarId"), resultSet.getInt("TrackId"), resultSet.getString("FlatNumber"), resultSet.getString("CarModel")
                        , resultSet.getString(
                        "CarNumber"),
                        resultSet.getDate("Date"
                        ), resultSet.getTime(
                        "InTime"),
                        resultSet.getTime(
                                "OutTime")));

            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
