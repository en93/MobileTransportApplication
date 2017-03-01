package com.development.ian.mobiletransportapplication;

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ian on 3/1/2017.
 */

public class APIResponseHandler implements responseHandler {

    private StationProvider stationProvider = new StationProvider();

    @Override
    public void onSuccess(AtApiManager.TAG tag, JSONObject dataObject, View view) {
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
//                AddNewStopActivity.displaySncakbarMessage(values.get(DBHelper.STOPS_ID) + " has been added");
            }catch (JSONException jEx){

            }

        }
    }


    @Override
    public void onFailure() {

    }
}
