package sample.controllers.resident;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import sample.UserId;
import sample.controllers.LoginPageController;
import sample.controllers.manager.flat.ManagerFlatOwnerEditController;
import sample.database.DatabaseHandler;
import sample.models.FlatOwner;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class FlatOwnerDashboardController implements Initializable {

    @FXML
    private JFXButton drawerButton;

    @FXML
    public AnchorPane contentViewPane;

    @FXML
    private AnchorPane opacityPane;

    @FXML
    private AnchorPane navDrawerPane;

    @FXML
    private AnchorPane flat_anchor_pane;

    @FXML public JFXButton updateInfoButton;

    @FXML public JFXButton changePassButton;

    @FXML public JFXButton complain_button;

    @FXML public JFXButton payment_button;

    @FXML public JFXButton notice_button;

    @FXML private JFXButton logout_button;

    @FXML public Label dashboard_flatNo;

    @FXML public Label dashboard_Name, dashboard_flatStatus;

    private DatabaseHandler databaseHandler;
    private String query =null;
    private FlatOwner flatOwner = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        UserId mUserId = UserId.getInstance();
        System.out.println(mUserId.mId +"  Logged in");
        try {
            setUserDashBoard(mUserId.mId);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

//        Navigation Animation
        opacityPane.setVisible(false);
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.5D), this.opacityPane);
        fadeTransition.setFromValue(1.0D);
        fadeTransition.setToValue(0.0D);
        fadeTransition.play();
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5D), this.navDrawerPane);
        translateTransition.setByX(-600.0D);
        translateTransition.play();
        drawerButton.setOnMouseClicked((mouseEvent) -> {
            opacityPane.setVisible(true);
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5D), this.opacityPane);
            fadeTransition1.setFromValue(0.0D);
            fadeTransition1.setToValue(0.15D);
            fadeTransition1.play();
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5D),
                    this.navDrawerPane);
            translateTransition1.setByX(600.0D);
            translateTransition1.play();
        });
        opacityPane.setOnMouseClicked((mouseEvent) -> {
            FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(0.5D), this.opacityPane);
            fadeTransition1.setFromValue(0.15D);
            fadeTransition1.setToValue(0.0D);
            fadeTransition1.play();
            fadeTransition1.setOnFinished((actionEvent) -> {
                opacityPane.setVisible(false);
            });
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5D),
                    this.navDrawerPane);
            translateTransition1.setByX(-600.0D);
            translateTransition1.play();
        });

        updateInfoButton.setOnAction(actionEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/sample/views/resident/update_flatOwner_info.fxml"));
                loader.load();
            UpdateFlatOwnerInfoController updateFlatOwnerInfoController = loader.getController();
            updateFlatOwnerInfoController.setElements(flatOwner);
                Scene scene = new Scene(loader.getRoot());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Edit Flat Owner Details");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });

        changePassButton.setOnAction(actionEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("/sample/views/resident/change_password.fxml"));
                loader.load();
                ChangePasswordController changePasswordController = loader.getController();
                Scene scene = new Scene(loader.getRoot());
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setTitle("Change Password");
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        complain_button.setOnAction(actionEvent -> {
            try {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/resident/complaint_box.fxml"));
                contentViewPane.getChildren().clear();
                contentViewPane.getChildren().setAll(anchorPane);
            } catch (IOException throwables) {
                throwables.printStackTrace();
            }
        });

        payment_button.setOnAction(actionEvent -> {
            try {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/resident/resident_payments.fxml"));
                contentViewPane.getChildren().clear();
                contentViewPane.getChildren().setAll(anchorPane);
            } catch (IOException throwables) {
                throwables.printStackTrace();
            }
        });

        notice_button.setOnAction(actionEvent -> {
            try {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/resident/resident_noitice_board.fxml"));
                contentViewPane.getChildren().clear();
                contentViewPane.getChildren().setAll(anchorPane);
            } catch (IOException throwables) {
                throwables.printStackTrace();
            }
        });

        logout_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Preferences userPreferences = Preferences.userRoot();
                userPreferences.put(LoginPageController.USER_STATUS, "root");
                userPreferences.put(LoginPageController.USER_ID, "root");
                userPreferences.put(LoginPageController.ALLOCATION_STATUS, "root");
                Parent root = null;
                try {
                    root = (Parent) FXMLLoader.load(this.getClass().getResource("/sample/views/login/LoginPage" +
                            ".fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage primaryStage = (Stage) drawerButton.getScene().getWindow();
                primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
                primaryStage.show();
            }
        });

    }

    private void setUserDashBoard(String mId) throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();

        query = "SELECT f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                "f2.PermanentAddress, COUNT(v.FlatNumber) as 'VehicleOwned' " +
                "FROM FlatOwner as f1 " +
                "INNER JOIN FlatOwner_Info as f2 " +
                    "ON f1.FlatNumber = f2.FlatNumber " +
                "LEFT JOIN Vehicle as v " +
                    "ON f1.FlatNumber = v.FlatNumber " +
                "WHERE f1.FlatNumber = '"+mId+"' " +
                "GROUP BY f1.FlatNumber, f1.OwnerName, f1.AllocatedParkingSlot, f1.AllocationStatus, f2.nid, f2.Mobile, f2.PresentAddress, " +
                "f2.PermanentAddress " +
                "ORDER BY f1.FlatNumber";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            flatOwner = new FlatOwner(rs.getString("FlatNumber"),rs.getString("OwnerName"),
                    rs.getString("Mobile"), rs.getString("PresentAddress"),rs.getString("PermanentAddress"),
                    rs.getString("Nid"),rs.getInt("AllocatedParkingSlot"),
                    rs.getString("AllocationStatus"),rs.getInt("VehicleOwned"));
        }
        dashboard_flatNo.setText(mId);
        dashboard_Name.setText(flatOwner.getOwnerName());

        if (flatOwner.getMobile().equals("")  || flatOwner.getPresentAdd().equals("")
                || flatOwner.getPermanentAdd().equals("") || flatOwner.getNid().equals("")) {
            updateInfoButton.setDisable(false);
        } else {
            updateInfoButton.setDisable(true);
        }

        databaseHandler.getDbConnection().close();

    }
}
