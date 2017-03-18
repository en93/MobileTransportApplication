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
    public static final String REQUEST_ROUTE_ID_URL = "https://api.at.govt.nz/v2/gtfs/trips/tripId/";
    public static final String REQUEST_ROUTE_NAME_URL = "https://api.at.govt.nz/v2/gtfs/routes/routeId/";
    private static AtApiManager APIAccess = new AtApiManager();

    private final String SUBKEYLABEL = "Ocp-Apim-Subscription-Key";
    private final String KEY = "c9c8bfee133f4646a9c92c7dcb7229a9";
    private StationProvider stationProvider= new StationProvider();

    static Cache cache;
    static Network network;
    static RequestQueue requestQueue;

    private Context context;


    public void GetStopById(final String id, final Context context, final APIResponseHandler responseHandler){
        ConfirmQueueRunning(context);
        String requestUrl = REQUEST_STOP_URL + id;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

           @Override
            public void onResponse(JSONObject response){
               JSONArray dataArray;
               try {
                   dataArray = response.getJSONArray("response");
                   responseHandler.onSuccess(AtApiManager.TAG.getStop, dataArray);
               } catch (JSONException e) {
                   responseHandler.onFailure(AtApiManager.TAG.getStop, id);
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

        };
        jsonRequest.setTag(TAG.getStop);
        requestQueue.add(jsonRequest);
    }

    public void getArrivalTimes(final String id, Context context){
        ConfirmQueueRunning(context);
        String requestUrl = REQUEST_Arrival_TIMES_URL  + id;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                JSONArray dataArray;

                try {
                    dataArray = response.getJSONArray("response");
                    ArrivalsActivity.responseHandler.onSuccess(TAG.getArrivals, dataArray);
                } catch (JSONException e) {
                    ArrivalsActivity.responseHandler.onFailure(AtApiManager.TAG.getStop, id);
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
        jsonRequest.setTag(TAG.getArrivals);
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

    public void getRouteId(final String id, final APIResponseHandler responseHandlerForAdapter){
        ConfirmQueueRunning();
        String requestUrl = REQUEST_ROUTE_ID_URL + id;
        final TAG REQUEST_TAG= TAG.getRouteID;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                JSONArray dataArray;
                try {
                    dataArray = response.getJSONArray("response");
//                    ArrivalsActivity.ResponseHandler.onSuccess(TAG.getArrivals, dataArray);
                    responseHandlerForAdapter.onSuccess(REQUEST_TAG, dataArray);
                } catch (JSONException e) {
//                    ArrivalsActivity.ResponseHandler.onFailure(AtApiManager.TAG.getStop, id);
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

    public void getRouteName(final String id, final APIResponseHandler responseHandlerForAdapter, Context context){
        ConfirmQueueRunning(context);
        String requestUrl = REQUEST_ROUTE_NAME_URL + id;
        final TAG REQUEST_TAG= TAG.getRouteName;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                JSONArray dataArray;
                try {
                    dataArray = response.getJSONArray("response");
//                    ArrivalsActivity.ResponseHandler.onSuccess(TAG.getArrivals, dataArray);
                    responseHandlerForAdapter.onSuccess(REQUEST_TAG, dataArray);
                } catch (JSONException e) {
//                    ArrivalsActivity.ResponseHandler.onFailure(AtApiManager.TAG.getStop, id);
                    System.out.print("something"); //todo remove
                    // todo handle failure
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print("something"); //todo remove
            }
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

    public static void CancelRequests(){
        try {
            if(requestQueue!=null) {
                requestQueue.cancelAll(TAG.getStop);
                requestQueue.cancelAll(TAG.getArrivals);
                requestQueue.cancelAll(TAG.getRouteID);
                requestQueue.cancelAll(TAG.getRouteName);
            }
        }catch (Exception e){}
    }

    public enum TAG{getStop, getArrivals, getRouteID, getRouteName}
}

