package com.katbutler.bebop.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.katbutler.bebop.musicservice.RemoteMusicServiceType;
import com.katbutler.bebop.provider.BebopContract;
import com.katbutler.bebop.utils.BebopLog;

import org.joda.time.LocalTime;

/**
 * Created by kat on 15-09-05.
 */
public class Alarm implements Comparable<Alarm>, Parcelable, BebopContract.AlarmsColumns, BebopContract.RingtonesColumns {

    /**
     * Alarms start with an invalid id when it hasn't been saved to the database.
     */
    public static final long INVALID_ID = -1;

    /**
     * The default sort order for this table
     */
    private static final String DEFAULT_SORT_ORDER =
            HOUR + ", " +
            MINUTES + " ASC" + ", " +
            ALARMS_TABLE_NAME + "." + _ID + " DESC";

    private static final String[] QUERY_COLUMNS = {
            ALARMS_TABLE_NAME + "." + _ID,
            HOUR,
            MINUTES,
            DAYS_OF_WEEK,
            ENABLED,
            VIBRATE,
            LABEL,
            RINGTONE_ID,
            RINGTONES_TABLE_NAME + "." + REMOTE_OBJECT_KEY,
            RINGTONES_TABLE_NAME + "." + MUSIC_SERVICE
    };

    /**
     * These Column Indexes must match the order of the {@link Alarm#QUERY_COLUMNS}
     */
    private static final int ID_INDEX = 0;
    private static final int HOUR_INDEX = 1;
    private static final int MINUTES_INDEX = 2;
    private static final int DAYS_OF_WEEK_INDEX = 3;
    private static final int ENABLED_INDEX = 4;
    private static final int VIBRATE_INDEX = 5;
    private static final int LABEL_INDEX = 6;
    private static final int RINGTONE_ID_INDEX = 7;
    private static final int REMOTE_OBJ_KEY_INDEX = 8;
    private static final int MUSIC_SERVICE_INDEX = 9;

    private static final int QUERY_COLUMN_COUNT = MUSIC_SERVICE_INDEX + 1;

    private long id = INVALID_ID;
    private LocalTime alarmTime;
    private boolean enabled = true;
    private boolean vibrateOn = true;
    private String label = "";
    private DaysOfWeek daysOfWeek = new DaysOfWeek(DaysOfWeek.NO_DAYS_SET);
    private Ringtone ringtone = Ringtone.createDefault();

    public Alarm(LocalTime alarmTime) {
        this.alarmTime = alarmTime;
        Ringtone ring = new Ringtone("spotify:track:4mnVTZqDC2fy2Sh1ooKEse", RemoteMusicServiceType.SPOTIFY);
        setRingtone(ring);
//        setRingtone(new Ringtone(Uri.parse("content://media/internal/audio/media/24")));
    }

    public Alarm(LocalTime alarmTime, boolean enabled, boolean vibrateOn, String label, DaysOfWeek daysOfWeek, Ringtone ringtone) {
        this(INVALID_ID, alarmTime, enabled, vibrateOn, label, daysOfWeek, ringtone);
    }

    public Alarm(long id, LocalTime alarmTime, boolean enabled, boolean vibrateOn, String label, DaysOfWeek daysOfWeek, Ringtone ringtone) {
        this.id = id;
        this.alarmTime = alarmTime;
        this.enabled = enabled;
        this.vibrateOn = vibrateOn;
        this.label = label;
        this.daysOfWeek = daysOfWeek;
        this.ringtone = ringtone;
    }

    public Alarm(Parcel src) {
        int hour = src.readInt();
        int min = src.readInt();
        int alarmStateOn = src.readInt();
        int daysOfWeek = src.readInt();
        Ringtone ringtone = src.readParcelable(Ringtone.class.getClassLoader());

        setAlarmTime(new LocalTime(hour, min));
        setEnabled(alarmStateOn == 1);
        setDaysOfWeek(daysOfWeek);
        setRingtone(ringtone);
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    //region accessors

    public boolean hasId() {
        return getId() != INVALID_ID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalTime getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(LocalTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public DaysOfWeek getDaysOfWeek() {
        return daysOfWeek;
    }

    public int getDaysOfWeekBitSet() {
        return daysOfWeek.getBitSet();
    }

    public void setDaysOfWeek(int daysOfWeekBitSet) {
        this.daysOfWeek = new DaysOfWeek(daysOfWeekBitSet);
    }

    public void setDaysOfWeek(DaysOfWeek daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public boolean isRepeating() {
        return getDaysOfWeek().isRepeating();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean isOn) {
        this.enabled = isOn;
    }

    public boolean isVibrateOn() {
        return this.vibrateOn;
    }

    public void setVibrateOn(boolean vibrateOn) {
        this.vibrateOn = vibrateOn;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Ringtone getRingtone() {
        return ringtone;
    }

    public void setRingtone(Ringtone ringtone) {
        this.ringtone = ringtone;
    }

    //endregion

    @Override
    public int compareTo(Alarm another) {
        return this.getAlarmTime().compareTo(another.getAlarmTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getAlarmTime().getHourOfDay());
        dest.writeInt(getAlarmTime().getMinuteOfHour());
        dest.writeInt(isEnabled() ? 1 : 0);
        dest.writeInt(getDaysOfWeekBitSet());
        dest.writeParcelable(getRingtone(), 0);
    }

    /**
     * Create an Intent with the Data set to this Alarm
     * @param action The Action string to use for the intent
     * @param alarmId The Alarm ID to use as the Alarm Data Uri
     * @return The new Intent for this Alarm instance
     */
    public static Intent createIntent(String action, long alarmId) {
        return new Intent(action).setData(getUri(alarmId));
    }

    /**
     * Create an Intent with the Data set to this Alarm
     * @param context The Context
     * @param cls The Class to create the Intent with
     * @param alarmId The Alarm ID to use as the Alarm Data Uri
     * @return The new Intent for this Alarm instance
     */
    public static Intent createIntent(Context context, Class<?> cls, long alarmId) {
        return new Intent(context, cls).setData(getUri(alarmId));
    }

    /**
     * Create a Uri for a specific alarm.
     * {@link BebopContract.AlarmsColumns#CONTENT_URI} with the ID appended.
     * @param alarmId The Alarm ID to append
     * @return The Uri to a specific alarm resource in the BebopProvider
     */
    public static Uri getUri(long alarmId) {
        return ContentUris.withAppendedId(BebopContract.AlarmsColumns.CONTENT_URI, alarmId);
    }

    /**
     * Parse the ID from the Alarm Resource Uri
     * @param contentUri The resource Uri to the Alarm
     * @return The ID parsed from the Uri
     */
    public static long getId(Uri contentUri) {
        return ContentUris.parseId(contentUri);
    }


    /**
     * @return the ContentValues for this Alarm Instance
     */
    public ContentValues createContentValues() {
        ContentValues values = new ContentValues();


        if (hasId()) {
            values.put(BebopContract.AlarmsColumns._ID, getId());
        }
        values.put(HOUR, getAlarmTime().getHourOfDay());
        values.put(MINUTES, getAlarmTime().getMinuteOfHour());
        values.put(DAYS_OF_WEEK, getDaysOfWeekBitSet());
        values.put(ENABLED, isEnabled());
        values.put(VIBRATE, isVibrateOn());
        values.put(LABEL, getLabel());

        if (getRingtone().hasId()) {
            values.put(RINGTONE_ID, getRingtone().getId());
        } else {
            BebopLog.wtf("Cannot createContentValues for Alarm with an invalid Ringtone.");
        }

        return values;
    }


    public static Alarm createFromCursor(Cursor cur) {
        Long id = cur.getLong(ID_INDEX);
        int hour = cur.getInt(HOUR_INDEX);
        int min = cur.getInt(MINUTES_INDEX);
        int dow = cur.getInt(DAYS_OF_WEEK_INDEX);
        boolean enabled = cur.getInt(ENABLED_INDEX) == 1;
        boolean vib = cur.getInt(VIBRATE_INDEX) == 1;
        String label = cur.getString(LABEL_INDEX);
        Long ringtoneId = cur.getLong(RINGTONE_ID_INDEX);
        String remoteObjKey = cur.getString(REMOTE_OBJ_KEY_INDEX);
        RemoteMusicServiceType service = RemoteMusicServiceType.remoteMusicService(cur.getInt(MUSIC_SERVICE_INDEX));

        Alarm alarm = new Alarm(new LocalTime(hour, min));
        alarm.setId(id);
        alarm.setDaysOfWeek(dow);
        alarm.setEnabled(enabled);
        alarm.setVibrateOn(vib);
        alarm.setLabel(label);
        alarm.setRingtone(new Ringtone(ringtoneId, remoteObjKey, service));

        return alarm;
    }

    public static Loader<Cursor> createCursorLoader(Context context, int id, Bundle args) {
        return new CursorLoader(context, BebopContract.AlarmsColumns.CONTENT_URI, QUERY_COLUMNS, null, null, DEFAULT_SORT_ORDER);
    }


    /**
     * Insert or Update Ringtone based on whether the Ringtone ID is valid.
     * @param resolver The ContentResolver to operate on
     * @param alarm The Ringtone to upsert
     * @return The Ringtone after inserting or updating with a valid ID set
     */
    public static Alarm upsertRingtone(ContentResolver resolver, Alarm alarm) {
        if (alarm.hasId()) {
            return updateAlarm(resolver, alarm);
        }
        return insertAlarm(resolver, alarm);
    }

    /**
     * Insert a new Alarm into the BebopProvider. The Alarm must have an
     * invalid ID when inserting.
     * @param resolver The ContentResolver to operate on
     * @param alarm The Alarm to insert
     * @return The Alarm with the newly created ID from the ContentProvider
     */
    public static Alarm insertAlarm(ContentResolver resolver, Alarm alarm) {
        Ringtone.upsertRingtone(resolver, alarm.getRingtone());
        Uri alarmUri = resolver.insert(BebopContract.AlarmsColumns.CONTENT_URI, alarm.createContentValues());
        if (alarmUri == null) {
            BebopLog.wtf("The ringtone could not be inserted! Null ringtoneUri");
        } else {
            alarm.setId(getId(alarmUri));
        }
        return alarm;
    }

    /**
     * Update a Alarm in the BebopProvider. The Alarm must have a valid
     * ID when updating.
     * @param resolver The ContentResolver to operate on
     * @param alarm The Alarm to update
     * @return The Alarm object as passed into this function. If the number
     * of updated rows does not equal 1 a WTF will occur.
     */
    public static Alarm updateAlarm(ContentResolver resolver, Alarm alarm) {
        int updated = resolver.update(getUri(alarm.getId()), alarm.createContentValues(), null, null);
        if (updated != 1) {
            BebopLog.wtf("One alarm should have been updated but instead %d alarms were updated.", updated);
        }
        return alarm;
    }

    /**
     * Delete an Alarm in the BebopProvider. The Alarm must have a valid
     * ID when deleting
     * @param resolver The ContentResolver to operate on
     * @param alarm The Alarm to delete
     * @return The Alarm object as passed into this function.
     */
    public static Alarm deleteAlarm(ContentResolver resolver, Alarm alarm) {
        int deleted = resolver.delete(getUri(alarm.getId()), null, null);
        if (deleted != 1) {
            BebopLog.wtf("One alarm should have been deleted but instead %d alarms were deleted.", deleted);
        }
        return alarm;
    }

    public static int findPositionOfAddedRow(Cursor newCursor, Cursor oldCursor) {
        boolean allowed = newCursor.moveToFirst();
        allowed = oldCursor.moveToFirst() && allowed;
        if (allowed) {
            do {
                Long newId = newCursor.getLong(ID_INDEX);
                Long oldId = oldCursor.getLong(ID_INDEX);
                if (newId != oldId) {
                    return newCursor.getPosition();
                }
            } while(newCursor.moveToNext() && oldCursor.moveToNext());
            if (newCursor.isLast() && oldCursor.isAfterLast()) {
                return newCursor.getPosition();
            }
        }
        return -1;
    }

    public static int findPositionOfDeletedRow(Cursor newCursor, Cursor oldCursor) {
        boolean allowed = newCursor.moveToFirst();
        allowed = oldCursor.moveToFirst() && allowed;
        if (allowed) {
            do {
                Long newId = newCursor.getLong(ID_INDEX);
                Long oldId = oldCursor.getLong(ID_INDEX);
                if (newId != oldId) {
                    return oldCursor.getPosition();
                }
            } while(newCursor.moveToNext() && oldCursor.moveToNext());
            if (newCursor.isLast() && oldCursor.isAfterLast()) {
                return newCursor.getPosition();
            }
        }
        return -1;
    }
}
