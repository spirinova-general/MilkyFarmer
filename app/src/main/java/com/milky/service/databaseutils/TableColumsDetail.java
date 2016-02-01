package com.milky.service.databaseutils;

/**
 * Created by Neha on 11/30/2015.
 */
public class TableColumsDetail {

    //ACCOUNT
    public static final String ACCOUNT = "CREATE TABLE " + TableNames.TABLE_ACCOUNT + "("
            + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.FARMER_CODE + " TEXT,"
            + TableColumns.DATE_MODIFIED + " DATETIME," + TableColumns.DEFAULT_RATE + " TEXT," + TableColumns.TAX + " TEXT,"
            + TableColumns.DATE_ADDED + " DATETIME," + TableColumns.DIRTY + " TEXT," + TableColumns.FIRST_NAME + " TEXT,"
            + TableColumns.LAST_NAME + " TEXT," + TableColumns.MOBILE + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" +
            ")";


    //AREA
    public static final String AREA = "CREATE TABLE " + TableNames.TABLE_AREA + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + TableColumns.LOCALITY + " TEXT," + TableColumns.AREA_NAME + " TEXT," + TableColumns.CITY_NAME + " TEXT," + TableColumns.DIRTY + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" + ")";


    //BILL
    public static final String BILL = "CREATE TABLE " + TableNames.TABLE_BILL + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.ACCOUNT_ID + " TEXT,"
            + TableColumns.CUSTOMER_ID + " TEXT," + TableColumns.START_DATE + " DATETIME," + TableColumns.END_DATE + " DATETIME," + TableColumns.QUANTITY + " TEXT," + TableColumns.BALANCE + " TEXT,"
            + TableColumns.ADJUSTMENTS + " TEXT," + TableColumns.TAX + " TEXT," + TableColumns.IS_CLEARED + " TEXT,"
            + TableColumns.PAYMENT_MADE + " TEXT,"
            + TableColumns.DATE_ADDED + " DATETIME," + TableColumns.TOTAL_AMOUNT + " TEXT,"
            + TableColumns.DATE_MODIFIED + " DATETIME," + TableColumns.DIRTY + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" + ")";
    //CITY
    public static final String CITY = "CREATE TABLE " + TableNames.TABLE_CITY + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.AREA_ID + " TEXT," + TableColumns.ACCOUNT_ID + " TEXT," + TableColumns.CITY_ID + " TEXT,"
            + TableColumns.CITY_NAME + " TEXT," + TableColumns.DIRTY + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" + ")";

    //CUSTOMER
    public static final String CUSTOMER = "CREATE TABLE " + TableNames.TABLE_CUSTOMER + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.FIRST_NAME + " TEXT,"
            + TableColumns.LAST_NAME + " TEXT," + TableColumns.MOBILE + " TEXT," + TableColumns.ADDRESS_1 + " TEXT," + TableColumns.ADDRESS_2 + " TEXT," + TableColumns.BALANCE + " TEXT,"
            + TableColumns.DATE_ADDED + " DATETIME," + TableColumns.AREA_ID + " TEXT," + TableColumns.CITY_ID + " TEXT," + TableColumns.BALANCE_TYPE
            + " TEXT,"
            + TableColumns.DATE_MODIFIED + " DATETIME," + TableColumns.ACCOUNT_ID + " TEXT," + TableColumns.QUANTITY + " TEXT," + TableColumns.DEFAULT_RATE + " TEXT,"
            + TableColumns.CUSTOMER_ID + " TEXT," + TableColumns.START_DATE + " DATETIME," + TableColumns.DATE_QUANTITY_MODIFIED + " DATETIME," + TableColumns.DELETED_ON + " TEXT," + TableColumns.DIRTY + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" +
            ")";

    //DELIVERY
    public static final String DELIVERY = "CREATE TABLE " + TableNames.TABLE_DELIVERY + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.DATE_MODIFIED + " DATETIME," + TableColumns.ACCOUNT_ID + " TEXT," + TableColumns.QUANTITY + " TEXT,"
            + TableColumns.CUSTOMER_ID + " TEXT," + TableColumns.START_DATE + " DATETIME," + TableColumns.DELETED_ON + " TEXT," + TableColumns.DIRTY + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" +
            ")";

    //CUSTOMER SETTINGS
    public static final String CUSTOMER_SETTINGS = "CREATE TABLE " + TableNames.TABLE_CUSTOMER_SETTINGS + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.ACCOUNT_ID + " TEXT,"
            + TableColumns.CUSTOMER_ID + " TEXT," + TableColumns.DEFAULT_RATE + " TEXT,"
            + TableColumns.DEFAULT_QUANTITY + " TEXT," + TableColumns.BALANCE_TYPE
            + " TEXT,"
            + TableColumns.BALANCE + " TEXT," + TableColumns.FIRST_NAME + " TEXT," + TableColumns.LAST_NAME + " TEXT,"
            + TableColumns.START_DATE + " DATETIME," + TableColumns.AREA_ID + " TEXT,"
            + TableColumns.END_DATE + " DATETIME," + TableColumns.DIRTY + " TEXT," + TableColumns.ADJUSTMENTS + " TEXT," + TableColumns.DATE_MODIFIED + " DATETIME," + TableColumns.DELETED_ON + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" + ")";


    //AREA_ACCOUNT_MAPPING
    public static final String AREA_ACCOUNT_MAPPING = "CREATE TABLE " + TableNames.TABLE_ACCOUNT_AREA_MAPPING + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.AREA_ID + " TEXT,"
            + TableColumns.ACCOUNT_ID + " TEXT," + TableColumns.DIRTY + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" + ")";

    //CUSTOMER"S BILL
    public static final String CUSTOMERS_BILL = "CREATE TABLE " + TableNames.TABLE_CUSTOMER_BILL + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.ACCOUNT_ID + " TEXT,"
            + TableColumns.CUSTOMER_ID + " TEXT," + TableColumns.START_DATE + " DATETIME," + TableColumns.END_DATE + " DATETIME," + TableColumns.QUANTITY + " TEXT," + TableColumns.BALANCE + " TEXT,"
            + TableColumns.ADJUSTMENTS + " TEXT," + TableColumns.TAX + " TEXT," + TableColumns.IS_CLEARED + " TEXT,"
            + TableColumns.PAYMENT_MADE + " TEXT," + TableColumns.IS_OUTSTANDING + " TEXT," + TableColumns.BILL_MADE + " TEXT," + TableColumns.BALANCE_TYPE
            + " TEXT,"
            + TableColumns.DATE_ADDED + " DATETIME," + TableColumns.DEFAULT_RATE + " TEXT,"
            + TableColumns.DATE_MODIFIED + " DATETIME," + TableColumns.DIRTY + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" + ")";


}
