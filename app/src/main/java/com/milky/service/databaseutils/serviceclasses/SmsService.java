package com.milky.service.databaseutils.serviceclasses;

import com.milky.service.databaseutils.serviceinterface.ISmsService;
import com.milky.service.serverapi.HttpAsycTask;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.service.serverapi.ServerApis;

/**
 * Created by admin on 4/13/2016.
 */
public class SmsService implements ISmsService {
    @Override
    public void SendSms(String mobile, String message,OnTaskCompleteListner listner)
    {
        String append = "?mobile=" + mobile + "&message=" + message;
        HttpAsycTask dataTask = new HttpAsycTask();
        dataTask.runRequest(ServerApis.SMS_API_ROOT + append, null, listner, false, null);
    }
}
