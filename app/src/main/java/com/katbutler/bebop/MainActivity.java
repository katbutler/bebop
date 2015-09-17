package com.katbutler.bebop;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.katbutler.bebop.alarm.AlarmActivity;
import com.katbutler.bebop.model.Alarm;
import com.katbutler.bebop.utils.AlarmTimeUtils;

import org.joda.time.LocalTime;
import org.joda.time.Seconds;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver mAlarmReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(BebopIntents.ACTION_CHANGE_ALARM_STATE)) {

                Alarm alarm = intent.getParcelableExtra(BebopIntents.EXTRA_ALARM);

                if (alarm.isEnabled()) {
                    Intent alarmIntent = new Intent(MainActivity.this, AlarmActivity.class);
                    alarmIntent.putExtra(AlarmActivity.EXTRA_ALARM, alarm);
                    AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

                    LocalTime now = AlarmTimeUtils.getCurrentTime();
                    Seconds secs = Seconds.secondsBetween(now, alarm.getAlarmTime());

                    am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (secs.getSeconds() * 1000), pendingIntent);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BebopIntents.ACTION_CHANGE_ALARM_STATE);

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mAlarmReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(mAlarmReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
