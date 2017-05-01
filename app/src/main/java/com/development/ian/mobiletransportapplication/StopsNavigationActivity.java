package com.development.ian.mobiletransportapplication;

import android.app.DownloadManager;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.Calendar;

//import com.development.ian.mobiletransportapplication.TransportContentProviders.StationProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.StopProvider;
import com.development.ian.mobiletransportapplication.TransportContentProviders.TripProvider;


public class StopsNavigationActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private CursorAdapter cAdapter;
//    private StationProvider stationProvider;
    private StopProvider stopProvider;
//    private SavedStopProvider savedStopProvider;
    private TripProvider tripProvider;

    private static Window window ;
    private static ProgressBar progressBar;

    public static final String STOP_NUMBER = "StopNumber";
    public static final String STOP_ADDRESS = "StopAddress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stops_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        window = getWindow();
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
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

        tripProvider = new TripProvider();

        //setup database
        if(tripProvider.isEmpty()){
            //do db request
            RemoveUserControl();
            CompletedCounter counter = new CompletedCounter(3); //todo change back to three once testing completed
            Context c = getApplicationContext();

//            //Use to make sure you access the right file
//            Calendar calender = Calendar.getInstance();
//            String seconds = ""+calender.get(Calendar.SECOND)+"_" + calender.get(Calendar.MINUTE)+"_"+ calender.get(Calendar.HOUR)+"_" +calender.get(Calendar.DAY_OF_YEAR)+"_"+calender.get(Calendar.YEAR);
//
//            DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//
//            DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(AtApiManager.REQUEST_TRIPS_ALL_URL));
//            request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE); //todo make hidden
//            request1.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//            request1.addRequestHeader(AtApiManager.SUBKEYLABEL, AtApiManager.KEY);
//            request1.setTitle("Trips" + seconds+".json");
//            long r1 = dm.enqueue(request1);
//
//            DownloadManager.Request request2 = new DownloadManager.Request(Uri.parse(AtApiManager.REQUEST_CALENDER_ALL_URL));
//            request2.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE); //todo make hidden
//            request2.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//            request2.addRequestHeader(AtApiManager.SUBKEYLABEL, AtApiManager.KEY);
//            request2.setTitle("Calender"+ seconds+".json");
//            long r2 = dm.enqueue(request2);
//
//            DownloadManager.Request request3 = new DownloadManager.Request(Uri.parse(AtApiManager.REQUEST_ROUTES_ALL_URL));
//            request3.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE); //todo make hidden
//            request3.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
//            request3.addRequestHeader(AtApiManager.SUBKEYLABEL, AtApiManager.KEY);
//            request3.setTitle("Routes"+ seconds+".json");
//            long r3 = dm.enqueue(request3);
//
//            //todo listen for completion and start adding to db
//            //todo once all 3 done release control
//            //todo addmethod to stop download
//
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);



            AtApiManager APIAccess = new AtApiManager();
            APIResponseHandler responseHandler = new APIResponseHandler(findViewById(R.id.navigation_view), c, counter);
            APIAccess.getAllTrips(c, responseHandler);                //todo consider using download manager as these files are large and
            APIAccess.getAllRoutes(c, responseHandler);
            APIAccess.getAllCalenders(c, responseHandler);
            //todo handle failure better
        }
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

    private void RemoveUserControl() {
        progressBar.setVisibility(View.VISIBLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    public static void restoreUserControl(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.GONE);
    }

    public class CompletedCounter{
        int count = 0;
        int done;
        public CompletedCounter(int x){
            done = x;
        }

        public synchronized boolean CanRestoreUserControl(){
            count++;
            return (count == done);
        }
    }
}