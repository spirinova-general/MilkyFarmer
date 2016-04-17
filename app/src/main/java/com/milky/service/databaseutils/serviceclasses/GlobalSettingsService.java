package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.GlobalSettings;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceinterface.IGlobalSetting;
import com.milky.ui.main.GlobalSetting;
import com.milky.utils.AppUtil;

import java.util.Calendar;
import java.util.Date;

public class GlobalSettingsService implements IGlobalSetting {

    @Override
    public void insert(GlobalSettings globalSettings) {
        Calendar c = Calendar.getInstance();
        Date dateModified = c.getTime();
        globalSettings.setDateModified(Utils.ToDateString(dateModified));

        Date rollDate = Utils.FromDateString(globalSettings.getRollDate());
        globalSettings.setRollDate(Utils.ToDateString(rollDate, true));
        ContentValues values = globalSettings.ToContentValues();

        getDb().insert(TableNames.GlobalSetting, null, values);
    }

    @Override
    public void update(GlobalSettings globalSettings) {
        Calendar c = Calendar.getInstance();
        Date dateModified = c.getTime();
        globalSettings.setDateModified(Utils.ToDateString(dateModified));

        Date rollDate = Utils.FromDateString(globalSettings.getRollDate());
        globalSettings.setRollDate(Utils.ToDateString(rollDate, true));

        ContentValues values = globalSettings.ToContentValues();
        getDb().update(TableNames.GlobalSetting, values, null, null);
    }

    @Override
    public void updateLastBillSyncedTime(){
        GlobalSettings setting = getData();

        Calendar c = Calendar.getInstance();
        Date now = c.getTime();
        setting.setLastBillSyncedTime(Utils.ToDateString(now));

        ContentValues values = setting.ToContentValues();
        getDb().update(TableNames.GlobalSetting, values, null, null);
    }

    @Override
    public GlobalSettings getData() {
        String selectquery = "SELECT * FROM " + TableNames.GlobalSetting;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        GlobalSettings holder = new GlobalSettings();
        if (cursor.moveToFirst()) {
            do {
               holder.PopulateFromCursor(cursor);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return holder;
    }

    @Override
    public String getRollDate() {
        return getData().getRollDate();
    }

    @Override
    public void calculateAndSetNextRollDate() {
        try {
            //Calculate  next roll date and update it in global settings
            //Next roll date - the last day of the month for (date + 1 day)
            Date newRollDate = null;
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DATE, 1);
            newRollDate = c.getTime();

            c.setTime(newRollDate);
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            newRollDate = c.getTime();

            GlobalSettings settings = getData();
            settings.setRollDate(Utils.ToDateString(newRollDate, true));
            update(settings);
        }
        catch(Exception ex)
        {
            //TBD...should not eat exceptions like these...will fix later
        }

    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

    /*public void updateRollDate(String date) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.RollDate,date);
        getDb().update(TableNames.GlobalSetting, values,null, null);
    }*/
}
