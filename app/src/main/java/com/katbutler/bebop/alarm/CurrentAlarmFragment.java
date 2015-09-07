package com.katbutler.bebop.alarm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.katbutler.bebop.R;
import com.katbutler.bebop.utils.AlarmTimeUtils;
import com.katbutler.bebopcommon.BaseFragment;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by kat on 15-09-06.
 */
public class CurrentAlarmFragment extends BaseFragment<CurrentAlarmPresenter, CurrentAlarmPresenter.CurrentAlarmUi> implements CurrentAlarmPresenter.CurrentAlarmUi {
    @Override
    public CurrentAlarmPresenter createPresenter() {
        return new CurrentAlarmPresenter();
    }

    @Override
    public CurrentAlarmPresenter.CurrentAlarmUi getUi() {
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_alarm, container, false);

        TextView currentTimeText = (TextView) view.findViewById(R.id.current_time_text);
        TextView currentAmPmText = (TextView) view.findViewById(R.id.current_ampm_text);
        DateTimeFormatter fmtTime = DateTimeFormat.forPattern("h:mm");
        DateTimeFormatter fmtAmPm = DateTimeFormat.forPattern("a");

        LocalTime time = AlarmTimeUtils.getCurrentTime();

        currentTimeText.setText(fmtTime.print(time));
        currentAmPmText.setText(fmtAmPm.print(time));

        return view;
    }
}
