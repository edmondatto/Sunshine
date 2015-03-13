package com.example.android.sunshine.app;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * Created by DELL on 3/11/2015.
 */
public class TestDb extends AndroidTestCase {
    private static final String LOG_TAG = TestDb.class.getSimpleName();

    public void testCreateDb() throws Throwable{
        mContext.deleteDatabase(WeatherDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new WeatherDbHelper(this.mContext).getReadableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertDb(){
        //test data to determine whether the database works
        String testLocationSetting = "99705";
        String testCityName = "North Pole";
        double testLatitude = 64.7488;
        double testLongitude = -147.353;

        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(WeatherContract.LocationEntry.COLUMN_CITY_NAME,testCityName);
        values.put(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,testLocationSetting);
        values.put(WeatherContract.LocationEntry.COLUMN_COORD_LAT,testLatitude);
        values.put(WeatherContract.LocationEntry.COLUMN_COORD_LONG,testLongitude);

        //the return type is a long
        //lets insert the row of data into the location table
        Long locationRowId;
        locationRowId = db.insert(WeatherContract.LocationEntry.TABLE_NAME,null,values);
        assertTrue(locationRowId !=-1);
        Log.d(LOG_TAG,"New row id: "+locationRowId);

        //query the database and receive a cursor back
        //but first create an array of location string

        String[] columns = {WeatherContract.LocationEntry._ID, WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING, WeatherContract.LocationEntry.COLUMN_COORD_LONG,
                WeatherContract.LocationEntry.COLUMN_COORD_LAT, WeatherContract.LocationEntry.COLUMN_CITY_NAME};

        //query the database

        Cursor cursor = db.query(WeatherContract.LocationEntry.TABLE_NAME,columns,null,null,null,null,null);

        //test if the db is not empty, if not it moves to the first entry
        if (cursor.moveToFirst()) {
            //why do we use int and then convert to string
            int locationIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);
            String location = cursor.getString(locationIndex);
            int nameIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_CITY_NAME);
            String name = cursor.getColumnName(nameIndex);
            int latIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
            double latitude = cursor.getDouble(latIndex);
            int longIndex = cursor.getColumnIndex(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
            double longitude = cursor.getDouble(longIndex);
            //testing with the values entered
            assertEquals(testLocationSetting, location);
            assertEquals(testCityName, name);
            assertEquals(testLatitude, latitude);
            assertEquals(testLongitude, longitude);
        }
            else {
            fail("no values returned");
        }






        }





    }


