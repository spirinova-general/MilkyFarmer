package com.milky.service.databaseutils.serviceinterface;


import com.milky.service.core.Bill;

import java.util.List;

public interface IBill {
    void insert(Bill bill);

    void update(Bill bill);

    List<Bill> getTotalAllBill();

    List<Bill> getOutstandingBill();

    List<Bill> getOutstandingBillsById(int id);

    List<Bill> getTotalBillById(int id);

    void updateBillById(Bill bill);

    void updateOutstandingBills(Bill bill);
    void updateRollDate();

    //Umesh - the ones to be used now
    void RecalculateAllOutstandingBills() throws Exception;
    List<Bill> getAllUnClearedBills();
    List<Bill> getBillsOfCustomer(int customerId);
}
