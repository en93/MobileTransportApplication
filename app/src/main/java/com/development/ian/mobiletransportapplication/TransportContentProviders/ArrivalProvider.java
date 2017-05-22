package com.development.ian.mobiletransportapplication.TransportContentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.development.ian.mobiletransportapplication.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Ian on 3/18/2017.
 */

public class ArrivalProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.ian.MobileTransportApplication.ArrivalProvider";
    private static final String BASE_PATH = "ArrivalData";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    private static SQLiteDatabase ATData;
//    public static ArrayList<Integer> requestedButNotCompleted = new ArrayList<Integer>();

//    private static String SQL_GET_ARRIVALS =
//            "SELECT 0 AS _id, * " +
//            "FROM arrival a, trip t, calender c " +
//            "WHERE a.trip_id = t.trip_id AND t.service_id = c.service_id " + //join tables
//            "AND a.arrival_time_seconds > " + //Calculate time since start of day
//                    "(strftime('%s','now', 'localtime') - strftime('%s', 'now', 'localtime', 'start of day')) " +
//            "AND a.%s = 1 " +
//            "AND a.stop_id = ? " +
//            "AND strftime('%s','now', 'localtime') > strftime('%s', c.start_date, 'localtime') " + //Ensure after start date
//            "AND strftime('%s','now', 'localtime') < strftime('%s', c.end_date, 'localtime') " + //Ensure before end date
//            "ORDER BY a.arrival_time_seconds " +
//            "LIMIT 15;"
//            ;


    @Override
    public boolean onCreate() {
        //ATData =
        DBHelper helper = DBHelper.getInstance(getContext().getApplicationContext());
        if(ATData == null) {
            ATData = helper.getWritableDatabase();
        }
        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        Calendar calendar = Calendar.getInstance(); //get current date and time
        int numRows = 15;
        int rowsSoFar=0;
        ArrayList<Cursor> cursorArrayList = new ArrayList<>();
        Cursor cursor = getCursor(calendar, uri, strings, s, strings1, s1, numRows);
        rowsSoFar += cursor.getCount();
        cursorArrayList.add(cursor);
        while(rowsSoFar<numRows){
            calendar.add(Calendar.DAY_OF_WEEK, 1);
            int remaining = numRows - cursor.getCount();
            cursor = getCursor(calendar, uri, strings, s, strings1, s1, remaining);
            if(cursor.getCount() == 0){
                break;
            }
            rowsSoFar += cursor.getCount();
            cursorArrayList.add(cursor);
        }
        Cursor[] cursorArray = new Cursor[cursorArrayList.size()];
        cursorArray = cursorArrayList.toArray(cursorArray);
        MergeCursor mergeCursor = new MergeCursor(cursorArray);
        return mergeCursor; //todo test once list is more readable

        //        return ATData.query(DBHelper.ARRIVAL_TABLE, DBHelper.ARRIVAL_COLUMNS, s, null,null,null, DBHelper.ARRIVAL_TIME_SECONDS + " DESC");
//        String sqlFormatted = String.format(SQL_GET_ARRIVALS, "monday");

//        return ATData.rawQuery(SQL_GET_ARRIVALS, new String[] {String.format("%s.%s", DBHelper.CALENDER_TABLE, DBHelper.CALENDER_SUNDAY)});

    }

    private Cursor getCursor(Calendar calendar, @NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1, int numRows) {
        String dayOfWeek="";
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY: dayOfWeek = DBHelper.CALENDER_MONDAY;
                break;
            case Calendar.TUESDAY: dayOfWeek = DBHelper.CALENDER__TUESDAY;
                break;
            case Calendar.WEDNESDAY: dayOfWeek = DBHelper.CALENDER_WEDNESDAY;
                break;
            case Calendar.THURSDAY: dayOfWeek = DBHelper.CALENDER_THURSDAY;
                break;
            case Calendar.FRIDAY: dayOfWeek = DBHelper.CALENDER_FRIDAY;
                break;
            case Calendar.SATURDAY: dayOfWeek = DBHelper.CALENDER_SATURDAY;
                break;
            case Calendar.SUNDAY: dayOfWeek = DBHelper.CALENDER_SUNDAY;
                break;
        }

        String sql_get_arrivals = //todo filter for stopID
                "SELECT 0 AS _id, * " + //todo select only what I need
                        "FROM arrival a, trip t, calender c, route r " + //todo remove hardcoded table names and fields
                        "WHERE a.trip_id = t.trip_id AND t.service_id = c.service_id AND t.route_id = r.route_id " + //join tables
                        "AND a.arrival_time_seconds > " + //Calculate time since start of day
                        "(strftime('%s','now', 'localtime') - strftime('%s', 'now', 'localtime', 'start of day')) " +
                        "AND c." + dayOfWeek +" = 1 " +
                        "AND a.stop_id = ? " + //todo set in selectionargs
                        "AND strftime('%s','now', 'localtime') > strftime('%s', c.start_date, 'localtime') " + //Ensure after start date
                        "AND strftime('%s','now', 'localtime') < strftime('%s', c.end_date, 'localtime') " + //Ensure before end date
                        "ORDER BY a.arrival_time_seconds " +
                        "LIMIT " + numRows + ";"
                ;

        Cursor test =ATData.rawQuery(sql_get_arrivals, new String[]{"7230"});
        return test;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = ATData.insert(DBHelper.ARRIVAL_TABLE, null, contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return ATData.delete(DBHelper.ARRIVAL_TABLE, s, strings);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return ATData.update(DBHelper.ARRIVAL_TABLE, contentValues, s, strings);
    }

    public boolean containsKeyValue(String s) {
        boolean result = false;
        String SQL_CHECK_DB = "SELECT * FROM " + DBHelper.ARRIVAL_TABLE
                + " WHERE " + DBHelper.ARRIVAL_STOP_ID + " = " + s
                + " LIMIT 1";
//        if(requestedButNotCompleted.contains(Integer.parseInt(s))){
//            result = true;
//        }
        if(ATData.rawQuery(SQL_CHECK_DB, null).getCount()>0){
            result = true;
        }
        return result;
    }
}
