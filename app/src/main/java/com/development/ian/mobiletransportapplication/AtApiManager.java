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
import android.view.View;

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
    private static AtApiManager APIAccess = new AtApiManager();

    private final String SUBKEYLABEL = "Ocp-Apim-Subscription-Key";
    private final String KEY = "c9c8bfee133f4646a9c92c7dcb7229a9";
    private StationProvider stationProvider= new StationProvider();

    static Cache cache;
    static Network network;
    static RequestQueue requestQueue;


    public void GetStopById(final String id, final Context context){
        if(requestQueue == null) {
            StartRequestQueue(context);
        }
        String requestUrl = REQUEST_STOP_URL + id;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, requestUrl, null, new Response.Listener<JSONObject>(){

           @Override
            public void onResponse(JSONObject response){
               JSONArray dataArray;
               try {
                   dataArray = response.getJSONArray("response");
                   AddNewStopActivity.responseHandler.onSuccess(AtApiManager.TAG.getStop, dataArray);
               } catch (JSONException e) {
                   AddNewStopActivity.responseHandler.onFailure(AtApiManager.TAG.getStop, id);
               }catch (Exception e){
                   e.printStackTrace();
               }
           }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
        if(requestQueue == null) {
            StartRequestQueue(context);
        }
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
            }
        }catch (Exception e){}
    }

    public enum TAG{getStop, getArrivals}
}

