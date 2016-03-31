package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;

import java.util.List;

public interface ICustomersSetting {
    public void insert(CustomersSetting customers);

    public void update(CustomersSetting customers);

    public void delete(CustomersSetting customers);

    public boolean isHasDataForDay(String date);
    public List<CustomersSetting> getByDate(String date);
    public List<CustomersSetting> getListByCustId(int custId,String day);
    public CustomersSetting getByCustId(int custId,String day);
    public boolean isHasDataForCustoner(String day, int id);
    public List<CustomersSetting> getCustomersByArea(int id,String day);
}
