package com.development.ian.mobiletransportapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ian on 3/5/2017.
 */

public class ArrivalAdapter extends ArrayAdapter<BusArrival> {

    private ArrayList<BusArrival> arrivals;

    public ArrivalAdapter(Context context, int resource, List<BusArrival> objects) {
        super(context, resource, objects);
        this.arrivals = (ArrayList<BusArrival>) objects;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.arrival_list_item, null);
        }
        BusArrival busArrival = arrivals.get(position);
//        TextView route = (TextView) v.findViewById(R.id.routeNum);
        TextView time = (TextView) v.findViewById(R.id.arrivalTime);
//        route.setText(busArrival.getRouteNum());
        time.setText(busArrival.getArrivalTime());
        return v;
    }
}
