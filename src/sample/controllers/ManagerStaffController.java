package sample.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import com.jfoenix.validation.RequiredFieldValidator;
import com.sun.source.tree.WhileLoopTree;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class ManagerStaffController implements Initializable {

    public static final String SELECTED_STAFF_ID = "staffId";

    DatabaseHandler databaseHandler =
            new DatabaseHandler();
    ObservableList<Staff> list;
    Timeline timeline;
    Preferences staffPreferences =
            Preferences.userRoot();
    String staffId =
            staffPreferences.get(SELECTED_STAFF_ID, "root");

    String typeSearchKey;
    ArrayList typeNames;
    LocalTime fromTime = null;
    LocalTime toTime = null;


    @FXML
    private JFXDatePicker from_datepicker;

    @FXML
    private JFXDatePicker to_datepicker;

    @FXML
    private TableColumn<String, Staff> nid;

    @FXML
    private ScrollPane rootScrollPane;

    @FXML
    private AnchorPane root_anchor_pane;

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
    private ComboBox<?> update_type;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private TextField search_text_field;


    @FXML
    private JFXTimePicker start_time_timepicker;

    @FXML
    private JFXTimePicker end_time_timepicker;
    @FXML
    private JFXButton add_button;

    @FXML
    private JFXButton update_button;

    @FXML
    private JFXButton delete_button;


    @FXML
    private TableView<Staff> table;

    @FXML
    private TableColumn<String, Staff> name;

    @FXML
    private TableColumn<String, Staff> type;

    @FXML
    private TableColumn<String, Staff> shift_start;

    @FXML
    private TableColumn<String, Staff> shift_end;

    @FXML
    private TableColumn<String, Staff> mobile;

    @FXML
    private TableColumn<String, Staff> present_address;

    @FXML
    private TableColumn<String, Staff> permanent_address;

    @FXML
    private TableColumn<Integer, Staff> age;

    @FXML
    private TableColumn<Integer, Staff> salary;

    @FXML
    private ComboBox<String> type_combo_box;


    @FXML
    void handleFromTime(ActionEvent event) {
        fromTime =
                start_time_timepicker.getValue();
        list.clear();

        if (fromTime != null && toTime != null) {

            loadByDate(fromTime, toTime);
            table.setItems(list);
        } else if (fromTime != null && toTime == null) {

            String query = "select Name," +
                    "StaffType,StartTime," +
                    "EndTime," +
                    "Mobile,PresentAddress," +
                    "PermanentAddress,Age,Nid," +
                    "Salary,StaffId from Staff " +
                    "where StartTime >= '" + Time.valueOf(fromTime) + "';";
            try {
                Statement statement =
                        databaseHandler.getDbConnection().createStatement();
                ResultSet resultSet =
                        statement.executeQuery(query);
                while (resultSet.next()) {
                    list.add(new Staff(resultSet.getString(1), resultSet.getString(2), resultSet.getTime(3), resultSet.getTime(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getInt(8), resultSet.getString(9), resultSet.getInt(10), resultSet.getInt(11)));

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            query = "select Name,StartTime," +
                    "EndTime," +
                    "Mobile,PresentAddress," +
                    "PermanentAddress,Age,Nid," +
                    "Salary,GuardId from Guard " +
                    "where StartTime >= '" + Time.valueOf(fromTime) +
                    "';";
            try {
                Statement statement =
                        databaseHandler.getDbConnection().createStatement();
                ResultSet resultSet =
                        statement.executeQuery(query);
                while (resultSet.next()) {
                    list.add(new Staff(resultSet.getString(1), "Guard", resultSet.getTime(2), resultSet.getTime(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7), resultSet.getString(8), resultSet.getInt(9), resultSet.getInt(10)));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("error at from " +
                    "date");
        }
    }


    @FXML
    void handleToTime(ActionEvent event) {
        toTime =
                end_time_timepicker.getValue();
        list.clear();

        if (fromTime != null && toTime != null) {

            loadByDate(fromTime, toTime);
            table.setItems(list);
        } else if (fromTime == null && toTime != null) {

            String query = "select Name," +
                    "StaffType,StartTime," +
                    "EndTime," +
                    "Mobile,PresentAddress," +
                    "PermanentAddress,Age,Nid," +
                    "Salary,StaffId from Staff " +
                    "where EndTime <= '" + Time.valueOf(toTime) + "';";
            try {
                Statement statement =
                        databaseHandler.getDbConnection().createStatement();
                ResultSet resultSet =
                        statement.executeQuery(query);
                while (resultSet.next()) {
                    list.add(new Staff(resultSet.getString(1), resultSet.getString(2), resultSet.getTime(3), resultSet.getTime(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getInt(8), resultSet.getString(9), resultSet.getInt(10), resultSet.getInt(11)));

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            query = "select Name,StartTime," +
                    "EndTime," +
                    "Mobile,PresentAddress," +
                    "PermanentAddress,Age,Nid," +
                    "Salary,GuardId from Guard " +
                    "where EndTime <= '" + Time.valueOf(toTime) +
                    "';";
            try {
                Statement statement =
                        databaseHandler.getDbConnection().createStatement();
                ResultSet resultSet =
                        statement.executeQuery(query);
                while (resultSet.next()) {
                    list.add(new Staff(resultSet.getString(1), "Guard", resultSet.getTime(2), resultSet.getTime(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7), resultSet.getString(8), resultSet.getInt(9), resultSet.getInt(10)));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("error at from " +
                    "date");
        }
    }


    private void loadByDate(LocalTime fromTime,
                            LocalTime toTime) {


        String query = "select Name," +
                "StaffType,StartTime," +
                "EndTime," +
                "Mobile,PresentAddress," +
                "PermanentAddress,Age,Nid," +
                "Salary,StaffId from Staff " +
                "where StartTime >= '" + Time.valueOf(fromTime) +
                "' and EndTime <= '" + Time.valueOf(toTime) + "';";
        try {
            Statement statement =
                    databaseHandler.getDbConnection().createStatement();
            ResultSet resultSet =
                    statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Staff(resultSet.getString(1), resultSet.getString(2), resultSet.getTime(3), resultSet.getTime(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getInt(8), resultSet.getString(9), resultSet.getInt(10), resultSet.getInt(11)));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        query = "select Name,StartTime," +
                "EndTime," +
                "Mobile,PresentAddress," +
                "PermanentAddress,Age,Nid," +
                "Salary,GuardId from Guard " +
                "where StartTime >= '" + Time.valueOf(fromTime) +
                "' and EndTime <= '" + Time.valueOf(toTime) + "';";
        try {
            Statement statement =
                    databaseHandler.getDbConnection().createStatement();
            ResultSet resultSet =
                    statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Staff(resultSet.getString(1), "Guard", resultSet.getTime(2), resultSet.getTime(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7), resultSet.getString(8), resultSet.getInt(9), resultSet.getInt(10)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void handleAdd(ActionEvent event) {

        Parent ownedParent = null;
        try {
            ownedParent =
                    FXMLLoader.load(getClass().getResource("/sample/views/manager/manager_staff_add.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(ownedParent,
                1000, 500);
        Stage curStage = new Stage();
        curStage.setScene(scene);
        curStage.show();
    }

    @FXML
    void handleDelete(ActionEvent event) throws IOException {
        ObservableList<Staff> staff = null;

        if (table.getSelectionModel().getSelectedItems().size() > 0) {
            staff =
                    table.getSelectionModel().getSelectedItems();


            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/views/manager/manager_staff_delete.fxml"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ManagerStaffDeleteController controller =
                    fxmlLoader.getController();
            controller.setStaffList(staff);
            Parent parent = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();
        }
        else {
            FXMLLoader fxmlLoader =
                    new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/views/utils/NoRowSelected.fxml"));
            DialogPane dialogPane =
                    fxmlLoader.load();


            Dialog<ButtonType> dialog =
                    new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Confirm");

            Optional<ButtonType> clickedButton
                    = dialog.showAndWait();

        }

    }


    @FXML
    void handleUpdate(ActionEvent event) {


        Staff staff =
                table.getSelectionModel().getSelectedItem();
        if (staff != null) {


            FXMLLoader fxmlLoader =
                    new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/views/manager/manager_staff_update.fxml"));

            try {
                fxmlLoader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ManagerStaffUpdateController controller = fxmlLoader.getController();
            controller.setTextFields(staff);
            Parent parent = fxmlLoader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.initStyle(StageStyle.UTILITY);
            stage.show();

            System.out.println(staff.getName());
        }
        else{
            FXMLLoader fxmlLoader =
                    new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/sample/views/utils/NoRowSelected.fxml"));
            DialogPane dialogPane =
                    null;
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





        }

    }

    @FXML
    void handleRefresh(MouseEvent event) {

        list.clear();
        load();
        table.setItems(list);
        type_combo_box.setPromptText("Staff " +
                "Type");
        type_combo_box.getSelectionModel().clearSelection();
        end_time_timepicker.getEditor().setText("");
        start_time_timepicker.getEditor().setText("");

    }


    @FXML
    void handleTypeFilter(ActionEvent event) {
        typeSearchKey =
                type_combo_box.getSelectionModel().getSelectedItem();
        if (typeSearchKey != null) {
            list.clear();
            loadByQuery();
            table.setItems(list);
        }
    }

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {


//         timeline = new Timeline(new KeyFrame
//         (Duration.seconds(1), actionEvent -> {

        //code

//        }));
//        timeline.setCycleCount(Timeline
//        .INDEFINITE);
//        timeline.play();


        search_text_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                loadBySearch(t1);
            }
        });


        System.out.println("timeline at staff");
        list = FXCollections.observableArrayList();
        initTable();
        load();
        table.setItems(list);

        table.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );


        String query = "select TypeName from " +
                "StaffTypeList";

        typeNames = new ArrayList();


        try {
            Statement statement =
                    databaseHandler.getDbConnection().createStatement();
            ResultSet resultSet =
                    statement.executeQuery(query);

            while (resultSet.next()) {
                typeNames.add(resultSet.getString(1));
            }


        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }


        ObservableList typeList =
                FXCollections.observableArrayList(typeNames);
        type_combo_box.setItems(typeList);

    }

    private void loadBySearch(String t1) {

        String query = "select * from(" +
                "select " +
                "GuardId as Id, Name,Nid, Age," +
                "Salary, " +
                "PresentAddress, " +
                "PermanentAddress, StartTime, " +
                "EndTime, Mobile, StaffType  " +
                "from Guard " +
                "union " +
                "select " +
                "StaffId as Id," +
                "Name,Nid, Age," +
                "Salary," +
                " PresentAddress, " +
                "PermanentAddress, StartTime, " +
                "EndTime, Mobile, StaffType  " +
                "from Staff" +
                ") as data " +
                "where " +
                "data.Name like '%" + t1 + "%' " +
                "or " +
                "data.Age like '%" + t1 + "%' " +
                "or " +
                "data.Salary like '%" + t1 +
                "%' " +
                "or " +
                "data.PresentAddress like '%" + t1 + "%' " +
                "or " +
                "data.PermanentAddress like '%" + t1 + "%' " +
                "or " +
                "data.Mobile like '%" + t1 +
                "%';";

        PreparedStatement preparedStatement =
                null;
        try {
            preparedStatement =
                    databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet =
                    preparedStatement.executeQuery();

            list.clear();
            while (resultSet.next()) {

                list.add(new Staff(resultSet.getString("Name"),
                        resultSet.getString(
                                "StaffType"),
                        resultSet.getTime(
                                "StartTime"),
                        resultSet.getTime(
                                "EndTime"),
                        resultSet.getString(
                                "Mobile"),
                        resultSet.getString(
                                "PresentAddress"),
                        resultSet.getString(
                                "PermanentAddress"),
                        resultSet.getInt("Age"),
                        resultSet.getString(
                                "Nid"),
                        resultSet.getInt(
                                "Salary"),
                        resultSet.getInt("Id")));


            }

            table.setItems(list);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    void initTable() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        shift_start.setCellValueFactory(new PropertyValueFactory<>("start_time"));
        shift_end.setCellValueFactory(new PropertyValueFactory<>("end_time"));
        mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        present_address.setCellValueFactory(new PropertyValueFactory<>("present_ad"));
        permanent_address.setCellValueFactory(new PropertyValueFactory<>("permanent_ad"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        nid.setCellValueFactory(new PropertyValueFactory<>("nid"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));


    }


    void loadByQuery() {
        String query = "select Name," +
                "StaffType,StartTime,EndTime," +
                "Mobile,PresentAddress," +
                "PermanentAddress,Age,Nid," +
                "Salary,StaffId from Staff " +
                "where StaffType = '" + typeSearchKey + "';";
        try {
            Statement statement =
                    databaseHandler.getDbConnection().createStatement();
            ResultSet resultSet =
                    statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Staff(resultSet.getString(1), resultSet.getString(2), resultSet.getTime(3), resultSet.getTime(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getInt(8), resultSet.getString(9), resultSet.getInt(10), resultSet.getInt(11)));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        query = "select Name,StartTime,EndTime," +
                "Mobile,PresentAddress," +
                "PermanentAddress,Age,Nid," +
                "Salary,GuardId from Guard " +
                "where StaffType = '" + typeSearchKey + "';";
        try {
            Statement statement =
                    databaseHandler.getDbConnection().createStatement();
            ResultSet resultSet =
                    statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Staff(resultSet.getString(1), "Guard", resultSet.getTime(2), resultSet.getTime(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7), resultSet.getString(8), resultSet.getInt(9), resultSet.getInt(10)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    void load() {
        String query = "select Name," +
                "StaffType,StartTime,EndTime," +
                "Mobile,PresentAddress," +
                "PermanentAddress,Age,Nid," +
                "Salary,StaffId from Staff";
        try {
            Statement statement =
                    databaseHandler.getDbConnection().createStatement();
            ResultSet resultSet =
                    statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Staff(resultSet.getString(1), resultSet.getString(2), resultSet.getTime(3), resultSet.getTime(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getInt(8), resultSet.getString(9), resultSet.getInt(10), resultSet.getInt(11)));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        query = "select Name,StartTime,EndTime," +
                "Mobile,PresentAddress," +
                "PermanentAddress,Age,Nid," +
                "Salary,GuardId from Guard";
        try {
            Statement statement =
                    databaseHandler.getDbConnection().createStatement();
            ResultSet resultSet =
                    statement.executeQuery(query);
            while (resultSet.next()) {
                list.add(new Staff(resultSet.getString(1), "Guard", resultSet.getTime(2), resultSet.getTime(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7), resultSet.getString(8), resultSet.getInt(9), resultSet.getInt(10)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
