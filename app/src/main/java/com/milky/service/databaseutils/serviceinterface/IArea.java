package com.milky.service.databaseutils.serviceinterface;


import com.milky.service.core.Area;

import java.util.List;

public interface IArea {
    long insert(Area area);

    List<Area> getAllArea();

    boolean deleteAreaById(int areaId);

    boolean hasAddress(String locality, String area, String city);

    Area getAreaById(int areaId);

    List<Area> getStoredAddresses();
}
