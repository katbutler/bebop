package com.katbutler.bebop.musicservice.rdio;

import android.content.Context;

import com.katbutler.bebop.musicservice.RemoteMusicObject;
import com.rdio.android.sdk.PlayRequest;

/**
 * Created by kat on 15-09-11.
 */
public class RdioMusicObject extends RemoteMusicObject<PlayRequest> {

    @Override
    public PlayRequest getRemoteObject() {
        return new PlayRequest(getKey());
    }

    @Override
    public void onPlay(PlayRequest obj) {

    }

    @Override
    public void onStop() {

    }
}
