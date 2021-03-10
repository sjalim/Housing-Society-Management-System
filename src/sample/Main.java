package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.controllers.LoginPageController;

import java.io.IOException;
import java.util.prefs.Preferences;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        Preferences userPreferences = Preferences.userRoot();
        String userStatus = userPreferences.get(LoginPageController.USER_STATUS, "root");
        String userId = userPreferences.get(LoginPageController.USER_ID, "root");
        String allocationStatus = userPreferences.get(LoginPageController.ALLOCATION_STATUS, "root");
        System.out.println(userId + " " + userId + " " + allocationStatus);
        if (userStatus.equals("root")) {
            Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("views/login/LoginPage.fxml"));
            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
            primaryStage.show();

        } else if (userStatus.equals(LoginPageController.MANAGER_STATUS)) {
            Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("views/manager/manager_dashboard.fxml"));
            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
            primaryStage.show();

        } else if (userStatus.equals(LoginPageController.GUARD_STATUS)) {
            Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("views/guard/Guard.fxml"));
            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
            primaryStage.show();
        } else if (userStatus.equals(LoginPageController.RESIDENT_STATUS)) {
            if (allocationStatus.equals(LoginPageController.ALLOCATION_STATUS_OWNED)) {

                Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("views/flat/FlatOwner.fxml"));
                primaryStage.setTitle("Hello World");
                primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
                primaryStage.show();
            } else if (allocationStatus.equals(LoginPageController.ALLOCATION_STATUS_RENTED)) {
                Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("views/flat/Tenant.fxml.fxml"));
                primaryStage.setTitle("Hello World");
                primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
                primaryStage.show();
            } else {

            }
        } else {
            System.out.println("Error at Main.java");
        }
    }

    void test(Stage primaryStage) throws IOException {
        Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("views/manager/manager_staff_add.fxml"));

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
