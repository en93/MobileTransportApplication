package com.development.ian.mobiletransportapplication;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Ian on 2/21/2017.
 */

public class StopsCursorAdapter extends CursorAdapter {

    public StopsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.stop_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvStopLocation;
        TextView tvStopID;

//        String stopNumber = "Stop: " + cursor.getString(cursor.getColumnIndex(DBHelper.STOPS_ID));
//        String stopLocation = cursor.getString(cursor.getColumnIndex(DBHelper.STOPS_NAME));
        String stopNumber = "Stop: " + cursor.getString(cursor.getColumnIndex(DBHelper.STOP_ID));
        String stopLocation = cursor.getString(cursor.getColumnIndex(DBHelper.STOP_NAME));

        tvStopID = (TextView) view.findViewById(R.id.tvStopNum);
        tvStopLocation = (TextView) view.findViewById(R.id.tvStop);

        tvStopID.setText(stopNumber);
        tvStopLocation.setText(stopLocation);

    }
}
