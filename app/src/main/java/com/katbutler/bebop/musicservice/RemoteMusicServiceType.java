package com.katbutler.bebop.musicservice;

import android.content.res.Resources;

import com.katbutler.bebop.R;
import com.katbutler.bebop.musicservice.local.LocalMusicObject;
import com.katbutler.bebop.musicservice.rdio.RdioMusicObject;
import com.katbutler.bebop.utils.BebopLog;

/**
 * Music Services supported by Bebop
 */
public enum RemoteMusicServiceType {
    NO_SERVICE(R.string.no_service, LocalMusicObject.class),
    RDIO(R.string.rdio, RdioMusicObject.class);

    int mServiceNameStringRes;
    Class<? extends RemoteMusicObject> mClazz;

    RemoteMusicServiceType(int serviceNameStringRes, Class<? extends RemoteMusicObject> clazz) {
        this.mServiceNameStringRes = serviceNameStringRes;
        this.mClazz = clazz;
    }

    public String getServiceName(Resources res) {
        return res.getString(mServiceNameStringRes);
    }


    public static RemoteMusicServiceType remoteMusicService(int ordinal) {
        for (RemoteMusicServiceType service : values()) {
            if (service.ordinal() == ordinal) {
                return service;
            }
        }
        return NO_SERVICE;
    }

    /**
     * Create a new instance of {@link RemoteMusicObject} using the {@link #mClazz} class value.
     * @param key They key to initialize the {@link RemoteMusicObject} with.
     * @return a new {@link RemoteMusicObject} for the {@link RemoteMusicServiceType}
     */
    protected RemoteMusicObject createRemoteMusicObject(String key) {
        try {
            RemoteMusicObject object = this.mClazz.newInstance();
            object.setKey(key);
            return object;
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            BebopLog.wtf("Wow I really messed this up. Fix the clazz value in this enum.");
        }
        return null;
    }

}
