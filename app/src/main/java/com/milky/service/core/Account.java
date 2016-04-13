package com.milky.service.core;

import android.content.ContentValues;
import android.database.Cursor;

import com.milky.service.databaseutils.TableColumns;

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

    public void PopulateFromCursor(Cursor cursor)
    {
        this.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.FarmerCode)));
        this.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
        this.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
        this.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));

        this.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
        this.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
        this.setTotalSms(cursor.getInt(cursor.getColumnIndex(TableColumns.TotalSms)));
        this.setUsedSms(cursor.getInt(cursor.getColumnIndex(TableColumns.UsedSms)));
        this.setServerAccountId(cursor.getInt(cursor.getColumnIndex(TableColumns.ServerAccountId)));
        this.setValidated(cursor.getInt(cursor.getColumnIndex(TableColumns.Validated)));
        this.setIsDeleted(cursor.getInt(cursor.getColumnIndex(TableColumns.IsDeleted)));
        this.setDirty(cursor.getInt(cursor.getColumnIndex(TableColumns.Dirty)));
        this.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
    }

    public ContentValues ToContentValues()
    {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FarmerCode, this.getFarmerCode());
        values.put(TableColumns.FirstName, this.getFirstName());
        values.put(TableColumns.LastName, this.getLastName());
        values.put(TableColumns.Mobile, this.getMobile());

        values.put(TableColumns.DateAdded, this.getDateAdded());
        values.put(TableColumns.EndDate, this.getEndDate());
        values.put(TableColumns.TotalSms, this.getTotalSms());
        values.put(TableColumns.UsedSms, this.getUsedSms());
        values.put(TableColumns.ServerAccountId, this.getServerAccountId());
        values.put(TableColumns.Validated, this.getValidated());
        values.put(TableColumns.IsDeleted,this.getIsDeleted());
        values.put(TableColumns.Dirty,this.getDirty());
        values.put(TableColumns.DateModified, this.getDateModified());
        return values;
    }
}
