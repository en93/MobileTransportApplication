package com.development.ian.mobiletransportapplication;

import android.content.Context;
import android.view.View;

import org.json.JSONObject;

/**
 * Created by Ian on 3/1/2017.
 */

public interface responseHandler {
    public void onSuccess(AtApiManager.TAG tag, JSONObject dataObject);
    public void onFailure();
}

