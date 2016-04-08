package com.milky.service.databaseutils.serviceinterface;


import com.milky.service.core.Bill;

import java.util.List;

public interface IBill {
    void insert(Bill bill);

    void update(Bill bill);

    //Umesh - this needs to be removed
    //List<Bill> getTotalAllBill();

    /*List<Bill> getOutstandingBill();

    List<Bill> getOutstandingBillsById(int id);

    List<Bill> getTotalBillById(int id);*/

    //void updateBillById(Bill bill);

    //void updateOutstandingBills(Bill bill);

    //Umesh - the ones to be used now
    void RecalculateAllOutstandingBills() throws Exception;
    //To be called from global bill tab
    List<Bill> getAllGlobalBills();
    //To be called from customer bill tab
    List<Bill> getBillsOfCustomer(int customerId);
}
