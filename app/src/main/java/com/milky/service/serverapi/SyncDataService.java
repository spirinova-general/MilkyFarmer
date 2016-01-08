package com.milky.service.serverapi;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.milky.service.databaseutils.Account;
import com.milky.service.databaseutils.BillTableManagement;
import com.milky.service.databaseutils.CustomerSettingTableManagement;
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.service.databaseutils.TableNames;
import com.milky.utils.AppUtil;
import com.milky.viewmodel.VAccount;
import com.milky.viewmodel.VBill;
import com.milky.viewmodel.VCustomersList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SyncDataService extends Service implements OnTaskCompleteListner {
    private DatabaseHelper _dbHelper;
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        _dbHelper = AppUtil.getInstance().getDatabaseHandler();
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                if ((c.get(Calendar.DAY_OF_MONTH)) == c.getActualMaximum(Calendar.DAY_OF_MONTH)) {

//                    updateDataForNewMonth();
                }
                SyncNow();
                handler.postDelayed(runnable, 50000);
            }
        };

        handler.postDelayed(runnable, 10000);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public void SyncNow() {

        HttpAsycTask dataTask = new HttpAsycTask();
        dataTask.runRequest(ServerApis.SYNC, getAllDataToSync(), this, true, requestedList);
    }

    public static HashMap<String, String> requestedList = new HashMap<>();

    private List<NameValuePair> getAllDataToSync() {
        JSONArray jsonArray;
        JSONObject jsonObject = new JSONObject();
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_ACCOUNT)) {
            VAccount custList = Account.getAccountDetailsToSync(_dbHelper.getReadableDatabase());
            if (custList == null) {

                requestedList.put("Account_List", "1");

            } else {
                jsonArray = new JSONArray();

                JSONObject obj = new JSONObject();
                try {
                    obj.put("FirstName", custList.getFirstName());
                    obj.put("LastName", custList.getLastName());
                    obj.put("Mobile", custList.getMobile());
                    obj.put("Validated", true);
                    obj.put("FarmerCode", custList.getFarmerCode());
                    obj.put("Dirty", "0");

                    jsonArray.put(obj);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    jsonObject.put("Account_List", jsonArray);
                    requestedList.put("Account_List", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }


        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {
            ArrayList<VCustomersList> custList = CustomersTableMagagement.getAllCustomersToSync(_dbHelper.getReadableDatabase());
            if (custList.size() == 0) {

                requestedList.put("Customer_List", "1");

            } else {
                jsonArray = new JSONArray();
                for (int i = 0; i < custList.size(); ++i) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("FirstName", custList.get(i).getFirstName());
                        obj.put("LastName", custList.get(i).getLastName());
                        obj.put("Mobile", custList.get(i).getMobile());
                        obj.put("Address1", custList.get(i).getAddress1());
                        obj.put("Address2", custList.get(i).getAddress2());
                        obj.put("Balance", custList.get(i).getBalance_amount());
                        obj.put("DateAdded", custList.get(i).getDateAdded());
                        obj.put("DateModified", custList.get(i).getDateModified());
                        obj.put("AccountId", "5");
                        obj.put("AreaId", "1");
                        obj.put("Dirty", "0");

                        jsonArray.put(obj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    jsonObject.put("Customer_List", jsonArray);
                    requestedList.put("Customer_List", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }
        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER_BILL)) {
            ArrayList<VCustomersList> billList = BillTableManagement.getCustomersBillToSync(_dbHelper.getReadableDatabase());
            if (billList.size() == 0) {

                requestedList.put("Customer_List", "1");

            } else {
                jsonArray = new JSONArray();
                for (int i = 0; i < billList.size(); ++i) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("CustomerId", billList.get(i).getCustomerId());
                        obj.put("StartDate", billList.get(i).getStart_date());
                        obj.put("EndDate", billList.get(i).getEnd_date());
                        obj.put("Quantity", billList.get(i).getQuantity());
                        obj.put("Balance", "0");
                        obj.put("DateAdded", billList.get(i).getDateAdded());
                        obj.put("DateModified", billList.get(i).getDateModified());
                        obj.put("AccountId", "5");
                        obj.put("AreaId", "1");
                        obj.put("Dirty", "0");
                        obj.put("Adjustment", billList.get(i).getAdjustment());
                        obj.put("TOTAL_AMOUNT", billList.get(i).getBalance_amount());
                        obj.put("TAX", billList.get(i).getTax());
                        obj.put("IsCleared", billList.get(i).getIsCleared());
                        obj.put("PaymentMade", billList.get(i).getPaymentMade());

                        jsonArray.put(obj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                try {
                    jsonObject.put("Bill_List", jsonArray);
                    requestedList.put("Bill_List", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

        if (_dbHelper.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {
            ArrayList<VCustomersList> custList = CustomerSettingTableManagement.getAllCustomersByCustomerIdToSync(_dbHelper.getReadableDatabase());
            if (custList == null) {

                requestedList.put("CustomerSetting_List", "1");

            } else {
                jsonArray = new JSONArray();
                for (int i = 0; i < custList.size(); ++i) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("AccountId", custList.get(i).getAccountId());
                        obj.put("CustomerId", custList.get(i).getCustomerId());
                        obj.put("Rate", custList.get(i).getRate());
                        obj.put("DefaultQuantity", custList.get(i).getQuantity());
                        obj.put("DateModified", custList.get(i).getDateModified());
                        obj.put("Dirty", "0");

                        jsonArray.put(obj);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    jsonObject.put("CustomerSetting_List", jsonArray);
                    requestedList.put("CustomerSetting_List", "0");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


        }

        List<NameValuePair> nameValuePair = new ArrayList<>();
        if (jsonObject.length() > 0)
            nameValuePair.add(new BasicNameValuePair("", jsonObject.toString()));


        return nameValuePair;
    }

    @Override
    public void onTaskCompleted(JSONArray result, String type, HashMap<String, String> requestType) {
        if (ServerApis.STATUS == 1) {
            if (type == ServerApis.SYNC) {
                if (requestType.get("Customer_List").equals("0")) {
                    CustomersTableMagagement.updateSyncedData(_dbHelper.getWritableDatabase());
                } else if (requestType.get("Bill_List").equals("0")) {
                    BillTableManagement.updateSyncedData(_dbHelper.getWritableDatabase());
                } else if (requestType.get("Account_List").equals("0")) {
                    Account.updateSyncedData(_dbHelper.getWritableDatabase());
                } else if (requestType.get("CustomerSetting_List").equals("0")) {
                    CustomerSettingTableManagement.updateSyncedData(_dbHelper.getWritableDatabase());
                }
            }
        } else {


        }
    }

    Calendar c = Calendar.getInstance();

    private void updateDataForNewMonth() {


        DatabaseHelper db = AppUtil.getInstance().getDatabaseHandler();
        if (db.isTableNotEmpty(TableNames.TABLE_CUSTOMER)) {
            ArrayList<VCustomersList> list = CustomersTableMagagement.getAllCustomers(db.getReadableDatabase());
            db.close();
            for (int i = 0; i < list.size(); ++i) {
                // update customers setting
                VBill holder = new VBill();
                holder.setEndDate(String.format("%02d", c.get(Calendar.MONTH) + 2) + "-"
                        + String.format("%02d", 32) + "-" + String.format("%02d", c.get(Calendar.YEAR)));
                holder.setStartDate(String.format("%02d", c.get(Calendar.MONTH) + 2) + "-"
                        + String.format("%02d", 1) + "-" + String.format("%02d", c.get(Calendar.YEAR)));
                holder.setRate(list.get(i).getRate());
                holder.setAccountId(list.get(i).getAccountId());
                holder.setAdjustment("0");
                holder.setBalance("0");
                holder.setCustomerId(list.get(i).getCustomerId());
                holder.setDateAdded(list.get(i).getDateAdded());
                holder.setDateModified(list.get(i).getDateModified());
                holder.setQuantity(list.get(i).getQuantity());
                holder.setTax(list.get(i).getTax());
                holder.setIsCleared("1");
                if (!CustomerSettingTableManagement.isHasDataForDay(db.getReadableDatabase(), list.get(i).getCustomerId(), String.format("%02d", c.get(Calendar.MONTH) + 2) + "-"
                        + String.format("%02d", 1) + "-" + String.format("%02d", c.get(Calendar.YEAR))))
                    CustomerSettingTableManagement.insertNewCustomersSetting(db.getWritableDatabase(), holder);
                if (!BillTableManagement.isHasDataForDay(db.getReadableDatabase(), list.get(i).getCustomerId(), String.format("%02d", c.get(Calendar.MONTH) + 2) + "-"
                        + String.format("%02d", 1) + "-" + String.format("%02d", c.get(Calendar.YEAR))))
                    BillTableManagement.insertNewBills(db.getWritableDatabase(), holder);
            }
            db.close();
        }
    }
}
