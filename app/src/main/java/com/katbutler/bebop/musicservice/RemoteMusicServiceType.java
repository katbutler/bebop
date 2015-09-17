package com.katbutler.bebop.musicservice;

import android.content.res.Resources;

import com.katbutler.bebop.R;
import com.katbutler.bebop.musicservice.local.LocalMusicObject;
import com.katbutler.bebop.musicservice.rdio.RdioMusicObject;

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

    public Class<? extends RemoteMusicObject> getRemoteMusicClass() {
        return mClazz;
    }

    public static RemoteMusicServiceType remoteMusicService(int ordinal) {
        for (RemoteMusicServiceType service : values()) {
            if (service.ordinal() == ordinal) {
                return service;
            }
        }

        return  NO_SERVICE;
    }
}
