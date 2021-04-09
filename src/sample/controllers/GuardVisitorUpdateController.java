package sample.controllers;

import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class GuardVisitorUpdateController implements Initializable {

    @FXML
    private TextField name_text_field;

    @FXML
    private TextField gate_no_text_field;

    @FXML
    private JFXTimePicker in_time_timepicker;

    @FXML
    private JFXTimePicker out_timepicker;

    @FXML
    private TextField flat_no_text_field;

    @FXML
    void handleSave(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
