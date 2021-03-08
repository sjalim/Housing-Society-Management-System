package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import com.jfoenix.validation.base.ValidatorBase;
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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManagerStaffAddController implements Initializable {

    String name, mobile, nid, age, present_address, permanent_address, type = "", start_time, end_time, salary,
            newType = "";

    DatabaseHandler dbConnect = new DatabaseHandler();
    Connection connection = dbConnect.getDbConnection();
    Statement statement = null;
    ArrayList<String> typeNames;
    String query;

    RequiredFieldValidator Invalidate;
    @FXML
    private JFXTextField name_text_field;

    @FXML
    private JFXTextField mobile_text_field;

    @FXML
    private JFXTextField nid_text_field;

    @FXML
    private JFXTextField age_text_field;

    @FXML
    private JFXTextField present_address_text_field;

    @FXML
    private JFXTextField permanent_address_text_field;

    @FXML
    private JFXButton confirm_button;

    @FXML
    private JFXTimePicker shift_start_time_picker;

    @FXML
    private JFXTimePicker shift_end_time_picker;

    @FXML
    private JFXButton preview_button;

    @FXML
    private JFXTextField salary_text_field;


    @FXML
    private ComboBox<String> type_combo_box;

    @FXML
    private ImageView type_error_icon_imageview;

    @FXML
    private Label type_error_label;

    @FXML
    private Label mobile_number_error_label;

    @FXML
    private Label nid_number_error_label;

    @FXML
    private Label age_number_error_label;

    @FXML
    private Label salary_number_error_label;

    @FXML
    private JFXTextField new_staff_type_text_field;


    public ManagerStaffAddController() throws SQLException, ClassNotFoundException {

    }

    @FXML
    void handleAddNewType(MouseEvent event) {
//        try {
//            Parent parent = FXMLLoader.load(this.getClass().getResource("/sample/views/utils/TypeAddition.fxml"));
//            Stage stage = new Stage();
//            stage.setTitle("Add new type");
//            Scene scene = new Scene(parent);
//            stage.setScene(scene);
//            stage.show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/sample/views/utils/TypeAddition.fxml"));
        DialogPane dialogPane = null;
        try {
            dialogPane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.setTitle("Add new type");
        Optional<ButtonType> clickedButton = dialog.showAndWait();

        if (clickedButton.get() == ButtonType.APPLY) {

            newType = new_staff_type_text_field.getText();
            if (!newType.isEmpty()) {

                String query = "insert into StaffTypeList " +
                        "values('" + newType + "')";

                try {

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.executeQuery();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                typeNames.add(newType);
                ObservableList<String> list = FXCollections.observableList(typeNames);
                type_combo_box.setItems(list);


            } else {
                RequiredFieldValidator validator = new RequiredFieldValidator();
                validator.setMessage("Fill the Field!");
                new_staff_type_text_field.getValidators().add(validator);
                new_staff_type_text_field.validate();

            }
        }
        new_staff_type_text_field.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                new_staff_type_text_field.getValidators().add(Invalidate);
                new_staff_type_text_field.validate();
            }
        });

    }

    @FXML
    void confirmAdd(ActionEvent event) {

        name = name_text_field.getText();
        mobile = mobile_text_field.getText();
        present_address = present_address_text_field.getText();
        permanent_address = permanent_address_text_field.getText();
        nid = nid_text_field.getText();
        age = age_text_field.getText();
        salary = salary_text_field.getText();
        try {
            start_time = shift_start_time_picker.getValue().format(DateTimeFormatter.ofPattern("hh:mm a"));
            end_time = shift_end_time_picker.getValue().format(DateTimeFormatter.ofPattern("hh:mm a"));
        } catch (Exception e) {
            shift_start_time_picker.validate();
            shift_end_time_picker.validate();
        }
        if (!name.isEmpty() && !mobile.isEmpty() && !present_address.isEmpty() && !permanent_address.isEmpty() && !nid.isEmpty() && !age.isEmpty() && !type.isEmpty() && !salary.isEmpty()) {


            insert();
        }
        if (name.isEmpty()) {

            name_text_field.validate();

        }
        if (mobile.isEmpty()) {
            mobile_text_field.validate();

        }
        if (nid.isEmpty()) {
            nid_text_field.validate();

        }
        if (age.isEmpty()) {
            age_text_field.validate();
        }
        if (present_address.isEmpty()) {
            present_address_text_field.validate();
        }
        if (permanent_address.isEmpty()) {
            permanent_address_text_field.validate();

        }
        if (type.isEmpty()) {
            type_error_icon_imageview.setVisible(true);
            type_error_label.setVisible(true);
        }
        if (salary.isEmpty()) {
            salary_text_field.validate();
        }
    }


    private void insert() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/views/utils/ConfirmDialog.fxml"));
            DialogPane dialogPane = fxmlLoader.load();


            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Confirm");

            Optional<ButtonType> clickedButton = dialog.showAndWait();
            System.out.println("i am here");

            if (clickedButton.get() == ButtonType.YES) {
                String insertQuery = null;
                PreparedStatement preparedStatement = null;
                if (type.equals("Guard")) {
                    insertQuery = "INSERT INTO Guard (Name,Nid,Age,Salary,PresentAddress,PermanentAddress,StartTime," +
                            "EndTime,ManagerId,Mobile)" +
                            "VALUEs(?,?,?,?,?,?,?,?,?,?) ";

                    preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, nid);
                    preparedStatement.setInt(3, Integer.parseInt(age));
                    preparedStatement.setString(4, salary);
                    preparedStatement.setString(5, present_address);
                    preparedStatement.setString(6, permanent_address);
                    preparedStatement.setString(7, start_time);
                    preparedStatement.setString(8, end_time);
                    preparedStatement.setInt(9, 1);
                    preparedStatement.setString(10, mobile);
                    preparedStatement.executeUpdate();
                    preparedStatement.close();

                    Thread.sleep(2000);
                   String insetQueryIntoLogin = "insert into GuardLogin (Password)"+
                           "values(?)";
                 PreparedStatement  preparedStatementLogin = connection.prepareStatement(insetQueryIntoLogin);
                 preparedStatementLogin.setString(1,mobile);
                 preparedStatementLogin.executeUpdate();

                } else {
                    insertQuery = "INSERT INTO Staff (Name,Nid,Age,Salary,PresentAddress,PermanentAddress,StartTime," +
                            "EndTime,StaffType,ManagerId,Mobile)" +
                            "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                    preparedStatement = connection.prepareStatement(insertQuery);
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, nid);
                    preparedStatement.setInt(3, Integer.parseInt(age));
                    preparedStatement.setString(4, salary);
                    preparedStatement.setString(5, present_address);
                    preparedStatement.setString(6, permanent_address);
                    preparedStatement.setString(7, start_time);
                    preparedStatement.setString(8, end_time);
                    preparedStatement.setString(9, type);
                    preparedStatement.setInt(10, 1);
                    preparedStatement.setString(11, mobile);
                    preparedStatement.executeUpdate();
                }

                System.out.println("inserted in staff table");



            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void previewAdd(ActionEvent event) {
    }

    @FXML
    void typeComboBoxAction(ActionEvent event) {
        System.out.println("alim");
//        typeNames = new ArrayList<>();
//        query = "select TypeName from StaffTypeList";
//
//
//        try {
//            statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(query);
//            while (resultSet.next()) {
//                typeNames.add(resultSet.getString(1));
//            }
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//
//        System.out.println(typeNames);

        ObservableList<String> list = FXCollections.observableList(typeNames);
        type_combo_box.setItems(list);
        type = type_combo_box.getSelectionModel().getSelectedItem();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Empty Field!");
        name_text_field.getValidators().add(validator);
        mobile_text_field.getValidators().add(validator);
        salary_text_field.getValidators().add(validator);
        nid_text_field.getValidators().add(validator);
        age_text_field.getValidators().add(validator);
        permanent_address_text_field.getValidators().add(validator);
        present_address_text_field.getValidators().add(validator);
        shift_start_time_picker.getValidators().add(validator);
        shift_end_time_picker.getValidators().add(validator);
        type_error_label.setVisible(false);
        type_error_icon_imageview.setVisible(false);

        typeNames = new ArrayList<>();
        query = "select TypeName from StaffTypeList";

        mobile_number_error_label.setVisible(false);
        age_number_error_label.setVisible(false);
        nid_number_error_label.setVisible(false);
        salary_number_error_label.setVisible(false);

        Invalidate = new RequiredFieldValidator();
        Invalidate.setMessage("");

        name_text_field.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                name_text_field.getValidators().add(Invalidate);
                name_text_field.validate();

            }
        });

        mobile_text_field.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                mobile_text_field.getValidators().add(Invalidate);
                mobile_text_field.validate();

            }
        });

        nid_text_field.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                nid_text_field.getValidators().add(Invalidate);
                nid_text_field.validate();

            }
        });

        present_address_text_field.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                present_address_text_field.getValidators().add(Invalidate);
                present_address_text_field.validate();

            }
        });

        permanent_address_text_field.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.isEmpty()) {
                permanent_address_text_field.getValidators().add(Invalidate);
                permanent_address_text_field.validate();

            }
        });

        salary_text_field.textProperty().addListener((observableValue, s, t1) -> {
            salary_text_field.getValidators().add(Invalidate);
            salary_text_field.validate();
        });


//        shift_end_time_picker.editorProperty().addListener(new ChangeListener<TextField>() {
//            @Override
//            public void changed(ObservableValue<? extends TextField> observableValue, TextField textField,
//                                TextField t1) {
//                if (!t1.getText().isEmpty()) {
//                    shift_end_time_picker.getValidators().add(Invalidate);
//                    shift_end_time_picker.validate();
//
//                }
//            }
//        });
//
//        shift_start_time_picker.editorProperty().addListener(new ChangeListener<TextField>() {
//            @Override
//            public void changed(ObservableValue<? extends TextField> observableValue, TextField textField,
//                                TextField t1) {
//                if (!t1.getText().isEmpty()) {
//                    shift_start_time_picker.getValidators().add(Invalidate);
//                    shift_start_time_picker.validate();
//
//                }
//            }
//        });


        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                typeNames.add(resultSet.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(typeNames);

        ObservableList<String> list = FXCollections.observableList(typeNames);
        type_combo_box.setItems(list);

        mobile_text_field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                mobile_text_field.setText(newValue.replaceAll("[^\\d]", ""));
                mobile_number_error_label.setVisible(true);

            } else {
                mobile_number_error_label.setVisible(false);

            }
        });

        nid_text_field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                nid_text_field.setText(newValue.replaceAll("[^\\d]", ""));
                nid_number_error_label.setVisible(true);
            } else {
                nid_number_error_label.setVisible(false);

            }
        });


        age_text_field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                age_text_field.setText(newValue.replaceAll("[^\\d]", ""));
                age_number_error_label.setVisible(true);
            } else {
                age_number_error_label.setVisible(false);
            }
            if (!newValue.isEmpty()) {
                age_text_field.getValidators().add(Invalidate);
                age_text_field.validate();

            }

        });


        salary_text_field.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                salary_text_field.setText(newValue.replaceAll("[^\\d]", ""));
                salary_number_error_label.setVisible(true);
            } else {
                salary_number_error_label.setVisible(false);
            }
        });


    }
}
