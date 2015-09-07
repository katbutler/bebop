package com.katbutler.bebop.alarmslist;

import android.content.ContentResolver;

import com.katbutler.bebop.model.Alarm;
import com.katbutler.bebop.provider.BebopContract;
import com.katbutler.bebop.provider.BebopDatabaseHelper;
import com.katbutler.bebopcommon.Presenter;
import com.katbutler.bebopcommon.Ui;

import java.util.List;

/**
 * Created by kat on 15-09-05.
 */
public class AlarmsPresenter extends Presenter<AlarmsPresenter.AlarmsUi> {

    @Override
    public void onUiReady(AlarmsUi ui) {
        super.onUiReady(ui);

    }

    public void addNewAlarm(Alarm alarm) {
        ContentResolver resolver = getUi().getContext().getContentResolver();
        BebopDatabaseHelper.insertAlarm(resolver, alarm);
    }

    public interface AlarmsUi extends Ui {
        List<String> getAlarmList();

    }
}
