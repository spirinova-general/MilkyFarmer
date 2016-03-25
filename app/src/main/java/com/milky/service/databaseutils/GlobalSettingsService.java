package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.viewmodel.VAccount;
import com.milky.viewmodel.VGlobalSettings;

/**
 * Created by Lead1 on 3/22/2016.
 */
public class GlobalSettingsService {
    //    Insert GlobalSettings data
    public static void insertGlobalSettingsData(SQLiteDatabase db, VGlobalSettings holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, holder.getDefaultRate());
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.ROLL_DATE, holder.getRollDate());
        db.insert(TableNames.TABLE_GLOBAL_SETTINGS, null, values);
    }

    // get Settings data
    public static VGlobalSettings getGlobalSettingsData(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        Cursor cursor = db.rawQuery(selectquery, null);
        VGlobalSettings holder = new VGlobalSettings();
        if (cursor.moveToFirst()) {
            do {
                holder.setDefaultRate(cursor.getInt(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.ROLL_DATE)));
                holder.setTax(cursor.getInt(cursor.getColumnIndex(TableColumns.TAX)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return holder;
    }
    //get account expirationDate

    public static String getExpirationDate(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        String date = "";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                date = cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE));
            }
            while (cursor.moveToNext());

        }

        return date;
    }

    //Update Settings
    public static void updateSettings(SQLiteDatabase db, VGlobalSettings holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ROLL_DATE, holder.getRollDate());
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.DEFAULT_RATE, holder.getDefaultRate());
        db.update(TableNames.TABLE_GLOBAL_SETTINGS, values, null, null);
    }

    //    Check if user set any default rate yet..
    public static String getDefaultRate(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        String rate = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                rate = cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return rate;
    }

    //Get default tax
    public static String getDefautTax(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        String rate = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                rate = cursor.getString(cursor.getColumnIndex(TableColumns.TAX));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return rate;
    }

    //Get Roll date
    public static String getRollDate(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        String rate = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                rate = cursor.getString(cursor.getColumnIndex(TableColumns.ROLL_DATE));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return rate;
    }

}
