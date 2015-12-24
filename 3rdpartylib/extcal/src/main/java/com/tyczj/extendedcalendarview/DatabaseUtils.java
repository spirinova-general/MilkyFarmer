package com.tyczj.extendedcalendarview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Neha on 12/14/2015.
 */
public class DatabaseUtils extends SQLiteOpenHelper {
    private static String TABLE_DELIVERY = "delivery";
    private static String QUANTITY = "quantity";
    private static String CUSTOMER_ID = "quantity";
    private static String START_DATE = "quantity";
    private static String END_DATE = "quantity";


    Context context;
    SQLiteDatabase db;

    public DatabaseUtils(Context context) {
        super(context, "milky_delivery", null,
                1);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_DELIVERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", "delivery_table"));
        onCreate(db);
    }

    public void insertCustomerDetail(DateQuantityModel holder) {
        ContentValues values = new ContentValues();
        values.put(QUANTITY, holder.getQuantity());
        values.put(CUSTOMER_ID, holder.getCustomerId());
        values.put(START_DATE, holder.getStartDate());
        values.put(END_DATE, holder.getEndDate());
        db.insert(TABLE_DELIVERY, null, values);
    }

    public static boolean isHasDataForCustomer(SQLiteDatabase db, String custId, String calDate) {
        String selectQuery = "SELECT * FROM " + TABLE_DELIVERY + " WHERE " + START_DATE + " >='"
                + calDate + "'" + " AND " + calDate + " <'" + END_DATE + "' AND "
                + CUSTOMER_ID + " ='" + custId + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public boolean isHasData(String calDate) {
        String selectQuery = "SELECT * FROM " + TABLE_DELIVERY + " WHERE " + START_DATE + " >='"
                + calDate + "'" + " AND " + calDate + " <'" + END_DATE + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public int getAllCustomerData(String calDate) {
        String selectquery = "SELECT * FROM " + TABLE_DELIVERY + " WHERE " + START_DATE + " >='"
                + calDate + "'" + " AND " + calDate + " <'" + END_DATE + "'";
//        ArrayList<DateQuantityModel> quantityList = new ArrayList<>();
        int quantity = 0;
        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
//                DateQuantityModel holder = new DateQuantityModel();
//                holder.setCustomerId(cursor.getString(cursor.getColumnIndex(CUSTOMER_ID)));
//                holder.setQuantity(cursor.getString(cursor.getColumnIndex(QUANTITY)));
//                holder.setStartDate(cursor.getString(cursor.getColumnIndex(START_DATE)));
//                holder.setEndDate(cursor.getString(cursor.getColumnIndex(END_DATE)));
//                quantityList.add(holder);
                quantity = Integer.parseInt(cursor.getString(cursor.getColumnIndex(QUANTITY))) + quantity;
            }
            while (cursor.moveToNext());


        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return quantity;
    }

    public void updateCustomerDetail(DateQuantityModel holder, String date) {
        ContentValues values = new ContentValues();
        values.put(QUANTITY, holder.getQuantity());
        values.put(START_DATE, holder.getStartDate());
        values.put(END_DATE, holder.getEndDate());

        db.update(TABLE_DELIVERY, values, END_DATE + " ='" + date + "'"
                + " AND " + CUSTOMER_ID +
                " ='" + holder.getCustomerId() + "'", null);
    }
}
