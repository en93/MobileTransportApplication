package com.development.ian.mobiletransportapplication;

import android.widget.TextView;

/**
 * Created by Ian on 3/5/2017.
 */

public class BusArrival {
    private String arrivalTime;
    private String route;
    private String tripId;
    private int arrivalSeconds;

//    private ArrivalAdapter arrivalAdapter;
    private TextView routeTextV;

    public final static String PLACE_HOLDER = "...";

    private APIResponseHandler responseHandler;
    private AtApiManager APIAccess = AtApiManager.getInstance();


    public BusArrival(){}
    public BusArrival(String time){ arrivalTime = time;}
    public BusArrival(String time, int arrivalSeconds, String tripId){
        arrivalTime = time;

        this.arrivalSeconds = arrivalSeconds;
        this.tripId = tripId;
        this.route = PLACE_HOLDER;

//        responseHandler = new APIResponseHandler(this, );
//        APIAccess.getRoute(this.tripId, responseHandler);
    }

    public String getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getRoute() {
        return route;
    }
    public void setRoute(String route) {
        this.route = route;
        if(!(routeTextV == null)){
//            arrivalAdapter.getView(arrivalAdapter.position, arrivalAdapter.convertView, arrivalAdapter.parent);
            routeTextV.setText(this.route);
        }
    }

    public int getArrivalSeconds(){return arrivalSeconds;}
    public void setArrivalSeconds(int arrivalSeconds){this.arrivalSeconds=arrivalSeconds;}

    public String getTripId(){        return tripId;    }
    public void setTripId(String tripId){this.tripId=tripId;}

//    public void setArrivalAdapter(ArrivalAdapter adapter){
//        arrivalAdapter = adapter;
//    }

    public void setRouteTextView(TextView textView){
        routeTextV = textView;
    }

}
