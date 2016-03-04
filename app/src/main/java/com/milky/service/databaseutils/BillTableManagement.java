package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.utils.Constants;
import com.milky.viewmodel.VBill;
import com.tyczj.extendedcalendarview.DeliveryTableManagement;
import com.tyczj.extendedcalendarview.ExtcalCustomerSettingTableManagement;
import com.tyczj.extendedcalendarview.ExtcalVCustomersList;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Neha on 11/30/2015.
 */
public class BillTableManagement {
    public static void insertBillData(SQLiteDatabase db, ExtcalVCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.START_DATE, holder.getStart_date());
        values.put(TableColumns.END_DATE, holder.getEnd_date());
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.ADJUSTMENTS, "0");
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.IS_CLEARED, holder.getIsCleared());
        values.put(TableColumns.PAYMENT_MADE, holder.getPaymentMade());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.BILL_MADE, holder.getBillMade());
        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.IS_OUTSTANDING, holder.getOutstanding());
        values.put(TableColumns.DIRTY, "1");

        long i = db.insert(TableNames.TABLE_CUSTOMER_BILL, null, values);
    }

//    public static void updateBillData(SQLiteDatabase db, VBill holder) {
//        ContentValues values = new ContentValues();
//        values.put(TableColumns.PAYMENT_MADE, holder.getPaymentMode());
//        values.put(TableColumns.BALANCE, holder.getBalance());
//        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
//        values.put(TableColumns.BILL_MADE, holder.getBillMade());
//        long i = db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
//                + " AND " + TableColumns.START_DATE + " ='" + holder.getStartDate() + "'", null);
//        long j = i;
//    }

    public static String isHasBill(SQLiteDatabase db, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE "
                + TableColumns.END_DATE + " >='" + day + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        String bill = null;

        if (cursor.moveToFirst()) {
            do {
                bill = cursor.getString(cursor.getColumnIndex(TableColumns.BILL_MADE));


            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return bill;
    }

    public static void updateOutstandingBills(SQLiteDatabase db, String date) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.IS_OUTSTANDING, "0");
        values.put(TableColumns.END_DATE, date);
        long i = db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.END_DATE + " >='" + date + "'"
                + " AND " + TableColumns.START_DATE + " <='" + date + "'", null);
    }

    public static void updateBillMade(SQLiteDatabase db, String date, String bill,String custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.BILL_MADE, bill);
        long i = db.update(TableNames.TABLE_CUSTOMER_BILL, values,TableColumns.CUSTOMER_ID+" ='"+custId+"'"+" AND "+TableColumns.END_DATE + " >='" + date + "'"
                + " AND " + TableColumns.START_DATE + " <='" + date + "'", null);
    }

    public static void updateClearBills(SQLiteDatabase db, String date, String custid, VBill holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.IS_CLEARED, "0");
        values.put(TableColumns.END_DATE, date);
        values.put(TableColumns.PAYMENT_MADE, holder.getPaymentMode());
        values.put(TableColumns.BALANCE, holder.getBalance());
        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        values.put(TableColumns.BILL_MADE, holder.getBillMade());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());

        long i = db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + custid + "' AND " + TableColumns.END_DATE + " >='" + date + "'"
                + " AND " + TableColumns.START_DATE + " <='" + date + "' AND " + TableColumns.IS_CLEARED + " ='1'", null);
    }

    public static void updateCurrentDateData(SQLiteDatabase db, ExtcalVCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.END_DATE, holder.getEnd_date());
        values.put(TableColumns.AREA_ID, holder.getAreaId());

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.START_DATE + " ='" + holder.getStart_date() + "'", null);
    }

    public static void updateEndDate(SQLiteDatabase db, ExtcalVCustomersList holder, String enddate, String updatedEndDate) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.END_DATE, updatedEndDate);

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.END_DATE + " ='" + enddate + "'", null);
    }

    public static boolean isHasDataForDay(SQLiteDatabase db, String custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE + " ='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static boolean isToBeOutstanding(SQLiteDatabase db, String custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.END_DATE + " ='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

//    public static double getQuatity(SQLiteDatabase db, String id, String date) {
//        String selectquery =
//
//                "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE "
//                        + TableColumns.START_DATE + " <='" + date + "'" + " AND " + TableColumns.END_DATE
//                        + " >'" + date + "'" + " AND "
//                        + TableColumns.CUSTOMER_ID + " ='" + id + "' AND " + TableColumns.IS_CLEARED + " ='1'";
//
//        double qty = 0;
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                qty = Double.parseDouble(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
//
//
//            }
//            while (cursor.moveToNext());
//        }
//
//
//        cursor.close();
//
//        return qty;
//    }

    public static float getPreviousBill(SQLiteDatabase db, final String custId, final String day, final double quantity) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        float amount = 0;

        if (cursor.moveToFirst()) {
            do {
//                if (Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX))) > 0)
//                    amount = (((Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE))) * Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX))))
//                            / 100) * (float) quantity) - Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
//                else
//                    amount = (Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE))) * (float) quantity)
//                            - Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));


           /*jkhjkdfgj*/
//                float totalRate = Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE))) * (float) quantity;
//                if (Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX))) > 0)
//                    amount = ((totalRate * Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX))))
//                            / 100) + totalRate - Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
//                else
//                    amount = totalRate
//                            - Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));


                float totalRate = Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE))) * (float) quantity;
                if (Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX))) > 0)
                    amount = ((totalRate * Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.TAX))))
                            / 100) + totalRate;
                else
                    amount = totalRate;


            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return amount;
    }

    public static boolean isClearedBill(SQLiteDatabase db, final String custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'"
                + " AND " + TableColumns.IS_CLEARED + " ='1'";
        Cursor cursor = db.rawQuery(selectquery, null);

        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static float getTotalRate(SQLiteDatabase db, final String custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        float amount = 0;

        if (cursor.moveToFirst()) {
            do {

                amount = Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return amount;
    }

    public static String outstandingStatus(SQLiteDatabase db, final String custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String amount = "1";

        if (cursor.moveToFirst()) {
            do {

                amount = cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return amount;
    }

    public static String getPaymentMade(SQLiteDatabase db, final String custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >'" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String amount = "1";

        if (cursor.moveToFirst()) do {

            amount = cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE));

        }
        while (cursor.moveToNext());
        cursor.close();

        return amount;
    }

    public static ArrayList<VBill> getCustomersBill(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'" ;
        ArrayList<VBill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VBill holder = new VBill();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null)
                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)) != null)
                    holder.setIsCleared("false");
                if (cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)) != null)
                    holder.setPaymentMode(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));


                list.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    public static ArrayList<ExtcalVCustomersList> getCustomersBillToSync(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.SYNC_STATUS + " ='" + "0'" + " AND " + TableColumns.DIRTY + " ='0'";
        ArrayList<ExtcalVCustomersList> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                ExtcalVCustomersList holder = new ExtcalVCustomersList();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null)
                    holder.setEnd_date(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)) != null)
                    holder.setIsCleared("false");
                if (cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)) != null)
                    holder.setPaymentMade(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));


                list.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    public static void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "0");
        values.put(TableColumns.SYNC_STATUS, "0");

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
                + " AND " + TableColumns.DIRTY + " ='" + "1" + "'", null);
    }

    public static void updateData(SQLiteDatabase db, ExtcalVCustomersList holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.SYNC_STATUS, "0");
        values.put(TableColumns.IS_OUTSTANDING, "1");
        values.put(TableColumns.DIRTY, "0");
        values.put(TableColumns.QUANTITY, holder.getQuantity());

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.IS_OUTSTANDING + " ='1'", null);
    }

    public static void updateTotalQuantity(SQLiteDatabase db, String quantity, String payment, String custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.QUANTITY, quantity);
        values.put(TableColumns.BILL_MADE, payment);
        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.IS_OUTSTANDING
                + " ='1' AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >'" + Constants.getCurrentDate() + "'", null);
    }

    public static void updateOutstandingBill(SQLiteDatabase db, String custId, String day) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.IS_OUTSTANDING, "0");
        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'" +
                " AND " + TableColumns.END_DATE + " ='" + day + "'" + " AND "
                + TableColumns.IS_OUTSTANDING + " ='1'", null);
    }

    public static ArrayList<VBill> getHistoryBills(SQLiteDatabase db, String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.IS_OUTSTANDING + " ='" + "0'" + " AND "
                + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'";
        ArrayList<VBill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VBill holder = new VBill();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null)
                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)) != null)
                    holder.setIsCleared(cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)) != null)
                    holder.setPaymentMode(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)) != null)
                    holder.setIsOutstanding(cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)) != null)
                    holder.setBalanceType(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BILL_MADE)) != null)
                    holder.setBillMade(cursor.getString(cursor.getColumnIndex(TableColumns.BILL_MADE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                list.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    public static ArrayList<VBill> getOutstandingsBill(SQLiteDatabase db, String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.IS_OUTSTANDING + " ='" + "0'" + " AND "
                + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'";
        ArrayList<VBill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VBill holder = new VBill();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null)
                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)) != null)
                    holder.setIsCleared("1");
                if (cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)) != null)
                    holder.setPaymentMode(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)) != null)
                    holder.setIsOutstanding(cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)) != null)
                    holder.setBalanceType(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));

//                if (isHasBill(db, Constants.getCurrentDate()) == null) {
                    holder.setBillMade(getOutstandingBillMade(db, holder.getCustomerId(), holder.getStartDate()));
                    //Update billmade
                    BillTableManagement.updateBillMade(db, Constants.getCurrentDate(), holder.getBillMade(),custId);

//                }
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                list.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    public static VBill getTotalBill(SQLiteDatabase db, String custId, String deliveryStartDate) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >='"
                + deliveryStartDate + "' AND " + TableColumns.IS_OUTSTANDING + " ='1'";
        VBill holder = null;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            holder = new VBill();
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null) {
//                    String s = cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE));
//                    Calendar c = Calendar.getInstance();
//
//                    try {
//
//                        c.setTime(Constants.work_format.parse(s));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
////                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
//                    holder.setEndDate(c.get(Calendar.YEAR) + "-" + String.format("%02d", c.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", c.get(Calendar.DAY_OF_MONTH)));
//
//
//                }
                holder.setEndDate(Constants.getCurrentDate());
                Calendar cal = Calendar.getInstance();
                Calendar currentDate = Calendar.getInstance();
                double totalQty = 0;
                try {
                    Date d = Constants.work_format.parse(deliveryStartDate);
                    cal.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = cal.get(Calendar.DAY_OF_MONTH); i <= currentDate.get(Calendar.DAY_OF_MONTH); ++i) {
//                    totalQty += getQuatity(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
                    totalQty += Constants.getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), holder.getCustomerId());
                }
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
//                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                holder.setQuantity(String.valueOf(totalQty));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)) != null)
                    holder.setIsCleared("1");
                if (cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)) != null)
                    holder.setPaymentMode(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)) != null)
                    holder.setIsOutstanding(cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)) != null)
                    holder.setBalanceType(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
                double payMade = 0;
                if (holder.getBalanceType().equals("1"))
                    payMade = BillTableManagement.getPreviousBill(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            + Float.parseFloat(holder.getBalance());
                else
                    payMade = BillTableManagement.getPreviousBill(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            - Float.parseFloat(holder.getBalance());


                holder.setBillMade(String.valueOf(round(payMade, 2)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return holder;
    }
    public static ArrayList<VBill> getTotalBillList(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " +  TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >='"
                + Constants.getCurrentDate()+"'";
        ArrayList<VBill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
           VBill  holder = new VBill();
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)) != null)
                    holder.setAccountId(cursor.getString(cursor.getColumnIndex(TableColumns.ACCOUNT_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)) != null)
                    holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)) != null) {
//                    String s = cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE));
//                    Calendar c = Calendar.getInstance();
//
//                    try {
//
//                        c.setTime(Constants.work_format.parse(s));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
////                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
//                    holder.setEndDate(c.get(Calendar.YEAR) + "-" + String.format("%02d", c.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", c.get(Calendar.DAY_OF_MONTH)));
//
//
//                }
                holder.setEndDate(Constants.getCurrentDate());
                Calendar cal = Calendar.getInstance();
                Calendar currentDate = Calendar.getInstance();
                double totalQty = 0;
                try {
                    Date d = Constants.work_format.parse(holder.getStartDate());
                    cal.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = cal.get(Calendar.DAY_OF_MONTH); i <= currentDate.get(Calendar.DAY_OF_MONTH); ++i) {
//                    totalQty += getQuatity(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
                    totalQty += Constants.getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), holder.getCustomerId());
                }
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
//                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                holder.setQuantity(String.valueOf(totalQty));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
                    holder.setBalance(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)) != null)
                    holder.setIsCleared("1");
                if (cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)) != null)
                    holder.setPaymentMode(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)) != null)
                    holder.setIsOutstanding(cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)) != null)
                    holder.setBalanceType(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
                double payMade = 0;
                if (holder.getBalanceType().equals("1"))
                    payMade = BillTableManagement.getPreviousBill(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            + Float.parseFloat(holder.getBalance());
                else
                    payMade = BillTableManagement.getPreviousBill(db,  holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            - Float.parseFloat(holder.getBalance());


                holder.setBillMade(String.valueOf(round(payMade, 2)));

                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
                list.add(holder);

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }
    public static String getBillMade(SQLiteDatabase db, String custId, String deliveryStartDate) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >='"
                + deliveryStartDate + "' AND " + TableColumns.IS_OUTSTANDING + " ='1'";
        String billmade = null;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Calendar cal = Calendar.getInstance();
                Calendar currentDate = Calendar.getInstance();
                double totalQty = 0;
                try {
                    Date d = Constants.work_format.parse(deliveryStartDate);
                    cal.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = cal.get(Calendar.DAY_OF_MONTH); i <= currentDate.get(Calendar.DAY_OF_MONTH); ++i) {
//                    totalQty += getQuatity(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
                    totalQty += Constants.getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), custId);

                }
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
//                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                String type = cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE));
                double payMade = 0;
                if (type.equals("1"))
                    payMade = BillTableManagement.getPreviousBill(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            + Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                else
                    payMade = BillTableManagement.getPreviousBill(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            - Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));


                billmade = String.valueOf(round(payMade, 2));


            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return billmade;
    }
    public static String getOutstandingBillMade(SQLiteDatabase db, String custId, String deliveryStartDate) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE + " <='" + deliveryStartDate + "'" + " AND " + TableColumns.END_DATE + " >='"
                + deliveryStartDate + "' AND " + TableColumns.IS_OUTSTANDING + " ='0'";
        String billmade = null;

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Calendar cal = Calendar.getInstance();
                Calendar currentDate = Calendar.getInstance();
                double totalQty = 0;
                try {
                    Date d = Constants.work_format.parse(deliveryStartDate);
                    cal.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = cal.get(Calendar.DAY_OF_MONTH); i <= currentDate.get(Calendar.DAY_OF_MONTH); ++i) {
//                    totalQty += getQuatity(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i));
                    totalQty += Constants.getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), custId);

                }
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
//                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
                String type = cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE));
                double payMade = 0;
                if (type.equals("1"))
                    payMade = BillTableManagement.getPreviousBill(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            + Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
                else
                    payMade = BillTableManagement.getPreviousBill(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            - Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));


                billmade = String.valueOf(round(payMade, 2));


            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return billmade;
    }

    public static String getStartDatebyCustomerId(SQLiteDatabase db, String custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='" + custId + "'"
                + " AND " + TableColumns.IS_OUTSTANDING + " ='1' AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'";
        String startDate = "";

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)) != null)
                    startDate = cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return startDate;
    }

    public static BigDecimal round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public static void insertNewBills(SQLiteDatabase db, VBill holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.START_DATE, holder.getStartDate());
        values.put(TableColumns.END_DATE, holder.getEndDate());
        values.put(TableColumns.QUANTITY, holder.getQuantity());
        values.put(TableColumns.BALANCE, holder.getBalance());
        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.ADJUSTMENTS, "0");
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.IS_CLEARED, holder.getIsCleared());
        values.put(TableColumns.PAYMENT_MADE, "0");
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.IS_OUTSTANDING, "1");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.BILL_MADE, holder.getBillMade());
        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        db.insert(TableNames.TABLE_CUSTOMER_BILL, null, values);

    }

    public static String getBill(SQLiteDatabase db, final String custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'" + " AND " + TableColumns.IS_CLEARED + " ='1'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String amount = "";

        if (cursor.moveToFirst()) {
            do {

                amount = cursor.getString(cursor.getColumnIndex(TableColumns.BILL_MADE));

            }
            while (cursor.moveToNext());

        }
        cursor.close();
//        if (db.isOpen())
//            db.close();
        return amount;
    }
}
