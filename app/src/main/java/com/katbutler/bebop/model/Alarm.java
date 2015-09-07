package com.katbutler.bebop.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.katbutler.bebop.provider.BebopContract;

import org.joda.time.Days;
import org.joda.time.LocalTime;

import java.util.Set;

/**
 * Created by kat on 15-09-05.
 */
public class Alarm implements Comparable<Alarm>, Parcelable{

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
        values.put(BebopContract.AlarmsColumns.HOUR, getAlarmTime().getHourOfDay());
        values.put(BebopContract.AlarmsColumns.MINUTES, getAlarmTime().getMinuteOfHour());
        values.put(BebopContract.AlarmsColumns.DAYS_OF_WEEK, getDaysOfWeekMask());
        values.put(BebopContract.AlarmsColumns.ENABLED, isAlarmStateOn());
        values.put(BebopContract.AlarmsColumns.VIBRATE, isVibrateOn());
        values.put(BebopContract.AlarmsColumns.LABEL, getLabel());
        values.put(BebopContract.AlarmsColumns.RINGTONE_ID, ringtoneId);

        return values;
    }


}
