package com.development.ian.mobiletransportapplication;

/**
 * Created by Ian on 3/5/2017.
 */

public class BusArrival {
    private String arrivalTime;
    private String route;

    public BusArrival(){}
    public BusArrival(String time){ arrivalTime = time;}
    public BusArrival(String time, String route){ arrivalTime = time; this.route = route;}

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




}
