package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;

import java.util.Date;
import java.util.List;

public interface ICustomersSettings {
    void insert(CustomersSetting customers);

    void update(CustomersSetting customers);

    void delete(CustomersSetting customers);

    boolean isHasDataForDay(String date);

    List<CustomersSetting> getByDate(String date);

    List<CustomersSetting> getListByCustId(int custId, String day);

    CustomersSetting getByCustId(int custId, String day);

    boolean isHasDataForCustoner(String day, int id);

    List<CustomersSetting> getCustomersByArea(int id, String day);



    String getEndDate(int id, String date);
}
