package sample.controllers;

import com.jfoenix.controls.JFXTimePicker;
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
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ManagerStaffController implements Initializable {

    DatabaseHandler databaseHandler = new DatabaseHandler();
    ObservableList<Staff> list;


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
    private Label search_label;



    @FXML
    void handleAdd(ActionEvent event) {

        Parent ownedParent = null;
        try {
            ownedParent = FXMLLoader.load(getClass().getResource("/sample/views/manager/manager_staff_add.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(ownedParent, 1000, 500);
        Stage curStage = new Stage();
        curStage.setScene(scene);
        curStage.show();
    }

    @FXML
    void handleDelete(ActionEvent event) {

    }

    @FXML
    void handleSearch(MouseEvent event) {

    }

    @FXML
    void handleUpdate(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        list = FXCollections.observableArrayList();
        String query =  "select Name,StaffType,StartTime,EndTime,Mobile from Staff";
        try {
            Statement statement = databaseHandler.getDbConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                list.add(new Staff(resultSet.getString(1),resultSet.getString(2),resultSet.getString(3),resultSet.getString(4),resultSet.getString(5)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        query = "select Name,StartTime,EndTime,Mobile from Guard";
        try {
            Statement statement = databaseHandler.getDbConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                list.add(new Staff(resultSet.getString(1),"Guard",resultSet.getString(2),resultSet.getString(3),resultSet.getString(4)));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        table.setItems(list);
        name.setCellValueFactory(new PropertyValueFactory<String,Staff>("name"));
        type.setCellValueFactory(new PropertyValueFactory<String,Staff>("type"));
        shift_start.setCellValueFactory(new PropertyValueFactory<String,Staff>("shift_start"));
        shift_end.setCellValueFactory(new PropertyValueFactory<String,Staff>("shift_end"));
        mobile.setCellValueFactory(new PropertyValueFactory<String,Staff>("mobile"));


    }
}
