package com.katbutler.bebop.utils;

import android.util.Log;

/**
 * Utility to help with logging in the Bebop Application
 */
public final class BebopLog {

    /**
     * The maximum allowed length of a tag in the Android System
     */
    private static final int MAX_TAG_LENGTH = 23;

    /**
     * The value to prefix on every log TAG
     */
    private static final String TAG_PREFIX = "Bebop";

    /**
     * Always log at DEBUG level on eng/userdebug builds
     */
    public final static boolean DEBUG = false;//"eng".equals(android.os.Build.TYPE) || "userdebug".equals(android.os.Build.TYPE);

    /**
     * Private constructor to prevent creating instances
     */
    private BebopLog() {

    }

    /**
     * Get a Default TAG name if no tag is supplied
     * @return
     */
    private static String getDefaultTagName() {
        return TAG_PREFIX;
    }

    /**
     * Get the Tag name with the postfix appended.
     * Truncate all characters over the {@link #MAX_TAG_LENGTH} length.
     * @param postfix The TAG to postfix with
     * @return The TAG value
     */
    private static String getTagName(String postfix) {
        String tagName = TAG_PREFIX + postfix;
        return tagName.length() > MAX_TAG_LENGTH ? tagName.substring(0, 21) + "\u2026" : tagName;
    }

    public static void v(String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.VERBOSE)) {
            Log.v(getDefaultTagName(), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void v(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.VERBOSE)) {
            Log.v(getTagName(tag), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void d(String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.DEBUG)) {
            Log.d(getDefaultTagName(), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void d(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.DEBUG)) {
            Log.d(getTagName(tag), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void i(String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.INFO)) {
            Log.i(getDefaultTagName(), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void i(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.INFO)) {
            Log.i(getTagName(tag), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void w(String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.WARN)) {
            Log.w(getDefaultTagName(), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void w(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.WARN)) {
            Log.w(getTagName(tag), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void e(String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.ERROR)) {
            Log.e(getDefaultTagName(), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void e(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.ERROR)) {
            Log.e(getTagName(tag), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void e(String message, Exception e) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.ERROR)) {
            Log.e(getDefaultTagName(), message, e);
        }
    }

    public static void e(String tag, String message, Exception e) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.ERROR)) {
            Log.e(getTagName(tag), message, e);
        }
    }

    public static void wtf(String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.ASSERT)) {
            Log.wtf(getDefaultTagName(), args.length == 0 ? message : String.format(message, args));
        }
    }

    public static void wtf(String tag, String message, Object... args) {
        if (DEBUG || Log.isLoggable(getDefaultTagName(), Log.ASSERT)) {
            Log.wtf(getTagName(tag), args.length == 0 ? message : String.format(message, args));
        }
    }
}