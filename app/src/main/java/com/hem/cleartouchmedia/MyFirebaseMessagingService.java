package com.hem.cleartouchmedia;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hem.cleartouchmedia.activities.CompositionDBActivity;
import com.hem.cleartouchmedia.model.CompositionLayoutDetailViewModel;
import com.hem.cleartouchmedia.utilities.ApplicationPreferences;
import com.hem.cleartouchmedia.utilities.Config;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.MainCoroutineDispatcher;


public class MyFirebaseMessagingService extends FirebaseMessagingService
{
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private static int count = 0;

    private NotificationCompat.Builder mBuilder;
    public static final String NOTIFICATION_CLEAR_TOUCH_CHANNEL_ID = "CLEAR TOUCH CHANNEL ID";
    public static final String NOTIFICATION_CLEAR_TOUCH_CHANNEL_NAME = "CLEAR TOUCH CHANNEL";

    public boolean isAppIsInForground;
    Context context;

    Uri uri;
    long[] v;

    @Override
    public void onNewToken(String token)
    {
        super.onNewToken(token);
        Log.e("NEW_TOKEN", token);
        // Saving reg id to shared preferences
        storeRegIdInPref(token);

        // sending reg id to your server
        sendRegistrationToServer(token);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", token);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    @SuppressLint("WrongThread")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

        FirebaseMessaging.getInstance().subscribeToTopic("topicName");

        ApplicationPreferences.getInstance(getApplicationContext()).setNotificationReceive(true);
        Intent i = new Intent().setClass(getApplicationContext(), SplashActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        startActivity(i);

//        startActivity(new Intent(getApplicationContext(), SplashActivity.class));
//        startActivity(new Intent(getApplicationContext(), CompositionDBActivity.class));
        Log.d(TAG, "<<<<<<<I am in onMessageReceived method ");

        remoteMessage.getOriginalPriority();
        Log.d(TAG, "<<<<<<<Notification Message: >>" + remoteMessage);
        if (remoteMessage == null)
            return;

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0)
        {
            Log.e(TAG, "Data Payload data size : " + remoteMessage.getData().size());
            try
            {
                Log.e(TAG, "Data Payload title : " + remoteMessage.getData().get("title"));
                Log.e(TAG, "Data Payload body : " + remoteMessage.getData().get("body"));

                getNewsNotification(remoteMessage.getData().get("title"),
//                 Html.fromHtml(jsoupDetailString, Html.FROM_HTML_MODE_LEGACY).toString()
                remoteMessage.getData().get("body"),
                remoteMessage.getData().get("title"),
                remoteMessage.getData().get("title"));

            }
            catch (Exception e)
            {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void getNewsNotification(String title, String body, String package_name, String message)
    {
        Log.d(TAG, "notification  ...");
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant")
            NotificationChannel notificationChannel=new NotificationChannel("my_notification","n_channel",NotificationManager.IMPORTANCE_MAX);
            notificationChannel.setDescription("description");
            notificationChannel.setName("Channel Name");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setPriority(NotificationManager.IMPORTANCE_MAX)
                .setOnlyAlertOnce(true)
                .setChannelId("my_notification")
                .setColor(Color.parseColor("#3F5996"));
        //.setProgress(100,50,false);
        notificationManager.notify(0, notificationBuilder.build());
    }

    @Override
    public void onSendError(String s, Exception e)
    {
        Log.e(TAG, "<<<<<<<Exception: firebase msg " + e.getMessage());
        super.onSendError(s, e);
    }

    private void sendRegistrationToServer(final String token)
    {
        // sending gcm token to server
        Log.e(TAG, "<<<<<<<sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.apply();
    }
}

