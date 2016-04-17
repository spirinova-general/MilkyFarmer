package com.milky.service.serverapi;

/**
 * Created by Neha on 12/10/2015.
 */
public class ServerApis {
    public static final String RESULT_JSON = "json";
    //Root api for server connection.
    public static String API_ROOT = "http://131.72.139.186:10141/api/syncobject";
    //Accoumt api , to get account details
//    public static String ACCOUNT_API = "/";
    //Sync Api
    public static String SYNC = "/SyncData";

    public static int STATUS = 0;
    //Area api , to get area details
    public static String AREA_API = "/getallareas";

    //    SMS apis
//    http://login.wishbysms.com/api/sendhttp.php?authkey=82145AlwUkIYg9UKj551bafcf
    public static String SMS_API_ROOT = "http://milky.yaylo.com/api/syncobject/sendsms";
//    public static String SMS_API_POSTFIX = "&sender=KRUSHI&route=1&country=0";


    //Sign up ,to get sign upe detail
    public static String API_ACCOUNT_ADD="http://milky.yaylo.com/api/SyncObject/AddAccount";
    public static String API_ACCOUNT_GET="http://milky.yaylo.com/api/SyncObject/GetAccount";
}
