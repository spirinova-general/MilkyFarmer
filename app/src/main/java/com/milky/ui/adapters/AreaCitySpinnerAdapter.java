package com.milky.ui.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.milky.R;
import com.milky.service.core.Area;
import java.util.List;

public class AreaCitySpinnerAdapter extends ArrayAdapter<Area> {
    private Activity _context;
    private List<Area> dataList;

    public AreaCitySpinnerAdapter(Activity context, int resource, List<Area> data) {
        super(context, resource, data);
        this._context = context;
        this.dataList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {   // Ordinary view in Spinner, we use android.R.layout.simple_spinner_item

        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = _context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_layout, parent, false);
        }

        Area item = dataList.get(position);

        if (item != null) {
            TextView area = (TextView) row.findViewById(R.id.spinnerText);
            area.setTextColor(_context.getResources().getColor(R.color.white));
            area.setText(item.getCityArea());
        }
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = _context.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_layout, parent, false);
        }
        Area item = dataList.get(position);
        if (item != null) {
            TextView area = (TextView) row.findViewById(R.id.spinnerText);
            area.setTextColor(_context.getResources().getColor(R.color.colorPrimary));
            area.setBackgroundColor(_context.getResources().getColor(R.color.white));
            area.setText(item.getCityArea());
        }
        return row;
    }
}
