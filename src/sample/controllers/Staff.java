package sample.controllers;

public class Staff {

    String name,type,shift_start,shift_end,
    mobile,present_ad,permanent_ad,nid;
    int id,salary,age;

    public Staff(String name, String type, String shift_start, String shift_end, String mobile, String present_ad,
                 String permanent_ad, int age,
                 String nid, int salary,int id) {
        this.name = name;
        this.type = type;
        this.shift_start = shift_start;
        this.shift_end = shift_end;
        this.mobile = mobile;
        this.present_ad = present_ad;
        this.permanent_ad = permanent_ad;
        this.id = id;
        this.salary = salary;
        this.age = age;
        this.nid = nid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPresent_ad() {
        return present_ad;
    }

    public void setPresent_ad(String present_ad) {
        this.present_ad = present_ad;
    }

    public String getPermanent_ad() {
        return permanent_ad;
    }

    public void setPermanent_ad(String permanent_ad) {
        this.permanent_ad = permanent_ad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
