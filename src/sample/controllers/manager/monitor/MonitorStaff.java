package sample.controllers.manager.monitor;

import com.jfoenix.controls.*;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.controllers.StaffAttendance;
import sample.database.DatabaseHandler;

import java.io.Console;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

public class MonitorStaff implements Initializable {


    Date date = null;
    Time inTime = null, outTime = null;
    DatabaseHandler databaseHandler =
            new DatabaseHandler();
    ObservableList list =
            FXCollections.observableArrayList();
    RequiredFieldValidator validator =
            new RequiredFieldValidator();
    String category, subCategory;
    ArrayList<String> month, week, day;
    int categoryIndex, subCategoryIndex,
            selectedMonth = -1;
    public static final String MONTHLY =
            "Monthly";
    public static final String DAILY = "Daily";

    ObservableList categoryList;
    ObservableList subCat1List;
    ObservableList subCat3List;

    XYChart.Series<String, Number> Electritian
            = new XYChart.Series<>();
    XYChart.Series<String, Number> Cleaner =
            new XYChart.Series<>();
    XYChart.Series<String, Number> Lift_Man =
            new XYChart.Series<>();
    XYChart.Series<String, Number> Plumber =
            new XYChart.Series<>();
    XYChart.Series<String, Number> Swiper =
            new XYChart.Series<>();
    ObservableList<XYChart.Series<String,
            Number>> data =
            FXCollections.observableArrayList();

    @FXML
    private TableView<StaffAttendance> table;

    @FXML
    private TableColumn<Date, StaffAttendance> date_col;

    @FXML
    private TableColumn<String,
            StaffAttendance> staff_id_col;

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
    private TableColumn<String,
            StaffAttendance> staff_type_col;

    @FXML
    private JFXTextField search_text_field;

    @FXML
    private JFXComboBox<String> sub_category_combo_box;

    @FXML
    private JFXTimePicker in_time_picker;

    @FXML
    private JFXTimePicker out_time_picker;

    @FXML
    private JFXDatePicker date_picker;

    @FXML
    private JFXButton filter_button;

    @FXML
    private JFXButton show_button;

    @FXML
    private BarChart<String, Number> bar_chart;

    @FXML
    private CategoryAxis x_axis;

    @FXML
    private NumberAxis y_axis;

    @FXML
    private JFXComboBox<String> month_combo_box;

    @FXML
    void handleMonthComboBox(ActionEvent event) {
        selectedMonth =
                month_combo_box.getSelectionModel().getSelectedIndex() + 1;
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
                    "st.InTime >= '" + inTime +
                    "' " +
                    "and st.OutTime <= '" + outTime +
                    "';";


        } else if (inTime != null && outTime != null && date != null) {


            query = "select st.TrackId, st" +
                    ".WorkDate as " +
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
                    " where st.InTime >= '" + inTime +
                    "' and st.OutTime <= '" + outTime +
                    "' and st.WorkDate = '" + date + "';";


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
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }


    @FXML
    void handleDate(ActionEvent event) {
        date =
                Date.valueOf(date_picker.getValue());
    }


    @FXML
    void handleInTime(ActionEvent event) {
        inTime =
                Time.valueOf(in_time_picker.getValue());
    }

    @FXML
    void handleOutTime(ActionEvent event) {
        outTime =
                Time.valueOf(out_time_picker.getValue());
    }


    @FXML
    void handleRefresh(ActionEvent event) {
        list.clear();
        loadStaff();
        table.setItems(list);
    }

    @FXML
    void handleShow(ActionEvent event) {


        if (subCategory != null) {


            if (subCategory.equals(MONTHLY)) {

                data.clear();
                Electritian.getData().clear();
                Plumber.getData().clear();
                Swiper.getData().clear();
                Lift_Man.getData().clear();
                Cleaner.getData().clear();
                data.clear();

                Electritian.setName(
                        "Electritian");
                Cleaner.setName("Cleaner");
                Lift_Man.setName("Lift Man");
                Plumber.setName("Plumber");
                Swiper.setName("Swiper");
                System.out.println("check");
                String query = "select count" +
                        "(id) as traffic,stype," +
                        "month(WorkDate) as " +
                        "mnth " +
                        "from " +
                        " (select st" +
                        ".WorkDate as WorkDate," +
                        " " +
                        "st.InTime as inTime,st" +
                        ".OutTime as outTime,s" +
                        ".StaffId as id,s" +
                        ".StaffType" +
                        " as stype " +
                        " from " +
                        "StaffAttTrack as st " +
                        " left join Staff as s " +
                        "on " +
                        "st.StaffId" +
                        " = s.StaffId " +
                        " where year(st" +
                        ".WorkDate) =" +
                        " '2021' " +
                        " ) as stable group by " +
                        "stype,month" +
                        "(WorkDate) ";


                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement =
                            databaseHandler.getDbConnection().prepareStatement(query);
                    ResultSet resultSet =
                            preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        if (resultSet.getString(2).equals("Electritian")) {
                            Electritian.getData().add(new XYChart.Data(month.get(resultSet.getInt(3) - 1), resultSet.getInt(1)));
                        } else if (resultSet.getString(2).equals("Cleaner")) {
                            Cleaner.getData().add(new XYChart.Data(month.get(resultSet.getInt(3) - 1), resultSet.getInt(1)));

                        } else if (resultSet.getString(2).equals("Lift Man")) {
                            Lift_Man.getData().add(new XYChart.Data(month.get(resultSet.getInt(3) - 1), resultSet.getInt(1)));

                        } else if (resultSet.getString(2).equals("Plumber")) {
                            Plumber.getData().add(new XYChart.Data(month.get(resultSet.getInt(3) - 1), resultSet.getInt(1)));

                        } else if (resultSet.getString(2).equals("Swiper")) {
                            Swiper.getData().add(new XYChart.Data(month.get(resultSet.getInt(3) - 1), resultSet.getInt(1)));

                        } else {

                        }
                    }

                    data.addAll(Electritian,
                            Cleaner
                            , Lift_Man, Plumber
                            , Swiper);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else if (subCategory.equals(DAILY)) {


                Electritian.getData().clear();
                Plumber.getData().clear();
                Swiper.getData().clear();
                Lift_Man.getData().clear();
                Cleaner.getData().clear();
                data.clear();


                if (selectedMonth != -1) {
                    x_axis.setLabel(month.get(selectedMonth-1));


                    String query = "select " +
                            "count(id) as " +
                            "traffic, type from" +
                            "(select st" +
                            ".WorkDate as " +
                            "WorkDate, st" +
                            ".InTime as inTime," +
                            "st.OutTime as " +
                            "outTime,s.StaffId " +
                            "as id,s.StaffType " +
                            "as type " +
                            " from " +
                            "StaffAttTrack as " +
                            "st " +
                            " left join " +
                            "Staff as s on st" +
                            ".StaffId = s" +
                            ".StaffId " +
                            " where year" +
                            "(st.WorkDate) = " +
                            "'2021' and month" +
                            "(st.WorkDate)= '" + selectedMonth +
                            "'" +
                            " " +
                            " ) as stable group" +
                            " " +
                            "by type ;";

                    PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement =
                                databaseHandler.getDbConnection().prepareStatement(query);
                        ResultSet resultSet =
                                preparedStatement.executeQuery();
                        data.clear();
                        while (resultSet.next()) {

                            System.out.println(resultSet.getInt(1) + " " + resultSet.getString(2));

                            if (resultSet.getString(2).equals("Electritian")) {
                                Electritian.getData().add(new XYChart.Data("Electritian", resultSet.getInt(1)));
                            } else if (resultSet.getString(2).equals("Cleaner")) {
                                Cleaner.getData().add(new XYChart.Data("Cleaner", resultSet.getInt(1)));

                            } else if (resultSet.getString(2).equals("Lift Man")) {
                                Lift_Man.getData().add(new XYChart.Data("Lift Man", resultSet.getInt(1)));

                            } else if (resultSet.getString(2).equals("Plumber")) {
                                Plumber.getData().add(new XYChart.Data("Plumber", resultSet.getInt(1)));

                            } else if (resultSet.getString(2).equals("Swiper")) {
                                Swiper.getData().add(new XYChart.Data("Swiper", resultSet.getInt(1)));
                            } else {

                            }
                        }

                        data.addAll(Electritian,
                                Cleaner
                                , Lift_Man,
                                Plumber
                                , Swiper);

                        System.out.println(
                                "data " +
                                        "size: "
                                        + data.size());

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                }
            } else {

            }
            bar_chart.getData().clear();
            bar_chart.layout();
            bar_chart.getData().addAll(data);
        } else {

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
                    list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
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
                    list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
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
                    list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
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
                    list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
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

    private void initStaffTable() {

        staff_id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        staff_name_col.setCellValueFactory(new PropertyValueFactory<>("name"));
        staff_mobile_col.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        staff_type_col.setCellValueFactory(new PropertyValueFactory<>("type"));
        date_col.setCellValueFactory(new PropertyValueFactory<>("date"));
        out_time_col.setCellValueFactory(new PropertyValueFactory<>("outTime"));
        in_time_col.setCellValueFactory(new PropertyValueFactory<>("inTime"));
    }


    @FXML
    void handleSubCategory(ActionEvent event) {
        subCategory =
                sub_category_combo_box.getSelectionModel().getSelectedItem();
        subCategoryIndex =
                sub_category_combo_box.getSelectionModel().getSelectedIndex() + 1;

        if (subCategoryIndex == 1) {
            month_combo_box.setVisible(false);
        } else {
            month_combo_box.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {

        month_combo_box.setVisible(false);
        x_axis.setAnimated(false);
        initStaffTable();
        loadStaff();
        table.setItems(list);

        intiData();

        validator.setMessage("Fill the field!");

        search_text_field.textProperty().addListener((observableValue, s, t1) -> {


            if (!t1.isEmpty()) {
                list.clear();

                String query = "select st" +
                        ".TrackId," +
                        " st" +
                        ".WorkDate as " +
                        "WorkDate, st" +
                        ".InTime as InTime, st" +
                        ".OutTime " +
                        "as OutTime" +
                        ", st" +
                        ".StaffId as StaffId, s" +
                        ".Name " +
                        "as Name, s.Mobile as " +
                        "Mobile,s" +
                        ".StaffType as " +
                        "StaffType" +
                        " " +
                        "from StaffAttTrack as " +
                        "st left " +
                        "join Staff as s " +
                        "on st.StaffId = s" +
                        ".StaffId " +
                        " where st.StaffId " +
                        "like '%" + t1 + "%' " +
                        "or s.Mobile like " +
                        "'%" + t1 + "%' " +
                        "or s.Name like " +
                        "'%" + t1 + "%' " +
                        "or s.StaffType " +
                        "like '%" + t1 + "%';";

                try {

                    PreparedStatement preparedStatement =
                            databaseHandler.getDbConnection().prepareStatement(query);
                    ResultSet resultSet =
                            preparedStatement.executeQuery();

                    while (resultSet.next()) {
                        list.add(new StaffAttendance(resultSet.getDate("WorkDate"),
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
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                list.clear();
                loadStaff();
            }

        });

    }


    void intiData() {


        Electritian.setName(
                "Electritian");
        Cleaner.setName("Cleaner");
        Lift_Man.setName("Lift Man");
        Plumber.setName("Plumber");
        Swiper.setName("Swiper");


        bar_chart.setAnimated(false);
        month = new ArrayList<>();
        day = new ArrayList<>();

        week = new ArrayList<>();

        categoryList =
                FXCollections.observableArrayList(MONTHLY, DAILY);

        month.add("JANUARY");
        month.add("FEBRUARY");
        month.add("MARCH");
        month.add("APRIL");
        month.add("MAY");
        month.add("JUNE");
        month.add("JULY");
        month.add("AUGUST");
        month.add("SEPTEMBER");
        month.add("OCTOBER");
        month.add("NOVEMBER");
        month.add("DECEMBER");

        week.add("1st");
        week.add("2nd");
        week.add("3rd");
        week.add("4th");
        week.add("5th");


        month_combo_box.setItems(FXCollections.observableArrayList(month));

        subCat1List =
                FXCollections.observableArrayList(month);

        subCat3List =
                FXCollections.observableArrayList(day);
        sub_category_combo_box.setItems(FXCollections.observableArrayList(MONTHLY, DAILY));
    }

}
