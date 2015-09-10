package com.katbutler.bebop.musicservice;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Abstract class for Remote Music Objects.
 *
 * The subclasses on a RemoteMusicObject are responsible for
 * creating the correct Music Object types for a Music Service.
 */
public abstract class RemoteMusicObject<T> implements Parcelable {

    private String mKey;

    //region Constructors and Factory Methods
    /**
     * Factory method to create a {@link RemoteMusicObject} for the specific Music Service
     * @param key They key String from the database to create the {@link RemoteMusicObject} with.
     * @param remoteMusicServiceType The Music Service Type to help create the {@link RemoteMusicObject} instance
     * @return a new {@link RemoteMusicObject} for the Music Service Type provided.
     */
    public static RemoteMusicObject createRemoteMusicObject(String key, RemoteMusicServiceType remoteMusicServiceType) {
        return remoteMusicServiceType.createRemoteMusicObject(key);
    }

    protected RemoteMusicObject(String key) {
        setKey(key);
    }

    protected RemoteMusicObject() {

    }
    //endregion

    //region Abstract Methods
    public abstract T getRemoteObject();

    public abstract RemoteMusicServiceType belongsToRemoteMusicService();

    public abstract void onPlay(T remoteObject);
    //endregion

    //region Logic Methods
    public void play() {
        onPlay(getRemoteObject());
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getKey() {
        return mKey;
    }
    //endregion

    //region Parcelable Implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mKey);
        dest.writeInt(belongsToRemoteMusicService().ordinal());
    }

    public static final Creator<RemoteMusicObject> CREATOR = new Creator<RemoteMusicObject>() {
        @Override
        public RemoteMusicObject createFromParcel(Parcel in) {
            String key = in.readString();
            RemoteMusicServiceType remoteMusicServiceType = RemoteMusicServiceType.remoteMusicService(in.readInt());
            return createRemoteMusicObject(key, remoteMusicServiceType);
        }

        @Override
        public RemoteMusicObject[] newArray(int size) {
            return new RemoteMusicObject[size];
        }
    };
    //endregion
}
