package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.Utils;
import com.milky.service.databaseutils.serviceinterface.ICustomers;
import com.milky.service.databaseutils.serviceinterface.ICustomersSettings;
import com.milky.utils.AppUtil;
import com.milky.viewmodel.VDelivery;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomersService implements ICustomers {

    private ICustomersSettings _customerSettingsService = new CustomersSettingService();

    //Umesh - can write opposite of PopulateFromCursor to put values

    @Override
    public long insert(Customers customers) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FirstName, customers.getFirstName());
        values.put(TableColumns.LastName, customers.getLastName());
        values.put(TableColumns.Balance, customers.getBalance_amount());
        values.put(TableColumns.Address1, customers.getAddress1());
        values.put(TableColumns.Address2, customers.getAddress2());
        values.put(TableColumns.AreaId, customers.getAreaId());
        values.put(TableColumns.Mobile, customers.getMobile());
        values.put(TableColumns.DateAdded, customers.getDateAdded());
        values.put(TableColumns.DateModified, customers.getDateAdded());
        values.put(TableColumns.IsDeleted, 0);
        values.put(TableColumns.DeletedOn, "null");
        values.put(TableColumns.Dirty, 0);
        return getDb().insert(TableNames.CUSTOMER, null, values);
    }

    @Override
    public void update(Customers customers) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FirstName, customers.getFirstName());
        values.put(TableColumns.LastName, customers.getLastName());
        values.put(TableColumns.Balance, customers.getBalance_amount());
        values.put(TableColumns.Address1, customers.getAddress1());
        values.put(TableColumns.AreaId, customers.getAreaId());
        values.put(TableColumns.Mobile, customers.getMobile());
        values.put(TableColumns.DateAdded, customers.getDateAdded());
        values.put(TableColumns.DateModified, customers.getDateAdded());
        values.put(TableColumns.IsDeleted, customers.getIsDeleted());
        values.put(TableColumns.DeletedOn, customers.getDeletedOn());
        values.put(TableColumns.Dirty, 1);
        getDb().update(TableNames.CUSTOMER, values, TableColumns.ID + " ='" + customers.getCustomerId() + "'", null);
    }

    @Override
    public void delete(Customers customers) {

    }

    @Override
    public void insertOrUpdateCustomerSetting(CustomersSetting setting) {
        try {
            Customers customer = getCustomerDetail(setting.getCustomerId(), true);
            Calendar cal = Calendar.getInstance();
            Date today = cal.getTime();
            CustomersSetting existingSetting = getCustomerSetting(customer, today, false);
            boolean toInsert = (existingSetting.getGetDefaultQuantity() != setting.getGetDefaultQuantity()) ||
                    (existingSetting.getDefaultRate() != setting.getDefaultRate());

            if (toInsert) {
                existingSetting.setEndDate(Utils.ToDateString(today));
                _customerSettingsService.update(existingSetting);

                setting.setStartDate(Utils.ToDateString(today));
                setting.setEndDate(Utils.ToDateString(Utils.GetMaxDate()));
                setting.setIsCustomDelivery(false);
                _customerSettingsService.insert(setting);
            } else {
                _customerSettingsService.update(setting);
            }
        }
        catch(Exception ex) {
        //TBD
        }
    }

    @Override
    public List<Customers> getCustomersLisytByArea(int areaId) {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.IsDeleted + " ='" + "1'"
                + " AND " + TableColumns.AreaId + " ='" + areaId + "'";
        ArrayList<Customers> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Customers holder = new Customers();
                holder.PopulateFromCursor(cursor);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    @Override
    public List<Customers> getAllCustomers() {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.IsDeleted + " ='" + "0'";
        ArrayList<Customers> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Customers holder = new Customers();
                holder.PopulateFromCursor(cursor);
                list.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    @Override
    public Customers getCustomerDetail(int id) {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.ID + " ='" + id + "'";
        Customers customers = null;

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                customers = new Customers();
                customers.PopulateFromCursor(cursor);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return customers;
    }


    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

    @Override
    public boolean isAreaAssociated(int areaId) {
        String selectQuery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.AreaId + " ='"
                + areaId + "'";

        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    //Umesh interface names should be same as class names except "I" and core class names should be like "Customer" without the "s"
    //as it represents one row of the table
    @Override
    public List<Customers> getCustomersWithinDeliveryRange(Integer areaId, Date startDateObj, Date endDateObj)
    {
        return searchCustomers(areaId, startDateObj, endDateObj);
    }

    //This does not hit the database gets a complete customersetting for a particular date
    @Override
    public CustomersSetting getCustomerSetting(Customers customer, Date date, boolean populateSettings) throws Exception
    {
        if( populateSettings)
            customer = getCustomerDetail(customer.getCustomerId(), true);

        //throwing just for safety
        if( !populateSettings && customer.customerSettings == null)
            throw new Exception("Customer setting is not populated");

        for(CustomersSetting setting: customer.customerSettings)
        {
            Date endDate = Utils.FromDateString(setting.getEndDate());
            Date startDate = Utils.FromDateString(setting.getStartDate());

            if( setting.getIsCustomDelivery() )
            {
                if( endDate == date && startDate == date)
                    return setting;
            }
            else
            {
                if( (startDate.before(date) || startDate.equals(date))&& endDate.after(date))
                    return setting;
            }
        }

        //did not find any setting, might happen for deleted customer
        return null;
    }

    public QuantityAmount getTotalQuantityAndAmount(Customers customer, Date startDate, Date endDate) throws Exception
    {
        //throwing just for safety - later call getCustomerrSetting with populatesetting to fill up
        if( customer.customerSettings == null)
            throw new Exception("Customer setting is not populated");

        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Date firstDayOfTheMonth = start.getTime();
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

        double totalQuantity = 0, totalAmount = 0;
        for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()){
            CustomersSetting setting = getCustomerSetting(customer, date, false);
            totalQuantity += setting.getGetDefaultQuantity();
            double rate = setting.getDefaultRate();
            totalAmount += rate*totalQuantity;
        }

        QuantityAmount qa = new QuantityAmount();
        qa.amount = totalAmount;
        qa.quantity = totalQuantity;
        return qa;
    }

    @Override
    public Customers getCustomerDetail(int id, boolean populateSettings) {
        if( !populateSettings)
            return getCustomerDetail(id);

        String selectQuery = "SELECT * FROM " + TableNames.CUSTOMER
                + " WHERE " + TableColumns.CustomerId + " ='" + id + "' INNER JOIN " + TableNames.CustomerSetting + " ON "
                + TableNames.CUSTOMER + "." + TableColumns.ID + " =" + TableNames.CustomerSetting + "." + TableColumns.CustomerId;

        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Customers customers = new Customers();

        if (cursor.moveToFirst()) {
            customers.PopulateFromCursor(cursor);
            customers.customerSettings = new ArrayList<CustomersSetting>();
            do {
                CustomersSetting holder = new CustomersSetting();
                holder.PopulateFromCursor(cursor);
                customers.customerSettings.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return customers;
    }



    private List<Customers> searchCustomers(Integer areaId, Date startDateObj, Date endDateObj) {

        String startDate = Utils.ToDateString(startDateObj);
        String endDate = Utils.ToDateString(endDateObj);

        String selectQuery = "SELECT * FROM " + TableNames.CUSTOMER + " INNER JOIN " + TableNames.CustomerSetting + " ON "
                + TableNames.CUSTOMER + "." + TableColumns.ID + " =" + TableNames.CustomerSetting + "." + TableColumns.CustomerId
                + " WHERE " + TableColumns.StartDate + " <='" + startDate + "'" + " AND " + TableColumns.EndDate + " >='" + endDate + "'"
                + " AND (" + TableColumns.IsDeleted + " ='0'" + " OR "
                + TableColumns.DeletedOn + " >='" + startDate + "'" + " AND " + TableColumns.DeletedOn + " <='" + endDate + "'";

        if (areaId != null)
            selectQuery += " AND " + TableColumns.AreaId + " ='" + areaId + "'";

        Cursor cursor = getDb().rawQuery(selectQuery, null);
        HashMap<Integer, Customers> customersMap = new HashMap<Integer, Customers>();

        if (cursor.moveToFirst()) {
            do {
                Integer customerId = cursor.getInt(cursor.getColumnIndex(TableColumns.ID));
                Customers customers = null;
                if (!customersMap.containsKey(customerId)) {
                    customers = new Customers();
                    customers.PopulateFromCursor(cursor);
                    customersMap.put(customerId, customers);
                } else {
                    customers = customersMap.get(customerId);
                }

                customers.customerSettings = new ArrayList<CustomersSetting>();

                CustomersSetting holder = new CustomersSetting();
                holder.PopulateFromCursor(cursor);
                customers.customerSettings.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  new ArrayList<Customers>(customersMap.values());
    }
}
