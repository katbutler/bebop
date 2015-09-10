package com.katbutler.bebop.model;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;

import com.katbutler.bebop.musicservice.RemoteMusicObject;
import com.katbutler.bebop.musicservice.RemoteMusicServiceType;
import com.katbutler.bebop.musicservice.local.LocalMusicObject;
import com.katbutler.bebop.provider.BebopContract;
import com.katbutler.bebop.utils.BebopLog;

/**
 * Ringtone Model for remote services
 */
public class Ringtone implements BebopContract.RingtonesColumns {

    /**
     * Ringtones start with an invalid id when it hasn't been saved to the database.
     */
    public static final long INVALID_ID = -1;

    private long id = INVALID_ID;
    private RemoteMusicObject remoteMusicObject;
    private RemoteMusicServiceType remoteMusicServiceType = RemoteMusicServiceType.NO_SERVICE;

    public Ringtone(RemoteMusicObject remoteMusicObject) {
        this(INVALID_ID, remoteMusicObject, remoteMusicObject.belongsToRemoteMusicService());
    }

    public Ringtone(long ringtoneId, RemoteMusicObject remoteMusicObject, RemoteMusicServiceType remoteMusicServiceType) {
        this.id = ringtoneId;
        this.remoteMusicObject = remoteMusicObject;
        this.remoteMusicServiceType = remoteMusicServiceType;
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

    public RemoteMusicObject getRemoteMusicObject() {
        return remoteMusicObject;
    }

    public void setRemoteMusicObject(RemoteMusicObject remoteMusicObject) {
        this.remoteMusicObject = remoteMusicObject;
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
        values.put(BebopContract.RingtonesColumns.REMOTE_OBJECT_KEY, getRemoteMusicObject().getKey());
        values.put(BebopContract.RingtonesColumns.MUSIC_SERVICE, getRemoteMusicServiceType().ordinal());

        return values;
    }

    public static Ringtone createDefault() {
        return new Ringtone(new LocalMusicObject(BebopContract.AlarmsColumns.NO_RINGTONE_URI));
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
}
