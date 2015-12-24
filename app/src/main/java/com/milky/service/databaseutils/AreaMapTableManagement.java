package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.viewmodel.VAreaMapper;

import java.util.ArrayList;

/**
 * Created by Neha on 12/2/2015.
 */
public class AreaMapTableManagement {
    public static void insertAreaDetail(SQLiteDatabase db, VAreaMapper holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.AREA_NAME, holder.getArea());
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.SYNC_STATUS,"0");
        values.put(TableColumns.CITY_ID, holder.getCityId());
        db.insert(TableNames.TABLE_AREA, null, values);
    }

    public static void insertCityDetail(SQLiteDatabase db, VAreaMapper holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.CITY_ID, holder.getCityId());
        values.put(TableColumns.CITY_NAME, holder.getCity());
        db.insert(TableNames.TABLE_CITY, null, values);
    }

    public static ArrayList<VAreaMapper> getCityById(SQLiteDatabase db, final String accountId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.ACCOUNT_ID + " ='" + accountId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        ArrayList<VAreaMapper> areaList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                VAreaMapper holder = new VAreaMapper();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)) != null)
                    holder.setCity(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)) != null)
                    holder.setCityId(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)));

                areaList.add(holder);
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return areaList;
    }

    public static ArrayList<VAreaMapper> getAreaById(SQLiteDatabase db, final String accountId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.ACCOUNT_ID + " ='" + accountId + "'";


        Cursor cursor = db.rawQuery(selectquery, null);
        ArrayList<VAreaMapper> areaList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                VAreaMapper holder = new VAreaMapper();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)) != null)
                    holder.setArea(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
                    holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)) != null)
                    holder.setCityId(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)));
                areaList.add(holder);
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return areaList;
    }
    public static VAreaMapper getAreabyAreaId(SQLiteDatabase db, final String arrId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.AREA_ID + " ='" + arrId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        VAreaMapper holder = new VAreaMapper();

        if (cursor.moveToFirst()) {
            do {
                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)) != null)
                    holder.setArea(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
                    holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)) != null)
                    holder.setCityId(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return holder;
    }
    public static String getAreaNameById(SQLiteDatabase db, final String areaId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.AREA_ID + " ='" + areaId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String area ="";
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)) != null)
                    area = cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return area;
    }

    public static String getCityNameById(SQLiteDatabase db, final String cityId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CITY + " WHERE " + TableColumns.CITY_ID + " ='" + cityId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String city ="";
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)) != null){
                    city =   cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME));
                }

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return city;
    }
    public static ArrayList<String> getallcities(SQLiteDatabase db, final String cityId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_CITY + " WHERE " + TableColumns.CITY_ID + " ='" + cityId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        ArrayList<String>citylist=new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)) != null){
                   citylist.add(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)));
                }

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return citylist;
    }

}
