package com.hem.cleartouchmedia;

import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hem.cleartouchmedia.networking.RetrofitAPIInerface;
import com.hem.cleartouchmedia.networking.RetrofitClient;
import com.hem.cleartouchmedia.response.DeviceStatusCheckReponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppService extends Service {
    private String TAG = AppService.class.getCanonicalName();
    String DeviceID;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Call your API here

        DeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<< onStartCommand DeviceID "+DeviceID);
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        FirebaseApp.initializeApp(this);

        DeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<< onTaskRemoved " + DeviceID);

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;

        if (info != null && info.isConnected()) {
            RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
            Call<DeviceStatusCheckReponse> call = apiService.getDeviceStatusCheckResponse(
                    FirebaseInstanceId.getInstance().getToken(),
                    DeviceID,
                    "onTaskRemoved",
                    "AppService",
                    "0");

            call.enqueue(new Callback<DeviceStatusCheckReponse>() {
                @Override
                public void onResponse(Call<DeviceStatusCheckReponse> call, Response<DeviceStatusCheckReponse> response) {
                    if (response.isSuccessful()) {
                        DeviceStatusCheckReponse deviceStatusCheckReponse = response.body();
                        if (deviceStatusCheckReponse != null && deviceStatusCheckReponse.getStatus().equalsIgnoreCase("1")) {
                            Log.d(TAG, "Response of DevicePushStatusReponse AppService success: " + deviceStatusCheckReponse.getMessage());
                        } else {
                            Log.d(TAG, "Response of DevicePushStatusReponse AppService failed: " + deviceStatusCheckReponse.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<DeviceStatusCheckReponse> call, Throwable t) {
                    Log.d(TAG, "Response of DevicePushStatusReponse AppService onFailure: " + t.getMessage());
                }
            });
        } else {
            Log.d(TAG, "<< Response of DevicePushStatusReponse AppService offline ");
        }
    }

    @Override
    public void onDestroy() {
        // This method is called when the service is stopped.
        // It often happens when the application is killed.
        Log.d("Application", "Application killed");
        DeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<< onDestroy "+DeviceID);
//        fetchData("onTaskRemoved","0");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<< onBind");
        return null;
    }
}
