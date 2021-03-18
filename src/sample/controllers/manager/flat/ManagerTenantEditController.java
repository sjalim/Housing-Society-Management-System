package sample.controllers.manager.flat;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import sample.database.DatabaseHandler;

import java.net.URL;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ManagerTenantEditController implements Initializable {

    @FXML public Label tenantId_EditField;

    @FXML public Label flatNo_EditField;

    @FXML public JFXTextField tenantName_EditField;

    @FXML public JFXTextField nid_EditField;

    @FXML public JFXTextField mobile_EditField;

    @FXML public JFXTextField prestAdd_EditField;

    @FXML public JFXTextField permaAdd_EditField;

    @FXML public JFXTextField occupation_EditField;

    @FXML public JFXTextField familyMember_EditField;

    @FXML public JFXDatePicker movedIn_DatePicker;

    @FXML public JFXDatePicker movedOut_DatePicker;

    @FXML public JFXButton save_EditButton;

    @FXML public JFXButton cancel_EditButton;

    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        movedIn_DatePicker.setConverter(new StringConverter<LocalDate>()
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
        movedOut_DatePicker.setConverter(new StringConverter<LocalDate>()
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
    }

    public void updateTenant() throws SQLException, ClassNotFoundException {
        databaseHandler = new DatabaseHandler();
        //Query
        String query1 = "UPDATE Tenant SET TenantName = '"+tenantName_EditField.getText()+
                "'WHERE Tenant.TenantId = '"+tenantId_EditField.getText()+"' " +
                "AND Tenant.FlatNumber = '"+flatNo_EditField.getText()+"'";

        PreparedStatement preparedStatement1 = databaseHandler.getDbConnection().prepareStatement(query1);
        preparedStatement1.executeUpdate();
        //Query for Second table
        String query2;
        if(movedOut_DatePicker.getValue() != null) {
            query2 = "UPDATE Tenant_Info SET Nid='"+nid_EditField.getText()+"', Mobile='"+mobile_EditField.getText()+"', " +
                    "PresentAddress='"+prestAdd_EditField.getText()+"', Occupation = '"+occupation_EditField.getText()+"'," +
                    "TotalFamilyMembers = '"+familyMember_EditField.getText()+"', MoveIn = '"+movedIn_DatePicker.getValue().toString()+"'," +
                    "MoveOut = '"+movedOut_DatePicker.getValue().toString()+"', PermanentAddress='"+permaAdd_EditField.getText()+
                    "'WHERE Tenant_Info.FlatNumber='"+flatNo_EditField.getText()+"' AND Tenant_Info.TenantId='"+tenantId_EditField.getText()+"'";
        } else {
            query2 = "UPDATE Tenant_Info SET Nid='"+nid_EditField.getText()+"', Mobile='"+mobile_EditField.getText()+"', " +
                    "PresentAddress='"+prestAdd_EditField.getText()+"', Occupation = '"+occupation_EditField.getText()+"'," +
                    "TotalFamilyMembers = '"+familyMember_EditField.getText()+"', MoveIn = '"+movedIn_DatePicker.getValue().toString()+"'," +
                    "PermanentAddress='"+permaAdd_EditField.getText()+
                    "'WHERE Tenant_Info.FlatNumber='"+flatNo_EditField.getText()+"' AND Tenant_Info.TenantId='"+tenantId_EditField.getText()+"'";
        }

        PreparedStatement preparedStatement2 = databaseHandler.getDbConnection().prepareStatement(query2);
        preparedStatement2.executeUpdate();
    }
}
