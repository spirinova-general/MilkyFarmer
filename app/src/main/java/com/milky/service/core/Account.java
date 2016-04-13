package com.milky.service.core;

/**
 * Created by Neha on 12/26/2015.
 */
public class Account {
    private String FarmerCode;
    private String FirstName;
    private String LastName;
    private int Validated;
    private String DateAdded;
    private int Dirty;
    private int TotalSms;
    private int UsedSms;
    private String EndDate;
    private int ServerAccountId;
    private String Mobile;
    private int isDeleted;
    private String DateModified;

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getFarmerCode() {
        return FarmerCode;
    }

    public void setFarmerCode(String farmerCode) {
        FarmerCode = farmerCode;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public int getValidated() {
        return Validated;
    }

    public void setValidated(int validated) {
        Validated = validated;
    }

    public String getDateAdded() {
        return DateAdded;
    }

    public void setDateAdded(String dateAdded) {
        DateAdded = dateAdded;
    }

    public String getDateModified() {
        return DateModified;
    }

    public void setDateModified(String dateModified) {
        DateModified = dateModified;
    }

    public int getDirty() {
        return Dirty;
    }

    public void setDirty(int dirty) {
        Dirty = dirty;
    }

    public int getTotalSms() {
        return TotalSms;
    }

    public void setTotalSms(int totalSms) {
        TotalSms = totalSms;
    }

    public int getUsedSms() {
        return UsedSms;
    }

    public void setUsedSms(int usedSms) {
        UsedSms = usedSms;
    }

    public int getServerAccountId() {
        return ServerAccountId;
    }

    public void setServerAccountId(int serverAccountId) {
        ServerAccountId = serverAccountId;
    }
}
