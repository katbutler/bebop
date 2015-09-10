package com.katbutler.bebop.musicservice;

import android.app.IntentService;

/**
 * Created by kat on 15-09-05.
 */
public abstract class RemoteMusicService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RemoteMusicService(String name) {
        super(name);
    }

    public abstract void playSong();
}
