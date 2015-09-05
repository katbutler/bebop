package com.katbutler.bebop.alarmslist;

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

    public interface AlarmsUi extends Ui {
        List<String> getAlarmList();

    }
}
