package com.development.ian.mobiletransportapplication.TransportContentProviders;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.development.ian.mobiletransportapplication.DBHelper;

import java.util.ArrayList;

/**
 * Created by Ian on 3/18/2017.
 */

public class ArrivalProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.ian.MobileTransportApplication.ArrivalProvider";
    private static final String BASE_PATH = "ArrivalData";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    private static SQLiteDatabase ATData;
//    public static ArrayList<Integer> requestedButNotCompleted = new ArrayList<Integer>();

    private static String SQL_GET_ARRIVALS = //todo filter for stopID
            "SELECT 0 AS _id, * " + //todo select only what I need
            "FROM arrival a, trip t, calender c " +
            "WHERE a.trip_id = t.trip_id AND t.service_id = c.service_id " + //join tables
            "AND a.arrival_time_seconds > " + //Calculate time since start of day
                    "(strftime('%s','now', 'localtime') - strftime('%s', 'now', 'localtime', 'start of day')) " +
            "AND c.sunday = 1 " + //todo set coulumn in selection args
            "AND a.stop_id = 7230 " + //todo set in selectionargs
            "AND strftime('%s','now', 'localtime') > strftime('%s', c.start_date, 'localtime') " + //Ensure after start date
            "AND strftime('%s','now', 'localtime') < strftime('%s', c.end_date, 'localtime') " + //Ensure before end date
            "ORDER BY a.arrival_time_seconds " +
            "LIMIT 15;"
            ;


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
//        return ATData.query(DBHelper.ARRIVAL_TABLE, DBHelper.ARRIVAL_COLUMNS, s, null,null,null, DBHelper.ARRIVAL_TIME_SECONDS + " DESC");
        Cursor test =ATData.rawQuery(SQL_GET_ARRIVALS, null/*new String[] {String.format("%s.%s", DBHelper.CALENDER_TABLE, DBHelper.CALENDER_SUNDAY)}*/);
//        return ATData.rawQuery(SQL_GET_ARRIVALS, new String[] {String.format("%s.%s", DBHelper.CALENDER_TABLE, DBHelper.CALENDER_SUNDAY)});
        return test; //todo returns -1 count results
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
