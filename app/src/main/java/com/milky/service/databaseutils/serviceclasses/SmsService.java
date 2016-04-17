package com.milky.service.databaseutils.serviceclasses;

import com.milky.service.databaseutils.serviceinterface.IAccountService;
import com.milky.service.databaseutils.serviceinterface.ISmsService;
import com.milky.service.serverapi.HttpAsycTask;
import com.milky.service.serverapi.OnTaskCompleteListner;
import com.milky.service.serverapi.ServerApis;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by admin on 4/13/2016.
 */
public class SmsService implements ISmsService {
    IAccountService _accountService = new AccountService();

    @Override
    public void SendSms(String mobile, String message,OnTaskCompleteListner listner, boolean incrementUsedSmsCount)
    {
        String append = "?mobile=" + mobile + "&message=" + message;
        HttpAsycTask dataTask = new HttpAsycTask();
        if( incrementUsedSmsCount )
            _accountService.incrementUsedSMSCount(1);
        dataTask.runRequest(ServerApis.SMS_API_ROOT + append, null, listner, false, null);
    }

    @Override
    public void SendOtp(String mobile, String Otp, OnTaskCompleteListner listner) {

        try {
            String mesg = URLEncoder.encode("OTP to sign in KrushiVikas is: " + Otp, "UTF-8");
            SendSms(mobile, mesg, listner, false);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
