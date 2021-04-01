package sample.controllers;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StaffAttendance {

    Date date;
    int id;
    String name,type,mobile;
    String inTime,outTime;
    Time timeIn ,timeOut;

    public StaffAttendance(Date date, int id,
                           String name,
                           String type,
                           String mobile,
                           String inTime,
                           String outTime) {
        this.date = date;
        this.id = id;
        this.name = name;
        this.type = type;
        this.mobile = mobile;
        this.inTime = inTime;
        this.outTime = outTime;
    }

    public StaffAttendance(Date date, int id,
                           String name,
                           String type,
                           String mobile,
                           Time timeIn,
                           Time timeOut) {
        this.date = date;
        this.id = id;
        this.name = name;
        this.type = type;
        this.mobile = mobile;
        this.timeIn = timeIn;
        this.timeOut = timeOut;

        if(timeIn== null)
        {
            this.inTime = "Not Recorded";
        }
        else
        {
            this.inTime =
                    timeIn.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
        }

        if(timeOut == null)
        {
            this.outTime = "Not Recorded";
        }else {

        this.outTime =
                timeOut.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
        }




    }






    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }
}
