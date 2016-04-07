package com.milky.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import com.milky.R;
import com.milky.service.core.Delivery;
import com.milky.service.databaseutils.serviceclasses.DeliveryService;
import com.milky.ui.customers.DeliveryActivity;
import com.milky.utils.Constants;
import com.milky.service.core.Customers;
import com.milky.viewmodel.VDelivery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neha on 11/17/2015.
 */
public class GlobalDeliveryAdapter extends ArrayAdapter<VDelivery> {
    private Context mContext;
    private List<VDelivery> filteredList, tempItems, suggestions;
    public GlobalDeliveryAdapter(final Context con, int resource, int textViewResourceId, final String quantityEditDate, final List<VDelivery> _mCustomersList) {
        super(con, resource, textViewResourceId, _mCustomersList);
        this.mContext = con;
        this.filteredList = _mCustomersList;
        tempItems = new ArrayList<>(filteredList); // this makes the difference.
        suggestions = new ArrayList<>();

    }

    @Override
    public int getCount() {
        return filteredList.size();
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
            holder._quantity_input_layout = (TextInputLayout) convertView.findViewById(R.id.quantity_layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

            holder._quantity.setText(String.valueOf(filteredList.get(position).getQuantity()));
        holder._quantity.setTag(position);
        holder._quantity.setId(position);
        String a = Character.toString(filteredList.get(position).getFirstname().charAt(0));
        String b = Character.toString(filteredList.get(position).getLastname().charAt(0));

        //get first name and last name letters
        holder._nameView.setText(a + b);
/*
        * Set text field listeners*/
        holder._firstName.setText(filteredList.get(position).getFirstname());
        holder._latsName.setText(" " + filteredList.get(position).getLastname());
        final ViewHolder finalHolder = holder;
        holder._quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int position2 = finalHolder._quantity.getId();
                if (s.toString().length() > 0) {
                    Delivery holder = new Delivery();
                    holder.setQuantity(Double.parseDouble(s.toString()));
                    holder.setCustomerId(DeliveryActivity._mCustomersList.get(position2).getCustomerId());
                    holder.setDeliveryDate(Constants.DELIVERY_DATE);
                    holder.setDateModified(Constants.getCurrentDate());
                    DeliveryActivity._mDeliveryList.add(holder);
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

    @Override
    public Filter getFilter() {
        return nameFilter;
    }


   /* Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((VCustomersList) resultValue).getFirstName();
            notifyDataSetChanged();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (VCustomersList Area : tempItems) {
                    if ((Area.getFirstName().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                            Area.getLastName().toLowerCase().contains(constraint.toString().toLowerCase()))) {
                        suggestions.add(Area);
                    }
                }


                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                suggestions.clear();
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<VCustomersList> filterList = (ArrayList<VCustomersList>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (VCustomersList Area : filterList) {
                    add(Area);
                    notifyDataSetChanged();
                }

            } else {
                clear();
                notifyDataSetChanged();
            }
            if (!Constants.selectedAreaId.equals("")) {
                clear();


                filterList = CustomersTableMagagement.getAllCustomersByArea(_dbHelper.getReadableDatabase(), Constants.selectedAreaId);
                for (VCustomersList Area : filterList) {
                    add(Area);
                    notifyDataSetChanged();
                }
            }
            notifyDataSetChanged();


        }
    };*/


    Filter nameFilter;

    {
        nameFilter = new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                String str = ((Customers) resultValue).getFirstName();
                notifyDataSetChanged();
                return str;
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint != null) {
                    suggestions.clear();
                    for (VDelivery Area : tempItems) {
                        if ((Area.getFirstname().toLowerCase().contains(constraint.toString().toLowerCase()) ||
                               Area.getLastname().toLowerCase().contains(constraint.toString().toLowerCase()))) {
                            suggestions.add(Area);
                        }
                    }

                    FilterResults filterResults = new FilterResults();
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                    return filterResults;
                } else {
                    suggestions.clear();
                    return new FilterResults();
                }
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                List<VDelivery> filterList = (ArrayList<VDelivery>) results.values;
                if (results != null && results.count > 0) {
                    clear();
                    for (VDelivery Area : filterList) {
                        add(Area);
                        notifyDataSetChanged();
                    }
                } else {
                    clear();
                    notifyDataSetChanged();
                }
                if (Constants.selectedAreaId!=-1) {
                    clear();

                    filterList = new DeliveryService().getDeliveryDetails(Constants.selectedAreaId, Constants.DELIVERY_DATE);
                    DeliveryActivity.selectedCustomersId = new ArrayList<>();
                    for (VDelivery Area : filterList) {
                        add(Area);
                        notifyDataSetChanged();
                        DeliveryActivity.selectedCustomersId.add(Area);
                    }
                }
                DeliveryActivity._mAdaapter.notifyDataSetChanged();
            }
        };
    }

}

