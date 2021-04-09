package sample.controllers;

import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class GuardVisitorController implements Initializable {

    String inTIme, outTIme, gateNo, name, flatNo;
    int guardNo;
    Preferences userPreferences = Preferences.userRoot();

    DatabaseHandler databaseHandler = new DatabaseHandler();

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
    void handleSave(ActionEvent event) throws SQLException, ClassNotFoundException {
        name = name_text_field.getText();
        inTIme = in_time_timepicker.getValue().format(DateTimeFormatter.ofPattern("hh:mm a"));
        outTIme = out_timepicker.getValue().format(DateTimeFormatter.ofPattern("hh:mm a"));
        gateNo = gate_no_text_field.getText();
        flatNo = flat_no_text_field.getText();



        if (!name.isEmpty() && !inTIme.isEmpty() && outTIme.isEmpty() && !gateNo.isEmpty() && !flatNo.isEmpty()) {
            String query = "insert into Visitor " +
                    "values (?,?,?,?,?,?)";

            PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, flatNo);
            preparedStatement.setString(3, inTIme);
            preparedStatement.setString(4, outTIme);
            preparedStatement.setInt(5, guardNo);
            preparedStatement.setInt(6, Integer.parseInt(gateNo));

            preparedStatement.executeUpdate();

        }

        if (!name.isEmpty() && inTIme.isEmpty() && !outTIme.isEmpty() && !gateNo.isEmpty() && !flatNo.isEmpty()) {
            String query = "insert into Visitor " +
                    "values (?,?,?,?,?,?)";

            PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, flatNo);
            preparedStatement.setString(3, null);
            preparedStatement.setString(4, outTIme);
            preparedStatement.setInt(5, guardNo);
            preparedStatement.setInt(6, Integer.parseInt(gateNo));

            preparedStatement.executeUpdate();

        }

        if (!name.isEmpty() && !inTIme.isEmpty() && !outTIme.isEmpty() && !gateNo.isEmpty() && !flatNo.isEmpty()) {
            String query = "insert into Visitor " +
                    "values (?,?,?,?,?,?)";

            PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, flatNo);
            preparedStatement.setString(3, inTIme);
            preparedStatement.setString(4, outTIme);
            preparedStatement.setInt(5, guardNo);
            preparedStatement.setInt(6, Integer.parseInt(gateNo));

            preparedStatement.executeUpdate();

        }


        Node node = (Node)(event.getSource());
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();


    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String guardId = userPreferences.get(LoginPageController.USER_ID, "root");

        String query = "select GuardId from Guard " +
                "where Mobile = '" + guardId + "';";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();


            if(resultSet.next())
            {
                guardNo  = resultSet.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
