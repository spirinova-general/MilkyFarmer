package com.milky.service.serverapi;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.milky.service.core.GlobalSettings;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceclasses.AccountService;
import com.milky.service.databaseutils.serviceclasses.BillService;
import com.milky.service.databaseutils.serviceclasses.GlobalSettingsService;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.service.core.Account;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SyncDataService extends Service implements OnTaskCompleteListner {
    private DatabaseHelper _dbHelper;
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private AccountService accountService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        _dbHelper = new DatabaseHelper(this);
        handler = new Handler();
        accountService = new AccountService();
//        _exDb = new ExtcalDatabaseHelper(this);
        runnable = new Runnable() {
            //SharedPreferences preferences = getApplicationContext().getSharedPreferences("com.milky_prefrences", MODE_PRIVATE);
            //SharedPreferences.Editor edit = preferences.edit();

            public void run() {
                if (_dbHelper.isTableNotEmpty(TableNames.ACCOUNT))
                    SyncNow();
                handler.postDelayed(runnable, 50000);

                // Get roll date from db.
                /*Calendar calendar = Calendar.getInstance();
                SimpleDateFormat work_format = new SimpleDateFormat("yyyy-MM-dd");
                work_format.format(calendar.getTime());

                try {
                    if (_dbHelper.isTableNotEmpty(TableNames.ACCOUNT)) {
                        Date date = work_format.parse(new GlobalSettingsService().getRollDate());
                        calendar.setTime(date);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
                /*Calendar c = Calendar.getInstance();
                if (c.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH) && c.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)) {
                    //Bill is to be outstanding
                    // update outstanding bills
                    if (_dbHelper.isTableNotEmpty(TableNames.CUSTOMER)) {
                        BillService billService = new BillService();
                        billService.getTotalAllBill();

                        if (preferences.contains(UserPrefrences.INSERT_BILL) && !preferences.getString(UserPrefrences.INSERT_BILL, "0").equals("1")) {
                            CustomersService customersService = new CustomersService();
                            List<Customers> list = customersService.getAllCustomers();
                            Calendar cal = Calendar.getInstance();
                            String
                                    currentDate = String.valueOf(cal.get(Calendar.YEAR)) + "-" + String.format("%02d",cal.get(Calendar.MONTH)+1) + "-" + String.format("%02d",cal.get(Calendar.DAY_OF_MONTH));
//
                            for (int i = 0; i < list.size(); ++i) {
                                CustomersSetting custHolder = new CustomersSettingService().getByCustId(list.get(i).getCustomerId(), currentDate);
                                Calendar nextMonth = Calendar.getInstance();
                                nextMonth.add(Calendar.MONTH, 1);
                                new CustomersSettingService().updateEndDateForRoll(currentDate, custHolder.getCustomerId());
//                                custHolder.setStart_date(cal.get(Calendar.YEAR) + "-" + String.format("%02d", nextMonth.get(Calendar.MONTH) + 1) + "-" +
//                                        "01");
                                if (cal.getActualMaximum(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH))
                                    custHolder.setStartDate(cal.get(Calendar.YEAR) + "-" + String.format("%02d", nextMonth.get(Calendar.MONTH) + 1) + "-" +
                                            "01");
                                else
                                    custHolder.setStartDate(cal.get(Calendar.YEAR) + "-" + String.format("%02d", nextMonth.get(Calendar.MONTH)) + "-" +
                                            String.format("%02d", cal.get(Calendar.DAY_OF_MONTH) + 1));
                                custHolder.setEndDate("2250" + "-" + String.format("%02d", nextMonth.get(Calendar.MONTH) + 13) + "-" +
                                        String.format("%02d", nextMonth.getActualMaximum(Calendar.DAY_OF_MONTH) + 5));
                                String newRolldate = cal.get(Calendar.YEAR) + "-" + String.format("%02d", nextMonth.get(Calendar.MONTH) + 1) + "-" +
                                        String.format("%02d", nextMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
                                new GlobalSettingsService().updateRollDate(newRolldate);
                                CustomersSettingService customersSettingService = new CustomersSettingService();

                                if (!customersSettingService.isHasDataForCustoner(custHolder.getStartDate(), custHolder.getCustomerId())) {
                                    customersSettingService.insert(custHolder);
                                    Bill bill = new Bill();
                                    bill.setStartDate(custHolder.getStartDate());
                                    bill.setEndDate(custHolder.getEndDate());
                                    bill.setDirty(0);
                                    bill.setTotalAmount(0);
                                    bill.setDateModified(currentDate);
                                    bill.setAdjustment(0);
                                    bill.setBalance(0);
                                    bill.setCustomerId(custHolder.getCustomerId());
                                    bill.setDateAdded(currentDate);
                                    bill.setIsCleared(0);
                                    bill.setTax(0);
                                    bill.setIsOutstanding(0);
                                    bill.setPaymentMade(0);
                                    bill.setQuantity(custHolder.getGetDefaultQuantity());
                                    bill.setRate(custHolder.getDefaultRate());
                                    bill.setRollDate(newRolldate);
                                    new BillService().insert(bill);
                                }
                            }
                            edit.putString(UserPrefrences.INSERT_BILL, "1");
                            edit.apply();
                        }
                    } else {
                        edit.putString(UserPrefrences.INSERT_BILL, "0");
                        edit.apply();
                    }
                } else {
                    edit.putString(UserPrefrences.INSERT_BILL, "0");
                    edit.apply();
                }*/
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
    }

    public void SyncNow() {
        HttpAsycTask dataTask = new HttpAsycTask();
        dataTask.runRequest(ServerApis.API_ACCOUNT_ADD, accountService.getJsonData(), this, true, null);
        ReCalculateBills();
    }

    private void ReCalculateBills()
    {
        GlobalSettingsService gsService = new GlobalSettingsService();
        BillService billService = new BillService();
        GlobalSettings setting = gsService.getData();
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        String lastBillSyncedTimeStr = setting.getLastBillSyncedTime();

        boolean toRecalculate = false;
        //Recalculate only once in a day
        if( lastBillSyncedTimeStr == null )
        {
            toRecalculate = true;
        }
        else
        {
            Date lastBillSyncedTime = Utils.FromDateString(lastBillSyncedTimeStr);
            if( Utils.BeforeDate(lastBillSyncedTime, today))
                toRecalculate = true;
        }

        if( toRecalculate) {
            billService.RecalculateAllCurrentBills();
            gsService.updateLastBillSyncedTime();
        }
    }

    public static HashMap<String, String> requestedList = new HashMap<>();

    private List<NameValuePair> getAllDataToSync() {
        JSONArray jsonArray;
        JSONObject jsonObject = new JSONObject();
        if (_dbHelper.isTableNotEmpty(TableNames.ACCOUNT)) {
            Account custList = accountService.getDetails();
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


//        if (_dbHelper.isTableNotEmpty(TableNames.CUSTOMER)) {
//            ArrayList<VCustomers> custList = CustomersTableMagagement.getAllCustomersToSync(_dbHelper.getReadableDatabase());
//            if (custList.size() == 0) {
//
//                requestedList.put("Customer_List", "1");
//
//            } else {
//                jsonArray = new JSONArray();
//                for (int i = 0; i < custList.size(); ++i) {
//                    JSONObject obj = new JSONObject();
//                    try {
//                        obj.put("FirstName", custList.get(i).getFirstName());
//                        obj.put("LastName", custList.get(i).getLastName());
//                        obj.put("Mobile", custList.get(i).getMobile());
//                        obj.put("Address1", custList.get(i).getAddress1());
//                        obj.put("Address2", custList.get(i).getAddress2());
//                        obj.put("Balance", custList.get(i).getBalance_amount());
//                        obj.put("DateAdded", custList.get(i).getDateAdded());
//                        obj.put("DateModified", custList.get(i).getDateModified());
//                        obj.put("AccountId", "5");
//                        obj.put("AreaId", "1");
//                        obj.put("Dirty", "0");
//
//                        jsonArray.put(obj);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                try {
//                    jsonObject.put("Customer_List", jsonArray);
//                    requestedList.put("Customer_List", "0");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        }
//        if (_dbHelper.isTableNotEmpty(TableNames.Bill)) {
//            ArrayList<VCustomers> billList = BillTableManagement.getCustomersBillToSync(_dbHelper.getReadableDatabase());
//            if (billList.size() == 0) {
//
//                requestedList.put("Customer_List", "1");
//
//            } else {
//                jsonArray = new JSONArray();
//                for (int i = 0; i < billList.size(); ++i) {
//                    JSONObject obj = new JSONObject();
//                    try {
//                        obj.put("CustomerId", billList.get(i).getCustomerId());
//                        obj.put("StartDate", billList.get(i).getStart_date());
//                        obj.put("EndDate", billList.get(i).getEnd_date());
//                        obj.put("Quantity", billList.get(i).getQuantity());
//                        obj.put("Balance", "0");
//                        obj.put("DateAdded", billList.get(i).getDateAdded());
//                        obj.put("DateModified", billList.get(i).getDateModified());
//                        obj.put("AccountId", "5");
//                        obj.put("AreaId", "1");
//                        obj.put("Dirty", "0");
//                        obj.put("Adjustment", billList.get(i).getAdjustment());
//                        obj.put("TotalAmount", billList.get(i).getBalance_amount());
//                        obj.put("TAX", billList.get(i).getTax());
//                        obj.put("IsCleared", billList.get(i).getIsCleared());
//                        obj.put("PaymentMade", billList.get(i).getPaymentMade());
//
//                        jsonArray.put(obj);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//                try {
//                    jsonObject.put("Bill_List", jsonArray);
//                    requestedList.put("Bill_List", "0");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        }

        List<NameValuePair> nameValuePair = new ArrayList<>();
        if (jsonObject.length() > 0)
            nameValuePair.add(new BasicNameValuePair("", jsonObject.toString()));


        return nameValuePair;
    }

    @Override
    public void onTaskCompleted(String type, HashMap<String, String> requestType) {
        if (type.equals(ServerApis.API_ACCOUNT_ADD)) {
            if (Constants.API_RESPONCE != null) {
                AccountService accountService = new AccountService();
                Account account = accountService.getDetails();
                try {
                    JSONObject result = Constants.API_RESPONCE;
                    account.setEndDate(result.getString("EndDate"));
                    accountService.update(account);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /*    } else if (type.equals(ServerApis.SYNC)) {
            if (requestType.get("Customer_List").equals("0")) {
//                CustomersTableMagagement.updateSyncedData(_dbHelper.getWritableDatabase());
            } else if (requestType.get("Bill_List").equals("0")) {
//                BillTableManagement.updateSyncedData(_dbHelper.getWritableDatabase());
            } else if (requestType.get("Account_List").equals("0")) {
//                accountService.updateSyncedData(_dbHelper.getWritableDatabase());
            }
// else if (requestType.get("CustomerSetting_List").equals("0")) {
//                    CustomerSettingTableManagement.updateSyncedData(_dbHelper.getWritableDatabase());
//                }
        }

    }


    private void updateDataForNewMonth() {
        Calendar c = Calendar.getInstance();
        DatabaseHelper db = AppUtil.getInstance().getDatabaseHandler();
        if (db.isTableNotEmpty(TableNames.CUSTOMER)) {
//            ArrayList<VCustomers> list = CustomersTableMagagement.getAllCustomers(db.getReadableDatabase());
//            for (int i = 0; i < list.size(); ++i) {
//                // update customers setting
//                VBill holder = new VBill();
//                holder.setEndDate(String.format("%02d", c.get(Calendar.MONTH) + 2) + "-"
//                        + String.format("%02d", 32) + "-" + String.format("%02d", c.get(Calendar.YEAR)));
//                holder.setStartDate(String.format("%02d", c.get(Calendar.MONTH) + 2) + "-"
//                        + String.format("%02d", 1) + "-" + String.format("%02d", c.get(Calendar.YEAR)));
//                holder.setRate(list.get(i).getRate());
//                holder.setAccountId(list.get(i).getAccountId());
//                holder.setAdjustment("0");
//                holder.setBalance("0");
//                holder.setCustomerId(list.get(i).getCustomerId());
//                holder.setDateAdded(list.get(i).getDateAdded());
//                holder.setDateModified(list.get(i).getDateModified());
//                holder.setQuantity(list.get(i).getQuantity());
//                holder.setTax(list.get(i).getTax());
//                holder.setIsCleared("1");
//                if (!CustomerSettingTableManagement.isHasDataForDayById(db.getReadableDatabase(), list.get(i).getCustomerId(), String.format("%02d", c.get(Calendar.MONTH) + 2) + "-"
//                        + String.format("%02d", 1) + "-" + String.format("%02d", c.get(Calendar.YEAR))))
//                    CustomerSettingTableManagement.insertNewCustomersSetting(db.getWritableDatabase(), holder);
//                if (!BillTableManagement.isHasDataForDay(db.getReadableDatabase(), list.get(i).getCustomerId(), String.format("%02d", c.get(Calendar.MONTH) + 2) + "-"
//                        + String.format("%02d", 1) + "-" + String.format("%02d", c.get(Calendar.YEAR))))
//                    BillTableManagement.insertNewBills(db.getWritableDatabase(), holder);
//            }
        }
    }*/

}
