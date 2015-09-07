package com.katbutler.bebop.alarm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.katbutler.bebop.R;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        getFragmentManager().beginTransaction()
                .add(R.id.alarm_container, new CurrentAlarmFragment())
                .commit();
    }


}
