//package com.development.ian.mobiletransportapplication.TransportContentProviders;
//
//import android.content.ContentProvider;
//import android.content.ContentValues;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.net.Uri;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//
//import com.development.ian.mobiletransportapplication.DBHelper;
//
///**
// * Created by Ian on 3/18/2017.
// */
//
//public class SavedStopProvider extends ContentProvider {
//
//    private static final String AUTHORITY = "com.example.ian.MobileTransportApplication.SavedStopProvider";
//    private static final String BASE_PATH = "SavedStopData";
//    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );
//
//    private static SQLiteDatabase ATData;
//
//
//    @Override
//    public boolean onCreate() {
//        //ATData =
//        DBHelper helper = DBHelper.getInstance(getContext().getApplicationContext());
//        if(ATData == null) {
//            ATData = helper.getWritableDatabase();
//        }
//        return true;
//    }
//
//
//    @Nullable
//    @Override
//    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
//        return ATData.query(DBHelper.SAVED_STOP_TABLE, DBHelper.SAVED_COLUMNS, s, null,null,null, DBHelper.SAVED_STOP_ID + " DESC");
//    }
//
//    @Nullable
//    @Override
//    public String getType(@NonNull Uri uri) {
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
//        long id = ATData.insert(DBHelper.SAVED_STOP_TABLE, null, contentValues);
//        return Uri.parse(BASE_PATH + "/" + id);
//    }
//
//    @Override
//    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
//        return ATData.delete(DBHelper.SAVED_STOP_TABLE, s, strings);
//    }
//
//    @Override
//    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
//        return ATData.update(DBHelper.SAVED_STOP_TABLE, contentValues, s, strings);
//    }
//}
