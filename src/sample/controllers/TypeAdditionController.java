package sample.controllers;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


public class TypeAdditionController {

    String newType;

    DatabaseHandler dbConnect = new DatabaseHandler();
    Connection connection = dbConnect.getDbConnection();
    @FXML
    private JFXTextField new_staff_type_text_field;

    @FXML
    private Button add_button;

    @FXML
    private Button cancel_button;

    public TypeAdditionController() throws SQLException, ClassNotFoundException {
    }

    @FXML
    void handleAdd(ActionEvent event) {

        newType = new_staff_type_text_field.getText();
        if (!newType.isEmpty()) {

            String query = "insert into StaffTypeList " +
                    "values('" + newType + "')";
            Statement statement = null;
            try {

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.executeQuery();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
                Stage stage = (Stage) new_staff_type_text_field.getScene().getWindow();
                stage.close();

            }

        } else {
            RequiredFieldValidator validator = new RequiredFieldValidator();
            validator.setMessage("Fill the Field!");
            new_staff_type_text_field.getValidators().add(validator);
            new_staff_type_text_field.validate();

        }
    }

    @FXML
    void handleCancel(ActionEvent event) {
        Stage stage = (Stage) new_staff_type_text_field.getScene().getWindow();
        stage.close();

    }


}
