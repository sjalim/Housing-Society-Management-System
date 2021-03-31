package sample.controllers.resident;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import sample.UserId;
import sample.database.DatabaseHandler;
import sample.models.FlatOwner;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class UpdateFlatOwnerInfoController implements Initializable {

    @FXML private Label flatNo_EditField;

    @FXML private JFXTextField nid_EditField;

    @FXML private JFXTextField mobile_EditField;

    @FXML private JFXTextField prestAdd_EditField;

    @FXML private JFXTextField permaAdd_EditField;

    @FXML private JFXButton save_EditButton;

    @FXML private JFXButton cancel_EditButton;
    private DatabaseHandler databaseHandler;
    String query = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UserId mUserId = UserId.getInstance();

        save_EditButton.setOnAction(actionEvent -> {
            try {
                updateDetails(mUserId.mId);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }

            Stage stage = (Stage) save_EditButton.getScene().getWindow();
            stage.close();
        });

        cancel_EditButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) cancel_EditButton.getScene().getWindow();
            stage.close();
        });
    }

    public void setElements(FlatOwner flatOwner) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();
        query = "SELECT * FROM FlatOwner_Info WHERE FlatNumber='"+flatOwner.getFlatNumber()+"'";

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            flatNo_EditField.setText(rs.getString("FlatNumber"));
            nid_EditField.setText(rs.getString("Nid"));
            mobile_EditField.setText(rs.getString("Mobile"));
            prestAdd_EditField.setText(rs.getString("PresentAddress"));
            permaAdd_EditField.setText(rs.getString("PermanentAddress"));
        }
        databaseHandler.getDbConnection().close();
    }

    public void updateDetails(String mId) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();

        query = "UPDATE FlatOwner_Info SET Nid='"+nid_EditField.getText()+"', " +
                    "Mobile='"+mobile_EditField.getText()+"', PresentAddress='"+prestAdd_EditField.getText()+"', " +
                    "PermanentAddress='"+permaAdd_EditField.getText()+"' WHERE FlatNumber='"+mId+"'";

        PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(query);
        ps.executeUpdate();
        ps.close();
        databaseHandler.getDbConnection().close();
    }
}
