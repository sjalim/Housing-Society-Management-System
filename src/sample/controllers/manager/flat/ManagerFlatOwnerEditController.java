package sample.controllers.manager.flat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerFlatOwnerEditController implements Initializable {

    @FXML public Label flatNo_EditField;

    @FXML public JFXTextField ownerName_EditField;

    @FXML public JFXTextField nid_EditField;

    @FXML public JFXTextField mobile_EditField;

    @FXML public JFXTextField prestAdd_EditField;

    @FXML public JFXTextField permaAdd_EditField;

    @FXML public JFXTextField allocatedParking_EditField;

    @FXML public JFXComboBox<String> allocStatus_EditComboBox;

    @FXML public JFXButton save_EditButton, cancel_EditButton;

    private String query, flatNo, ownerName, nid, mobile,presentAdd,permanentAdd,
            allocateParkingNo,managerId, allocationStatus;
    private final String[] allocate_status = {"OWNED","RENTED"};
    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initAllocationSatus();
    }

    public void updateFlatOwner() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        //Query
        String query1 = "UPDATE FlatOwner SET OwnerName='"+ownerName_EditField.getText()+"'," +
                "AllocatedParkingSlot="+Integer.parseInt(allocatedParking_EditField.getText())+"," +
                "AllocationStatus='"+allocStatus_EditComboBox.getSelectionModel().getSelectedItem()+
                "' WHERE FlatOwner.FlatNumber='"+flatNo_EditField.getText()+"'";



        PreparedStatement preparedStatement1 = databaseHandler.getDbConnection().prepareStatement(query1);
        preparedStatement1.executeUpdate();
        //Query for Second table
        String query2 = "UPDATE FlatOwner_Info SET Nid='"+nid_EditField.getText()+"', Mobile='"+mobile_EditField.getText()+"', " +
                "PresentAddress='"+prestAdd_EditField.getText()+"', PermanentAddress='"+permaAdd_EditField.getText()+
                "'WHERE FlatOwner_Info.FlatNumber='"+flatNo_EditField.getText()+"'";
        PreparedStatement preparedStatement2 = databaseHandler.getDbConnection().prepareStatement(query2);
        preparedStatement2.executeUpdate();
    }

    private void initAllocationSatus() {
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, allocate_status);
        //convert list to observable list
        ObservableList observableList = FXCollections.observableArrayList(list);
        allocStatus_EditComboBox.setItems(observableList);
    }
}
