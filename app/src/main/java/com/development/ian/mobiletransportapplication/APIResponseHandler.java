package com.development.ian.mobiletransportapplication;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.development.ian.mobiletransportapplication.TransportContentProviders.ArrivalProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.CalenderProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.QueryHandler;
import com.development.ian.mobiletransportapplication.TransportContentProviders.RouteProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.SavedStopProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.StopProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.TripProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.VersionProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

/**
 * Created by Ian on 3/1/2017.
 */

public class APIResponseHandler implements ResponseHandler {

    private View view;
    private ListView list;
    private Context context;

    private BusArrival busArrival;
    private StopsNavigationActivity.CompletedCounter counter;

    private AtApiManager APIAccess = AtApiManager.getInstance();

//    private StationProvider stationProvider = new StationProvider();//todo remove

    private SavedStopProvider savedStopProvider;
    private StopProvider stopProvider;
    private ArrivalProvider arrivalProvider;
    private TripProvider tripProvider;
    private RouteProvider routeProvider;
    private VersionProvider versionProvider;
    private CalenderProvider calenderProvider;

    public APIResponseHandler(View view, Context context){
        this.context=context;
        this.view = view;
        CreateProviders();
    }

    private void CreateProviders() {
        savedStopProvider = new SavedStopProvider();
         stopProvider = new StopProvider();
         arrivalProvider = new ArrivalProvider();
         tripProvider = new TripProvider();
         routeProvider = new RouteProvider();
         versionProvider= new VersionProvider();
         calenderProvider= new CalenderProvider();
    }

//    public APIResponseHandler(BusArrival busArrival, Context context){
//        this.context = context;
//        this.busArrival = busArrival;
//        CreateProviders();
//    }

    public APIResponseHandler(View view, ListView list, Context context){
        this.view = view;
        this.list = list;
        this.context = context;
        CreateProviders();
    }

    public APIResponseHandler(View view, Context context, StopsNavigationActivity.CompletedCounter counter){
        this.view = view;
//        this.list = list;
        this.context = context;
        CreateProviders();
        this.counter = counter;
    }


    @Override
    public void onSuccess(AtApiManager.TAG tag, JSONArray dataArray) throws JSONException { //todo handle exception rather than throwing
        if(tag == AtApiManager.TAG.getStop){ //todo move to other thread
            try {
                //old
//
//                ContentValues stopsValues = new ContentValues();
//                stopsValues.put(DBHelper.STOPS_ID, dataObject.getString("stop_id"));
//                stopsValues.put(DBHelper.STOPS_NAME, dataObject.getString("stop_name"));
//                stopsValues.put(DBHelper.STOPS_LAT, Double.parseDouble(dataObject.getString("stop_lat")));
//                stopsValues.put(DBHelper.STOPS_LON, Double.parseDouble(dataObject.getString("stop_lon")));
//                stationProvider.insert(StationProvider.CONTENT_URI, stopsValues);
//                AddNewStopActivity.restoreUserControl();
//                Snackbar.make(view, "Stop " + stopsValues.get(DBHelper.STOPS_ID) + " has been added", Snackbar.LENGTH_SHORT).show();
                //end old

                //add to saved stops
                JSONObject dataObject = dataArray.getJSONObject(0);

                ContentValues savedStopValues = new ContentValues();
                savedStopValues.put(DBHelper.SAVED_STOP_ID, dataObject.getInt(DBHelper.SAVED_STOP_ID));
                savedStopProvider.insert(SavedStopProvider.CONTENT_URI, savedStopValues);

                //add to stops
                if(stopProvider==null){
                    stopProvider = new StopProvider();
                }
                ContentValues stopValues = new ContentValues();
                stopValues.put(DBHelper.STOP_NAME, dataObject.getString(DBHelper.STOP_NAME));
                stopValues.put(DBHelper.STOP_ID, dataObject.getInt("stop_id")); //consider changing all getters to work like this
                stopValues.put(DBHelper.STOP_LAT, dataObject.getDouble(DBHelper.STOP_LAT));
                stopValues.put(DBHelper.STOP_LON, dataObject.getDouble(DBHelper.STOP_LON));
                stopValues.put(DBHelper.STOP_CODE, dataObject.getInt(DBHelper.STOP_CODE));
                stopValues.put(DBHelper.STOP_PARENT, dataObject.getInt(DBHelper.STOP_PARENT));
                stopProvider.insert(StopProvider.CONTENT_URI, stopValues);

//                if(!arrivalProvider.containsKeyValue(stopValues.get(DBHelper.STOP_ID).toString())) {
//                    APIAccess.getArrivalTimes(stopValues.get(DBHelper.STOP_ID).toString(), context, this);
//                }

                AddNewStopActivity.restoreUserControl();
                Snackbar.make(view, "Stop " + stopValues.get(DBHelper.STOP_ID).toString() + " has been added", Snackbar.LENGTH_SHORT).show();

            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getArrivals){
            try{

                JSONObject dataObject;
                for (int i = 0; i<dataArray.length(); i++) {
                    dataObject = dataArray.getJSONObject(i);
                    ContentValues arrivalValues = new ContentValues();
                    arrivalValues.put(DBHelper.ARRIVAL_STOP_ID, dataObject.getString(DBHelper.ARRIVAL_STOP_ID));
                    arrivalValues.put(DBHelper.ARRIVAL_TRIP_ID, dataObject.getString(DBHelper.ARRIVAL_TRIP_ID));
                    arrivalValues.put(DBHelper.ARRIVAL_TIME, dataObject.getString(DBHelper.ARRIVAL_TIME));
                    arrivalValues.put(DBHelper.ARRIVAL_TIME_SECONDS, dataObject.getString(DBHelper.ARRIVAL_TIME_SECONDS));
                    arrivalProvider.insert(ArrivalProvider.CONTENT_URI, arrivalValues);

                    if(!tripProvider.containsKeyValue(dataObject.getString(DBHelper.ARRIVAL_TRIP_ID))){
//                        APIAccess.getTripById(dataObject.getString(DBHelper.ARRIVAL_TRIP_ID), context, this);
                    }
                }
            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getRouteAll){
            try {
                JSONObject dataObject;
                for (int i = 0; i<dataArray.length(); i++) {
                    dataObject = dataArray.getJSONObject(i);
                    ContentValues routeValues = new ContentValues();
                    routeValues.put(DBHelper.ROUTE_ID, dataObject.getString(DBHelper.ROUTE_ID));
                    routeValues.put(DBHelper.ROUTE_AGENCY_ID, dataObject.getString(DBHelper.ROUTE_AGENCY_ID));
                    routeValues.put(DBHelper.ROUTE_SHORT_NAME, dataObject.getString(DBHelper.ROUTE_SHORT_NAME));
                    routeValues.put(DBHelper.ROUTE_LONG_NAME, dataObject.getString(DBHelper.ROUTE_LONG_NAME));
//                    routeProvider.insert(RouteProvider.CONTENT_URI, routeValues);

                    AsyncQueryHandler queryHandler = new QueryHandler(context.getContentResolver());
                    queryHandler.startInsert(i, null, RouteProvider.CONTENT_URI, routeValues);

                }
                if(counter.CanRestoreUserControl()){
                    StopsNavigationActivity.restoreUserControl(); //todo reconsider this approach
                }
            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getCalenderAll){
            try {
                JSONObject dataObject;
                for (int i = 0; i<dataArray.length(); i++) {
                    dataObject = dataArray.getJSONObject(i);
                    ContentValues calenderValues = new ContentValues();
                    calenderValues.put(DBHelper.CALENDER_SERVICE_ID, dataObject.getString(DBHelper.CALENDER_SERVICE_ID));
                    calenderValues.put(DBHelper.CALENDER_MONDAY, dataObject.getInt(DBHelper.CALENDER_MONDAY));
                    calenderValues.put(DBHelper.CALENDER__TUESDAY, dataObject.getInt(DBHelper.CALENDER__TUESDAY));
                    calenderValues.put(DBHelper.CALENDER_WEDNESDAY, dataObject.getInt(DBHelper.CALENDER_WEDNESDAY));
                    calenderValues.put(DBHelper.CALENDER_THURSDAY, dataObject.getInt(DBHelper.CALENDER_THURSDAY));
                    calenderValues.put(DBHelper.CALENDER_FRIDAY, dataObject.getInt(DBHelper.CALENDER_FRIDAY));
                    calenderValues.put(DBHelper.CALENDER_SATURDAY, dataObject.getInt(DBHelper.CALENDER_SATURDAY));
                    calenderValues.put(DBHelper.CALENDER_SUNDAY, dataObject.getInt(DBHelper.CALENDER_SUNDAY));
                    calenderValues.put(DBHelper.CALENDER_START, dataObject.getString(DBHelper.CALENDER_START));
                    calenderValues.put(DBHelper.CALENDER_END, dataObject.getString(DBHelper.CALENDER_END));
                    calenderProvider.insert(CalenderProvider.CONTENT_URI, calenderValues);
                }
                if(counter.CanRestoreUserControl()){
                    StopsNavigationActivity.restoreUserControl();
                }
            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getRoute){
            try {
                JSONObject dataObject = dataArray.getJSONObject(0);
                ContentValues routeValues = new ContentValues();
                routeValues.put(DBHelper.ROUTE_ID, dataObject.getString(DBHelper.ROUTE_ID));
                routeValues.put(DBHelper.ROUTE_AGENCY_ID, dataObject.getString(DBHelper.ROUTE_AGENCY_ID));
                routeValues.put(DBHelper.ROUTE_SHORT_NAME, dataObject.getString(DBHelper.ROUTE_SHORT_NAME));
                routeValues.put(DBHelper.ROUTE_LONG_NAME, dataObject.getString(DBHelper.ROUTE_LONG_NAME));
                routeProvider.insert(RouteProvider.CONTENT_URI, routeValues);
            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getTrip){
            JSONObject dataObject = dataArray.getJSONObject(0);
            ContentValues tripValues = new ContentValues();
            tripValues.put(DBHelper.TRIP_ID, dataObject.getString(DBHelper.TRIP_ID));
            tripValues.put(DBHelper.TRIP_ROUTE_ID, dataObject.getString(DBHelper.TRIP_ROUTE_ID));
            tripValues.put(DBHelper.TRIP_HEADSIGN, dataObject.getString(DBHelper.TRIP_HEADSIGN));
            tripValues.put(DBHelper.TRIP_DIRECTION, dataObject.getString(DBHelper.TRIP_DIRECTION));
            tripValues.put(DBHelper.TRIP_SERVICE_ID, dataObject.getString(DBHelper.TRIP_SERVICE_ID));
            tripProvider.insert(TripProvider.CONTENT_URI, tripValues);

            //Call for route values using routeID
            if(!routeProvider.containsKeyValue(dataObject.getString(DBHelper.TRIP_ROUTE_ID))) {
//                APIAccess.getRouteById(dataObject.getString(DBHelper.TRIP_ROUTE_ID), this);
            }

            //Call for calender values by serviceID
            if(!calenderProvider.containsKeyValue(dataObject.getString(DBHelper.TRIP_SERVICE_ID))) {
//                APIAccess.getCalenderById(dataObject.getString(DBHelper.TRIP_SERVICE_ID), this);
            }
        }
        else if(tag == AtApiManager.TAG.getTripAll){
//            JSONObject dataObject = dataArray.getJSONObject(0);
            JSONObject dataObject;
            for (int i = 0; i< dataArray.length(); i++) {
                dataObject = dataArray.getJSONObject(i);
                ContentValues tripValues = new ContentValues();
                tripValues.put(DBHelper.TRIP_ID, dataObject.getString(DBHelper.TRIP_ID));
                tripValues.put(DBHelper.TRIP_ROUTE_ID, dataObject.getString(DBHelper.TRIP_ROUTE_ID));
                tripValues.put(DBHelper.TRIP_HEADSIGN, dataObject.getString(DBHelper.TRIP_HEADSIGN));
                tripValues.put(DBHelper.TRIP_DIRECTION, dataObject.getString(DBHelper.TRIP_DIRECTION));
                tripValues.put(DBHelper.TRIP_SERVICE_ID, dataObject.getString(DBHelper.TRIP_SERVICE_ID));
                tripProvider.insert(TripProvider.CONTENT_URI, tripValues);
            }
            if(counter.CanRestoreUserControl()){
                StopsNavigationActivity.restoreUserControl();
            }

            //Call for route values using routeID
//            if(!routeProvider.containsKeyValue(dataObject.getString(DBHelper.TRIP_ROUTE_ID))) {
////                APIAccess.getRouteById(dataObject.getString(DBHelper.TRIP_ROUTE_ID), this);
//            }
//
//            //Call for calender values by serviceID
//            if(!calenderProvider.containsKeyValue(dataObject.getString(DBHelper.TRIP_SERVICE_ID))) {
////                APIAccess.getCalenderById(dataObject.getString(DBHelper.TRIP_SERVICE_ID), this);
//            }
        }
        else if(tag == AtApiManager.TAG.getCalender){
            if(calenderProvider ==null){
                calenderProvider = new CalenderProvider();
            }
            JSONObject dataObject = dataArray.getJSONObject(0);
            ContentValues calenderValues = new ContentValues();
            calenderValues.put(DBHelper.CALENDER_SERVICE_ID, dataObject.getString(DBHelper.CALENDER_SERVICE_ID));
            calenderValues.put(DBHelper.CALENDER_MONDAY, dataObject.getInt(DBHelper.CALENDER_MONDAY));
            calenderValues.put(DBHelper.CALENDER__TUESDAY, dataObject.getInt(DBHelper.CALENDER__TUESDAY));
            calenderValues.put(DBHelper.CALENDER_WEDNESDAY, dataObject.getInt(DBHelper.CALENDER_WEDNESDAY));
            calenderValues.put(DBHelper.CALENDER_THURSDAY, dataObject.getInt(DBHelper.CALENDER_THURSDAY));
            calenderValues.put(DBHelper.CALENDER_FRIDAY, dataObject.getInt(DBHelper.CALENDER_FRIDAY));
            calenderValues.put(DBHelper.CALENDER_SATURDAY, dataObject.getInt(DBHelper.CALENDER_SATURDAY));
            calenderValues.put(DBHelper.CALENDER_SUNDAY, dataObject.getInt(DBHelper.CALENDER_SUNDAY));
            calenderValues.put(DBHelper.CALENDER_START, dataObject.getString(DBHelper.CALENDER_START));
            calenderValues.put(DBHelper.CALENDER_END, dataObject.getString(DBHelper.CALENDER_END));
            calenderProvider.insert(CalenderProvider.CONTENT_URI, calenderValues);
        }
    } //todo place inside of iterator just incase there is a case where multiple calanders are returned


    @Override
    public void onFailure(AtApiManager.TAG tag, String detail, JSONException e) {
        if(tag == AtApiManager.TAG.getStop) {
            AddNewStopActivity.restoreUserControl();
            Snackbar.make(view, "UTTER FAILURE... ABORT", Snackbar.LENGTH_INDEFINITE).show();
            Log.e(""+tag, "onFailure: " + detail +",     " + e.getMessage() );
        }
    }

    class BusTimeComparator implements Comparator<BusArrival>{

        @Override
        public int compare(BusArrival t1, BusArrival t2) {
            return Integer.compare(t1.getArrivalSeconds(), t2.getArrivalSeconds());
        }
    }

}
