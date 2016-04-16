package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Area;
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


    @Override
    public long insert(Customers customers) {
        ContentValues values = customers.ToContentValues();
        return getDb().insert(TableNames.CUSTOMER, null, values);
    }

    @Override
    public void update(Customers customers) {
        ContentValues values = customers.ToContentValues();
        getDb().update(TableNames.CUSTOMER, values, TableColumns.ID + " ='" + customers.getCustomerId() + "'", null);
    }

    @Override
    public void delete(int customerId) {
        Customers customer = getCustomerDetail(customerId);

        customer.setIsDeleted(1);
        Calendar cal = Calendar.getInstance();
        String today = Utils.ToDateString(cal.getTime());
        customer.setDateModified(today);
        customer.setDeletedOn(today);

        update(customer);
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
            Date today = cal.getTime();
            Date date = Utils.FromDateString(setting.getStartDate());
            CustomersSetting existingSetting = getCustomerSetting(customer, date, false, false);
            CustomersSetting existingSettingWithoutCustomDelivery = getCustomerSetting(customer, date, false, true);

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

                //setting.setStartDate(setting.getStartDate());
                //setting.setEndDate(setting.getEndDate());
                setting.setDefaultRate(existingSettingWithoutCustomDelivery.getDefaultRate());
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
    public List<Customers> getCustomersListByArea(int areaId) {
        String selectquery = "SELECT " + TableNames.CUSTOMER +"." + TableColumns.ID + " as CustomerId, * FROM "
                + TableNames.CUSTOMER + " INNER JOIN " + TableNames.AREA + " ON "
                + TableColumns.AreaId + " =" + TableNames.AREA + "." + TableColumns.ID + " AND "
                + TableNames.CUSTOMER + "." + TableColumns.IsDeleted + " ='" + "0'";

        if( areaId != -1) {
            selectquery += " AND " + TableColumns.AreaId + " ='" + areaId + "'";
        }

        ArrayList<Customers> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Customers holder = new Customers();
                holder.PopulateFromCursor(cursor);
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex("CustomerId")));

                Area area = new Area();
                area.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AreaId)));
                area.setArea(cursor.getString(cursor.getColumnIndex(TableColumns.Name)));
                area.setCity(cursor.getString(cursor.getColumnIndex(TableColumns.City)));
                area.setLocality(cursor.getString(cursor.getColumnIndex(TableColumns.Locality)));
                holder.area = area;

                list.add(holder);
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
        return searchCustomers(areaId, startDateObj, endDateObj, null);
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

            if (!ignoreCustomDelivery && setting.getIsCustomDelivery()) {
                if (Utils.EqualsDate(date, endDate) && Utils.EqualsDate(startDate, date))
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

        double totalQuantity = 0, totalAmount = 0;
        Date date = startDate;
        for ( ;Utils.BeforeOrEqualsDate(date, endDate); start.add(Calendar.DATE, 1), date = start.getTime())
        {
            CustomersSetting setting = getCustomerSetting(customer, date, false, false);
            if( setting != null) {
                double quantity = setting.getGetDefaultQuantity();
                totalQuantity += quantity;
                double rate = setting.getDefaultRate();
                totalAmount += rate * quantity;
            }
        }

        totalAmount += customer.getBalance_amount();
        QuantityAmount qa = new QuantityAmount();
        qa.amount = totalAmount;
        qa.quantity = totalQuantity;
        return qa;
    }

    @Override
    public Customers getCustomerDetail(int id, boolean populateSettings) {
        if (!populateSettings)
            return getCustomerDetail(id);

        Customers customer = searchCustomers(null, null, null, id).get(0);
        return customer;
    }



    private List<Customers> searchCustomers(Integer areaId, Date startDateObj, Date endDateObj, Integer customerIdInt) {
        //String endDate = Utils.ToDateString(endDateObj);

        String selectQuery = "SELECT *, " + TableNames.CUSTOMER + "." + TableColumns.IsDeleted + " as IsCustomerDeleted, "
                + TableNames.CustomerSetting + "." +  TableColumns.IsDeleted + " as IsCustomerSettingDeleted, "
                + TableNames.CUSTOMER + "." + TableColumns.StartDate + " as CustomerStartDate, "
                + TableNames.CustomerSetting + "." +  TableColumns.StartDate + " as CustomerSettingStartDate "
                + " FROM " + TableNames.CUSTOMER
                + " INNER JOIN " + TableNames.CustomerSetting + " ON "
                + TableNames.CUSTOMER + "." + TableColumns.ID + " =" + TableNames.CustomerSetting + "." + TableColumns.CustomerId;

        if( customerIdInt == null ) {
            String startDate = Utils.ToDateString(startDateObj, true);

            selectQuery += " WHERE IsCustomerDeleted ='0' OR ( IsCustomerDeleted ='1' AND "
                    + TableColumns.DeletedOn + " >='" + startDate + "')";
        }
        else
            selectQuery +=  " WHERE " + TableColumns.CustomerId + " ='" + customerIdInt  + "'";

        if (areaId != null)
            selectQuery += " AND " + TableColumns.AreaId + " ='" + areaId + "'";

        Cursor cursor = getDb().rawQuery(selectQuery, null);
        HashMap<Integer, Customers> customersMap = new HashMap<>();

        if (cursor.moveToFirst()) {
            do {
                Integer customerId = cursor.getInt(cursor.getColumnIndex(TableColumns.CustomerId));
                Customers customers = null;
                if (!customersMap.containsKey(customerId)) {
                    customers = new Customers();
                    customers.PopulateFromCursor(cursor);
                    customers.setCustomerId(customerId);
                    customers.setIsDeleted(cursor.getInt(cursor.getColumnIndex("IsCustomerDeleted")));
                    customers.setStartDate(cursor.getString(cursor.getColumnIndex("CustomerStartDate")));
                    customersMap.put(customerId, customers);
                    customers.customerSettings = new ArrayList<>();
                } else {
                    customers = customersMap.get(customerId);
                }
                CustomersSetting holder = new CustomersSetting();
                holder.PopulateFromCursor(cursor);
                holder.setStartDate(cursor.getString(cursor.getColumnIndex("CustomerSettingStartDate")));
                holder.setIsDeleted(cursor.getInt(cursor.getColumnIndex("IsCustomerSettingDeleted")));
                customers.customerSettings.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  new ArrayList<Customers>(customersMap.values());
    }
}
