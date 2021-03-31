package sample.controllers.manager.noticeboard;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.controllers.manager.collectpayments.ManagerAddPaymentController;
import sample.database.DatabaseHandler;
import sample.models.Notice;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ResourceBundle;

public class NoticeRowController extends JFXListCell<Notice> implements Initializable {

    @FXML private AnchorPane rootAnchorPane;

    @FXML private Label noticeDate;

    @FXML private Label noticeTitle;

    @FXML public JFXButton noticeDeleteButton;

    private FXMLLoader fxmlLoader;

    String query;
    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    protected void updateItem(Notice mNotice, boolean empty) {
        super.updateItem(mNotice, empty);

        if (empty || mNotice == null) {
            setText(null);
            setGraphic(null);
        }else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/sample/views/manager/noticeboard/notice_row.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            noticeDate.setText(String.valueOf(mNotice.getDateAdded()));
            noticeTitle.setText(mNotice.getNoticeTitle());

            //View Notice in Details
            setOnMouseClicked(mouseClickedEvent -> {
                Notice notice = getListView().getSelectionModel().getSelectedItem();
                if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY)
                        && mouseClickedEvent.getClickCount() == 2 && notice!=null) {
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(this.getClass().getResource("/sample/views/manager/noticeboard/view_notice.fxml"));
                    try {
                        loader.load();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ViewNoticeController viewNoticeController = loader.getController();
                    viewNoticeController.viewNoticeDate.setText(notice.getDateAdded().toString());
                    viewNoticeController.viewNoticeTitle.setText(notice.getNoticeTitle());
                    viewNoticeController.viewNoticeDes.setText(notice.getNoticeDescription());
                    viewNoticeController.updateNoticeButton.setOnAction(actionEvent -> {
                        try {
                            viewNoticeController.updateNotice(notice);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                        Stage stage = (Stage) viewNoticeController.updateNoticeButton.getScene().getWindow();
                        stage.close();
                    });
                    Scene scene = new Scene(loader.getRoot());
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Post Notice");
                    stage.show();
                }
            });

            //Delete Notice
            noticeDeleteButton.setOnMouseClicked(event -> {
                Notice notice = getListView().getSelectionModel().getSelectedItem();
                if(notice==null)
                    return;
                else {
                    try {
                        deleteNotice(notice);
                    } catch (SQLException | ClassNotFoundException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });
            setText(null);
            setGraphic(rootAnchorPane);
        }
    }

    private void deleteNotice(Notice notice) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();

        String query = "DELETE FROM NoticeBoard WHERE NoticeId = '"+notice.getNoticeId()+"'";

        PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query);
        preparedStatement.execute();
        preparedStatement.close();
    }
}
