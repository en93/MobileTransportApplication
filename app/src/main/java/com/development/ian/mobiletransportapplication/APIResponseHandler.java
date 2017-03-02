package com.development.ian.mobiletransportapplication;

import android.content.ContentValues;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ian on 3/1/2017.
 */

public class APIResponseHandler implements responseHandler {

    private View view;
    private EditText editText;

    private StationProvider stationProvider = new StationProvider();

    public APIResponseHandler(View view, EditText editText){
        this.view = view;
        this.editText = editText;
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
//                AddNewStopActivity.displaySncakbarMessage(values.get(DBHelper.STOPS_ID) + " has been added");
            }catch (JSONException jEx){

            }

        }
    }


    @Override
    public void onFailure() {
        AddNewStopActivity.restoreUserControl();
        Snackbar.make(view, "Stop " + editText.getText() + " is not a valid stop", Snackbar.LENGTH_SHORT).show();
    }
}
