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

    private boolean isQuantityOrRateDifferent(CustomersSetting setting1, CustomersSetting setting2){
       boolean same =  (setting1.getGetDefaultQuantity() == setting2.getGetDefaultQuantity()) &&
                    (setting1.getDefaultRate() == setting2.getDefaultRate());
        return !same;
    }

    @Override
    public void insertOrUpdateCustomerSetting(CustomersSetting setting) {
        try {
            Customers customer = getCustomerDetail(setting.getCustomerId(), true);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            Date today = cal.getTime();
            CustomersSetting existingSetting = getCustomerSetting(customer, today, false, false);
            CustomersSetting existingSettingWithoutCustomDelivery = getCustomerSetting(customer, today, false, true);

            boolean isCustomDeliveryPresent = ((existingSetting != null) && existingSetting.getIsCustomDelivery());
            boolean isSettingWithoutCustomDeliveryPresent = (existingSettingWithoutCustomDelivery != null);

            if( setting.getIsCustomDelivery() && isCustomDeliveryPresent) {
                boolean toUpdate = isQuantityOrRateDifferent(existingSetting, setting);
                if( !toUpdate )
                    return;

                existingSetting.setGetDefaultQuantity(setting.getGetDefaultQuantity());
                _customerSettingsService.update(existingSetting);
            }
            else if(setting.getIsCustomDelivery() && isSettingWithoutCustomDeliveryPresent){
                boolean toInsert = isQuantityOrRateDifferent(existingSettingWithoutCustomDelivery, setting);

                if( !toInsert)
                    return;

                setting.setStartDate(setting.getStartDate());
                setting.setEndDate(setting.getEndDate());
                setting.setIsCustomDelivery(true);
                _customerSettingsService.insert(setting);
            }
            else if( !setting.getIsCustomDelivery() ) {
                boolean toInsert = isQuantityOrRateDifferent(existingSettingWithoutCustomDelivery, setting);
                if( !toInsert) {
                    return;
                }
                //Delete existing custom setting
                if(isCustomDeliveryPresent) {
                    _customerSettingsService.delete(existingSetting);
                }
                existingSettingWithoutCustomDelivery.setEndDate(Utils.ToDateString(today));
                _customerSettingsService.update(existingSettingWithoutCustomDelivery);

                setting.setStartDate(Utils.ToDateString(today));
                setting.setEndDate(Utils.ToDateString(Utils.GetMaxDate()));
                //setting.setIsCustomDelivery(false);
                _customerSettingsService.insert(setting);
            }
        } catch (Exception ex) {
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
    public List<Customers> getCustomersWithinDeliveryRange(Integer areaId, Date startDateObj, Date endDateObj) {
        return searchCustomers(areaId, startDateObj, endDateObj);
    }

    //This does not hit the database gets a complete customersetting for a particular date
    @Override
    public CustomersSetting getCustomerSetting(Customers customer, Date date, boolean populateSettings, boolean ignoreCustomDelivery) throws Exception {
        if (populateSettings)
            customer = getCustomerDetail(customer.getCustomerId(), true);

        //throwing just for safety
        if (!populateSettings && customer.customerSettings == null)
            throw new Exception("Customer setting is not populated");

        CustomersSetting toReturn = null;
        for (CustomersSetting setting : customer.customerSettings) {
            Date endDate = Utils.FromDateString(setting.getEndDate());
            Date startDate = Utils.FromDateString(setting.getStartDate());

<<<<<<< HEAD
            if (setting.getIsCustomDelivery()) {
                if (endDate.equals(date) && startDate.equals(date))
=======
            if (!ignoreCustomDelivery && setting.getIsCustomDelivery()) {
                if (Utils.EqualsDate(date, endDate) && Utils.EqualsDate(startDate, date))
>>>>>>> 7184480cf90290868e08648c8eb09a8f3952f8e6
                    return setting;
            } else {
                if ((Utils.BeforeOrEqualsDate(startDate, date)) && Utils.AfterDate(endDate,date))
                    toReturn = setting;
            }
        }

        //did not find any setting, might happen for deleted customer
        return toReturn;
    }

    public QuantityAmount getTotalQuantityAndAmount(Customers customer, Date startDate, Date endDate) throws Exception {
        //throwing just for safety - later call getCustomerrSetting with populatesetting to fill up
        if (customer.customerSettings == null)
            throw new Exception("Customer setting is not populated");
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);

<<<<<<< HEAD
        double totalQuantity = 0, totalAmount = 0, rate = 0;
        for (Date date = start.getTime(); start.before(end) || start.equals(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
            CustomersSetting setting = getCustomerSetting(customer, date, false);
            if (setting != null) {
=======
        double totalQuantity = 0, totalAmount = 0;
        Date date = start.getTime();
        for ( ;Utils.BeforeOrEqualsDate(date, endDate); start.add(Calendar.DATE, 1), date = start.getTime())
        {
            CustomersSetting setting = getCustomerSetting(customer, date, false, false);
            if( setting != null) {
>>>>>>> 7184480cf90290868e08648c8eb09a8f3952f8e6
                totalQuantity += setting.getGetDefaultQuantity();
                double rate = setting.getDefaultRate();
                totalAmount += rate * totalQuantity;
            }
<<<<<<< HEAD


=======
>>>>>>> 7184480cf90290868e08648c8eb09a8f3952f8e6
        }

        QuantityAmount qa = new QuantityAmount();
        qa.amount = totalAmount;
        qa.quantity = totalQuantity;
        return qa;
    }

    @Override
    public Customers getCustomerDetail(int id, boolean populateSettings) {
        if (!populateSettings)
            return getCustomerDetail(id);

        String selectQuery = "SELECT * FROM " + TableNames.CUSTOMER
               + " INNER JOIN " + TableNames.CustomerSetting + " ON "
                + TableNames.CUSTOMER + "." + TableColumns.ID + " =" + TableNames.CustomerSetting + "." + TableColumns.CustomerId
                + " WHERE " + TableColumns.CustomerId + " ='" + id  + "'";

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

        String selectQuery = "SELECT * FROM " + TableNames.CUSTOMER
                + " INNER JOIN " + TableNames.CustomerSetting + " ON "
                + TableNames.CUSTOMER + "." + TableColumns.ID + " =" + TableNames.CustomerSetting + "." + TableColumns.CustomerId
                + " WHERE " + TableColumns.IsDeleted + " ='0'" + " OR (" + TableColumns.IsDeleted +  "='1' AND "
                + TableColumns.DeletedOn + " >='" + startDate + "')";

        if (areaId != null)
            selectQuery += " AND " + TableColumns.AreaId + " ='" + areaId + "'";

        Cursor cursor = getDb().rawQuery(selectQuery, null);
        HashMap<Integer, Customers> customersMap = new HashMap<Integer, Customers>();

        if (cursor.moveToFirst()) {
            do {
                Integer customerId = cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId));
                Customers customers = null;
                if (!customersMap.containsKey(customerId)) {
                    customers = new Customers();
                    customers.PopulateFromCursor(cursor);
                    customersMap.put(customerId, customers);
                    customers.customerSettings = new ArrayList<CustomersSetting>();
                } else {
                    customers = customersMap.get(customerId);
                }

                CustomersSetting holder = new CustomersSetting();
                holder.PopulateFromCursor(cursor);
                customers.customerSettings.add(holder);
<<<<<<< HEAD
                customersData = new ArrayList<>(customersMap.values());
=======
>>>>>>> 7184480cf90290868e08648c8eb09a8f3952f8e6
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  new ArrayList<Customers>(customersMap.values());
    }
}
