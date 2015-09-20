package com.katbutler.bebop.alarm;

import com.katbutler.bebop.model.Alarm;
import com.katbutler.bebop.musicservice.RemoteMusicObject;
import com.katbutler.bebop.utils.BebopLog;
import com.katbutler.bebopcommon.Presenter;
import com.katbutler.bebopcommon.Ui;

/**
 * Created by kat on 15-09-06.
 */
public class CurrentAlarmPresenter extends Presenter<CurrentAlarmPresenter.CurrentAlarmUi> {

    private RemoteMusicObject mMusicObj;

    public void onAlarmOffClicked() {
        getUi().showAlarmOffView();
        if (mMusicObj != null) {
            mMusicObj.stop();
        } else {
            BebopLog.wtf("Why doesn't this music object exist?!");
        }
    }

    public void playAlarm(Alarm alarm) {
        mMusicObj = RemoteMusicObject.createRemoteMusicObject(
                getUi().getContext(),
                alarm.getRingtone().getRemoteObjectKey(),
                alarm.getRingtone().getRemoteData(),
                alarm.getRingtone().getRemoteMusicServiceType());

        if (mMusicObj != null) {
            mMusicObj.play();
        } else {
            BebopLog.wtf("Why doesn't this music object exist?!");
        }
    }

    public interface CurrentAlarmUi extends Ui {
        void showAlarmOffView();
    }
}
