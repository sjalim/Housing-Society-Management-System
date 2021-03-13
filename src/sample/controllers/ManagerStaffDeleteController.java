package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManagerStaffDeleteController  {


    ObservableList<Staff> staff;
    ObservableList list =
            FXCollections.observableArrayList();
    DatabaseHandler databaseHandler =
            new DatabaseHandler();
    @FXML
    private Label number_of_records_label;

    @FXML
    private ListView<String> delete_item_list_view;

    @FXML
    void handleCancel(ActionEvent event) {

    }

    @FXML
    void handleConfirm(ActionEvent event) {
        StringBuilder guardIds =
                new StringBuilder();
        StringBuilder staffIds =
                new StringBuilder();
        String query = null;

        for (Staff s : staff) {
            if (s.getType().equals(
                    "Guard")) {
                guardIds.append(s.getId()).append(",");
            } else {
                staffIds.append(s.getId()).append(",");
            }
        }

        if (guardIds.length() != 0) {
            guardIds.deleteCharAt(guardIds.length() - 1);
            guardIds.append(")");
            guardIds.insert(0, "(");
            System.out.println(guardIds);

                query = "delete from Guard " +
                        "where GuardId in " + guardIds + ";";

            PreparedStatement preparedStatement =
                    null;
            try {
                preparedStatement =
                        databaseHandler.getDbConnection().prepareStatement(query);
                preparedStatement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (staffIds.length() != 0) {

            staffIds.deleteCharAt(staffIds.length() - 1);
            staffIds.append(")");
            staffIds.insert(0, "(");
            System.out.println(staffIds);


            query = "delete from Staff " +
                    "where StaffId in " + staffIds + ";";

            PreparedStatement preparedStatement =
                    null;
            try {
                preparedStatement =
                        databaseHandler.getDbConnection().prepareStatement(query);
                preparedStatement.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    public void setStaffList(ObservableList<Staff> staff) {
        this.staff = staff;

        list.clear();
        for (Staff s : staff) {
            list.add(s.getName());
        }

        delete_item_list_view.getItems().addAll(list);
        number_of_records_label.setText(String.valueOf(list.size()));
    }

}
