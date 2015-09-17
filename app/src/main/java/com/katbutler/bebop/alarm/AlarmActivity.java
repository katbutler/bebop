package com.katbutler.bebop.alarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.katbutler.bebop.R;
import com.katbutler.bebop.model.Alarm;
import com.katbutler.bebop.musicservice.RemoteMusicObject;
import com.katbutler.bebop.utils.BebopLog;

public class AlarmActivity extends AppCompatActivity {

    public static final String EXTRA_ALARM = "com.katbutler.bebop.extra.ALARM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Alarm alarm = getIntent().getParcelableExtra(EXTRA_ALARM);

        if (alarm == null) {
            finish();
        }

        CurrentAlarmFragment currentAlarmFragment = new CurrentAlarmFragment();

        getFragmentManager().beginTransaction()
                .add(R.id.alarm_container, currentAlarmFragment)
                .commit();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        currentAlarmFragment.playAlarm(alarm);
    }

}
