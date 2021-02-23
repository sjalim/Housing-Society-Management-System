package sample.controllers;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import sample.database.DatabaseHandler;
import sample.models.FlatOwner;

public class ManagerDashboardController implements Initializable {
    @FXML
    private JFXButton drawerButton;

    @FXML
    private AnchorPane opacityPane, navDrawerPane, contentViewPane;

    @FXML
    private JFXButton monitorParking_button, monitorVisitors_button, monitorStaffs_button, flatsOwner_button,
            flatsTenant_button, staffs_button, parkingAllocate_button, complain_button, notification_button,
            payment_button, notice_button, logout_manager_button;

    private DatabaseHandler databaseHandler;

    public ManagerDashboardController() {
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
         databaseHandler = new DatabaseHandler();

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
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5D), this.navDrawerPane);
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
            TranslateTransition translateTransition1 = new TranslateTransition(Duration.seconds(0.5D), this.navDrawerPane);
            translateTransition1.setByX(-600.0D);
            translateTransition1.play();
        });

        flatsOwner_button.setOnAction(actionEvent -> {
            try {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/manager/manager_FlatOwner.fxml"));
                contentViewPane.getChildren().setAll(anchorPane);
            } catch (IOException throwables) {
                throwables.printStackTrace();
            }
        });
    }
}
