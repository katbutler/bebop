package com.katbutler.bebop;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

/**
 * Created by kat on 15-09-06.
 */
public class BebopApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JodaTimeAndroid.init(this);
    }
}
