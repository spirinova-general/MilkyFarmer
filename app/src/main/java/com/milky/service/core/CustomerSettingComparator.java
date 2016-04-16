package com.milky.service.core;

import com.milky.service.databaseutils.Utils;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by admin on 4/17/2016.
 */
public class CustomerSettingComparator implements Comparator<CustomersSetting> {
    @Override
    public int compare(CustomersSetting c1, CustomersSetting c2) {
        Date c1StartDate = Utils.FromDateString(c1.getStartDate());
        Date c2StartDate = Utils.FromDateString(c2.getStartDate());

        if( c1.getIsCustomDelivery() && !c2.getIsCustomDelivery()){
            return 1;
        }
        if( !c1.getIsCustomDelivery() && c2.getIsCustomDelivery())
            return -1;
        else {
           if( Utils.BeforeDate(c1StartDate, c2StartDate) == true )
                   return -1;
            else if( !Utils.BeforeOrEqualsDate(c1StartDate, c2StartDate))
                return 1;
            else
                return 0;
        }
    }
}
