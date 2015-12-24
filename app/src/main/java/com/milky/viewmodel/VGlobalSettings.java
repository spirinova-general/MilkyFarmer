package com.milky.viewmodel;

/**
 * Created by Neha on 11/30/2015.
 */
public class VGlobalSettings {
    private String id;
    private String accountId;
    private String dateModified;
    private String defaultRate;
    private String tax;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(String defaultRate) {
        this.defaultRate = defaultRate;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }
}
