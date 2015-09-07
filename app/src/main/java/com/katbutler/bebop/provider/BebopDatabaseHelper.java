package com.katbutler.bebop.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kat on 15-09-07.
 */
public class BebopDatabaseHelper extends SQLiteOpenHelper{

    /*
     * Original bebop database
     */
    private static final int VERSION_1 = 1;

    public static final String DATABASE_NAME = "bebop.db";
    public static final String ALARMS_TABLE_NAME = "alarms";
    public static final String INSTANCES_TABLE_NAME = "alarm_instances";
    public static final String RINGTONES_TABLE_NAME = "ringtones";

    public BebopDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createAlarmsTable(db);
        createAlarmInstancesTable(db);
        createRingtonesTable(db);
    }

    private void createRingtonesTable(SQLiteDatabase db) {

    }

    private void createAlarmInstancesTable(SQLiteDatabase db) {

    }

    private void createAlarmsTable(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
