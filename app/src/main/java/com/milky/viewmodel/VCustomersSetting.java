package com.milky.viewmodel;

public class VCustomersSetting
{
    private int customerId;
    private double defaultRate;
    private double getDefaultQuantity;
    private String startDate;
    private String endDate;
    private int dirty;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

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

    public int getDirty() {
        return dirty;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }
}
