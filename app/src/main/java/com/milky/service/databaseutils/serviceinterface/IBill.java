package com.milky.service.databaseutils.serviceinterface;


import com.milky.service.core.Bill;

import java.util.List;

public interface IBill {
    //Insert new bill
    void insert(Bill bill);

    //Update bills
    void updateBills(Bill bill);

    //getAll uncleared bills by date
    List<Bill> getUnclearedBills(String day, boolean recalculate);

    //Get uncleared bills by customer id and day
    List<Bill> getUnclearedBillById(String day,int id);

}
