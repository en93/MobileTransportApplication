package com.development.ian.mobiletransportapplication;

import android.content.Context;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by Ian on 21-May-17.
 */

public class ArrivalCursorAdapter extends CursorAdapter {

    public ArrivalCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.arrival_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String routeNumber = cursor.getString(cursor.getColumnIndex(DBHelper.ROUTE_SHORT_NAME));
        String arrivalTime = cursor.getString(cursor.getColumnIndex(DBHelper.ARRIVAL_TIME));

//        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("hh:mm a");
//        Date date=new Date();
//        try {
//            date = sdf.parse(arrivalTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


        TextView tvRouteNumber = (TextView) view.findViewById(R.id.routeNum);
        TextView tvArrivalTime = (TextView) view.findViewById(R.id.arrivalTime);

        tvRouteNumber.setText(routeNumber);
        tvArrivalTime.setText(arrivalTime);
    }
}
