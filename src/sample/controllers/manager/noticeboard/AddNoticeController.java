package sample.controllers.manager.noticeboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import sample.UserId;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddNoticeController implements Initializable {

    @FXML
    private JFXTextArea addNoticeDes;

    @FXML
    private JFXTextField addNoticeTitle;

    @FXML
    private JFXButton postNoticeButton;

    private DatabaseHandler databaseHandler;
    //Singleton Instance
    UserId mUserId = UserId.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        postNoticeButton.setOnAction(actionEvent -> {
            try {
                insertNotice();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
            Stage stage = (Stage) postNoticeButton.getScene().getWindow();
            stage.close();
        });
    }

    private void insertNotice() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        if (addNoticeTitle.getText() == null || addNoticeDes.getText() == null){
            AlertDialog("No input found");
        } else {
            String insert = "INSERT INTO NoticeBoard (NoticeTitle,NoticeDescription,ManagerId) " +
                    "VALUES(?,?,?)";
            PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(insert);
            ps.setString(1,addNoticeTitle.getText());
            ps.setString(2,addNoticeDes.getText());
            ps.setInt(3, Integer.parseInt(mUserId.mId)); //MANAGER ID
            ps.executeUpdate();
            ps.close();
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
