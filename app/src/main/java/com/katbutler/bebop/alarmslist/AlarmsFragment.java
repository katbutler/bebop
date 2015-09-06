package com.katbutler.bebop.alarmslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katbutler.bebop.BebopIntents;
import com.katbutler.bebop.R;
import com.katbutler.bebop.model.Alarm;
import com.katbutler.bebopcommon.BaseFragment;

import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kat on 15-09-05.
 */
public class AlarmsFragment extends BaseFragment<AlarmsPresenter, AlarmsPresenter.AlarmsUi> implements AlarmsPresenter.AlarmsUi {


    private RecyclerView mAlarmRecyclerView;

    @Override
    public AlarmsPresenter createPresenter() {
        return new AlarmsPresenter();
    }

    @Override
    public AlarmsPresenter.AlarmsUi getUi() {
        return this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container);

        List<Alarm> alarms = new ArrayList<>();
        alarms.add(new Alarm(new LocalTime(7, 0)));
        alarms.add(new Alarm(new LocalTime(15, 30)));
        alarms.add(new Alarm(new LocalTime(11, 10)));
        alarms.add(new Alarm(new LocalTime(5, 55)));
        alarms.add(new Alarm(new LocalTime(6, 55)));
        alarms.add(new Alarm(new LocalTime(2, 22)));
        alarms.add(new Alarm(new LocalTime(14, 18)));
        alarms.add(new Alarm(new LocalTime(14, 22)));
        alarms.add(new Alarm(new LocalTime(12, 55)));

        Collections.sort(alarms);

        mAlarmRecyclerView = findViewOnView(view,R.id.alarm_recyclerview);
        AlarmAdapter adapter = new AlarmAdapter(alarms);
        adapter.setOnAlarmStateChangeListener(new AlarmAdapter.OnAlarmStateChangeListener() {
            @Override
            public void onAlarmStateChange(Alarm alarm) {
                Intent intent = new Intent(BebopIntents.ACTION_CHANGE_ALARM_STATE);
                intent.putExtra(BebopIntents.EXTRA_ALARM, alarm);

                LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
            }
        });
        mAlarmRecyclerView.setAdapter(adapter);
        mAlarmRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    @Override
    public List<String> getAlarmList() {
        return null;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }
}
