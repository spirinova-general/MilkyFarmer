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
        values.put(TableColumns.FARMER_CODE, account.getFarmerCode());
        values.put(TableColumns.DATE_MODIFIED, account.getDateModified());
        values.put(TableColumns.DATE_ADDED, account.getDateAdded());
        values.put(TableColumns.FIRST_NAME, account.getFirstName());
        values.put(TableColumns.LAST_NAME, account.getLastName());
        values.put(TableColumns.MOBILE, account.getMobile());
        values.put(TableColumns.END_DATE, account.getEndDate());
        values.put(TableColumns.TOTAL_SMS, account.getTotalSms());
        values.put(TableColumns.USED_SMS, account.getUsedSms());
        values.put(TableColumns.SERVER_ACCOUNT_ID, account.getServerAccountId());
        values.put(TableColumns.VALIDATED, account.getValidated());
        getDb().insert(TableNames.TABLE_ACCOUNT, null, values);
    }

    @Override
    public void update(Account account) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.END_DATE, account.getEndDate());
        values.put(TableColumns.TOTAL_SMS, account.getTotalSms());
        values.put(TableColumns.SERVER_ACCOUNT_ID, account.getServerAccountId());

        getDb().update(TableNames.TABLE_ACCOUNT, values, TableColumns.SERVER_ACCOUNT_ID + " ='" + account.getServerAccountId() + "'", null);
    }

    @Override
    public void delete(Account account) {

    }

    @Override
    public Account getDetails() {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        com.milky.service.core.Account holder = new com.milky.service.core.Account();
        if (cursor.moveToFirst()) {
            do {

                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.FARMER_CODE)));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        return holder;
    }

    @Override
    public String getRollDate() {
        return null;
    }

    @Override
    public int getLeftSMS() {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        int sms = 0;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                sms = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TableColumns.TOTAL_SMS)))
                        - getUsedSMS();

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return sms;
    }

    @Override
    public int getUsedSMS() {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        int sms = 0;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                sms = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TableColumns.USED_SMS)));

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
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT + " WHERE "
                + TableColumns.END_DATE + " <'" + day + "'";
        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    @Override
    public JSONObject getJsonData() {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        JSONObject jsonObject = new JSONObject();
        if (cursor.moveToFirst()) {
            do {
                try {
                    jsonObject.put("FarmerCode", cursor.getString(cursor.getColumnIndex(TableColumns.FARMER_CODE)));
                    jsonObject.put("FirstName", cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                    jsonObject.put("LastName", cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                    jsonObject.put("Mobile", cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                    jsonObject.put("Validated", cursor.getString(cursor.getColumnIndex(TableColumns.VALIDATED)));
                    jsonObject.put("Dirty", 1);
                    jsonObject.put("DateAdded", cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                    jsonObject.put("DateModified", cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                    jsonObject.put("StartDate", cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                    jsonObject.put("EndDate", cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                    jsonObject.put("UsedSms", cursor.getString(cursor.getColumnIndex(TableColumns.USED_SMS)));
                    jsonObject.put("TotalSms", cursor.getString(cursor.getColumnIndex(TableColumns.TOTAL_SMS)));
                    jsonObject.put("Id", cursor.getString(cursor.getColumnIndex(TableColumns.SERVER_ACCOUNT_ID)));

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
        values.put(TableColumns.USED_SMS, String.valueOf(getUsedSMS() + count));
        getDb().update(TableNames.TABLE_ACCOUNT, values, null, null);
    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }
}
