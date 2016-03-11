package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.utils.Constants;
import com.milky.viewmodel.VAccount;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Created by Neha on 12/26/2015.
 */
public class Account {
    public static void insertAccountDetails(SQLiteDatabase db, VAccount holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FARMER_CODE, holder.getFarmerCode());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.DATE_ADDED, holder.getDateAdded());
        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.MOBILE, holder.getMobile());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.EXPIRY_DATE, holder.getExpiryDate());
        values.put(TableColumns.TOTAL_SMS, holder.getTotalSms());
        values.put(TableColumns.USED_SMS, holder.getUsedSms());
        values.put(TableColumns.ACCOUNT_ID, holder.getId());
        values.put(TableColumns.VALIDATED, holder.getValidated());
        values.put(TableColumns.ROLL_DATE, holder.getRollDate());

        long i = db.insert(TableNames.TABLE_ACCOUNT, null, values);
    }

    public static void updateAllAccountDetails(SQLiteDatabase db, VAccount holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.EXPIRY_DATE, holder.getExpiryDate());
        values.put(TableColumns.TOTAL_SMS, holder.getTotalSms());
//        values.put(TableColumns.USED_SMS, holder.getUsedSms());
        values.put(TableColumns.ACCOUNT_ID, holder.getId());

        db.update(TableNames.TABLE_ACCOUNT, values, TableColumns.ACCOUNT_ID + " ='" + holder.getId() + "'", null);
    }

    public static void updateSMSCount(SQLiteDatabase db, int count) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.USED_SMS, String.valueOf(getUsedSMS(db) + count));
//        values.put(TableColumns.TOTAL_SMS, String.valueOf(getLeftsmsCount(db) - count));
        db.update(TableNames.TABLE_ACCOUNT, values, null, null);
    }
    public static boolean columnRollDateExists(SQLiteDatabase db) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getColumnIndex(TableColumns.ROLL_DATE)  > 0;

        cursor.close();
        return result;
    }

    public static String getDefaultRate(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        String rate = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    rate = cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return rate;
    }
    public static String getRollDate(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        String rate = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ROLL_DATE)) != null)
                    rate = cursor.getString(cursor.getColumnIndex(TableColumns.ROLL_DATE));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return rate;
    }

    public static int getLeftsmsCount(SQLiteDatabase db) {
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

    public static int getUsedSMS(SQLiteDatabase db) {
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

    public static String getExpirationDate(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        String date = "";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.EXPIRY_DATE)) != null)
                    date = cursor.getString(cursor.getColumnIndex(TableColumns.EXPIRY_DATE));

            }
            while (cursor.moveToNext());

        }

        return date;
    }

    public static void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.SYNC_STATUS, "1");

        db.update(TableNames.TABLE_ACCOUNT, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
                + " AND " + TableColumns.DIRTY + " ='" + "0" + "'", null);
    }
    public static void updateRollDate(SQLiteDatabase db,String date) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ROLL_DATE,date );

        db.update(TableNames.TABLE_ACCOUNT, values,null,null);
    }

    public static void updateAccountDetails(SQLiteDatabase db, VAccount holder) {
        ContentValues values = new ContentValues();

//        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());

        values.put(TableColumns.FIRST_NAME, holder.getFirstName());
        values.put(TableColumns.LAST_NAME, holder.getLastName());
        values.put(TableColumns.MOBILE, holder.getMobile());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.ROLL_DATE,holder.getRollDate());

        values.put(TableColumns.SYNC_STATUS, "0");

        values.put(TableColumns.DIRTY, "0");

        long i = db.update(TableNames.TABLE_ACCOUNT, values, null, null);
    }

    public static void updateMobileNumber(SQLiteDatabase db, String mobile) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.MOBILE, mobile);
        values.put(TableColumns.SYNC_STATUS, "0");
        values.put(TableColumns.DIRTY, "0");

        db.update(TableNames.TABLE_ACCOUNT, values, null, null);
    }

    public static String getDefautTax(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        String rate = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    rate = cursor.getString(cursor.getColumnIndex(TableColumns.TAX));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return rate;
    }

    public static VAccount getAccountDetailsToSync(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT + " WHERE " + TableColumns.SYNC_STATUS + " ='0'";
        Cursor cursor = db.rawQuery(selectquery, null);
        VAccount holder = new VAccount();
        if (cursor.moveToFirst()) {
            do {

                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.FARMER_CODE)));


            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return holder;
    }

    public static VAccount getFarmerName(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        VAccount holder = null;
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                holder = new VAccount();
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

    public static VAccount getAccountDetails(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(selectquery, null);
        VAccount holder = new VAccount();
        if (cursor.moveToFirst()) {
            do {

                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setFarmerCode(cursor.getString(cursor.getColumnIndex(TableColumns.FARMER_CODE)));


            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return holder;
    }

    public static boolean isAccountExpired(SQLiteDatabase db) {
        Calendar cal = Calendar.getInstance();
        String day = Constants.api_format.format(cal.getTime());
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT + " WHERE "
                + TableColumns.EXPIRY_DATE + " <'" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static JSONObject getDetails(SQLiteDatabase db) {
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
                    jsonObject.put("EndDate", cursor.getString(cursor.getColumnIndex(TableColumns.EXPIRY_DATE)));
                    jsonObject.put("UsedSms", cursor.getString(cursor.getColumnIndex(TableColumns.USED_SMS)));
                    jsonObject.put("TotalSms", cursor.getString(cursor.getColumnIndex(TableColumns.TOTAL_SMS)));
                    jsonObject.put("Id", cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return jsonObject;
    }

    public static int getAccountId(SQLiteDatabase db) {
        String countQuery = "SELECT  * FROM " + TableNames.TABLE_ACCOUNT;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }


    //to get account Expiry date
//    public static String getEXpiryDate(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT;
//        String expiryDate = null;
//        Cursor cursor = db.rawQuery(selectquery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.EXPIRY_DATE)) != null)
//                    expiryDate = cursor.getString(cursor.getColumnIndex(TableColumns.EXPIRY_DATE));
//
//            }
//            while (cursor.moveToNext());
//
//        }
//        cursor.close();
//        if (db.isOpen())
//            db.close();
//        return expiryDate;
//    }

}
