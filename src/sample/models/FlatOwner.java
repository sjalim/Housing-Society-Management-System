package sample.models;

public class FlatOwner {
    private String flatNumber;
    private String ownerName;
    private String ownerPassword;
    private String mobile;
    private String nid;
    private String presentAdd;
    private String permanentAdd;
    private int managerId;
    private int allocatedParkingNo;

    public FlatOwner() {
    }

    public FlatOwner(String flatNumber, String ownerName, String ownerPassword, String mobile, String nid, String presentAdd, String permanentAdd, int managerId, int allocatedParkingNo) {
        this.flatNumber = flatNumber;
        this.ownerName = ownerName;
        this.ownerPassword = ownerPassword;
        this.mobile = mobile;
        this.nid = nid;
        this.presentAdd = presentAdd;
        this.permanentAdd = permanentAdd;
        this.managerId = managerId;
        this.allocatedParkingNo = allocatedParkingNo;
    }

    public FlatOwner(String flatNumber, String ownerName, String mobile, String presentAdd, String permanentAdd, String nid, int allocatedParkingNo) {
        this.flatNumber = flatNumber;
        this.ownerName = ownerName;
        this.mobile = mobile;
        this.nid = nid;
        this.presentAdd = presentAdd;
        this.permanentAdd = permanentAdd;
        this.allocatedParkingNo = allocatedParkingNo;
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

    public String getOwnerPassword() {
        return ownerPassword;
    }

    public void setOwnerPassword(String ownerPassword) {
        this.ownerPassword = ownerPassword;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getPresentAdd() {
        return presentAdd;
    }

    public void setPresentAdd(String presentAdd) {
        this.presentAdd = presentAdd;
    }

    public String getPermanentAdd() {
        return permanentAdd;
    }

    public void setPermanentAdd(String permanentAdd) {
        this.permanentAdd = permanentAdd;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getAllocatedParkingNo() {
        return allocatedParkingNo;
    }

    public void setAllocatedParkingNo(int allocatedParkingNo) {
        this.allocatedParkingNo = allocatedParkingNo;
    }
}
