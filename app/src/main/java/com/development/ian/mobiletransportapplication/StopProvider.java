package com.development.ian.mobiletransportapplication;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Ian on 2/22/2017.
 */

public class StopProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.ian.MobileTransportApplication.StopProvider";
    private static final String BASE_PATH = "StopData";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    private static SQLiteDatabase ATData;


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
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return ATData.query(DBHelper.STOP_TABLE, DBHelper.STOPS_COLUMNS, s,null, null,null, DBHelper.STOPS_ID + " DESC");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {return null;}

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long id = ATData.insert(DBHelper.STOP_TABLE, null, contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return ATData.delete(DBHelper.STOP_TABLE, s, strings);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return ATData.update(DBHelper.STOP_TABLE, contentValues, s, strings);
    }
}
