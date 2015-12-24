package com.milky.utils;

import android.app.Application;
import android.content.SharedPreferences;

import com.milky.service.databaseutils.DatabaseHelper;

/**
 * Singleton class for access shared resource in application wide.
 * <p/>
 * Created by Neha on 12/2/2015.
 */
public class AppUtil extends Application {


    private static final String TAG = AppUtil.class.getSimpleName();
    private static AppUtil _instance;
    private DatabaseHelper _dbHandler;
    private SharedPreferences _sharedPRefrences;

    @Override
    public void onCreate() {
        super.onCreate();

        _instance = this;
        _dbHandler = new DatabaseHelper(getApplicationContext());
        _sharedPRefrences = getApplicationContext().getSharedPreferences(UserPrefrences.PREFRENCES, MODE_PRIVATE);

    }

    public static synchronized AppUtil getInstance() {
        return _instance;
    }


    public DatabaseHelper getDatabaseHandler() {
        return _dbHandler;
    }

    public SharedPreferences getPrefrences() {
        return _sharedPRefrences;
    }

}