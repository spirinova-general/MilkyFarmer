package com.milky.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.core.CustomersSetting;
import com.milky.service.databaseutils.Utils;
import com.milky.utils.Constants;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BillDetailDeliveryAdapter extends BaseAdapter {
    private List<CustomersSetting> customerSettingsData;
    private Context mContext;

    public BillDetailDeliveryAdapter(final List<CustomersSetting> dataList, final Context con) {
        this.mContext = con;
        this.customerSettingsData = dataList;
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

        try {
            Date sDate = Utils.FromDateString(data.getStartDate());
            Date eDAte = Utils.FromDateString(data.getEndDate());
            startCal.setTime(sDate);
            endCal.setTime(eDAte);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String startDate = String.format("%02d", startCal.get(Calendar.DAY_OF_MONTH)) + "-" + Constants.MONTHS[startCal.get(Calendar.MONTH)];
        String endDate = String.format("%02d", endCal.get(Calendar.DAY_OF_MONTH)) + "-" + Constants.MONTHS[endCal.get(Calendar.MONTH)];
        holder.deliveryFrom.setText(startDate + " to " + endDate);
        holder.dayRate.setText("Rupees " + String.valueOf(data.getDefaultRate()));
        holder.dayQuantity.setText(String.valueOf(data.getGetDefaultQuantity()) + " Litres");
        return convertView;
    }

    class ViewHolder {
        TextView dayQuantity, dayRate, deliveryFrom;
    }
}
