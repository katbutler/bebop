package com.katbutler.bebop.musicservice.local;

import android.net.Uri;

import com.katbutler.bebop.musicservice.RemoteMusicServiceType;
import com.katbutler.bebop.musicservice.RemoteMusicObject;

/**
 * Created by kat on 15-09-10.
 */
public class LocalMusicObject extends RemoteMusicObject<Uri> {

    public LocalMusicObject(Uri ringtoneUri) {
        super(ringtoneUri.toString());
    }

    @Override
    public Uri getRemoteObject() {
        return Uri.parse(getKey());
    }

    @Override
    public RemoteMusicServiceType belongsToRemoteMusicService() {
        return RemoteMusicServiceType.NO_SERVICE;
    }

    @Override
    public void onPlay(Uri remoteObject) {

    }
}
