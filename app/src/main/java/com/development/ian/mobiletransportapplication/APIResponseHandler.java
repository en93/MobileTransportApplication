package com.development.ian.mobiletransportapplication;

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Ian on 3/1/2017.
 */

public class APIResponseHandler implements responseHandler {

    private View view;
    private ListView list;
    private Context context;


    private StationProvider stationProvider = new StationProvider();

    public APIResponseHandler(View view){
        this.view = view;
    }

    public APIResponseHandler(View view, ListView list, Context context){
        this.view = view;
        this.list = list;
        this.context = context;
    }

    @Override
    public void onSuccess(AtApiManager.TAG tag, JSONArray dataArray) {
        if(tag == AtApiManager.TAG.getStop){
            try {
                JSONObject dataObject = dataArray.getJSONObject(0);
                ContentValues values = new ContentValues();
                values.put(DBHelper.STOPS_ID, dataObject.getString("stop_id"));
                values.put(DBHelper.STOPS_NAME, dataObject.getString("stop_name"));
                values.put(DBHelper.STOPS_LAT, Double.parseDouble(dataObject.getString("stop_lat")));
                values.put(DBHelper.STOPS_LON, Double.parseDouble(dataObject.getString("stop_lon")));
                stationProvider.insert(StationProvider.CONTENT_URI, values);
                AddNewStopActivity.restoreUserControl();
                Snackbar.make(view, "Stop " + values.get(DBHelper.STOPS_ID) + " has been added", Snackbar.LENGTH_SHORT).show();
            }catch (JSONException jEx){

            }finally {
                return;
            }
        }
        else if(tag == AtApiManager.TAG.getArrivals){
//            Snackbar.make(view, "Message received, handling not yet implemented", Snackbar.LENGTH_SHORT).show();
            ArrayList<BusArrival> arrivalList = new ArrayList<BusArrival>();
            try{
                JSONObject dataObject;
                for (int i = 0; i<dataArray.length(); i++) {
                    dataObject = dataArray.getJSONObject(i);
                    arrivalList.add(new BusArrival(dataObject.getString("arrival_time"), "???", dataObject.getInt("arrival_time_seconds") ));
                }

                //todo sort array by time

                Collections.sort(arrivalList, new BusTimeComparator());

                ArrivalAdapter adapter = new ArrivalAdapter(context, R.layout.arrival_list_item, arrivalList);
                list.setAdapter(adapter);
            }catch (JSONException e){

            }
        }
    }


    @Override
    public void onFailure(AtApiManager.TAG tag, String detail) {
        if(tag == AtApiManager.TAG.getStop) {
            AddNewStopActivity.restoreUserControl();
            Snackbar.make(view, "Stop " + detail + " is not a valid stop", Snackbar.LENGTH_SHORT).show();
        }
    }

    class BusTimeComparator implements Comparator<BusArrival>{

        @Override
        public int compare(BusArrival t1, BusArrival t2) {
            return Integer.compare(t1.getArrivalSeconds(), t2.getArrivalSeconds());
        }
    }

}
