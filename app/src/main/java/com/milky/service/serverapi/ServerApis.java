package com.milky.service.serverapi;

/**
 * Created by Neha on 12/10/2015.
 */
public class ServerApis {
    public static final String RESULT_JSON = "json";
    //Root api for server connection.
    public static String API_ROOT = "http://131.72.139.186:10141/api/syncobject";
    //Accoumt api , to get account details
    public static String ACCOUNT_API = "/";
    //Sync Api
    public static String SYNC = "/SyncData";

    public static int STATUS = 0;
    //Area api , to get area details
    public static String AREA_API = "/getallareas";
}
