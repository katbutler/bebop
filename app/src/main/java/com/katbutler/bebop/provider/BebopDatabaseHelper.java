package com.katbutler.bebop.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.katbutler.bebop.utils.BebopLog;

/**
 * Created by kat on 15-09-07.
 */
public class BebopDatabaseHelper extends SQLiteOpenHelper{
    private static final String TAG = BebopDatabaseHelper.class.getSimpleName();

    /*
     * Original bebop database
     */
    private static final int VERSION_1 = 1;

    public static final String DATABASE_NAME = "bebop.db";
    public static final String ALARMS_TABLE_NAME = "alarms";
    public static final String INSTANCES_TABLE_NAME = "alarm_instances";
    public static final String RINGTONES_TABLE_NAME = "ringtones";


    public BebopDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createRingtonesTable(db);
        createAlarmsTable(db);
        createAlarmInstancesTable(db);
    }

    private void createRingtonesTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + RINGTONES_TABLE_NAME + " (" +
                BebopContract.RingtonesColumns._ID + " INTEGER PRIMARY KEY," +
                BebopContract.RingtonesColumns.REMOTE_OBJECT_KEY + " TEXT NOT NULL," +
                BebopContract.RingtonesColumns.MUSIC_SERVICE + " INTEGER NOT NULL" +
                ");");

        BebopLog.i(TAG, "Ringtones table created");
    }

    private void createAlarmInstancesTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + INSTANCES_TABLE_NAME + " (" +
                BebopContract.InstancesColumns._ID + " INTEGER PRIMARY KEY," +
                BebopContract.InstancesColumns.YEAR + " INTEGER NOT NULL, " +
                BebopContract.InstancesColumns.MONTH + " INTEGER NOT NULL, " +
                BebopContract.InstancesColumns.DAY + " INTEGER NOT NULL, " +
                BebopContract.InstancesColumns.HOUR + " INTEGER NOT NULL, " +
                BebopContract.InstancesColumns.MINUTES + " INTEGER NOT NULL, " +
                BebopContract.InstancesColumns.VIBRATE + " INTEGER NOT NULL, " +
                BebopContract.InstancesColumns.LABEL + " TEXT NOT NULL, " +
                BebopContract.AlarmsColumns.RINGTONE_ID + " INTEGER REFERENCES " +
                RINGTONES_TABLE_NAME + "(" + BebopContract.RingtonesColumns._ID + "), " +
                BebopContract.InstancesColumns.ALARM_STATE + " INTEGER NOT NULL, " +
                BebopContract.InstancesColumns.ALARM_ID + " INTEGER REFERENCES " +
                ALARMS_TABLE_NAME + "(" + BebopContract.AlarmsColumns._ID + ") " +
                "ON UPDATE CASCADE ON DELETE CASCADE" +
                ");");

        BebopLog.i(TAG, "Instance table created");
    }

    private void createAlarmsTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + ALARMS_TABLE_NAME + " (" +
                BebopContract.AlarmsColumns._ID + " INTEGER PRIMARY KEY," +
                BebopContract.AlarmsColumns.HOUR + " INTEGER NOT NULL, " +
                BebopContract.AlarmsColumns.MINUTES + " INTEGER NOT NULL, " +
                BebopContract.AlarmsColumns.DAYS_OF_WEEK + " INTEGER NOT NULL, " +
                BebopContract.AlarmsColumns.ENABLED + " INTEGER NOT NULL, " +
                BebopContract.AlarmsColumns.VIBRATE + " INTEGER NOT NULL, " +
                BebopContract.AlarmsColumns.LABEL + " TEXT NOT NULL, " +
                BebopContract.AlarmsColumns.RINGTONE_ID + " INTEGER REFERENCES " +
                    RINGTONES_TABLE_NAME + "(" + BebopContract.RingtonesColumns._ID + ") " +
                    "ON UPDATE CASCADE ON DELETE CASCADE" +
                ");");

        BebopLog.i(TAG, "Alarms Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long fixAlarmInsert(ContentValues values) {
        // Why are we doing this? Is this not a programming bug if we try to
        // insert an already used id?
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        long rowId = -1;
        try {
            // Check if we are trying to re-use an existing id.
            Object value = values.get(BebopContract.AlarmsColumns._ID);
            if (value != null) {
                long id = (Long) value;
                if (id > -1) {
                    final Cursor cursor = db.query(ALARMS_TABLE_NAME,
                            new String[]{BebopContract.AlarmsColumns._ID},
                            BebopContract.AlarmsColumns._ID + " = ?",
                            new String[]{id + ""}, null, null, null);
                    if (cursor.moveToFirst()) {
                        // Record exists. Remove the id so sqlite can generate a new one.
                        values.putNull(BebopContract.AlarmsColumns._ID);
                    }
                }
            }

            rowId = db.insert(ALARMS_TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        if (rowId < 0) {
            throw new SQLException("Failed to insert row");
        }
        BebopLog.v(TAG, "Added alarm rowId = " + rowId);

        return rowId;
    }

}
