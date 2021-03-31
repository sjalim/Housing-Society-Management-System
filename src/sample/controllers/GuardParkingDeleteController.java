package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import javax.naming.ldap.PagedResultsControl;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GuardParkingDeleteController {

    ObservableList<ParkingVehicle> parkingVehicles;
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

        Node node = (Node)event.getSource();
        Stage stage =
                (Stage) node.getScene().getWindow();
        stage.close();

    }

    @FXML
    void handleConfirm(ActionEvent event) {
        StringBuilder vehicleIds =
                new StringBuilder();

        String query = null;

        for(ParkingVehicle p : parkingVehicles )
        {
            vehicleIds.append(p.getTrackId()).append(",");
        }

        vehicleIds.deleteCharAt(vehicleIds.length() - 1);
        vehicleIds.append(")");
        vehicleIds.insert(0, "(");

        query = "delete from ParkingSlotTrack " +
                "where TrackId in "+vehicleIds + ";";

        try {
        PreparedStatement preparedStatement =
                databaseHandler.getDbConnection().prepareStatement(query);
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        Node node = (Node)event.getSource();
        Stage stage =
                (Stage) node.getScene().getWindow();
        stage.close();

    }


    public void setVehiclesList(ObservableList<ParkingVehicle> parkingVehicles) {
        this.parkingVehicles = parkingVehicles;

        list.clear();

        list.add("Date          Car Number");
        for (ParkingVehicle s : parkingVehicles) {
            list.add(s.getDate()+"          "+s.getCarNumber());
        }

        delete_item_list_view.getItems().addAll(list);
        number_of_records_label.setText(String.valueOf(list.size()-1));
    }
}
