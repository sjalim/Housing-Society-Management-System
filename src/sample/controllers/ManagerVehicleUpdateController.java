package sample.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManagerVehicleUpdateController implements Initializable {

    String flatNum, carRegNum, carModel,
            selectedOwnerType;
    public static final String RESIDENT_OWNER_TYPE_OWNED =
            "OWNED";
    public static final String RESIDENT_OWNER_TYPE_TENANT = "TENANT";

    RequiredFieldValidator invalidator = new RequiredFieldValidator();

    Vehicle vehicle;
    ArrayList<String> residentOwnerTypeNames;
    @FXML
    private JFXComboBox<String> resident_owner_type_combo_box;


    @FXML
    private JFXTextField flat_num_text_field;

    @FXML
    private JFXTextField car_reg_num_text_field;

    @FXML
    private JFXTextField car_model_text_field;

    @FXML
    private Label parking_slot_num_label;

    @FXML
    void handleCancel(ActionEvent event) {
        Node node = (Node) (event.getSource());
        Stage stage =
                (Stage) node.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleConfirm(ActionEvent event) {

        flatNum = flat_num_text_field.getText();
        carRegNum =
                car_reg_num_text_field.getText();
        carModel = car_model_text_field.getText();

        if(!flatNum.isEmpty() && !carModel.isEmpty() && !carRegNum.isEmpty() && !selectedOwnerType.isEmpty())
        {


        }

        if(selectedOwnerType.isEmpty())
        {
            resident_owner_type_combo_box.validate();
        }
        if(carRegNum.isEmpty())
        {
            car_reg_num_text_field.validate();
        }

        if(carModel.isEmpty())
        {
            car_model_text_field.validate();

        }

        if(flatNum.isEmpty())
        {
            flat_num_text_field.validate();
        }



        Node node = (Node) (event.getSource());
        Stage stage =
                (Stage) node.getScene().getWindow();
        stage.close();
    }

    @FXML
    void handleOwnerTypeComboBox(ActionEvent event) {

        selectedOwnerType =
                resident_owner_type_combo_box.getValue();

        if(!selectedOwnerType.isEmpty())
        {
         resident_owner_type_combo_box.getValidators().add(invalidator);
         resident_owner_type_combo_box.validate();
        }

    }

    void setTextData(Vehicle vehicle) {
        this.vehicle = vehicle;

        parking_slot_num_label.setText(vehicle.getParkingSlotNum());
        flat_num_text_field.setText(vehicle.getFlatNumber());
        car_model_text_field.setText(vehicle.getCarModel());
        car_reg_num_text_field.setText(vehicle.getCarRegNum());
    }

    @Override
    public void initialize(URL url,
                           ResourceBundle resourceBundle) {

        residentOwnerTypeNames =
                new ArrayList<>();

        residentOwnerTypeNames.add(RESIDENT_OWNER_TYPE_OWNED);
        residentOwnerTypeNames.add(RESIDENT_OWNER_TYPE_TENANT);
        ObservableList<String> list =
                FXCollections.observableList(residentOwnerTypeNames);
        resident_owner_type_combo_box.setItems(list);

        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Fill the Field!");

        flat_num_text_field.getValidators().add(validator);
        car_model_text_field.getValidators().add(validator);
        car_reg_num_text_field.getValidators().add(validator);
        resident_owner_type_combo_box.getValidators().add(validator);

        invalidator.setMessage("");


        flat_num_text_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                flat_num_text_field.getValidators().add(invalidator);
                flat_num_text_field.validate();
            }
        });

        car_model_text_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                car_model_text_field.getValidators().add(invalidator);
                car_model_text_field.validate();
            }
        });

        car_reg_num_text_field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                car_reg_num_text_field.getValidators().add(invalidator);
                car_reg_num_text_field.validate();
            }
        });

    }
}
