package com.milky.service.legacy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.customers.CustomersBillingFragment;
import com.milky.ui.main.BillingFragment;
import com.milky.utils.Constants;
import com.milky.service.core.Bill;
import com.milky.service.core.Customers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Neha on 11/30/2015.
 */
public class BillTableManagement {
    public static void insertBillData(SQLiteDatabase db, Bill holder) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.ServerAccountId, holder.getAccountId());
        values.put(TableColumns.CustomerId, holder.getCustomerId());
        values.put(TableColumns.StartDate, holder.getStartDate());
        values.put(TableColumns.EndDate, holder.getEndDate());
        values.put(TableColumns.DefaultQuantity, holder.getQuantity());
//        values.put(TableColumns.Balance, holder.getBalance_amount());
        values.put(TableColumns.Balance, holder.getBalance());
        values.put(TableColumns.Adjustment, 0);
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.IsCleared, holder.getIsCleared());
        values.put(TableColumns.PaymentMade, holder.getPaymentMade());
        values.put(TableColumns.DateModified, holder.getDateModified());
        values.put(TableColumns.TotalAmount, holder.getTotalAmount());
        values.put(TableColumns.IsOutstanding, holder.getIsOutstanding());
        values.put(TableColumns.DateAdded, holder.getDateAdded());
        values.put(TableColumns.Dirty, 1);
        values.put(TableColumns.RollDate, holder.getRollDate());
//        values.put(TableColumns.DeletedOn, "1");
        long i = db.insert(TableNames.Bill, null, values);
    }

    public static boolean columnRollDateExists(SQLiteDatabase db) {

        String selectQuery = "SELECT * FROM " + TableNames.Bill;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getColumnIndex(TableColumns.RollDate) > 0;

        cursor.close();
        return result;
    }

    public static boolean columnRollDateExistsDeletedOn(SQLiteDatabase db) {

        String selectQuery = "SELECT * FROM " + TableNames.Bill;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getColumnIndex(TableColumns.DeletedOn) > 0;

        cursor.close();
        return result;
    }
//    public static void updateBillData(SQLiteDatabase db, VBill holder) {
//        ContentValues values = new ContentValues();
//        values.put(TableColumns.PaymentMade, holder.getPaymentMade());
//        values.put(TableColumns.Balance, holder.getBalance());
//        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
//        values.put(TableColumns.BILL_MADE, holder.getTotal());
//        long i = db.update(TableNames.Bill, values, TableColumns.ID + " ='" + holder.getCustomerId() + "'"
//                + " AND " + TableColumns.StartDate + " ='" + holder.getStartDate() + "'", null);
//        long j = i;
//    }

    public static Double isHasBill(SQLiteDatabase db, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.Bill + " WHERE "
                + TableColumns.EndDate + " >='" + day + "'"
                + " AND " + TableColumns.StartDate + " <='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        double bill = 0;

        if (cursor.moveToFirst()) {
            do {
                bill = cursor.getDouble(cursor.getColumnIndex(TableColumns.TotalAmount));


            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return bill;
    }

    public static void updateOutstandingBills(SQLiteDatabase db, String date) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.IsOutstanding, "0");
        values.put(TableColumns.IsCleared, "1");
        values.put(TableColumns.RollDate, GlobalSettingsService.getRollDate(db));
        long i = db.update(TableNames.Bill, values, TableColumns.StartDate + " <='" + date + "'", null);

    }

    public static void updateRollDate(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.RollDate, GlobalSettingsService.getRollDate(db));
        long i = db.update(TableNames.Bill, values, TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "' AND " +
                TableColumns.IsOutstanding + " ='1'", null);
    }

    public static void updateBillMade(SQLiteDatabase db, double date, double bill, int custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.TotalAmount, bill);
        long i = db.update(TableNames.Bill, values, TableColumns.CustomerId + " ='" + custId + "'" + " AND " + TableColumns.EndDate + " >='" + date + "'"
                + " AND " + TableColumns.StartDate + " <='" + date + "'", null);
    }

    public static void updateDeletedOn(SQLiteDatabase db, int custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DeletedOn, Constants.getCurrentDate());
        Calendar deletedCal = Calendar.getInstance();
        Calendar startCal = Calendar.getInstance();
        try {
            Date start = Constants.work_format.parse(getStartDatebyCustomerId(db, custId));
            Date date = Constants.work_format.parse(Constants.getCurrentDate());
            deletedCal.setTime(date);
            startCal.setTime(start);
            if (startCal.get(Calendar.YEAR) == deletedCal.get(Calendar.YEAR) && startCal.get(Calendar.MONTH) <= deletedCal.get(Calendar.MONTH)
                    && startCal.get(Calendar.DAY_OF_MONTH) == deletedCal.get(Calendar.DAY_OF_MONTH)) {
                values.put(TableColumns.EndDate, String.valueOf(deletedCal.get(Calendar.YEAR)) + "-" + String.format("%02d", deletedCal.get(Calendar.MONTH))
                        + "-" + String.format("%02d", deletedCal.get(Calendar.DAY_OF_MONTH) - 1));
            }
        } catch (ParseException pexp) {
        }
        long i = db.update(TableNames.Bill, values, TableColumns.CustomerId + " ='" + custId + "'"
                , null);
    }

//    public static void updateClearBills(SQLiteDatabase db, String date, int custid, Bill holder) {
//        ContentValues values = new ContentValues();
//        values.put(TableColumns.IsCleared, "0");
//        values.put(TableColumns.EndDate, date);
//        values.put(TableColumns.PaymentMade, holder.getPaymentMade());
//        values.put(TableColumns.Balance, holder.getBalance());
//        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
//        values.put(TableColumns.TotalAmount, holder.getTotalAmount());
////        values.put(TableColumns.DefaultRate, holder.getRate());
//        long i = db.update(TableNames.Bill, values, TableColumns.CustomerId + " ='" + custid + "'"
//                + " AND " + TableColumns.StartDate + " ='" + holder.getStartDate() + "' AND " + TableColumns.IsCleared + " ='1' AND " + TableColumns.IsOutstanding + " ='0'", null);
//    }

//    public static void updateCurrentDateData(SQLiteDatabase db, VCustomers holder) {
//        ContentValues values = new ContentValues();
//        values.put(TableColumns.DefaultRate, holder.getRate());
//        values.put(TableColumns.QUANTITY, holder.getQuantity());
//        values.put(TableColumns.EndDate, holder.getEnd_date());
//        values.put(TableColumns.AreaId, holder.getAreaId());
//        db.update(TableNames.Bill, values, TableColumns.CustomerId + " ='" + holder.getCustomerId() + "'"
//                + " AND " + TableColumns.StartDate + " ='" + holder.getStart_date() + "'", null);
//    }

    public static void updateEndDate(SQLiteDatabase db, Customers holder, String enddate, String updatedEndDate) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.EndDate, updatedEndDate);

        db.update(TableNames.Bill, values, TableColumns.CustomerId + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.EndDate + " ='" + enddate + "'", null);
    }

    public static boolean isHasDataForDay(SQLiteDatabase db, int custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.Bill + " WHERE "
                + TableColumns.CustomerId + " ='" + custId + "'" + " AND " + TableColumns.StartDate + " ='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public static boolean isToBeOutstanding(SQLiteDatabase db, int custId, String day) {
        String selectQuery = "SELECT * FROM " + TableNames.Bill + " WHERE "
                + TableColumns.CustomerId + " ='" + custId + "'" + " AND " + TableColumns.EndDate + " ='" + day + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

//    public static double getQuatity(SQLiteDatabase db, String id, String date) {
//        String selectquery =
//
//                "SELECT * FROM " + TableNames.Bill + " WHERE "
//                        + TableColumns.StartDate + " <='" + date + "'" + " AND " + TableColumns.EndDate
//                        + " >'" + date + "'" + " AND "
//                        + TableColumns.ID + " ='" + id + "' AND " + TableColumns.IsCleared + " ='1'";
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

    public static double getTotalAmount(SQLiteDatabase db, final int custId, final String day, final double quantity) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.CustomerId + " ='"
                + custId + "'"
                + " AND " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >='" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double amount = 0;
        if (cursor.moveToFirst()) {
            do {
                double totalRate = cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate)) * quantity;
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
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.CustomerId + " ='"
                + custId + "'"
                + " AND " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >='" + day + "'"
                + " AND " + TableColumns.IsCleared + " ='1'";
        Cursor cursor = db.rawQuery(selectquery, null);

        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static float getTotalRate(SQLiteDatabase db, final int custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.CustomerId + " ='"
                + custId + "'"
                + " AND " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >='" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        float amount = 0;
        if (cursor.moveToFirst()) {
            do {
                amount = Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return amount;
    }

    public static String outstandingStatus(SQLiteDatabase db, final int custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.CustomerId + " ='"
                + custId + "'"
                + " AND " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String amount = "1";
        if (cursor.moveToFirst()) {
            do {
                amount = cursor.getString(cursor.getColumnIndex(TableColumns.IsOutstanding));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return amount;
    }

    //Get Tax
    public static double getTax(SQLiteDatabase db, final int custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.CustomerId + " ='"
                + custId + "'"
                + " AND " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >='" + day + "'";
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
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.CustomerId + " ='"
                + custId + "'"
                + " AND " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >'" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String amount = "1";

        if (cursor.moveToFirst()) do {

            amount = cursor.getString(cursor.getColumnIndex(TableColumns.PaymentMade));

        }
        while (cursor.moveToNext());
        cursor.close();

        return amount;
    }

    public static ArrayList<Bill> getCustomersBill(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsCleared + " ='" + "0'";
        ArrayList<Bill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
                holder.setQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.Adjustment)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(1);
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));

                list.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

//    public static ArrayList<VCustomers> getCustomersBillToSync(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.SYNC_STATUS + " ='" + "0'" + " AND " + TableColumns.Dirty + " ='0'";
//        ArrayList<VCustomers> list = new ArrayList<>();
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                VCustomers holder = new VCustomers();
//                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ServerAccountId)));
//                holder.setStart_date(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//                holder.setEnd_date(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
//                holder.setQuantity(cursor.getString(cursor.getColumnIndex(TableColumns.QUANTITY)));
//                holder.setBalance_amount(cursor.getString(cursor.getColumnIndex(TableColumns.Balance)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Adjustment)) != null)
//                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.Adjustment)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
//                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.IsCleared)) != null)
//                    holder.setIsCleared("false");
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.PaymentMade)) != null)
//                    holder.setPaymentMade(cursor.getString(cursor.getColumnIndex(TableColumns.PaymentMade)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)) != null)
//                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)) != null)
//                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
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
        values.put(TableColumns.Dirty, "0");
//        values.put(TableColumns.SYNC_STATUS, "0");
//
//        db.update(TableNames.Bill, values, TableColumns.SYNC_STATUS + " ='" + "0" + "'"
//                + " AND " + TableColumns.Dirty + " ='" + "1" + "'", null);
    }

    public static void updateData(SQLiteDatabase db, Bill holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.Balance, holder.getBalance());
        values.put(TableColumns.IsOutstanding, 1);
        values.put(TableColumns.Dirty, 0);
        values.put(TableColumns.DefaultQuantity, holder.getQuantity());

        db.update(TableNames.Bill, values, TableColumns.CustomerId + " ='" + holder.getCustomerId() + "'"
                + " AND " + TableColumns.IsOutstanding + " ='1'", null);
    }

    public static void updateOutstandingBills(SQLiteDatabase db, double quantity, double payment, int custId) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DefaultQuantity, quantity);
        values.put(TableColumns.TotalAmount, payment);
        if (Constants.getCurrentDate().equals(GlobalSettingsService.getRollDate(db))) {
            values.put(TableColumns.EndDate, Constants.getCurrentDate());
            values.put(TableColumns.IsOutstanding, 0);
        }
        db.update(TableNames.Bill, values, TableColumns.CustomerId + " ='" + custId + "'" + " AND "
                + TableColumns.IsOutstanding + " ='1'  AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.EndDate + " >'" + Constants.getCurrentDate() + "'", null);
    }

    /* static ArrayList<Bill> getHistoryBills(SQLiteDatabase db, int custId) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE "
                + TableColumns.CustomerId + " ='" + custId + "'" + " AND " + TableColumns.IsOutstanding + " ='" + "0'" + " AND "
                + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'";
        ArrayList<Bill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
                holder.setQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.Adjustment)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(cursor.getInt(cursor.getColumnIndex(TableColumns.IsCleared)));
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IsOutstanding)));
//                holder.setBalanceType(cursor.getInt(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
                holder.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(TableColumns.TotalAmount)));
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
//                CustomersBillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }
*/

    public static ArrayList<Bill> getOutstandingsBill(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsCleared + " ='" + "1'"
                + " AND " + TableColumns.IsOutstanding + " ='" + "0'" + " AND "
                + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'";
        ArrayList<Bill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
                holder.setQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultQuantity)));
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.Adjustment)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(1);
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IsOutstanding)));
//                holder.setBalanceType(cursor.getInt(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
                holder.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(TableColumns.TotalAmount)));
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
//                BillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

//    public static ArrayList<Bill> getUnclearedBillById(SQLiteDatabase db, int id) {
//        String selectquery = "SELECT * FROM " + TableNames.Bill + " INNER JOIN " + TableNames.CUSTOMER
//                + " ON " + TableNames.Bill + "." + TableColumns.CustomerId + "=" + TableNames.CUSTOMER + "." + TableColumns.ID + " WHERE " + TableColumns.IsCleared + " ='" + "1'"
//                + " AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.EndDate + " >='"
//                + Constants.getCurrentDate() + "' AND " + TableColumns.IsOutstanding + " ='1' AND" +
//                " (" + TableColumns.DeletedOn + " ='1' OR " + TableColumns.DeletedOn + " >'" + Constants.getCurrentDate() + "')" +
//                " AND " + TableColumns.CustomerId + " ='" + id + "'";
//
//        ArrayList<Bill> list = new ArrayList<>();
//        Cursor cursor = db.rawQuery(selectquery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                Bill holder = new Bill();
//                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
//                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//                Calendar cal = Calendar.getInstance();
//                double totalQty = 0;
//                try {
//                    Date d = Constants.work_format.parse(holder.getStartDate());
//                    cal.setTime(d);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Calendar currentDate = Calendar.getInstance();
//                if ("1".equals(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)))) {
//                    holder.setEndDate(Constants.getCurrentDate());
//                    totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
//                            cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false);
//
//                } else {
//                    Calendar deletedCal = Calendar.getInstance();
//                    try {
//                        Date date = Constants.work_format.parse(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)));
//                        deletedCal.setTime(date);
//                        holder.setEndDate(String.valueOf(deletedCal.get(Calendar.YEAR)) + "-" + String.format("%02d", deletedCal.get(Calendar.MONTH))
//                                + "-" + String.format("%02d", deletedCal.get(Calendar.DAY_OF_MONTH) - 1));
//                        totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
//                                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false);//
////
//                    } catch (ParseException pexp) {
//
//                    }
//                }
//                holder.setQuantity(totalQty);
//                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
//                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.Adjustment)));
//                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
//                holder.setIsCleared(1);
//                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
//                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
//                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
//                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IsOutstanding)));
//                holder.setBalanceType(cursor.getInt(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
//                holder.setRate(new CustomersSettingService().getByCustId(holder.getCustomerId(), Constants.getCurrentDate()).getDefaultRate());
//                double billMade = 0;
//                if (holder.getBalanceType() == 1)
//                    billMade = BillTableManagement.getTotalAmount(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            + holder.getBalance();
//                else
//                    billMade = getTotalAmount(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            - holder.getBalance();
//
//
//                holder.setTotalAmount(billMade);
//
////                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
//                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
//                updateOutstandingBills(db, holder.getQuantity(), holder.getTotalAmount(), holder.getCustomerId());
////                BillingFragment.custIdsList.add(holder.getCustomerId());
//                CustomersBillingFragment.payment.add(holder);
//            }
//            while (cursor.moveToNext());
//
//
//        }
//        cursor.close();
//
//        return list;
//    }

//    public static ArrayList<Bill> getTotalBill(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.Bill + " INNER JOIN " + TableNames.CUSTOMER
//                + " ON " + TableNames.Bill + "." + TableColumns.CustomerId + "=" + TableNames.CUSTOMER + "." + TableColumns.ID + " WHERE " + TableColumns.IsCleared + " ='" + "1'"
//                + " AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.EndDate + " >='"
//                + Constants.getCurrentDate() + "' AND " + TableColumns.IsOutstanding + " ='1' AND" +
//                " (" + TableColumns.DeletedOn + " ='1' OR " + TableColumns.DeletedOn + " >'" + Constants.getCurrentDate() + "')";
//
//        ArrayList<Bill> list = new ArrayList<>();
//        Cursor cursor = db.rawQuery(selectquery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                Bill holder = new Bill();
//                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
//                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//                Calendar cal = Calendar.getInstance();
//                double totalQty = 0;
//                try {
//                    Date d = Constants.work_format.parse(holder.getStartDate());
//                    cal.setTime(d);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                Calendar currentDate = Calendar.getInstance();
//                if ("1".equals(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)))) {
//                    holder.setEndDate(Constants.getCurrentDate());
//                    totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
//                            cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false);
//
//                } else {
//                    Calendar deletedCal = Calendar.getInstance();
//                    try {
//                        Date date = Constants.work_format.parse(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)));
//                        deletedCal.setTime(date);
//                        holder.setEndDate(String.valueOf(deletedCal.get(Calendar.YEAR)) + "-" + String.format("%02d", deletedCal.get(Calendar.MONTH))
//                                + "-" + String.format("%02d", deletedCal.get(Calendar.DAY_OF_MONTH) - 1));
//                        totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
//                                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false);//
////
//                    } catch (ParseException pexp) {
//
//                    }
//
//
//                }
//                holder.setQuantity(totalQty);
//                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
//                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.Adjustment)));
//                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
//                holder.setIsCleared(1);
//                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
//                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
//                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
//                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IsOutstanding)));
//                holder.setBalanceType(cursor.getInt(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
//                holder.setRate(new CustomersSettingService().getByCustId(holder.getCustomerId(), Constants.getCurrentDate()).getDefaultRate());
//                double billMade = 0;
//                if (holder.getBalanceType() == 1)
//                    billMade = getTotalAmount(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            + holder.getBalance();
//                else
//                    billMade = getTotalAmount(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            - holder.getBalance();
//
//
//                holder.setTotalAmount(billMade);
//
////                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
//                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
//                updateOutstandingBills(db, holder.getQuantity(), holder.getTotalAmount(), holder.getCustomerId());
////                BillingFragment.custIdsList.add(holder.getCustomerId());
//                BillingFragment.payment.add(holder);
//            }
//            while (cursor.moveToNext());
//
//
//        }
//        cursor.close();
//
//        return list;
//    }
//    TODO COMMENTED

//    public static void getAndUpdateTotalBill(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsCleared + " ='" + "1'"
//                + " AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.EndDate + " >='"
//                + Constants.getCurrentDate() + "' AND " + TableColumns.IsOutstanding + " ='1'";
//
//        Cursor cursor = db.rawQuery(selectquery, null);
//        if (cursor.moveToFirst()) {
//            do {
//                VBill holder = new VBill();
//                holder.setCustomerId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
//                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//                holder.setDeletedOn(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)));
//                holder.setBalance(cursor.getString(cursor.getColumnIndex(TableColumns.Balance)));
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
//                holder.setPaymentMade(cursor.getString(cursor.getColumnIndex(TableColumns.PaymentMade)));
//                holder.setBalanceType(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
//                double payMade = 0;
//                if (holder.getBalanceType().equals("1"))
//                    payMade = BillTableManagement.getTotalAmount(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            + Float.parseFloat(holder.getBalance());
//                else
//                    payMade = BillTableManagement.getTotalAmount(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            - Float.parseFloat(holder.getBalance());
//                holder.setBillMade(String.valueOf(Constants.round(payMade, 2)));
//                holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate)));
//                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
//                updateOutstandingBills(db, holder.getQuantity(), holder.getBillMade(), holder.getCustomerId());
////                BillingFragment.custIdsList.add(holder.getCustomerId());
//            }
//            while (cursor.moveToNext());
//        }
//        cursor.close();
//    }

    public static ArrayList<Bill> getTotalBillList(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsCleared + " ='" + "1'"
                + " AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.EndDate + " >='"
                + Constants.getCurrentDate() + "'";
        ArrayList<Bill> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            Bill holder = new Bill();
            do {
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)) != null)
                    holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)) != null) {
//                    String s = cursor.getString(cursor.getColumnIndex(TableColumns.EndDate));
//                    Calendar c = Calendar.getInstance();
//
//                    try {
//
//                        c.setTime(Constants.work_format.parse(s));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
////                    holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
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
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Balance)) != null)
//                    holder.setBalance(cursor.getString(cursor.getColumnIndex(TableColumns.Balance)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Adjustment)) != null)
//                    holder.setAdjustment(cursor.getString(cursor.getColumnIndex(TableColumns.Adjustment)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.TAX)) != null)
//                    holder.setTax(cursor.getString(cursor.getColumnIndex(TableColumns.TAX)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.IsCleared)) != null)
//                    holder.setIsCleared("1");
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.PaymentMade)) != null)
//                    holder.setPaymentMade(cursor.getString(cursor.getColumnIndex(TableColumns.PaymentMade)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)) != null)
//                    holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)) != null)
//                    holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.IsOutstanding)) != null)
//                    holder.setIsOutstanding(cursor.getString(cursor.getColumnIndex(TableColumns.IsOutstanding)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)) != null)
//                    holder.setBalanceType(cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE)));
//                double payMade = 0;
//                if (holder.getBalanceType().equals("1"))
//                    payMade = BillTableManagement.getTotalAmount(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            + Float.parseFloat(holder.getBalance());
//                else
//                    payMade = BillTableManagement.getTotalAmount(db, holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            - Float.parseFloat(holder.getBalance());
//
//
//                holder.setBillMade(String.valueOf(Constants.round(payMade, 2)));
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate)) != null)
//                    holder.setRate(cursor.getString(cursor.getColumnIndex(TableColumns.DefaultRate)));
//                list.add(holder);

            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    public static String getBillMade(SQLiteDatabase db, int custId, String deliveryStartDate) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsCleared + " ='" + "1'"
                + " AND " + TableColumns.CustomerId + " ='" + custId + "'" + " AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.EndDate + " >='"
                + deliveryStartDate + "' AND " + TableColumns.IsOutstanding + " ='1'";
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
//                String type = cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE));
                double payMade = 0;
//                if (type.equals("1"))
//                    payMade = getTotalAmount(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            + Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.Balance)));
//                else
//                    payMade = getTotalAmount(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
//                            - Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.Balance)));


                billmade = String.valueOf(Constants.round(payMade, 2));


            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return billmade;
    }

    public static String getOutstandingBillMade(SQLiteDatabase db, int custId, String deliveryStartDate) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsCleared + " ='" + "1'"
                + " AND " + TableColumns.CustomerId + " ='" + custId + "'" + " AND " + TableColumns.StartDate + " <='" + deliveryStartDate + "'" + " AND " + TableColumns.EndDate + " >='"
                + deliveryStartDate + "' AND " + TableColumns.IsOutstanding + " ='0'";
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
//                String type = cursor.getString(cursor.getColumnIndex(TableColumns.BALANCE_TYPE));
                double payMade = 0;
//                if (type.equals("1"))
                    payMade = getTotalAmount(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            + Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.Balance)));
//                else
                    payMade = getTotalAmount(db, custId, cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            - Float.parseFloat(cursor.getString(cursor.getColumnIndex(TableColumns.Balance)));


                billmade = String.valueOf(Constants.round(payMade, 2));


            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return billmade;
    }

    public static String getStartDatebyCustomerId(SQLiteDatabase db, int custId) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.CustomerId + " ='" + custId + "'"
                + " AND " + TableColumns.IsOutstanding + " ='1' AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'";
        String startDate = "";

        Cursor cursor = db.rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)) != null)
                    startDate = cursor.getString(cursor.getColumnIndex(TableColumns.StartDate));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return startDate;
    }


    public static void insertNewBills(SQLiteDatabase db, Bill holder) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.ServerAccountId, holder.getAccountId());
        values.put(TableColumns.CustomerId, holder.getCustomerId());
        values.put(TableColumns.StartDate, holder.getStartDate());
        values.put(TableColumns.EndDate, holder.getEndDate());
        values.put(TableColumns.DefaultQuantity, holder.getQuantity());
        values.put(TableColumns.Balance, holder.getBalance());
//        values.put(TableColumns.DefaultRate, holder.getRate());
        values.put(TableColumns.Adjustment, "0");
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.IsCleared, holder.getIsCleared());
        values.put(TableColumns.PaymentMade, "0");
        values.put(TableColumns.DateModified, holder.getDateModified());
//        values.put(TableColumns.SYNC_STATUS, "1");
        values.put(TableColumns.IsOutstanding, "1");
        values.put(TableColumns.Dirty, "1");
        values.put(TableColumns.TotalAmount, holder.getTotalAmount());
//        values.put(TableColumns.BALANCE_TYPE, holder.getBalanceType());
        db.insert(TableNames.Bill, null, values);

    }

    public static double getBill(SQLiteDatabase db, final int custId, final String day) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.CustomerId + " ='"
                + custId + "'" + " AND " + TableColumns.IsCleared + " ='1'"
                + " AND " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >='" + day + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        double amount = 0;

        if (cursor.moveToFirst()) {
            do {

                amount = cursor.getDouble(cursor.getColumnIndex(TableColumns.TotalAmount));

            }
            while (cursor.moveToNext());

        }
        cursor.close();
//        if (db.isOpen())
//            db.close();
        return amount;
    }

}
