package com.tyczj.extendedcalendarview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Created by Lead1 on 2/16/2016.
 */
public class ExtcalDatabaseHelper extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase db;

//    public ExtcalDatabaseHelper(final Context context) {
//        super(context, Environment.getExternalStorageDirectory()
//                + File.separator + "milky"
//                + File.separator + "Extcal.db", null, 1);
//    }

    public ExtcalDatabaseHelper(Context context) {
        super(context, "Extcal", null,
                2);
        this.context = context;

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String cs = "CREATE TABLE " + "customers" + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.ACCOUNT_ID + " TEXT,"
                + TableColumns.CUSTOMER_ID + " TEXT," + TableColumns.DEFAULT_RATE + " TEXT,"
                + TableColumns.DEFAULT_QUANTITY + " TEXT," + TableColumns.BALANCE_TYPE
                + " TEXT,"
                + TableColumns.BALANCE + " TEXT," + TableColumns.FIRST_NAME + " TEXT," + TableColumns.LAST_NAME + " TEXT,"
                + TableColumns.START_DATE + " DATETIME," + TableColumns.AREA_ID + " TEXT,"
                + TableColumns.END_DATE + " DATETIME," + TableColumns.DIRTY + " TEXT," + TableColumns.ADJUSTMENTS + " TEXT," + TableColumns.DATE_MODIFIED + " DATETIME," + TableColumns.DELETED_ON + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" + ")";
        db.execSQL(cs);
        //DELIVERY
        String DELIVERY = "CREATE TABLE " + "delivery" + "(" + TableColumns.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + TableColumns.DATE_MODIFIED + " DATETIME," + TableColumns.ACCOUNT_ID + " TEXT," + TableColumns.QUANTITY + " TEXT,"
                + TableColumns.CUSTOMER_ID + " TEXT," + TableColumns.START_DATE + " DATETIME," + TableColumns.DELETED_ON + " TEXT," + TableColumns.DIRTY + " TEXT," + TableColumns.SYNC_STATUS + " TEXT" +
                ")";
        db.execSQL(DELIVERY);
        String custIndex = "CREATE UNIQUE INDEX " + TableColumns.CUST_INDEX + " ON " + "customers" + " (" +TableColumns.CUSTOMER_ID+", "+TableColumns.ID+ " )";
        db.execSQL(custIndex);
        //        Adding custId Index..
        String deliveryIndex = "CREATE UNIQUE INDEX " + TableColumns.DELV_INDEX + " ON " + "delivery" + " (" +TableColumns.CUSTOMER_ID+", "+TableColumns.ID+ " )";
        db.execSQL(deliveryIndex);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String custIndex = "CREATE UNIQUE INDEX " + TableColumns.CUST_INDEX + " ON " + "customers" + " (" +TableColumns.CUSTOMER_ID+", "+TableColumns.ID+ " )";
        db.execSQL(custIndex);
        //        Adding custId Index..
        String deliveryIndex = "CREATE UNIQUE INDEX " + TableColumns.DELV_INDEX + " ON " + "delivery" + " (" +TableColumns.CUSTOMER_ID+", "+TableColumns.ID+ " )";
        db.execSQL(deliveryIndex);
    }
    public boolean isTableNotEmpty(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + table;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }
}
