package com.katbutler.bebop.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.katbutler.bebop.MainActivity;
import com.katbutler.bebop.alarms.AlarmStateManager;
import com.katbutler.bebop.alarmslist.AlarmsFragment;
import com.katbutler.bebop.model.Alarm;
import com.katbutler.bebop.model.AlarmInstance;

/**
 * Created by kat on 2015-09-07.
 */
public class AlarmUtils {

    public static void registerNextAlarmWithAlarmManager(Context context, AlarmInstance instance)  {
        // Sets a surrogate alarm with alarm manager that provides the AlarmClockInfo for the
        // alarm that is going to fire next. The operation is constructed such that it is ignored
        // by AlarmStateManager.

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int flags = instance == null ? PendingIntent.FLAG_NO_CREATE : 0;
        PendingIntent operation = PendingIntent.getBroadcast(context, 0 /* requestCode */,
                AlarmStateManager.createIndicatorIntent(context), flags);
//                AlarmStateManager.createIndicatorIntent(context), flags);

        if (instance != null) {
            long alarmTime = instance.getInstanceTime().getMillis();

            // Create an intent that can be used to show or edit details of the next alarm.
            PendingIntent viewIntent = PendingIntent.getActivity(context, instance.hashCode(),
                    createViewAlarmIntent(context, instance), PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager.AlarmClockInfo info =
                    new AlarmManager.AlarmClockInfo(alarmTime, viewIntent);
            alarmManager.setAlarmClock(info, operation);
        } else if (operation != null) {
            alarmManager.cancel(operation);
        }
    }

    private static Intent createViewAlarmIntent(Context context, AlarmInstance instance) {
        Intent viewAlarmIntent = Alarm.createIntent(context, MainActivity.class, instance.getAlarmId());
//        viewAlarmIntent.putExtra(AlarmsFragment.SCROLL_TO_ALARM_INTENT_EXTRA, instance.getAlarmId());
        viewAlarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return viewAlarmIntent;
    }

}
