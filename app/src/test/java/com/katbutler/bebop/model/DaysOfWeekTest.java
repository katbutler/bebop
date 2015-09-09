package com.katbutler.bebop.model;


import com.katbutler.bebop.BuildConfig;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class)
public class DaysOfWeekTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void testGetAndSetBitSet() throws Exception {
        DaysOfWeek dow = new DaysOfWeek(DaysOfWeek.ALL_DAYS_SET);

        assertTrue("BitSet is not set to all days.", dow.getBitSet() == DaysOfWeek.ALL_DAYS_SET);

        dow.setBitSet(DaysOfWeek.NO_DAYS_SET);

        assertTrue("BitSet is not set to no days.", dow.getBitSet() == DaysOfWeek.NO_DAYS_SET);

    }

    @Test
    public void testGetSetDays() throws Exception {
        DaysOfWeek dow = new DaysOfWeek(DaysOfWeek.NO_DAYS_SET);
        dow.setDaysOfWeek(true, DateTimeConstants.MONDAY);

        assertTrue("BitSet contains Monday.", dow.getSetDays().contains(DateTimeConstants.MONDAY));

        dow.setDaysOfWeek(true, DateTimeConstants.MONDAY, DateTimeConstants.WEDNESDAY, DateTimeConstants.SUNDAY);

        assertTrue("BitSet size is not 3 it is " + dow.getSetDays().size(), dow.getSetDays().size() == 3);
        assertTrue("BitSet does not contains Monday.", dow.getSetDays().contains(DateTimeConstants.MONDAY));
        assertFalse("BitSet contains Tuesday.", dow.getSetDays().contains(DateTimeConstants.TUESDAY));
        assertTrue("BitSet does not contains Wednesday.", dow.getSetDays().contains(DateTimeConstants.WEDNESDAY));
        assertFalse("BitSet contains Thursday.", dow.getSetDays().contains(DateTimeConstants.THURSDAY));
        assertFalse("BitSet contains Friday.", dow.getSetDays().contains(DateTimeConstants.FRIDAY));
        assertFalse("BitSet contains Saturday.", dow.getSetDays().contains(DateTimeConstants.SATURDAY));
        assertTrue("BitSet does not contains Sunday.", dow.getSetDays().contains(DateTimeConstants.SUNDAY));

        dow.clearAllDays();
        dow.setDaysOfWeek(true, DateTimeConstants.TUESDAY, DateTimeConstants.THURSDAY, DateTimeConstants.FRIDAY, DateTimeConstants.SATURDAY);

        assertTrue("BitSet size is not 4 it is " + dow.getSetDays().size(), dow.getSetDays().size() == 4);
        assertFalse("BitSet does not contains Monday.", dow.getSetDays().contains(DateTimeConstants.MONDAY));
        assertTrue("BitSet does not contains Tuesday.", dow.getSetDays().contains(DateTimeConstants.TUESDAY));
        assertFalse("BitSet contains Wednesday.", dow.getSetDays().contains(DateTimeConstants.WEDNESDAY));
        assertTrue("BitSet does not contains Thursday.", dow.getSetDays().contains(DateTimeConstants.THURSDAY));
        assertTrue("BitSet does not contains Friday.", dow.getSetDays().contains(DateTimeConstants.FRIDAY));
        assertTrue("BitSet does not contains Saturday.", dow.getSetDays().contains(DateTimeConstants.SATURDAY));
        assertFalse("BitSet contains Sunday.", dow.getSetDays().contains(DateTimeConstants.SUNDAY));
    }

    @Test
    public void testIsRepeating() throws Exception {
        DaysOfWeek dow = new DaysOfWeek(DaysOfWeek.ALL_DAYS_SET);
        assertTrue("BitSet should not equal NO_DAYS_SET when isRepeating == true", dow.isRepeating() && dow.getBitSet() != DaysOfWeek.NO_DAYS_SET);

        dow.clearAllDays();
        dow.setDaysOfWeek(true, DateTimeConstants.TUESDAY, DateTimeConstants.THURSDAY, DateTimeConstants.FRIDAY, DateTimeConstants.SATURDAY);
        assertTrue("BitSet is == NO_DAYS_SET when isRepeating == true", dow.isRepeating() && dow.getBitSet() != DaysOfWeek.NO_DAYS_SET);
    }

    @Test
    public void testCalculateDaysToNextAlarm() throws Exception {
        DaysOfWeek dow = new DaysOfWeek(DaysOfWeek.NO_DAYS_SET);
        dow.setDaysOfWeek(true, DateTimeConstants.FRIDAY);

        //int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour
        // Tuesday Sept 8th, 2015 12:00 AM
        DateTime date = new DateTime(2015, 9, 8, 0, 0);
        int daysUntil = dow.calculateDaysToNextAlarm(date);

        assertTrue("There should be 3 days until Friday Sept 11th 2015 not " + daysUntil, daysUntil == 3);


        // Test if the next alarm is the same day
        dow.clearAllDays();
        dow.setDaysOfWeek(true, DateTimeConstants.MONDAY, DateTimeConstants.FRIDAY);

        // Friday April 1st, 2016 12:00 AM
        date = new DateTime(2016, 4, 1, 0, 0);
        daysUntil = dow.calculateDaysToNextAlarm(date);

        assertTrue("There should be 0 days until Friday April 1st, 2016 not " + daysUntil, daysUntil == 0);

        // Test if the next alarm is 6 days from date
        dow.clearAllDays();
        dow.setDaysOfWeek(true, DateTimeConstants.THURSDAY);

        // Friday April 1st, 2016 12:00 AM
        date = new DateTime(2016, 4, 1, 0, 0);
        daysUntil = dow.calculateDaysToNextAlarm(date);

        assertTrue("There should be 6 days until Thursday April 7th, 2016 not " + daysUntil, daysUntil == 6);

        // Test if the DaysOfWeek is not repeating
        dow.clearAllDays();
        date = new DateTime(2016, 4, 1, 0, 0);
        daysUntil = dow.calculateDaysToNextAlarm(date);

        assertTrue("Days until should be -1 when not repeating " + daysUntil, daysUntil == -1);
    }

    @Test
    public void testClearAllDays() throws Exception {
        DaysOfWeek dow = new DaysOfWeek(DaysOfWeek.ALL_DAYS_SET);
        dow.clearAllDays();

        assertTrue("BitSet != NO_DAYS_SET", dow.getBitSet() == DaysOfWeek.NO_DAYS_SET);
    }

    @Test
    public void testDaysOfWeekToString() throws Exception {
        DaysOfWeek dow = new DaysOfWeek(DaysOfWeek.ALL_DAYS_SET);

        String dowStr = "DaysOfWeek{mBitSet=127 Monday=true Tuesday=true Wednesday=true Thursday=true Friday=true Saturday=true Sunday=true}";

        assertTrue("Days of Week toString produced invalid string: " + dow.toString(), dow.toString().equals(dowStr));
    }
}
