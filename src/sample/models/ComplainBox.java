package sample.models;

import org.joda.time.DateTime;

import java.sql.Timestamp;

public class ComplainBox {
    private int complainId;
    private String flatNumber;
    private String complainStatus;
    private String complainTitle;
    private String complainDescription;
    private int voteFlag;
    private String dateAdded;
    private int voteUp;
    private int voteDown;
    private int categoryId;
    private String category;

    public ComplainBox() {
    }

    public ComplainBox(int complainId, String flatNumber, String complainStatus, String complainTitle,
                       String complainDescription, int voteFlag, String dateAdded) {
        this.complainId = complainId;
        this.flatNumber = flatNumber;
        this.complainStatus = complainStatus;
        this.complainTitle = complainTitle;
        this.complainDescription = complainDescription;
        this.voteFlag = voteFlag;
        this.dateAdded = dateAdded;
    }

    public ComplainBox(int complainId, String flatNumber, String complainStatus,
                       String complainTitle, String complainDescription, String dateAdded, int voteUp, int voteDown, String category) {
        this.complainId = complainId;
        this.flatNumber = flatNumber;
        this.complainStatus = complainStatus;
        this.complainTitle = complainTitle;
        this.complainDescription = complainDescription;
        this.dateAdded = dateAdded;
        this.voteUp = voteUp;
        this.voteDown = voteDown;
        this.category = category;
    }

    public ComplainBox(int categoryId, String category) {
        this.categoryId = categoryId;
        this.category = category;
    }

    @Override
    public String toString() {
        return category;
    }

    public int getComplainId() {
        return complainId;
    }

    public void setComplainId(int complainId) {
        this.complainId = complainId;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getComplainStatus() {
        return complainStatus;
    }

    public void setComplainStatus(String complainStatus) {
        this.complainStatus = complainStatus;
    }

    public String getComplainTitle() {
        return complainTitle;
    }

    public void setComplainTitle(String complainTitle) {
        this.complainTitle = complainTitle;
    }

    public String getComplainDescription() {
        return complainDescription;
    }

    public void setComplainDescription(String complainDescription) {
        this.complainDescription = complainDescription;
    }

    public int getVoteFlag() {
        return voteFlag;
    }

    public void setVoteFlag(int voteFlag) {
        this.voteFlag = voteFlag;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getVoteUp() {
        return voteUp;
    }

    public void setVoteUp(int voteUp) {
        this.voteUp = voteUp;
    }

    public int getVoteDown() {
        return voteDown;
    }

    public void setVoteDown(int voteDown) {
        this.voteDown = voteDown;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
