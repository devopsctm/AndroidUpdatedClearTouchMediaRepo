package com.hem.cleartouchmedia.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ApplicationPreferences {
    private SharedPreferences mSharedPreferences;
    private static ApplicationPreferences mPreference;

    private ApplicationPreferences(Context context)
    {
        mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
    }

    public static ApplicationPreferences getInstance(Context context)
    {
        if (mPreference == null)
        {
            mPreference = new ApplicationPreferences(context);
        }
        return mPreference;
    }

    /**
     * Called to store the if the user is logged in or not.
     *
     * @param notificationReceive
     *            : True if user is loggede in.
     */
    public void setNotificationReceive(Boolean notificationReceive)
    {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putBoolean(ApplicationConstants.IS_NOTIFICATION_RECEIVE, notificationReceive);
        e.commit();
    }

    /**
     * Called to get if the user is logged in or not.
     *
     * @return true if user is logged in.
     */
    public boolean getNotificationReceive()
    {
        return mSharedPreferences.getBoolean(ApplicationConstants.IS_NOTIFICATION_RECEIVE, false);
    }

    /**
     * Called to store the if the user is logged in or not.
     *
     * @param islogged
     *            : True if user is loggede in.
     */
    public void setIsLoggedin(Boolean islogged)
    {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putBoolean(ApplicationConstants.IS_LOGGED, islogged);
        e.commit();
    }

    /**
     * Called to get if the user is logged in or not.
     *
     * @return true if user is logged in.
     */
    public boolean getIsLoggedin()
    {
        return mSharedPreferences.getBoolean(ApplicationConstants.IS_LOGGED, false);
    }

    /**
     * Called to store the if the user is logged in or not.
     *
     * @param response
     *            : True if user is set Twitter Response.
     */
    public void setTwitterResponse(String response)
    {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(ApplicationConstants.TWITTER_RESPONSE, response);
        e.commit();
    }

    /**
     * Called to get if the user is get Twitter Response in or not.
     *
     * @return true if user is get Twitter Response.
     */
    public String getTwitterResponse()
    {
        return mSharedPreferences.getString(ApplicationConstants.TWITTER_RESPONSE, "");
    }

    /**
     * Called to store the if the user have screen id or not.
     *
     * @param *setScreenID
     *            : value if user have screen id.
     **/
    public void setScreenID(String screenID)
    {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(ApplicationConstants.SCREEN_ID, screenID);
        e.commit();
    }

    /**
     * Called to get if the user have screen id or not.
     *
     * @return value if user have screen id.
     */
    public String getScreenID()
    {
        return mSharedPreferences.getString(ApplicationConstants.SCREEN_ID, "");
    }

    /**
     * Called to store the if the user have screen id or not.
     *
     * @param *setScreenID
     *            : value if user have screen id.
     **/
    public void setBackgroundMusic(String bg)
    {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(ApplicationConstants.BG_MUSIC_STR, bg);
        e.commit();
    }

    /**
     * Called to get if the user have screen id or not.
     *
     * @return value if user have screen id.
     */
    public String getBackgroundMusic()
    {
        return mSharedPreferences.getString(ApplicationConstants.BG_MUSIC_STR, "");
    }

    /**
     * Called to store the if the user have screen id or not.
     *
     * @param *setScreenID
     *            : value if user have screen id.
     **/
    public void setBackgroundMusicStatus(String bg)
    {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putString(ApplicationConstants.BG_MUSIC_STATUS, bg);
        e.commit();
    }

    /**
     * Called to get if the user have screen id or not.
     *
     * @return value if user have screen id.
     */
    public String getBackgroundMusicStatus()
    {
        return mSharedPreferences.getString(ApplicationConstants.BG_MUSIC_STATUS, "");
    }

    /**
     * Called to store the if the user have screen id or not.
     *
     * @param *setScreenID
     *            : value if user have screen id.
     **/
    public void setBackgroundMusicCount(int count)
    {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putInt(ApplicationConstants.BG_MUSIC_COUNT, count);
        e.commit();
    }

    /**
     * Called to get if the user have screen id or not.
     *
     * @return value if user have screen id.
     */
    public int getBackgroundMusicCount()
    {
        return mSharedPreferences.getInt(ApplicationConstants.BG_MUSIC_COUNT, 0);
    }

    /**
     * Called to store the if the user is logged in or not.
     *
     * @param islogged
     *            : True if user is loggede in.
     */
    public void setIsDownload(Boolean isDownload)
    {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putBoolean(ApplicationConstants.IS_DOWNLOAD, isDownload);
        e.commit();
    }

    /**
     * Called to get if the user is logged in or not.
     *
     * @return true if user is Download in.
     */
    public boolean getIsDownload()
    {
        return mSharedPreferences.getBoolean(ApplicationConstants.IS_DOWNLOAD, false);
    }

    /**
     * Called to store the if the permission or not.
     *
     * @param isPermission
     *            : True if user is loggede in.
     */
    public void setBooleanValue(String Tag, boolean token) {
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.putBoolean(Tag, token);
        e.commit();
    }

    /**
     * Called to get if the permission or not.
     *
     * @return true if user is Download in.
     */
    public boolean getBooleanValue(String Tag) {
        return mSharedPreferences.getBoolean(Tag, false);

    }
}
