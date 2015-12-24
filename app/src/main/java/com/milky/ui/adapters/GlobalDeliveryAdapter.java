package com.milky.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.databaseutils.DeliveryTableManagement;
import com.milky.service.databaseutils.DatabaseHelper;
import com.milky.service.databaseutils.TableNames;
import com.milky.ui.customers.CustomersList;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.viewmodel.VCustomersList;

/**
 * Created by Neha on 11/17/2015.
 */
public class GlobalDeliveryAdapter extends BaseAdapter {
    private Context mContext;
    private String date;

    public GlobalDeliveryAdapter(final Context con, final String quantityEditDate) {
        this.mContext = con;
        this.date = quantityEditDate;

    }

    @Override
    public int getCount() {
        return CustomersList._mCustomersList.size();
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
            convertView = mInflater.inflate(R.layout.global_delivery_items, parent, false);
            holder._nameView = (TextView) convertView.findViewById(R.id.nameView);
            holder._firstName = (TextView) convertView.findViewById(R.id.first_name);
            holder._quantity = (EditText) convertView.findViewById(R.id.milk_quantity);
            holder._latsName = (TextView) convertView.findViewById(R.id.last_name);
            holder._quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            holder._quantity_input_layout = (TextInputLayout) convertView.findViewById(R.id.quantity_layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DatabaseHelper db = AppUtil.getInstance().getDatabaseHandler();
        if (db.isTableNotEmpty(TableNames.TABLE_DELIVERY)) {
            if (DeliveryTableManagement.isHasData(db.getReadableDatabase(), CustomersList._mCustomersList
                    .get(position).getCustomerId(), Constants.DELIVERY_DATE)) {
                holder._quantity.setText(DeliveryTableManagement.getQuantityBySelectedDay(db.getReadableDatabase(), CustomersList._mCustomersList
                        .get(position).getCustomerId(), Constants.DELIVERY_DATE));
            } else {
                holder._quantity.setText(CustomersList._mCustomersList.get(position).getQuantity());
            }
        } else {
            holder._quantity.setText(CustomersList._mCustomersList.get(position).getQuantity());
        }
        holder._quantity.setTag(position);
        holder._quantity.setId(position);
        String a = Character.toString(CustomersList._mCustomersList.get(position).getFirstName().charAt(0));
        String b = Character.toString(CustomersList._mCustomersList.get(position).getLastName().charAt(0));

        //get first name and last name letters
        holder._nameView.setText(a + b);
/*
        * Set text field listeners*/
        holder._firstName.setText(CustomersList._mCustomersList.get(position).getFirstName());
        holder._latsName.setText(CustomersList._mCustomersList.get(position).getLastName());
        final ViewHolder finalHolder = holder;
        holder._quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int position2 = finalHolder._quantity.getId();
                if (s.toString().length() > 0) {
                    VCustomersList holder = new VCustomersList();
                    holder.setQuantity(s.toString());
                    holder.setCustomerId(CustomersList._mCustomersList.get(position2).getCustomerId());
                    holder.setDeliverydate(Constants.DELIVERY_DATE);
                    CustomersList._mDeliveryList.add(holder);
                    finalHolder._quantity_input_layout.setError(null);
                } else {
                    finalHolder._quantity_input_layout.setError(mContext.getString(R.string.field_cant_empty));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        return convertView;
    }

    private class ViewHolder {
        private TextView _firstName, _latsName, _nameView;
        private EditText _quantity;
        private TextInputLayout _quantity_input_layout;
    }


}

