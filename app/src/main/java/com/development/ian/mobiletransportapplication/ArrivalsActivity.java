package com.development.ian.mobiletransportapplication;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.development.ian.mobiletransportapplication.TransportContentProviders.ArrivalProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.StopProvider;

public class ArrivalsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private String stopName;
    private String stopNumber;

    private TextView title;
    private TextView subtitle;
    private ListView list;

    private CursorAdapter cAdapter;

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

        cAdapter = new ArrivalCursorAdapter(this, null, 0);
        list.setAdapter(cAdapter);
        getLoaderManager().initLoader(0,null,this);

//        responseHandler = new APIResponseHandler(findViewById(R.id.arrivals_layout), list, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        APIAccess.getArrivalTimes(stopNumber, getApplicationContext());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, ArrivalProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cAdapter.swapCursor(null);
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    public void onResume(){
        super.onResume();
        restartLoader();
    }
}
