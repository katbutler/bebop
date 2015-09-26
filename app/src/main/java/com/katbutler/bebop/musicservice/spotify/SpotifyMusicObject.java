package com.katbutler.bebop.musicservice.spotify;

import com.katbutler.bebop.musicservice.RemoteMusicObject;
import com.katbutler.bebop.musicservice.RemoteMusicServiceType;
import com.katbutler.bebop.utils.BebopLog;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayConfig;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

/**
 * TODO: Write Class description
 */
public class SpotifyMusicObject extends RemoteMusicObject<PlayConfig> implements PlayerNotificationCallback, ConnectionStateCallback, SpotifyConstants {

    private static final String TAG = SpotifyMusicObject.class.getSimpleName();

    private Player mPlayer;
    private SpotifyPreferences mPreferences;

    @Override
    public PlayConfig getRemoteObject() {
        return PlayConfig.createFor(getKey());
    }

    @Override
    public void onPlay(final PlayConfig playConfig) {
        mPreferences = SpotifyPreferences.getInstance();
        Config playerConfig = new Config(getContext(), mPreferences.getAccessToken(), CLIENT_ID);
        mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
            @Override
            public void onInitialized(Player player) {
                mPlayer.addConnectionStateCallback(SpotifyMusicObject.this);
                mPlayer.addPlayerNotificationCallback(SpotifyMusicObject.this);
                mPlayer.play(playConfig);
//                        "spotify:track:2TpxZ7JUBn3uw46aR7qd6V"  "spotify:track:1244xKUG27TnmQhUJlo3gU"
//                        "spotify:track:4VqPOruhp5EdPBeR92t6lQ"
//                        PlayConfig playConfig = PlayConfig.createFor("spotify:album:0eFHYz8NmK75zSplL5qlfM");
//                        playConfig.withTrackIndex(7);
//                        mPlayer.play(playConfig);
            }

            @Override
            public void onError(Throwable throwable) {
                BebopLog.e(TAG, "Could not initialize player: " + throwable.getMessage());
            }
        });
    }

    @Override
    public void onStop() {
        if (!mPlayer.isShutdown()) {
            mPlayer.pause();
            mPlayer.shutdownNow();
        }
    }


    @Override
    public void onLoggedIn() {
        BebopLog.d(TAG, "User logged in");
    }

    @Override
    public void onLoggedOut() {
        BebopLog.d(TAG, "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        error.printStackTrace();
        BebopLog.d(TAG, "Login failed", error);
    }

    @Override
    public void onTemporaryError() {
        BebopLog.d(TAG, "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        BebopLog.d(TAG, "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        BebopLog.d(TAG, "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        BebopLog.d(TAG, "Playback error received: " + errorType.name());
    }

}
