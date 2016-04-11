package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.GlobalSettings;

public interface IGlobalSetting {
    void insert(GlobalSettings globalSettings);

    void update(GlobalSettings globalSettings);

    GlobalSettings getData();

    String getRollDate();
    void setNextRollDate();

}
