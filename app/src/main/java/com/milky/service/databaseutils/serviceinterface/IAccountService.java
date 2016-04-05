package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Account;

import org.json.JSONObject;

public interface IAccountService {
     void insert(Account account);
     void update(Account account);
     void delete(Account account);
     Account getDetails();
     int getLeftSMS();
     int getUsedSMS();
     boolean isAccountExpired();
     JSONObject getJsonData();
     void updateSMSCount(int count);
}