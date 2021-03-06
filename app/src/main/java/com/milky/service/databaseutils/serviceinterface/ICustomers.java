package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.serviceclasses.QuantityAmount;

import java.util.Date;
import java.util.List;

public interface ICustomers {
    long insert(Customers customers);

    void update(Customers customers);

    List<Customers> getCustomersListByArea(int areaId);

    List<Customers> getAllCustomers();

    Customers getCustomerDetail(int id);

    boolean isAreaAssociated(int areaId);
    void delete(int customerId);


    List<Customers> getCustomersWithinDeliveryRange(Integer areaId, Date startDateObj, Date endDateObj);
    CustomersSetting getCustomerSetting(Customers customer, Date date, boolean populateSettings, boolean ignoreCustomDelivery) throws Exception;
    QuantityAmount getTotalQuantityAndAmount(Customers customer, Date startDate, Date endDate) throws Exception;
    Customers getCustomerDetail(int id, boolean populateSettings);
}
