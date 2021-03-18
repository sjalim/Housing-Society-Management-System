package sample.controllers.manager.collectpayments;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.database.DatabaseHandler;
import sample.models.FlatList;
import sample.models.Payments;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class ManagerPaymentFlatListController implements Initializable {

    @FXML
    private TableView<FlatList> flatListTableView;

    @FXML
    private JFXCheckBox selectAll;

    @FXML
    private TableColumn<FlatList, String> col_flatNo;

    @FXML
    private TableColumn<FlatList, String> col_select;

    @FXML
    private JFXButton doneButton;

    @FXML
    private JFXTextField filteredFlatList;

    String query = null;
    private DatabaseHandler databaseHandler;
    ObservableList<FlatList> flatListObservableList;
    ObservableList<FlatList> items;

    private ManagerAddPaymentController managerAddPaymentController;
    public void setManagerAddPaymentController(ManagerAddPaymentController managerAddPaymentController) {
        this.managerAddPaymentController = managerAddPaymentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        flatListObservableList = FXCollections.observableArrayList();
        selectAll.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                items = flatListTableView.getItems();

                for (FlatList item : items) {
                    if (selectAll.isSelected())
                        item.getCheckBox().setSelected(true);
                    else
                        item.getCheckBox().setSelected(false);
                }
            }
        });

        try {
            loadFlatsFromDB();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        initFlatListTable();

        FilteredList<FlatList> filteredData = new FilteredList<>(flatListObservableList, b -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        filteredFlatList.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(flatList -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (flatList.getFlatNumber().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else
                    return false; // Does not match.
            });
        });
        // 3. Wrap the FilteredList in a SortedList.
        SortedList<FlatList> sortedData = new SortedList<>(filteredData);
        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(flatListTableView.comparatorProperty());
        // 5. Add sorted (and filtered) data to the table.
        flatListTableView.setItems(sortedData);

        doneButton.setOnAction(actionEvent -> {
            ObservableList<FlatList> addFLats = FXCollections.observableArrayList();
            for(FlatList item : flatListObservableList) {
                if(item.getCheckBox().isSelected()) {
                    addFLats.add(item);
                }
            }
            if(addFLats.toString() == null) {
                AlertDialog("Select Flats");
            } else {
                System.out.println(addFLats.toString());
            }
            managerAddPaymentController.loadFlatList(addFLats); // Passing flatList to Parent
            Stage stage = (Stage) doneButton.getScene().getWindow();
            stage.close();
        });

    }

    private void initFlatListTable() {
        col_flatNo.setCellValueFactory(
                new PropertyValueFactory<>("flatNumber")
        );
        col_select.setCellValueFactory(
                new PropertyValueFactory<>("checkBox")
        );
    }

    private void loadFlatsFromDB() throws SQLException, ClassNotFoundException {
        flatListObservableList.clear();
        databaseHandler = new DatabaseHandler();
        Statement statement = databaseHandler.getDbConnection().createStatement();

        query = "SELECT FlatNumber FROM FlatOwner";

        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            flatListObservableList.add(new FlatList(rs.getString("FlatNumber"), ""));
        }
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
