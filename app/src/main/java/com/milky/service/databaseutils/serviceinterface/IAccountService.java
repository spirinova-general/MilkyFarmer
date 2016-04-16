package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Account;
import com.milky.service.serverapi.OnTaskCompleteListner;

import org.json.JSONObject;

public interface IAccountService {
     void insert(Account account);
     void update(Account account);
     Account getDetails();
     int getRemainingSMS();
     boolean isAccountExpired();
     JSONObject getJsonData();
     void incrementUsedSMSCount(int count);

}
