package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GuardUpdateRecordController {



    @FXML
    private JFXTimePicker in_time_timepicker;

    @FXML
    private JFXTimePicker out_time_timepicker;

    @FXML
    private JFXTextField car_no_text_field;

    @FXML
    private JFXButton update_button;

    @FXML
    private JFXComboBox<?> category_combo_box;

    @FXML
    void handleCategory(ActionEvent event) {

    }

    @FXML
    void handleUpdate(ActionEvent event) {

    }


    void setTextFields(StaffAttendance attendance)
    {

    }

    void setTextFields(ParkingVehicle vehicle)
    {

    }
}
