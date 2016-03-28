package com.milky.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.milky.ui.main.BillingEdit;

import java.util.List;

import com.milky.R;
import com.milky.utils.DialogScreen;
import com.milky.viewmodel.VCustomers;

/**
 * Created by Neha on 11/17/2015.
 */
public class CustomersListAdapter extends BaseAdapter {
    private List<VCustomers> mCustomersData;
    private Context mContext;
    private boolean _mIsCustomer = false;

    public CustomersListAdapter(final List<VCustomers> dataList, final Context con, final boolean isCustomer) {
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
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            final LayoutInflater mInflater = (LayoutInflater)
                    mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.customers_list_item, parent, false);
        }
        holder._name = (TextView) convertView.findViewById(R.id.name);
        holder._quantity = (EditText) convertView.findViewById(R.id.milk_quantity);
        holder._literText = (TextView) convertView.findViewById(R.id.litreText);
        holder._clear = (Button) convertView.findViewById(R.id.clear);
        holder._quantityText = (TextView) convertView.findViewById(R.id.quantityText);
        holder._date = (TextView) convertView.findViewById(R.id.date);
        holder._nameView = (TextView) convertView.findViewById(R.id.nameView);
        holder._date.setText(mCustomersData.get(position).getStartDate() + "-" + mCustomersData.get(position).getEndDate());
        holder._quantity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (_mIsCustomer)
                    return false;
                else
                    return true;
            }
        });
        String a = Character.toString(mCustomersData.get(position).getFirstName().charAt(0));
        String b = Character.toString(mCustomersData.get(position).getLastName().charAt(0));

        //get first name and last name letters
        holder._nameView.setText(a + b);

        holder._name.setText(mCustomersData.get(position).getFirstName() + " " + mCustomersData.get(position).getLastName());
        if (_mIsCustomer) {

            holder._clear.setVisibility(View.GONE);
        } else {
            holder._clear.setVisibility(View.VISIBLE);

        }
        holder._quantity.setText(String.valueOf(mCustomersData.get(position).getBalance_amount()));
//            holder._dated.setText(mCustomersData.get(position).getStart_date() + " - " + mCustomersData.get(position).getEnd_date());
//            holder._dated.setVisibility(View.VISIBLE);
        holder._quantityText.setText("Bill Amount ");
        holder._literText.setText(" Rs");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, BillingEdit.class).putExtra("first_name", mCustomersData.get(position).getFirstName())
                        .putExtra("last_name", mCustomersData.get(position).getLastName())
                        .putExtra("bill_amount", mCustomersData.get(position).getBalance_amount())
                        .putExtra("start_date", mCustomersData.get(position).getStartDate())
                        .putExtra("end_date", mCustomersData.get(position).getEndDate())
                        .putExtra("quantity", mCustomersData.get(position).getGetDefaultQuantity())
                        .putExtra("rate", mCustomersData.get(position).getDefaultRate());
//                        .putExtra("tax", mCustomersData.get(position).getT());
                mContext.startActivity(i);
            }
        });
        holder._clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!_mIsCustomer) {
                    new DialogScreen(mContext, String .valueOf(mCustomersData.get(position).getBalance_amount()));
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        private TextView _name, _literText, _quantityText, _date, _nameView;
        private EditText _quantity;
        private Button _clear;
    }


}

