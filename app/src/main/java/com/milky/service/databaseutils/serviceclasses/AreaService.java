package com.milky.service.databaseutils.serviceclasses;

import com.milky.service.core.Area;
import com.milky.service.databaseutils.serviceinterface.IArea;

import java.util.List;

public class AreaService implements IArea  {
    @Override
    public void insert(Area area) {

    }

    @Override
    public List<Area> getAllArea() {
        return null;
    }

    @Override
    public boolean deleteAreaById(int areaId) {
        return false;
    }
}
