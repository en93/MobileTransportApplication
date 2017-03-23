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

public class RouteProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.ian.MobileTransportApplication.RouteProvider";
    private static final String BASE_PATH = "RouteData";
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

//        ContentValues test = new ContentValues();
//        test.put(DBHelper.ROUTE_ID, "32102-20170310094731_v52.8");
//        test.put(DBHelper.ROUTE_AGENCY_ID, "NZBML");
//        test.put(DBHelper.ROUTE_SHORT_NAME, "321");
//        test.put(DBHelper.ROUTE_LONG_NAME, "Britomart to Middlemore station via Greenlane");
//        insert(CONTENT_URI, test);

        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return ATData.query(DBHelper.ROUTE_TABLE, DBHelper.ROUTE_COLUMNS, s, null,null,null, DBHelper.ROUTE_ID+ " DESC");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = ATData.insert(DBHelper.ROUTE_TABLE, null, contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return ATData.delete(DBHelper.ROUTE_TABLE, s, strings);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return ATData.update(DBHelper.ROUTE_TABLE, contentValues, s, strings);
    }

    public boolean containsKeyValue(String value) {
        boolean result = false;
        String SQL_CHECK_DB = "SELECT * FROM " + DBHelper.ROUTE_TABLE
                + " WHERE " + DBHelper.ROUTE_ID + " = '" + value
                + "' LIMIT 1";
        if(requestedButNotCompleted.contains(value)){
            result = true;
        }
        else if(ATData.rawQuery(SQL_CHECK_DB, null).getCount()>0){
            result = true;
        }
        return result;

    }
}
