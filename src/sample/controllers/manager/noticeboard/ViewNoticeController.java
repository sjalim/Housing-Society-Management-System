package sample.controllers.manager.noticeboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import sample.database.DatabaseHandler;
import sample.models.Notice;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewNoticeController implements Initializable {

    @FXML
    public Label viewNoticeDate;

    @FXML
    public JFXTextField viewNoticeTitle;

    @FXML
    public JFXTextArea viewNoticeDes;

    @FXML
    public JFXButton updateNoticeButton;

    String query;
    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void updateNotice(Notice notice) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        String title = viewNoticeTitle.getText();
        String description = viewNoticeDes.getText();
        //Query
        query = "UPDATE NoticeBoard SET NoticeTitle ='"+title+"', " +
                "NoticeDescription ='"+description+"' WHERE NoticeId = '"+notice.getNoticeId()+"'";

        PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(query);
        ps.executeUpdate();
        ps.close();
        databaseHandler.getDbConnection().close();
    }
}
