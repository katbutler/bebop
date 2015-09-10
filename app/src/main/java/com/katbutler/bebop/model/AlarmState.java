package com.katbutler.bebop.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@IntDef({
        AlarmState.SILENT_STATE,
        AlarmState.LOW_NOTIFICATION_STATE,
        AlarmState.HIDE_NOTIFICATION_STATE,
        AlarmState.HIGH_NOTIFICATION_STATE,
        AlarmState.SNOOZE_STATE,
        AlarmState.FIRED_STATE,
        AlarmState.MISSED_STATE,
        AlarmState.DISMISSED_STATE
})
public @interface AlarmState {

    /**
     * Alarm state when to show no notification.
     * <p/>
     * Can transitions to:
     * LOW_NOTIFICATION_STATE
     */
    int SILENT_STATE = 0;

    /**
     * Alarm state to show low priority alarm notification.
     * <p/>
     * Can transitions to:
     * HIDE_NOTIFICATION_STATE
     * HIGH_NOTIFICATION_STATE
     * DISMISSED_STATE
     */
    int LOW_NOTIFICATION_STATE = 1;

    /**
     * Alarm state to hide low priority alarm notification.
     * <p/>
     * Can transitions to:
     * HIGH_NOTIFICATION_STATE
     */
    int HIDE_NOTIFICATION_STATE = 2;

    /**
     * Alarm state to show high priority alarm notification.
     * <p/>
     * Can transitions to:
     * DISMISSED_STATE
     * FIRED_STATE
     */
    int HIGH_NOTIFICATION_STATE = 3;

    /**
     * Alarm state when alarm is in snooze.
     * <p/>
     * Can transitions to:
     * DISMISSED_STATE
     * FIRED_STATE
     */
    int SNOOZE_STATE = 4;

    /**
     * Alarm state when alarm is being fired.
     * <p/>
     * Can transitions to:
     * DISMISSED_STATE
     * SNOOZED_STATE
     * MISSED_STATE
     */
    int FIRED_STATE = 5;

    /**
     * Alarm state when alarm has been missed.
     * <p/>
     * Can transitions to:
     * DISMISSED_STATE
     */
    int MISSED_STATE = 6;

    /**
     * Alarm state when alarm is done.
     */
    int DISMISSED_STATE = 7;
}
