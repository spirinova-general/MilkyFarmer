package com.milky.viewmodel;

/**
 * Created by Neha on 12/26/2015.
 */
public class VAccount {
    public String FarmerCode;
    public String FirstName;
    public String LastName;
    public boolean Validated;
    public String DateAdded;
    public String DateModified;
    public String Rate;
    public String Tax;
    public String Mobile;
    public String Dirty;
    public String Sync;

    public String getRate() {
        return Rate;
    }

    public void setRate(String rate) {
        Rate = rate;
    }

    public String getTax() {
        return Tax;
    }

    public void setTax(String tax) {
        Tax = tax;
    }

    public String getDirty() {
        return Dirty;
    }

    public void setDirty(String dirty) {
        Dirty = dirty;
    }

    public String getSync() {
        return Sync;
    }

    public void setSync(String sync) {
        Sync = sync;
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

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public boolean isValidated() {
        return Validated;
    }

    public void setValidated(boolean validated) {
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
}
