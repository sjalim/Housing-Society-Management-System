package sample.controllers;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import java.util.regex.Pattern;


public class LoginPageController implements Initializable {


    //    DBConnect dbConnect = new DBConnect();
    DatabaseHandler dbConnect =
            new DatabaseHandler();
    Connection connection =
            dbConnect.getDbConnection();
    Preferences userPreferences =
            Preferences.userRoot();
    public static final String USER_ID =
            "USER_ID";
    public static final String USER_STATUS =
            "USER_STATUS";
    public static final String RESIDENT_STATUS
            = "RESIDENT";
    public static final String GUARD_STATUS =
            "GUARD";
    public static final String MANAGER_STATUS =
            "MANAGER";
    public static final String ALLOCATION_STATUS = "TENANT";
    public static final String ALLOCATION_STATUS_OWNED = "OWNED";
    public static final String ALLOCATION_STATUS_RENTED = "RENTED";

    ArrayList<String> typeNames;

    String curLoginType;


    String userId, password, passwordDB,
            userIdDB, AllocationStatus;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    //at least 1 digit
                    "(?=.*[a-z])" +
                    //at least 1 lower case letter
                    "(?=.*[A-Z])" +
                    //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +
                    //any letter
                    "(?=.*[@#$%^&+=])" +
                    //at least 1 special character
                    "(?=\\S+$)" +
                    //no white spaces
                    ".{4,}" +
                    //at least 4 characters
                    "$");


    @FXML
    private AnchorPane rootPane;


    @FXML
    private ComboBox<String> login_type_combo_box;

    @FXML
    private Label error_label;

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


//        int validateUser = validateUserId
//        (userId);


        try {


            if (validatePassword(password)) {

                if (curLoginType.isEmpty()) {

                    error_label.setVisible(true);

                } else {


                    if (curLoginType.equals(RESIDENT_STATUS)) {
                        String query = "select " +
                                "FlatOwner" +
                                ".FlatNumber, " +
                                "Password," +
                                "AllocationStatus " +
                                "from FlatLogin" +
                                " " +
                                "inner join " +
                                "FlatOwner on " +
                                "FlatOwner" +
                                ".FlatNumber = " +
                                "FlatLogin" +
                                ".FlatNumber " +
                                "where " +
                                "FlatLogin" +
                                ".FlatNumber = " +
                                "'" + userId +
                                "';";



                        Statement statement =
                                connection.createStatement();
                        ResultSet resultSet =
                                statement.executeQuery(query);
                        if (resultSet.next()) {


                            userIdDB =
                                    resultSet.getString(1);

                            if (userIdDB.equals(userId)) {
                                passwordDB =
                                        resultSet.getString(2);
                                AllocationStatus =
                                        resultSet.getString(3);

                                if (passwordDB.equals(password)) {

                                    if (AllocationStatus.equals("OWNED")) {

                                        Parent ownedParent = FXMLLoader.load(getClass().getResource("/sample/views/flat" +
                                                "/FlatOwner.fxml"));
                                        Scene ownedScene = new Scene(ownedParent, 1200, 700);
                                        userPreferences.put(USER_STATUS, RESIDENT_STATUS);
                                        userPreferences.put(USER_ID, userId);
                                        userPreferences.put(ALLOCATION_STATUS, ALLOCATION_STATUS_OWNED);
                                        loadNext(ownedScene);
                                    } else if (AllocationStatus.equals("RENTED")) {

                                        Parent tenantParent = FXMLLoader.load(getClass().getResource("/sample/views/flat" +
                                                "/Tenant.fxml"));
                                        Scene tenantScene = new Scene(tenantParent, 1200, 700);
                                        userPreferences.put(USER_STATUS, RESIDENT_STATUS);
                                        userPreferences.put(USER_ID, userId);
                                        userPreferences.put(ALLOCATION_STATUS, ALLOCATION_STATUS_RENTED);
                                        loadNext(tenantScene);

                                    } else {

                                        AlertDialog("Invalid User Name or Password");
                                    }

                                } else {
                                    AlertDialog(
                                            "Incorrect User ID!");
                                }
                            }
                        }



                    } else if (curLoginType.equals(GUARD_STATUS)) {

                        String query = "select " +
                                "Password from " +
                                "GuardLogin " +
                                "inner " +
                                "join Guard on " +
                                "Guard" +
                                ".GuardId" +
                                "=GuardLogin" +
                                ".GuardId " +
                                "where Guard" +
                                ".Mobile=" + userId + ";";



                        Statement statement =
                                connection.createStatement();
                        ResultSet resultSet =
                                statement.executeQuery(query);

                        if (resultSet.next()) {
                            String passwordDB =
                                    resultSet.getString(1);

                            if (passwordDB.equals(password)) {
                                Parent guardParent = FXMLLoader.load(getClass().getResource("/sample/views/guard/Guard" +
                                        ".fxml"));
                                Scene guardScene = new Scene(guardParent, 1200, 700);
                                userPreferences.put(USER_STATUS, GUARD_STATUS);
                                userPreferences.put(USER_ID, userId);
                                loadNext(guardScene);
                            } else {
                                AlertDialog(
                                        "Incorrect Password!");
                            }

                            query =
                                    "select Password from GuardLogin inner join Guard on Guard.GuardId=GuardLogin.GuardId " +
                                            "where Guard.Mobile=" + userId + ";";


                            statement =
                                    connection.createStatement();
                            resultSet =
                                    statement.executeQuery(query);

                            if (resultSet.next()) {
                                passwordDB
                                        =
                                        resultSet.getString(1);

                                if (passwordDB.equals(password)) {
                                    Parent guardParent
                                            =
                                            FXMLLoader.load(getClass().getResource("/sample/views/guard/guard_dashboard.fxml"));
                                    Scene guardScene =
                                            new Scene(guardParent, 1200, 500);
                                    userPreferences.put(USER_STATUS, GUARD_STATUS);
                                    userPreferences.put(USER_ID, userId);
                                    loadNext(guardScene);
                                } else {
                                    AlertDialog("Incorrect Password!");

                                }
                            }
                        } else if (curLoginType.equals(MANAGER_STATUS)) {


                            query = "select " +
                                    "ManagerPassword from Manager where Mobile=" + userId + ";";
                            statement =
                                    connection.createStatement();
                            resultSet =
                                    statement.executeQuery(query);
                            if (resultSet.next()) {
                                passwordDB =
                                        resultSet.getString(1);

                                if (passwordDB.equals(password)) {
                                    Parent managerParent = FXMLLoader.load(getClass().getResource("/sample/views/manager" +
                                            "/manager_dashboard.fxml"));
                                    Scene managerScene = new Scene(managerParent, 1200, 700);
                                    userPreferences.put(USER_STATUS, MANAGER_STATUS);
                                    userPreferences.put(USER_ID, userId);
                                    loadNext(managerScene);

                                } else {
                                    AlertDialog(
                                            "Incorrect Password!");
                                }
                            }


                        } else {
                            AlertDialog(
                                    "Invalid User " +
                                            "Name or Password");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    @FXML
    void handleLoginType(ActionEvent event) {

        curLoginType =
                login_type_combo_box.getValue();
    }

    @FXML
    void handleLoginType(ActionEvent event) {

        curLoginType =
                login_type_combo_box.getValue();
    }

    private int validateUserId(String userId) {

        RequiredFieldValidator validator =
                new RequiredFieldValidator();
        if (userId.isEmpty()) {
            validator.setMessage("User name is " +
                    "empty");
            userIdTextField.getValidators().add(validator);
            userIdTextField.validate();
            return 0;
        } else {
            validator.setMessage("");
            userIdTextField.validate();


            if (connection != null) {

                try {
                    String query = "select " +
                            "COUNT(FlatNumber) " +
                            "from FlatLogin " +
                            "where FlatNumber =" +
                            " '" + userId + "';";

                    Statement statement =
                            connection.createStatement();
                    ResultSet resultSet =
                            statement.executeQuery(query);


                    if (resultSet.next()) {
                        if (!resultSet.getString(1).equals("0")) {
                            System.out.println(
                                    "alim");
                            return 1;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    String query = "select " +
                            "count(Mobile) from" +
                            " Guard where " +
                            "Mobile = '"
                            + userId + "';";

                    Statement statement =
                            connection.createStatement();
                    ResultSet resultSet =
                            statement.executeQuery(query);
                    System.out.println("alim1");
                    if (resultSet.next()) {

                        if (!resultSet.getString(1).equals("0")) {
                            System.out.println(
                                    "alim2");
                            return 2;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    String query = "select " +
                            "count(Mobile) from" +
                            " Manager where " +
                            "Mobile = '"
                            + userId + "';";
                    Statement statement =
                            connection.createStatement();
                    ResultSet resultSet =
                            statement.executeQuery(query);
                    if (resultSet.next()) {
                        if (!resultSet.getString(1).equals("0")) {
                            return 3;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("alim4");
            }


            return 0;
        }
    }


    private boolean validatePassword(String password) {

        RequiredFieldValidator passValidator =
                new RequiredFieldValidator();
        if (password.isEmpty()) {
            passValidator.setMessage("Password " +
                    "is empty!");
            passwordTextField.getValidators().add(passValidator);
            passwordTextField.validate();
            return false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            passValidator =
                    new RequiredFieldValidator();
            passValidator.setMessage("Password " +
                    "too weak");
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
        Stage curStage =
                (Stage) rootPane.getScene().getWindow();
        curStage.setScene(scene);
        curStage.show();
    }

    void AlertDialog(String message) {
        Alert alert =
                new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }


    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {
        typeNames = new ArrayList<>();

        typeNames.add(MANAGER_STATUS);
        typeNames.add(RESIDENT_STATUS);
        typeNames.add(GUARD_STATUS);

        ObservableList list =
                FXCollections.observableArrayList(typeNames);

        login_type_combo_box.setItems(list);

        login_type_combo_box.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

                if (!t1.isEmpty()) {

                    error_label.setVisible(false);
                }
            }
        });


    }
}
