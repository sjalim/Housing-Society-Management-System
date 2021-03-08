package sample.controllers;

public class Staff {

    String name,type,shift_start,shift_end,mobile;

    public Staff(String name, String type, String shift_start, String shift_end, String mobile) {
        this.name = name;
        this.type = type;
        this.shift_start = shift_start;
        this.shift_end = shift_end;
        this.mobile = mobile;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShift_start() {
        return shift_start;
    }

    public void setShift_start(String shift_start) {
        this.shift_start = shift_start;
    }

    public String getShift_end() {
        return shift_end;
    }

    public void setShift_end(String shift_end) {
        this.shift_end = shift_end;
    }
}
