package com.development.ian.mobiletransportapplication;

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ian on 3/1/2017.
 */

public class APIResponseHandler implements responseHandler {

    private View view;
    private ListView list;


    private StationProvider stationProvider = new StationProvider();

    public APIResponseHandler(View view){
        this.view = view;
    }

    public APIResponseHandler(View view, ListView list){
        this.view = view;
        this.list = list;
    }

    @Override
    public void onSuccess(AtApiManager.TAG tag, JSONObject dataObject) {
        if(tag == AtApiManager.TAG.getStop){
            try {
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
            Snackbar.make(view, "Message received, handling not yet implemented", Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onFailure(AtApiManager.TAG tag, String detail) {
        if(tag == AtApiManager.TAG.getStop) {
            AddNewStopActivity.restoreUserControl();
            Snackbar.make(view, "Stop " + detail + " is not a valid stop", Snackbar.LENGTH_SHORT).show();
        }
    }
}
