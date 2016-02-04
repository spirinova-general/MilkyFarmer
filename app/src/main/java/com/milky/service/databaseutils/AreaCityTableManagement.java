package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.viewmodel.VAreaMapper;

import java.util.ArrayList;

/**
 * Created by Neha on 12/2/2015.
 */
public class AreaCityTableManagement {
    public static long insertAreaDetail(SQLiteDatabase db, VAreaMapper holder) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
//        values.put(TableColumns.AREA_NAME, holder.getArea());
//        values.put(TableColumns.AREA_ID, holder.getAreaId());
//        values.put(TableColumns.CITY_ID, holder.getCityId());
//        values.put(TableColumns.SYNC_STATUS,"0");
        values.put(TableColumns.AREA_NAME, holder.getArea());
        values.put(TableColumns.CITY_NAME, holder.getCity());
        values.put(TableColumns.LOCALITY, holder.getLocality());
//        values.put(TableColumns.CITY_ID, holder.getCityId());
        values.put(TableColumns.SYNC_STATUS, "0");
        return db.insert(TableNames.TABLE_AREA, null, values);
    }

    public static void insertCityDetail(SQLiteDatabase db, VAreaMapper holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
//        values.put(TableColumns.CITY_ID, holder.getCityId());
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
//                    holder.setCityId(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_ID)));

                areaList.add(holder);
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return areaList;
    }

    public static VAreaMapper getAreaById(SQLiteDatabase db, final String accountId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.ID + " ='" + accountId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        VAreaMapper holder = null;
        if (cursor.moveToFirst()) {
            do {
                 holder = new VAreaMapper();
                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)) != null)
                    holder.setArea(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)) != null)
                    holder.setCity(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.LOCALITY)) != null)
                    holder.setLocality(cursor.getString(cursor.getColumnIndex(TableColumns.LOCALITY)));
                if (cursor.getString(cursor.getColumnIndex(TableColumns.ID)) != null)
                    holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return holder;
    }

    public static String getAreaNameById(SQLiteDatabase db, final String areaId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.ID + " ='" + areaId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String area = "";
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
    public static String getLocalityById(SQLiteDatabase db, final String areaId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.ID + " ='" + areaId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String area = "";
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.LOCALITY)) != null)
                    area = cursor.getString(cursor.getColumnIndex(TableColumns.LOCALITY));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return area;
    }

    public static String getCityNameById(SQLiteDatabase db, final String cityId) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE " + TableColumns.ID + " ='" + cityId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String city = "";
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)) != null) {
                    city = cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME));
                }

            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return city;
    }

//    public static ArrayList<VAreaMapper> getAddress(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA;
//        Cursor cursor = db.rawQuery(selectquery, null);
//        ArrayList<VAreaMapper> areaList = new ArrayList<>();
//
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                VAreaMapper holder = new VAreaMapper();
//                holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)) != null)
//                    holder.setArea(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)) != null)
//                    holder.setCity(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)));
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.LOCALITY)) != null)
//                    holder.setLocality(cursor.getString(cursor.getColumnIndex(TableColumns.LOCALITY)));
//
//                areaList.add(holder);
//            }
//            while (cursor.moveToNext());
//
//        }
//        cursor.close();
//        if (db.isOpen())
//            db.close();
//        return areaList;
//    }

    public static ArrayList<VAreaMapper> getFullAddress(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA;
        Cursor cursor = db.rawQuery(selectquery, null);
        ArrayList<VAreaMapper> areaList = new ArrayList<>();
        String address ="";

        if (cursor.moveToFirst()) {
            do {

                VAreaMapper holder = new VAreaMapper();
                holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));

                if(cursor.getString(cursor.getColumnIndex(TableColumns.LOCALITY)).equals(""))
                {
                    address = cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME))+", "
                            +cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME));

                }
                else
                    address = cursor.getString(cursor.getColumnIndex(TableColumns.LOCALITY))+", "+cursor.getString(cursor.getColumnIndex(TableColumns.AREA_NAME))+", "
                            +cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME));
            holder.setCityArea(address);
                areaList.add(holder);
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return areaList;
    }



    public static boolean hasArea(SQLiteDatabase db, String area) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE "
                + TableColumns.AREA_NAME + " ='" + area + "'";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;

    }

    public static boolean hasCity(SQLiteDatabase db, String area) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE "
                + TableColumns.CITY_NAME + " ='" + area + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;

    }

    public static boolean hasLocation(SQLiteDatabase db, String area) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE "
                + TableColumns.LOCALITY + " ='" + area + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;

    }
    public static boolean hasAddress(SQLiteDatabase db, String locality,String area,String city) {
        String selectQuery="";
        if(locality.equals(""))
            selectQuery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE "+TableColumns.LOCALITY+" ='' AND (("+TableColumns.CITY_NAME+" ='"+city+"' COLLATE NOCASE"
                    + " AND "+TableColumns.AREA_NAME+" ='"+area+"' COLLATE NOCASE )OR ("
               +TableColumns.CITY_NAME+" ='"+area+"' COLLATE NOCASE"
                + " AND "+TableColumns.AREA_NAME+" ='"+city+"' COLLATE NOCASE))";

        else
            selectQuery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE "+TableColumns.LOCALITY+" ='"+locality+"' COLLATE NOCASE"
                    +" AND (("+TableColumns.CITY_NAME+" ='"+city+"' COLLATE NOCASE"+ " AND "+TableColumns.AREA_NAME+" ='"+area+"' COLLATE NOCASE)"
                    +" OR (" +TableColumns.CITY_NAME+" ='"+area+"' COLLATE NOCASE"+ " AND "+TableColumns.AREA_NAME+" ='"+city+"' COLLATE NOCASE))";

//        selectQuery = "SELECT * FROM " + TableNames.TABLE_AREA + " WHERE "+TableColumns.LOCALITY+" ='"+locality+"' COLLATE NOCASE"
//                +" AND (("+TableColumns.CITY_NAME+" ='"+city+"' COLLATE NOCASE"+ " AND "+TableColumns.AREA_NAME+" ='"+area+"' COLLATE NOCASE)"
//                +" OR (" +TableColumns.CITY_NAME+" ='"+area+"' COLLATE NOCASE"+ " AND "+TableColumns.AREA_NAME+" ='"+city+"' COLLATE NOCASE))";
        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;

    }
    public static boolean deleteArea(SQLiteDatabase db, String areaId) {
        Boolean result = db.delete(TableNames.TABLE_AREA, TableColumns.ID + " ='" + areaId + "'", null) > 0;
        return  result;
    }
    public static ArrayList<String> getArea(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_AREA ;
        Cursor cursor = db.rawQuery(selectquery, null);
        ArrayList<String> areaList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.ID)) != null)
                    areaList.add(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return areaList;
    }

}
