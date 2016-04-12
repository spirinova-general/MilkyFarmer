package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.serviceclasses.QuantityAmount;

import java.util.Date;
import java.util.List;

public interface ICustomersSettings {
    void insert(CustomersSetting customers);

    void update(CustomersSetting customers);

    void delete(CustomersSetting customers);

}
