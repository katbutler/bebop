package com.katbutler.bebop.model;

import org.joda.time.Days;
import org.joda.time.LocalTime;

import java.util.Set;

/**
 * Created by kat on 15-09-05.
 */
public class Alarm {

    private LocalTime alarmTime;

    private int daysOfWeekMask = 0;

    public Alarm(LocalTime alarmTime) {
        this.alarmTime = alarmTime;
    }

    public LocalTime getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(LocalTime alarmTime) {
        this.alarmTime = alarmTime;
    }
}
