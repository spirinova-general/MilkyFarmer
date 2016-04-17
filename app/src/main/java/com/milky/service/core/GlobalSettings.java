package com.milky.service.core;

import android.content.ContentValues;
import android.database.Cursor;

import com.milky.service.databaseutils.TableColumns;

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
    private String lastBillSyncedTime;
    private String dateModified;

    public String getLastBillSyncedTime(){
        return lastBillSyncedTime; }

    public void setLastBillSyncedTime(String lastBillSyncedTime ){
        this.lastBillSyncedTime = lastBillSyncedTime;
    }
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

    public ContentValues ToContentValues() {
        ContentValues values = new ContentValues();

        values.put(TableColumns.RollDate, this.getRollDate());
        values.put(TableColumns.TAX, this.getTax());
        values.put(TableColumns.DefaultRate, this.getDefaultRate());
        values.put(TableColumns.IsDeleted,this.getIsDeleted());
        values.put(TableColumns.Dirty,this.getDirty());
        values.put(TableColumns.DateModified,this.getDateModified());
        values.put(TableColumns.LastBillSyncedTime,this.getLastBillSyncedTime());
        return values;
    }

    public void PopulateFromCursor(Cursor cursor) {
        this.setDefaultRate(cursor.getInt(cursor.getColumnIndex(TableColumns.DefaultRate)));
        this.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
        this.setTax(cursor.getInt(cursor.getColumnIndex(TableColumns.TAX)));
        this.setIsDeleted(cursor.getInt(cursor.getColumnIndex(TableColumns.IsDeleted)));
        this.setDirty(cursor.getInt(cursor.getColumnIndex(TableColumns.Dirty)));
        this.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
        this.setLastBillSyncedTime(cursor.getString(cursor.getColumnIndex(TableColumns.LastBillSyncedTime)));
    }
}
