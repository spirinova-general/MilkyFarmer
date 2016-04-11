package com.milky.service.databaseutils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    SQLiteDatabase db;

 //Database for External Storage use..

    public DatabaseHelper(final Context context) {
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + "milky"
                + File.separator + DatabaseVersioControl.DATABASE_NAME, null, DatabaseVersioControl.DATABASE_VERSION);
    }

    //Database for internal use..
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
//                + TableNames.ACCOUNT + " ADD COLUMN " + TableColumns.RollDate + " TEXT NOT NULL DEFAULT '" + date + "'";
//        if (!Account.columnRollDateExists(db))
//            db.execSQL(ADD_ROLL_DATE_COLUMN);
//
//        final String ADD_ROLL_DATE_COLUMN_BILL = "ALTER TABLE "
//                + TableNames.Bill + " ADD COLUMN " + TableColumns.RollDate + " TEXT NOT NULL DEFAULT '" + date + "'";
//        if (!BillTableManagement.columnRollDateExists(db))
//            db.execSQL(ADD_ROLL_DATE_COLUMN_BILL);
//        final String DELETED_FOR_BILLS = "ALTER TABLE "
//                + TableNames.Bill + " ADD COLUMN " + TableColumns.DeletedOn + " TEXT NOT NULL DEFAULT '" +"1" + "'";
//        if (!BillTableManagement.columnRollDateExistsDeletedOn(db))
//            db.execSQL(DELETED_FOR_BILLS);
        String areaIndex = "CREATE INDEX " + TableColumns.IndexCustomerArea + " ON " + TableNames.CUSTOMER + " (" + TableColumns.AreaId +" )";
        db.execSQL(areaIndex);
        String custIndex = "CREATE INDEX " + TableColumns.IndexBillCustomer + " ON " + TableNames.Bill + " (" + TableColumns.CustomerId + " )";
        db.execSQL(custIndex);
        String custSettingIndex = "CREATE INDEX " + TableColumns.Index_Customer_Customer_Setting + " ON " + TableNames.CustomerSetting + " (" + TableColumns.CustomerId+ " )";
        db.execSQL(custSettingIndex);
        //        Adding custId Index..
        String deliveryIndex = "CREATE INDEX " + TableColumns.Index_Delivery_Customer + " ON " + TableNames.DELIVERY + " (" + TableColumns.CustomerId+ " )";
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
//                + TableNames.ACCOUNT + " ADD COLUMN " + TableColumns.RollDate + " TEXT NOT NULL DEFAULT '" + date + "'";
//
//        final String ADD_ROLL_DATE_COLUMN_BILL = "ALTER TABLE "
//                + TableNames.Bill + " ADD COLUMN " + TableColumns.RollDate + " TEXT NOT NULL DEFAULT '" + date + "'";
//        final String ADD_DELETE_COLUMN_TO_BILL = "ALTER TABLE "
//                + TableNames.Bill + " ADD COLUMN " + TableColumns.DeletedOn + " TEXT NOT NULL DEFAULT '" + "1" + "'";
//        if (newVersion > oldVersion && !Account.columnRollDateExists(db))
//            db.execSQL(ADD_ROLL_DATE_COLUMN);
//        if (newVersion > oldVersion && !BillTableManagement.columnRollDateExists(db))
//            db.execSQL(ADD_ROLL_DATE_COLUMN_BILL);
//        if (newVersion > oldVersion && !BillTableManagement.columnRollDateExistsDeletedOn(db))
//            db.execSQL(ADD_DELETE_COLUMN_TO_BILL);


//        Adding indexes..
//        try {
//            String areaIndex = "CREATE UNIQUE INDEX " + TableColumns.IndexCustomerArea + " ON " + TableNames.CUSTOMER + " (" + TableColumns.AreaId + ", " + TableColumns.ID + ", " + TableColumns.ID + " )";
//            db.execSQL(areaIndex);
//            String custIndex = "CREATE UNIQUE INDEX " + TableColumns.CUSTID_INDEX + " ON " + TableNames.Bill + " (" + TableColumns.ID + ", " + TableColumns.ID + " )";
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

    public boolean columnIndexExists(SQLiteDatabase db, String table, String index, String indexObj) {
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

    public void updateSyncInfo(SQLiteDatabase db, String tableName) {
        ContentValues values = new ContentValues();
        values.put(TableColumns.Dirty, "1");
        db.update(tableName, values, TableColumns.Dirty + " ='0'"
                , null);
    }

}
