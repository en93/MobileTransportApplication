package com.development.ian.mobiletransportapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class ArrivalsActivity extends AppCompatActivity {

    private String stopName;
    private String stopNumber;

    private TextView title;
    private TextView subtitle;
    private ListView list;

    public static APIResponseHandler responseHandler;

    private AtApiManager APIAccess = AtApiManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrivals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        stopName = intent.getStringExtra(StopsNavigationActivity.STOP_ADDRESS);
        stopNumber = intent.getStringExtra(StopsNavigationActivity.STOP_NUMBER);

        title = (TextView) findViewById(R.id.title);
        subtitle = (TextView) findViewById(R.id.subtitle);
        list = (ListView) findViewById(R.id.arrivalList);

        title.setText(stopName);
        subtitle.setText("Stop: " + stopNumber);

        responseHandler = new APIResponseHandler(findViewById(R.id.arrivals_layout), list, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        APIAccess.getArrivalTimes(stopNumber, getApplicationContext());
    }

}
