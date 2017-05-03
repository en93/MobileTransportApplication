package com.development.ian.mobiletransportapplication;

import android.content.AsyncQueryHandler;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.os.AsyncTask;
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
                ContentValues stopValues = new ContentValues();     //todo handle async rather than in UI thread
                stopValues.put(DBHelper.STOP_NAME, dataObject.getString(DBHelper.STOP_NAME));
                stopValues.put(DBHelper.STOP_ID, dataObject.getInt("stop_id")); //consider changing all getters to work like this
                stopValues.put(DBHelper.STOP_LAT, dataObject.getDouble(DBHelper.STOP_LAT));
                stopValues.put(DBHelper.STOP_LON, dataObject.getDouble(DBHelper.STOP_LON));
                stopValues.put(DBHelper.STOP_CODE, dataObject.getInt(DBHelper.STOP_CODE));
                stopValues.put(DBHelper.STOP_PARENT, dataObject.getInt(DBHelper.STOP_PARENT));
                stopProvider.insert(StopProvider.CONTENT_URI, stopValues);


//                if(!arrivalProvider.containsKeyValue(stopValues.get(DBHelper.STOP_ID).toString())) {
                //find arrivals for the station
                APIAccess.getArrivalTimes(stopValues.get(DBHelper.STOP_ID).toString(), context, this);
//               }else{
//                    AddNewStopActivity.restoreUserControl();
//                }


                Snackbar.make(view, "Stop " + stopValues.get(DBHelper.STOP_ID).toString() + " has been added", Snackbar.LENGTH_SHORT).show();

            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getArrivals){
            try{
//todo make async
                JSONObject dataObject;
                for (int i = 0; i<dataArray.length(); i++) {
                    dataObject = dataArray.getJSONObject(i);
                    ContentValues arrivalValues = new ContentValues();
                    arrivalValues.put(DBHelper.ARRIVAL_STOP_ID, dataObject.getString(DBHelper.ARRIVAL_STOP_ID));
                    arrivalValues.put(DBHelper.ARRIVAL_TRIP_ID, dataObject.getString(DBHelper.ARRIVAL_TRIP_ID));
                    arrivalValues.put(DBHelper.ARRIVAL_TIME, dataObject.getString(DBHelper.ARRIVAL_TIME));
                    arrivalValues.put(DBHelper.ARRIVAL_TIME_SECONDS, dataObject.getString(DBHelper.ARRIVAL_TIME_SECONDS));
                    arrivalProvider.insert(ArrivalProvider.CONTENT_URI, arrivalValues);

//                    if(!tripProvider.containsKeyValue(dataObject.getString(DBHelper.ARRIVAL_TRIP_ID))){
////                        APIAccess.getTripById(dataObject.getString(DBHelper.ARRIVAL_TRIP_ID), context, this);
//                    }
                }
                AddNewStopActivity.restoreUserControl();

            }catch (JSONException e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getRouteAll){
            try {
                JSONObject dataObject;
                AsyncQueryHandler queryHandler = new QueryHandler(context.getContentResolver());

                AsyncJsonParser parser = new AsyncJsonParser(queryHandler, dataArray, tag);
                parser.execute();

                /*
                for (int i = 0; i<dataArray.length(); i++) {
                    dataObject = dataArray.getJSONObject(i);
                    ContentValues routeValues = new ContentValues();
                    routeValues.put(DBHelper.ROUTE_ID, dataObject.getString(DBHelper.ROUTE_ID));
                    routeValues.put(DBHelper.ROUTE_AGENCY_ID, dataObject.getString(DBHelper.ROUTE_AGENCY_ID));
                    routeValues.put(DBHelper.ROUTE_SHORT_NAME, dataObject.getString(DBHelper.ROUTE_SHORT_NAME));
                    routeValues.put(DBHelper.ROUTE_LONG_NAME, dataObject.getString(DBHelper.ROUTE_LONG_NAME));
//                    routeProvider.insert(RouteProvider.CONTENT_URI, routeValues);


                    queryHandler.startInsert(i, null, RouteProvider.CONTENT_URI, routeValues);

                }
                if(counter.CanRestoreUserControl()){
                    StopsNavigationActivity.restoreUserControl(); //todo reconsider this approach
                }

                */
            }catch (Exception e){
                throw e;
            }
        }
        else if(tag == AtApiManager.TAG.getCalenderAll){
            try {
                JSONObject dataObject;
                AsyncQueryHandler queryHandler = new QueryHandler(context.getContentResolver());

                AsyncJsonParser parser = new AsyncJsonParser(queryHandler, dataArray, tag);
                parser.execute();

                /*
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
                    queryHandler.startInsert(i, null, CalenderProvider.CONTENT_URI, calenderValues);
//                    calenderProvider.insert(CalenderProvider.CONTENT_URI, calenderValues);
                }
                if(counter.CanRestoreUserControl()){
                    StopsNavigationActivity.restoreUserControl();
                }
                */
            }catch (Exception e){
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

        /* TODO testing async here*/
        else if(tag == AtApiManager.TAG.getTripAll){
//            JSONObject dataObject = dataArray.getJSONObject(0);
            AsyncQueryHandler queryHandler = new QueryHandler(context.getContentResolver());

            AsyncJsonParser parser = new AsyncJsonParser(queryHandler, dataArray, tag);
            parser.execute();

//            JSONObject dataObject;
//            for (int i = 0; i< dataArray.length(); i++) {
//                dataObject = dataArray.getJSONObject(i);
//                ContentValues tripValues = new ContentValues();
//                tripValues.put(DBHelper.TRIP_ID, dataObject.getString(DBHelper.TRIP_ID));
//                tripValues.put(DBHelper.TRIP_ROUTE_ID, dataObject.getString(DBHelper.TRIP_ROUTE_ID));
//                tripValues.put(DBHelper.TRIP_HEADSIGN, dataObject.getString(DBHelper.TRIP_HEADSIGN));
//                tripValues.put(DBHelper.TRIP_DIRECTION, dataObject.getString(DBHelper.TRIP_DIRECTION));
//                tripValues.put(DBHelper.TRIP_SERVICE_ID, dataObject.getString(DBHelper.TRIP_SERVICE_ID));
//                queryHandler.startInsert(i, null, TripProvider.CONTENT_URI, tripValues);
//            }
//            if(counter.CanRestoreUserControl()){
//                StopsNavigationActivity.restoreUserControl();
//            }
        //todo end testing


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

    class AsyncJsonParser extends AsyncTask<Void, Void, Void>{

        AsyncQueryHandler queryHandler;
        JSONArray dataArray;
        AtApiManager.TAG tag;
        SQLiteDatabase db;

        AsyncJsonParser(AsyncQueryHandler a, JSONArray j, AtApiManager.TAG t){
            queryHandler = a;
            dataArray = j;
            tag = t;
            db = DBHelper.getInstance(context).getWritableDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //todo use switch to decide method to use to create
            try {
                if (tag == AtApiManager.TAG.getTripAll) {
                    parseTrips();
                }else if(tag == AtApiManager.TAG.getRouteAll){
                    parseRoute();
                }else if(tag == AtApiManager.TAG.getCalenderAll){
                    parseCalender();
                }
            }catch (JSONException e){                //todo handle
                Log.e("JSON_Exception", e.getMessage());
            }catch (Exception e){                //todo handle
                Log.e("Other_exception", e.getMessage());
            }
            return null;
        }

        private void parseRoute() throws JSONException {
            JSONObject dataObject;
//            ContentValues routeValues;
//            ContentValues[] values = new ContentValues[dataArray.length()];

            String sqlInsert = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES (?,?,?,?);",
                    DBHelper.ROUTE_TABLE, DBHelper.ROUTE_ID, DBHelper.ROUTE_AGENCY_ID, DBHelper.ROUTE_SHORT_NAME, DBHelper.ROUTE_LONG_NAME);
            SQLiteStatement statement = db.compileStatement(sqlInsert);

            db.beginTransaction();
            for (int i = 0; i < dataArray.length(); i++) {

            /* //works but is too slow
            dataObject = dataArray.getJSONObject(i);
            routeValues = new ContentValues();
            routeValues.put(DBHelper.ROUTE_ID, dataObject.getString(DBHelper.ROUTE_ID));
            routeValues.put(DBHelper.ROUTE_AGENCY_ID, dataObject.getString(DBHelper.ROUTE_AGENCY_ID));
            routeValues.put(DBHelper.ROUTE_SHORT_NAME, dataObject.getString(DBHelper.ROUTE_SHORT_NAME));
            routeValues.put(DBHelper.ROUTE_LONG_NAME, dataObject.getString(DBHelper.ROUTE_LONG_NAME));
//                queryHandler.startInsert(i, null, RouteProvider.CONTENT_URI, routeValues);
//                routeProvider.insert(RouteProvider.CONTENT_URI, routeValues);
*/
                dataObject = dataArray.getJSONObject(i);
                statement.clearBindings();
                statement.bindString(1, dataObject.getString(DBHelper.ROUTE_ID));
                statement.bindString(2, dataObject.getString(DBHelper.ROUTE_AGENCY_ID));
                statement.bindString(3, dataObject.getString(DBHelper.ROUTE_SHORT_NAME));
                statement.bindString(4, dataObject.getString(DBHelper.ROUTE_LONG_NAME));
                statement.execute();

//                values[i] = routeValues;
            }

            db.setTransactionSuccessful();
            db.endTransaction();
//            context.getContentResolver().bulkInsert(RouteProvider.CONTENT_URI,values);
        }

        private void parseCalender() throws JSONException {
            JSONObject dataObject;
//            ContentValues calenderValues;
//            ContentValues[] values = new ContentValues[dataArray.length()];

            String sqlInsert = String.format("INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (?,?,?,?,?,?,?,?,?,?);",
                    DBHelper.CALENDER_TABLE, DBHelper.CALENDER_SERVICE_ID, DBHelper.CALENDER_MONDAY,
                    DBHelper.CALENDER__TUESDAY, DBHelper.CALENDER_WEDNESDAY, DBHelper.CALENDER_THURSDAY,
                    DBHelper.CALENDER_FRIDAY, DBHelper.CALENDER_SATURDAY, DBHelper.CALENDER_SUNDAY,
                    DBHelper.CALENDER_START, DBHelper.CALENDER_END);
            SQLiteStatement statement = db.compileStatement(sqlInsert);

            db.beginTransaction();
            for (int i = 0; i<dataArray.length(); i++) {
                /*
                dataObject = dataArray.getJSONObject(i);
                calenderValues = new ContentValues();
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
//                queryHandler.startInsert(i, null, CalenderProvider.CONTENT_URI, calenderValues);
//                calenderProvider.insert(CalenderProvider.CONTENT_URI, calenderValues);
//                values[i] = calenderValues;
*/
                dataObject = dataArray.getJSONObject(i);
                statement.clearBindings();
                statement.bindString(1, dataObject.getString(DBHelper.CALENDER_SERVICE_ID));
                statement.bindLong(2, dataObject.getInt(DBHelper.CALENDER_MONDAY));
                statement.bindLong(3, dataObject.getInt(DBHelper.CALENDER__TUESDAY));
                statement.bindLong(4, dataObject.getInt(DBHelper.CALENDER_WEDNESDAY));
                statement.bindLong(5, dataObject.getInt(DBHelper.CALENDER_THURSDAY));
                statement.bindLong(6, dataObject.getInt(DBHelper.CALENDER_FRIDAY));
                statement.bindLong(7, dataObject.getInt(DBHelper.CALENDER_SATURDAY));
                statement.bindLong(8, dataObject.getInt(DBHelper.CALENDER_SUNDAY));
                statement.bindString(9, dataObject.getString(DBHelper.CALENDER_START));
                statement.bindString(10, dataObject.getString(DBHelper.CALENDER_END));
                statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
//            context.getContentResolver().bulkInsert(CalenderProvider.CONTENT_URI, values);
        }

        private void parseTrips() throws JSONException {
            JSONObject dataObject;
//            ContentValues tripValues;

            String sqlInsert = String.format("INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?,?,?,?,?);",
                    DBHelper.TRIP_TABLE, DBHelper.TRIP_ROUTE_ID, DBHelper.TRIP_SERVICE_ID, DBHelper.TRIP_ID, DBHelper.TRIP_HEADSIGN, DBHelper.TRIP_DIRECTION);
            SQLiteStatement statement = db.compileStatement(sqlInsert);

//            ContentValues[] values = new ContentValues[dataArray.length()];
            db.beginTransaction();
            for (int i = 0; i< dataArray.length(); i++) {
             /*   dataObject = dataArray.getJSONObject(i);
                tripValues = new ContentValues();
                tripValues.put(DBHelper.TRIP_ID, dataObject.getString(DBHelper.TRIP_ID));
                tripValues.put(DBHelper.TRIP_ROUTE_ID, dataObject.getString(DBHelper.TRIP_ROUTE_ID));
                tripValues.put(DBHelper.TRIP_HEADSIGN, dataObject.getString(DBHelper.TRIP_HEADSIGN));
                tripValues.put(DBHelper.TRIP_DIRECTION, dataObject.getString(DBHelper.TRIP_DIRECTION));
                tripValues.put(DBHelper.TRIP_SERVICE_ID, dataObject.getString(DBHelper.TRIP_SERVICE_ID));
//                queryHandler.startInsert(i, null, TripProvider.CONTENT_URI, tripValues);
//                tripProvider.insert(TripProvider.CONTENT_URI, tripValues);
//                values[i] = tripValues;
*/
                dataObject = dataArray.getJSONObject(i);
                statement.clearBindings();
                statement.bindString(1, dataObject.getString(DBHelper.TRIP_ROUTE_ID));
                statement.bindString(2, dataObject.getString(DBHelper.TRIP_SERVICE_ID));
                statement.bindString(3, dataObject.getString(DBHelper.TRIP_ID));
                statement.bindString(4, dataObject.getString(DBHelper.TRIP_HEADSIGN));
                statement.bindString(5, dataObject.getString(DBHelper.TRIP_DIRECTION));
                statement.execute();

            }
            db.setTransactionSuccessful();
            db.endTransaction();
//            context.getContentResolver().bulkInsert(TripProvider.CONTENT_URI,values);
        }

        @Override
        protected void onPostExecute(Void v){
            if(counter.CanRestoreUserControl()){
                StopsNavigationActivity.restoreUserControl();
            }
        }
    }

    //todo feedback on what stage it is at

}
