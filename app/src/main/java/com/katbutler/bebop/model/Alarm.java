package com.katbutler.bebop.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.Days;
import org.joda.time.LocalTime;

import java.util.Set;

/**
 * Created by kat on 15-09-05.
 */
public class Alarm implements Comparable<Alarm>, Parcelable{

    private LocalTime alarmTime;
    private boolean alarmStateOn = true;
    private int daysOfWeekMask = 0;

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
}
