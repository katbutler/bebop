package com.katbutler.bebop.musicservice;

import android.content.Context;

/**
 * Created by kat on 2015-09-17.
 */
public abstract class RemoteMusicPreferences {

    private Context mContext;

    protected RemoteMusicPreferences(Context context) {
        mContext = context;
    }

    // DO NOT USE
    private RemoteMusicPreferences() {
        throw new IllegalArgumentException("Use RemoteMusicPreferences(Context) instead");
    }

    public Context getContext() {
        return mContext;
    }
}
