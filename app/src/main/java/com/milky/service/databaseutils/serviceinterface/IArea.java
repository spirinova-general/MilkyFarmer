package com.milky.service.databaseutils.serviceinterface;


import com.milky.service.core.Area;

import java.util.List;

public interface IArea {
    public void insert(Area area);

    public List<Area> getAllArea();

    public boolean deleteAreaById(int areaId);
}
