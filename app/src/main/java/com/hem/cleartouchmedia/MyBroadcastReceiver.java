package com.hem.cleartouchmedia;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hem.cleartouchmedia.networking.RetrofitAPIInerface;
import com.hem.cleartouchmedia.networking.RetrofitClient;
import com.hem.cleartouchmedia.response.DeviceStatusCheckReponse;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by LENOVO on 20/12/2018.
 */

public class MyBroadcastReceiver extends BroadcastReceiver
{
    private static final String TAG = MyBroadcastReceiver.class.getSimpleName();

    public static final String TAG_NOTIFICATION = "NOTIFICATION_MESSAGE";
    public static final String CHANNEL_ID = "channel_1111";
    public static final int NOTIFICATION_ID = 111;

    private static final int MY_PERMISSIONS_REQUEST_BOOT_COMPLETED = 100;

    Context context;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        this.context = context;
        // Use like this:
//        Toast.makeText(context, "Broadcast was triggered", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_BOOT_COMPLETED)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted, request it
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.RECEIVE_BOOT_COMPLETED},
                        MY_PERMISSIONS_REQUEST_BOOT_COMPLETED);
            }
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            Toast.makeText(context.getApplicationContext(), "android version 10 or more Action: " +
                    intent.getAction(), Toast.LENGTH_SHORT).show();
            Log.d("BootReceiver section", "<<<<<<<<<<<<<<<android version 10 or more onReceive Action: " +
                    intent.getAction());

            startActivityNotification(
                    context,
                    NOTIFICATION_ID,
                    context.getResources().getString(R.string.open_app),
                    context.getResources().getString(R.string.click_app)
            );
        }
        else
        {
            /*Sent when the user is present after
             * device wakes up (e.g when the keyguard is gone)
             * */
            if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
                Toast.makeText(context, "Broadcast was triggered in user present", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.setClass(context, SplashActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }

            /*Device is shutting down. This is broadcast when the device
             * is being shut down (completely turned off, not sleeping)
             * */
            else if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
                Toast.makeText(context, "Broadcast was triggered in shut down", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.setClass(context, SplashActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }

            else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON))
            {
                deviceStatus("1", "onScreenOn");
                Toast.makeText(context, "Broadcast was triggered in Screen On", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();

                intent1.setClass(context, SplashActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }

            else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
            {
                Toast.makeText(context, "Broadcast was triggered in Screen Off", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.setClass(context, SplashActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }

            else if(Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
            {
                Toast.makeText(context, "Broadcast was triggered in Boot Completed", Toast.LENGTH_SHORT).show();
                Intent activityIntent = new Intent(context, SplashActivity.class);
                activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(activityIntent);
            }

            String action = intent.getAction();
            if (action.equals("com.hem.cleartouchmedia.AN_INTENT"))
            {
                Log.d(TAG, "Explicit Broadcast was triggered");
            }

            if (("android.net.conn.CONNECTIVITY_CHANGE").equals(action))
            {
                Log.d(TAG, "Implicit Broadcast was triggered using registerReceiver");
            }
        }

        deviceStatus("0", "onScreenOff");

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            Toast.makeText(context, "Broadcast was triggered in Screen Off", Toast.LENGTH_SHORT).show();
            deviceStatus("0", "onScreenOff");
        }

        if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            Toast.makeText(context, "Broadcast was triggered in shut down", Toast.LENGTH_SHORT).show();
            deviceStatus("0", "onShutDown");
        }
    }


    // notification method to support opening activities on Android 10
    public static void startActivityNotification (Context context, int notificationID, String title, String message) {

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //Create GPSNotification builder
        NotificationCompat.Builder mBuilder;

        //Initialise ContentIntent

        Intent ContentIntent = new Intent(context, SplashActivity.class);
        ContentIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            flags |= PendingIntent.FLAG_IMMUTABLE;
        }
        PendingIntent ContentPendingIntent = PendingIntent.getActivity(
                context,
                0,
                ContentIntent,
                flags
        );

        mBuilder = new NotificationCompat . Builder (context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(message)
                .setColor(context.getResources().getColor(R.color.purple_200))
                .setAutoCancel(true)
                .setContentIntent(ContentPendingIntent)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Activity Opening Notification",
                    NotificationManager.IMPORTANCE_HIGH
            );
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setDescription("Activity opening notification");

            mBuilder.setChannelId(CHANNEL_ID);

            Objects.requireNonNull(mNotificationManager).createNotificationChannel(mChannel);
        }
        Objects.requireNonNull(mNotificationManager).notify(
                TAG_NOTIFICATION, notificationID,
                mBuilder.build()
        );
    }

    private void deviceStatus(String status, String method) {
        String DeviceID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        ConnectivityManager manager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            manager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        }
        NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
        if (info != null && info.isConnected())
        {
            RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
            Call<DeviceStatusCheckReponse> call = apiService.getDeviceStatusCheckResponse(
                    FirebaseInstanceId.getInstance().getToken(),
                    DeviceID,
                    method,
                    "broadcast",
                    status);
            call.enqueue(new retrofit2.Callback<DeviceStatusCheckReponse>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<DeviceStatusCheckReponse> call, Response<DeviceStatusCheckReponse> response) {
                    if(response.isSuccessful()) {
                        DeviceStatusCheckReponse deviceStatusCheckReponse = response.body();
                        if(deviceStatusCheckReponse.getStatus().equalsIgnoreCase("1"))
                        {
                            Log.d(TAG, "<< Response of DevicePushStatusReponse Broadcast success "+deviceStatusCheckReponse.getMessage());
                        }
                        else
                        {
                            Log.d(TAG, "<< Response of DevicePushStatusReponse Broadcast failed ");
                        }
                    }
                }
                @Override
                public void onFailure(Call<DeviceStatusCheckReponse> call, Throwable t) {
                    Log.d(TAG, "onFailure Broadcast: "+t.getMessage());
                }
            });
        }
        else
        {
            Log.d(TAG, "<< Response of compositionLayoutOneResponse Broadcast offline ");
        }
    }
}
