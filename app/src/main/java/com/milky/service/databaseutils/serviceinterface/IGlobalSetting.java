package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.GlobalSettings;

public interface IGlobalSetting {
    public void insert(GlobalSettings globalSettings);
    public void update(GlobalSettings globalSettings);
    public GlobalSettings getData();
}
