package com.katbutler.bebop.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by kat on 15-09-07.
 */
public class BebopProvider extends ContentProvider{
    private static final String TAG = BebopProvider.class.getSimpleName();

    private BebopDatabaseHelper mDatabaseHelper;
    private static final int ALARMS = 1;
    private static final int ALARMS_ID = 2;
    private static final int INSTANCES = 3;
    private static final int INSTANCES_ID = 4;
    private static final int RINGTONES = 5;
    private static final int RINGTONES_ID = 6;

    private static final UriMatcher sURLMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURLMatcher.addURI(BebopContract.AUTHORITY, "alarms", ALARMS);
        sURLMatcher.addURI(BebopContract.AUTHORITY, "alarms/#", ALARMS_ID);
        sURLMatcher.addURI(BebopContract.AUTHORITY, "instances", INSTANCES);
        sURLMatcher.addURI(BebopContract.AUTHORITY, "instances/#", INSTANCES_ID);
        sURLMatcher.addURI(BebopContract.AUTHORITY, "ringtones", RINGTONES);
        sURLMatcher.addURI(BebopContract.AUTHORITY, "ringtones/#", RINGTONES_ID);
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new BebopDatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        int match = sURLMatcher.match(uri);
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        switch (match) {
            case ALARMS:
                qb.setTables(BebopDatabaseHelper.ALARMS_TABLE_NAME +
                        " JOIN " +
                        BebopDatabaseHelper.RINGTONES_TABLE_NAME +
                        " ON (" +
                        BebopDatabaseHelper.ALARMS_TABLE_NAME + "." + BebopContract.AlarmsColumns.RINGTONE_ID +
                        " = " +
                        BebopDatabaseHelper.RINGTONES_TABLE_NAME + "." + BebopContract.RingtonesColumns._ID +
                ")");
                break;
            case ALARMS_ID:
                qb.setTables(BebopDatabaseHelper.ALARMS_TABLE_NAME);
                qb.appendWhere(BebopContract.AlarmsColumns._ID + "=");
                qb.appendWhere(uri.getLastPathSegment());
                break;
            case INSTANCES:
                qb.setTables(BebopDatabaseHelper.INSTANCES_TABLE_NAME);
                break;
            case INSTANCES_ID:
                qb.setTables(BebopDatabaseHelper.INSTANCES_TABLE_NAME);
                qb.appendWhere(BebopContract.InstancesColumns._ID + "=");
                qb.appendWhere(uri.getLastPathSegment());
                break;
            case RINGTONES:
                qb.setTables(BebopDatabaseHelper.RINGTONES_TABLE_NAME);
                break;
            case RINGTONES_ID:
                qb.setTables(BebopDatabaseHelper.RINGTONES_TABLE_NAME);
                qb.appendWhere(BebopContract.RingtonesColumns._ID + "=");
                qb.appendWhere(uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri");
        }

        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();
        Cursor ret = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        if(ret == null) {
            Log.e(TAG, "Alarms.query failed");
        } else {
            ret.setNotificationUri(getContext().getContentResolver(), uri);
        }

        return ret;
    }

    @Override
    public String getType(Uri uri) {
        int match = sURLMatcher.match(uri);
        switch (match) {
            case ALARMS:
                return "vnd.android.cursor.dir/alarms";
            case ALARMS_ID:
                return "vnd.android.cursor.item/alarms";
            case INSTANCES:
                return "vnd.android.cursor.dir/instances";
            case INSTANCES_ID:
                return "vnd.android.cursor.item/instances";
            case RINGTONES:
                return "vnd.android.cursor.dir/ringtones";
            case RINGTONES_ID:
                return "vnd.android.cursor.item/ringtones";
            default:
                throw new IllegalArgumentException("Unknown URL");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        switch (sURLMatcher.match(uri)) {
            case ALARMS:
                rowId = db.insert(BebopDatabaseHelper.ALARMS_TABLE_NAME, null, values); //mDatabaseHelper.fixAlarmInsert(values);
                break;
            case INSTANCES:
                rowId = db.insert(BebopDatabaseHelper.INSTANCES_TABLE_NAME, null, values);
                break;
            case RINGTONES:
                rowId = db.insert(BebopDatabaseHelper.RINGTONES_TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Cannot insert from URL: " + uri);
        }

        Uri uriResult = ContentUris.withAppendedId(BebopContract.AlarmsColumns.CONTENT_URI, rowId);
        getContext().getContentResolver().notifyChange(uriResult, null);
        return uriResult;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        String alarmId;
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        switch (sURLMatcher.match(uri)) {
            case ALARMS_ID:
                alarmId = uri.getLastPathSegment();
                count = db.update(BebopDatabaseHelper.ALARMS_TABLE_NAME, values,
                        BebopContract.AlarmsColumns._ID + "=" + alarmId,
                        null);
                break;
            case INSTANCES_ID:
                alarmId = uri.getLastPathSegment();
                count = db.update(BebopDatabaseHelper.INSTANCES_TABLE_NAME, values,
                        BebopContract.InstancesColumns._ID + "=" + alarmId,
                        null);
                break;
            case RINGTONES_ID:
                alarmId = uri.getLastPathSegment();
                count = db.update(BebopDatabaseHelper.RINGTONES_TABLE_NAME, values,
                        BebopContract.RingtonesColumns._ID + "=" + alarmId,
                        null);
                break;
            default: {
                throw new UnsupportedOperationException(
                        "Cannot update URL: " + uri);
            }
        }
        Log.v(TAG, "*** notifyChange() id: " + alarmId + " url " + uri);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
