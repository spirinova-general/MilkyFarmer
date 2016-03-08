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
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.ui.main.BillingEdit;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VBill;
import com.tyczj.extendedcalendarview.ExtcalVCustomersList;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Neha on 12/17/2015.
 */
public class BillingAdapter extends BaseAdapter {

    private List<ExtcalVCustomersList> mCustomersData;
    private List<VBill> totalBill;
    private Context mContext;
    private boolean _mIsCustomer = false;
    private ArrayList<String> names = new ArrayList<>();

    public BillingAdapter(final List<VBill> dataList, final Context con, ArrayList<String> name) {
        this.mContext = con;
        this.totalBill = dataList;
        this.names = name;
    }

    @Override
    public int getCount() {
        if (mCustomersData == null)
            return totalBill.size();
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
        holder.startDate = (TextView) convertView.findViewById(R.id.startDate);
        holder.endDate = (TextView) convertView.findViewById(R.id.endDate);
        holder.amount = (TextView) convertView.findViewById(R.id.amount);
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.custName = (TextView) convertView.findViewById(R.id.quantityText);
        holder.history = (TextView) convertView.findViewById(R.id.history);

        String nfNAme = CustomersTableMagagement.getFirstName(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), names.get(position));
        String lfNAme = CustomersTableMagagement.getLastName(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), names.get(position));
        String a = Character.toString(nfNAme.charAt(0));
        String b = Character.toString(lfNAme.charAt(0));
        holder.custName.setText(nfNAme + " " + lfNAme);

        holder.name.setText(a + b);
        if (("0").equals(totalBill.get(position).isOutstanding())) {
            holder.history.setVisibility(View.VISIBLE);
            holder.history.setText("Outstanding");
        }
        if ("0".equals(totalBill.get(position).getIsCleared())) {
            holder.history.setVisibility(View.VISIBLE);
            holder.history.setText("History");
        }

        final Calendar showDatePattern = Calendar.getInstance();
        final Calendar shownEndDate = Calendar.getInstance();
        try {
            showDatePattern.setTime(Constants.work_format.parse(totalBill.get(position).getStartDate()));
            shownEndDate.setTime(Constants.work_format.parse(totalBill.get(position).getEndDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.startDate.setText(showDatePattern.get(Calendar.DAY_OF_MONTH) + "-" +
                Constants.MONTHS[showDatePattern.get(Calendar.MONTH)] + "-" + showDatePattern.get(Calendar.YEAR));
        holder.endDate.setText(shownEndDate.get(Calendar.DAY_OF_MONTH) + "-" +
                Constants.MONTHS[shownEndDate.get(Calendar.MONTH)] + "-" + shownEndDate.get(Calendar.YEAR));
        holder.amount.setText(totalBill.get(position).getBillMade());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BillingEdit.class);
                intent.putExtra("start_date", showDatePattern.get(Calendar.DAY_OF_MONTH) + "-" +
                        Constants.MONTHS[showDatePattern.get(Calendar.MONTH)] + "-" + showDatePattern.get(Calendar.YEAR))
                        .putExtra("end_date", shownEndDate.get(Calendar.DAY_OF_MONTH) + "-" +
                                Constants.MONTHS[shownEndDate.get(Calendar.MONTH)] + "-" + shownEndDate.get(Calendar.YEAR))
                        .putExtra("quantity", totalBill.get(position).getQuantity())
                        .putExtra("amount", "0")
                        .putExtra("balance", totalBill.get(position).getBalance())
                        .putExtra("titleString", holder.custName.getText())
                        .putExtra("totalPrice", totalBill.get(position).getRate())
                        .putExtra("custId", totalBill.get(position).getCustomerId())
                        .putExtra("clear", totalBill.get(position).getIsCleared())
                        .putExtra("total", totalBill.get(position).getBillMade())
                        .putExtra("balance_type", totalBill.get(position).getBalanceType())
                        .putExtra("payment_made", totalBill.get(position).getPaymentMode())
                        .putExtra("start_date_work_format", totalBill.get(position).getStartDate())
                        .putExtra("end_date_work_format", totalBill.get(position).getEndDate());
                mContext.startActivity(intent);

            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView startDate, endDate, amount, name, custName, history;
    }


    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
