package com.hem.cleartouchmedia.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

import com.hem.cleartouchmedia.response.TwitterResponse;
import com.hem.cleartouchmedia.response.WeatherResponse;

import java.util.Set;

public class ApplicationConstants {

    public static final String DEFAULT = "default";

   public static final String BASE_URL = "https://dev.cleartouchmedia.com/api/";
    public static final String BUILT_STATUS = "DEV";

  //  public static final String BASE_URL = "https://demo.cleartouchmedia.com/api/";
  //  public static final String BUILT_STATUS = "DEMO";

    public static String IS_LOGGED = "IsLoggedIn";
    public static String IS_DOWNLOAD = "IsDownload";
    public static String IS_NOTIFICATION_RECEIVE = "IsNotificationReceive";
    public static String IS_NOTIFICATION_ON = "IsNotificationOn";
    public static String SCREEN_ID = "screen_id";
    public static String BG_MUSIC_STR = "bg";
    public static String BG_MUSIC_STATUS = "bg_status";
    public static String BG_MUSIC_COUNT = "count";
    public static String TWITTER_RESPONSE = "twitter_response";
    public static final String DEVICE_TOKEN = "DEVICE_TOKEN";
    public final static String  DEVICE_TOKEN_STATUS  = "DEVICE_TOKEN_STATUS";
    public static final String APP_ID ="937221464581";
    public static boolean IS_FOREGROUND = false;
    public static boolean IS_HOME_PRESSED = false;
    public static boolean IS_DESTROY = false;
    public static Context APPLICATION_CONTEXT = null;
    public static String DEVICE_ID = "";

    public static SharedPreferences mSharedPreferences ;

    public static TwitterResponse twitterResponse;

    public static WeatherResponse weatherResponse;

    public static TwitterResponse getGlobalTwitterResponse() {
        return twitterResponse;
    }

    public static void setGlobalTwitterResponse(TwitterResponse globalVariableTwo) {
        twitterResponse = globalVariableTwo;
    }

    public static WeatherResponse getGlobalWeatherResponse() {
        return weatherResponse;
    }

    public static void setGlobalWeatherResponse(WeatherResponse globalVariableOne) {
        weatherResponse = globalVariableOne;
    }

    /**
     * This method will save device token status in Preferences.
     * @param status : Status which to be save.
     */
    public static void setDeviceTokenStatus(boolean status)
    {
        if(mSharedPreferences!=null)
        {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putBoolean(DEVICE_TOKEN_STATUS, status);
            editor.commit();
        }
    }

    /**
     * This method will return device token in status.
     * @return : Status of device token
     */
    public static boolean getDeviceTokenStatus()
    {
        return mSharedPreferences.getBoolean(DEVICE_TOKEN_STATUS, true);
    }

    public static String getAndroidID()
    {
        String Device_ID = Settings.Secure.getString(APPLICATION_CONTEXT.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        return Device_ID;
    }
}
