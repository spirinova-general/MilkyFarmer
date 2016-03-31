package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.GlobalSettings;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceinterface.IGlobalSetting;
import com.milky.utils.AppUtil;

public class GlobalSettingsService implements IGlobalSetting {

    @Override
    public void insert(GlobalSettings globalSettings) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, globalSettings.getDefaultRate());
        values.put(TableColumns.TAX, globalSettings.getTax());
        values.put(TableColumns.ROLL_DATE, globalSettings.getRollDate());
        getDb().insert(TableNames.TABLE_GLOBAL_SETTINGS, null, values);
    }

    @Override
    public void update(GlobalSettings globalSettings) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ROLL_DATE, globalSettings.getRollDate());
        values.put(TableColumns.TAX, globalSettings.getTax());
        values.put(TableColumns.DEFAULT_RATE, globalSettings.getDefaultRate());
        getDb().update(TableNames.TABLE_GLOBAL_SETTINGS, values, null, null);
    }

    @Override
    public GlobalSettings getData() {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_GLOBAL_SETTINGS;
        Cursor cursor = getDb().rawQuery(selectquery, null);
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

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }
}
