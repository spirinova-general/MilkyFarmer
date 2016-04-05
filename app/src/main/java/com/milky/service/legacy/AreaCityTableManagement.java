package com.milky.service.legacy;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.service.core.Area;
import com.milky.service.databaseutils.TableColumns;
import com.milky.service.databaseutils.TableNames;

/**
 * Created by Neha on 12/2/2015.
 */
public class AreaCityTableManagement {
    public static long insertAreaDetail(SQLiteDatabase db, Area holder) {
        ContentValues values = new ContentValues();
//        values.put(TableColumns.ServerAccountId, holder.getAccountId());
//        values.put(TableColumns.City, holder.getArea());
//        values.put(TableColumns.AreaId, holder.getAreaId());
//        values.put(TableColumns.CITY_ID, holder.getCityId());
        values.put(TableColumns.Dirty,"1");
        values.put(TableColumns.City, holder.getArea());
//        values.put(TableColumns.CITY_NAME, holder.getCity());
        values.put(TableColumns.Locality, holder.getLocality());
//        values.put(TableColumns.CITY_ID, holder.getCityId());
        return db.insert(TableNames.AREA, null, values);
    }


//    public static Area getAreaById(SQLiteDatabase db, final int areaid) {
//        String selectquery = "SELECT * FROM " + TableNames.AREA + " WHERE " + TableColumns.ID + " ='" + areaid + "'";
//        Cursor cursor = db.rawQuery(selectquery, null);
//        Area holder = null;
//        if (cursor.moveToFirst()) {
//            do {
//                holder = new Area();
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.City)) != null)
//                    holder.setArea(cursor.getString(cursor.getColumnIndex(TableColumns.City)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)) != null)
//                    holder.setCity(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Locality)) != null)
//                    holder.setLocality(cursor.getString(cursor.getColumnIndex(TableColumns.Locality)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.ID)) != null)
//                    holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
//
//            }
//            while (cursor.moveToNext());
//
//        }
//        cursor.close();
//
//        return holder;
//    }

    public static String getAreaNameById(SQLiteDatabase db, final int areaId) {
        String selectquery = "SELECT * FROM " + TableNames.AREA + " WHERE " + TableColumns.ID + " ='" + areaId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String area = "";
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.City)) != null)
                    area = cursor.getString(cursor.getColumnIndex(TableColumns.City));
            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return area;
    }

    public static String getLocalityById(SQLiteDatabase db, final int areaId) {
        String selectquery = "SELECT * FROM " + TableNames.AREA + " WHERE " + TableColumns.ID + " ='" + areaId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String area = "";
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.Locality)) != null)
                    area = cursor.getString(cursor.getColumnIndex(TableColumns.Locality));
            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return area;
    }

    public static String getCityNameById(SQLiteDatabase db, final int cityId) {
        String selectquery = "SELECT * FROM " + TableNames.AREA + " WHERE " + TableColumns.ID + " ='" + cityId + "'";
        Cursor cursor = db.rawQuery(selectquery, null);
        String city = "";
        if (cursor.moveToFirst()) {
            do {

//                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)) != null) {
//                    city = cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME));
//                }

            }
            while (cursor.moveToNext());

        }
        cursor.close();

        return city;
    }

//    public static ArrayList<VAreaMapper> getAddress(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.AREA;
//        Cursor cursor = db.rawQuery(selectquery, null);
//        ArrayList<VAreaMapper> areaList = new ArrayList<>();
//
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                VAreaMapper holder = new VAreaMapper();
//                holder.setAreaId(cursor.getString(cursor.getColumnIndex(TableColumns.ID)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.City)) != null)
//                    holder.setArea(cursor.getString(cursor.getColumnIndex(TableColumns.City)));
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)) != null)
//                    holder.setCity(cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME)));
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Locality)) != null)
//                    holder.setLocality(cursor.getString(cursor.getColumnIndex(TableColumns.Locality)));
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

//    public static ArrayList<Area> getFullAddress(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.AREA;
//        Cursor cursor = db.rawQuery(selectquery, null);
//        ArrayList<Area> areaList = new ArrayList<>();
//        String address = "";
//
//        if (cursor.moveToFirst()) {
//            do {
//
//                Area holder = new Area();
//                holder.setAreaId(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.Locality)).equals("")) {
//                    address = cursor.getString(cursor.getColumnIndex(TableColumns.City)) + ", "
//                            + cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME));
//
//                } else
//                    address = cursor.getString(cursor.getColumnIndex(TableColumns.Locality)) + ", " + cursor.getString(cursor.getColumnIndex(TableColumns.City)) + ", "
//                            + cursor.getString(cursor.getColumnIndex(TableColumns.CITY_NAME));
//                holder.setCityArea(address);
//                areaList.add(holder);
//            }
//            while (cursor.moveToNext());
//
//        }
//        cursor.close();
//
//        return areaList;
//    }



//    public static boolean hasAddress(SQLiteDatabase db, String locality, String area, String city) {
//        String selectQuery = "";
//        if (locality.equals(""))
//            selectQuery = "SELECT * FROM " + TableNames.AREA + " WHERE " + TableColumns.Locality + " ='' AND ((" + TableColumns.CITY_NAME + " ='" + city + "' COLLATE NOCASE"
//                    + " AND " + TableColumns.City + " ='" + area + "' COLLATE NOCASE )OR ("
//                    + TableColumns.CITY_NAME + " ='" + area + "' COLLATE NOCASE"
//                    + " AND " + TableColumns.City + " ='" + city + "' COLLATE NOCASE))";
//
//        else
//            selectQuery = "SELECT * FROM " + TableNames.AREA + " WHERE " + TableColumns.Locality + " ='" + locality + "' COLLATE NOCASE"
//                    + " AND ((" + TableColumns.CITY_NAME + " ='" + city + "' COLLATE NOCASE" + " AND " + TableColumns.City + " ='" + area + "' COLLATE NOCASE)"
//                    + " OR (" + TableColumns.CITY_NAME + " ='" + area + "' COLLATE NOCASE" + " AND " + TableColumns.City + " ='" + city + "' COLLATE NOCASE))";
//
////        selectQuery = "SELECT * FROM " + TableNames.AREA + " WHERE "+TableColumns.Locality+" ='"+locality+"' COLLATE NOCASE"
////                +" AND (("+TableColumns.CITY_NAME+" ='"+city+"' COLLATE NOCASE"+ " AND "+TableColumns.City+" ='"+area+"' COLLATE NOCASE)"
////                +" OR (" +TableColumns.CITY_NAME+" ='"+area+"' COLLATE NOCASE"+ " AND "+TableColumns.City+" ='"+city+"' COLLATE NOCASE))";
//        Cursor cursor = db.rawQuery(selectQuery, null);
//        Boolean result = cursor.getCount() > 0;
//
//        cursor.close();
//        return result;
//
//    }

//    public static boolean deleteArea(SQLiteDatabase db, int areaId) {
//        Boolean result = db.delete(TableNames.AREA, TableColumns.ID + " ='" + areaId + "'", null) > 0;
//        return result;
//    }

//    public static ArrayList<Integer> getArea(SQLiteDatabase db) {
//        String selectquery = "SELECT * FROM " + TableNames.AREA;
//        Cursor cursor = db.rawQuery(selectquery, null);
//        ArrayList<Integer> areaList = new ArrayList<>();
//        if (cursor.moveToFirst()) {
//            do {
//
//                if (cursor.getString(cursor.getColumnIndex(TableColumns.ID)) != null)
//                    areaList.add(cursor.getInt(cursor.getColumnIndex(TableColumns.ID)));
//            }
//            while (cursor.moveToNext());
//
//        }
//        cursor.close();
//
//        return areaList;
//    }

}
