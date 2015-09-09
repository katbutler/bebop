package com.katbutler.bebop.model;


import android.content.Context;

import com.katbutler.bebop.R;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeField;
import org.joda.time.DateTimeFieldType;

import java.text.DateFormatSymbols;
import java.util.HashSet;

/*
 * Days of week code as a single int.
 * 0x00: no day
 * 0x01: Monday
 * 0x02: Tuesday
 * 0x04: Wednesday
 * 0x08: Thursday
 * 0x10: Friday
 * 0x20: Saturday
 * 0x40: Sunday
 */
public final class DaysOfWeek {
    // Number if days in the week.
    public static final int DAYS_IN_A_WEEK = 7;

    // Value when all days are set
    public static final int ALL_DAYS_SET = 0x7f;

    // Value when no days are set
    public static final int NO_DAYS_SET = 0;

    /**
     * Need to have monday start at index 0 to be backwards compatible. This converts
     * DateTimeFieldType.dayOfWeek() constants to our internal bit structure.
     */
    private static int convertDayToBitIndex(int day) {
        return day - 1; //(day + 5) % DAYS_IN_A_WEEK;
    }

    /**
     * Need to have monday start at index 0 to be backwards compatible. This converts
     * our bit structure to DateTimeFieldType.dayOfWeek() constant value.
     */
    private static int convertBitIndexToDay(int bitIndex) {
        return bitIndex + 1; //(bitIndex + 1) % DAYS_IN_A_WEEK + 1;
    }

    // Bitmask of all repeating days
    private int mBitSet;

    public DaysOfWeek(int bitSet) {
        mBitSet = bitSet;
    }

    public String toString(Context context, boolean showNever) {
        return toString(context, showNever, false);
    }

    public String toAccessibilityString(Context context) {
        return toString(context, false, true);
    }

    private String toString(Context context, boolean showNever, boolean forAccessibility) {
        StringBuilder ret = new StringBuilder();

        // no days
        if (mBitSet == NO_DAYS_SET) {
            return showNever ? context.getText(R.string.never).toString() : "";
        }

        // every day
        if (mBitSet == ALL_DAYS_SET) {
            return context.getText(R.string.every_day).toString();
        }

        // count selected days
        int dayCount = 0;
        int bitSet = mBitSet;
        while (bitSet > 0) {
            if ((bitSet & 1) == 1) dayCount++;
            bitSet >>= 1;
        }

        // short or long form?
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] dayList = (forAccessibility || dayCount <= 1) ?
                dfs.getWeekdays() :
                dfs.getShortWeekdays();

        // selected days
        for (int bitIndex = 0; bitIndex < DAYS_IN_A_WEEK; bitIndex++) {
            if ((mBitSet & (1 << bitIndex)) != 0) {
                ret.append(dayList[convertBitIndexToDay(bitIndex)]);
                dayCount -= 1;
                if (dayCount > 0) ret.append(context.getText(R.string.day_concat));
            }
        }
        return ret.toString();
    }

    /**
     * Enables or disable certain days of the week.
     *
     * @param daysOfWeek DateTimeConstants.SUNDAY, DateTimeConstants.MONDAY, DateTimeConstants.TUESDAY, etc.
     */
    public void setDaysOfWeek(boolean value, int ... daysOfWeek) {
        for (int day : daysOfWeek) {
            setBit(convertDayToBitIndex(day), value);
        }
    }

    private boolean isDayEnabled(int dayOfWeekConstant) {
        return isBitEnabled(convertDayToBitIndex(dayOfWeekConstant));
    }

    private boolean isBitEnabled(int bitIndex) {
        return ((mBitSet & (1 << bitIndex)) > 0);
    }

    private void setBit(int bitIndex, boolean set) {
        if (set) {
            mBitSet |= (1 << bitIndex);
        } else {
            mBitSet &= ~(1 << bitIndex);
        }
    }

    public void setBitSet(int bitSet) {
        mBitSet = bitSet;
    }

    public int getBitSet() {
        return mBitSet;
    }

    public HashSet<Integer> getSetDays() {
        final HashSet<Integer> result = new HashSet<Integer>();
        for (int bitIndex = 0; bitIndex < DAYS_IN_A_WEEK; bitIndex++) {
            if (isBitEnabled(bitIndex)) {
                result.add(convertBitIndexToDay(bitIndex));
            }
        }
        return result;
    }

    public boolean isRepeating() {
        return mBitSet != NO_DAYS_SET;
    }

    /**
     * Returns number of days from today until next alarm.
     *
     * @param current must be set to today
     */
    public int calculateDaysToNextAlarm(DateTime current) {
        if (!isRepeating()) {
            return -1;
        }

        int dayCount = 0;
        int currentDayBit = convertDayToBitIndex(current.getDayOfWeek());
        for (; dayCount < DAYS_IN_A_WEEK; dayCount++) {
            int nextAlarmBit = (currentDayBit + dayCount) % DAYS_IN_A_WEEK;
            if (isBitEnabled(nextAlarmBit)) {
                break;
            }
        }
        return dayCount;
    }

    public void clearAllDays() {
        mBitSet = NO_DAYS_SET;
    }

    @Override
    public String toString() {
        return "DaysOfWeek{" +
                "mBitSet=" + mBitSet +
                " Monday=" + isDayEnabled(DateTimeConstants.MONDAY) +
                " Tuesday=" + isDayEnabled(DateTimeConstants.TUESDAY) +
                " Wednesday=" + isDayEnabled(DateTimeConstants.WEDNESDAY) +
                " Thursday=" + isDayEnabled(DateTimeConstants.THURSDAY) +
                " Friday=" + isDayEnabled(DateTimeConstants.FRIDAY) +
                " Saturday=" + isDayEnabled(DateTimeConstants.SATURDAY) +
                " Sunday=" + isDayEnabled(DateTimeConstants.SUNDAY) +
                '}';
    }

}