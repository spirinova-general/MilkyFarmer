package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Delivery;
import com.milky.viewmodel.VDelivery;

import java.util.List;

public interface IDelivery {
     void insert(Delivery delivery);

     void update(Delivery delivery);

     boolean isHasDataForDay(String day, int id);

     void updateByDayandId(Delivery delivery, String day, int id);

     double getTotalDeliveryByDay(int id, String day);

     List<Double> getTotalDelivery(int start,int maxDay, int month, int year, boolean isForCustomers);

     List<VDelivery> getCustomersDelivery(String date);

     List<VDelivery> getByAreaAndDay(int areaId, String day);
}
