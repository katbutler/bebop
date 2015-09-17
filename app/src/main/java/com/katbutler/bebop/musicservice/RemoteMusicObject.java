package com.katbutler.bebop.musicservice;

import android.content.Context;

/**
 * Created by kat on 15-09-11.
 */
public abstract class RemoteMusicObject<T> {
    private String key;
    private String data;
    private RemoteMusicServiceType serviceType;
    private Context mContext;

    public static RemoteMusicObject createRemoteMusicObject(String key, String data, RemoteMusicServiceType type) {
        try {
            RemoteMusicObject obj = type.getRemoteMusicClass().newInstance();
            obj.setKey(key);
            obj.setData(data);
            obj.setServiceType(type);
            return obj;

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public abstract T getRemoteObject();

    public abstract void onPlay(T obj);

    public abstract void onStop();

    public void init(Context context) {
        mContext = context;
    }

    public void stop() {
        onStop();
    }

    public void play() {
        onPlay(getRemoteObject());
    }

    public Context getContext() {
        return mContext;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public RemoteMusicServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(RemoteMusicServiceType serviceType) {
        this.serviceType = serviceType;
    }
}
