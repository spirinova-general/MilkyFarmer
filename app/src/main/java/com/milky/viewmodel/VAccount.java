package com.milky.viewmodel;

/**
 * Created by Neha on 12/26/2015.
 */
public class VAccount {
    public String FarmerCode;
    public String FirstName;
    public String LastName;
    public int Validated;
    public String DateAdded;
    public String DateModified;
    public int Dirty;
    public int TotalSms;
    public int UsedSms;
    public String EndDate;
    public int ServerAccountId;
    public String Mobile;

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
