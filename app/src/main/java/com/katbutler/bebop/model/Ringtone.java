package com.katbutler.bebop.model;

import android.content.ContentValues;
import android.net.Uri;

import com.katbutler.bebop.provider.BebopContract;

/**
 * Ringtone Model for remote services
 */
public class Ringtone {

    private long id = -1;
    private String remoteObjectKey = "";
    private RemoteMusicService remoteMusicService = RemoteMusicService.NO_SERVICE;

    public Ringtone(Uri ringtoneUri) {
        this(ringtoneUri.toString(), RemoteMusicService.NO_SERVICE);
    }

    public Ringtone(String remoteObjectKey, RemoteMusicService remoteMusicService) {
        this.remoteObjectKey = remoteObjectKey;
        this.remoteMusicService = remoteMusicService;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRemoteObjectKey() {
        return remoteObjectKey;
    }

    public void setRemoteObjectKey(String remoteObjectKey) {
        this.remoteObjectKey = remoteObjectKey;
    }

    public RemoteMusicService getRemoteMusicService() {
        return remoteMusicService;
    }

    public void setRemoteMusicService(RemoteMusicService remoteMusicService) {
        this.remoteMusicService = remoteMusicService;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();

        if (getId() != -1) {
            values.put(BebopContract.RingtonesColumns._ID, getId());
        }
        values.put(BebopContract.RingtonesColumns.REMOTE_OBJECT_KEY, getRemoteObjectKey());
        values.put(BebopContract.RingtonesColumns.MUSIC_SERVICE, getRemoteMusicService().ordinal());

        return values;
    }

    public static Ringtone createDefault() {
        return new Ringtone(BebopContract.AlarmsColumns.NO_RINGTONE_URI);
    }
}
