package sample.controllers.resident;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import sample.UserId;
import sample.database.DatabaseHandler;
import sample.models.ComplainBox;
import sample.models.Notice;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class CustomRowComplaintController extends JFXListCell<ComplainBox> implements Initializable {

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML private Label complaintIdField;

    @FXML private Label complaintTitleField;

    @FXML private JFXTextArea complaintDescriptionField;

    @FXML private JFXToggleButton toggleButton;

    @FXML private Label complaintFlatNoField;

    @FXML private Label dateAddedField;

    @FXML private Label countVoteUp;

    @FXML private JFXButton voteUpButton, deleteComplaint;

    @FXML private Label countVoteDown;

    @FXML private JFXButton voteDownButton;

    @FXML private Label categoryField;

    private FXMLLoader fxmlLoader;

    UserId mUserId = UserId.getInstance();

    String query;
    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @Override
    protected void updateItem(ComplainBox mComplainBox, boolean empty) {
        super.updateItem(mComplainBox, empty);

        if (empty || mComplainBox == null) {
            setText(null);
            setGraphic(null);
        }else {
            if (fxmlLoader == null) {
                fxmlLoader = new FXMLLoader(getClass().getResource("/sample/views/resident/custom_row_complaint.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(mComplainBox.getFlatNumber().equals(mUserId.mId)) {
                deleteComplaint.setVisible(true);
            } else {
                deleteComplaint.setVisible(false);
            }

            deleteComplaint.setOnAction(actionEvent -> {
                try {
                    deleteComplaint(mComplainBox);
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            });

            if (mComplainBox.getComplainStatus().equals("Unsolved")) {
                toggleButton.setSelected(false);
                toggleButton.setText("Unsolved");
            } else {
                toggleButton.setSelected(true);
                toggleButton.setText("Solved");
            }

            voteUpButton.setOnAction(actionEvent -> {
                boolean canVote = true;
                try {
                    databaseHandler = new DatabaseHandler();
                    Statement statement = databaseHandler.getDbConnection().createStatement();
                    query = "SELECT * FROM ComplainBoxVote WHERE FlatNumber = '"+mUserId.mId+"' " +
                            "AND ComplainBoxId ='"+mComplainBox.getComplainId()+"'";
                    ResultSet rs = statement.executeQuery(query);
                    while (rs.next()) {
                        canVote = false;
                    }
                    if(canVote) {
                        System.out.println("U can vote");
                        updateVote(1);
                    } else {
                        AlertDialog("You can't vote twice");
                    }
                    databaseHandler.getDbConnection().close();
                }catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            });

            voteDownButton.setOnAction(actionEvent -> {
                boolean canVote = true;
                try {
                    databaseHandler = new DatabaseHandler();
                    Statement statement = databaseHandler.getDbConnection().createStatement();
                    query = "SELECT * FROM ComplainBoxVote WHERE FlatNumber = '"+mUserId.mId+"' " +
                            "AND ComplainBoxId ='"+mComplainBox.getComplainId()+"'";
                    ResultSet rs = statement.executeQuery(query);
                    while (rs.next()) {
                        canVote = false;
                    }
                    if(canVote) {
                        System.out.println("U can vote");
                        updateVote(0);
                    } else {
                        AlertDialog("You can't vote twice");
                    }
                    databaseHandler.getDbConnection().close();
                }catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
            });

            complaintIdField.setText(String.valueOf(mComplainBox.getComplainId()));
            complaintTitleField.setText(mComplainBox.getComplainTitle());
            complaintDescriptionField.setText(mComplainBox.getComplainDescription());
            complaintFlatNoField.setText(mComplainBox.getFlatNumber());
            dateAddedField.setText(mComplainBox.getDateAdded());
            countVoteUp.setText(String.valueOf(mComplainBox.getVoteUp()));
            countVoteDown.setText(String.valueOf(mComplainBox.getVoteDown()));
            categoryField.setText(mComplainBox.getCategory());

            setText(null);
            setGraphic(rootAnchorPane);
        }
    }

    private void deleteComplaint(ComplainBox complainBox) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();

        String query1 = "DELETE FROM ComplainBoxBoard WHERE ComplainBoxId = '"+complainBox.getComplainId()+"'";
        PreparedStatement preparedStatement = databaseHandler.getDbConnection().prepareStatement(query1);
        preparedStatement.execute();
        preparedStatement.close();

        String query2 = "DELETE FROM ComplainBoxVote WHERE ComplainBoxId = '"+complainBox.getComplainId()+"'";
        PreparedStatement preparedStatement1 = databaseHandler.getDbConnection().prepareStatement(query2);
        preparedStatement1.execute();
        preparedStatement1.close();
        databaseHandler.getDbConnection().close();
    }

    private void updateVote(int value) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        query = "INSERT INTO ComplainBoxVote (ComplainBoxId,FlatNumber,VoteFlag) " +
                "VALUES(?,?,?)";
        PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(query);
        ps.setString(1,complaintIdField.getText());
        ps.setString(2,mUserId.mId);
        ps.setInt(3,value);
        ps.executeUpdate();
        ps.close();
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
