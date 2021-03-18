package sample.models;

import com.jfoenix.controls.JFXCheckBox;

public class FlatList {
    private int flatOwnerId;
    private String flatNumber;
    private JFXCheckBox checkBox;

    public FlatList() {
    }

    public FlatList(String flatNumber, String value) {
        this.flatNumber = flatNumber;
        this.checkBox = new JFXCheckBox();
    }

    @Override
    public String toString() {
        return flatNumber;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public JFXCheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(JFXCheckBox checkBox) {
        this.checkBox = checkBox;
    }
}
