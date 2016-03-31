package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Account;

import org.json.JSONObject;

public interface IAccountService {
    public void insert(Account account);
    public void update(Account account);
    public void delete(Account account);
    public Account getDetails();
    public String getRollDate();
    public int getLeftSMS();
    public int getUsedSMS();
    public boolean isAccountExpired();
    public JSONObject getJsonData();
    public void updateSMSCount(int count);
}
