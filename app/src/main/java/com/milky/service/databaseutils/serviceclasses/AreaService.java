package com.milky.service.databaseutils.serviceclasses;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Area;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;
import com.milky.service.databaseutils.serviceinterface.IArea;
import com.milky.utils.AppUtil;

import java.util.ArrayList;
import java.util.List;

public class AreaService implements IArea  {
    @Override
    public long insert(Area area) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY,"1");
        values.put(TableColumns.AREA_NAME, area.getArea());
        values.put(TableColumns.CITY_NAME, area.getCity());
        values.put(TableColumns.LOCALITY, area.getLocality());
        return getDb().insert(TableNames.TABLE_AREA, null, values);
    }

    @Override
    public List<Area> getAllArea() {
        return null;
    }

    @Override
    public boolean deleteAreaById(int areaId) {
        return getDb().delete(TableNames.TABLE_AREA, TableColumns.ID + " ='" + areaId + "'", null) > 0;

    }

    @Override
    public boolean hasAddress(String locality, String area, String city) {
        String selectQuery = "";
        if (locality.equals(""))
            selectQuery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.LOCALITY + " ='' AND ((" + TableColumns.CITY_NAME + " ='" + city + "' COLLATE NOCASE"
                    + " AND " + TableColumns.AREA_NAME + " ='" + area + "' COLLATE NOCASE )OR ("
                    + TableColumns.CITY_NAME + " ='" + area + "' COLLATE NOCASE"
                    + " AND " + TableColumns.AREA_NAME + " ='" + city + "' COLLATE NOCASE))";
        else
            selectQuery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.LOCALITY + " ='" + locality + "' COLLATE NOCASE"
                    + " AND ((" + TableColumns.CITY_NAME + " ='" + city + "' COLLATE NOCASE" + " AND " + TableColumns.AREA_NAME + " ='" + area + "' COLLATE NOCASE)"
                    + " OR (" + TableColumns.CITY_NAME + " ='" + area + "' COLLATE NOCASE" + " AND " + TableColumns.AREA_NAME + " ='" + city + "' COLLATE NOCASE))";

        Cursor cursor = getDb().rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    @Override
    public Area getAreaById(int areaId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.ID + " ='" + areaId + "'";
        Cursor cursor = getDb().rawQuery(selectquery, null);
        Area holder = null;
        if (cursor.moveToFirst()) {
            do {
                holder = new Area();
                    holder.setArea(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)));
                    holder.setCity(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)));
                    holder.setLocality(cursor.getString(cursor.getColumnIndex(TableColumns.LOCALITY)));
                    holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return holder;
    }

    @Override
    public List<Area> getStoredAddresses() {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA;
        Cursor cursor = getDb().rawQuery(selectquery, null);
        ArrayList<Area> areaList = new ArrayList<>();
        String address = "";

        if (cursor.moveToFirst()) {
            do {

                Area holder = new Area();
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setArea(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)));
                holder.setCity(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)));
                holder.setLocality(cursor.getString(cursor.getColumnIndex(TableColumns.LOCALITY)));
                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
                holder.setCityArea(holder.getFullAddress(holder));
                areaList.add(holder);
            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return areaList;
    }

    private SQLiteDatabase getDb() {
        return AppUtil.getInstance().getDatabaseHandler().getWritableDatabase();
    }

}
