package com.milky.viewmodel;

/**
 * Created by Neha on 11/17/2015.
 */
public class VCustomers {
    private String firstName;
    private String lastName;
//    private String rollDate;
//    private double quantity;
//    private int area;
    private double balance_amount;
//    private String start_date;
//    private String end_date;
//    private String city;
    private String dateAdded;
    private String dateModified;
    private String address1;
    private String address2;
    private int areaId;
    private String mobile;
//    private int accountId;
//    private double rate;
//    private double tax;
//    private double adjustment;
//    private int isCleared;
//    private double paymentMade;
//    private int outstanding;
//    private double total;
//    private int balanceType;
//    private String locality;
    private int isDeleted;
    private String deletedOn;
    private int customerId;
    private double defaultRate;
    private double getDefaultQuantity;
    private String startDate;
    private String endDate;
    private int dirty;

    public double getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(double defaultRate) {
        this.defaultRate = defaultRate;
    }

    public double getGetDefaultQuantity() {
        return getDefaultQuantity;
    }

    public void setGetDefaultQuantity(double getDefaultQuantity) {
        this.getDefaultQuantity = getDefaultQuantity;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(String deletedOn) {
        this.deletedOn = deletedOn;
    }
    public int getDirty() {
        return dirty;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public double getBalance_amount() {
        return balance_amount;
    }

    public void setBalance_amount(double balance_amount) {
        this.balance_amount = balance_amount;
    }


    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }


}
