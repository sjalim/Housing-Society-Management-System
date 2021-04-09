package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Optional;

public class GuardUpdateRecordController {


    StaffAttendance staffAttendance = null;
    ParkingVehicle parkingVehicle = null;
    Time in_time, out_time;


    DatabaseHandler databaseHandler = new DatabaseHandler();


    @FXML
    private JFXTimePicker in_time_timepicker;

    @FXML
    private JFXTimePicker out_time_timepicker;

    @FXML
    private JFXButton update_button;

    @FXML
    private Label category_label;

    @FXML
    private Label number_label;

    @FXML
    void handleUpdate(ActionEvent event) {

        in_time =
                Time.valueOf(in_time_timepicker.getValue());
        out_time =
                Time.valueOf(out_time_timepicker.getValue());
        if (staffAttendance != null) {
            String query = "update " +
                    "StaffAttTrack set InTime =" +
                    " '" + in_time + "', " +
                    "OutTime =" +
                    " '" + out_time + "' where " +
                    "TrackId = '" + staffAttendance.getTrackId() + "'";
            PreparedStatement preparedStatement
                    =
                    null;
            try {
                preparedStatement =
                        databaseHandler.getDbConnection().prepareStatement(query);
                preparedStatement.executeUpdate();


                Node node =
                        (Node)  event.getSource();
                Stage stage =
                        (Stage)  node.getScene().getWindow();
                stage.close();

                FXMLLoader fxmlLoader =
                        new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/sample/views/utils/RecordStored.fxml"));
                DialogPane dialogPane = null;
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
                System.out.println("i am here");

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if (parkingVehicle != null) {
            in_time =
                    Time.valueOf(in_time_timepicker.getValue());
            out_time =
                    Time.valueOf(out_time_timepicker.getValue());
            if (parkingVehicle != null) {
                String query = "update ParkingSlotTrack" +
                        " set InTime =" +
                        " '" + in_time + "', " +
                        "OutTime =" +
                        " '" + out_time + "' where " +
                        "TrackId = '" + parkingVehicle.getTrackId() + "'";
                PreparedStatement preparedStatement
                        =
                        null;
                try {
                    preparedStatement =
                            databaseHandler.getDbConnection().prepareStatement(query);
                    preparedStatement.executeUpdate();

                  Node node =
                          (Node)  event.getSource();
                  Stage stage =
                          (Stage)  node.getScene().getWindow();
                  stage.close();


                    FXMLLoader fxmlLoader =
                            new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("/sample/views/utils/RecordStored.fxml"));
                    DialogPane dialogPane = null;
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
                    System.out.println("i am here");


                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            } else {

            }

        }
    }

    void setTextFields(StaffAttendance attendance) {
        this.staffAttendance = attendance;
        category_label.setText("Staff");
        number_label.setText(String.valueOf(staffAttendance.getId()));
        if(attendance.getTimeIn()!=null)
        {
            in_time_timepicker.setValue(attendance.getTimeIn().toLocalTime());
        }

        if(attendance.getTimeOut()!=null)
        {
            out_time_timepicker.setValue(attendance.getTimeOut().toLocalTime());
        }

        parkingVehicle = null;
    }

    void setTextFields(ParkingVehicle vehicle) {
        parkingVehicle = vehicle;
        category_label.setText("Parking");
        number_label.setText(String.valueOf(parkingVehicle.getCarNumber()));

        if(vehicle.getInTime()!=null)
        {
            in_time_timepicker.setValue(vehicle.getInTime().toLocalTime());
        }

        if(vehicle.getOutTime()!=null)
        {
            out_time_timepicker.setValue(vehicle.getOutTime().toLocalTime());
        }

        staffAttendance = null;
    }
}
