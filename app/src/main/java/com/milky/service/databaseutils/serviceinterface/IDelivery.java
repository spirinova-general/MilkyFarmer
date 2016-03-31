package com.milky.service.databaseutils.serviceinterface;

import com.milky.service.core.Delivery;
import com.milky.viewmodel.VDelivery;

import java.util.List;

public interface IDelivery {
    public void insert(Delivery delivery);

    public void update(Delivery delivery);

    public boolean isHasDataForDay(String day, int id);

    public void updateByDayandId(Delivery delivery, String day, int id);

    public double getTotalDeliveryByDay(int id, String day);

    public List<Double> getTotalDelivery(int start,int maxDay, int month, int year, boolean isForCustomers);

    public List<VDelivery> getCustomersDelivery(String date);

    public List<VDelivery> getByAreaAndDay(int areaId, String day);
}
