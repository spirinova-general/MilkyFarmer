package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Delivery;
import com.milky.viewmodel.VDelivery;
import java.util.List;

public interface IDelivery {
    void insert(Delivery delivery);

    void update(Delivery delivery);

    boolean isHasDataForDay(String day, int id);

    //For all customers delivery (Calendar)..
    List<Double> getAllTotalDeliveries(int start, int maxDay, int month, int year);

    //For customer delivery
    List<Double> getTotalDeliveriesForCustomer(int start, int maxDay, int month, int year);

    List<VDelivery> getDelivery(String date);

    List<VDelivery> getByAreaAndDay(int areaId, String day);

    double getTotalDeliveryOfCustomerTillDate(String date, int id);

    double getDeliveryOfCustomerTillDate(String date, int id);
}
