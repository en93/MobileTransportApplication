package com.development.ian.mobiletransportapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import java.util.ArrayList;


public class AddNewStopActivity extends AppCompatActivity {

    private AtApiManager APIAccess = AtApiManager.getInstance();

    private static Window window ;
    private static ProgressBar progressBar;

    public static APIResponseHandler responseHandler;

    //TODO saying stop is invalid but is still adding successfully?


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                 //todo if a stop already has a matching primary key replace it with the new stop
        setContentView(R.layout.activity_add_new_stop);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        window = getWindow();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Button addStop = (Button) findViewById(R.id.add_stop_button);
        final Button addSample = (Button) findViewById(R.id.addSample);
        final EditText editIdText = (EditText) findViewById(R.id.editIdText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        responseHandler = new APIResponseHandler(findViewById(R.id.add_new_layout), getApplicationContext());
        addStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                RemoveUserControl();
                //todo check there is not already the values in db
                APIAccess.GetStopById(editIdText.getText().toString(), getApplicationContext(),responseHandler);

            }
        });

        addSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveUserControl();
                ArrayList<Integer> list = GetSampleStops();
                for (Integer b : list) {
                    APIAccess.GetStopById(b.toString(), getApplicationContext(), responseHandler);
                }
            }
        });
    }

    private void RemoveUserControl() {
        progressBar.setVisibility(View.VISIBLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    @Override
    public void onBackPressed() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if(progressBar.getVisibility()==View.VISIBLE){
            progressBar.setVisibility(View.GONE);
            AtApiManager.CancelStopRequests();

        }else {
            super.onBackPressed();
        }
    }

    private ArrayList<Integer> GetSampleStops() {
        ArrayList<Integer> samples = new ArrayList<Integer>();
        samples.add(7230);
        samples.add(7231);
        samples.add(7232);
        return samples;
}

    @Override
    public void onDestroy(){
        AtApiManager.CancelStopRequests();
        super.onDestroy();
    }

    public static void restoreUserControl(){
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        progressBar.setVisibility(View.GONE);
    }
}
