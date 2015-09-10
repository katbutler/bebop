package com.katbutler.bebop.musicservice.local;

import android.content.Intent;

import com.katbutler.bebop.musicservice.RemoteMusicService;

/**
 * Created by kat on 15-09-10.
 */
public class LocalMusicService extends RemoteMusicService {

    private static final String TAG = LocalMusicService.class.getSimpleName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public LocalMusicService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void playSong() {

    }
}
