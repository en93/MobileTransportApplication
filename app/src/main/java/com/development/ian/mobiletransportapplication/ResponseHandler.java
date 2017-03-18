package com.development.ian.mobiletransportapplication;

import android.content.Context;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ian on 3/1/2017.
 */

public interface ResponseHandler {
    public void onSuccess(AtApiManager.TAG tag, JSONArray dataArray) throws JSONException;
    public void onFailure(AtApiManager.TAG tag, String detail);
}

