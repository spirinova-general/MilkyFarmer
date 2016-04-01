package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Customers;

import java.util.List;

public interface ICustomers {
    long insert(Customers customers);

    void update(Customers customers);

    void delete(Customers customers);

    List<Customers> getCustomersLisytByArea(int areaId);

    List<Customers> getAllCustomers();

    Customers getCustomerDetail(int id);

    boolean isAreaAssociated(int areaId);
}
