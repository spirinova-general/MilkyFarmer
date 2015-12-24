package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.milky.viewmodel.VAreaMapper;

import java.util.ArrayList;

/**
 * Created by Sensation on 12/16/2015.
 */
public class AccountAreaMapping {
    public static void insertmappedareas(SQLiteDatabase db, VAreaMapper holder) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.ACCOUNT_ID, holder.getAccountId());
        values.put(TableColumns.AREA_ID, holder.getAreaId());
        values.put(TableColumns.SYNC_STATUS,"0");
        db.insert(TableNames.TABLE_ACCOUNT_AREA_MAPPING, null, values);
    }
    public static boolean hasData(SQLiteDatabase db, String arrID) {
        String selectQuery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT_AREA_MAPPING + " WHERE "
                + TableColumns.AREA_ID + " ='" + arrID + "'"
                ;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;

    } public static ArrayList<String> getArea(SQLiteDatabase db) {
        String selectquery = "SELECT * FROM " + TableNames.TABLE_ACCOUNT_AREA_MAPPING ;
        Cursor cursor = db.rawQuery(selectquery, null);
        ArrayList<String> areaList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {

                if (cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)) != null)
                    areaList.add(cursor.getString(cursor.getColumnIndex(TableColumns.AREA_ID)));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        if (db.isOpen())
            db.close();
        return areaList;
    }
    public static boolean deleteArea(SQLiteDatabase db, String areaId)
    {
      return db.delete(TableNames.TABLE_ACCOUNT_AREA_MAPPING,TableColumns.AREA_ID+" ='"+areaId+"'",null)>0;
    }
}
