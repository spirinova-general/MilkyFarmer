package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.GlobalSettings;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceinterface.IGlobalSetting;
import com.milky.utils.AppUtil;

import java.util.Calendar;
import java.util.Date;

public class GlobalSettingsService implements IGlobalSetting {

    @Override
    public void insert(GlobalSettings globalSettings) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DefaultRate, globalSettings.getDefaultRate());
        values.put(TableColumns.TAX, globalSettings.getTax());
        values.put(TableColumns.RollDate, globalSettings.getRollDate());
        values.put(TableColumns.IsDeleted,globalSettings.getIsDeleted());
        values.put(TableColumns.Dirty,globalSettings.getDirty());
        values.put(TableColumns.DateModified,globalSettings.getDateModified());
        getDb().insert(TableNames.GlobalSetting, null, values);
    }

    @Override
    public void update(GlobalSettings globalSettings) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.RollDate, globalSettings.getRollDate());
        values.put(TableColumns.TAX, globalSettings.getTax());
        values.put(TableColumns.DefaultRate, globalSettings.getDefaultRate());
        values.put(TableColumns.IsDeleted,globalSettings.getIsDeleted());
        values.put(TableColumns.Dirty,globalSettings.getDirty());
        values.put(TableColumns.DateModified,globalSettings.getDateModified());
        getDb().update(TableNames.GlobalSetting, values, null, null);
    }

    @Override
    public GlobalSettings getData() {
        String selectquery = "SELECT * FROM " + TableNames.GlobalSetting;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        GlobalSettings holder = new GlobalSettings();
        if (cursor.moveToFirst()) {
            do {
                holder.setDefaultRate(cursor.getInt(cursor.getColumnIndex(TableColumns.DefaultRate)));
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
                holder.setTax(cursor.getInt(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsDeleted(cursor.getInt(cursor.getColumnIndex(TableColumns.IsDeleted)));
                holder.setDirty(cursor.getInt(cursor.getColumnIndex(TableColumns.Dirty)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return holder;
    }

    @Override
    public String getRollDate() {
        String selectquery = "SELECT * FROM " + TableNames.GlobalSetting;
        String rate = null;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                rate = cursor.getString(cursor.getColumnIndex(TableColumns.RollDate));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return rate;
    }

    @Override
    public void setNextRollDate() {
        try {
            //Calculate  next roll date and update it in global settings
            //Next roll date - the last day of the month for (date + 1 day)
            Date rollDate = Utils.FromDateString(getRollDate());

            Date newRollDate = null;
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            newRollDate = c.getTime();


            c.setTime(newRollDate);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            newRollDate = c.getTime();
        }
        catch(Exception ex)
        {
            //TBD...should not eat exceptions like these...will fix later
        }

    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }
    public void updateRollDate(String date) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.RollDate,date);
        getDb().update(TableNames.GlobalSetting, values,null, null);
    }
}
