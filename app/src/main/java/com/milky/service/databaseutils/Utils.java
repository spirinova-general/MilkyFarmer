package com.milky.service.databaseutils;

import com.milky.service.serverapi.HttpAsycTask;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.service.serverapi.ServerApis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 4/7/2016.
 */
//umesh - may be we should merge the other util classes - we should move them to this class...
public class Utils {

    //Umesh - Should have used in built Date functions...TOString for formatting...this function should be used across for
    //date formatting
    public static String ToDateString(int day, int month, int year)
    {
        return String.valueOf(year) + "-" + String.format("%02d", month) +
                "-" + String.format("%02d", day);
    }

    public static String ToDateString(Date date)
    {
        //Umesh - change to calendar ones
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static String ToDateString(Date date, boolean zeroMinutesAndSeconds)
    {
        if( zeroMinutesAndSeconds) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.setTime(date);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            date = cal.getTime();
        }
        return ToDateString(date);
    }

    //Umesh - Correct the format and use similar method for above function too. These functionos can be later part of Utils
    //They should be used everywhere you convert dates to and from strings...
    public static Date FromDateString(String date)
    {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            return format.parse(date);
        }
        catch(ParseException ex) {
            //Will fix and do something later
            ex.printStackTrace();
            return null;
        }
    }

    public static Date GetMaxDate()
    {
        Calendar c = Calendar.getInstance();
        //This must change, have just kept this arbitrary date as it was there earlier
        //return new Date(Long.MAX_VALUE);
        return FromDateString("2999-12-30");
    }

    public static boolean BeforeDate(Date date1, Date date2)
    {
        date1 = GetDateWithoutTime(date1);
        date2 = GetDateWithoutTime(date2);

        return date1.before(date2);
    }

    public static boolean BeforeOrEqualsDate(Date date1, Date date2){
        date1 = GetDateWithoutTime(date1);
        date2 = GetDateWithoutTime(date2);

        return (date1.before(date2) || date1.equals(date2));

    }

    public static Date GetDateWithoutTime(Date d)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTime();
    }

    public static boolean AfterDate(Date date1, Date date2)
    {
        date1 = GetDateWithoutTime(date1);
        date2 = GetDateWithoutTime(date2);

        return date1.after(date2);
    }

    public static boolean EqualsDate(Date date1, Date date2){
        date1 = GetDateWithoutTime(date1);
        date2 = GetDateWithoutTime(date2);

        return date1.equals(date2);
    }

    public static boolean GetBoolean(int i)
    {
        if( i == 0)
            return false;
        else
            return true;
    }
}
