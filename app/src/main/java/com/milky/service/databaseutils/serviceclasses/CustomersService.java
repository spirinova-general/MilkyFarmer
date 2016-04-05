package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Customers;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceinterface.ICustomers;
import com.milky.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class CustomersService implements ICustomers {

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
    public List<Customers> getCustomersLisytByArea(int areaId) {
        String selectquery = "SELECT * FROM " + TableNames.CUSTOMER + " WHERE " + TableColumns.IsDeleted + " ='" + "1'"
                + " AND " + TableColumns.AreaId + " ='" + areaId + "'";
        ArrayList<Customers> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Customers holder = new Customers();
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));
                holder.setBalance_amount(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
                holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.Address1)));
                holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.Address2)));
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AreaId)));
                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
                holder.setIsDeleted(cursor.getInt(cursor.getColumnIndex(TableColumns.IsDeleted)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
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
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));
                holder.setBalance_amount(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
                holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.Address1)));
                holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.Address2)));
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AreaId)));
                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
                holder.setIsDeleted(cursor.getInt(cursor.getColumnIndex(TableColumns.IsDeleted)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
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
                customers.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DateAdded)));
                customers.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                customers.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FirstName)));
                customers.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LastName)));
                customers.setBalance_amount(cursor.getDouble(cursor.getColumnIndex(TableColumns.Balance)));
                customers.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.Address1)));
                customers.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.Address2)));
                customers.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AreaId)));
                customers.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.Mobile)));
                customers.setIsDeleted(cursor.getInt(cursor.getColumnIndex(TableColumns.IsDeleted)));
                customers.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DateModified)));
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
}
