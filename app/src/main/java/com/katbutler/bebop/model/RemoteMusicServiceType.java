package com.katbutler.bebop.model;

import android.content.res.Resources;

import com.katbutler.bebop.R;

/**
 * Music Services supported by Bebop
 */
public enum RemoteMusicServiceType {
    NO_SERVICE(R.string.no_service),
    RDIO(R.string.rdio);

    int mServiceNameStringRes;

    RemoteMusicServiceType(int serviceNameStringRes) {
        this.mServiceNameStringRes = serviceNameStringRes;
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

        return  NO_SERVICE;
    }
}
