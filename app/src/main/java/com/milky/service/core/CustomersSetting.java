package com.milky.service.core;

import android.database.Cursor;

import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.serviceclasses.CustomersSettingService;

public class CustomersSetting extends CustomersSettingService {
    private int customerId;
    private double defaultRate;
    private double getDefaultQuantity;
    private String startDate;
    private String endDate;
    private int dirty;
    private boolean isCustomDelivery;

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

    public void setIsCustomDelivery(int isCustomDelivery) {
        this.isCustomDelivery = isCustomDelivery == 1? true:false;
    }

    public boolean getIsCustomDelivery() {
        return isCustomDelivery;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }

    public void PopulateFromCursor(Cursor cursor) {
        this.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
        this.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
        this.setDirty(cursor.getInt(cursor.getColumnIndex(TableColumns.Dirty)));
        this.setDefaultRate(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate)));
        this.setGetDefaultQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
        this.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
        this.setIsCustomDelivery(cursor.getInt(cursor.getColumnIndex(TableColumns.IsCustomDelivery)));
    }

}
