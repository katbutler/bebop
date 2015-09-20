package com.katbutler.bebop.alarm;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.katbutler.bebop.R;
import com.katbutler.bebop.model.Alarm;
import com.katbutler.bebop.musicservice.RemoteMusicObject;
import com.katbutler.bebop.musicservice.RemoteMusicServiceType;
import com.katbutler.bebop.musicservice.spotify.SpotifyConstants;
import com.katbutler.bebop.musicservice.spotify.SpotifyPreferences;
import com.katbutler.bebop.utils.BebopLog;

public class AlarmActivity extends AppCompatActivity {

    public static final String EXTRA_ALARM = "com.katbutler.bebop.extra.ALARM";
    private CurrentAlarmFragment mCurrentAlarmFragment;
    private Alarm mAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        mAlarm = getIntent().getParcelableExtra(EXTRA_ALARM);

        if (mAlarm == null) {
            finish();
        }

        mCurrentAlarmFragment = new CurrentAlarmFragment();

        getFragmentManager().beginTransaction()
                .add(R.id.alarm_container, mCurrentAlarmFragment)
                .commit();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        switch (mAlarm.getRingtone().getRemoteMusicServiceType()) {
            case NO_SERVICE:
                break;
            case RDIO:
                break;
            case SPOTIFY:
                SpotifyPreferences.getInstance().login(this);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SpotifyConstants.AUTH_REQUEST_CODE:
                SpotifyPreferences.getInstance().onAuthResult(resultCode, data);
                mCurrentAlarmFragment.playAlarm(mAlarm);
                break;
        }
    }
}
