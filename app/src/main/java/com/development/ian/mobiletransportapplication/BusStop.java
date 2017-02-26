package com.development.ian.mobiletransportapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ian on 12/5/2016.
 */

public class BusStop implements Parcelable{
    private int id;
    private String name;
    private double latitude ;
    private double longitude;

    public BusStop(int Id, String Name, double Latitude, double Longitude){
        id = Id;
        name = Name;
        latitude = Latitude;
        longitude = Longitude;
    }

    protected BusStop(Parcel in) {
        id = in.readInt();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<BusStop> CREATOR = new Creator<BusStop>() {
        @Override
        public BusStop createFromParcel(Parcel in) {
            return new BusStop(in);
        }

        @Override
        public BusStop[] newArray(int size) {
            return new BusStop[size];
        }
    };

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
    }
}
