package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.controllers.LoginPageController;
import sample.controllers.resident.FlatOwnerDashboardController;
import sample.database.DatabaseHandler;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.prefs.Preferences;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {


        Preferences userPreferences = Preferences.userRoot();
        String userStatus = userPreferences.get(LoginPageController.USER_STATUS, "root");
        String userId = userPreferences.get(LoginPageController.USER_ID, "root");

//        Singleton Instance
        UserId mUserId = UserId.getInstance();
        mUserId.mId = userId;

        String allocationStatus = userPreferences.get(LoginPageController.ALLOCATION_STATUS, "root");
        System.out.println(userId + " " + userId + " " + allocationStatus);
        if (userStatus.equals("root")) {
            Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("views/login/LoginPage.fxml"));
            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
            primaryStage.show();

        } else if (userStatus.equals(LoginPageController.MANAGER_STATUS)) {
            //Singleton Instance
            mUserId.mId  = findManagerId(mUserId.mId);

            Parent root = (Parent) FXMLLoader.load(this.getClass().getResource("views/manager/manager_dashboard.fxml"));
            primaryStage.setTitle("Manager");
            primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
            primaryStage.show();

        } else if (userStatus.equals(LoginPageController.GUARD_STATUS)) {
            Parent root =
                    (Parent) FXMLLoader.load(this.getClass().getResource("views/guard/guard_dashboard.fxml"));
            primaryStage.setTitle("Guard");
            primaryStage.setScene(new Scene(root, 1200.0D, 500.0D));
            primaryStage.show();
        } else if (userStatus.equals(LoginPageController.RESIDENT_STATUS)) {
            if (allocationStatus.equals(LoginPageController.ALLOCATION_STATUS_OWNED)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("views/resident/flatOwner_dashboard.fxml"));
                Parent root = (Parent) loader.load();
                FlatOwnerDashboardController flatOwnerDashboardController = loader.getController();
                flatOwnerDashboardController.dashboard_flatStatus.setText(allocationStatus);

                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/resident/complaint_box.fxml"));
                flatOwnerDashboardController.contentViewPane.getChildren().clear();
                flatOwnerDashboardController.contentViewPane.getChildren().setAll(anchorPane);

                primaryStage.setTitle("Flat User");
                primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
                primaryStage.show();
            } else if (allocationStatus.equals(LoginPageController.ALLOCATION_STATUS_RENTED)) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(this.getClass().getResource("views/resident/flatOwner_dashboard.fxml"));
                Parent root = (Parent) loader.load();
                FlatOwnerDashboardController flatOwnerDashboardController = loader.getController();
                flatOwnerDashboardController.dashboard_flatStatus.setText(allocationStatus);

                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/sample/views/resident/complaint_box.fxml"));
                flatOwnerDashboardController.contentViewPane.getChildren().clear();
                flatOwnerDashboardController.contentViewPane.getChildren().setAll(anchorPane);

                primaryStage.setTitle("Flat User");
                primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
                primaryStage.show();
            } else {

            }
        } else {
            System.out.println("Error at Main.java");
        }
    }

    private String findManagerId(String mId) throws SQLException, ClassNotFoundException {
        String id = null;
        DatabaseHandler databaseHandler = new DatabaseHandler();
        Statement st = databaseHandler.getDbConnection().createStatement();

        String query = "select " +
                "ManagerId from Manager where Mobile=" + mId + ";";

        ResultSet rs = st.executeQuery(query);
        if (rs.next()) {
            id = rs.getString(1);
        }
        databaseHandler.getDbConnection().close();
        return id;
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
