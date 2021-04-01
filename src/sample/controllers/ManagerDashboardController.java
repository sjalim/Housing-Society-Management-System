package sample.controllers;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import javafx.util.Duration;
import sample.controllers.manager.flat.ManagerFlatOwnerController;
import sample.database.DatabaseHandler;

public class ManagerDashboardController implements Initializable {

    @FXML
    private JFXButton drawerButton;

    @FXML
    private AnchorPane contentViewPane;

    @FXML
    private AnchorPane opacityPane;

    @FXML
    private AnchorPane navDrawerPane;

    @FXML
    private AnchorPane monitor_anchor_pane;

    @FXML
    private VBox monitor_vbox;

    @FXML
    private JFXButton monitorParking_button;

    @FXML
    private JFXButton monitorVisitors_button;

    @FXML
    private JFXButton monitorStaffs_button;

    @FXML
    private AnchorPane flat_anchor_pane;

    @FXML
    private VBox flat_vbox;

    @FXML
    private JFXButton flatsOwner_button;

    @FXML
    private JFXButton flatsTenant_button;

    @FXML
    private JFXButton staffs_button;

    @FXML
    private JFXButton parkingAllocate_button;

    @FXML
    private JFXButton complain_button;

    @FXML
    private JFXButton notification_button;

    @FXML
    private JFXButton payment_button;

    @FXML
    private JFXButton notice_button;

    @FXML
    private JFXButton logout_manager_button;


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

//        ScrollPane scrollPane = null;
//        try {
//            scrollPane = FXMLLoader.load(getClass().getResource("/sample/views/manager/monitor_parking.fxml"));
//            contentViewPane.getChildren().setAll(scrollPane);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            ScrollPane scrollPane  = FXMLLoader.load(this.getClass().getResource("/sample/views/manager/monitor_parking.fxml"));
            contentViewPane.getChildren().clear();
            contentViewPane.getChildren().setAll(scrollPane);
        } catch (IOException e) {
            e.printStackTrace();
        }


        flatsOwner_button.setOnAction(actionEvent -> {
            try {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/manager" +
                        "/flat/manager_FlatOwner.fxml"));
                contentViewPane.getChildren().setAll(anchorPane);
            } catch (IOException throwables) {
                throwables.printStackTrace();
            }
        });

        flatsTenant_button.setOnAction(actionEvent -> {
            try {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/manager" +
                        "/flat/manager_FlatTenant.fxml"));
                contentViewPane.getChildren().clear();
                contentViewPane.getChildren().setAll(anchorPane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        logout_manager_button.setOnAction(new EventHandler<ActionEvent>() {
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
        staffs_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/manager" +
                            "/manager_staff.fxml"));

                    contentViewPane.getChildren().setAll(anchorPane);
                } catch (IOException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        complain_button.setOnAction(actionEvent -> {
            try {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/manager/complaintbox/manager_complaint_box.fxml"));
                contentViewPane.getChildren().clear();
                contentViewPane.getChildren().setAll(anchorPane);
            } catch (IOException throwables) {
                throwables.printStackTrace();
            }
        });

        payment_button.setOnAction(actionEvent -> {
            try {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/manager/collectpayments/manager_collect_payments.fxml"));
                contentViewPane.getChildren().clear();
                contentViewPane.getChildren().setAll(anchorPane);
            } catch (IOException throwables) {
                throwables.printStackTrace();
            }
        });

        notice_button.setOnAction(actionEvent -> {
            try {
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/manager/noticeboard/manager_notice_board.fxml"));
                contentViewPane.getChildren().clear();
                contentViewPane.getChildren().setAll(anchorPane);
            } catch (IOException throwables) {
                throwables.printStackTrace();
            }
        });

        parkingAllocate_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    AnchorPane anchorPane =
                            FXMLLoader.load(getClass().getResource("/sample/views/manager/manager_parking_allocation.fxml"));

                    contentViewPane.getChildren().setAll(anchorPane);
                } catch (IOException throwables) {
                    throwables.printStackTrace();
                }
            }
        });


        monitorParking_button.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                ScrollPane scrollPane  = FXMLLoader.load(this.getClass().getResource("/sample/views/manager/monitor_parking.fxml"));
                    contentViewPane.getChildren().clear();
                    contentViewPane.getChildren().setAll(scrollPane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

}


