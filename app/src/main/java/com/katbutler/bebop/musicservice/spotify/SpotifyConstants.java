package com.katbutler.bebop.musicservice.spotify;

public interface SpotifyConstants {
    int AUTH_REQUEST_CODE = 7290;
    String CLIENT_ID = "662b643f936a4275846788656a7fc1f5";
    String REDIRECT_URI = "com-katbutler-bebop://callback";
    String[] SPOTIFY_SCOPES = new String[]{
            "user-read-private",
            "user-library-read",
            "playlist-read-collaborative",
            "playlist-read-private",
            "streaming"};
}
