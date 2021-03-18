package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.database.DatabaseHandler;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.ResourceBundle;

public class GuardParkingController implements Initializable {


    String flatNumber, ownerName, carModel,
            carNumber;
    Date date;
    Time inTime, outTime;
    String timeIn, timeOut;

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


    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {


        initTable();
        load();
        table.setItems(list);


        search_text_field.textProperty().addListener((observableValue, s, t1) -> {
            if(!t1.isEmpty())
            {
                list.clear();

                String query = "select v.CarId as " +
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
                        " and v.CarNumber = p" +
                        ".CarNumber " +
                        "where p.FlatNumber " +
                        "like " +
                        "'%"+t1+"%' " +
                        "or " +
                        "v.CarNumber like  '%"+ t1+
                        "%' " +
                        "or " +
                        "v.CarModel like '%"+t1+
                        "%'" +
                        ";";

                try {

                    PreparedStatement preparedStatement =
                            databaseHandler.getDbConnection().prepareStatement(query);
                    ResultSet resultSet =
                            preparedStatement.executeQuery();

                    while (resultSet.next())
                    {
                        list.add(new ParkingVehicle(resultSet.getInt("CarId"),resultSet.getString("FlatNumber"),resultSet.getString("CarModel")
                                ,resultSet.getString("CarNumber"),resultSet.getDate("Date"),resultSet.getTime("InTime"),resultSet.getTime("OutTime")));

                    }


                } catch (ClassNotFoundException e ) {
                    e.printStackTrace();
                } catch (SQLException e)
                {
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


    void load()  {

        String query = "select v.CarId as " +
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

            while (resultSet.next())
            {
                list.add(new ParkingVehicle(resultSet.getInt("CarId"),resultSet.getString("FlatNumber"),resultSet.getString("CarModel")
                ,resultSet.getString("CarNumber"),resultSet.getDate("Date"),resultSet.getTime("InTime"),resultSet.getTime("OutTime")));

            }


        } catch (ClassNotFoundException e ) {
            e.printStackTrace();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
