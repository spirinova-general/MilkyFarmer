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
        values.put(TableColumns.Dirty, 0);
        values.put(TableColumns.EndDate, holder.getEndDate());
        values.put(TableColumns.TotalSms, holder.getTotalSms());
        values.put(TableColumns.ServerAccountId, holder.getServerAccountId());

        db.update(TableNames.ACCOUNT, values, TableColumns.ServerAccountId + " ='" + holder.getServerAccountId() + "'", null);
    }

    public void updateSMSCount(SQLiteDatabase db, int count) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.UsedSms, String.valueOf(getUsedSMS(db) + count));
//        values.put(TableColumns.TotalSms, String.valueOf(getLeftsmsCount(db) - count));
        db.update(TableNames.ACCOUNT, values, null, null);
    }

    public boolean columnRollDateExists(SQLiteDatabase db) {

        String selectQuery = "SELECT * FROM " + TableNames.ACCOUNT;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getColumnIndex(TableColumns.RollDate) > 0;

        cursor.close();
        return result;
    }



    public int getLeftsmsCount(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        int sms = 0;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TotalSms)) != null)
                    sms = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TableColumns.TotalSms)))
                            - getUsedSMS(db);

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return sms;
    }

    public int getUsedSMS(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        int sms = 0;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TotalSms)) != null)
                    sms = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TableColumns.UsedSms)));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return sms;
    }

    public String getExpirationDate(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        String date = "";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                    date = cursor.getString(cursor.getColumnIndex(TableColumns.EndDate));

            }
            while (cursor.moveToNext());

        }

        return date;
    }

    public void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.Dirty, 0);

        db.update(TableNames.ACCOUNT, values, TableColumns.Dirty + " ='" +0 + "'", null);
    }

    public void updateRollDate(SQLiteDatabase db, String date) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.RollDate, date);

        db.update(TableNames.ACCOUNT, values, null, null);
    }

    public String staticCall() {
        return "sucess static";

    }

    public  String nonstaticCall() {
        return "sucess nonstatic";

    }

    public void updateAccountDetails(SQLiteDatabase db, com.milky.service.core.Account holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FirstName, holder.getFirstName());
        values.put(TableColumns.LastName, holder.getLastName());
        values.put(TableColumns.Mobile, holder.getMobile());

        db.update(TableNames.ACCOUNT, values, null, null);
    }

    public void updateMobileNumber(SQLiteDatabase db, String mobile) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.Mobile, mobile);
        values.put(TableColumns.Dirty, 0);

        db.update(TableNames.ACCOUNT, values, null, null);
    }



    public com.milky.service.core.Account getFarmerName(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        com.milky.service.core.Account holder = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                holder = new com.milky.service.core.Account();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)) != null)
                    holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.LastName)) != null)
                    holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return holder;
    }

    public com.milky.service.core.Account getAccountDetails(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        Cursor cursor = db.rawQuery(selectquery, null);
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

    public boolean isAccountExpired(SQLiteDatabase db) {
        Calendar cal = Calendar.getInstance();
        String day = Constants.api_format.format(cal.getTime());
        String selectQuery = "SELECT * FROM " + TableNames.ACCOUNT + " WHERE "
                + TableColumns.EndDate + " <'" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public JSONObject getDetails(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        Cursor cursor = db.rawQuery(selectquery, null);
        JSONObject jsonObject = new JSONObject();
        if (cursor.moveToFirst()) {
            do {
                try {
                    jsonObject.put("FarmerCode", cursor.getString(cursor.getColumnIndex(TableColumns.FarmerCode)));
                    jsonObject.put("FirstName", cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
                    jsonObject.put("LastName", cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));
                    jsonObject.put("Mobile", cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
                    jsonObject.put("Validated", cursor.getString(cursor.getColumnIndex(TableColumns.Validated)));
                    jsonObject.put("Dirty", cursor.getString(cursor.getColumnIndex(TableColumns.Dirty)));
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

    public int getAccountId(SQLiteDatabase db) {
        String countQuery = "SELECT  * FROM " + TableNames.ACCOUNT;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
}
