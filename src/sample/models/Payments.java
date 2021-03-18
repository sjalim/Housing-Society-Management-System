package sample.models;

import com.jfoenix.controls.JFXToggleButton;

import java.sql.Date;

public class Payments {
    private int paymentId;
    private int pType;
    private String paymentType;
    private double amount;
    private String paymentStatus;
    private String flatNumber;
    private Date dateAdded;
    private int onlineId;
    private String transactionId;
    private int offlineId;
    private byte[] depositSlip;
    private String paymentMethod;
    private Date paidDate;

    public Payments() {
    }

    public Payments(int paymentId, String paymentType, String flatNumber, double amount, Date dateAdded,
                    String paymentStatus, String paymentMethod, Date paidDate, String transactionId, byte[] depositSlip) {
        this.paymentId = paymentId;
        this.paymentType = paymentType;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
        this.flatNumber = flatNumber;
        this.dateAdded = dateAdded;
        this.paymentMethod = paymentMethod;
        this.paidDate = paidDate;
        this.transactionId = transactionId;
        this.depositSlip = depositSlip;
    }

    public Payments(int pType, String paymentType) {
        this.pType = pType;
        this.paymentType = paymentType;
    }

    public Payments(String paymentType) {
        this.paymentType = paymentType;
    }

    @Override
    public String toString() {
        return paymentType;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getpType() {
        return pType;
    }

    public void setpType(int pType) {
        this.pType = pType;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getOnlineId() {
        return onlineId;
    }

    public void setOnlineId(int onlineId) {
        this.onlineId = onlineId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public int getOfflineId() {
        return offlineId;
    }

    public void setOfflineId(int offlineId) {
        this.offlineId = offlineId;
    }

    public byte[] getDepositSlip() {
        return depositSlip;
    }

    public void setDepositSlip(byte[] depositSlip) {
        this.depositSlip = depositSlip;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(Date paidDate) {
        this.paidDate = paidDate;
    }

}
