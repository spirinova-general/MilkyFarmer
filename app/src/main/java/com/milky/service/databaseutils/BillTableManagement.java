package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.ui.customers.CustomersBillingFragment;
import com.milky.ui.main.BillingFragment;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VBill;
import com.milky.viewmodel.VCustomers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Neha on 11/30/2015.
 */
public class BillTableManagement {
    public static void insertBillData(SQLiteDatabase db, VBill holder) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.SERVER_ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.START_DATE, holder.getStartDate());
        values.put(TableColumns.END_DATE, holder.getEndDate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
//        values.put(TableColumns.BALANCE, holder.getBalance_amount());
        values.put(TableColumns.BALANCE, holder.getBalance());
        values.put(TableColumns.ADJUSTMENTS, 0);
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.IS_CLEARED, holder.getIsCleared());
        values.put(TableColumns.PAYMENT_MADE, holder.getPaymentMade());
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.TOTAL_AMOUNT, holder.getTotalAmount());
        values.put(TableColumns.IS_OUTSTANDING, holder.getIsOutstanding());
        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        values.put(TableColumns.DATE_ADDED, holder.getDateAdded());
        values.put(TableColumns.DIRTY, 1);
        values.put(TableColumns.ROLL_DATE, holder.getRollDate());
//        values.put(TableColumns.DELETED_ON, "1");
        long i = db.insert(TableNames.TABLE_CUSTOMER_BILL, null, values);
    }

    public static boolean columnRollDateExists(SQLiteDatabase db) {

        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getColumnIndex(TableColumns.ROLL_DATE) > 0;

        cursor.close();
        return result;
    }

    public static boolean columnRollDateExistsDeletedOn(SQLiteDatabase db) {

        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getColumnIndex(TableColumns.DELETED_ON) > 0;

        cursor.close();
        return result;
    }
//    public static void updateBillData(SQLiteDatabase db, VBill holder) {
//        ContentValues values = new ContentValues();
//        values.put(TableColumns.PAYMENT_MADE, holder.getPaymentMade());
//        values.put(TableColumns.BALANCE, holder.getBalance());
//        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
//        values.put(TableColumns.BILL_MADE, holder.getTotal());
//        long i = db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.ID + " ='" + holder.getCustomerId() + "'"
//                + " AND " + TableColumns.START_DATE + " ='" + holder.getStartDate() + "'", null);
//        long j = i;
//    }

    public static Double isHasBill(SQLiteDatabase db, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE "
                + TableColumns.END_DATE + " >='" + day + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        double bill = 0;

        if (cursor.moveToFirst()) {
            do {
                bill = cursor.getDouble(cursor.getColumnIndex(TableColumns.TOTAL_AMOUNT));


            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return bill;
    }

    public static void updateOutstandingBills(SQLiteDatabase db, String date) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.IS_OUTSTANDING, "0");
        values.put(TableColumns.IS_CLEARED, "1");
        values.put(TableColumns.ROLL_DATE, GlobalSettingsService.getRollDate(db));
        long i = db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.START_DATE + " <='" + date + "'", null);

    }

    public static void updateRollDate(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ROLL_DATE, GlobalSettingsService.getRollDate(db));
        long i = db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "' AND " +
                TableColumns.IS_OUTSTANDING + " ='1'", null);
    }

    public static void updateBillMade(SQLiteDatabase db, double date, double bill, int custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.TOTAL_AMOUNT, bill);
        long i = db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.END_DATE + " >='" + date + "'"
                + " AND " + TableColumns.START_DATE + " <='" + date + "'", null);
    }

    public static void updateDeletedOn(SQLiteDatabase db, int custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DELETED_ON, Constants.getCurrentDate());
        Calendar deletedCal = Calendar.getInstance();
        Calendar startCal = Calendar.getInstance();
        try {
            Date start = Constants.work_format.parse(CustomersTableMagagement.getStartDatebyCustomerId(db, custId));
            Date date = Constants.work_format.parse(Constants.getCurrentDate());
            deletedCal.setTime(date);
            startCal.setTime(start);
            if (startCal.get(Calendar.YEAR) == deletedCal.get(Calendar.YEAR) && startCal.get(Calendar.MONTH) <= deletedCal.get(Calendar.MONTH)
                    && startCal.get(Calendar.DAY_OF_MONTH) == deletedCal.get(Calendar.DAY_OF_MONTH)) {
                values.put(TableColumns.END_DATE, String.valueOf(deletedCal.get(Calendar.YEAR)) + "-" + String.format("%02d", deletedCal.get(Calendar.MONTH))
                        + "-" + String.format("%02d", deletedCal.get(Calendar.DAY_OF_MONTH) - 1));
            }
        } catch (ParseException pexp) {
        }
        long i = db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'"
                , null);
    }

    public static void updateClearBills(SQLiteDatabase db, String date, int custid, VBill holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.IS_CLEARED, "0");
        values.put(TableColumns.END_DATE, date);
        values.put(TableColumns.PAYMENT_MADE, holder.getPaymentMade());
        values.put(TableColumns.BALANCE, holder.getBalance());
        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        values.put(TableColumns.TOTAL_AMOUNT, holder.getTotalAmount());
//        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        long i = db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + custid + "'"
                + " AND " + TableColumns.START_DATE + " ='" + holder.getStartDate() + "' AND " + TableColumns.IS_CLEARED + " ='1' AND " + TableColumns.IS_OUTSTANDING + " ='0'", null);
    }

//    public static void updateCurrentDateData(SQLiteDatabase db, VCustomers holder) {
//        ContentValues values = new ContentValues();
//        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
//        values.put(TableColumns.QUANTITY, holder.getQuantity());
//        values.put(TableColumns.END_DATE, holder.getEnd_date());
//        values.put(TableColumns.AREA_ID, holder.getAreaId());
//        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
//                + " AND " + TableColumns.START_DATE + " ='" + holder.getStart_date() + "'", null);
//    }

    public static void updateEndDate(SQLiteDatabase db, VCustomers holder, String enddate, String updatedEndDate) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.END_DATE, updatedEndDate);

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.END_DATE + " ='" + enddate + "'", null);
    }

    public static boolean isHasDataForDay(SQLiteDatabase db, int custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.START_DATE + " ='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public static boolean isToBeOutstanding(SQLiteDatabase db, int custId, String day) {
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
//                        + TableColumns.ID + " ='" + id + "' AND " + TableColumns.IS_CLEARED + " ='1'";
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

    public static double getPreviousBill(SQLiteDatabase db, final int custId, final String day, final double quantity) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double amount = 0;
        if (cursor.moveToFirst()) {
            do {
                double totalRate = cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) * quantity;
                if (getTax(db, custId, day) > 0)
                    amount = ((totalRate * getTax(db, custId, day))
                            / 100) + totalRate;
                else
                    amount = totalRate;
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return amount;
    }

    public static boolean isClearedBill(SQLiteDatabase db, final int custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'"
                + " AND " + TableColumns.IS_CLEARED + " ='1'";
        Cursor cursor = db.rawQuery(selectquery, null);

        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static float getTotalRate(SQLiteDatabase db, final int custId, final String day) {
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

    public static String outstandingStatus(SQLiteDatabase db, final int custId, final String day) {
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

    //Get Tax
    public static double getTax(SQLiteDatabase db, final int custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double amount = 0;
        if (cursor.moveToFirst()) {
            do {
                amount = cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return amount;
    }

    public static String getPaymentMade(SQLiteDatabase db, final int custId, final String day) {
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
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'";
        ArrayList<VBill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VBill holder = new VBill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                holder.setQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.BALANCE)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(1);
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));

                list.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

//    public static ArrayList<VCustomers> getCustomersBillToSync(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.SYNC_STATUS + " ='" + "0'" + " AND " + TableColumns.DIRTY + " ='0'";
//        ArrayList<VCustomers> list = new ArrayList<>();
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                VCustomers holder = new VCustomers();
//                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.SERVER_ACCOUNT_ID)));
//                holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
//                holder.setEnd_date(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
//                holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
//                holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
//                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
//                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)) != null)
//                    holder.setIsCleared("false");
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)) != null)
//                    holder.setPaymentMade(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
//                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
//                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
//
//
//                list.add(holder);
//            }
//            while (cursor.moveToNext());
//
//
//        }
//        cursor.close();
//
//        return list;
//    }

    public static void updateSyncedData(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "0");
        values.put(TableColumns.SYNC_STATUS, "0");

        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
                + " AND " + TableColumns.DIRTY + " ='" + "1" + "'", null);
    }

//    public static void updateData(SQLiteDatabase db, VCustomers holder) {
//        ContentValues values = new ContentValues();
//        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
//        values.put(TableColumns.BALANCE, holder.getBalance_amount());
//        values.put(TableColumns.SYNC_STATUS, "0");
//        values.put(TableColumns.IS_OUTSTANDING, "1");
//        values.put(TableColumns.DIRTY, "0");
//        values.put(TableColumns.QUANTITY, holder.getQuantity());
//
//        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + holder.getCustomerId() + "'"
//                + " AND " + TableColumns.IS_OUTSTANDING + " ='1'", null);
//    }

    public static void updateOutstandingBills(SQLiteDatabase db, double quantity, double payment, int custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_QUANTITY, quantity);
        values.put(TableColumns.TOTAL_AMOUNT, payment);
        if (Constants.getCurrentDate().equals(GlobalSettingsService.getRollDate(db))) {
            values.put(TableColumns.END_DATE, Constants.getCurrentDate());
            values.put(TableColumns.IS_OUTSTANDING, 0);
        }
        db.update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND "
                + TableColumns.IS_OUTSTANDING + " ='1'  AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >'" + Constants.getCurrentDate() + "'", null);
    }

    public static ArrayList<VBill> getHistoryBills(SQLiteDatabase db, int custId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE "
                + TableColumns.CUSTOMER_ID + " ='" + custId + "'" + " AND " + TableColumns.IS_OUTSTANDING + " ='" + "0'" + " AND "
                + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'";
        ArrayList<VBill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VBill holder = new VBill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                holder.setQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.BALANCE)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(cursor.getInt(cursor.getColumnIndex(TableColumns.IS_CLEARED)));
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)));
                holder.setBalanceType(cursor.getInt(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
                holder.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(TableColumns.TOTAL_AMOUNT)));
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.ROLL_DATE)));
                CustomersBillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }


    public static ArrayList<VBill> getOutstandingsBill(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.IS_OUTSTANDING + " ='" + "0'" + " AND "
                + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'";
        ArrayList<VBill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                VBill holder = new VBill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.END_DATE)));
                holder.setQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_QUANTITY)));
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.BALANCE)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(1);
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)));
                holder.setBalanceType(cursor.getInt(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
                holder.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(TableColumns.TOTAL_AMOUNT)));
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.ROLL_DATE)));
                BillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    public static ArrayList<VBill> getTotalBillById(SQLiteDatabase db, int id) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >='"
                + Constants.getCurrentDate() + "' AND " + TableColumns.IS_OUTSTANDING + " ='1' AND " + TableColumns.CUSTOMER_ID + " ='" + id + "'";
        ArrayList<VBill> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                VBill holder = new VBill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));

                String deletionDate = CustomersTableMagagement.getCustomerDeletionDate(db, holder.getCustomerId());
                if ("1".equals(deletionDate))
                    holder.setEndDate(Constants.getCurrentDate());
                else
                    holder.setEndDate(deletionDate);
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
//                    totalQty += Constants.getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), holder.getCustomerId());
                }
                holder.setQuantity(totalQty);
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.BALANCE)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(1);
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)));
                holder.setBalanceType(cursor.getInt(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
                double payMade = 0;
                if (holder.getBalanceType() == 1)
                    payMade = BillTableManagement.getPreviousBill(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            + holder.getBalance();
                else
                    payMade = BillTableManagement.getPreviousBill(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            - holder.getBalance();
                holder.setTotalAmount(payMade);
//                holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
//                updateOutstandingBills(db, holder.getQuantity(), holder.getTotal(), holder.getCustomerId());
//                BillingFragment.custIdsList.add(holder.getCustomerId());
                CustomersBillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    public static ArrayList<VBill> getTotalBill(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >='"
                + Constants.getCurrentDate() + "' AND " + TableColumns.IS_OUTSTANDING + " ='1'";

        ArrayList<VBill> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                VBill holder = new VBill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
                Calendar cal = Calendar.getInstance();
                double totalQty = 0;
                try {
                    Date d = Constants.work_format.parse(holder.getStartDate());
                    cal.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar currentDate = Calendar.getInstance();
                String deleted = CustomersTableMagagement.getCustomerDeletionDate(db, holder.getCustomerId());
                if ("1".equals(deleted)) {
                    holder.setEndDate(Constants.getCurrentDate());
                    for (int i = cal.get(Calendar.DAY_OF_MONTH); i <= currentDate.get(Calendar.DAY_OF_MONTH); ++i)
                        totalQty += getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), holder.getCustomerId(), deleted);

                } else {
                    Calendar deletedCal = Calendar.getInstance();
                    try {
                        Date date = Constants.work_format.parse(deleted);
                        deletedCal.setTime(date);
                        holder.setEndDate(String.valueOf(deletedCal.get(Calendar.YEAR)) + "-" + String.format("%02d", deletedCal.get(Calendar.MONTH))
                                + "-" + String.format("%02d", deletedCal.get(Calendar.DAY_OF_MONTH) - 1));
                        for (int i = cal.get(Calendar.DAY_OF_MONTH); i <= deletedCal.get(Calendar.DAY_OF_MONTH) - 1; ++i)
                            totalQty += getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), holder.getCustomerId(), deleted);


                    } catch (ParseException pexp) {

                    }


                }


                holder.setQuantity(totalQty);
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.BALANCE)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(1);
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)));
                holder.setBalanceType(cursor.getInt(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
                holder.setRate(CustomerSettingTableManagement.getRateByCustomerId(db, holder.getCustomerId()));
                double billMade = 0;
                if (holder.getBalanceType() == 1)
                    billMade = BillTableManagement.getPreviousBill(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            + holder.getBalance();
                else
                    billMade = BillTableManagement.getPreviousBill(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            - holder.getBalance();


                holder.setTotalAmount(billMade);

//                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.ROLL_DATE)));
                updateOutstandingBills(db, holder.getQuantity(), holder.getTotalAmount(), holder.getCustomerId());
//                BillingFragment.custIdsList.add(holder.getCustomerId());
                BillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }
//    TODO COMMENTED
//    public static void getAndUpdateTotalBill(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
//                + " AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >='"
//                + Constants.getCurrentDate() + "' AND " + TableColumns.IS_OUTSTANDING + " ='1'";
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                VBill holder = new VBill();
//                holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
//                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.START_DATE)));
//                holder.setDeletedOn(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
//                holder.setBalance(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
//                Calendar cal = Calendar.getInstance();
//                Calendar currentDate = Calendar.getInstance();
//                double totalQty = 0;
//                try {
//                    Date d = Constants.work_format.parse(holder.getStartDate());
//                    cal.setTime(d);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                if ("1".equals(holder.getDeletedOn())) {
//                    holder.setEndDate(Constants.getCurrentDate());
////                    for (int i = cal.get(Calendar.DAY_OF_MONTH); i <= currentDate.get(Calendar.DAY_OF_MONTH); ++i)
////                        totalQty += Constants.getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i-1), holder.getCustomerId());
//                } else {
//                    Calendar deletedCal = Calendar.getInstance();
//                    try {
//                        Date date = Constants.work_format.parse(holder.getDeletedOn());
//                        deletedCal.setTime(date);
//                        holder.setEndDate(String.valueOf(deletedCal.get(Calendar.YEAR)) + "-" + String.format("%02d", deletedCal.get(Calendar.MONTH))
//                                + "-" + String.format("%02d", deletedCal.get(Calendar.DAY_OF_MONTH) - 1));
////                        for (int i = cal.get(Calendar.DAY_OF_MONTH); i <= deletedCal.get(Calendar.DAY_OF_MONTH) - 1; ++i)
////                            totalQty += Constants.getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), holder.getCustomerId());
//                    } catch (ParseException pexp) {
//                    }
//                }
//                holder.setQuantity(String.valueOf(totalQty));
//                holder.setPaymentMade(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
//                holder.setBalanceType(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
//                double payMade = 0;
//                if (holder.getBalanceType().equals("1"))
//                    payMade = BillTableManagement.getPreviousBill(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            + Float.parseFloat(holder.getBalance());
//                else
//                    payMade = BillTableManagement.getPreviousBill(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            - Float.parseFloat(holder.getBalance());
//                holder.setBillMade(String.valueOf(Constants.round(payMade, 2)));
//                holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
//                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.ROLL_DATE)));
//                updateOutstandingBills(db, holder.getQuantity(), holder.getBillMade(), holder.getCustomerId());
////                BillingFragment.custIdsList.add(holder.getCustomerId());
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//    }

    public static ArrayList<VBill> getTotalBillList(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >='"
                + Constants.getCurrentDate() + "'";
        ArrayList<VBill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            VBill holder = new VBill();
            do {
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CUSTOMER_ID)));
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
//                    totalQty += Constants.getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), holder.getCustomerId());
                }
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)) != null)
//                    holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));

//                TODO COMMENTED

//                holder.setQuantity(String.valueOf(totalQty));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)) != null)
//                    holder.setBalance(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)) != null)
//                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.ADJUSTMENTS)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
//                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_CLEARED)) != null)
//                    holder.setIsCleared("1");
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)) != null)
//                    holder.setPaymentMade(cursor.getString(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)) != null)
//                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)) != null)
//                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)) != null)
//                    holder.setIsOutstanding(cursor.getString(cursor.getColumnIndex(TableColumns.IS_OUTSTANDING)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)) != null)
//                    holder.setBalanceType(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
//                double payMade = 0;
//                if (holder.getBalanceType().equals("1"))
//                    payMade = BillTableManagement.getPreviousBill(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            + Float.parseFloat(holder.getBalance());
//                else
//                    payMade = BillTableManagement.getPreviousBill(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            - Float.parseFloat(holder.getBalance());
//
//
//                holder.setBillMade(String.valueOf(Constants.round(payMade, 2)));
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) != null)
//                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)));
//                list.add(holder);

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    public static String getBillMade(SQLiteDatabase db, int custId, String deliveryStartDate) {
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
//                    totalQty += Constants.getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), custId);

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


                billmade = String.valueOf(Constants.round(payMade, 2));


            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return billmade;
    }

    public static String getOutstandingBillMade(SQLiteDatabase db, int custId, String deliveryStartDate) {
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
//                    totalQty += Constants.getQtyOfCustomer(cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", i), custId);

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


                billmade = String.valueOf(Constants.round(payMade, 2));


            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return billmade;
    }

    public static String getStartDatebyCustomerId(SQLiteDatabase db, int custId) {
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


    public static void insertNewBills(SQLiteDatabase db, VBill holder) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.SERVER_ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.START_DATE, holder.getStartDate());
        values.put(TableColumns.END_DATE, holder.getEndDate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
        values.put(TableColumns.BALANCE, holder.getBalance());
//        values.put(TableColumns.DEFAULT_RATE, holder.getRate());
        values.put(TableColumns.ADJUSTMENTS, "0");
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.IS_CLEARED, holder.getIsCleared());
        values.put(TableColumns.PAYMENT_MADE, "0");
        values.put(TableColumns.DATE_MODIFIED, holder.getDateModified());
        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.IS_OUTSTANDING, "1");
        values.put(TableColumns.DIRTY, "1");
        values.put(TableColumns.TOTAL_AMOUNT, holder.getTotalAmount());
        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        db.insert(TableNames.TABLE_CUSTOMER_BILL, null, values);

    }

    public static double getBill(SQLiteDatabase db, final int custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'" + " AND " + TableColumns.IS_CLEARED + " ='1'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double amount = 0;

        if (cursor.moveToFirst()) {
            do {

                amount = cursor.getDouble(cursor.getColumnIndex(TableColumns.TOTAL_AMOUNT));

            }
            while (cursor.moveToNext());

        }
        cursor.close();
//        if (db.isOpen())
//            db.close();
        return amount;
    }

    //Get quantity
    public static double getQtyOfCustomer(String day, int custId, String deletedOn) {
        double qty = 0;
        DatabaseHelper db = AppUtil.getInstance().getDatabaseHandler();
        if (db.isTableNotEmpty(TableNames.TABLE_DELIVERY)) {
            qty = DeliveryTableManagement.getQuantityOfDayByDateForCustomer(db.getReadableDatabase(), day, custId);
            if (!DeliveryTableManagement.isFromDelivery && db.isTableNotEmpty("customers")) {
                qty = CustomerSettingTableManagement.getAllCustomersByCustId(db.getReadableDatabase(), day
                        , custId);
            }
        } else if (db.isTableNotEmpty(TableNames.TABLE_CUSTOMER_SETTINGS)) {
            qty = CustomerSettingTableManagement.getAllCustomersByCustId(db.getReadableDatabase(), day
                    , custId);
        }
        DeliveryTableManagement.isFromDelivery = false;

        return qty;
    }
}
