package com.milky.service.databaseutils;

/**
 * Created by Neha on 11/30/2015.
 */
public class TableColumsDetail {
    //ACCOUNT
    public static final String ACCOUNT = "CREATE TABLE " + TableNames.ACCOUNT + "("
            + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.FarmerCode + " TEXT,"
            + TableColumns.DateModified + " DATETIME,"+ TableColumns.DateAdded + " DATETIME," + TableColumns.FirstName + " TEXT,"
            + TableColumns.LastName + " TEXT," + TableColumns.Mobile + " TEXT,"
            + TableColumns.EndDate + " DATETIME," + TableColumns.TotalSms + " INTEGER," + TableColumns.UsedSms + " INTEGER,"
            + TableColumns.Validated + " INTEGER," + TableColumns.ServerAccountId + " TEXT" + ")";

    //Global Settings
    public static final String GLOBAL_ETTINGS = "CREATE TABLE " + TableNames.GlobalSetting + "("
            + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.DefaultRate + " REAL,"
            + TableColumns.TAX + " REAL," + TableColumns.RollDate + " DATETIME" + ")";

    //AREA
    public static final String AREA = "CREATE TABLE " + TableNames.AREA + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + TableColumns.Locality + " TEXT,"+TableColumns.Name+" TEXT," + TableColumns.City + " TEXT," +  TableColumns.Dirty + " TEXT" + ")";

    //CUSTOMER
    public static final String CUSTOMER = "CREATE TABLE " + TableNames.CUSTOMER + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.FirstName + " TEXT,"
            + TableColumns.LastName + " TEXT," + TableColumns.Mobile + " TEXT," + TableColumns.Address1 + " TEXT," + TableColumns.Address2 + " TEXT," + TableColumns.Balance + " REAL,"
            + TableColumns.DateAdded + " DATETIME," + TableColumns.AreaId + " INTEGER,"+ TableColumns.DeletedOn + " INTEGER," + TableColumns.DateModified + " DATETIME,"
            + TableColumns.IsDeleted + " INTEGER," + TableColumns.Dirty + " INTEGER" + " ,FOREIGN KEY (" + TableColumns.AreaId + ") REFERENCES " + TableNames.AREA + "(" + TableColumns.ID + ")" +
            ")";

    //CUSTOMER"S BILL
    public static final String CUSTOMERS_BILL = "CREATE TABLE " + TableNames.Bill + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.StartDate + " DATETIME," + TableColumns.EndDate + " DATETIME,"
            + TableColumns.TotalQuantity + " REAL," +  TableColumns.Rate + " REAL," + TableColumns.Balance + " REAL," + TableColumns.Adjustment + " REAL," + TableColumns.TAX + " REAL," + TableColumns.IsCleared + " INTEGER,"
            + TableColumns.PaymentMade + " REAL," + TableColumns.IsOutstanding + " INTEGER," + TableColumns.TotalAmount + " REAL," + TableColumns.RollDate + " DATETIME," + TableColumns.DateAdded + " DATETIME,"+TableColumns.DateModified + " DATETIME," + TableColumns.Dirty + " INTEGER," + TableColumns.CustomerId + " INTEGER,"
            + " FOREIGN KEY (" + TableColumns.CustomerId + ")" + " REFERENCES " + TableNames.CUSTOMER + "(" + TableColumns.ID + ")" + ")";

    //Customers Setting
    public static final String CUSTOMER_SETTINGS = "CREATE TABLE " + TableNames.CustomerSetting + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            TableColumns.DefaultRate + " REAL," + TableColumns.DefaultQuantity + " TEXT,"
            + TableColumns.Balance + " REAL," + TableColumns.StartDate + " DATETIME," + TableColumns.EndDate + " DATETIME,"
            + TableColumns.IsCustomDelivery + " INTEGER," + TableColumns.Dirty + " INTEGER," + TableColumns.CustomerId + " INTEGER,"
            + " FOREIGN KEY (" + TableColumns.CustomerId + ")" + " REFERENCES " + TableNames.CUSTOMER + "(" + TableColumns.ID + ")" + ")";

    //DELIVERY
    public static final String DELIVERY = "CREATE TABLE " + TableNames.DELIVERY + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.DateModified + " DATETIME," + TableColumns.DefaultQuantity + " REAL,"
            + TableColumns.DeliveryDate + " DATETIME," + TableColumns.Dirty + " TEXT," + TableColumns.CustomerId + " INTEGER,"
            + " FOREIGN KEY (" + TableColumns.CustomerId + ")" + " REFERENCES " + TableNames.CUSTOMER + "(" + TableColumns.ID + ")" +
            ")";
}
