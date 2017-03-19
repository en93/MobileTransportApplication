package com.development.ian.mobiletransportapplication;

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ListView;

import com.development.ian.mobiletransportapplication.TransportContentProviders.SavedStopProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.StationProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.StopProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Ian on 3/1/2017.
 */

public class APIResponseHandler implements ResponseHandler {

    private View view;
    private ListView list;
    private Context context;

    private BusArrival busArrival;

    private AtApiManager APIAccess = AtApiManager.getInstance();

    private StationProvider stationProvider = new StationProvider();//todo remove

    private SavedStopProvider savedStopProvider;
    private StopProvider stopProvider;

    public APIResponseHandler(View view){
        this.view = view;
    }

    public APIResponseHandler(BusArrival busArrival){
        this.busArrival = busArrival;
    }

    public APIResponseHandler(View view, ListView list, Context context){
        this.view = view;
        this.list = list;
        this.context = context;
    }


    @Override
    public void onSuccess(AtApiManager.TAG tag, JSONArray dataArray) throws JSONException { //todo handle exception rather than throwing
        if(tag == AtApiManager.TAG.getStop){
            try {

                //old
                JSONObject dataObject = dataArray.getJSONObject(0);
                ContentValues stopsValues = new ContentValues();
                stopsValues.put(DBHelper.STOPS_ID, dataObject.getString("stop_id"));
                stopsValues.put(DBHelper.STOPS_NAME, dataObject.getString("stop_name"));
                stopsValues.put(DBHelper.STOPS_LAT, Double.parseDouble(dataObject.getString("stop_lat")));
                stopsValues.put(DBHelper.STOPS_LON, Double.parseDouble(dataObject.getString("stop_lon")));
                stationProvider.insert(StationProvider.CONTENT_URI, stopsValues);
                AddNewStopActivity.restoreUserControl();
                Snackbar.make(view, "Stop " + stopsValues.get(DBHelper.STOPS_ID) + " has been added", Snackbar.LENGTH_SHORT).show();
                //end old

                //add to saved stops
                if(savedStopProvider==null){
                    savedStopProvider = new SavedStopProvider();
                }
                ContentValues savedStopValues = new ContentValues();
                savedStopValues.put(DBHelper.SAVED_STOP_ID, dataObject.getInt(DBHelper.SAVED_STOP_ID));
                savedStopProvider.insert(SavedStopProvider.CONTENT_URI, savedStopValues);

                //add to stops
                if(stopProvider==null){
                    stopProvider = new StopProvider();
                }
                ContentValues stopValues = new ContentValues();
                stopsValues.put(DBHelper.STOP_NAME, dataObject.getString(DBHelper.STOP_NAME));
                stopsValues.put(DBHelper.STOP_ID, dataObject.getInt(DBHelper.STOP_ID));
                stopsValues.put(DBHelper.STOP_LAT, dataObject.getDouble(DBHelper.STOP_LAT));
                stopsValues.put(DBHelper.STOP_LON, dataObject.getDouble(DBHelper.STOP_LON));
                stopsValues.put(DBHelper.STOP_CODE, dataObject.getInt(DBHelper.STOP_CODE));
                stopsValues.put(DBHelper.STOP_PARENT, dataObject.getInt(DBHelper.STOP_PARENT));
                stopProvider.insert(StopProvider.CONTENT_URI, stopsValues);


            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getArrivals){
//            Snackbar.make(view, "Message received, handling not yet implemented", Snackbar.LENGTH_SHORT).show();
            ArrayList<BusArrival> arrivalList = new ArrayList<BusArrival>();
            try{
                JSONObject dataObject;
                for (int i = 0; i<dataArray.length(); i++) {
                    dataObject = dataArray.getJSONObject(i);
                    arrivalList.add(new BusArrival(dataObject.getString("arrival_time"), dataObject.getInt("arrival_time_seconds"), dataObject.getString("trip_id") ));
                }

                //todo sort array by time

                Collections.sort(arrivalList, new BusTimeComparator());

                ArrivalAdapter adapter = new ArrivalAdapter(context, R.layout.arrival_list_item, arrivalList);
                list.setAdapter(adapter);
            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getRouteID){
            try {
                JSONObject dataObject = dataArray.getJSONObject(0);
                String routeID = dataObject.getString("route_id");
                APIAccess.getRouteName(routeID, this, context );
            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getRouteName){
            try {
                JSONObject dataObject = dataArray.getJSONObject(0);
                String routeName = dataObject.getString("route_short_name");
//                TextView textView = (TextView) view;
//                textView.setText(routeName);
                busArrival.setRoute(routeName);
            }catch (JSONException e){
                throw e;
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
