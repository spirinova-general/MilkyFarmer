package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.serverapi.OnTaskCompleteListner;

/**
 * Created by admin on 4/13/2016.
 */
public interface ISmsService {
    void SendSms(String mobile, String message,OnTaskCompleteListner listner);
}
