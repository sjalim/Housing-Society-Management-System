package sample.controllers;

import com.jfoenix.controls.JFXTimePicker;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
    private ImageView search_image_view;

    @FXML
    private Button add_button;

    @FXML
    private Button update_text_field;

    @FXML
    private Button delete_text_field;

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
    private ComboBox<?> type_combo_box;


    @FXML
    private Label search_text_label;

    @FXML
    private Label search_key_label;


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
        }
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

    @FXML
    void handleSearch(MouseEvent event) {


    }

    @FXML
    void handleUpdate(ActionEvent event) {


        Staff staff =
                table.getSelectionModel().getSelectedItem();

        FXMLLoader fxmlLoader = new FXMLLoader();
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

    @FXML
    void handleRefresh(MouseEvent event) {

        list.clear();
        load();
        table.setItems(list);
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


        System.out.println("timeline at staff");
        list = FXCollections.observableArrayList();
        initTable();
        load();
        table.setItems(list);

        table.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
    }

    void initTable() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        shift_start.setCellValueFactory(new PropertyValueFactory<>("shift_start"));
        shift_end.setCellValueFactory(new PropertyValueFactory<>("shift_end"));
        mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
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
                list.add(new Staff(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7), resultSet.getInt(8), resultSet.getString(9), resultSet.getInt(10), resultSet.getInt(11)));

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
                list.add(new Staff(resultSet.getString(1), "Guard", resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7), resultSet.getString(8), resultSet.getInt(9), resultSet.getInt(10)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
