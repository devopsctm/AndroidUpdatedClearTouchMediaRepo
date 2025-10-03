package com.hem.cleartouchmedia.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class ApplicationPreferences {

    private static final String PREF_FILE = "com.hem.cleartouchmedia.prefs";
    private static final String KEY_FCM_TOKEN = "fcm_token";

    private static volatile ApplicationPreferences sInstance;
    private final SharedPreferences mSharedPreferences;

    private ApplicationPreferences(Context context) {
        // Use app context to avoid leaking an Activity
        mSharedPreferences = context.getApplicationContext()
                .getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
    }

    public static ApplicationPreferences getInstance(Context context) {
        if (sInstance == null) {
            synchronized (ApplicationPreferences.class) {
                if (sInstance == null) {
                    sInstance = new ApplicationPreferences(context);
                }
            }
        }
        return sInstance;
    }

    // ---------------- FCM token ----------------
    public void setFcmToken(String token) {
        mSharedPreferences.edit()
                .putString(KEY_FCM_TOKEN, token == null ? "" : token)
                .apply();
    }

    public String getFcmToken() {
        return mSharedPreferences.getString(KEY_FCM_TOKEN, "");
    }

    // ---------------- Notification receive flag ----------------
    public void setNotificationReceive(Boolean notificationReceive) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putBoolean(ApplicationConstants.IS_NOTIFICATION_RECEIVE, notificationReceive);
        e.commit();
    }

    public boolean getNotificationReceive() {
        return mSharedPreferences.getBoolean(ApplicationConstants.IS_NOTIFICATION_RECEIVE, false);
    }

    // ---------------- Logged-in flag ----------------
    public void setIsLoggedin(Boolean islogged) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putBoolean(ApplicationConstants.IS_LOGGED, islogged);
        e.commit();
    }

    public boolean getIsLoggedin() {
        return mSharedPreferences.getBoolean(ApplicationConstants.IS_LOGGED, false);
    }

    // ---------------- Twitter response ----------------
    public void setTwitterResponse(String response) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(ApplicationConstants.TWITTER_RESPONSE, response);
        e.commit();
    }

    public String getTwitterResponse() {
        return mSharedPreferences.getString(ApplicationConstants.TWITTER_RESPONSE, "");
    }

    // ---------------- Screen ID ----------------
    public void setScreenID(String screenID) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(ApplicationConstants.SCREEN_ID, screenID);
        e.commit();
    }

    public String getScreenID() {
        return mSharedPreferences.getString(ApplicationConstants.SCREEN_ID, "");
    }

    // ---------------- Background music URL ----------------
    public void setBackgroundMusic(String bg) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(ApplicationConstants.BG_MUSIC_STR, bg);
        e.commit();
    }

    public String getBackgroundMusic() {
        return mSharedPreferences.getString(ApplicationConstants.BG_MUSIC_STR, "");
    }

    // ---------------- Background music status ----------------
    public void setBackgroundMusicStatus(String bg) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(ApplicationConstants.BG_MUSIC_STATUS, bg);
        e.commit();
    }

    public String getBackgroundMusicStatus() {
        return mSharedPreferences.getString(ApplicationConstants.BG_MUSIC_STATUS, "");
    }

    // ---------------- Background music count ----------------
    public void setBackgroundMusicCount(int count) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putInt(ApplicationConstants.BG_MUSIC_COUNT, count);
        e.commit();
    }

    public int getBackgroundMusicCount() {
        return mSharedPreferences.getInt(ApplicationConstants.BG_MUSIC_COUNT, 0);
    }

    // ---------------- Download flag ----------------
    public void setIsDownload(Boolean isDownload) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putBoolean(ApplicationConstants.IS_DOWNLOAD, isDownload);
        e.commit();
    }

    public boolean getIsDownload() {
        return mSharedPreferences.getBoolean(ApplicationConstants.IS_DOWNLOAD, false);
    }

    // ---------------- Generic boolean KV ----------------
    public void setBooleanValue(String Tag, boolean token) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putBoolean(Tag, token);
        e.commit();
    }

    public boolean getBooleanValue(String Tag) {
        return mSharedPreferences.getBoolean(Tag, false);
    }
}
