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

public class CalenderProvider extends ContentProvider {

    private static final String AUTHORITY = "com.example.ian.MobileTransportApplication.CalenderProvider";
    private static final String BASE_PATH = "CalenderData";
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
//        test.put(DBHelper.CALENDER_SERVICE_ID, "12881052794-20170310094731_v52.8");
//        test.put(DBHelper.CALENDER_MONDAY, 1);
//        test.put(DBHelper.CALENDER__TUESDAY, 1);
//        test.put(DBHelper.CALENDER_WEDNESDAY,1);
//        test.put(DBHelper.CALENDER_THURSDAY, 1);
//        test.put(DBHelper.CALENDER_FRIDAY, 1);
//        test.put(DBHelper.CALENDER_SATURDAY, 0);
//        test.put(DBHelper.CALENDER_SUNDAY, 0);
//        test.put(DBHelper.CALENDER_START, "2017-03-12T00:00:00.000Z");
//        test.put(DBHelper.CALENDER_END, "2017-03-17T00:00:00.000Z");
//        insert(CONTENT_URI, test);

        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return ATData.query(DBHelper.CALENDER_TABLE, DBHelper.CALENDER_COLUMNS, s, null,null,null, DBHelper.CALENDER_SERVICE_ID+ " DESC");
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        long id = ATData.insert(DBHelper.CALENDER_TABLE, null, contentValues);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return ATData.delete(DBHelper.CALENDER_TABLE, s, strings);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return ATData.update(DBHelper.CALENDER_TABLE, contentValues, s, strings);
    }

    public boolean containsKeyValue(String value) {
        boolean result = false;
        String SQL_CHECK_DB = "SELECT * FROM " + DBHelper.CALENDER_TABLE
                + " WHERE " + DBHelper.CALENDER_SERVICE_ID + " = '" + value
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
