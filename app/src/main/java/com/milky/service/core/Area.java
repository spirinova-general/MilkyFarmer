package com.milky.service.core;

import com.milky.service.databaseutils.TableColumns;

/**
 * Created by Neha on 12/2/2015.
 */
public class Area {
    private String area;
    private int areaId;
    private String city;
    private String cityArea;
    private String locality;
    private int dirty;

    ///Umesh Since we will always have public getters and setters for these fields of core classes we can just keep them public
    public int getDirty() {
        return dirty;
    }

    public void setDirty(int dirty) {
        this.dirty = dirty;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCityArea() {
        return cityArea;
    }

    public void setCityArea(String cityArea) {
        this.cityArea = cityArea;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getFullAddress(Area area)
    {
        String address ="";
        if (area.getLocality().equals("")) {
            address = area.getArea() + ", "
                    + area.getCity();

        } else
            address = area.getLocality() + ", " + area.getArea() + ", "
                    +area.getCity();
        return address;
    }
}
