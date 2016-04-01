package com.milky.service.legacy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Neha on 12/26/2015.
 */
public class Account {


    public void updateAllAccountDetails(SQLiteDatabase db, com.milky.service.core.Account holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.END_DATE, holder.getEndDate());
        values.put(TableColumns.TOTAL_SMS, holder.getTotalSms());
        values.put(TableColumns.SERVER_ACCOUNT_ID, holder.getServerAccountId());

        db.update(TableNames.TABLE_ACCOUNT, values, TableColumns.SERVER_ACCOUNT_ID + " ='" + holder.getServerAccountId() + "'", null);
    }

    public void updateSMSCount(SQLiteDatabase db, int count) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.USED_SMS, String.valueOf(getUsedSMS(db) + count));
//        values.put(TableColumns.TOTAL_SMS, String.valueOf(getLeftsmsCount(db) - count));
        db.update(TableNames.TABLE_ACCOUNT, values, null, null);
    }

    public boolean columnRollDateExists(SQLiteDatabase db) {

        String selectQuery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getColumnIndex(TableColumns.ROLL_DATE) > 0;

        cursor.close();
        return result;
    }



    public int getLeftsmsCount(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        int sms = 0;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TOTAL_SMS)) != null)
                    sms = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TableColumns.TOTAL_SMS)))
                            - getUsedSMS(db);

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return sms;
    }

    public int getUsedSMS(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        int sms = 0;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TOTAL_SMS)) != null)
                    sms = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TableColumns.USED_SMS)));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return sms;
    }

    public String getExpirationDate(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
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

    public void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");

        db.update(TableNames.TABLE_ACCOUNT, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
                + " AND " + TableColumns.DIRTY + " ='" + "0" + "'", null);
    }

    public void updateRollDate(SQLiteDatabase db, String date) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ROLL_DATE, date);

        db.update(TableNames.TABLE_ACCOUNT, values, null, null);
    }

    public String staticCall() {
        return "sucess static";

    }

    public  String nonstaticCall() {
        return "sucess nonstatic";

    }

    public void updateAccountDetails(SQLiteDatabase db, com.milky.service.core.Account holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.MOBILE, holder.getMobile());

        db.update(TableNames.TABLE_ACCOUNT, values, null, null);
    }

    public void updateMobileNumber(SQLiteDatabase db, String mobile) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.MOBILE, mobile);
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.DIRTY, "1");

        db.update(TableNames.TABLE_ACCOUNT, values, null, null);
    }



    public com.milky.service.core.Account getAccountDetailsToSync(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT + " WHERE " + TableColumns.SYNC_STATUS + " ='0'";
        Cursor cursor = db.rawQuery(selectquery, null);
        com.milky.service.core.Account holder = new com.milky.service.core.Account();
        if (cursor.moveToFirst()) {
            do {

                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.FARMER_CODE)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return holder;
    }

    public com.milky.service.core.Account getFarmerName(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        com.milky.service.core.Account holder = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                holder = new com.milky.service.core.Account();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)) != null)
                    holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)) != null)
                    holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return holder;
    }

    public com.milky.service.core.Account getAccountDetails(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(selectquery, null);
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

    public boolean isAccountExpired(SQLiteDatabase db) {
        Calendar cal = Calendar.getInstance();
        String day = Constants.api_format.format(cal.getTime());
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT + " WHERE "
                + TableColumns.END_DATE + " <'" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public JSONObject getDetails(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(selectquery, null);
        JSONObject jsonObject = new JSONObject();
        if (cursor.moveToFirst()) {
            do {
                try {
                    jsonObject.put("FarmerCode", cursor.getString(cursor.getColumnIndex(TableColumns.FARMER_CODE)));
                    jsonObject.put("FirstName", cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                    jsonObject.put("LastName", cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                    jsonObject.put("Mobile", cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                    jsonObject.put("Validated", cursor.getString(cursor.getColumnIndex(TableColumns.VALIDATED)));
                    jsonObject.put("Dirty", cursor.getString(cursor.getColumnIndex(TableColumns.DIRTY)));
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

    public int getAccountId(SQLiteDatabase db) {
        String countQuery = "SELECT  * FROM " + TableNames.TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
}
