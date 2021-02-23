package sample.models;

public class Tenant {
    private String flatNumber;
    private String tenantName;
    private String mobile;
    private String nid;
    private String presentAdd;
    private String permanentAdd;
    private int managerId;

    public Tenant() {
    }

    public Tenant(String flatNumber, String tenantName, String mobile, String nid, String presentAdd, String permanentAdd, int managerId) {
        this.flatNumber = flatNumber;
        this.tenantName = tenantName;
        this.mobile = mobile;
        this.nid = nid;
        this.presentAdd = presentAdd;
        this.permanentAdd = permanentAdd;
        this.managerId = managerId;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
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
}
