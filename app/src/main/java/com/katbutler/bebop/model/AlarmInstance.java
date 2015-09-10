package com.katbutler.bebop.model;

import org.joda.time.DateTime;

/**
 * Created by kat on 2015-09-07.
 */
public class AlarmInstance {

    /**
     * Alarms start with an invalid id when it hasn't been saved to the database.
     */
    public static final long INVALID_ID = -1;

    private long id = INVALID_ID;
    private DateTime instanceTime;
    private boolean enabled = true;
    private boolean vibrateOn = true;
    private String label = "";
    private DaysOfWeek daysOfWeek = new DaysOfWeek(DaysOfWeek.NO_DAYS_SET);
    private Ringtone ringtone = Ringtone.createDefault();
    private int alarmState = AlarmState.SILENT_STATE;
    private long alarmId = INVALID_ID;

    public AlarmInstance(DateTime instanceTime, boolean enabled, boolean vibrateOn, String label, DaysOfWeek daysOfWeek, Ringtone ringtone, @AlarmState int alarmState, long alarmId) {
        this(INVALID_ID, instanceTime, enabled, vibrateOn, label, daysOfWeek, ringtone, alarmState, alarmId);
    }

    public AlarmInstance(long id, DateTime instanceTime, boolean enabled, boolean vibrateOn, String label, DaysOfWeek daysOfWeek, Ringtone ringtone, @AlarmState int alarmState, long alarmId) {
        this.id = id;
        this.instanceTime = instanceTime;
        this.enabled = enabled;
        this.vibrateOn = vibrateOn;
        this.label = label;
        this.daysOfWeek = daysOfWeek;
        this.ringtone = ringtone;
        this.alarmState = alarmState;
        this.alarmId = alarmId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DateTime getInstanceTime() {
        return instanceTime;
    }

    public void setInstanceTime(DateTime instanceTime) {
        this.instanceTime = instanceTime;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isVibrateOn() {
        return vibrateOn;
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

    public DaysOfWeek getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(DaysOfWeek daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public Ringtone getRingtone() {
        return ringtone;
    }

    public void setRingtone(Ringtone ringtone) {
        this.ringtone = ringtone;
    }

    @AlarmState
    public int getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(@AlarmState int alarmState) {
        this.alarmState = alarmState;
    }

    public long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(long alarmId) {
        this.alarmId = alarmId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AlarmInstance)) return false;
        final AlarmInstance other = (AlarmInstance) o;
        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Long.valueOf(getId()).hashCode();
    }
}
