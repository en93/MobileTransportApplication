package com.development.ian.mobiletransportapplication;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.development.ian.mobiletransportapplication.TransportContentProviders.SavedStopProvider;
//import com.development.ian.mobiletransportapplication.TransportContentProviders.StationProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.StopProvider;


public class StopsNavigationActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter cAdapter;
//    private StationProvider stationProvider;
    private StopProvider stopProvider;
    private SavedStopProvider savedStopProvider;

    public static final String STOP_NUMBER = "StopNumber";
    public static final String STOP_ADDRESS = "StopAddress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ListView list = (ListView) findViewById(R.id.stopList);
        stopProvider = new StopProvider();
        cAdapter = new StopsCursorAdapter(this, null, 0);
        list.setAdapter(cAdapter);
        getLoaderManager().initLoader(0,null,this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewStopActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stops_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_clear_data) { //todo put new handling here
//            if(stationProvider == null){
//                stationProvider = new StationProvider();
//            }
//        if(savedStopProvider== null){
//            savedStopProvider = new SavedStopProvider();
//        }
//        if(stopProvider == null){
//            stopProvider = new StopProvider();
//        }
//        stationProvider.delete(StationProvider.CONTENT_URI, null, null);
//        savedStopProvider.delete(StationProvider.CONTENT_URI, null, null);
//        stopProvider.delete(StationProvider.CONTENT_URI, null, null);
//        restartLoader();
//        return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return new CursorLoader(this, StationProvider.CONTENT_URI, null, null, null, null);
        return new CursorLoader(this, StopProvider.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cAdapter.swapCursor(null);
    }

    public void onResume(){
        super.onResume();
        restartLoader();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    public void showArrivals(View view) {
        String stopNumber;
        String stopAddress;
        TextView tvId = (TextView) view.findViewById(R.id.tvStopNum);
        TextView tvAd = (TextView) view.findViewById(R.id.tvStop);
        stopNumber = tvId.getText().toString().substring(6);
        stopAddress = tvAd.getText().toString();
        Intent intent = new Intent(this, ArrivalsActivity.class);
        intent.putExtra(STOP_NUMBER, stopNumber);
        intent.putExtra(STOP_ADDRESS, stopAddress);
        startActivity(intent);
    }
}