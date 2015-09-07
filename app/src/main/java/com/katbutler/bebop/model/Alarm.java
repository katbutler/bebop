package com.katbutler.bebop.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.katbutler.bebop.provider.BebopContract;

import org.joda.time.Days;
import org.joda.time.LocalTime;

import java.util.Set;

/**
 * Created by kat on 15-09-05.
 */
public class Alarm implements Comparable<Alarm>, Parcelable, BebopContract.AlarmsColumns {

    /**
     * The default sort order for this table
     */
    private static final String DEFAULT_SORT_ORDER =
            HOUR + ", " +
            MINUTES + " ASC" + ", " +
            _ID + " DESC";

    private static final String[] QUERY_COLUMNS = {
            _ID,
            HOUR,
            MINUTES,
            DAYS_OF_WEEK,
            ENABLED,
            VIBRATE,
            LABEL,
            RINGTONE_ID
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

    private long id = -1;
    private LocalTime alarmTime;
    private boolean alarmStateOn = true;
    private boolean vibrateOn = true;
    private String label = "";
    private int daysOfWeekMask = 0;
    private Ringtone ringtone = Ringtone.createDefault();

    public Alarm(LocalTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Alarm(Parcel src) {
        int hour = src.readInt();
        int min = src.readInt();
        int alarmStateOn = src.readInt();
        int daysOfWeek = src.readInt();

        setAlarmTime(new LocalTime(hour, min));
        setAlarmStateOn(alarmStateOn == 1);
        setDaysOfWeekMask(daysOfWeek);
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

    public int getDaysOfWeekMask() {
        return daysOfWeekMask;
    }

    public void setDaysOfWeekMask(int daysOfWeekMask) {
        this.daysOfWeekMask = daysOfWeekMask;
    }

    public boolean isAlarmStateOn() {
        return alarmStateOn;
    }

    public void setAlarmStateOn(boolean isOn) {
        this.alarmStateOn = isOn;
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
        dest.writeInt(isAlarmStateOn() ? 1 : 0);
        dest.writeInt(getDaysOfWeekMask());
    }

    public ContentValues getContentValues(long ringtoneId) {
        ContentValues values = new ContentValues();


        if (getId() != -1) {
            values.put(BebopContract.AlarmsColumns._ID, getId());
        }
        values.put(HOUR, getAlarmTime().getHourOfDay());
        values.put(MINUTES, getAlarmTime().getMinuteOfHour());
        values.put(DAYS_OF_WEEK, getDaysOfWeekMask());
        values.put(ENABLED, isAlarmStateOn());
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

        Alarm alarm = new Alarm(new LocalTime(hour, min));
        alarm.setId(id);
        alarm.setDaysOfWeekMask(dow);
        alarm.setAlarmStateOn(enabled);
        alarm.setVibrateOn(vib);
        alarm.setLabel(label);
        alarm.setRingtone(null);

        return alarm;
    }

    public static Loader<Cursor> createCursorLoader(Context context, int id, Bundle args) {
        return new CursorLoader(context, CONTENT_URI, QUERY_COLUMNS, null, null, DEFAULT_SORT_ORDER);
    }
}
