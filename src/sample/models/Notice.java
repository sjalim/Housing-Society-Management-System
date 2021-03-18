package sample.models;

import java.sql.Date;

public class Notice {
    private int noticeId;
    private String noticeTitle;
    private String noticeDescription;
    private int managerId;
    private Date dateAdded;

    public Notice() {
    }

    public Notice(int noticeId, String noticeTitle, String noticeDescription, int managerId, Date dateAdded) {
        this.noticeId = noticeId;
        this.noticeTitle = noticeTitle;
        this.noticeDescription = noticeDescription;
        this.managerId = managerId;
        this.dateAdded = dateAdded;
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeTitle() {
        return noticeTitle;
    }

    public void setNoticeTitle(String noticeTitle) {
        this.noticeTitle = noticeTitle;
    }

    public String getNoticeDescription() {
        return noticeDescription;
    }

    public void setNoticeDescription(String noticeDescription) {
        this.noticeDescription = noticeDescription;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
