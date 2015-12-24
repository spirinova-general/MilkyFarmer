package com.milky.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.milky.R;
import com.milky.viewmodel.VAreaMapper;

import java.util.ArrayList;

/**
 * Created by Lead1 on 12/3/2015.
 */
public class AutocompleteAreaArrayAdapter extends ArrayAdapter<VAreaMapper> implements Filterable{

    Activity mContext;
    int layoutResourceId;
    ArrayList<VAreaMapper> data;

    public AutocompleteAreaArrayAdapter(Activity mContext, int layoutResourceId, ArrayList<VAreaMapper> data) {

        super(mContext, layoutResourceId, data);

        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }
    public int getCount()
    {
        return data.size();
    }


    public long getItemId(int position)
    {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


            /*
             * It will have a non-null value when ListView is asking you recycle the row layout.
             * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
             */
            if(convertView==null){
                // inflate the layout


                    LayoutInflater inflater = mContext.getLayoutInflater();
                    convertView = inflater.inflate(R.layout.spinner_layout, parent, false);
                }


            // object item based on the position
            VAreaMapper objectItem = data.get(position);

            // get the TextView and then set the text (item name) and tag (item ID) values
            TextView textViewItem = (TextView) convertView.findViewById(R.id.spinnerText);
        textViewItem.setText(objectItem.getCity());

            // in case you want to add some style, you can do something like:



        return convertView;

    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }
}