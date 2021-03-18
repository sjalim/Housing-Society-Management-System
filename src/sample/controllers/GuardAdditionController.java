package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class GuardAdditionController implements Initializable {

    LocalTime inTime;
    LocalTime outTime;

    ObservableList categoryList =
            FXCollections.observableArrayList();

    DatabaseHandler databaseHandler =
            new DatabaseHandler();

    public static final String STAFF = "Staff";
    public static final String PARKING =
            "Parking";
    String fieldKeyStaffParking = "";
    String categoryKey = "";

    RequiredFieldValidator validator =
            new RequiredFieldValidator();


    @FXML
    private JFXTimePicker in_time_timepicker;

    @FXML
    private JFXTimePicker out_time_timepicker;

    @FXML
    private JFXTextField car_no_text_field;

    @FXML
    private JFXButton add_button;

    @FXML
    private JFXComboBox<String> addition_category_combo_box;

    @FXML
    void handleAddition(ActionEvent event) {

        fieldKeyStaffParking =
                car_no_text_field.getText();
        System.out.println("value ="+ fieldKeyStaffParking);

        if (!categoryKey.equals("")) {


            if (inTime != null && outTime != null && fieldKeyStaffParking != null) {

                if (categoryKey.equals(STAFF)) {
                    String query = "insert into" +
                            " " +
                            "StaffAttTrack " +
                            "( InTime," +
                            " OutTime, StaffId)" +
                            "select '"+
                            Time.valueOf(inTime)+"','"+
                            Time.valueOf(outTime) + "',"+
                            Integer.parseInt(fieldKeyStaffParking) +
                            " where exists" +
                            "(select StaffId " +
                            "from Staff where " +
                            "StaffId = " + Integer.valueOf(fieldKeyStaffParking) +
                            ")";


                    PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement =
                                databaseHandler.getDbConnection().prepareStatement(query);
                        preparedStatement.executeUpdate();


                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                } else if (categoryKey.equals(PARKING)) {

                    String query = "insert into" +
                            " ParkingSlotTrack " +
                            "( " +
                            "FlatNumber, " +
                            "InTime, " +
                            "OutTime, " +
                            "CarNumber) " +
                            "select t1" +
                            ".FlatNumber, " +
                            ",'" + Time.valueOf(inTime) +
                            "','" + Time.valueOf(outTime) +
                            "','" + fieldKeyStaffParking +
                            "' from Vehicle as " +
                            "t1 " +
                            "where t1.CarNumber" +
                            " = '" + fieldKeyStaffParking +
                            "';";

                    PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement =
                                databaseHandler.getDbConnection().prepareStatement(query);
                        preparedStatement.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("error");
                }


            } else if(inTime == null && outTime != null && fieldKeyStaffParking != null){

                if (categoryKey.equals(STAFF)) {
                    String query = "insert into" +
                            " " +
                            "StaffAttTrack " +
                            "( InTime," +
                            " OutTime, StaffId)" +
                            "select " +
                            null + ",'" +
                            Time.valueOf(outTime) + "'," +
                            Integer.parseInt(fieldKeyStaffParking) +
                            "where exists" +
                            "(select StaffId " +
                            "from Staff where " +
                            "StaffId = " + Integer.valueOf(fieldKeyStaffParking) +
                            ")";


                    PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement =
                                databaseHandler.getDbConnection().prepareStatement(query);
                        preparedStatement.executeUpdate();


                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                } else if (categoryKey.equals(PARKING)) {

                    String query = "insert into" +
                            " ParkingSlotTrack " +
                            "(" +
                            "FlatNumber, " +
                            "InTime, " +
                            "OutTime, " +
                            "CarNumber) " +
                            "select t1" +
                            ".FlatNumber, " +
                            "," + null +
                            ", '" + Time.valueOf(outTime) +
                            "', '" + fieldKeyStaffParking +
                            "' from Vehicle as " +
                            "t1 " +
                            "where t1.CarNumber" +
                            " = '" + fieldKeyStaffParking +
                            "';";

                    PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement =
                                databaseHandler.getDbConnection().prepareStatement(query);
                        preparedStatement.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("error");
                }

            } else if(inTime != null && outTime == null && fieldKeyStaffParking != null){

                System.out.println(new Date(System.currentTimeMillis()) + " " + Time.valueOf(inTime));
                if (categoryKey.equals(STAFF)) {
                    String query = "insert into" +
                            " " +
                            "StaffAttTrack " +
                            "( InTime," +
                            " OutTime, StaffId)" +
                            " " +
                            "select '" +
                            Time.valueOf(inTime)+"',"+
                            null+ "," +
                            Integer.parseInt(fieldKeyStaffParking)+
                            " where exists" +
                            "(select StaffId " +
                            "from Staff where " +
                            "StaffId = " + Integer.valueOf(fieldKeyStaffParking) +
                            ")";


                    PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement =
                                databaseHandler.getDbConnection().prepareStatement(query);

                        preparedStatement.executeUpdate();


                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }


                } else if (categoryKey.equals(PARKING)) {

                    String query = "insert into" +
                            " ParkingSlotTrack " +
                            "( " +
                            "FlatNumber, " +
                            "InTime, " +
                            "OutTime, " +
                            "CarNumber) " +
                            "select t1" +
                            ".FlatNumber, " +
                            ",'" + Time.valueOf(inTime) +
                            "', " + null +
                            ", '" + fieldKeyStaffParking +
                            "' from Vehicle as " +
                            "t1 " +
                            "where t1.CarNumber" +
                            " = '" + fieldKeyStaffParking +
                            "';";

                    PreparedStatement preparedStatement = null;
                    try {
                        preparedStatement =
                                databaseHandler.getDbConnection().prepareStatement(query);
                        preparedStatement.executeUpdate();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("error");
                }

            }
                else {
                if (inTime == null) {

                    in_time_timepicker.getValidators().add(validator);
                    in_time_timepicker.validate();

                }

                if (outTime == null) {
                    out_time_timepicker.getValidators().add(validator);
                    out_time_timepicker.validate();
                }

                if (fieldKeyStaffParking.equals("")) {
                    car_no_text_field.getValidators().add(validator);
                    car_no_text_field.validate();
                    System.out.println("chek");
                }

            }
        } else {
            addition_category_combo_box.getValidators().add(validator);
            addition_category_combo_box.validate();
            if (inTime == null) {

                in_time_timepicker.getValidators().add(validator);
                in_time_timepicker.validate();

            }

            if (outTime == null) {
                out_time_timepicker.getValidators().add(validator);
                out_time_timepicker.validate();
            }

            if (fieldKeyStaffParking.equals("")) {
                car_no_text_field.getValidators().add(validator);
                car_no_text_field.validate();
                System.out.println("chek");
            }
        }


    }


    @FXML
    void handleCategory(ActionEvent event) {


        if (addition_category_combo_box.getValue().equals(STAFF)) {
            car_no_text_field.setPromptText(
                    "Staff Id:");
            categoryKey =
                    addition_category_combo_box.getValue();
            addition_category_combo_box.resetValidation();


        } else if (addition_category_combo_box.getValue().equals(PARKING)) {
            car_no_text_field.setPromptText(
                    "Car No:");
            categoryKey =
                    addition_category_combo_box.getValue();
            addition_category_combo_box.resetValidation();


        } else {
            System.out.println("Error occured " +
                    "at combo box ");
        }
    }

    @FXML
    void handleInTime(ActionEvent event) {

        inTime = in_time_timepicker.getValue();


    }

    @FXML
    void handleOutTime(ActionEvent event) {
        outTime = out_time_timepicker.getValue();
    }

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {

        categoryList.add(STAFF);
        categoryList.add(PARKING);

        addition_category_combo_box.setItems(categoryList);

        validator.setMessage("Fill the field!");

        car_no_text_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.isEmpty()) {
                    car_no_text_field.resetValidation();
                }
            }
        });

        in_time_timepicker.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.isEmpty()) {
                    in_time_timepicker.resetValidation();

                }
            }
        });

        out_time_timepicker.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if (!t1.isEmpty()) {
                    out_time_timepicker.resetValidation();
                }
            }
        });


    }
}
