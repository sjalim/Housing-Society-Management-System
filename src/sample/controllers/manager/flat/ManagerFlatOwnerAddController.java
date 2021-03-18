package sample.controllers.manager.flat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.models.FlatOwner;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class ManagerFlatOwnerAddController implements Initializable {

    @FXML private JFXTextField flatNo_AddField;

    @FXML private JFXTextField ownerName_AddField;

    @FXML private JFXTextField nid_AddField;

    @FXML private JFXTextField mobile_AddField;

    @FXML private JFXTextField prestAdd_AddField;

    @FXML private JFXTextField permaAdd_AddField;

    @FXML private JFXTextField allocateParking_AddField;

    @FXML private JFXComboBox<String> allocStatus_AddComboBox;

    @FXML private JFXButton save_AddButton, cancel_AddButton;

    private String query, flatNo, ownerName, nid, mobile,presentAdd,permanentAdd,
            allocateParkingNo,managerId, allocationStatus;
    private final String[] allocate_status = {"OWNED","RENTED"};
    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initAllocationSatus();
        save_AddButton.setOnAction(actionEvent -> {
            try {
                insertNewFlatOwner();
                Stage stage = (Stage) save_AddButton.getScene().getWindow();
                stage.close();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
        cancel_AddButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) save_AddButton.getScene().getWindow();
            stage.close();
        });
    }

    private void initAllocationSatus(){
        List<String> list = new ArrayList<String>();
        Collections.addAll(list, allocate_status);
        //convert list to observable list
        ObservableList observableList = FXCollections.observableArrayList(list);
        allocStatus_AddComboBox.setItems(observableList);
    }
    //NEED TO ADD CONSTRAINTS WHILE INSERTING
    private void insertNewFlatOwner() throws SQLException, ClassNotFoundException {
        flatNo = flatNo_AddField.getText();
        ownerName = ownerName_AddField.getText();
        nid = nid_AddField.getText();
        mobile = mobile_AddField.getText();
        presentAdd = prestAdd_AddField.getText();
        permanentAdd = permaAdd_AddField.getText();
        allocateParkingNo = allocateParking_AddField.getText();
//        managerId = PLACE CURRENT LOGGED IN MANAGER HERE
        allocationStatus = allocStatus_AddComboBox.getSelectionModel().getSelectedItem();
        //Query
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();
        query = "SELECT FlatNumber FROM FlatOwner WHERE FlatNumber ='"+flatNo+"';";
        ResultSet rs = statement.executeQuery(query);
        //checking if FlatNumber exists
        if(rs.next()) {
            String checkFlatNo = rs.getString("FlatNumber");
            if(checkFlatNo.equals(flatNo)) {
                System.out.println("FLAT Exists");
                AlertDialog("FlatNo already exists");
            }
        }else {
            //Insert into FlatOwner Table
            String insertFlatOwner = "INSERT INTO FlatOwner (FlatNumber,OwnerName,ManagerId,AllocatedParkingSlot,AllocationStatus) " +
                    "VALUES(?,?,?,?,?)";
            PreparedStatement psFlatOwner = databaseHandler.getDbConnection().prepareStatement(insertFlatOwner);
            psFlatOwner.setString(1,flatNo);
            psFlatOwner.setString(2,ownerName);
            psFlatOwner.setString(3,"2"); ////CHANGE MANAGER ID
            psFlatOwner.setString(4,allocateParkingNo);
            psFlatOwner.setString(5,allocationStatus);
            psFlatOwner.executeUpdate();
            psFlatOwner.close();

            //Insert into FlatOwner_Info Table
            String insertFlatOwner_Info = "INSERT INTO FlatOwner_Info (FlatNumber,Nid,Mobile,PresentAddress,PermanentAddress) " +
                    "VALUES(?,?,?,?,?)";
            PreparedStatement psFlatOwner_Info = databaseHandler.getDbConnection().prepareStatement(insertFlatOwner_Info);
            psFlatOwner_Info.setString(1,flatNo);
            psFlatOwner_Info.setString(2,nid);
            psFlatOwner_Info.setString(3,mobile);
            psFlatOwner_Info.setString(4,presentAdd);
            psFlatOwner_Info.setString(5,permanentAdd);
            psFlatOwner_Info.executeUpdate();
            psFlatOwner_Info.close();

            //Insert into FlatLogin Table
            String insertFlatLogin = "INSERT INTO FlatLogin (FlatNumber,Password) " +
                    "VALUES(?,?)";
            String password = flatNo+"#";
            PreparedStatement psFlatLogin = databaseHandler.getDbConnection().prepareStatement(insertFlatLogin);
            psFlatLogin.setString(1,flatNo);
            psFlatLogin.setString(2,password); //Initially setting FlatOwner's password as FlatNo+#
            psFlatLogin.executeUpdate();
            psFlatLogin.close();
            databaseHandler.getDbConnection().close();
        }
    }

    void AlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
