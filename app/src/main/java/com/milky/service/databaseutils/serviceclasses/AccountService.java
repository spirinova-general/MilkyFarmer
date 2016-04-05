package com.milky.service.databaseutils.serviceclasses;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceinterface.IAccountService;
import com.milky.service.core.Account;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class AccountService implements IAccountService {
    @Override
    public void insert(Account account) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FarmerCode, account.getFarmerCode());
        values.put(TableColumns.DateModified, account.getDateModified());
        values.put(TableColumns.DateAdded, account.getDateAdded());
        values.put(TableColumns.FirstName, account.getFirstName());
        values.put(TableColumns.LastName, account.getLastName());
        values.put(TableColumns.Mobile, account.getMobile());
        values.put(TableColumns.EndDate, account.getEndDate());
        values.put(TableColumns.TotalSms, account.getTotalSms());
        values.put(TableColumns.UsedSms, account.getUsedSms());
        values.put(TableColumns.ServerAccountId, account.getServerAccountId());
        values.put(TableColumns.Validated, account.getValidated());
        getDb().insert(TableNames.ACCOUNT, null, values);
    }

    @Override
    public void update(Account account) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.EndDate, account.getEndDate());
        values.put(TableColumns.TotalSms, account.getTotalSms());
        values.put(TableColumns.ServerAccountId, account.getServerAccountId());

        getDb().update(TableNames.ACCOUNT, values, TableColumns.ServerAccountId + " ='" + account.getServerAccountId() + "'", null);
    }

    @Override
    public void delete(Account account) {

    }

    @Override
    public Account getDetails() {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        com.milky.service.core.Account holder = new com.milky.service.core.Account();
        if (cursor.moveToFirst()) {
            do {

                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.FarmerCode)));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        return holder;
    }
    @Override
    public int getLeftSMS() {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        int sms = 0;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                sms = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TableColumns.TotalSms)))
                        - getUsedSMS();

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return sms;
    }

    @Override
    public int getUsedSMS() {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        int sms = 0;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                sms = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TableColumns.UsedSms)));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return sms;
    }

    @Override
    public boolean isAccountExpired() {
        Calendar cal = Calendar.getInstance();
        String day = Constants.api_format.format(cal.getTime());
        String selectQuery = "SELECT * FROM " + TableNames.ACCOUNT + " WHERE "
                + TableColumns.EndDate + " <'" + day + "'";
        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    @Override
    public JSONObject getJsonData() {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        JSONObject jsonObject = new JSONObject();
        if (cursor.moveToFirst()) {
            do {
                try {
                    jsonObject.put("FarmerCode", cursor.getString(cursor.getColumnIndex(TableColumns.FarmerCode)));
                    jsonObject.put("FirstName", cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
                    jsonObject.put("LastName", cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));
                    jsonObject.put("Mobile", cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
                    jsonObject.put("Validated", cursor.getString(cursor.getColumnIndex(TableColumns.Validated)));
                    jsonObject.put("Dirty", 1);
                    jsonObject.put("DateAdded", cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                    jsonObject.put("DateModified", cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
                    jsonObject.put("StartDate", cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                    jsonObject.put("EndDate", cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
                    jsonObject.put("UsedSms", cursor.getString(cursor.getColumnIndex(TableColumns.UsedSms)));
                    jsonObject.put("TotalSms", cursor.getString(cursor.getColumnIndex(TableColumns.TotalSms)));
                    jsonObject.put("Id", cursor.getString(cursor.getColumnIndex(TableColumns.ServerAccountId)));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return jsonObject;
    }

    @Override
    public void updateSMSCount(int count) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.UsedSms, String.valueOf(getUsedSMS() + count));
        getDb().update(TableNames.ACCOUNT, values, null, null);
    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

}
