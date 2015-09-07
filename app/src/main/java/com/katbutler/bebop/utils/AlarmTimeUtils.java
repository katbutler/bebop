package com.katbutler.bebop.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

import java.util.TimeZone;

/**
 * Created by kat on 15-09-06.
 */
public abstract class AlarmTimeUtils {

    public static LocalTime getCurrentTime() {
        return new DateTime(DateTimeZone.forTimeZone(TimeZone.getDefault())).toLocalTime();
    }
}
