package com.katbutler.bebop.alarm;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.katbutler.bebop.R;
import com.katbutler.bebop.model.Alarm;
import com.katbutler.bebop.utils.AlarmTimeUtils;
import com.katbutler.bebopcommon.BaseFragment;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by kat on 15-09-06.
 */
public class CurrentAlarmFragment extends BaseFragment<CurrentAlarmPresenter, CurrentAlarmPresenter.CurrentAlarmUi> implements CurrentAlarmPresenter.CurrentAlarmUi, View.OnClickListener {

    private Alarm mAlarm;

    @Override
    public CurrentAlarmPresenter createPresenter() {
        return new CurrentAlarmPresenter();
    }

    @Override
    public CurrentAlarmPresenter.CurrentAlarmUi getUi() {
        return this;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPresenter().playAlarm(mAlarm);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_alarm, container, false);

        TextView currentTimeText = (TextView) view.findViewById(R.id.current_time_text);
        TextView currentAmPmText = (TextView) view.findViewById(R.id.current_ampm_text);
        ImageButton alarmOffBtn = (ImageButton) view.findViewById(R.id.alarm_off_btn);
        alarmOffBtn.setOnClickListener(this);

        DateTimeFormatter fmtTime = DateTimeFormat.forPattern("h:mm");
        DateTimeFormatter fmtAmPm = DateTimeFormat.forPattern("a");

        LocalTime time = AlarmTimeUtils.getCurrentTime();

        currentTimeText.setText(fmtTime.print(time));
        currentAmPmText.setText(fmtAmPm.print(time));

        return view;
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.alarm_off_btn:
                getPresenter().onAlarmOffClicked();
                break;
        }
    }

    @Override
    public void showAlarmOffView() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.alarm_container, new AlarmOffFragment());
        ft.commit();
    }

    public void playAlarm(Alarm alarm) {
        mAlarm = alarm;
    }
}
