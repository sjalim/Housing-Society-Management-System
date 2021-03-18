package sample.controllers.manager.complaintbox;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import sample.database.DatabaseHandler;
import sample.models.ComplainBox;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ManagerCustomComplaintRowController extends JFXListCell<ComplainBox> implements Initializable {

    @FXML private AnchorPane rootAnchorPane;

    @FXML private Label complaintIdField;

    @FXML private Label complaintTitleField;

    @FXML private JFXTextArea complaintDescriptionField;

    @FXML private JFXToggleButton toggleButton;

    @FXML private Label complaintFlatNoField;

    @FXML private Label countVoteUp;

    @FXML private JFXButton voteUpButton;

    @FXML private JFXButton voteDownButton;

    @FXML private Label countVoteDown;

    @FXML private Label dateAddedField;

    @FXML private Label categoryField;

    private FXMLLoader fxmlLoader;

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
                fxmlLoader = new FXMLLoader(getClass().getResource("/sample/views/manager/complaintbox/manager_customComplaintRow.fxml"));
                fxmlLoader.setController(this);

                try {
                    fxmlLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            toggleButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if(toggleButton.isSelected()) {
                        toggleButton.setText("Solved");
                        System.out.println("Solved called from CHANGED");
                        try {
                            updateStatus("Solved");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }else {
                        toggleButton.setText("Unsolved");
                        System.out.println("Unsolved called from CHANGED");
                        try {
                            updateStatus("Unsolved");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            if (mComplainBox.getComplainStatus().equals("Unsolved")) {
                System.out.println("Unsolved called from getComplainStatus()");
                toggleButton.setSelected(false);
                toggleButton.setText("Unsolved");
            } else {
                System.out.println("Solved called from getComplainStatus()");
                toggleButton.setSelected(true);
                toggleButton.setText("Solved");
            }
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

    private void updateStatus(String status) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        query = "UPDATE ComplainBoxBoard SET ComplainBoxStatus ='"+status+"' WHERE ComplainBoxId='"+complaintIdField.getText()+"'";

        PreparedStatement ps = databaseHandler.getDbConnection().prepareStatement(query);
        ps.executeUpdate();
        System.out.println("Status Updated");
        databaseHandler.getDbConnection().close();
    }
}
