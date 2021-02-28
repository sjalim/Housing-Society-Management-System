package sample.controllers;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;


public class LoginPageController {


    //    DBConnect dbConnect = new DBConnect();
    DatabaseHandler dbConnect = new DatabaseHandler();
    Connection connection = dbConnect.getDbConnection();


    String userId, password, passwordDB, userIdDB, AllocationStatus;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    @FXML
    private AnchorPane rootPane;


    @FXML
    private Label forgotPasswordLabel;

    @FXML
    private Label signUpButton;

    @FXML
    private JFXTextField userIdTextField;

    @FXML
    private JFXPasswordField passwordTextField;

    @FXML
    private JFXButton loginButton;

    public LoginPageController() throws SQLException, ClassNotFoundException {
    }


    @FXML
    void loginButtonPressed(ActionEvent event) throws IOException {


        userId = userIdTextField.getText();
        password = passwordTextField.getText();


        int validateUser = validateUserId(userId);

        if (validateUser > 0 && validatePassword(password)) {


            if (validateUser == 1) {
                String query = "select FlatOwner.FlatNumber, Password,AllocationStatus from FlatLogin inner join " +
                        "FlatOwner on FlatOwner" +
                        ".FlatNumber = FlatLogin.FlatNumber where FlatLogin.FlatNumber = '" + userId + "';";


                try {

                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    if (resultSet.next()) {

                        userIdDB = resultSet.getString(1);

                        if (userIdDB.equals(userId)) {
                            passwordDB = resultSet.getString(2);
                            AllocationStatus = resultSet.getString(3);
                            if (AllocationStatus.equals("OWNED")) {
                                Parent ownedParent = FXMLLoader.load(getClass().getResource("/sample/views/flat" +
                                        "/FlatOwner.fxml"));
                                Scene ownedScene = new Scene(ownedParent, 1200, 700);
                                loadNext(ownedScene);
                            } else if (AllocationStatus.equals("RENTED")) {
                                Parent tenantParent = FXMLLoader.load(getClass().getResource("/sample/views/flat" +
                                        "/Tenant.fxml"));
                                Scene tenantScene = new Scene(tenantParent, 1200, 700);
                                loadNext(tenantScene);

                            } else {

                                AlertDialog("Invalid User Name or Password");
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (validateUser == 2) {

                Parent guardParent = FXMLLoader.load(getClass().getResource("/sample/views/guard/Guard.fxml"));
                Scene guardScene = new Scene(guardParent, 1200, 700);
                loadNext(guardScene);

            } else {

                AlertDialog("Invalid User Name or Password");
            }


        } else {
            AlertDialog("Invalid User Name or Password");
        }


    }

    private int validateUserId(String userId) {

            RequiredFieldValidator validator = new RequiredFieldValidator();
        if (userId.isEmpty()) {
            validator.setMessage("User name is empty");
            userIdTextField.getValidators().add(validator);
            userIdTextField.validate();
            return 0;
        } else {
            validator.setMessage("");
            userIdTextField.validate();


            if (connection != null) {

                try {
                    String query = "select COUNT(FlatNumber) from FlatLogin where FlatNumber = '" + userId + "';";

                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);


                    if (resultSet.next()) {
                        if (!resultSet.getString(1).equals("0")) {
                            System.out.println("alim");
                            return 1;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    String query = "select count(Mobile) from Guard where Mobile = '"
                            + userId + "';";

                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);
                    System.out.println("alim1");
                    if (resultSet.next()) {

                        if (!resultSet.getString(1).equals("0")) {
                            System.out.println("alim2");
                            return 2;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userId.toLowerCase() == "admin") {
                    return 3;
                }

                System.out.println("alim4");
            }


            return 0;
        }
    }


    private boolean validatePassword(String password) {

            RequiredFieldValidator passValidator = new RequiredFieldValidator();
        if (password.isEmpty()) {
            passValidator.setMessage("Password is empty!");
            passwordTextField.getValidators().add(passValidator);
            passwordTextField.validate();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {

             passValidator = new RequiredFieldValidator();
            passValidator.setMessage("Password too weak");
            passwordTextField.getValidators().add(passValidator);
            passwordTextField.validate();
            return false;
        } else {
            passValidator.setMessage("");
            passwordTextField.validate();
            return true;
        }
    }


    private void loadNext(Scene scene) {
        Stage curStage = (Stage) rootPane.getScene().getWindow();


        curStage.setScene(scene);
        curStage.show();


    }

    void AlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();

    }


}
