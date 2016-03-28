package com.milky.service.databaseutils;

/**
 * Created by Neha on 11/30/2015.
 */
public class TableColumsDetail {
    //ACCOUNT
    public static final String ACCOUNT = "CREATE TABLE " + TableNames.TABLE_ACCOUNT + "("
            + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.FARMER_CODE + " TEXT,"
            + TableColumns.DATE_MODIFIED + " DATETIME,"+ TableColumns.DATE_ADDED + " DATETIME," + TableColumns.FIRST_NAME + " TEXT,"
            + TableColumns.LAST_NAME + " TEXT," + TableColumns.MOBILE + " TEXT,"
            + TableColumns.END_DATE + " DATETIME," + TableColumns.TOTAL_SMS + " INTEGER," + TableColumns.USED_SMS + " INTEGER,"
            + TableColumns.VALIDATED + " INTEGER," + TableColumns.SERVER_ACCOUNT_ID + " TEXT" + ")";

    //Global Settings
    public static final String GLOBAL_ETTINGS = "CREATE TABLE " + TableNames.TABLE_GLOBAL_SETTINGS + "("
            + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.DEFAULT_RATE + " REAL,"
            + TableColumns.TAX + " REAL," + TableColumns.ROLL_DATE + " DATETIME" + ")";

    //AREA
    public static final String AREA = "CREATE TABLE " + TableNames.TABLE_AREA + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + TableColumns.LOCALITY + " TEXT," + TableColumns.AREA_NAME + " TEXT," + TableColumns.CITY_NAME + " TEXT," + TableColumns.DIRTY + " TEXT" + ")";

    //CUSTOMER
    public static final String CUSTOMER = "CREATE TABLE " + TableNames.TABLE_CUSTOMER + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.FIRST_NAME + " TEXT,"
            + TableColumns.LAST_NAME + " TEXT," + TableColumns.MOBILE + " TEXT," + TableColumns.ADDRESS_1 + " TEXT," + TableColumns.ADDRESS_2 + " TEXT," + TableColumns.BALANCE + " REAL,"
            + TableColumns.DATE_ADDED + " DATETIME," + TableColumns.AREA_ID + " INTEGER,"+ TableColumns.DELETED_ON + " INTEGER," + TableColumns.DATE_MODIFIED + " DATETIME,"
            + TableColumns.ISDELETED + " INTEGER," + TableColumns.DIRTY + " INTEGER" + " ,FOREIGN KEY (" + TableColumns.AREA_ID + ") REFERENCES " + TableNames.TABLE_AREA + "(" + TableColumns.ID + ")" +
            ")";

    //CUSTOMER"S BILL
    public static final String CUSTOMERS_BILL = "CREATE TABLE " + TableNames.TABLE_CUSTOMER_BILL + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.START_DATE + " DATETIME," + TableColumns.END_DATE + " DATETIME,"
            + TableColumns.DEFAULT_QUANTITY + " REAL," + TableColumns.BALANCE + " REAL," + TableColumns.ADJUSTMENTS + " REAL," + TableColumns.TAX + " REAL," + TableColumns.IS_CLEARED + " INTEGER,"
            + TableColumns.PAYMENT_MADE + " REAL," + TableColumns.IS_OUTSTANDING + " INTEGER," + TableColumns.TOTAL_AMOUNT + " REAL," + TableColumns.BALANCE_TYPE
            + " INTEGER," + TableColumns.ROLL_DATE + " DATETIME," + TableColumns.DATE_ADDED + " DATETIME,"+TableColumns.DATE_MODIFIED + " DATETIME," + TableColumns.DIRTY + " INTEGER," + TableColumns.CUSTOMER_ID + " INTEGER,"
            + " FOREIGN KEY (" + TableColumns.CUSTOMER_ID + ")" + " REFERENCES " + TableNames.TABLE_CUSTOMER + "(" + TableColumns.ID + ")" + ")";

    //Customers Setting
    public static final String CUSTOMER_SETTINGS = "CREATE TABLE " + TableNames.TABLE_CUSTOMER_SETTINGS + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            TableColumns.DEFAULT_RATE + " REAL," + TableColumns.DEFAULT_QUANTITY + " TEXT," + TableColumns.BALANCE_TYPE + " TEXT,"
            + TableColumns.BALANCE + " REAL," + TableColumns.START_DATE + " DATETIME," + TableColumns.END_DATE + " DATETIME,"
            + TableColumns.DIRTY + " INTEGER," + TableColumns.CUSTOMER_ID + " INTEGER,"
            + " FOREIGN KEY (" + TableColumns.CUSTOMER_ID + ")" + " REFERENCES " + TableNames.TABLE_CUSTOMER + "(" + TableColumns.ID + ")" + ")";

    //DELIVERY
    public static final String DELIVERY = "CREATE TABLE " + TableNames.TABLE_DELIVERY + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.DATE_MODIFIED + " DATETIME," + TableColumns.DEFAULT_QUANTITY + " REAL,"
            + TableColumns.DELEVERY_DATE + " DATETIME," + TableColumns.DIRTY + " TEXT," + TableColumns.CUSTOMER_ID + " INTEGER,"
            + " FOREIGN KEY (" + TableColumns.CUSTOMER_ID + ")" + " REFERENCES " + TableNames.TABLE_CUSTOMER + "(" + TableColumns.ID + ")" +
            ")";
}
