package sample.controllers;

public class Visitor {

    String name,flatNumber, inTime,outTime;
    int id,guardId,gateNo;


    public Visitor(String name, String flatNumber, String inTime, String outTime, int id, int guardId, int gateNo) {
        this.name = name;
        this.flatNumber = flatNumber;
        this.inTime = inTime;
        this.outTime = outTime;
        this.id = id;
        this.guardId = guardId;
        this.gateNo = gateNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuardId() {
        return guardId;
    }

    public void setGuardId(int guardId) {
        this.guardId = guardId;
    }

    public int getGateNo() {
        return gateNo;
    }

    public void setGateNo(int gateNo) {
        this.gateNo = gateNo;
    }
}
