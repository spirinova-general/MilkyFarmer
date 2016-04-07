package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Bill;
import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.*;
import com.milky.service.databaseutils.serviceinterface.IBill;
import com.milky.service.databaseutils.serviceinterface.ICustomers;
import com.milky.service.databaseutils.serviceinterface.IGlobalSetting;
import com.milky.ui.customers.CustomersBillingFragment;
import com.milky.ui.main.BillingFragment;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillService implements IBill {

    ICustomers _customerService = new CustomersService();
    IGlobalSetting _globalSettingService = new GlobalSettingsService();

    @Override
    public void insert(Bill holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CustomerId, holder.getCustomerId());
        values.put(TableColumns.StartDate, holder.getStartDate());
        values.put(TableColumns.EndDate, holder.getEndDate());
        values.put(TableColumns.TotalQuantity, holder.getQuantity());
        values.put(TableColumns.Balance, holder.getBalance());
        values.put(TableColumns.Adjustment, 0);
        values.put(TableColumns.TAX, holder.getTax());
        values.put(TableColumns.IsCleared, holder.getIsCleared());
        values.put(TableColumns.PaymentMade, holder.getPaymentMade());
        values.put(TableColumns.DateModified, holder.getDateModified());
        values.put(TableColumns.TotalAmount, holder.getTotalAmount());
        values.put(TableColumns.IsOutstanding, holder.getIsOutstanding());
        values.put(TableColumns.DateAdded, holder.getDateAdded());
        values.put(TableColumns.Dirty, 0);
        values.put(TableColumns.RollDate, holder.getRollDate());
        getDb().insert(TableNames.Bill, null, values);
    }

    @Override
    public void update(Bill bill) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CustomerId, bill.getCustomerId());
        values.put(TableColumns.StartDate, bill.getStartDate());
        values.put(TableColumns.EndDate, bill.getEndDate());
        values.put(TableColumns.TotalQuantity, bill.getQuantity());
        values.put(TableColumns.Balance, bill.getBalance());
        values.put(TableColumns.Adjustment, 0);
        values.put(TableColumns.TAX, bill.getTax());
        values.put(TableColumns.IsCleared, bill.getIsCleared());
        values.put(TableColumns.PaymentMade, bill.getPaymentMade());
        values.put(TableColumns.DateModified, bill.getDateModified());
        values.put(TableColumns.TotalAmount, bill.getTotalAmount());
        values.put(TableColumns.IsOutstanding, bill.getIsOutstanding());
        values.put(TableColumns.DateAdded, bill.getDateAdded());
        values.put(TableColumns.Dirty, 1);
        values.put(TableColumns.RollDate, bill.getRollDate());
        getDb().update(TableNames.Bill, values, TableColumns.CustomerId + " ='" + bill.getCustomerId() + "'", null);
    }

    @Override
    public List<Bill> getAllUnClearedBills() {
        String selectquery = "SELECT * FROM " + TableNames.Bill  + " WHERE " + TableColumns.IsCleared + " ='" + "0')";

        ArrayList<Bill> list = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.PopulateFromCursor(cursor);
                list.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public List<Bill> getBillsOfCustomer(int customerId) {

        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.CustomerId + " ='" + customerId + "'";
        ArrayList<Bill> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.PopulateFromCursor(cursor);
                list.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    @Override
    public void RecalculateAllOutstandingBills() throws Exception
    {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        Date rollDate = Utils.FromDateString(_globalSettingService.getRollDate());
        //Umesh check this - I just want to find the first day of current month
        cal.set(today.getYear(), today.getMonth(), 1);
        Date firstDayOfMonth = cal.getTime();

        HashMap<Integer, Bill> currentbillsMap = getAllCurrentBills();
        if( today.before(rollDate)) {
            InsertOrUpdateCurrentBills(currentbillsMap);
        }
        else{
            PerformBillRoll();
        }
    }

    private void PerformBillRoll() throws Exception{
        //Mark all current bills outstanding
        String selectquery =  "UPDATE " + TableNames.Bill  + " SET " + TableColumns.IsOutstanding + " ='" + "1'"
                +  " WHERE " + TableColumns.IsOutstanding + " ='" + "0' AND " + TableColumns.IsCleared + " ='" + "0'";

        //Umesh check if it updates values
        getDb().execSQL(selectquery);

        InsertOrUpdateCurrentBills(null);

    }

    private void InsertOrUpdateCurrentBills(HashMap<Integer, Bill> currentbillsMap) throws Exception
    {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        //Umesh check this - I just want to find the first day of current month
        cal.set(today.getYear(), today.getMonth(), 1);
        Date firstDayOfMonth = cal.getTime();

        List<Customers> customers =  _customerService.getCustomersWithinDeliveryRange(null, firstDayOfMonth, today);

        for(Customers customer: customers) {
            Bill bill;
            if (currentbillsMap != null && currentbillsMap.containsKey(customer.getCustomerId())) {
                bill = currentbillsMap.get(customer.getCustomerId());
            }
            else
            {
                bill = new Bill();
            }

            QuantityAmount qa = _customerService.getTotalQuantityAndAmount(customer,firstDayOfMonth, today);
            bill.setStartDate(Utils.ToDateString(firstDayOfMonth));
            bill.setEndDate(Utils.ToDateString(today));
            bill.setQuantity(qa.quantity);
            bill.setTotalAmount(qa.amount);
            bill.setBalance(customer.getBalance_amount());
            bill.setIsCleared(0);
            bill.setPaymentMade(0);
            bill.setIsOutstanding(0);

            if (currentbillsMap != null && currentbillsMap.containsKey(customer.getCustomerId())){
                update(bill);
            }
            else
            {
                insert(bill);
            }
        }
    }

    //Gets current month's bills - that are not yet marked outstanding, get bills in a map with customer id for easier access
    private HashMap<Integer, Bill> getAllCurrentBills() {
        String selectquery = "SELECT * FROM " + TableNames.Bill  + " WHERE " + TableColumns.IsOutstanding + " ='" + "0'"
                +  " AND " + TableColumns.IsCleared + " ='" + "0'";

        HashMap<Integer, Bill> map = new HashMap<Integer, Bill>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.PopulateFromCursor(cursor);
                map.put(holder.getCustomerId(), holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return map;
    }

    //Umesh - clean up the old code when they are not in use. Comment them and put them at the bottom of the file - don't delete
     public List<Bill>getTotalAllBill() {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " INNER JOIN " + TableNames.CUSTOMER
                + " ON " + TableNames.Bill + "." + TableColumns.CustomerId + "=" + TableNames.CUSTOMER + "." + TableColumns.ID + " WHERE " + TableColumns.IsCleared + " ='" + "0'"
                + " AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.EndDate + " >='"
                + Constants.getCurrentDate() + "' AND " + TableColumns.IsOutstanding + " ='0' AND" +
                " (" + TableColumns.IsDeleted + " ='0' OR " + TableColumns.DeletedOn + " >'" + Constants.getCurrentDate() + "')";

        ArrayList<Bill> list = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                Calendar cal = Calendar.getInstance();
                double totalQty = 0;
                try {
                    Date d = Constants.work_format.parse(holder.getStartDate());
                    cal.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar currentDate = Calendar.getInstance();
                if (0 == cursor.getInt(cursor.getColumnIndex(TableColumns.IsDeleted))) {
                    holder.setEndDate(Constants.getCurrentDate());
                    totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
                            cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false, holder.getCustomerId());

                } else {
                    Calendar deletedCal = Calendar.getInstance();
                    try {
                        Date date = Constants.work_format.parse(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)));
                        deletedCal.setTime(date);
                        holder.setEndDate(String.valueOf(deletedCal.get(Calendar.YEAR)) + "-" + String.format("%02d", deletedCal.get(Calendar.MONTH))
                                + "-" + String.format("%02d", deletedCal.get(Calendar.DAY_OF_MONTH) - 1));
                        totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
                                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false, holder.getCustomerId());//
//
                    } catch (ParseException pexp) {

                    }
                }
                holder.setQuantity(totalQty);
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.Adjustment)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(1);
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IsOutstanding)));
                holder.setRate(new CustomersSettingService().getByCustId(holder.getCustomerId(), Constants.getCurrentDate()).getDefaultRate());
                double billMade = 0;
                billMade = calculateTotalAmount(holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                        + holder.getBalance();


                holder.setTotalAmount(billMade);

                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
                updateOutstandingBills(holder);
                //Umesh - never use UI elements back in the service layer
                if (BillingFragment.payment != null)
                    BillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());


        }
        cursor.close();

        return list;
    }

    public void updateQuantity(Bill bill) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.CustomerId, bill.getCustomerId());
        values.put(TableColumns.TotalQuantity, bill.getQuantity());
        values.put(TableColumns.Balance, bill.getBalance());
        getDb().update(TableNames.Bill, values, TableColumns.CustomerId + " ='" + bill.getCustomerId() + "'", null);
    }

    @Override
    public List<Bill> getOutstandingBill() {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsCleared + " ='" + "0'"
                + " AND " + TableColumns.IsOutstanding + " ='" + "1'" + " AND "
                + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'";
        ArrayList<Bill> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
                holder.setQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.TotalQuantity)));
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.Adjustment)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(1);
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IsOutstanding)));
                holder.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(TableColumns.TotalAmount)));
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
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
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsCleared + " ='" + "0'"
                + " AND " + TableColumns.IsOutstanding + " ='" + "1'" + " AND "
                + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "' AND " + TableColumns.CustomerId + " ='" + id + "'";
        ArrayList<Bill> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                holder.setEndDate(cursor.getString(cursor.getColumnIndex(TableColumns.EndDate)));
                holder.setQuantity(cursor.getDouble(cursor.getColumnIndex(TableColumns.TotalQuantity)));
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.Adjustment)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(1);
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IsOutstanding)));
                holder.setTotalAmount(cursor.getDouble(cursor.getColumnIndex(TableColumns.TotalAmount)));
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));
                CustomersBillingFragment.payment.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    @Override
    public List<Bill> getTotalBillById(int id) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " INNER JOIN " + TableNames.CUSTOMER
                + " ON " + TableNames.Bill + "." + TableColumns.CustomerId + "=" + TableNames.CUSTOMER + "." +
                TableColumns.ID + " WHERE " + TableColumns.IsCleared + " ='" + "0'"
                + " AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'" + " AND " + TableColumns.EndDate + " >='"
                + Constants.getCurrentDate() + "' AND " + TableColumns.IsOutstanding + " ='0' AND" +
                " (" + TableColumns.IsDeleted + " ='0' OR " + TableColumns.DeletedOn + " >'" + Constants.getCurrentDate() + "')" +
                " AND " + TableColumns.CustomerId + " ='" + id + "'";

        ArrayList<Bill> list = new ArrayList<>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId)));
                holder.setStartDate(cursor.getString(cursor.getColumnIndex(TableColumns.StartDate)));
                Calendar cal = Calendar.getInstance();
                double totalQty = 0;
                try {
                    Date d = Constants.work_format.parse(holder.getStartDate());
                    cal.setTime(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar currentDate = Calendar.getInstance();
                if (0 == cursor.getInt(cursor.getColumnIndex(TableColumns.IsDeleted))) {
                    holder.setEndDate(Constants.getCurrentDate());
                    totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
                            cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false, id);

                } else {
                    Calendar deletedCal = Calendar.getInstance();
                    try {
                        Date date = Constants.work_format.parse(cursor.getString(cursor.getColumnIndex(TableColumns.DeletedOn)));
                        deletedCal.setTime(date);
                        holder.setEndDate(String.valueOf(deletedCal.get(Calendar.YEAR)) + "-" + String.format("%02d", deletedCal.get(Calendar.MONTH))
                                + "-" + String.format("%02d", deletedCal.get(Calendar.DAY_OF_MONTH) - 1));
                        totalQty += new DeliveryService().getTotalQuantityConsumed(cal.get(Calendar.DAY_OF_MONTH), currentDate.get(Calendar.DAY_OF_MONTH),
                                cal.get(Calendar.MONTH), cal.get(Calendar.YEAR), false, id);
                    } catch (ParseException pexp) {
                    }
                }
                holder.setQuantity(totalQty);
                holder.setBalance(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
                holder.setAdjustment(cursor.getDouble(cursor.getColumnIndex(TableColumns.Adjustment)));
                holder.setTax(cursor.getDouble(cursor.getColumnIndex(TableColumns.TAX)));
                holder.setIsCleared(1);
                holder.setPaymentMade(cursor.getDouble(cursor.getColumnIndex(TableColumns.PaymentMade)));
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
                holder.setIsOutstanding(cursor.getInt(cursor.getColumnIndex(TableColumns.IsOutstanding)));
                holder.setRate(new CustomersSettingService().getByCustId(holder.getCustomerId(), Constants.getCurrentDate()).getDefaultRate());
                double billMade = calculateTotalAmount(holder.getCustomerId(), cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-" + String.format("%02d", cal.get(Calendar.DAY_OF_MONTH)), totalQty)
                        + holder.getBalance();
                holder.setTotalAmount(billMade);
                holder.setRollDate(cursor.getString(cursor.getColumnIndex(TableColumns.RollDate)));

                updateOutstandingBills(holder);
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
        values.put(TableColumns.TotalQuantity, bill.getQuantity());
        values.put(TableColumns.TotalAmount, bill.getPaymentMade());
        if (Constants.getCurrentDate().equals(new GlobalSettingsService().getRollDate())) {
            values.put(TableColumns.EndDate, Constants.getCurrentDate());
            values.put(TableColumns.IsOutstanding, 1);
            values.put(TableColumns.RollDate,new GlobalSettingsService().getRollDate());
        }
        getDb().update(TableNames.Bill, values, TableColumns.CustomerId + " ='" + bill.getCustomerId() + "'" + " AND "
                + TableColumns.IsOutstanding + " ='0'  AND " + TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "'"
                + " AND " + TableColumns.EndDate + " >'" + Constants.getCurrentDate() + "'", null);
    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

    public double calculateTotalAmount(final int custId, final String day, final double quantity) {
        String selectquery = "SELECT * FROM " + TableNames.CustomerSetting + " WHERE " + TableColumns.CustomerId + " ='"
                + custId + "'"
                + " AND " + TableColumns.StartDate + " <='" + day + "'" + " AND " + TableColumns.EndDate + " >='" + day + "'";
        Cursor cursor = getDb().rawQuery(selectquery, null);
        double amount = 0;
        if (cursor.moveToFirst()) {
            do {
                double totalRate = cursor.getDouble(cursor.getColumnIndex(TableColumns.DefaultRate)) * quantity;
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
        values.put(TableColumns.RollDate, new GlobalSettingsService().getRollDate());
        long i = getDb().update(TableNames.Bill, values, TableColumns.StartDate + " <='" + Constants.getCurrentDate() + "' AND " +
                TableColumns.IsOutstanding + " ='1'", null);
    }
}
