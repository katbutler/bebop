package com.katbutler.bebop.model;

import android.content.res.Resources;

import com.katbutler.bebop.R;

/**
 * Music Services supported by Bebop
 */
public enum RemoteMusicService {
    NO_SERVICE(R.string.no_service),
    RDIO(R.string.rdio);

    int mServiceNameStringRes;

    RemoteMusicService(int serviceNameStringRes) {
        this.mServiceNameStringRes = serviceNameStringRes;
    }

    public String getServiceName(Resources res) {
        return res.getString(mServiceNameStringRes);
    }


    public static RemoteMusicService remoteMusicService(int ordinal) {
        for (RemoteMusicService service : values()) {
            if (service.ordinal() == ordinal) {
                return service;
            }
        }

        return  NO_SERVICE;
    }
}
