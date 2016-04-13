package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Delivery;
import com.milky.viewmodel.VDelivery;

import java.util.List;

public interface IDelivery {
     void insert(Delivery delivery);

     void update(Delivery delivery);


     List<Double> getMonthlyDeliveryOfAllCustomers(int month, int year);
     List<Double> getMonthlyDeliveryOfCustomer(int customerId, int month, int year);
     List<VDelivery> getDeliveryDetails(String date);
     List<VDelivery> getDeliveryDetails(Integer areaId, String day);
}
