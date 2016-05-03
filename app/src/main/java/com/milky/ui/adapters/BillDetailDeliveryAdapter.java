package com.milky.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.core.Bill;
import com.milky.service.core.CustomerSettingComparator;
import com.milky.service.core.Customers;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.Utils;
import com.milky.utils.Constants;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BillDetailDeliveryAdapter extends BaseAdapter {
    private List<CustomersSetting> customerSettingsData;
    private Bill _bill;
    private Context mContext;

    public BillDetailDeliveryAdapter(final Bill bill, final List<CustomersSetting> dataList, final Context con) {
        this.mContext = con;
        this.customerSettingsData = new ArrayList<CustomersSetting>();
        for(CustomersSetting customersSetting:dataList ) {
            Date customerSettingStartDate = Utils.FromDateString(customersSetting.getStartDate());
            Date customerSettingEndDate = Utils.FromDateString(customersSetting.getEndDate());
            Date billStartDate = Utils.FromDateString(bill.getStartDate());
            Date billEndDate = Utils.FromDateString(bill.getEndDate());

            if( Utils.BeforeDate(customerSettingEndDate, billStartDate) ||
                    Utils.AfterDate(customerSettingStartDate, billEndDate)) {
                continue;
            }
            else
            {
                this.customerSettingsData.add(customersSetting);
            }

        }

        Collections.sort(this.customerSettingsData, new CustomerSettingComparator());
        this._bill = bill;
    }

    @Override
    public int getCount() {
        return customerSettingsData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            final LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.bill_detailed_items, parent, false);

        }
        holder.dayQuantity = (TextView) convertView.findViewById(R.id.dayQuantity);
        holder.dayRate = (TextView) convertView.findViewById(R.id.dayRate);
        holder.deliveryFrom = (TextView) convertView.findViewById(R.id.deliveryFrom);
        CustomersSetting data = customerSettingsData.get(position);
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        Date sDate = Utils.FromDateString(data.getStartDate());
        Date eDAte = Utils.FromDateString(data.getEndDate());
        Date billEndDate = Utils.FromDateString(_bill.getEndDate());
        Date billStartDate = Utils.FromDateString(_bill.getStartDate());

        if( Utils.BeforeOrEqualsDate(billEndDate, eDAte))
        {
            eDAte = billEndDate;
        }
        if(Utils.BeforeOrEqualsDate(sDate, billStartDate))
        {
            sDate = billStartDate;
        }

        startCal.setTime(sDate);
        endCal.setTime(eDAte);

        String startDate = String.format("%02d", startCal.get(Calendar.DAY_OF_MONTH)) + "-" + Constants.MONTHS[startCal.get(Calendar.MONTH)];
        String endDate = String.format("%02d", endCal.get(Calendar.DAY_OF_MONTH)) + "-" + Constants.MONTHS[endCal.get(Calendar.MONTH)];
        if( data.getIsCustomDelivery())
        {
            holder.deliveryFrom.setText("        " + startDate + "    ");
        }
        else
        {
            holder.deliveryFrom.setText(startDate + " to " + endDate);
        }
        holder.dayRate.setText(String.valueOf(data.getDefaultRate()));
        holder.dayQuantity.setText(String.valueOf(data.getGetDefaultQuantity()));
        return convertView;
    }

    class ViewHolder {
        TextView dayQuantity, dayRate, deliveryFrom;
    }
}
