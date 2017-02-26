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

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;

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
               JSONObject dataObject;

               try { //todo handle incorrect input of stop id
                   dataArray = response.getJSONArray("response");
                   dataObject = dataArray.getJSONObject(0);
                   ContentValues values = new ContentValues();
                   values.put(DBHelper.STOPS_ID, id);
                   values.put(DBHelper.STOPS_NAME, dataObject.getString("stop_name"));
                   values.put(DBHelper.STOPS_LAT, Double.parseDouble(dataObject.getString("stop_lat")));
                   values.put(DBHelper.STOPS_LON, Double.parseDouble(dataObject.getString("stop_lon")));
                   stationProvider.insert(StationProvider.CONTENT_URI, values);
                   AddNewStopActivity.restoreUserControl();
                   AddNewStopActivity.displaySncakbarMessage(values.get(DBHelper.STOPS_ID) + " has been added");

               } catch (JSONException e) {
                   e.printStackTrace();
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
        jsonRequest.setTag(TAG.json);
        requestQueue.add(jsonRequest);
        return;
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
                requestQueue.cancelAll(TAG.json);
            }
        }catch (Exception e){

        }
    }

    public enum TAG{json}
}

