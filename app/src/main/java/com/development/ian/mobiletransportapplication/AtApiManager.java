package com.development.ian.mobiletransportapplication;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.development.ian.mobiletransportapplication.TransportContentProviders.ArrivalProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.CalenderProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.RouteProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.TripProvider;
//import com.development.ian.mobiletransportapplication.TransportContentProviders.StationProvider;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ian on 12/6/2016.
 */

public class AtApiManager {

    public static final String REQUEST_STOP_URL = "https://api.at.govt.nz/v2/gtfs/stops/stopId/";
    public static final String REQUEST_Arrival_TIMES_URL = "https://api.at.govt.nz/v2/gtfs/stopTimes/stopId/";
    public static final String REQUEST_TRIPS_ALL_URL = "https://api.at.govt.nz/v2/gtfs/trips";
    public static final String REQUEST_ROUTES_ALL_URL = "https://api.at.govt.nz/v2/gtfs/routes";
    public static final String REQUEST_CALENDER_ALL_URL = "https://api.at.govt.nz/v2/gtfs/calendar";

    public static final String REQUEST_ROUTE_URL = "https://api.at.govt.nz/v2/gtfs/routes/routeId/";
    public static final String REQUEST_TRIP_URL = "https://api.at.govt.nz/v2/gtfs/trips/tripId/";
    public static final String REQUEST_CALENDER_URL = "https://api.at.govt.nz/v2/gtfs/calendar/serviceId/";

    private static AtApiManager APIAccess = new AtApiManager();

    private final String SUBKEYLABEL = "Ocp-Apim-Subscription-Key";
    private final String KEY = "c9c8bfee133f4646a9c92c7dcb7229a9";
//    private StationProvider stationProvider= new StationProvider();

    static Cache cache;
    static Network network;
    static RequestQueue requestQueue;

    static int requestsMade =0;
    static int requestsActive=0;

    private Context context;

//    private synchronized static void updateCallNumbers(int i){
////        if(i == 1){
////            requestsMade++;
////            requestsActive++;
////        }else if(i == -1){
////            requestsActive--;
////        }
//    }

    public void getAllTrips(Context context, final APIResponseHandler responseHandler){ //todo write handling of response and initial call to this

//        ArrivalProvider.requestedButNotCompleted.add(Integer.parseInt(id));
        ConfirmQueueRunning(context);
        String requestUrl = REQUEST_TRIPS_ALL_URL;
        final TAG REQUEST_TAG= TAG.getTripAll;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                JSONArray dataArray;

                try {
                    dataArray = response.getJSONArray("response");
                    responseHandler.onSuccess(REQUEST_TAG, dataArray);
                } catch (JSONException e) {
                    responseHandler.onFailure(REQUEST_TAG,e.getMessage(), e);
                }
//                finally {
//                    updateCallNumbers(-1);
//                    ArrivalProvider.requestedButNotCompleted.remove(new Integer(Integer.parseInt(id)));
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<String, String>();
                headers.put(SUBKEYLABEL, KEY);
                return headers;
            }
        };
        jsonRequest.setTag(REQUEST_TAG);
//        updateCallNumbers(1); //todo remove
        requestQueue.add(jsonRequest);
    }

    public void getAllRoutes(Context context, final APIResponseHandler responseHandler){ //todo write handling of response and initial call to this

//        ArrivalProvider.requestedButNotCompleted.add(Integer.parseInt(id));
        ConfirmQueueRunning(context);
        String requestUrl = REQUEST_ROUTES_ALL_URL;
        final TAG REQUEST_TAG= TAG.getRouteAll;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                JSONArray dataArray;

                try {
                    dataArray = response.getJSONArray("response");
                    responseHandler.onSuccess(REQUEST_TAG, dataArray);
                } catch (JSONException e) {
                    responseHandler.onFailure(REQUEST_TAG,e.getMessage(), e);
                }
//                finally {
//                    updateCallNumbers(-1);
//                    ArrivalProvider.requestedButNotCompleted.remove(new Integer(Integer.parseInt(id)));
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<String, String>();
                headers.put(SUBKEYLABEL, KEY);
                return headers;
            }
        };
        jsonRequest.setTag(REQUEST_TAG);
//        updateCallNumbers(1); //todo remove
        requestQueue.add(jsonRequest);
    }

    public void getAllCalenders(Context context, final APIResponseHandler responseHandler){ //todo write handling of response and initial call to this

//        ArrivalProvider.requestedButNotCompleted.add(Integer.parseInt(id));
        ConfirmQueueRunning(context);
        String requestUrl = REQUEST_CALENDER_ALL_URL;
        final TAG REQUEST_TAG= TAG.getCalenderAll;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                JSONArray dataArray;

                try {
                    dataArray = response.getJSONArray("response");
                    responseHandler.onSuccess(REQUEST_TAG, dataArray);
                } catch (JSONException e) {
                    responseHandler.onFailure(REQUEST_TAG,e.getMessage(), e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<String, String>();
                headers.put(SUBKEYLABEL, KEY);
                return headers;
            }
        };
        jsonRequest.setTag(REQUEST_TAG);
        requestQueue.add(jsonRequest);
    }


//    public int getRequestsMade(){
//        return requestsMade;
//    }
//    public int getRequestsActive(){
//        return requestsActive;
//    }


    public void GetStopById(final String id, final Context context, final APIResponseHandler responseHandler){
        final Request.Priority PRIORITY = Request.Priority.HIGH;
        ConfirmQueueRunning(context);
        String requestUrl = REQUEST_STOP_URL + id;
        final TAG REQUEST_TAG= TAG.getStop;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

           @Override
            public void onResponse(JSONObject response){
               JSONArray dataArray;
               try {
                   dataArray = response.getJSONArray("response");
                   responseHandler.onSuccess(REQUEST_TAG, dataArray);
               } catch (JSONException e) {
                   responseHandler.onFailure(REQUEST_TAG, id, e);
               }catch (Exception e){
                   e.printStackTrace();
               }finally {
//                   updateCallNumbers(-1);
               }
           }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //todo throw errors
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> headers = new HashMap<String, String>();
                headers.put(SUBKEYLABEL, KEY);
                return headers;
            }
            @Override
            public Request.Priority getPriority() {
                return PRIORITY;
            }


        };
        jsonRequest.setTag(REQUEST_TAG);
//        updateCallNumbers(1);
        requestQueue.add(jsonRequest);
    }

    public void getArrivalTimes(final String id, Context context, final APIResponseHandler responseHandler){

        ArrivalProvider.requestedButNotCompleted.add(Integer.parseInt(id));
        ConfirmQueueRunning(context);
        String requestUrl = REQUEST_Arrival_TIMES_URL  + id;
        final TAG REQUEST_TAG= TAG.getArrivals;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                JSONArray dataArray;

                try {
                    dataArray = response.getJSONArray("response");
                    responseHandler.onSuccess(REQUEST_TAG, dataArray);
                } catch (JSONException e) {
                    responseHandler.onFailure(REQUEST_TAG, id, e);
                } finally {
//                    updateCallNumbers(-1);
                    ArrivalProvider.requestedButNotCompleted.remove(new Integer(Integer.parseInt(id)));
                }
            }
        }, new Response.ErrorListener() {
            @Override
                public void onErrorResponse(VolleyError error) {}
        }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError{
                    Map<String, String> headers = new HashMap<String, String>();
                    headers.put(SUBKEYLABEL, KEY);
                    return headers;
                }
            };
        jsonRequest.setTag(REQUEST_TAG);
//        updateCallNumbers(1);
        requestQueue.add(jsonRequest);
    }

//    public void getTripById(final String id, Context context, final APIResponseHandler responseHandler){
//
//        TripProvider.requestedButNotCompleted.add(id);
//
//        ConfirmQueueRunning(context);
//        String requestUrl = REQUEST_TRIP_URL  + id;
//        final TAG REQUEST_TAG= TAG.getTrip;
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){
//
//            @Override
//            public void onResponse(JSONObject response){
//                JSONArray dataArray;
//
//                try {
//                    dataArray = response.getJSONArray("response");
//                    responseHandler.onSuccess(REQUEST_TAG, dataArray);
//                } catch (JSONException e) {
//                    responseHandler.onFailure(REQUEST_TAG, id, e);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }finally {
//                    TripProvider.requestedButNotCompleted.remove(id);
//                    updateCallNumbers(-1);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {}
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError{
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put(SUBKEYLABEL, KEY);
//                return headers;
//            }
//        };
//        jsonRequest.setTag(REQUEST_TAG);
//        updateCallNumbers(1);
//        requestQueue.add(jsonRequest);
//    }

    private void ConfirmQueueRunning(Context context) {
        if(requestQueue == null) {
            this.context = context;
            StartRequestQueue(context);
        }
    }

    private void ConfirmQueueRunning() {
        ConfirmQueueRunning(this.context);
    }

//    public void getRouteById(final String id, final APIResponseHandler responseHandler){
//        RouteProvider.requestedButNotCompleted.add(id);
//
//        ConfirmQueueRunning();
//        String requestUrl = REQUEST_ROUTE_URL + id;
//        final TAG REQUEST_TAG= TAG.getRoute;
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){
//
//            @Override
//            public void onResponse(JSONObject response){
//                JSONArray dataArray;
//                try {
//                    dataArray = response.getJSONArray("response");
////                    ArrivalsActivity.ResponseHandler.onSuccess(TAG.getArrivals, dataArray);
//                    responseHandler.onSuccess(REQUEST_TAG, dataArray);
//                } catch (JSONException e) {
//                    responseHandler.onFailure(REQUEST_TAG, id, e);
//                    // todo handle failure
//                }catch (Exception e){
//                    e.printStackTrace();
//                }finally {
//                    updateCallNumbers(-1);
//                    RouteProvider.requestedButNotCompleted.remove(id);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {}
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError{
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put(SUBKEYLABEL, KEY);
//                return headers;
//            }
//        };
//        jsonRequest.setTag(REQUEST_TAG);
//        updateCallNumbers(1);
//        requestQueue.add(jsonRequest);
//    }

//    public void getCalenderById(final String id, final APIResponseHandler responseHandler){
//
//        CalenderProvider.requestedButNotCompleted.add(id);
//
//        ConfirmQueueRunning();
//        String requestUrl = REQUEST_CALENDER_URL + id;
//        final TAG REQUEST_TAG= TAG.getCalender;
//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){
//
//            @Override
//            public void onResponse(JSONObject response){
//                JSONArray dataArray;
//                try {
//                    dataArray = response.getJSONArray("response");
//                    responseHandler.onSuccess(REQUEST_TAG, dataArray);
//                } catch (JSONException e) {
//                    responseHandler.onFailure(REQUEST_TAG, id, e);
//                    // todo handle failure
//                }catch (Exception e){
//                    e.printStackTrace();
//                }finally {
//                    updateCallNumbers(-1);
//                    CalenderProvider.requestedButNotCompleted.remove(id);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {}
//        }){
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError{
//                Map<String, String> headers = new HashMap<String, String>();
//                headers.put(SUBKEYLABEL, KEY);
//                return headers;
//            }
//        };
//        jsonRequest.setTag(REQUEST_TAG);
//        updateCallNumbers(1);
//        requestQueue.add(jsonRequest);
//    }



    private void StartRequestQueue(Context context) {
        cache = new DiskBasedCache(context.getCacheDir(), 1024 * 1024);
        network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
    }

    public static AtApiManager getInstance(){
        if(APIAccess == null){
            APIAccess = new AtApiManager();
        }
        return APIAccess;
    }

    public static void CancelStopRequests(){
        try {
            if(requestQueue!=null) {
                requestQueue.cancelAll(TAG.getStop);
            }
        }catch (Exception e){}
    }

    public enum TAG{getStop, getArrivals, getRoute, getCalender, getVersion, getTrip, getTripAll, getRouteAll, getCalenderAll}
}

