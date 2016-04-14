package com.milky.service.core;

/**
 * Created by Neha on 11/30/2015.
 */
public class GlobalSettings {
    private String Id;
    private double DefaultRate;
    private double Tax;
    private String RollDate;
    private int isDeleted;
    private int dirty;
    private String dateModified;

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getDirty() {
        return dirty;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getRollDate() {
        return RollDate;
    }

    public void setRollDate(String rollDate) {
        RollDate = rollDate;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public double getDefaultRate() {
        return DefaultRate;
    }

    public void setDefaultRate(double defaultRate) {
        this.DefaultRate = defaultRate;
    }

    public double getTax() {
        return Tax;
    }

    public void setTax(double tax) {
        this.Tax = tax;
    }
}
