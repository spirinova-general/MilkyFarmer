package com.milky.ui.adapters;

/**
 * Created by Sensation on 12/9/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.milky.R;
import com.milky.utils.AppUtil;
import com.milky.utils.Constants;
import com.milky.service.core.Area;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akshay on 1/2/15.
 */
public class AreaCityAdapter extends ArrayAdapter<Area> {

    Context context;
    int resource, textViewResourceId;
    List<Area> items, tempItems, suggestions;

    public AreaCityAdapter(Context context, int resource, int textViewResourceId, List<Area> items) {
        super(context, resource, textViewResourceId, items);
        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;
        tempItems = new ArrayList<>(items); // this makes the difference.
        suggestions = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.areacityadapter, parent, false);
        }
        Area Area = items.get(position);
        if (Area != null) {
            TextView lblName = (TextView) view.findViewById(R.id.address);
//            TextView lblName2 = (TextView) view.findViewById(R.id.te2);
            lblName.setText(Area.getCityArea());


//            lblName2.setText(AreaMapTableManagement.getCityNameById(AppUtil.getInstance().getDatabaseHandler().getReadableDatabase(), Area.getCityId()));
//            lblName2.setText(Area.getArea()+", "+Area.getCity());
            AppUtil.getInstance().getDatabaseHandler().close();
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Area) resultValue).getCityArea();

            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (com.milky.service.core.Area Area : tempItems) {
                    if (Area.getCityArea().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(Area);
                    }
                }
                if (suggestions.size() > 0)
                    Constants.validArea = true;
                else
                    Constants.validArea = false;
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                suggestions.clear();
                Constants.validArea = false;
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Area> filterList = (ArrayList<Area>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (com.milky.service.core.Area Area : filterList) {
                    add(Area);
                    notifyDataSetChanged();
                }
            }
        }
    };


}