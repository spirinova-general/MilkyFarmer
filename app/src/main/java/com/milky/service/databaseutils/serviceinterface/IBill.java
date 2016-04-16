package com.milky.service.databaseutils.serviceinterface;


import com.milky.service.core.Bill;
import com.milky.service.serverapi.OnTaskCompleteListner;

import java.util.List;

public interface IBill {
    void insert(Bill bill);

    void update(Bill bill);

    void SmsBill(int billId,OnTaskCompleteListner listner);

    void RecalculateAllCurrentBills();
    //To be called from global bill tab
    List<Bill> getAllGlobalBills(boolean reCalculate);
    //To be called from customer bill tab
    List<Bill> getBillsOfCustomer(int customerId);
    Bill getBill(int id);
    void clearBill(Bill bill, double paymentMade);
}
