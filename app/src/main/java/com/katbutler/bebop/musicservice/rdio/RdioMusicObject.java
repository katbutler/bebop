package com.katbutler.bebop.musicservice.rdio;

import com.katbutler.bebop.musicservice.RemoteMusicServiceType;
import com.katbutler.bebop.musicservice.RemoteMusicObject;
import com.rdio.android.sdk.PlayRequest;

public class RdioMusicObject extends RemoteMusicObject<PlayRequest> {

    public RdioMusicObject(String key) {
        super(key);
    }

    @Override
    public PlayRequest getRemoteObject() {
        return new PlayRequest(getKey());
    }

    @Override
    public RemoteMusicServiceType belongsToRemoteMusicService() {
        return RemoteMusicServiceType.RDIO;
    }

    @Override
    public void onPlay(PlayRequest remoteObject) {

    }


}