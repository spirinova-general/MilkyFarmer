package com.milky.service.core;

import android.database.Cursor;

import com.milky.service.databaseutils.TableColumns;

import java.math.BigDecimal;

/**
 * Created by Neha on 11/30/2015.
 */
public class Bill {
    //    private String id;
//    private String accountId;
    private int customerId;
    private String startDate;
    private String endDate;
    private double quantity;
    private double balance;
    private double adjustment;
    private double tax;
    private double rate;
    private int isCleared;
    private double paymentMade;
    private String dateAdded;
    private String dateModified;
    private double totalAmount;
    private int isOutstanding;
    //    private String firstname;
//    private String lastName;
//    private String message;
//    private String billMade;
    private String rollDate;
    //    private String deletedOn;
    private int dirty;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getDirty() {
        return dirty;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }

    public String getRollDate() {
        return rollDate;
    }

    public void setRollDate(String rollDate) {
        this.rollDate = rollDate;
    }
    public int getIsOutstanding() {
        return isOutstanding;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int isOutstanding() {
        return isOutstanding;
    }

    public void setIsOutstanding(int isOutstanding) {
        this.isOutstanding = isOutstanding;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(double adjustment) {
        this.adjustment = adjustment;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public int getIsCleared() {
        return isCleared;
    }

    public void setIsCleared(int isCleared) {
        this.isCleared = isCleared;
    }

    public double getPaymentMade() {
        return paymentMade;
    }

    public void setPaymentMade(double paymentMade) {
        this.paymentMade = paymentMade;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }


    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public void PopulateFromCursor(Cursor cursor)
    {
        this.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
        this.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
        this.setQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.TotalQuantity)));
        this.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
        this.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.Adjustment)));
        this.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
        this.setIsCleared(cursor.getInt(cursor.getColumnIndex(TableColumns.IsCleared)));
        this.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
        this.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
        this.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
        this.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IsOutstanding)));
        this.setRate(cursor.getInt(cursor.getColumnIndex(TableColumns.Rate)));
        this.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(TableColumns.TotalAmount)));
        this.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
    }
}
