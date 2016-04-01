package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Bill;
import com.milky.service.databaseutils.*;
import com.milky.service.databaseutils.serviceinterface.IBill;
import com.milky.ui.customers.CustomersBillingFragment;
import com.milky.ui.main.BillingFragment;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BillService implements IBill {
    @Override
    public void insert(Bill holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CUSTOMER_ID, holder.getCustomerId());
        values.put(TableColumns.START_DATE, holder.getStartDate());
        values.put(TableColumns.END_DATE, holder.getEndDate());
        values.put(TableColumns.DEFAULT_QUANTITY, holder.getQuantity());
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
        getDb().insert(TableNames.TABLE_CUSTOMER_BILL, null, values);
    }

    @Override
    public void update(Bill bill) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CUSTOMER_ID, bill.getCustomerId());
        values.put(TableColumns.START_DATE, bill.getStartDate());
        values.put(TableColumns.END_DATE, bill.getEndDate());
        values.put(TableColumns.DEFAULT_QUANTITY, bill.getQuantity());
        values.put(TableColumns.BALANCE, bill.getBalance());
        values.put(TableColumns.ADJUSTMENTS, 0);
        values.put(TableColumns.TAX, bill.getTax());
        values.put(TableColumns.IS_CLEARED, bill.getIsCleared());
        values.put(TableColumns.PAYMENT_MADE, bill.getPaymentMade());
        values.put(TableColumns.DATE_MODIFIED, bill.getDateModified());
        values.put(TableColumns.TOTAL_AMOUNT, bill.getTotalAmount());
        values.put(TableColumns.IS_OUTSTANDING, bill.getIsOutstanding());
        values.put(TableColumns.BALANCE_TYPE, bill.getBalanceType());
        values.put(TableColumns.DATE_ADDED, bill.getDateAdded());
        values.put(TableColumns.DIRTY, 1);
        values.put(TableColumns.ROLL_DATE, bill.getRollDate());
        getDb().update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + bill.getCustomerId() + "'", null);
    }

    @Override
    public List<Bill> getTotalAllBill() {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " INNER JOIN " + TableNames.TABLE_CUSTOMER
                + " ON " + TableNames.TABLE_CUSTOMER_BILL + "." + TableColumns.CUSTOMER_ID + "=" + TableNames.TABLE_CUSTOMER + "." + TableColumns.ID + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >='"
                + Constants.getCurrentDate() + "' AND " + TableColumns.IS_OUTSTANDING + " ='1' AND" +
                " (" + TableColumns.DELETED_ON + " ='1' OR " + TableColumns.DELETED_ON + " >'" + Constants.getCurrentDate() + "')";

        ArrayList<Bill> list = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
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
                if ("1".equals(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)))) {
                    holder.setEndDate(Constants.getCurrentDate());
                    totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
                            cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false);

                } else {
                    Calendar deletedCal = Calendar.getInstance();
                    try {
                        Date date = Constants.work_format.parse(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
                        deletedCal.setTime(date);
                        holder.setEndDate(String.valueOf(deletedCal.get(Calendar.YEAR)) + "-" + String.format("%02d", deletedCal.get(Calendar.MONTH))
                                + "-" + String.format("%02d", deletedCal.get(Calendar.DAY_OF_MONTH) - 1));
                        totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
                                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false);//
//
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
                holder.setRate(new CustomersSettingService().getByCustId(holder.getCustomerId(), Constants.getCurrentDate()).getDefaultRate());
                double billMade = 0;
                if (holder.getBalanceType() == 1)
                    billMade = calculateTotalAmount(holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            + holder.getBalance();
                else
                    billMade = calculateTotalAmount(holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            - holder.getBalance();


                holder.setTotalAmount(billMade);

                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.ROLL_DATE)));
                updateOutstandingBills(holder);
                BillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }
    public void updateQuantity(Bill bill) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CUSTOMER_ID, bill.getCustomerId());
        values.put(TableColumns.DEFAULT_QUANTITY, bill.getQuantity());
        values.put(TableColumns.BALANCE, bill.getBalance());
        getDb().update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + bill.getCustomerId() + "'", null);
    }

    @Override
    public List<Bill> getOutstandingBill() {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.IS_OUTSTANDING + " ='" + "0'" + " AND "
                + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'";
        ArrayList<Bill> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
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
                if (BillingFragment.payment != null)
                    BillingFragment.payment.add(holder);
                else
                    list.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return list;

    }

    @Override
    public List<Bill> getOutstandingBillsById(int id) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.IS_OUTSTANDING + " ='" + "0'" + " AND "
                + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "' AND " + TableColumns.CUSTOMER_ID + " ='" + id + "'";
        ArrayList<Bill> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
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
                CustomersBillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    @Override
    public List<Bill> getTotalBillById(int id) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_BILL + " INNER JOIN " + TableNames.TABLE_CUSTOMER
                + " ON " + TableNames.TABLE_CUSTOMER_BILL + "." + TableColumns.CUSTOMER_ID + "=" + TableNames.TABLE_CUSTOMER + "." + TableColumns.ID + " WHERE " + TableColumns.IS_CLEARED + " ='" + "1'"
                + " AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >='"
                + Constants.getCurrentDate() + "' AND " + TableColumns.IS_OUTSTANDING + " ='1' AND" +
                " (" + TableColumns.DELETED_ON + " ='1' OR " + TableColumns.DELETED_ON + " >'" + Constants.getCurrentDate() + "')" +
                " AND " + TableColumns.CUSTOMER_ID + " ='" + id + "'";

        ArrayList<Bill> list = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
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
                if ("1".equals(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)))) {
                    holder.setEndDate(Constants.getCurrentDate());
                    totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
                            cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false);

                } else {
                    Calendar deletedCal = Calendar.getInstance();
                    try {
                        Date date = Constants.work_format.parse(cursor.getString(cursor.getColumnIndex(TableColumns.DELETED_ON)));
                        deletedCal.setTime(date);
                        holder.setEndDate(String.valueOf(deletedCal.get(Calendar.YEAR)) + "-" + String.format("%02d", deletedCal.get(Calendar.MONTH))
                                + "-" + String.format("%02d", deletedCal.get(Calendar.DAY_OF_MONTH) - 1));
                        totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
                                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false);//
//
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
                holder.setRate(new CustomersSettingService().getByCustId(holder.getCustomerId(), Constants.getCurrentDate()).getDefaultRate());
                double billMade = 0;
                if (holder.getBalanceType() == 1)
                    billMade = calculateTotalAmount(holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            + holder.getBalance();
                else
                    billMade = calculateTotalAmount(holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                            - holder.getBalance();


                holder.setTotalAmount(billMade);

//                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PAYMENT_MADE)));
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.ROLL_DATE)));
                updateOutstandingBills(holder);
//                BillingFragment.custIdsList.add(holder.getCustomerId());
                CustomersBillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;


    }

    @Override
    public void updateBillById(Bill bill) {

    }

    @Override
    public void updateOutstandingBills(Bill bill) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DEFAULT_QUANTITY, bill.getQuantity());
        values.put(TableColumns.TOTAL_AMOUNT, bill.getPaymentMade());
        if (Constants.getCurrentDate().equals(new GlobalSettingsService().getRollDate())) {
            values.put(TableColumns.END_DATE, Constants.getCurrentDate());
            values.put(TableColumns.IS_OUTSTANDING, 0);
        }
        getDb().update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.CUSTOMER_ID + " ='" + bill.getCustomerId() + "'" + " AND "
                + TableColumns.IS_OUTSTANDING + " ='1'  AND " + TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.END_DATE + " >'" + Constants.getCurrentDate() + "'", null);

    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

    public double calculateTotalAmount(final int custId, final String day, final double quantity) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER_SETTINGS + " WHERE " + TableColumns.CUSTOMER_ID + " ='"
                + custId + "'"
                + " AND " + TableColumns.START_DATE + " <='" + day + "'" + " AND " + TableColumns.END_DATE + " >='" + day + "'";
        Cursor cursor = getDb().rawQuery(selectquery, null);
        double amount = 0;
        if (cursor.moveToFirst()) {
            do {
                double totalRate = cursor.getDouble(cursor.getColumnIndex(TableColumns.DEFAULT_RATE)) * quantity;
                double tax = new GlobalSettingsService().getData().getTax();
                if (tax > 0)
                    amount = ((totalRate * tax)
                            / 100) + totalRate;
                else
                    amount = totalRate;
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return amount;
    }

    @Override
    public void updateRollDate() {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ROLL_DATE, new GlobalSettingsService().getRollDate());
        long i = getDb().update(TableNames.TABLE_CUSTOMER_BILL, values, TableColumns.START_DATE + " <='" + Constants.getCurrentDate() + "' AND " +
                TableColumns.IS_OUTSTANDING + " ='1'", null);
    }
}
