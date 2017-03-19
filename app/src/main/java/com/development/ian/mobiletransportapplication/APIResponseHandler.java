package com.development.ian.mobiletransportapplication;

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ListView;

import com.development.ian.mobiletransportapplication.TransportContentProviders.ArrivalProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.CalenderProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.RouteProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.SavedStopProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.StationProvider;
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

    private AtApiManager APIAccess = AtApiManager.getInstance();

    private StationProvider stationProvider = new StationProvider();//todo remove

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
    }

    public APIResponseHandler(BusArrival busArrival, Context context){
        this.context = context;
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
                stopValues.put(DBHelper.STOP_NAME, dataObject.getString(DBHelper.STOP_NAME));
                stopValues.put(DBHelper.STOP_ID, dataObject.getInt(DBHelper.STOP_ID));
                stopValues.put(DBHelper.STOP_LAT, dataObject.getDouble(DBHelper.STOP_LAT));
                stopValues.put(DBHelper.STOP_LON, dataObject.getDouble(DBHelper.STOP_LON));
                stopValues.put(DBHelper.STOP_CODE, dataObject.getInt(DBHelper.STOP_CODE));
                stopValues.put(DBHelper.STOP_PARENT, dataObject.getInt(DBHelper.STOP_PARENT));
                stopProvider.insert(StopProvider.CONTENT_URI, stopValues);

                APIAccess.getArrivalTimes(""+dataObject.getInt(DBHelper.STOP_ID), context, this);


            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getArrivals){
//            ArrayList<BusArrival> arrivalList = new ArrayList<BusArrival>();
            try{
                if(arrivalProvider==null){
                    arrivalProvider = new ArrivalProvider();
                }
                JSONObject dataObject;
                for (int i = 0; i<dataArray.length(); i++) {
                    dataObject = dataArray.getJSONObject(i);
                    ContentValues arrivalValues = new ContentValues();
                    arrivalValues.put(DBHelper.ARRIVAL_STOP_ID, dataObject.getString(DBHelper.ARRIVAL_STOP_ID));
                    arrivalValues.put(DBHelper.ARRIVAL_TRIP_ID, dataObject.getString(DBHelper.ARRIVAL_TRIP_ID));
                    arrivalValues.put(DBHelper.ARRIVAL_TIME, dataObject.getString(DBHelper.ARRIVAL_TIME));
                    arrivalValues.put(DBHelper.ARRIVAL_TIME_SECONDS, dataObject.getString(DBHelper.ARRIVAL_TIME_SECONDS));
                    arrivalProvider.insert(ArrivalProvider.CONTENT_URI, arrivalValues);

                    APIAccess.getTrip(dataObject.getString(DBHelper.ARRIVAL_TRIP_ID), context, this);

//                    arrivalList.add(new BusArrival(dataObject.getString("arrival_time"), dataObject.getInt("arrival_time_seconds"), dataObject.getString("trip_id") ));
                }


//                Collections.sort(arrivalList, new BusTimeComparator());
//
//                ArrivalAdapter adapter = new ArrivalAdapter(context, R.layout.arrival_list_item, arrivalList);
//                list.setAdapter(adapter);
            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getRoute){
            try {
                if(routeProvider ==null){
                    routeProvider = new RouteProvider();
                }
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
//        else if(tag == AtApiManager.TAG.getRouteName){ //todo fix or remove
//            try {
//                JSONObject dataObject = dataArray.getJSONObject(0);
//                String routeName = dataObject.getString("route_short_name");
////                TextView textView = (TextView) view;
////                textView.setText(routeName);
//                busArrival.setRoute(routeName);
//            }catch (JSONException e){
//                throw e;
//            }
//        }
        else if(tag == AtApiManager.TAG.getTrip){
            if(tripProvider == null){
                tripProvider = new TripProvider();
            }
            JSONObject dataObject = dataArray.getJSONObject(0);
            ContentValues tripValues = new ContentValues();
            tripValues.put(DBHelper.TRIP_ID, dataObject.getString(DBHelper.TRIP_ID));
            tripValues.put(DBHelper.TRIP_ROUTE_ID, dataObject.getString(DBHelper.TRIP_ROUTE_ID));
            tripValues.put(DBHelper.TRIP_HEADSIGN, dataObject.getString(DBHelper.TRIP_HEADSIGN));
            tripValues.put(DBHelper.TRIP_DIRECTION, dataObject.getString(DBHelper.TRIP_DIRECTION));
            tripValues.put(DBHelper.TRIP_SERVICE_ID, dataObject.getString(DBHelper.TRIP_SERVICE_ID));
            tripProvider.insert(TripProvider.CONTENT_URI, tripValues);

            //Call for route values using routeID
            APIAccess.getRoute(dataObject.getString(DBHelper.TRIP_ROUTE_ID), this);

            //Call for calender values by serviceID
            APIAccess.getCalender(dataObject.getString(DBHelper.TRIP_SERVICE_ID), this);
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
        }
    } //todo place inside of iterator just incase there is a case where multiple calanders are returned


    @Override
    public void onFailure(AtApiManager.TAG tag, String detail) {
        if(tag == AtApiManager.TAG.getStop) {
            AddNewStopActivity.restoreUserControl();
            Snackbar.make(view, "UTTER FAILURE... ABORT", Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    class BusTimeComparator implements Comparator<BusArrival>{

        @Override
        public int compare(BusArrival t1, BusArrival t2) {
            return Integer.compare(t1.getArrivalSeconds(), t2.getArrivalSeconds());
        }
    }

}
