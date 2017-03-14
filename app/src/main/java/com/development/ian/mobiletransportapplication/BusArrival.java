package com.development.ian.mobiletransportapplication;

/**
 * Created by Ian on 3/5/2017.
 */

public class BusArrival {
    private String arrivalTime;
    private String route;
    private String tripId;
    private int arrivalSeconds;


    public BusArrival(){}
    public BusArrival(String time){ arrivalTime = time;}
    public BusArrival(String time, int arrivalSeconds, String tripId){
        arrivalTime = time;

        this.arrivalSeconds = arrivalSeconds;
        this.tripId = tripId;
        this.route = "loading";
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
    }

    public int getArrivalSeconds(){return arrivalSeconds;}
    public void setArrivalSeconds(int arrivalSeconds){this.arrivalSeconds=arrivalSeconds;}

    public String getTripId(){        return tripId;    }
    public void setTripId(String tripId){this.tripId=tripId;}


}
