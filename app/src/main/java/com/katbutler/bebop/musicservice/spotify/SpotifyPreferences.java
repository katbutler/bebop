package com.katbutler.bebop.musicservice.spotify;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.katbutler.bebop.musicservice.RemoteMusicPreferences;
import com.katbutler.bebop.utils.BebopLog;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.joda.time.DateTime;

/**
 * TODO: Write class description
 */
public class SpotifyPreferences extends RemoteMusicPreferences implements SpotifyConstants {

    private static final String PREF_NAME = "pref_spotfiy";
    private static final String PREF_ACCESS_TOKEN = "pref_access_token";
    private static final String PREF_EXPIRY_DATE = "pref_expiry_date";


    private static SpotifyPreferences instance;

    private AuthenticationResponse mResponse;
    private String mAccessToken;
    private DateTime mTokenExpiryDateTime;

    public static SpotifyPreferences init(Context context) {
        if (instance == null) {
            instance = new SpotifyPreferences(context.getApplicationContext());
        }
        return instance;
    }

    public static SpotifyPreferences getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("Must call SpotifyPreferences.init(context) first.");
        }
        return instance;
    }

    private SpotifyPreferences(Context applicationContext) {
        super(applicationContext);
        load();
    }

    public void login(Activity activity) {
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(SPOTIFY_SCOPES);
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(activity, AUTH_REQUEST_CODE, request);
    }

    public void logout() {
        AuthenticationClient.logout(getContext());
        mAccessToken = "";
        saveState();
    }

    public void onAuthResult(int resultCode, Intent intent) {
        mResponse = AuthenticationClient.getResponse(resultCode, intent);
        if (mResponse.getType() == AuthenticationResponse.Type.TOKEN) {
            mAccessToken = mResponse.getAccessToken();
            mTokenExpiryDateTime = DateTime.now().plusSeconds(mResponse.getExpiresIn());
            saveState();

        } else if(mResponse.getType() == AuthenticationResponse.Type.ERROR) {
            BebopLog.e(mResponse.getError());
        }

    }

    public boolean hasAccessToken() {
        return getAccessToken() != null && getAccessToken().length() > 0;
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public DateTime getTokenExpiryDateTime() {
        return mTokenExpiryDateTime;
    }

    private void saveState() {
        SharedPreferences.Editor edit = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        edit.putString(PREF_ACCESS_TOKEN, getAccessToken());
        edit.putLong(PREF_EXPIRY_DATE, getTokenExpiryDateTime().getMillis());
        edit.apply();
    }

    private void load() {
        SharedPreferences prefs = getContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mAccessToken = prefs.getString(PREF_ACCESS_TOKEN, "");
        long millis = prefs.getLong(PREF_EXPIRY_DATE, -1);
        if (millis > 0) {
            mTokenExpiryDateTime = new DateTime(millis);
        }
    }
}
