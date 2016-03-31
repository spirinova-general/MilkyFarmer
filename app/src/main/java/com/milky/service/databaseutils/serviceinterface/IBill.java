package com.milky.service.databaseutils.serviceinterface;


import com.milky.service.core.Bill;

import java.util.List;

public interface IBill {
    public void insert(Bill bill);

    public void update(Bill bill);

    public List<Bill> getAllBill();

    public List<Bill> getOutstandingBill();

    public List<Bill> getBillById(int id);
}
