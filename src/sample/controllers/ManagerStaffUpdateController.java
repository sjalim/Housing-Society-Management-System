package sample.controllers;

import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class ManagerStaffUpdateController implements Initializable {

    Staff staff = null;
    Preferences staffPreferences =
            Preferences.userRoot();

    DatabaseHandler databaseHandler =
            new DatabaseHandler();

    ArrayList<String> typeNames;
    @FXML
    private DialogPane dialogPane;

    @FXML
    private ScrollPane rootScrollPane;

    @FXML
    private AnchorPane root_anchor_pane;

    @FXML
    private TextField update_salary;

    @FXML
    private TextField update_name;

    @FXML
    private TextField update_mobile;

    @FXML
    private TextField update_permanent_ad;

    @FXML
    private TextField update_present_ad;

    @FXML
    private TextField update_age;

    @FXML
    private TextField update_nid;

    @FXML
    private JFXTimePicker update_start_time;

    @FXML
    private JFXTimePicker update_end_time;

    @FXML
    private ComboBox<String> update_type;

    @FXML
    void handleCancel(ActionEvent event) {

        Node node = (Node) (event.getSource());
        Stage stage =
                (Stage) node.getScene().getWindow();
        stage.close();

    }

    @FXML
    void handleSave(ActionEvent event) {
        String query = null;


        try {
            PreparedStatement preparedStatement = null;
            if (!staff.getType().equals("")) {
                if (staff.getType().equals(
                        "Guard")) {
                    query = "update " +
                            "Guard " +
                            "set Name = ?," +
                            "Nid = ?," +
                            "Age = ?," +
                            "Salary = ?," +
                            "PresentAddress = " +
                            "?," +
                            "PermanentAddress =" +
                            " ?," +
                            "StartTime = ?," +
                            "EndTime = ?," +
                            "Mobile = ? " +
                            "where GuardId = "
                            + staff.getId() +";";

                    preparedStatement =
                            databaseHandler.getDbConnection().prepareStatement(query);

                } else {
                    query = "update " +
                            "Staff " +
                            "set Name = ?," +
                            "Nid = ?," +
                            "Age = ?," +
                            "Salary = ?," +
                            "PresentAddress = " +
                            "?," +
                            "PermanentAddress =" +
                            " ?," +
                            "StartTime = ?," +
                            "EndTime = ?," +
                            "Mobile = ?," +
                            "StaffType = ? " +
                            "where StaffId = "
                            + staff.getId()+";";
                    System.out.println(
                            "staff id :"+staff
                                    .getId());

                    preparedStatement =
                            databaseHandler.getDbConnection().prepareStatement(query);
                    String type =
                            update_type.getSelectionModel().getSelectedItem();

                    preparedStatement.setString(10, type);

                }
                preparedStatement.setString(1,
                        update_name.getText());
                preparedStatement.setString(2,
                        update_nid.getText());
                preparedStatement.setInt(3,
                        Integer.parseInt(update_age.getText()));
                preparedStatement.setInt(4,
                        Integer.parseInt(update_salary.getText()));
                preparedStatement.setString(5,
                        update_present_ad.getText());
                preparedStatement.setString(6,
                        update_permanent_ad.getText());

                preparedStatement.setString(7,
                        update_start_time.getValue().format(DateTimeFormatter.ofPattern("hh:mm a")) );

                preparedStatement.setString(8,
                        update_end_time.getValue().format(DateTimeFormatter.ofPattern("hh:mm a")));


                preparedStatement.setString(9,
                        staff.getMobile());

                preparedStatement.executeUpdate();

                Node node =
                        (Node) (event.getSource());
                Stage stage =
                        (Stage) node.getScene().getWindow();
                stage.close();


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

    }

    public void setTextFields(Staff staff) {
        this.staff = staff;
        update_name.setText(staff.getName());
        update_age.setText(String.valueOf
                (staff.getAge()));
        update_mobile.setText(staff.getMobile
                ());
        update_nid.setText(String.valueOf
                (staff.getNid()));
        update_present_ad.setText(staff
                .getPresent_ad());
        update_permanent_ad.setText(staff
                .getPermanent_ad());
        update_type.setValue(staff
                .getType());
        update_salary.setText(String.valueOf(staff.salary));

        LocalTime start_time= LocalTime.parse(
                staff.getShift_start(),
                DateTimeFormatter.ofPattern(
                        "hh:mm a"));
        update_start_time.setValue(start_time);

        LocalTime end_time= LocalTime.parse(
                staff.getShift_end(),
                DateTimeFormatter.ofPattern(
                        "hh:mm a"));
        update_end_time.setValue(end_time);







        typeNames = new ArrayList<>();

        String query = "select TypeName from " +
                "StaffTypeList";
        Statement statement =
                null;
        try {
            statement =
                    databaseHandler.getDbConnection().createStatement();
            ResultSet resultSet =
                    statement.executeQuery(query);
            while (resultSet.next()) {
                typeNames.add(resultSet.getString(1));
            }
            ObservableList<String> list =
                    FXCollections.observableList(typeNames);
            update_type.setItems(list);




        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }
}
