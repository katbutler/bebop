package com.katbutler.bebop.alarms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.katbutler.bebop.utils.AlarmAlertWakeLock;
import com.katbutler.bebop.utils.AsyncHandler;

/**
 * Created by kat on 2015-09-07.
 */
public final class AlarmStateManager extends BroadcastReceiver {

    private static final String INDICATOR_ACTION = "indicator";

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (INDICATOR_ACTION.equals(intent.getAction())) {
            return;
        }

        final PendingResult result = goAsync();
        final PowerManager.WakeLock wl = AlarmAlertWakeLock.createPartialWakeLock(context);
        wl.acquire();
        AsyncHandler.post(new Runnable() {
            @Override
            public void run() {
                handleIntent(context, intent);
                result.finish();
                wl.release();
            }
        });
    }

    private void handleIntent(Context context, Intent intent) {

    }

    /**
     * Creates an intent that can be used to set an AlarmManager alarm to set the next alarm
     * indicators.
     */
    public static Intent createIndicatorIntent(Context context) {
        return new Intent(context, AlarmStateManager.class).setAction(INDICATOR_ACTION);
    }


}
