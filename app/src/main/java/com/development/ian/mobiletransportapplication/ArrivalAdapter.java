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
    private Context context;

    private AtApiManager APIAccess = AtApiManager.getInstance();
    public APIResponseHandler responseHandler;

    int position;
    View convertView;
    ViewGroup parent;


    public ArrivalAdapter(Context context, int resource, List<BusArrival> objects) {
        super(context, resource, objects);
        this.arrivals = (ArrayList<BusArrival>) objects;
        this.context = context;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        this.position = position;
        this.convertView = convertView;
        this.parent = parent;
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.arrival_list_item, null);
        }
        BusArrival busArrival = arrivals.get(position);
//        busArrival.setArrivalAdapter(this);
        TextView route = (TextView) v.findViewById(R.id.routeNum);
        TextView time = (TextView) v.findViewById(R.id.arrivalTime);
//        route.setText(busArrival.getRouteNum());
        route.setText(busArrival.getRoute());
        time.setText(busArrival.getArrivalTime());
        busArrival.setRouteTextView(route);
//        if(busArrival.getRoute() == BusArrival.PLACE_HOLDER) {
//            ResponseHandler = new APIResponseHandler(route, busArrival);
//            APIAccess.getRouteId(busArrival.getTripId(), ResponseHandler, context.getApplicationContext());
//        }
        return v;
    }
}
