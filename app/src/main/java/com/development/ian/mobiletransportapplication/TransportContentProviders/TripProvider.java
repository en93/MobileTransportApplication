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

public class TripProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.ian.MobileTransportApplication.TripProvider";
    private static final String BASE_PATH = "TripData";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    private static SQLiteDatabase ATData;

    public static ArrayList<String> requestedButNotCompleted = new ArrayList<String>();



    @Override
    public boolean onCreate() {
        //ATData =
        DBHelper helper = DBHelper.getInstance(getContext().getApplicationContext());
        if(ATData == null) {
            ATData = helper.getWritableDatabase();
        }
//        ATData.rawQuery(
//                "INSERT INTO trip(route_id, service_id, trip_id, trip_headsign, direction_id) VALUES('88103-20170310094731_v52.8', '12881052789-20170310094731_v52.8',
// '12881052789-20170310094731_v52.8', 'Newmarket' 0)",null);
//        ContentValues test = new ContentValues();
//        test.put(DBHelper.TRIP_ROUTE_ID, "88103-20170310094731_v52.8");
//        test.put(DBHelper.TRIP_SERVICE_ID, "12881052789-20170310094731_v52.8");
//        test.put(DBHelper.TRIP_ID, "12881052789-20170310094731_v52.8" );
//        test.put(DBHelper.TRIP_HEADSIGN, "Newmarket");
//        test.put(DBHelper.TRIP_DIRECTION, 0);
//        insert(CONTENT_URI, test);

        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return ATData.query(DBHelper.TRIP_TABLE, DBHelper.TRIP_COLUMNS, s, null,null,null, DBHelper.TRIP_ID+ " DESC");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = ATData.insert(DBHelper.TRIP_TABLE, null, contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return ATData.delete(DBHelper.TRIP_TABLE, s, strings);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return ATData.update(DBHelper.TRIP_TABLE, contentValues, s, strings);
    }

    public boolean containsKeyValue(String value) {
        boolean result = false;
        String SQL_CHECK_DB = "SELECT * FROM " + DBHelper.TRIP_TABLE
                + " WHERE " + DBHelper.TRIP_ID + " = '" + value
                + "' LIMIT 1";
        if(requestedButNotCompleted.contains(value)){
            result = true;
        }
        else if(ATData.rawQuery(SQL_CHECK_DB, null).getCount()>0){
            result = true;
        }
        return result;

    }

    public boolean isEmpty(){
        boolean result = false;
        String SQL_CHECK_EMPTY = "SELECT * FROM " + DBHelper.TRIP_TABLE + " LIMIT 1";
        if(ATData.rawQuery(SQL_CHECK_EMPTY, null).getCount() == 0){
            result = true;
        }
        return result;
    }
}
