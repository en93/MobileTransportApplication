package com.development.ian.mobiletransportapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ian on 12/5/2016.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    final static private String DATABASE_NAME ="AucklandTransport.db";
    final static private int DATBASE_VERSION = 9;


//    public static final String STOPS_TABLE = "stops";
//    public static final String STOPS_ID = "_id";
//    public static final String STOPS_LAT = "lat";
//    public static final String STOPS_LON = "long";
//    public static final String STOPS_NAME = "name";
//    public static final String[] STOPS_COLUMNS = {STOPS_ID, STOPS_NAME, STOPS_LAT, STOPS_LAT};
//    public static final String STOPS_CREATE = "CREATE TABLE "+ STOPS_TABLE + "(" + STOPS_ID + " INTEGER PRIMARY KEY, "
//            + STOPS_NAME + " varchar(255)," + STOPS_LAT+" float,"+STOPS_LON+" float)";



    //For setting up table of bus stops
    public static final String STOP_TABLE= "stop";
    public static final String STOP_NAME = "stop_name";
    public static final String STOP_ID = "_id";
    public static final String STOP_LAT = "stop_lat";
    public static final String STOP_LON = "stop_lon";
    public static final String STOP_CODE = "stop_code";
    public static final String STOP_PARENT = "parent_station";
    public static final String[] STOP_COLUMNS = {STOP_NAME, STOP_ID, STOP_LAT, STOP_LON, STOP_CODE, STOP_PARENT};
    public static final String STOP_CREATE = "CREATE TABLE " + STOP_TABLE +"(" + STOP_NAME + " VARCHAR(255), "
            + STOP_ID + " INTEGER PRIMARY KEY, " + STOP_LAT + " FLOAT, " + STOP_LON + " FLOAT, "
            + STOP_CODE + " INTEGER, " + STOP_PARENT + " INTEGER)";

    //For setting up arrival values
    public static final String ARRIVAL_TABLE = "arrival";
    public static final String ARRIVAL_STOP_ID = "stop_id";
    public static final String ARRIVAL_TRIP_ID = "trip_id";
    public static final String ARRIVAL_TIME = "arrival_time";
    public static final String ARRIVAL_TIME_SECONDS = "arrival_time_seconds";
    public static final String[] ARRIVAL_COLUMNS = {ARRIVAL_STOP_ID, ARRIVAL_TRIP_ID, ARRIVAL_TIME, ARRIVAL_TIME_SECONDS};
    public static final String ARRIVAL_CREATE = "CREATE TABLE " + ARRIVAL_TABLE + "(" + ARRIVAL_STOP_ID
            + " Integer, " + ARRIVAL_TRIP_ID + " VARCHAR(255), " + ARRIVAL_TIME + " VARCHAR(255), "
            + ARRIVAL_TIME_SECONDS + " INTEGER, PRIMARY KEY (" + ARRIVAL_STOP_ID+", " + ARRIVAL_TRIP_ID+"))";

    //For setting up table of trip values
    public static final String TRIP_TABLE = "trip";
    public static final String TRIP_ROUTE_ID = "route_id";
    public static final String TRIP_SERVICE_ID = "service_id";
    public static final String TRIP_ID = "trip_id";
    public static final String TRIP_HEADSIGN = "trip_headsign";
    public static final String TRIP_DIRECTION =  "direction_id";
    public static final String[] TRIP_COLUMNS = {TRIP_ROUTE_ID, TRIP_SERVICE_ID, TRIP_ID, TRIP_HEADSIGN, TRIP_DIRECTION};
    public static final String TRIP_CREATE = " CREATE TABLE " + TRIP_TABLE + "(" + TRIP_ROUTE_ID
            + " VARCHAR(255), " + TRIP_SERVICE_ID + " VARCHAR(255), " + TRIP_ID + " VARCHAR(255) PRIMARY KEY, " + TRIP_HEADSIGN
            + " VARCHAR(255), " + TRIP_DIRECTION + " INTEGER)";

    //For setting up table of route values
    public static final String ROUTE_TABLE = "route";
    public static final String ROUTE_ID = "route_id";
    public static final String ROUTE_AGENCY_ID = "agency_id";
    public static final String ROUTE_SHORT_NAME = "route_short_name";
    public static final String ROUTE_LONG_NAME = "route_long_name";
    public static final String[] ROUTE_COLUMNS = {ROUTE_ID, ROUTE_AGENCY_ID, ROUTE_SHORT_NAME, ROUTE_LONG_NAME };
    public static final String ROUTE_CREATE = "CREATE TABLE " + ROUTE_TABLE + "(" + ROUTE_ID + " VARCHAR(255) PRIMARY KEY, "
            + ROUTE_AGENCY_ID + " VARCHAR(255), " + ROUTE_SHORT_NAME + " VARCHAR(255), " + ROUTE_LONG_NAME
            + " VARCHAR(255))";

    //For setting up calender dates
    public static final String CALENDER_TABLE = "calender";
    public static final String CALENDER_SERVICE_ID = "service_id";
    public static final String CALENDER_MONDAY = "monday";
    public static final String CALENDER__TUESDAY = "tuesday";
    public static final String CALENDER_WEDNESDAY = "wednesday";
    public static final String CALENDER_THURSDAY = "thursday";
    public static final String CALENDER_FRIDAY = "friday";
    public static final String CALENDER_SATURDAY = "saturday";
    public static final String CALENDER_SUNDAY = "sunday";
    public static final String CALENDER_START = "start_date";
    public static final String CALENDER_END = "end_date";
    public static final String[] CALENDER_COLUMNS = {CALENDER_SERVICE_ID, CALENDER_MONDAY,
            CALENDER__TUESDAY, CALENDER_WEDNESDAY, CALENDER_THURSDAY, CALENDER_FRIDAY,
            CALENDER_SATURDAY, CALENDER_SUNDAY, CALENDER_START, CALENDER_END};
    public static final String CALENDER_CREATE = "CREATE TABLE " + CALENDER_TABLE + "(" +
            CALENDER_SERVICE_ID + " VARCHAR(255) PRIMARY KEY, " + CALENDER_MONDAY + " INTEGER, "
            + CALENDER__TUESDAY + " INTEGER, " + CALENDER_WEDNESDAY+ " INTEGER, "
            + CALENDER_THURSDAY+ " INTEGER, " + CALENDER_FRIDAY+ " INTEGER, "
            + CALENDER_SATURDAY+ " INTEGER, " + CALENDER_SUNDAY+ " INTEGER, "
            + CALENDER_START + " VARCHAR(255), " + CALENDER_END + " VARCHAR(255))"; //todo use compouund primary key instead

    //For setting up version table
    public static final String VERSION_TABLE = "version";
    public static final String VERSION_ID ="version";
    public static final String VERSION_START = "startdate";
    public static final String VERSON_END = "enddate";
    public static final String[] VERSION_COLUMNS = {VERSION_ID, VERSION_START, VERSON_END};
    public static final String VERSION_CREATE = "CREATE TABLE " + VERSION_TABLE + "("
            + VERSION_ID + " VARCHAR(255) PRIMARY KEY, " + VERSION_START + " VARCHAR(255), "
            + VERSON_END + " VARCHAR(255))";

    //For maintaining saved stations
    public static final String SAVED_STOP_TABLE = "saved_stops_table";
    public static final String SAVED_STOP_ID = "stop_id";
    public static final String[] SAVED_COLUMNS = {SAVED_STOP_ID};
    public static final String SAVED_STOP_CREATE = "CREATE TABLE " + SAVED_STOP_TABLE + "("
            + SAVED_STOP_ID + " INTEGER PRIMARY KEY)";

//    public static final String GLOBALS_TABLE = "global";
//    public static final String GLOBALS_HAS_SETUP= "has_setup";
//    public static final String[] GLOBALS_COLUMNS = {GLOBALS_HAS_SETUP};
//    public static final String GLOBALS_CREATE = "CREATE TABLE " + GLOBALS_TABLE + "(" +

    private DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATBASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STOP_CREATE);
        db.execSQL(TRIP_CREATE);
        db.execSQL(ROUTE_CREATE);
        db.execSQL(CALENDER_CREATE);
        db.execSQL(VERSION_CREATE);
        db.execSQL(SAVED_STOP_CREATE);
        db.execSQL(ARRIVAL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //todo have clear all rather than specifying manually

        //remove all old names
        db.execSQL("DROP TABLE IF EXISTS " + "stops"); //todo remove from app
        db.execSQL("DROP TABLE IF EXISTS " + "stop"); //todo remove from app
        db.execSQL("DROP TABLE IF EXISTS " + "arrival"); //todo remove from app
        db.execSQL("DROP TABLE IF EXISTS " + "trip"); //todo remove from app
        db.execSQL("DROP TABLE IF EXISTS " + "route_table"); //todo remove from app
        db.execSQL("DROP TABLE IF EXISTS " + "calender_table"); //todo remove from app
        db.execSQL("DROP TABLE IF EXISTS " + "version_table"); //todo remove from app
        db.execSQL("DROP TABLE IF EXISTS " + "saved_stops_table"); //todo remove from app



        db.execSQL("DROP TABLE IF EXISTS " + STOP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TRIP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ROUTE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CALENDER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + VERSION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + SAVED_STOP_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ARRIVAL_TABLE);
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
