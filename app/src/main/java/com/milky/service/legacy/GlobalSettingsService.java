package com.milky.service.legacy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.GlobalSettings;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;

/**
 * Created by Lead1 on 3/22/2016.
 */
public class GlobalSettingsService {
    //    Insert GlobalSettings data
    public static void insertGlobalSettingsData(SQLiteDatabase db, GlobalSettings holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, holder.getDefaultRate());
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.ROLL_DATE, holder.getRollDate());
        db.insert(TableNames.TABLE_GLOBAL_SETTINGS, null, values);
    }

    // get Settings data
    public static GlobalSettings getGlobalSettingsData(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        Cursor cursor = db.rawQuery(selectquery, null);
        GlobalSettings holder = new GlobalSettings();
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
    public static void updateSettings(SQLiteDatabase db, GlobalSettings holder) {
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
    public static double getDefautTax(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        double rate = 0;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                rate = cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX));

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
