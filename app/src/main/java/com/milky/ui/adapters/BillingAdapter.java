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
import com.milky.service.databaseutils.CustomersTableMagagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.ui.main.BillingEdit;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.service.core.Bill;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Neha on 12/17/2015.
 */
public class BillingAdapter extends BaseAdapter {

    private List<Bill> totalBill;
    private Context mContext;
    private boolean _mIsCustomer = false;
    private DatabaseHelper _dbhelper;

    public BillingAdapter(final List<Bill> dataList, final Context con) {
        this.mContext = con;
        this.totalBill = dataList;
    }

    @Override
    public int getCount() {
//        if (mCustomersData == null)
            return totalBill.size();
//        return mCustomersData.size();
//        return 0;
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
        final Bill data = totalBill.get(position);
         holder.startDate = (TextView) convertView.findViewById(R.id.startDate);
        holder.endDate = (TextView) convertView.findViewById(R.id.endDate);
        holder.amount = (TextView) convertView.findViewById(R.id.amount);
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.custName = (TextView) convertView.findViewById(R.id.quantityText);
        holder.history = (TextView) convertView.findViewById(R.id.history);

        String nfNAme = CustomersTableMagagement.getFirstName(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), data.getCustomerId());
        String lfNAme = CustomersTableMagagement.getLastName(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), data.getCustomerId());
        String a = Character.toString(nfNAme.charAt(0));
        String b = Character.toString(lfNAme.charAt(0));
        holder.custName.setText(nfNAme + " " + lfNAme);

        holder.name.setText(a + b);


        if ("1".equals(data.getIsCleared()) && "0".equals(data.isOutstanding())) {
            holder.history.setVisibility(View.VISIBLE);
            holder.history.setText("Outstanding");
        }
        if ("0".equals(data.getIsCleared()) && "0".equals(data.isOutstanding())) {
            holder.history.setVisibility(View.VISIBLE);
            holder.history.setText("History");
        }

        final Calendar showDatePattern = Calendar.getInstance();
        final Calendar shownEndDate = Calendar.getInstance();
        try {
            showDatePattern.setTime(Constants.work_format.parse(data.getStartDate()));
            shownEndDate.setTime(Constants.work_format.parse(data.getEndDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.startDate.setText(showDatePattern.get(Calendar.DAY_OF_MONTH) + "-" +
                Constants.MONTHS[showDatePattern.get(Calendar.MONTH)] + "-" + showDatePattern.get(Calendar.YEAR));
        holder.endDate.setText(shownEndDate.get(Calendar.DAY_OF_MONTH) + "-" +
                Constants.MONTHS[shownEndDate.get(Calendar.MONTH)] + "-" + shownEndDate.get(Calendar.YEAR));
        holder.amount.setText(String.valueOf(data.getTotalAmount()));
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BillingEdit.class);
                intent.putExtra("start_date", showDatePattern.get(Calendar.DAY_OF_MONTH) + "-" +
                        Constants.MONTHS[showDatePattern.get(Calendar.MONTH)] + "-" + showDatePattern.get(Calendar.YEAR))
                        .putExtra("end_date", shownEndDate.get(Calendar.DAY_OF_MONTH) + "-" +
                                Constants.MONTHS[shownEndDate.get(Calendar.MONTH)] + "-" + shownEndDate.get(Calendar.YEAR))
                        .putExtra("quantity", data.getQuantity())
                        .putExtra("amount", 0)
                        .putExtra("balance", data.getBalance())
                        .putExtra("titleString", holder.custName.getText())
                        .putExtra("totalPrice", data.getRate())
                        .putExtra("custId", data.getCustomerId())
                        .putExtra("clear", data.getIsCleared())
                        .putExtra("total", data.getTotalAmount())
                        .putExtra("balance_type", data.getBalanceType())
                        .putExtra("payment_made", data.getPaymentMade())
                        .putExtra("start_date_work_format", data.getStartDate())
                        .putExtra("end_date_work_format", data.getEndDate())
                        .putExtra("roll_date", data.getRollDate())
                        .putExtra("is_outstanding", data.getIsOutstanding());
                mContext.startActivity(intent);

            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView startDate, endDate, amount, name, custName, history;
    }


}
