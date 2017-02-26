package com.development.ian.mobiletransportapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ian on 12/5/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    final static private String DATABASE_NAME ="AucklandTransport.db";
    final static private int DATBASE_VERSION = 2;
    public static final String STOPS_TABLE = "stops";
    public static final String STOPS_ID = "_id";
    public static final String STOPS_LAT = "lat";
    public static final String STOPS_LON = "long";
    public static final String STOPS_NAME = "name";
    public static final String[] ALL_COLUMNS = {STOPS_ID, STOPS_NAME, STOPS_LAT, STOPS_LAT};
    public static final String STOPS_CREATE = "CREATE TABLE "+ STOPS_TABLE + "(" + STOPS_ID + " INTEGER PRIMARY KEY, " + STOPS_NAME + " varchar(255)," + STOPS_LAT+" float,"+STOPS_LON+" float)";

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STOPS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STOPS_TABLE);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context.getApplicationContext());
        }
        return instance;
    }
}
