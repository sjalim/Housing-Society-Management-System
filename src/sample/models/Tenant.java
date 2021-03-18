package sample.models;

import java.sql.Date;

public class Tenant {
    private int tenantId;
    private String flatNumber;
    private String tenantName;
    private String mobile;
    private String nid;
    private String presentAdd;
    private String permanentAdd;
    private String occupation;
    private int totalFamilyMember;
    private int managerId;
    private Date moveIn;
    private Date moveOut;

    public Tenant() {
    }

    public Tenant(int tenantId, String flatNumber, String tenantName, String mobile, String nid, String presentAdd,
                  String permanentAdd, String occupation, int totalFamilyMember, int managerId, Date moveIn, Date moveOut) {
        this.tenantId = tenantId;
        this.flatNumber = flatNumber;
        this.tenantName = tenantName;
        this.mobile = mobile;
        this.nid = nid;
        this.presentAdd = presentAdd;
        this.permanentAdd = permanentAdd;
        this.occupation = occupation;
        this.totalFamilyMember = totalFamilyMember;
        this.managerId = managerId;
        this.moveIn = moveIn;
        this.moveOut = moveOut;
    }

    public Tenant(int tenantId, String flatNumber, String tenantName, String mobile, String presentAdd,
                  String permanentAdd, String nid, String occupation, int totalFamilyMember, Date moveIn, Date moveOut) {
        this.tenantId = tenantId;
        this.flatNumber = flatNumber;
        this.tenantName = tenantName;
        this.mobile = mobile;
        this.nid = nid;
        this.presentAdd = presentAdd;
        this.permanentAdd = permanentAdd;
        this.occupation = occupation;
        this.totalFamilyMember = totalFamilyMember;
        this.moveIn = moveIn;
        this.moveOut = moveOut;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getTotalFamilyMember() {
        return totalFamilyMember;
    }

    public void setTotalFamilyMember(int totalFamilyMember) {
        this.totalFamilyMember = totalFamilyMember;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public Date getMoveIn() {
        return moveIn;
    }

    public void setMoveIn(Date moveIn) {
        this.moveIn = moveIn;
    }

    public Date getMoveOut() {
        return moveOut;
    }

    public void setMoveOut(Date moveOut) {
        this.moveOut = moveOut;
    }
}
