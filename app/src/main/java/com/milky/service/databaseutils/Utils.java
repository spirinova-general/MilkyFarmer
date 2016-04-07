package com.milky.service.databaseutils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        return ToDateString(date.getDay(), date.getMonth(), date.getYear());
    }

    //Umesh - Correct the format and use similar method for above function too. These functionos can be later part of Utils
    //They should be used everywhere you convert dates to and from strings...
    public static Date FromDateString(String date) throws ParseException
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(date);
    }
}
