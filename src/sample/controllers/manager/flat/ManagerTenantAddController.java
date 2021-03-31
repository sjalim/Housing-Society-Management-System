package sample.controllers.manager.flat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.UserId;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ManagerTenantAddController implements Initializable {

    @FXML private JFXTextField flatNo_AddField;

    @FXML private JFXTextField tenantName_AddField;

    @FXML private JFXTextField nid_AddField;

    @FXML private JFXTextField mobile_AddField;

    @FXML private JFXTextField prestAdd_AddField;

    @FXML private JFXTextField permaAdd_AddField;

    @FXML private JFXTextField occupation_AddField;

    @FXML private JFXTextField familyMember_AddField;

    @FXML private JFXDatePicker movedInDatePicker;

    @FXML private JFXButton save_AddButton;

    @FXML private JFXButton cancel_AddButton;

    private String query, flatNo, tenantName, nid, mobile,presentAdd,permanentAdd,
            occupation,managerId, totalFamilyMember,movedIn;
    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        save_AddButton.setOnAction(actionEvent -> {
            try {
                insertNewTenant();
                Stage stage = (Stage) save_AddButton.getScene().getWindow();
                stage.close();
            } catch (SQLException | ClassNotFoundException | InterruptedException throwables) {
                throwables.printStackTrace();
            }
        });
        cancel_AddButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) save_AddButton.getScene().getWindow();
            stage.close();
        });
    }

    private void insertNewTenant() throws SQLException, ClassNotFoundException, InterruptedException {
        //Singleton Instance
        UserId mUserId = UserId.getInstance();

        flatNo = flatNo_AddField.getText();
        tenantName = tenantName_AddField.getText();
        nid = nid_AddField.getText();
        mobile = mobile_AddField.getText();
        presentAdd = prestAdd_AddField.getText();
        permanentAdd = permaAdd_AddField.getText();
        occupation = occupation_AddField.getText();
//        managerId = PLACE CURRENT LOGGED IN MANAGER HERE
        totalFamilyMember = familyMember_AddField.getText();
        movedIn = movedInDatePicker.getValue().toString();

        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        //Insert into Tenant Table
        String insertTenant = "INSERT INTO Tenant (FlatNumber,TenantName,ManagerId) " +
                "VALUES(?,?,?)";
        PreparedStatement psTenant = databaseHandler.getDbConnection().prepareStatement(insertTenant);
        psTenant.setString(1,flatNo);
        psTenant.setString(2,tenantName);
        psTenant.setString(3,mUserId.mId); ////CHANGE MANAGER ID
        psTenant.executeUpdate();
        psTenant.close();

        Thread.sleep(2000);
        //Insert into Tenant_Info Table
        String insertTenant_Info = "INSERT INTO Tenant_Info (FlatNumber,Nid,Mobile,PresentAddress," +
                "PermanentAddress,Occupation,TotalFamilyMembers,MoveIn) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        PreparedStatement psTenant_Info = databaseHandler.getDbConnection().prepareStatement(insertTenant_Info);
        psTenant_Info.setString(1,flatNo);
        psTenant_Info.setString(2,nid);
        psTenant_Info.setString(3,mobile);
        psTenant_Info.setString(4,presentAdd);
        psTenant_Info.setString(5,permanentAdd);
        psTenant_Info.setString(6,occupation);
        psTenant_Info.setString(7,totalFamilyMember);
        psTenant_Info.setString(8,movedIn);
        psTenant_Info.executeUpdate();
        psTenant_Info.close();

        databaseHandler.getDbConnection().close();
    }

    void AlertDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
