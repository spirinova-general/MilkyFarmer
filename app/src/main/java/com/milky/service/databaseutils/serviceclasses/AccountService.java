package com.milky.service.databaseutils.serviceclasses;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Bill;
import com.milky.service.core.Customers;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceinterface.IAccountService;
import com.milky.service.core.Account;
import com.milky.service.databaseutils.serviceinterface.ISmsService;
import com.milky.service.serverapi.HttpAsycTask;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.service.serverapi.ServerApis;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccountService implements IAccountService {

    @Override
    public void insert(Account account) {
        ContentValues values = account.ToContentValues();
        getDb().insert(TableNames.ACCOUNT, null, values);
    }

    @Override
    public void update(Account account) {
        ContentValues values = account.ToContentValues();
        getDb().update(TableNames.ACCOUNT, values, TableColumns.ServerAccountId + " ='" + account.getServerAccountId() + "'", null);
    }

    @Override
    public Account getDetails() {
        String selectquery = "SELECT * FROM " + TableNames.ACCOUNT;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        com.milky.service.core.Account holder = new com.milky.service.core.Account();
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
    public int getRemainingSMS() {
        Account account = getDetails();
        return account.getTotalSms() - account.getUsedSms();
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
    public void incrementUsedSMSCount(int count) {
        Account account = getDetails();
        ContentValues values = new ContentValues();
        values.put(TableColumns.UsedSms, String.valueOf(account.getUsedSms() + count));
        getDb().update(TableNames.ACCOUNT, values, null, null);
    }

    @Override
    public void syncAccount(){
        Account account = getDetails();
        HttpAsycTask dataTask = new HttpAsycTask();
        String append = ServerApis.API_ACCOUNT_GET + "?accountId=" + account.getServerAccountId();
        dataTask.runRequest(ServerApis.API_ACCOUNT_GET, null, null, false, null);

        //To do later
    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

}
