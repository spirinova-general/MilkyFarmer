package com.milky.service.core;

import android.database.Cursor;

import com.milky.service.databaseutils.TableColumns;

import java.math.BigDecimal;

public class Bill {
    private double paymentMade;
    private int dirty;
    private double totalAmount;
    private String endDate;
    private double adjustment;
    private double balance;
    private double tax;
    private int isCleared;
    private String dateModified;
    private int customerId;
    private String startDate;
    private double quantity;
    private String dateAdded;
    private int isOutstanding;
    private double rate;
    private int isDeleted;

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getPaymentMade() {
        return paymentMade;
    }

    public void setPaymentMade(double paymentMade) {
        this.paymentMade = paymentMade;
    }

    public int getDirty() {
        return dirty;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public double getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(double adjustment) {
        this.adjustment = adjustment;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
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

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getIsOutstanding() {
        return isOutstanding;
    }

    public void setIsOutstanding(int isOutstanding) {
        this.isOutstanding = isOutstanding;
    }

    public void PopulateFromCursor(Cursor cursor)
    {
        this.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
        this.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
        this.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
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
    }
}
