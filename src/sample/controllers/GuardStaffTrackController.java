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
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class GuardStaffTrackController implements Initializable {


    Date date = null;
    Time inTime = null, outTime = null;


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
    private TableView<Staff> table;

    @FXML
    private TableColumn<Date, StaffAttendance> date_col;

    @FXML
    private TableColumn<String,
            StaffAttendance> staff_name_col;

    @FXML
    private TableColumn<String,
            StaffAttendance> staff_mobile_col;

    @FXML
    private TableColumn<String,
            StaffAttendance> in_time_col;

    @FXML
    private TableColumn<String,
            StaffAttendance> out_time_col;
    @FXML
    private TableColumn<Integer,
            StaffAttendance> staff_id_col;


    @FXML
    private TableColumn<String,
            StaffAttendance> staff_type_col;


    @FXML
    private JFXTextField search_text_field;

    @FXML
    void handleDelete(ActionEvent event) {

    }

    @FXML
    void handleFilter(ActionEvent event) {


        String query = null;
        list.clear();
        list.clear();

        if (inTime == null && outTime == null && date == null) {

        } else if (inTime == null && outTime == null && date != null) {

            query = "select st.WorkDate as " +
                    "WorkDate, st" +
                    ".InTime as InTime," +
                    " st.OutTime " +
                    "as OutTime" +
                    ", st" +
                    ".StaffId as " +
                    "StaffId, s.Name " +
                    "as Name, s.Mobile " +
                    "as Mobile,s" +
                    ".StaffType as " +
                    "StaffType" +
                    " " +
                    "from StaffAttTrack" +
                    " as st left " +
                    "join Staff as s " +
                    "on st.StaffId = s" +
                    ".StaffId " +
                    " " +
                    "where st.WorkDate " +
                    "=  '" + date +
                    "';";


        } else if (inTime == null && outTime != null && date == null) {


            query = "select st.WorkDate as " +
                    "WorkDate, st" +
                    ".InTime as InTime, st" +
                    ".OutTime " +
                    "as OutTime" +
                    ", st" +
                    ".StaffId as StaffId, s" +
                    ".Name " +
                    "as Name, s.Mobile as " +
                    "Mobile,s" +
                    ".StaffType as StaffType" +
                    " " +
                    "from StaffAttTrack as st " +
                    "left " +
                    "join Staff as s " +
                    "on st.StaffId = s.StaffId " +
                    " " +
                    "where st.OutTime <= '" + outTime +
                    "';";


        } else if (inTime == null && outTime != null && date != null) {
            query = "select st.WorkDate as " +
                    "WorkDate, st" +
                    ".InTime as InTime," +
                    " st.OutTime " +
                    "as OutTime" +
                    ", st" +
                    ".StaffId as " +
                    "StaffId, s.Name " +
                    "as Name, s.Mobile " +
                    "as Mobile,s" +
                    ".StaffType as " +
                    "StaffType" +
                    " " +
                    "from StaffAttTrack" +
                    " as st left " +
                    "join Staff as s " +
                    "on st.StaffId = s" +
                    ".StaffId " +
                    " " +
                    "where st.OutTime " +
                    "<= '" + outTime +
                    "' and " +
                    "st.WorkDate = '" + date +
                    "';";


        } else if (inTime != null && outTime == null && date == null) {
            query = "select st.WorkDate as " +
                    "WorkDate, st" +
                    ".InTime as InTime, st" +
                    ".OutTime " +
                    "as OutTime" +
                    ", st" +
                    ".StaffId as StaffId, s" +
                    ".Name " +
                    "as Name, s.Mobile as " +
                    "Mobile,s" +
                    ".StaffType as StaffType" +
                    " " +
                    "from StaffAttTrack as st " +
                    "left " +
                    "join Staff as s " +
                    "on st.StaffId = s.StaffId " +
                    " " +
                    "where st.InTime >= '" + inTime +
                    "';";


        } else if (inTime != null && outTime == null && date != null) {


            query = "select st.WorkDate as " +
                    "WorkDate, st" +
                    ".InTime as InTime, st" +
                    ".OutTime " +
                    "as OutTime" +
                    ", st" +
                    ".StaffId as StaffId, s" +
                    ".Name " +
                    "as Name, s.Mobile as " +
                    "Mobile,s" +
                    ".StaffType as StaffType" +
                    " " +
                    "from StaffAttTrack as st " +
                    "left " +
                    "join Staff as s " +
                    "on st.StaffId = s.StaffId " +
                    " " +
                    "where st.InTime >= '" + inTime +
                    "' and st.WorkDate = '" + date + "';";


        } else if (inTime != null && outTime != null && date == null) {


            query = "select st.WorkDate as " +
                    "WorkDate, st" +
                    ".InTime as InTime, st" +
                    ".OutTime " +
                    "as OutTime" +
                    ", st" +
                    ".StaffId as StaffId, s" +
                    ".Name " +
                    "as Name, s.Mobile as " +
                    "Mobile,s" +
                    ".StaffType as StaffType" +
                    " " +
                    "from StaffAttTrack as st " +
                    "left " +
                    "join Staff as s " +
                    "on st.StaffId = s.StaffId " +
                    " where " +
                    "st.InTime >= '"+ inTime+
                    "' " +
                    "and st.OutTime <= '" + outTime +
                    "';";


        } else if (inTime != null && outTime != null && date != null) {


            query = "select st.WorkDate as " +
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
                    " where st.InTime >= '"+inTime+
                    "' and st.OutTime <= '"+outTime+
                    "' and st.WorkDate = '"+date+ "';";


        } else {

        }

        try {

            PreparedStatement preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {
                list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                        resultSet.getInt(
                                "StaffId"),
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
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @FXML
    void handleInTime(ActionEvent event) {
        inTime =
                Time.valueOf(in_time_timepicker.getValue());
    }

    @FXML
    void handleOutTIme(ActionEvent event) {
        outTime =
                Time.valueOf(out_time_timepicker.getValue());
    }

    @FXML
    void handleRefresh(ActionEvent event) {
        list.clear();
        loadStaff();

    }

    @FXML
    void handleTrackerDate(ActionEvent event) {
        date =
                Date.valueOf(tracker_date_datepicker.getValue());
    }


    private void loadStaff() {

        String query = "select st.WorkDate as " +
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
                    list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            "Not Recorded",
                            resultSet.getTime(
                                    "OutTime").toLocalTime().toString()));

                } else if (in != null && out == null) {
                    list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            resultSet.getTime(
                                    "InTime").toLocalTime().toString(),
                            "Not Recorded"));

                } else if (in != null && out != null) {
                    list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            resultSet.getTime(
                                    "InTime").toLocalTime().toString(),
                            resultSet.getTime(
                                    "OutTime").toLocalTime().toString()));

                } else {
                    list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                            resultSet.getInt(
                                    "StaffId"),
                            resultSet.getString(
                                    "Name"),
                            resultSet.getString(
                                    "StaffType"),
                            resultSet.getString(
                                    "Mobile"),
                            "Not Recorded",
                            "Not Recorded"));
                }
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

        initStaffTable();
        loadStaff();
        table.setItems(list);


        search_text_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(!t1.isEmpty())
                {
                    list.clear();
                    String query = "select st.WorkDate as " +
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
                            " where st.StaffId " +
                            "like '%"+t1+"%' " +
                            "or s.Mobile like " +
                            "'%"+t1+"%' " +
                            "or s.Name like " +
                            "'%"+t1+"%' " +
                            "or s.StaffType " +
                            "like '%"+t1+"%';";

                try {

                    PreparedStatement preparedStatement =
                            databaseHandler.getDbConnection().prepareStatement(query);
                    ResultSet resultSet =
                            preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
                                resultSet.getInt(
                                        "StaffId"),
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
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {                        
                    e.printStackTrace();
                }
                }else{
                    loadStaff();
                }


            }
        });
    }

    private void initStaffTable() {

        staff_id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        staff_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        staff_mobile_col.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        staff_type_col.setCellValueFactory(new PropertyValueFactory<>("type"));
        date_col.setCellValueFactory(new PropertyValueFactory<>("date"));
        out_time_col.setCellValueFactory(new PropertyValueFactory<>("outTime"));
        in_time_col.setCellValueFactory(new PropertyValueFactory<>("inTime"));
    }
}
