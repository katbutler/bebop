package com.katbutler.bebop;

import android.app.Application;

import com.katbutler.bebop.musicservice.spotify.SpotifyPreferences;
import com.katbutler.bebop.utils.BebopLog;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by kat on 15-09-06.
 */
public class BebopApplication extends Application {

    private static final String TAG = BebopApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        BebopLog.v(TAG, "Application onCreate()");

        // Initialize JodaTime
        JodaTimeAndroid.init(this);

        // Initialize the Spotify Preferences with the Application
        SpotifyPreferences.init(this);
    }
}
