package sample.controllers;

import java.sql.Date;

public class Vehicle {

    String flatNumber, parkingSlotNum,
            carRegNum, carModel,ownerType;
    Date allocationDate;
    int carId;

    public Vehicle(String flatNumber,
                   String parkingSlotNum, String carRegNum, String carModel, String ownerType, Date allocationDate, int carId) {
        this.flatNumber = flatNumber;
        this.parkingSlotNum = parkingSlotNum;
        this.carRegNum = carRegNum;
        this.carModel = carModel;
        this.ownerType = ownerType;
        this.allocationDate = allocationDate;
        this.carId = carId;
    }

    public Vehicle(String carRegNum,
                   String carModel) {
        this.carRegNum = carRegNum;
        this.carModel = carModel;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getParkingSlotNum() {
        return parkingSlotNum;
    }

    public void setParkingSlotNum(String parkingSlotNum) {
        this.parkingSlotNum = parkingSlotNum;
    }

    public String getCarRegNum() {
        return carRegNum;
    }

    public void setCarRegNum(String carRegNum) {
        this.carRegNum = carRegNum;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Date getAllocationDate() {
        return allocationDate;
    }

    public void setAllocationDate(Date allocationDate) {
        this.allocationDate = allocationDate;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
}
