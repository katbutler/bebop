package com.katbutler.bebop.musicservice.local;

import android.content.Context;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;

import com.katbutler.bebop.musicservice.RemoteMusicObject;

import java.io.IOException;


/**
 * Created by kat on 15-09-11.
 */
public class LocalMusicObject extends RemoteMusicObject<Uri> {

    private MediaPlayer mMediaPlayer;

    @Override
    public Uri getRemoteObject() {
        return Uri.parse(getKey());
    }

    @Override
    public void onPlay(Uri uri) {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
        try {
            String fileInfo = getRingtonePathFromContentUri(getContext(), uri);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(getContext(), Uri.parse(fileInfo));
        } catch (IllegalArgumentException | IllegalStateException | SecurityException | IOException e) {
            e.printStackTrace();
        }
        try {
            mMediaPlayer.prepare();
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }

    @Override
    public void onStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
        }
    }

    public static String getRingtonePathFromContentUri(Context context,
                                                       Uri contentUri) {
        String[] proj = { MediaStore.Audio.Media.DATA };
        Cursor ringtoneCursor = context.getContentResolver().query(contentUri,
                proj, null, null, null);
        ringtoneCursor.moveToFirst();

        String path = ringtoneCursor.getString(0);

        ringtoneCursor.close();
        return path;
    }
}
