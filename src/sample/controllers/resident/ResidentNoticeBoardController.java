package sample.controllers.resident;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import sample.database.DatabaseHandler;
import sample.models.Notice;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ResidentNoticeBoardController implements Initializable{

    @FXML
    private JFXDatePicker fromDatePicker;

    @FXML
    private JFXDatePicker toDatePicker;

    @FXML
    private JFXButton findButton;

    @FXML private JFXButton searchButton;

    @FXML
    private JFXTextField filteredNotice;

    @FXML
    private JFXButton refreshButton;

    @FXML
    private JFXListView<Notice> noticeListView;

    String query;
    private DatabaseHandler databaseHandler;
    private ObservableList<Notice> noticeObservableList;
    private ObservableList<Notice> filteredByDateList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        filteredByDateList = FXCollections.observableArrayList();

        fromDatePicker.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });
        toDatePicker.setConverter(new StringConverter<LocalDate>()
        {
            private DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate)
            {
                if(localDate==null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString)
            {
                if(dateString==null || dateString.trim().isEmpty())
                {
                    return null;
                }
                return LocalDate.parse(dateString,dateTimeFormatter);
            }
        });

        Timeline refreshTableTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        event -> {
                            noticeObservableList = FXCollections.observableArrayList();
                            try {
                                loadFromDB();
                            } catch (SQLException | ClassNotFoundException throwables) {
                                throwables.printStackTrace();
                            }

                            noticeListView.setItems(noticeObservableList);
                            noticeListView.setCellFactory(ResidentNoticeRowController
                                    -> new ResidentNoticeRowController());

                        }));
        refreshTableTimeline.setCycleCount(Timeline.INDEFINITE);
        refreshTableTimeline.play();

        findButton.setOnAction(actionEvent -> {
            if (fromDatePicker.getValue() != null && toDatePicker.getValue() != null) {
                refreshTableTimeline.stop();
                String fDate = fromDatePicker.getValue().toString();
                String tDate = toDatePicker.getValue().toString();
                try {
                    filterByDate(fDate,tDate);
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
                noticeListView.setItems(null);
                noticeListView.setItems(filteredByDateList);
            } else {
                AlertDialog("Select all the properties");
            }
        });

        searchButton.setOnAction(actionEvent -> {
            refreshTableTimeline.stop();
            if(filteredNotice.getText() != null) {
                try {
                    filterBySearch(filteredNotice.getText());
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
                noticeListView.setItems(null);
                noticeListView.setItems(noticeObservableList);
            } else {
                AlertDialog("Search field is blank!");
            }
        });

        refreshButton.setOnAction(actionEvent -> {
            refreshTableTimeline.play();
        });
    }

    private void filterByDate(String fDate, String tDate) throws SQLException, ClassNotFoundException {
        filteredByDateList.clear();
        Statement st = databaseHandler.getDbConnection().createStatement();
        query = "SELECT * FROM NoticeBoard WHERE DateAdded BETWEEN '"+fDate+"'AND '"+tDate+"' ORDER BY NoticeId desc";

        ResultSet rs = st.executeQuery(query);
        while(rs.next()) {
            System.out.println(rs.getString("NoticeId"));
            filteredByDateList.add(new Notice(rs.getInt("NoticeId"),
                    rs.getString("NoticeTitle"),rs.getString("NoticeDescription"),
                    rs.getInt("ManagerId"),rs.getDate("DateAdded")));
        }
    }

    private void filterBySearch(String searchedTerm) throws SQLException, ClassNotFoundException {
        noticeObservableList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT * FROM NoticeBoard WHERE NoticeTitle LIKE '%"+searchedTerm+"%' ORDER BY NoticeId desc";

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            noticeObservableList.add(new Notice(rs.getInt("NoticeId"),
                    rs.getString("NoticeTitle"),rs.getString("NoticeDescription"),
                    rs.getInt("ManagerId"),rs.getDate("DateAdded")));
        }
        databaseHandler.getDbConnection().close();
    }

    private void loadFromDB() throws SQLException, ClassNotFoundException {
        noticeObservableList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT * FROM NoticeBoard ORDER BY NoticeId desc";

        ResultSet rs = statement.executeQuery(query);
        while(rs.next()) {
            noticeObservableList.add(new Notice(rs.getInt("NoticeId"),
                    rs.getString("NoticeTitle"),rs.getString("NoticeDescription"),
                    rs.getInt("ManagerId"),rs.getDate("DateAdded")));
        }
        System.out.println("Notice db loaded");
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
