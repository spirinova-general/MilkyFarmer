package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Bill;
import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.*;
import com.milky.service.databaseutils.serviceinterface.IAccountService;
import com.milky.service.databaseutils.serviceinterface.IBill;
import com.milky.service.databaseutils.serviceinterface.ICustomers;
import com.milky.service.databaseutils.serviceinterface.IGlobalSetting;

import com.milky.service.databaseutils.serviceinterface.ISmsService;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.utils.AppUtil;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BillService implements IBill {

    ICustomers _customerService = new CustomersService();
    IGlobalSetting _globalSettingService = new GlobalSettingsService();
    ISmsService _smsService = new SmsService();
    IAccountService _accountService = new AccountService();

    //We did not take care of exceptions well from start so just let it be...throw back only when absolutely needed
    @Override
    public void insert(Bill holder) {
        try {
            Date billStartDate = Utils.FromDateString(holder.getStartDate());
            Date nextRollDate = Utils.FromDateString(_globalSettingService.getRollDate());

            //if customer start date is after bill roll date, this bill is not to be inserted
            if( Utils.BeforeOrEqualsDate(billStartDate, nextRollDate)) {
                ContentValues values = holder.ToContentValues();
                getDb().insert(TableNames.Bill, null, values);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Bill bill) {
        ContentValues values = bill.ToContentValues();
        getDb().update(TableNames.Bill, values, TableColumns.ID + " ='" + bill.getId() + "'", null);
    }

    @Override
    public List<Bill> getAllGlobalBills(boolean reCalculate) {
        try {
            if (reCalculate)
                RecalculateAllCurrentAndOutstandingBills();

            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            String todayStr = Utils.ToDateString(today, true);

            String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsCleared + " ='" + "0'"
                    + " AND " + TableColumns.StartDate + " <='" + todayStr + "'";

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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    public void RecalculateAllCurrentAndOutstandingBills() {
        try {
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            Date rollDate = Utils.FromDateString(_globalSettingService.getRollDate());

            HashMap<Integer, Bill> currentbillsMap = getAllCurrentBills(-1);

            //First update all bills with quantity, total, end date etc.
            InsertOrUpdateCurrentBills(currentbillsMap, -1);

            InsertOrUpdateOutstandingBills(-1);

            //if we are after roll date...
            if (today.after(rollDate)) {
                PerformBillRoll();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bill getBill(int id) {
        String selectQuery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.ID + " ='" + id + "' AND "
                + TableColumns.IsDeleted + " ='0'";

        Bill bill = null;
        Cursor cursor = getDb().rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                bill = new Bill();
                bill.PopulateFromCursor(cursor);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return bill;
    }

    @Override
    public void SmsBill(int billId, OnTaskCompleteListner listner) {
        //Umesh - too many DB hits for each sms fix later...
        try {
            Bill bill = getBill(billId);
            Customers customer = _customerService.getCustomerDetail(bill.getCustomerId());

            Date startDate = Utils.FromDateString(bill.getStartDate());
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy");
            String startDateStr = df.format(startDate);

            Date endDate = Utils.FromDateString(bill.getEndDate());
            df = new SimpleDateFormat("dd-MMM-yy");
            String endDateStr = df.format(endDate);


            String msg = URLEncoder.encode(
                    "Dear " + customer.getFirstName() + ", " + "Your milk bill from "
                            + startDateStr + " to " + endDateStr + " is Rs. " + bill.getTotalAmount()
                            + ". Total quantity " + bill.getQuantity() + " litres. ", "UTF-8");


            _smsService.SendSms(customer.getMobile(), msg, listner, true);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void clearBill(Bill bill, double paymentMade)
    {
        bill.setPaymentMade(paymentMade);
        bill.setIsCleared(1);
        double balance = bill.getTotalAmount() - paymentMade;
        Customers customer = _customerService.getCustomerDetail(bill.getCustomerId());
        customer.setBalance_amount(balance);
        update(bill);
        _customerService.update(customer);
    }

    @Override
    public void updateCustomerBills(int customerId)
    {
        try {
            InsertOrUpdateCurrentBills(getAllCurrentBills(customerId), customerId);
            InsertOrUpdateOutstandingBills(customerId);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void PerformBillRoll() throws Exception {
        //Mark all current bills outstanding
        String selectquery = "UPDATE " + TableNames.Bill + " SET " + TableColumns.IsOutstanding + " ='" + "1'"
                + " WHERE " + TableColumns.IsOutstanding + " ='" + "0' AND " + TableColumns.IsCleared + " ='" + "0'";

        //Umesh check if it updates values
        getDb().execSQL(selectquery);

        InsertOrUpdateCurrentBills(null, -1);


        _globalSettingService.calculateAndSetNextRollDate();

    }

    private void InsertOrUpdateCurrentBills(HashMap<Integer, Bill> currentbillsMap, int customerId) throws Exception {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        //Set calendar to first date of month
        cal.set(Calendar.DAY_OF_MONTH, 1);

        Date firstDayOfMonth = cal.getTime();

        List<Customers> customers = null;

        if( customerId == -1)
        {
            customers = _customerService.getCustomersWithinDeliveryRange(null, firstDayOfMonth, today);
        }
        else
        {
            customers =  new ArrayList<Customers>();
            customers.add(_customerService.getCustomerDetail(customerId, true));
        }

        for (Customers customer : customers) {
            Bill bill;
            Date startDate = Utils.FromDateString(customer.getStartDate());
            //If bill's start date is after today, leave it
            if( !Utils.BeforeOrEqualsDate(startDate, today)) {
                continue;
            }

            if (currentbillsMap != null && currentbillsMap.containsKey(customer.getCustomerId())) {
                bill = currentbillsMap.get(customer.getCustomerId());
            } else {
                bill = new Bill();
                bill.setCustomerId(customer.getCustomerId());
                bill.setDateAdded(Utils.ToDateString(today));
                bill.setDateModified(Utils.ToDateString(today));

            }

            QuantityAmount qa = _customerService.getTotalQuantityAndAmount(customer, firstDayOfMonth, today);

            //Set start date, taking care of the condition where start date is after first day of month...
            startDate = Utils.FromDateString(customer.getStartDate());
            if( Utils.BeforeDate(firstDayOfMonth, startDate))
                bill.setStartDate(customer.getStartDate());
            else
                bill.setStartDate(Utils.ToDateString(firstDayOfMonth));

            //Set end date, taking care of the condition where customer has been deleted in betweeen...
            String endDate = Utils.ToDateString(today);
            if( customer.getIsDeleted() == 1 )
            {
                if( customer.getDeletedOn() != null) {
                    Date deletedDate = Utils.FromDateString(customer.getDeletedOn());
                    if (Utils.BeforeDate(deletedDate, today))
                        endDate = customer.getDeletedOn();
                }
            }

            bill.setEndDate(endDate);
            bill.setQuantity(qa.quantity);
            bill.setTotalAmount(qa.amount);
            bill.setBalance(customer.getBalance_amount());
            bill.setIsCleared(0);
            bill.setPaymentMade(0);
            bill.setIsOutstanding(0);
            CustomersSetting lastDaySetting = _customerService.getCustomerSetting(customer, today, false, true);
            bill.setRate(lastDaySetting.getDefaultRate());
            bill.setIsDeleted(0);
            if (currentbillsMap != null && currentbillsMap.containsKey(customer.getCustomerId())) {
                update(bill);
            } else {
                insert(bill);
            }
        }
    }

    //Gets current month's bills - that are not yet marked outstanding, get bills in a map with customer id for easier access
    private HashMap<Integer, Bill> getAllCurrentBills(int customerId) {
        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        String todayStr = Utils.ToDateString(today, true);

        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsOutstanding + " ='" + "0'"
                + " AND " + TableColumns.IsCleared + " ='" + "0'"
                + " AND " + TableColumns.StartDate + " <='" + todayStr + "'";

        if( customerId != -1)
        {
            selectquery += " AND " + TableColumns.CustomerId + " ='" + customerId  + "'";
        }

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

    private List<Bill> getAllOutstandingBills(int customerId ) {
        String selectquery = "SELECT * FROM " + TableNames.Bill + " WHERE " + TableColumns.IsOutstanding + " ='" + "1'"
                + " AND " + TableColumns.IsCleared + " ='" + "0'";


        if( customerId != -1)
        {
            selectquery += " AND " + TableColumns.CustomerId + " ='" + customerId  + "'";
        }

        List<Bill> bills = new ArrayList<Bill>();
        Cursor cursor = getDb().rawQuery(selectquery, null);
        if (cursor.moveToFirst()) {
            do {
                Bill holder = new Bill();
                holder.PopulateFromCursor(cursor);
                bills.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return bills;
    }


    private void InsertOrUpdateOutstandingBills( int customerId) throws Exception {
        List<Bill> outstandingbills = getAllOutstandingBills(customerId);
        if (outstandingbills == null || outstandingbills.size() == 0)
            return;

        for( Bill bill: outstandingbills)
        {
            Customers customer = _customerService.getCustomerDetail(bill.getCustomerId(), true);
            if (customerId != -1 && customerId != bill.getCustomerId())
                continue;
            Date startDate = Utils.FromDateString(bill.getStartDate());
            Date endDate = Utils.FromDateString(bill.getEndDate());
            QuantityAmount qa = _customerService.getTotalQuantityAndAmount(customer, startDate, endDate);
            bill.setQuantity(qa.quantity);
            bill.setTotalAmount(qa.amount);
            update(bill);
        }
    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

    //Umesh - clean up the old code when they are not in use. Comment them and put them at the bottom of the file - don't delete
    //Umesh commented these since they are now not used and also because they were accessing UI fragments.
    // Never access UI back in service layer

    /* public List<Bill>getTotalAllBill() {
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



    private List<Bill> getOutstandingBill() {
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


   private List<Bill> getOutstandingBillsById(int id) {
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

    private List<Bill> getTotalBillById(int id) {
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


    private void updateOutstandingBills(Bill bill) {
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
    public static void updateOutstandingBills(SQLiteDatabase db, String date) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.IsOutstanding, 1);
        values.put(TableColumns.EndDate, date);
        long i = db.update(TableNames.Bill, values, TableColumns.StartDate + " <='" + date + "'", null);

    }*/

}
