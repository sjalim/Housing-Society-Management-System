package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class GuardVisitorListController implements Initializable {


    DatabaseHandler databaseHandler = new DatabaseHandler();

    ObservableList list = FXCollections.observableArrayList();

    @FXML
    private TextField search_text_field;


    @FXML
    private TableView<Visitor> table;


    @FXML
    private TableColumn<String, Visitor> name_col;

    @FXML
    private TableColumn<String, Visitor> flat_number_col;

    @FXML
    private TableColumn<String, Visitor> in_time_col;

    @FXML
    private TableColumn<String, Visitor> out_time_col;

    @FXML
    private TableColumn<Integer, Visitor> guard_id_col;

    @FXML
    private TableColumn<Integer, Visitor> gate_no_col;

    @FXML
    private TextField search;


    @FXML
    void handleRefresh(ActionEvent event) {

        list.clear();
        load();
        table.setItems(list);

    }

    @FXML
    void handleAdd(ActionEvent event) {

        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/sample/views/guard/guard_visitor.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    void initTable() {


        flat_number_col.setCellValueFactory(new PropertyValueFactory<>("flatNumber"));
        guard_id_col.setCellValueFactory(new PropertyValueFactory<>("id"));
        gate_no_col.setCellValueFactory(new PropertyValueFactory<>("gateNo"));
        in_time_col.setCellValueFactory(new PropertyValueFactory<>("inTime"));
        out_time_col.setCellValueFactory(new PropertyValueFactory<>("outTime"));
        name_col.setCellValueFactory(new PropertyValueFactory<>("name"));


    }


    void load() {

        String query = "select * from Visitor";
        try {
            PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                list.add(new Visitor(resultSet.getString("name"), resultSet.getString("FlatNumber"), resultSet.getString("InTime")
                        , resultSet.getString("OutTime"), resultSet.getInt("VisitorId"), resultSet.getInt("GuardId"), resultSet.getInt("GateNo")));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        initTable();
        load();

        table.setItems(list);


    }

    @FXML
    void handleSearch(ActionEvent event) {

        list.clear();
        String search = search_text_field.getText();

        if(!search.isEmpty())
        {
            String query = "select * from Visitor where " +
                    "Name like '%"+search+"%' " +
                    "or " +
                    "FlatNumber like '%"+search+"%' " +
                    "or " +
                    "InTime like '"+search+"' " +
                    "or " +
                    "OutTime like '"+search+"'" ;

            try {
                PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    list.add(new Visitor(resultSet.getString("name"), resultSet.getString("FlatNumber"), resultSet.getString("InTime")
                            , resultSet.getString("OutTime"), resultSet.getInt("VisitorId"), resultSet.getInt("GuardId"), resultSet.getInt("GateNo")));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
