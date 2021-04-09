package sample.controllers;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.UserId;
import sample.controllers.resident.FlatOwnerDashboardController;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class Controller implements Initializable {


    Stage primaryStage;





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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        try {
//
//             primaryStage.setResizable(false);
//            Preferences userPreferences =
//                    Preferences.userRoot();
//            String userStatus =
//                    userPreferences.get(LoginPageController.USER_STATUS, "root");
//            String userId =
//                    userPreferences.get(LoginPageController.USER_ID, "root");
//
//            //        Singleton Instance
//            UserId mUserId = UserId.getInstance();
//            mUserId.mId = userId;
//
//            String allocationStatus =
//                    userPreferences.get(LoginPageController.ALLOCATION_STATUS, "root");
//            System.out.println(userId + " " + userId + " " + allocationStatus);
//            if (userStatus.equals("root")) {
//                Parent root =
//                        (Parent) FXMLLoader.load(this.getClass().getResource("views/login/LoginPage.fxml"));
//                primaryStage.setTitle("Hello " +
//                        "World");
//                primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
//                primaryStage.show();
//
//            } else if (userStatus.equals(LoginPageController.MANAGER_STATUS)) {
//                //Singleton Instance
//                mUserId.mId =
//                        findManagerId(mUserId.mId);
//
//                Parent root =
//                        (Parent) FXMLLoader.load(this.getClass().getResource("views/manager/manager_dashboard.fxml"));
//                primaryStage.setTitle("Manager");
//                primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
//                primaryStage.show();
//
//            } else if (userStatus.equals(LoginPageController.GUARD_STATUS)) {
//                Parent root =
//                        (Parent) FXMLLoader.load(this.getClass().getResource("views/guard/guard_dashboard.fxml"));
//                primaryStage.setTitle("Guard");
//                primaryStage.setScene(new Scene(root, 1200.0D, 500.0D));
//                primaryStage.show();
//            } else if (userStatus.equals(LoginPageController.RESIDENT_STATUS)) {
//                if (allocationStatus.equals(LoginPageController.ALLOCATION_STATUS_OWNED)) {
//                    FXMLLoader loader =
//                            new FXMLLoader();
//                    loader.setLocation(this.getClass().getResource("views/resident/flatOwner_dashboard.fxml"));
//                    Parent root =
//                            (Parent) loader.load();
//                    FlatOwnerDashboardController flatOwnerDashboardController = loader.getController();
//                    flatOwnerDashboardController.dashboard_flatStatus.setText(allocationStatus);
//
//                    AnchorPane anchorPane =
//                            FXMLLoader.load(getClass().getResource("/sample/views/resident/complaint_box.fxml"));
//                    flatOwnerDashboardController.contentViewPane.getChildren().clear();
//                    flatOwnerDashboardController.contentViewPane.getChildren().setAll(anchorPane);
//
//                    primaryStage.setTitle("Flat" +
//                            " User");
//                    primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
//                    primaryStage.show();
//                } else if (allocationStatus.equals(LoginPageController.ALLOCATION_STATUS_RENTED)) {
//                    FXMLLoader loader =
//                            new FXMLLoader();
//                    loader.setLocation(this.getClass().getResource("views/resident/flatOwner_dashboard.fxml"));
//                    Parent root =
//                            (Parent) loader.load();
//                    FlatOwnerDashboardController flatOwnerDashboardController = loader.getController();
//                    flatOwnerDashboardController.dashboard_flatStatus.setText(allocationStatus);
//
//                    AnchorPane anchorPane =
//                            FXMLLoader.load(getClass().getResource("/sample/views/resident/complaint_box.fxml"));
//                    flatOwnerDashboardController.contentViewPane.getChildren().clear();
//                    flatOwnerDashboardController.contentViewPane.getChildren().setAll(anchorPane);
//
//                    primaryStage.setTitle("Flat" +
//                            " User");
//                    primaryStage.setScene(new Scene(root, 1200.0D, 700.0D));
//                    primaryStage.show();
//                } else {
//
//                }
//            } else {
//                System.out.println("Error at Main.java");
//            }
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }

    }

    public void setPrimaryStage(Stage stage)
    {
        this.primaryStage = stage;
    }
}
