package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    SQLiteDatabase db;
    public DatabaseHelper(final Context context) {
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + "milky"
                + File.separator + DatabaseVersioControl.DATABASE_NAME, null, DatabaseVersioControl.DATABASE_VERSION);
    }

//    public DatabaseHelper(Context context) {
//        super(context, DatabaseVersioControl.DATABASE_NAME, null,
//                DatabaseVersioControl.DATABASE_VERSION);
//        this.context = context;
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableColumsDetail.ACCOUNT);
        db.execSQL(TableColumsDetail.AREA);
        db.execSQL(TableColumsDetail.CUSTOMER);
        db.execSQL(TableColumsDetail.CUSTOMER_SETTINGS);
        db.execSQL(TableColumsDetail.DELIVERY);
        db.execSQL(TableColumsDetail.CUSTOMERS_BILL);
        db.execSQL(TableColumsDetail.GLOBAL_ETTINGS);
//Add extra tables
//       Calendar cal = Calendar.getInstance();
//        String date = String.valueOf(cal.get(Calendar.YEAR)) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) +
//                "-" + String.format("%02d", cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//
//        final String ADD_ROLL_DATE_COLUMN = "ALTER TABLE "
//                + TableNames.TABLE_ACCOUNT + " ADD COLUMN " + TableColumns.ROLL_DATE + " TEXT NOT NULL DEFAULT '" + date + "'";
//        if (!Account.columnRollDateExists(db))
//            db.execSQL(ADD_ROLL_DATE_COLUMN);
//
//        final String ADD_ROLL_DATE_COLUMN_BILL = "ALTER TABLE "
//                + TableNames.TABLE_CUSTOMER_BILL + " ADD COLUMN " + TableColumns.ROLL_DATE + " TEXT NOT NULL DEFAULT '" + date + "'";
//        if (!BillTableManagement.columnRollDateExists(db))
//            db.execSQL(ADD_ROLL_DATE_COLUMN_BILL);
//        final String DELETED_FOR_BILLS = "ALTER TABLE "
//                + TableNames.TABLE_CUSTOMER_BILL + " ADD COLUMN " + TableColumns.DELETED_ON + " TEXT NOT NULL DEFAULT '" +"1" + "'";
//        if (!BillTableManagement.columnRollDateExistsDeletedOn(db))
//            db.execSQL(DELETED_FOR_BILLS);
        String areaIndex = "CREATE UNIQUE INDEX " + TableColumns.AREA_INDEX + " ON " + TableNames.TABLE_CUSTOMER + " (" + TableColumns.AREA_ID + ", " + TableColumns.ID +  " )";
        db.execSQL(areaIndex);
        String custIndex = "CREATE UNIQUE INDEX " + TableColumns.BILL_INDEX + " ON " + TableNames.TABLE_CUSTOMER_BILL + " (" + TableColumns.ID + " )";
        db.execSQL(custIndex);
        String custSettingIndex = "CREATE UNIQUE INDEX " + TableColumns.CUSTOMER_SETTING_INDEX + " ON " + TableNames.TABLE_CUSTOMER_SETTINGS + " (" +TableColumns.ID+", "+TableColumns.ID+ " )";
        db.execSQL(custSettingIndex);
        //        Adding custId Index..
        String deliveryIndex = "CREATE UNIQUE INDEX " + TableColumns.DELIVERY_INDEX + " ON " + TableNames.TABLE_DELIVERY + " (" +TableColumns.ID+", "+TableColumns.ID+ " )";
        db.execSQL(deliveryIndex);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        for (String tableName : tables) {
//            db.execSQL(String.format("DROP TABLE IF EXISTS %s", tableName));
//        }
//        Calendar cal = Calendar.getInstance();
//        String date = String.valueOf(cal.get(Calendar.YEAR)) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1) +
//                "-" + String.format("%02d", cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//        final String ADD_ROLL_DATE_COLUMN = "ALTER TABLE "
//                + TableNames.TABLE_ACCOUNT + " ADD COLUMN " + TableColumns.ROLL_DATE + " TEXT NOT NULL DEFAULT '" + date + "'";
//
//        final String ADD_ROLL_DATE_COLUMN_BILL = "ALTER TABLE "
//                + TableNames.TABLE_CUSTOMER_BILL + " ADD COLUMN " + TableColumns.ROLL_DATE + " TEXT NOT NULL DEFAULT '" + date + "'";
//        final String ADD_DELETE_COLUMN_TO_BILL = "ALTER TABLE "
//                + TableNames.TABLE_CUSTOMER_BILL + " ADD COLUMN " + TableColumns.DELETED_ON + " TEXT NOT NULL DEFAULT '" + "1" + "'";
//        if (newVersion > oldVersion && !Account.columnRollDateExists(db))
//            db.execSQL(ADD_ROLL_DATE_COLUMN);
//        if (newVersion > oldVersion && !BillTableManagement.columnRollDateExists(db))
//            db.execSQL(ADD_ROLL_DATE_COLUMN_BILL);
//        if (newVersion > oldVersion && !BillTableManagement.columnRollDateExistsDeletedOn(db))
//            db.execSQL(ADD_DELETE_COLUMN_TO_BILL);



//        Adding indexes..
//        try {
//            String areaIndex = "CREATE UNIQUE INDEX " + TableColumns.AREA_INDEX + " ON " + TableNames.TABLE_CUSTOMER + " (" + TableColumns.AREA_ID + ", " + TableColumns.ID + ", " + TableColumns.ID + " )";
//            db.execSQL(areaIndex);
//            String custIndex = "CREATE UNIQUE INDEX " + TableColumns.CUSTID_INDEX + " ON " + TableNames.TABLE_CUSTOMER_BILL + " (" + TableColumns.ID + ", " + TableColumns.ID + " )";
//            db.execSQL(custIndex);
//        }
//        catch (SQLiteException exp)
//        {
//        }

//        SharedPreferences preferences = AppUtil.getInstance().getSharedPreferences(UserPrefrences.PREFRENCES,Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = preferences.edit();
//        editor.clear();
//        editor.commit();

//        onCreate(db);
    }

    public static boolean columnIndexExists(SQLiteDatabase db, String table, String index, String indexObj) {
        String selectQuery = "SELECT * FROM sys.indexes WHERE name ='" + index + "' AND object_id =" + indexObj + "('" + table + "')";

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public boolean isTableNotEmpty(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + table;

        Cursor cursor = db.rawQuery(selectQuery, null);
        Boolean result = cursor.getCount() > 0;

        cursor.close();
        return result;
    }

    public static void updateSyncInfo(SQLiteDatabase db, String tableName) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.DIRTY, "1");

        db.update(tableName, values, TableColumns.DIRTY + " ='0'"
                , null);
    }

}
