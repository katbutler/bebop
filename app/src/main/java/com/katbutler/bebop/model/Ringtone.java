package com.katbutler.bebop.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.katbutler.bebop.musicservice.RemoteMusicServiceType;
import com.katbutler.bebop.provider.BebopContract;
import com.katbutler.bebop.utils.BebopLog;

/**
 * Ringtone Model for remote services
 */
public class Ringtone implements BebopContract.RingtonesColumns, Parcelable {

    /**
     * Ringtones start with an invalid id when it hasn't been saved to the database.
     */
    public static final long INVALID_ID = -1;

    private long id = INVALID_ID;
    private String remoteObjectKey = "";
    private RemoteMusicServiceType remoteMusicServiceType = RemoteMusicServiceType.NO_SERVICE;
    private String remoteData;

    private Ringtone(Parcel in) {
        id = in.readLong();
        remoteObjectKey = in.readString();
        remoteData = in.readString();
        int serviceTypeOrdinal = in.readInt();
        remoteMusicServiceType = RemoteMusicServiceType.remoteMusicService(serviceTypeOrdinal);
    }

    public Ringtone(Uri ringtoneUri) {
        this(ringtoneUri.toString(), RemoteMusicServiceType.NO_SERVICE);
    }

    public Ringtone(String remoteObjectKey, RemoteMusicServiceType remoteMusicServiceType) {
        this.remoteObjectKey = remoteObjectKey;
        this.remoteMusicServiceType = remoteMusicServiceType;
    }

    public Ringtone(long ringtoneId, String remoteObjKey, RemoteMusicServiceType service) {
        this(remoteObjKey, service);
        setId(ringtoneId);
    }

    public boolean hasId() {
        return getId() != INVALID_ID;
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

    public String getRemoteData() {
        return remoteData;
    }

    public void setRemoteData(String remoteData) {
        this.remoteData = remoteData;
    }

    public void setRemoteObjectKey(String remoteObjectKey) {
        this.remoteObjectKey = remoteObjectKey;
    }

    public RemoteMusicServiceType getRemoteMusicServiceType() {
        return remoteMusicServiceType;
    }

    public void setRemoteMusicServiceType(RemoteMusicServiceType remoteMusicServiceType) {
        this.remoteMusicServiceType = remoteMusicServiceType;
    }

    public static Uri getUri(long alarmId) {
        return ContentUris.withAppendedId(BebopContract.AlarmsColumns.CONTENT_URI, alarmId);
    }

    public static long getId(Uri contentUri) {
        return ContentUris.parseId(contentUri);
    }

    public ContentValues createContentValues() {
        ContentValues values = new ContentValues();

        if (getId() != -1) {
            values.put(BebopContract.RingtonesColumns._ID, getId());
        }
        values.put(REMOTE_OBJECT_KEY, getRemoteObjectKey());
        values.put(MUSIC_SERVICE, getRemoteMusicServiceType().ordinal());
        values.put(REMOTE_DATA, getRemoteData());

        return values;
    }

    public static Ringtone createDefault() {
        return new Ringtone(BebopContract.AlarmsColumns.NO_RINGTONE_URI);
    }

    /**
     * Insert or Update Ringtone based on whether the Ringtone ID is valid.
     * @param resolver The ContentResolver to operate on
     * @param ringtone The Ringtone to upsert
     * @return The Ringtone after inserting or updating with a valid ID set
     */
    public static Ringtone upsertRingtone(ContentResolver resolver, Ringtone ringtone) {
        if (ringtone.hasId()) {
            return updateRingtone(resolver, ringtone);
        }
        return insertRingtone(resolver, ringtone);
    }

    /**
     * Insert a new Ringtone into the BebopProvider. The Ringtone must have an
     * invalid ID when inserting.
     * @param resolver The ContentResolver to operate on
     * @param ringtone The Ringtone to insert
     * @return The Ringtone with the newly created ID from the ContentProvider
     */
    public static Ringtone insertRingtone(ContentResolver resolver, Ringtone ringtone) {
        Uri ringtoneUri = resolver.insert(CONTENT_URI, ringtone.createContentValues());
        if (ringtoneUri == null) {
            BebopLog.wtf("The ringtone could not be inserted! Null ringtoneUri");
        } else {
            ringtone.setId(getId(ringtoneUri));
        }
        return ringtone;
    }

    /**
     * Update a Ringtone in the BebopProvider. The Ringtone must have a valid
     * ID when updating.
     * @param resolver The ContentResolver to operate on
     * @param ringtone The Ringtone to update
     * @return The Ringtone object as passed into this function. If the number
     * of updated rows does not equal 1 a WTF will occur.
     */
    public static Ringtone updateRingtone(ContentResolver resolver, Ringtone ringtone) {
        int updated = resolver.update(getUri(ringtone.getId()), ringtone.createContentValues(), null, null);
        if (updated != 1) {
            BebopLog.wtf("One ringtone should have been updated but instead %d ringtones were updated.", updated);
        }
        return ringtone;
    }

    /**
     * Delete a Ringtone in the BebopProvider. The Ringtone must have a valid
     * ID when deleting
     * @param resolver The ContentResolver to operate on
     * @param ringtone The Ringtone to delete
     * @return The Ringtone object as passed into this function.
     */
    public static Ringtone deleteRingtone(ContentResolver resolver, Ringtone ringtone) {
        throw new UnsupportedOperationException("deleteRingtone has not been implemented");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getRemoteObjectKey());
        dest.writeString(getRemoteData() == null ? "" : getRemoteData());
        dest.writeInt(getRemoteMusicServiceType().ordinal());
    }

    public static final Creator<Ringtone> CREATOR = new Creator<Ringtone>() {
        @Override
        public Ringtone createFromParcel(Parcel in) {
            return new Ringtone(in);
        }

        @Override
        public Ringtone[] newArray(int size) {
            return new Ringtone[size];
        }
    };
}
