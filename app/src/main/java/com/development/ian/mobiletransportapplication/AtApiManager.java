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

    private Context context;


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
        requestQueue.add(jsonRequest);
    }

    public void getArrivalTimes(final String id, Context context, final APIResponseHandler responseHandler){

        ArrivalProvider.requestedButNotCompleted.add(Integer.parseInt(id)); //todo investage if there will be concurency issues

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
        requestQueue.add(jsonRequest);
    }

    public void getTrip(final String id, Context context, final APIResponseHandler responseHandler){

        TripProvider.requestedButNotCompleted.add(id);

        ConfirmQueueRunning(context);
        String requestUrl = REQUEST_TRIP_URL  + id;
        final TAG REQUEST_TAG= TAG.getTrip;
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
                    TripProvider.requestedButNotCompleted.remove(id); // todo test this!
                } //todo handle failure better
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

    private void ConfirmQueueRunning(Context context) {
        if(requestQueue == null) {
            this.context = context;
            StartRequestQueue(context);
        }
    }

    private void ConfirmQueueRunning() {
        ConfirmQueueRunning(this.context);
    }

    public void getRoute(final String id, final APIResponseHandler responseHandler){
        ConfirmQueueRunning();
        String requestUrl = REQUEST_ROUTE_URL + id;
        final TAG REQUEST_TAG= TAG.getRoute;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                JSONArray dataArray;
                try {
                    dataArray = response.getJSONArray("response");
//                    ArrivalsActivity.ResponseHandler.onSuccess(TAG.getArrivals, dataArray);
                    responseHandler.onSuccess(REQUEST_TAG, dataArray);
                } catch (JSONException e) {
                    responseHandler.onFailure(REQUEST_TAG, id, e);
                    // todo handle failure
                }catch (Exception e){
                    e.printStackTrace();
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

    public void getCalender(final String id, final APIResponseHandler responseHandler){
        ConfirmQueueRunning();
        String requestUrl = REQUEST_CALENDER_URL + id;
        final TAG REQUEST_TAG= TAG.getCalender;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                JSONArray dataArray;
                try {
                    dataArray = response.getJSONArray("response");
                    responseHandler.onSuccess(REQUEST_TAG, dataArray);
                } catch (JSONException e) {
                    responseHandler.onFailure(REQUEST_TAG, id, e);
                    // todo handle failure
                }catch (Exception e){
                    e.printStackTrace();
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

    public enum TAG{getStop, getArrivals, getRoute, getCalender, getVersion, getTrip}
}

