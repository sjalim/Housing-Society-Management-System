package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.database.DatabaseHandler;
import sample.models.FlatOwner;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ManagerFlatOwnerController implements Initializable {

    @FXML private TableView<FlatOwner> flatOwner_tableView;

    @FXML private TableColumn<FlatOwner, String> table_flatNo;

    @FXML private TableColumn<FlatOwner, String> table_ownerName;

    @FXML private TableColumn<FlatOwner, String> table_mobile;

    @FXML private TableColumn<FlatOwner, String> table_presAdd;

    @FXML private TableColumn<FlatOwner, String> table_permAdd;

    @FXML private TableColumn<FlatOwner, String> table_nid;

    @FXML private TableColumn<FlatOwner, Integer> table_allocatedParkNo;

    private DatabaseHandler databaseHandler;
    ObservableList<FlatOwner> flatOwnerObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            loadFromDatabase();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

        table_flatNo.setCellValueFactory(new PropertyValueFactory<>("flatNumber"));
        table_ownerName.setCellValueFactory(new PropertyValueFactory<>("ownerName"));
        table_mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        table_presAdd.setCellValueFactory(new PropertyValueFactory<>("presentAdd"));
        table_permAdd.setCellValueFactory(new PropertyValueFactory<>("permanentAdd"));
        table_nid.setCellValueFactory(new PropertyValueFactory<>("nid"));
        table_allocatedParkNo.setCellValueFactory(new PropertyValueFactory<>("allocatedParkingNo"));

        flatOwner_tableView.setItems(flatOwnerObservableList);

    }

    private void loadFromDatabase() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM FlatOwner");

        while(rs.next()) {
            flatOwnerObservableList.add(new FlatOwner(rs.getString("FlatNumber"),rs.getString("OwnerName"),
                    rs.getString("Mobile"), rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getInt("AllocatedParkingSlot")));
        }
    }
}
