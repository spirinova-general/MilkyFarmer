package com.milky.service.core;

import android.database.Cursor;

import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceclasses.CustomersSettingService;


//Why was is derived from CustomerSettingServic!!!!
public class CustomersSetting  {
    private int id;
    private int customerId;
    private double defaultRate;
    private double getDefaultQuantity;
    private String startDate;
    private String endDate;
    private int dirty;
    private int isDeleted;
    private String dateModified;
    private boolean isCustomDelivery;

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public boolean isCustomDelivery() {
        return isCustomDelivery;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setIsCustomDelivery(boolean isCustomDelivery) {
        this.isCustomDelivery = isCustomDelivery;
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
        this.setIsCustomDelivery(Utils.GetBoolean(cursor.getInt(cursor.getColumnIndex(TableColumns.IsCustomDelivery))));
        this.setId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
        this.setIsDeleted(cursor.getInt(cursor.getColumnIndex(TableColumns.IsDeleted)));
        this.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
    }

}
