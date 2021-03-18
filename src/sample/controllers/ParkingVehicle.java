package sample.controllers;

import java.sql.Date;
import java.sql.Time;
import java.time.format.DateTimeFormatter;

public class ParkingVehicle {

    String flatNumber,ownerName,carModel,
            carNumber;
    Date date;
    Time inTime, outTime;
    String timeIn,timeOut;
    int id;

    public ParkingVehicle(int id,
                          String flatNumber,
                           String carModel, String carNumber, Date date, Time inTime, Time outTime) {
        this.id = id;
        this.flatNumber = flatNumber;

        this.carModel = carModel;
        this.carNumber = carNumber;
        this.date = date;
        this.inTime = inTime;
        this.outTime = outTime;
        this.timeIn =
                inTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));
      this.timeOut  =
              outTime.toLocalTime().format(DateTimeFormatter.ofPattern("hh:mm a"));


    }

    public String getTimeIn() {
        return timeIn;
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(String timeOut) {
        this.timeOut = timeOut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getInTime() {
        return inTime;
    }

    public void setInTime(Time inTime) {
        this.inTime = inTime;
    }

    public Time getOutTime() {
        return outTime;
    }

    public void setOutTime(Time outTime) {
        this.outTime = outTime;
    }
}
