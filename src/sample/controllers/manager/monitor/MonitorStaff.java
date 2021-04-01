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

    RequiredFieldValidator validator = new RequiredFieldValidator();

    String category, subCategory;

    ArrayList<String> month, week, day;
    int categoryIndex, subCategoryIndex;


    public static final String MONTHLY = "Monthly";
    //    public static final String WEEKLY = "Weekly";
    public static final String DAILY = "Daily";

    ObservableList categoryList;
    ObservableList subCat1List;
    //    ObservableList subCat2List;
    ObservableList subCat3List;



    @FXML
    private TableView<StaffAttendance> table;

    @FXML
    private TableColumn<Date, StaffAttendance> date_col;

    @FXML
    private TableColumn<String, StaffAttendance> staff_id_col;

    @FXML
    private TableColumn<String, StaffAttendance> staff_name_col;

    @FXML
    private TableColumn<String, StaffAttendance> staff_mobile_col;

    @FXML
    private TableColumn<String, StaffAttendance> in_time_col;

    @FXML
    private TableColumn<String, StaffAttendance> out_time_col;

    @FXML
    private TableColumn<String, StaffAttendance> staff_type_col;

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


        if (category != null && subCategory != null) {



                String query = "select count(id) as traffic,type  from " +
                        "(select st.WorkDate as workDate, st.InTime as inTime,st.OutTime as outTime,s.StaffId as id,s.StaffType as type " +
                        "from StaffAttTrack as st "
                        + "left join Staff as s on st.StaffId = s.StaffId " +
                        "where year(WorkDate) = '" + Calendar.getInstance().get(Calendar.YEAR) + "' and month(WorkDate) = '" + subCategoryIndex + "'" +
                        ") as stable group by stable.type";


                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery();


                    XYChart.Series setDataMonth = new XYChart.Series<>();
                    while (resultSet.next()) {


//                        System.out.println(resultSet.getString(2)+" " + resultSet.getInt(1));

                        if(resultSet.getInt(1)>0)
                        {

                        setDataMonth.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(1)));
                        }
                    }

                    bar_chart.getData().clear();
                    bar_chart.layout();
                    bar_chart.getData().addAll(setDataMonth);




                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

//            } else if (category.equals(DAILY)) {
//
//
//
//
//
//                System.out.println("month"+ Calendar.getInstance().get(Calendar.MONTH) );
//                String query = "select count(id) as traffic,type as mnth from " +
//                        "(select st.WorkDate as workDate, st.InTime as inTime," +
//                        "st.OutTime as outTime,s.StaffId as id,s.StaffType as type " +
//                        "from StaffAttTrack as st "
//                        + "left join Staff as s on st.StaffId = s.StaffId " +
//                        "where year(WorkDate) = '" + Calendar.getInstance().get(Calendar.YEAR) +
//                        "' and month(WorkDate) = '" +
//                        Calendar.getInstance().get(Calendar.MONTH)  +
//                        "'  and day(WorkDate) =  '"+subCategoryIndex+"') as stable group by stable.type";
//
//
//                PreparedStatement preparedStatement = null;
//                try {
//                    preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
//                    ResultSet resultSet = preparedStatement.executeQuery();
//                    XYChart.Series setDataDay = new XYChart.Series<>();
//
//
//                    while (resultSet.next()) {
//
//
//
//                        System.out.println(resultSet.getString(2)+" " + resultSet.getInt(1));
//
//
//                        if(resultSet.getInt(1)>0)
//                        {
//                        setDataDay.getData().add(new XYChart.Data(resultSet.getString(2), resultSet.getInt(1)));
//
//                        }
//
//                        bar_chart.getData().clear();
//                        bar_chart.layout();
//                        bar_chart.getData().addAll(setDataDay);
//
//                    }
//
//                } catch (SQLException throwables) {
//                    throwables.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
            }



//
//        } else if (category == null) {
//
//        } else if (subCategory == null) {
//
//        } else {
//            System.out.println("error at show button");
//        }


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
        subCategory = sub_category_combo_box.getSelectionModel().getSelectedItem();
        subCategoryIndex = sub_category_combo_box.getSelectionModel().getSelectedIndex() + 1;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        x_axis.setAnimated(false);
        initStaffTable();
        loadStaff();
        table.setItems(list);

        intiData();

        validator.setMessage("Fill the field!");

        search_text_field.textProperty().addListener((observableValue, s, t1) -> {


            if (!t1.isEmpty()) {
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


        month = new ArrayList<>();
        day = new ArrayList<>();

        week = new ArrayList<>();

        categoryList = FXCollections.observableArrayList(MONTHLY, DAILY);

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

        for (int i = 1; i <= 31; i++) {

            if (i % 10 == 1) {

                day.add(String.valueOf(i) + "st");
            } else if (i % 10 == 2) {
                day.add(String.valueOf(i) + "nd");

            } else if (i % 10 == 3) {
                day.add(String.valueOf(i) + "rd");

            } else {
                day.add(String.valueOf(i) + "th");

            }
        }


        subCat1List = FXCollections.observableArrayList(month);
//        subCat2List = FXCollections.observableArrayList(week);
        subCat3List = FXCollections.observableArrayList(day);

        sub_category_combo_box.setDisable(true);
    }

}
