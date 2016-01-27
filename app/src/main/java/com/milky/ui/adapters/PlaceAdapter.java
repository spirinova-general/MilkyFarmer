package com.milky.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import com.milky.R;

import org.json.JSONObject;

/**
 * Created by Lead1 on 1/27/2016.
 */
public class PlaceAdapter extends ArrayAdapter<JSONObject> implements Filterable{
    private Context context;
    private int textViewResourceId;
    private JSONObject[] data;
    public PlaceAdapter(Context context, int textViewResourceId, JSONObject[] objects) {
        super(context, textViewResourceId, objects);
        this.context = context;
        this.textViewResourceId = textViewResourceId;
        data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Viewholder holder = null;
        try {
            if(convertView==null){
                holder = new Viewholder();
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(textViewResourceId, parent, false);
                holder.txtPlace = (TextView) convertView.findViewById(R.id.txtPlace);
            }

            holder.txtPlace.setText(data[position].getString("description"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }

    static class Viewholder{
        TextView txtPlace;
    }
}