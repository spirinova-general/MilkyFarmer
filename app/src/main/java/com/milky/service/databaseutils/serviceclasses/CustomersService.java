package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Customers;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceinterface.ICustomersService;
import com.milky.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class CustomersService implements ICustomersService {

    @Override
    public void insert(Customers customers) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FIRST_NAME, customers.getFirstName());
        values.put(TableColumns.LAST_NAME, customers.getLastName());
        values.put(TableColumns.BALANCE, customers.getBalance_amount());
        values.put(TableColumns.ADDRESS_1, customers.getAddress1());
        values.put(TableColumns.ADDRESS_2, customers.getAddress2());
        values.put(TableColumns.AREA_ID, customers.getAreaId());
        values.put(TableColumns.MOBILE, customers.getMobile());
        values.put(TableColumns.DATE_ADDED, customers.getDateAdded());
        values.put(TableColumns.DATE_MODIFIED, customers.getDateAdded());
        values.put(TableColumns.ISDELETED, 1);
        values.put(TableColumns.DELETED_ON, 1);
        values.put(TableColumns.DIRTY, 1);
        getDb().insert(TableNames.TABLE_CUSTOMER, null, values);
    }

    @Override
    public void update(Customers customers) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.FIRST_NAME, customers.getFirstName());
        values.put(TableColumns.LAST_NAME, customers.getLastName());
        values.put(TableColumns.BALANCE, customers.getBalance_amount());
        values.put(TableColumns.ADDRESS_1, customers.getAddress1());
        values.put(TableColumns.ADDRESS_2, customers.getAddress2());
        values.put(TableColumns.AREA_ID, customers.getAreaId());
        values.put(TableColumns.MOBILE, customers.getMobile());
        values.put(TableColumns.DATE_ADDED, customers.getDateAdded());
        values.put(TableColumns.DATE_MODIFIED, customers.getDateAdded());
        values.put(TableColumns.ISDELETED, customers.getIsDeleted());
        values.put(TableColumns.DELETED_ON, customers.getDeletedOn()
        );
        values.put(TableColumns.DIRTY, 1);
        getDb().update(TableNames.TABLE_CUSTOMER, values, TableColumns.ID + " ='" + customers.getCustomerId() + "'", null);
    }

    @Override
    public void delete(Customers customers) {

    }

    @Override
    public List<Customers> getByArea(int areaId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DELETED_ON + " ='" + "1'"
                + " AND " + TableColumns.AREA_ID + " ='" + areaId + "'";
        ArrayList<Customers> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Customers holder = new Customers();
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                holder.setBalance_amount(cursor.getDouble(cursor.getColumnIndex(TableColumns.BALANCE)));
                holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)));
                holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)));
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AREA_ID)));
                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setIsDeleted(cursor.getInt(cursor.getColumnIndex(TableColumns.ISDELETED)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                list.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    @Override
    public List<Customers> getAllCustomers() {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CUSTOMER + " WHERE " + TableColumns.DELETED_ON + " ='" + "1'";
        ArrayList<Customers> list = new ArrayList<>();

        Cursor cursor = getDb().rawQuery(selectquery, null);

        if (cursor.moveToFirst()) {
            do {
                Customers holder = new Customers();
                holder.setDateAdded(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_ADDED)));
                holder.setCustomerId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setFirstName(cursor.getString(cursor.getColumnIndex(TableColumns.FIRST_NAME)));
                holder.setLastName(cursor.getString(cursor.getColumnIndex(TableColumns.LAST_NAME)));
                holder.setBalance_amount(cursor.getDouble(cursor.getColumnIndex(TableColumns.BALANCE)));
                holder.setAddress1(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_1)));
                holder.setAddress2(cursor.getString(cursor.getColumnIndex(TableColumns.ADDRESS_2)));
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.AREA_ID)));
                holder.setMobile(cursor.getString(cursor.getColumnIndex(TableColumns.MOBILE)));
                holder.setIsDeleted(cursor.getInt(cursor.getColumnIndex(TableColumns.ISDELETED)));
                holder.setDateModified(cursor.getString(cursor.getColumnIndex(TableColumns.DATE_MODIFIED)));
                list.add(holder);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return list;
    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }
}
