package sample.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class GuardDashboardController implements Initializable {

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
    private JFXButton profile_button;

    @FXML
    private JFXButton logout_button;

    @FXML
    private VBox monitor_vbox;

    @FXML
    private JFXButton guardParking_button;

    @FXML
    private JFXButton guardVisitors_button;

    @FXML
    private JFXButton add_track_button;

    @FXML
    private JFXButton update_track_button;


    @FXML
    private JFXButton Staffs_button;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        guardVisitors_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    ScrollPane scrollPane = FXMLLoader.load(getClass().getResource("/sample/views/guard/guard_visitor_list.fxml"));
                    contentViewPane.getChildren().setAll(scrollPane);
                } catch (IOException throwables) {
                    throwables.printStackTrace();
                }


//                Node node = (Node)(actionEvent.getSource());
//                Stage primaryStage = (Stage)node.getScene().getWindow();
//                Parent root = null;
//                try {
//                    root =
//                            (Parent) FXMLLoader.load(this.getClass().getResource("/sample/views/guard/guard_visitor.fxml"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                primaryStage.setTitle("Hello World");
//                primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
//                primaryStage.show();
            }
        });

        logout_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Preferences userPreferences = Preferences.userRoot();
                userPreferences.put(LoginPageController.USER_STATUS, "root");
                userPreferences.put(LoginPageController.USER_ID, "root");

                Parent root = null;
                try {
                    root = (Parent) FXMLLoader.load(this.getClass().getResource("/sample/views/login/LoginPage" +
                            ".fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Node node = (Node)(actionEvent.getSource());
                Stage primaryStage =
                        (Stage) node.getScene().getWindow();
                primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
                primaryStage.show();

            }
        });


        guardParking_button.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    ScrollPane anchorPane =
                            FXMLLoader.load(getClass().getResource("/sample/views/guard/guard_parking.fxml"));
                    contentViewPane.getChildren().setAll(anchorPane);
                } catch (IOException throwables) {
                    throwables.printStackTrace();
                }

            }
        });

        add_track_button.setOnAction(actionEvent -> {
            try {
                AnchorPane anchorPane =
                        FXMLLoader.load(getClass().getResource("/sample/views/guard/addition_record.fxml"));
                contentViewPane.getChildren().setAll(anchorPane);
            } catch (IOException throwables) {
                throwables.printStackTrace();
            }
        });

        update_track_button.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    ScrollPane pane =
                            FXMLLoader.load(this.getClass().getResource("/sample/views/guard/guard_update.fxml"));
                    contentViewPane.getChildren().setAll(pane);
                } catch (IOException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        Staffs_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    ScrollPane pane =
                            FXMLLoader.load(this.getClass().getResource("/sample/views/guard/guard_staff_track.fxml"));
                    contentViewPane.getChildren().setAll(pane);
                } catch (IOException throwables) {
                    throwables.printStackTrace();
                }
            }
        });
    }

}
