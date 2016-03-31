package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Customers;

import java.util.List;

public interface ICustomersService {
    public void insert(Customers customers);
    public void update(Customers customers);
    public void delete(Customers customers);
    public List<Customers> getByArea(int areaId);
    public List<Customers> getAllCustomers();
}
