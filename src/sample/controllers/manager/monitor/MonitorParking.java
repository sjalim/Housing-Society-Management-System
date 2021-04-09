package sample.controllers.manager.monitor;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sample.controllers.ParkingVehicle;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

public class MonitorParking implements Initializable {

    Time inTime = null, outTime = null;
    Date date = null;
    ArrayList<String> month, week, day, day28;
    int firstWeek, secondWeek, thirdWeek, fourthWeek, fifthWeek, sixthWeek;


    String yearIn = null, yearOut = null, monthIn = null, monthOut = null, weekIn = null, weekOut = null, dayIn = null, dayOut = null;
    boolean yearInB = false, yearOutB = false, monthInB = false, monthOutB = false, weekInB = false, weekOutB = false, dayInB = false, dayOutB = false;


    DatabaseHandler databaseHandler =
            new DatabaseHandler();


    ObservableList list =
            FXCollections.observableArrayList();

    XYChart.Series setYear = new XYChart.Series<String, Number>();
    XYChart.Series setMonth = new XYChart.Series<>();
    XYChart.Series setWeek = new XYChart.Series<>();
    XYChart.Series setDay = new XYChart.Series<>();

    RequiredFieldValidator validatorYear = new RequiredFieldValidator();

    @FXML
    private BarChart<?, ?> in_time_chart;

    @FXML
    private CategoryAxis in_time_x_axis;

    @FXML
    private NumberAxis in_time_y_axis;

    @FXML
    private BarChart<?, ?> out_time_chart;

    @FXML
    private CategoryAxis out_time_x_axis;

    @FXML
    private NumberAxis out_time_y_axis;
    @FXML
    private JFXComboBox<String> in_time_week_combo_box;

    @FXML
    private JFXComboBox<String> in_time_month_combo_box;

    @FXML
    private JFXComboBox<String> out_time_week_combo_box;

    @FXML
    private JFXComboBox<String> out_time_month_combo_box;

    @FXML
    private TableView<ParkingVehicle> table;

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

//    @FXML
//    private JFXDatePicker date_picker;


    @FXML
    private DatePicker date_picker;


    @FXML
    private JFXTextField search_text_filed;

    @FXML
    private JFXTimePicker in_time_picker;

    @FXML
    private JFXTimePicker out_time_picker;

    @FXML
    private JFXComboBox<String> in_time_day_combo_box;

    @FXML
    private JFXComboBox<String> out_time_day_combo_box;


    @FXML
    private JFXTextField in_time_year_text_field;

    @FXML
    private JFXTextField out_time_year_text_field;

    @FXML
    void handleInTimeYear(ActionEvent event) {

        String year = in_time_year_text_field.getText();
        System.out.println(year);
        in_time_x_axis.setLabel("Month");
        in_time_y_axis.setLabel("Traffic");
        in_time_x_axis.setAnimated(false);

        if (year.length() == 4) {
            yearIn = year;
            yearInB = true;
            in_time_month_combo_box.setDisable(false);

            String query =
                    "select count(TrackId) as traffic, mnth from " +
                            "(" +
                            "select TrackId, MONTH(p.Date) as mnth from " +
                            "ParkingSlotTrack as p where YEAR(p.Date) = '" + year + "' and " +
                            " p.InTime is not null) as tim " +
                            "group by tim.mnth";

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);

                ResultSet resultSet = preparedStatement.executeQuery();

                System.out.println(resultSet);
                while (resultSet.next()) {

                    setYear.getData().add(new XYChart.Data<String, Number>(month.get(resultSet.getInt(2) - 1).toString(), resultSet.getInt(1)));

                    System.out.println(month.get(resultSet.getInt(2) - 1) + " " + resultSet.getInt(1));
                }

                in_time_chart.getData().addAll(setYear);


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    @FXML
    void handleOutTimeYear(ActionEvent event) {
        String year = out_time_year_text_field.getText();
        System.out.println(year);
        out_time_x_axis.setLabel("Month");
        out_time_y_axis.setLabel("Traffic");
        out_time_x_axis.setAnimated(false);

        if (year.length() == 4) {
            yearOut = year;
            yearOutB = true;
            out_time_month_combo_box.setDisable(false);


            String query =  "select count(TrackId) as traffic, mnth from " +
                            "(" +
                            "select TrackId, MONTH(p.Date) as mnth from " +
                            "ParkingSlotTrack as p where YEAR(p.Date) = '" + year + "' and " +
                            " p.OutTime is not null) as tim " +
                            "group by tim.mnth";

            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                System.out.println(resultSet);
                while (resultSet.next()) {
                    setYear.getData().add(new XYChart.Data<String, Number>(month.get(resultSet.getInt(2) - 1).toString(), resultSet.getInt(1)));
                    System.out.println(month.get(resultSet.getInt(2) - 1) + " " + resultSet.getInt(1));
                }

                out_time_chart.getData().addAll(setYear);


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
    }


    @FXML
    void handleRefresh(ActionEvent event) {

        list.clear();
        load();

        date_picker.getEditor().clear();
        in_time_picker.getEditor().clear();
        out_time_picker.getEditor().clear();
    }

    @FXML
    void handleDate(ActionEvent event) {
        date = Date.valueOf(date_picker.getValue());
        filter();
    }

    @FXML
    void handleInTIme(ActionEvent event) {
        inTime =
                Time.valueOf(in_time_picker.getValue());
        filter();
    }

    @FXML
    void handleOutTime(ActionEvent event) {
        outTime =
                Time.valueOf(out_time_picker.getValue());
        filter();
    }

    @FXML
    void handleMonthInTime(ActionEvent event) {

        int m = in_time_month_combo_box.getSelectionModel().getSelectedIndex();
        in_time_day_combo_box.setDisable(false);
        System.out.println(m);
        if (yearInB) {

            monthIn = String.valueOf(m + 1);

            monthInB = true;
            in_time_week_combo_box.setDisable(false);
            String query = "select count(id) as traffic, inDate" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearIn + "' and p.InTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthIn + "')" +
                    " as tim group by inDate ";
            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    Date date = resultSet.getDate(2);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(date.toLocalDate().getYear(), date.toLocalDate().getMonthValue(), date.toLocalDate().getDayOfMonth());
                    int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
                    setMonth.getData().add(new XYChart.Data<String, Number>(week.get(weekOfMonth - 1), resultSet.getInt(1)));
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            in_time_chart.getData().clear();
//            in_time_chart.layout();
            in_time_month_combo_box.setDisable(false);
            in_time_x_axis.setLabel("Week");
            in_time_chart.getData().addAll(setMonth);
        }
    }

    @FXML
    void handleWeekInTime(ActionEvent event) {
        int w = in_time_week_combo_box.getSelectionModel().getSelectedIndex();
        setWeek.getData().clear();
        in_time_day_combo_box.setDisable(false);
        if (w == 0) {
            String query = "select count(id) as traffic, datename(weekday,inDate) as dayOfWeek" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearIn + "' and p.InTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthIn + "' and day(p.Date)>=1 and day(p.Date)<=7)" +
                    " as tim group by inDate ";

            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    setWeek.getData().add(new XYChart.Data<String, Number>(resultSet.getString(2), resultSet.getInt(1)));
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        } else if (w == 1) {

            String query = "select count(id) as traffic, datename(weekday,inDate) as dayOfWeek" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearIn + "' and p.InTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthIn + "' and day(p.Date)>=8 and day(p.Date)<=14)" +
                    " as tim group by inDate ";

            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    setWeek.getData().add(new XYChart.Data<String, Number>(resultSet.getString(2), resultSet.getInt(1)));
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else if (w == 2) {
            String query = "select count(id) as traffic, datename(weekday,inDate) as dayOfWeek" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearIn + "' and p.InTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthIn + "' and day(p.Date)>=15 and day(p.Date)<=21)" +
                    " as tim group by inDate ";

            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    setWeek.getData().add(new XYChart.Data<String, Number>(resultSet.getString(2), resultSet.getInt(1)));
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else if (w == 3) {
            String query = "select count(id) as traffic, datename(weekday,inDate) as dayOfWeek" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearIn + "' and p.InTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthIn + "' and day(p.Date)>=22 and day(p.Date)<=28)" +
                    " as tim group by inDate ";

            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    setWeek.getData().add(new XYChart.Data<String, Number>(resultSet.getString(2), resultSet.getInt(1)));
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else if (w == 4) {
            String query = "select count(id) as traffic, datename(weekday,inDate) as dayOfWeek" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearIn + "' and p.InTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthIn + "' and day(p.Date)>=29 and day(p.Date)<=31)" +
                    " as tim group by inDate ";

            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {

                    setWeek.getData().add(new XYChart.Data<String, Number>(resultSet.getString(2), resultSet.getInt(1)));
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {

            System.out.println("error");
        }
        in_time_chart.getData().clear();
//        in_time_chart.layout();
        in_time_month_combo_box.setDisable(false);
        in_time_x_axis.setLabel("Week");
        in_time_chart.getData().addAll(setWeek);
    }

    @FXML
    void handleDayInTime(ActionEvent event) {

        int d = in_time_day_combo_box.getSelectionModel().getSelectedIndex();
        dayIn = String.valueOf(d + 1);
        setDay.getData().clear();
        String query;
        for (int i = 1; i <= 12; i++) {


            if(i==12)
            {
                 query = "select count(TrackId) as traffic from ParkingSlotTrack where "
                        + " Date = '" + yearIn + "-" + monthIn + "-" + dayIn + "' and InTime is not null and InTime>= '" + (2 * i - 2) + ":00:00' and InTime < '00:00:00';";

            }
            else{

             query = "select count(TrackId) as traffic from ParkingSlotTrack where "
                    + " Date = '" + yearIn + "-" + monthIn + "-" + dayIn + "' and InTime is not null and InTime>= '" + (2 * i - 2) + ":00:00' and InTime < '" + (2 * i ) + ":00:00';";
            }


            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {


                    if(i>6)
                    {
                        setDay.getData().add(new XYChart.Data<>((i*2-2)+"-"+(i*2)+" PM",resultSet.getInt(1)));
                    }
                    else
                    {
                        setDay.getData().add(new XYChart.Data<>((i*2-2)+"-"+(i*2)+" AM",resultSet.getInt(1)));
                    }
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }
        in_time_chart.getData().clear();
//        in_time_chart.layout();
        in_time_month_combo_box.setDisable(false);
        in_time_x_axis.setLabel("Week");
        in_time_chart.getData().addAll(setDay);
    }


    @FXML
    void handleMonthOutTime(ActionEvent event) {

        int m = out_time_month_combo_box.getSelectionModel().getSelectedIndex();
        System.out.println(m);
        if (yearOutB) {

            monthOut = String.valueOf(m + 1);

            monthOutB = true;
            out_time_week_combo_box.setDisable(false);
            out_time_day_combo_box.setDisable(false);
            String query = "select count(id) as traffic, inDate" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearOut + "' and p.OutTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthOut + "')" +
                    " as tim group by inDate ";
            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    Date date = resultSet.getDate(2);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(date.toLocalDate().getYear(), date.toLocalDate().getMonthValue(), date.toLocalDate().getDayOfMonth());
                    int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
                    setMonth.getData().add(new XYChart.Data<String, Number>(week.get(weekOfMonth - 1), resultSet.getInt(1)));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            out_time_chart.getData().clear();
//            out_time_chart.layout();
            out_time_month_combo_box.setDisable(false);
            out_time_x_axis.setLabel("Week");
            out_time_chart.getData().addAll(setMonth);
        }
    }
    @FXML
    void handleWeekOutTIme(ActionEvent event) {
        int w = out_time_week_combo_box.getSelectionModel().getSelectedIndex();
        setWeek.getData().clear();
        out_time_day_combo_box.setDisable(false);
        if (w == 0) {
            String query = "select count(id) as traffic, datename(weekday,inDate) as dayOfWeek" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearOut + "' and p.OutTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthOut + "' and day(p.Date)>=1 and day(p.Date)<=7)" +
                    " as tim group by inDate ";

            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    setWeek.getData().add(new XYChart.Data<String, Number>(resultSet.getString(2), resultSet.getInt(1)));
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        } else if (w == 1) {

            String query = "select count(id) as traffic, datename(weekday,inDate) as dayOfWeek" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearOut + "' and p.OutTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthOut + "' and day(p.Date)>=8 and day(p.Date)<=14)" +
                    " as tim group by inDate ";

            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    setWeek.getData().add(new XYChart.Data<String, Number>(resultSet.getString(2), resultSet.getInt(1)));
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else if (w == 2) {
            String query = "select count(id) as traffic, datename(weekday,inDate) as dayOfWeek" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearOut + "' and p.OutTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthOut + "' and day(p.Date)>=15 and day(p.Date)<=21)" +
                    " as tim group by inDate ";

            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    setWeek.getData().add(new XYChart.Data<String, Number>(resultSet.getString(2), resultSet.getInt(1)));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else if (w == 3) {
            String query = "select count(id) as traffic, datename(weekday,inDate) as dayOfWeek" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearOut + "' and p.OutTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthOut + "' and day(p.Date)>=22 and day(p.Date)<=28)" +
                    " as tim group by inDate ";

            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    setWeek.getData().add(new XYChart.Data<String, Number>(resultSet.getString(2), resultSet.getInt(1)));
                }


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if (w == 4) {
            String query = "select count(id) as traffic, datename(weekday,inDate) as dayOfWeek" +
                    " from(select p.TrackId as id, p.Date as inDate from ParkingSlotTrack as p where year(Date) = '" + yearOut + "' and p.OutTime is not" +
                    " null" +
                    " and month(p.Date) = '" + monthOut + "' and day(p.Date)>=29 and day(p.Date)<=31)" +
                    " as tim group by inDate ";

            PreparedStatement preparedStatement = null;
            try {

                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {

                    setWeek.getData().add(new XYChart.Data<String, Number>(resultSet.getString(2), resultSet.getInt(1)));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else {

            System.out.println("error");
        }
        out_time_chart.getData().clear();
//        out_time_chart.layout();
        out_time_month_combo_box.setDisable(false);
        out_time_x_axis.setLabel("Week");
        out_time_chart.getData().addAll(setWeek);    }

    @FXML
    void handleDayOutTime(ActionEvent event) {
        int d = out_time_day_combo_box.getSelectionModel().getSelectedIndex();
        dayOut = String.valueOf(d + 1);
        setDay.getData().clear();
        String query;
        for (int i = 1; i <= 12; i++) {

            if(i==12)
            {
                query = "select count(TrackId) as traffic from ParkingSlotTrack where "
                        + " Date = '" + yearOut + "-" + monthOut + "-" + dayOut + "' and OutTime is not null and OutTime>= '" + (2 * i - 2) + ":00:00' and OutTime < '00:00:00';";
            }
            else{
                query = "select count(TrackId) as traffic from ParkingSlotTrack where "
                        + " Date = '" + yearOut + "-" + monthOut + "-" + dayOut + "' and OutTime is not null and OutTime>= '" + (2 * i - 2) + ":00:00' and OutTime < '" + (2 * i ) + ":00:00';";
            }
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    if(i>6)
                    {
                        setDay.getData().add(new XYChart.Data<>((i*2-2)+"-"+(i*2)+" PM",resultSet.getInt(1)));
                    }
                    else
                    {
                        setDay.getData().add(new XYChart.Data<>((i*2-2)+"-"+(i*2)+" AM",resultSet.getInt(1)));
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        out_time_chart.getData().clear();
//        out_time_chart.layout();
        out_time_month_combo_box.setDisable(false);
        out_time_x_axis.setLabel("Week");
        out_time_chart.getData().addAll(setDay);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        week = new ArrayList<>();
        month = new ArrayList<>();
        day = new ArrayList<>();
        day28 = new ArrayList<>();


        validatorYear.setMessage("Invalid year follow e.g.");

        dataInit();
        initTable();
        load();
        table.setItems(list);


        in_time_year_text_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (t1.isEmpty()) {
                    in_time_chart.getData().clear();
//                    in_time_chart.layout();
                }

                if (t1.length() <= 4) {
                    in_time_year_text_field.resetValidation();
                }

                if (t1.length() > 4) {
                    in_time_year_text_field.getValidators().add(validatorYear);
                    in_time_year_text_field.validate();
                }
            }
        });

        search_text_filed.textProperty().addListener((observableValue, s, t1) -> {
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

            } else {
                list.clear();
                load();
            }
        });


    }


    void dataInit() {

        in_time_chart.setAnimated(false);
        out_time_chart.setAnimated(false);

        week.add("1st");
        week.add("2nd");
        week.add("3rd");
        week.add("4th");
        week.add("5th");


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


        in_time_month_combo_box.setItems(FXCollections.observableArrayList(month));
        out_time_month_combo_box.setItems(FXCollections.observableArrayList(month));

        in_time_week_combo_box.setItems(FXCollections.observableArrayList(week));
        out_time_week_combo_box.setItems(FXCollections.observableArrayList(week));


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


        for (int i = 1; i <= 31; i++) {

            if (i % 10 == 1) {

                day28.add(String.valueOf(i) + "st");
            } else if (i % 10 == 2) {
                day28.add(String.valueOf(i) + "nd");

            } else if (i % 10 == 3) {
                day28.add(String.valueOf(i) + "rd");

            } else {
                day28.add(String.valueOf(i) + "th");

            }
        }

        in_time_day_combo_box.setItems(FXCollections.observableArrayList(day));
        out_time_day_combo_box.setItems(FXCollections.observableArrayList(day));


        in_time_day_combo_box.setDisable(true);
        out_time_day_combo_box.setDisable(true);
        in_time_week_combo_box.setDisable(true);
        out_time_week_combo_box.setDisable(true);
        in_time_month_combo_box.setDisable(true);
        out_time_month_combo_box.setDisable(true);


    }

    String detectDay(int y, int m, int d) {

        String day = "Error";

        switch (day_of_week(y, m, d)) {
            case 0:
                day = "Sunday";
                break;
            case 1:
                day = "Monday";
                break;
            case 2:
                day = "Tuesday";
                break;
            case 3:
                day = "Wednesday";
                break;
            case 4:
                day = "Thursday";
                break;
            case 5:
                day = "Friday";
                break;
            case 6:
                day = "Saturday";
                break;
        }
        return day;
    }


    int day_of_week(int y, int m, int d) {
        int t[] = {0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};

        if (m < 3) {
            y -= m;
        }
//        y -= m < 3;
        return (y + y / 4 - y / 100 + y / 400 + t[m - 1] + d) % 7;
    }


    void filter() {
        String query = null;
        list.clear();

        if (inTime == null && outTime == null && date == null) {

        } else if (inTime == null && outTime == null && date != null) {

            query = "select p.TrackId as " +
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
                    "where p.Date = '" + date + "';";


        } else if (inTime == null && outTime != null && date == null) {
            query = "select p.TrackId as " +
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

        } else if (inTime == null && outTime != null && date != null) {
            query = "select p.TrackId as " +
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
                    "' and p.Date = '" + date +
                    "';";

        } else if (inTime != null && outTime == null && date == null) {
            query = "select p.TrackId as " +
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
                    "where p.InTime >= '" + inTime +
                    "';";

        } else if (inTime != null && outTime == null && date != null) {
            query = "select p.TrackId as " +
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
                    "where p.Intime >= '" + inTime +
                    "' and p.Date = '" + date +
                    "' ;";

        } else if (inTime != null && outTime != null && date == null) {
            query = "select p.TrackId as " +
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
                    "where p.InTime >= '" + inTime +
                    "' and p.OutTime <= '" + outTime +
                    "';";

        } else if (inTime != null && outTime != null && date != null) {
            query = "select p.TrackId as " +
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
                    "where p.InTime >= '" + inTime +
                    "' and p.OutTime <= '" + outTime +
                    "' and p.Date = '" + date +
                    "';";

        } else {

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
