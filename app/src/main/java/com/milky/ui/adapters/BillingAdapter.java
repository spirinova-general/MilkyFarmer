package com.milky.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.Account;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.ui.main.BillingEdit;
import com.milky.ui.main.CustomersActivity;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VCustomersList;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Neha on 12/17/2015.
 */
public class BillingAdapter extends BaseAdapter {

    private List<VCustomersList> mCustomersData;
    private Context mContext;
    private boolean _mIsCustomer = false;

    public BillingAdapter(final List<VCustomersList> dataList, final Context con, final boolean isCustomer) {
        this.mContext = con;
        this.mCustomersData = dataList;
        this._mIsCustomer = isCustomer;
    }

    @Override
    public int getCount() {
        return mCustomersData.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            final LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.bill_history_items, parent, false);
        }
        Calendar c = Calendar.getInstance();
        final String time = String.valueOf(String.valueOf(c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.DAY_OF_MONTH)) + "-" +
                String.valueOf(c.get(Calendar.YEAR));
        holder.startDate = (TextView) convertView.findViewById(R.id.startDate);
        holder.endDate = (TextView) convertView.findViewById(R.id.endDate);
        holder.amount = (TextView) convertView.findViewById(R.id.amount);

        holder.startDate.setText(mCustomersData.get(position).getStart_date());
        holder.endDate.setText(time);
        holder.amount.setText(getBill(time, mCustomersData.get(position).getCustomerId(),
                mCustomersData.get(position).getStart_date(), mCustomersData.get(position).getEnd_date(), mCustomersData.get(position).getRate()
                , mCustomersData.get(position).getQuantity(), mCustomersData.get(position).getBalance_amount(), mCustomersData.get(position).getAdjustment()));

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BillingEdit.class);
                intent.putExtra("start_date", mCustomersData.get(position).getStart_date())
                        .putExtra("end_date", time)
                        .putExtra("quantity", mCustomersData.get(position).getQuantity())
                        .putExtra("amount", mCustomersData.get(position).getRate())
                        .putExtra("balance", mCustomersData.get(position).getBalance_amount())
                        .putExtra("titleString", CustomersActivity.titleString)
                        .putExtra("total", holder.amount.getText().toString());
                mContext.startActivity(intent);

            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView startDate, endDate, amount;
    }

    private DatabaseHelper databaseHelper = AppUtil.getInstance().getDatabaseHandler();

    private String getBill(String currentDate, String custId, String deliveryDate, String endDate, String rate, String qty, String balance, String adjustment) {
        Date todaydate = null, deliverydate = null, enddate = null, tempdate = null;
        String billAmount = "0";
        float previousBill = 0;
        SimpleDateFormat sdf = Constants.format;
        Float tax = (Float.parseFloat(rate) * Float.parseFloat(Account.getDefautTax(databaseHelper.getReadableDatabase()))) / 100;
        try {
            todaydate = sdf.parse(currentDate);
            tempdate = sdf.parse(currentDate);
            deliverydate = sdf.parse(deliveryDate);
            if (!endDate.equals("0"))
                enddate = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (deliverydate.compareTo(todaydate) == 0) {
            previousBill = ((Float.parseFloat(rate) * Float.parseFloat(qty)) + tax) - (Float.parseFloat(balance) + Float.parseFloat(adjustment));
            return String.valueOf(round(previousBill, 2));
        } else if (deliverydate.before(todaydate)) {
            if (!endDate.equals("0")) {
                if (enddate.after(todaydate) && enddate.compareTo(todaydate) != 0) {
                    for (int i = 0; i < (todaydate.compareTo(deliverydate)); i++) {
                        previousBill = (Float.parseFloat(rate) * Float.parseFloat(qty)) + tax - (Float.parseFloat(balance) + Float.parseFloat(adjustment)) + previousBill;
                        billAmount = String.valueOf(round(previousBill, 2));
                    }
                }
            } else {
                for (int i = 0; i < (todaydate.compareTo(deliverydate)); i++) {
                    previousBill = (Float.parseFloat(rate) * Float.parseFloat(qty)) + tax - (Float.parseFloat(balance) + Float.parseFloat(adjustment)) + previousBill;
                    billAmount = String.valueOf(round(previousBill, 2));
                }
            }
        } else {
            if ((tempdate.compareTo(todaydate) < 0)) {

                for (int i = 0; i < (todaydate.compareTo(deliverydate)); i++) {
                    previousBill = (Float.parseFloat(rate) * Float.parseFloat(qty)) + tax - (Float.parseFloat(balance) + Float.parseFloat(adjustment)) + previousBill;

                    tempdate = addDays(tempdate, 1);
                    billAmount = String.valueOf(round(previousBill, 2));
                }
            }
            return String.valueOf(round(previousBill, 2));
        }


        return billAmount;
    }

    public Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
