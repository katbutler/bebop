package com.katbutler.bebop.model;

/**
 * Created by kat on 2015-09-07.
 */
public enum AlarmState {
    SILENT_STATE,
    LOW_NOTIFICATION_STATE,
    HIDE_NOTIFICATION_STATE,
    SNOOZE_STATE,
    FIRED_STATE,
    MISSED_STATE,
    DISMISSED_STATE;

    /**
     * Get the AlarmState enum value for the ordinal value
     * @param ordinal The ordinal index of the enum value
     * @return The AlarmState for the ordinal value
     */
    public static AlarmState alarmState(int ordinal) {
        for (AlarmState service : values()) {
            if (service.ordinal() == ordinal) {
                return service;
            }
        }

        return null;
    }
}
