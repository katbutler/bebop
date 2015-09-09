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

import com.katbutler.bebop.provider.BebopContract;

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

        setAlarmTime(new LocalTime(hour, min));
        setEnabled(alarmStateOn == 1);
        setDaysOfWeek(daysOfWeek);
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
    }

    public static Intent createIntent(String action, long alarmId) {
        return new Intent(action).setData(getUri(alarmId));
    }

    public static Intent createIntent(Context context, Class<?> cls, long alarmId) {
        return new Intent(context, cls).setData(getUri(alarmId));
    }

    public static Uri getUri(long alarmId) {
        return ContentUris.withAppendedId(BebopContract.AlarmsColumns.CONTENT_URI, alarmId);
    }

    public static long getId(Uri contentUri) {
        return ContentUris.parseId(contentUri);
    }

    /**
     * Insert a new Alarm into the database
     * @param resolver The Android Content Resolver
     * @param alarm The Alarm to insert into the database
     * @return true if the alarm was inserted otherwise false
     */
    public static Alarm insertAlarm(ContentResolver resolver, Alarm alarm) {
        Uri ringtoneUri = resolver.insert(BebopContract.RingtonesColumns.CONTENT_URI, alarm.getRingtone().getContentValues());
        if (ringtoneUri != null) {
            long ringtoneId = getId(ringtoneUri);
            Uri alarmUri = resolver.insert(BebopContract.AlarmsColumns.CONTENT_URI, alarm.getContentValues(ringtoneId));
            if (alarmUri != null) {
                alarm.setId(getId(alarmUri));
                return alarm;
            }
        }
        return null;
    }

    public ContentValues getContentValues(long ringtoneId) {
        ContentValues values = new ContentValues();


        if (getId() != -1) {
            values.put(BebopContract.AlarmsColumns._ID, getId());
        }
        values.put(HOUR, getAlarmTime().getHourOfDay());
        values.put(MINUTES, getAlarmTime().getMinuteOfHour());
        values.put(DAYS_OF_WEEK, getDaysOfWeekBitSet());
        values.put(ENABLED, isEnabled());
        values.put(VIBRATE, isVibrateOn());
        values.put(LABEL, getLabel());
        values.put(RINGTONE_ID, ringtoneId);

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
        RemoteMusicService service = RemoteMusicService.remoteMusicService(cur.getInt(MUSIC_SERVICE_INDEX));

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
}
