package com.hem.cleartouchmedia;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hem.cleartouchmedia.activities.CompositionDBActivity;
import com.hem.cleartouchmedia.networking.RetrofitAPIInerface;
import com.hem.cleartouchmedia.networking.RetrofitClient;
import com.hem.cleartouchmedia.response.CompositionStatusCheckResponse;
import com.hem.cleartouchmedia.response.ScreenRegResponse;
import com.hem.cleartouchmedia.response.UpdateScreenRegiResponse;
import com.hem.cleartouchmedia.utilities.ApplicationPreferences;
import com.hem.cleartouchmedia.utilities.ImeiUtil;
import com.hem.cleartouchmedia.utilities.MacAddressUtil;
import com.hem.cleartouchmedia.utilities.PrivateIPUtil;
import com.hem.cleartouchmedia.utilities.PublicIPUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Enumeration;

import retrofit2.Call;
import retrofit2.Response;

public class ScreenActivity extends AppCompatActivity {
    private String TAG = SplashActivity.class.getCanonicalName();
    private static final int PERMISSION_REQUEST_CODE = 1001;
    TextView screenCode;
    String DeviceID;
    String IMEI;
    String AndroidID;
    String APK_Version;
    private String macAddress;
    String privateIpAddress;
    String publicIpAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        screenCode = findViewById(R.id.screen_code_txt);

        FirebaseApp.initializeApp(this);

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.READ_PHONE_STATE},
                    0);
        }
        DeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Get Android version
        int version = Build.VERSION.SDK_INT;
        String versionName = Build.VERSION.RELEASE;
        privateIpAddress = PrivateIPUtil.getPrivateIpAddress();
//        publicIpAddress = String.valueOf(new PublicIPUtil.GetPublicIP().execute());
        macAddress = MacAddressUtil.getMacAddress(this);
        IMEI = ImeiUtil.getImeiNumber(this);

        PublicIPUtil.getPublicIp(new PublicIPUtil.PublicIpCallback() {
            @Override
            public void onIpReceived(String ip) {
                publicIpAddress = ip;
                Log.d("Public IP", "Public IP Address: " + ip);
                Log.d(TAG, "<<<<<<<<<<<<<<<<<<Android Public IP Address: " + publicIpAddress);
            }

            @Override
            public void onError(String error) {
                Log.e("Public IP", "Error: " + error);
            }
        });

        Log.d(TAG, "<<<<<<<<<<<<<<<<<<Android Private IP Address: " + privateIpAddress);
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<Android Public IP Address: " + publicIpAddress);
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<Android MAC Address: "+macAddress);
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<Android Version: " + versionName + " (API Level " + version + ")");
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<Android macAddress: " + macAddress);
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<Android IMEI: " + IMEI);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            APK_Version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        getRegisterScreenResponse(DeviceID);
    }

    public static String getIMEINumber(@NonNull final Context context)
            throws SecurityException, NullPointerException {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*assert tm != null;
            imei = tm.getImei();
            //this change is for Android 10 as per security concern it will not provide the imei number.
            if (imei == null) {
                imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }*/
        } else {
            assert tm != null;
            if (tm.getDeviceId() != null && !tm.getDeviceId().equals("000000000000000")) {
                imei = tm.getDeviceId();
            } else {
                imei = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }

        return imei;
    }

    public void getRegisterScreenResponse(String deviceID)
    {
        Log.d("TAG", "<< Response of screen in getRegisterScreenResponse(); in ScreenActivity "+ deviceID);
        ConnectivityManager manager = (ConnectivityManager) ScreenActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
        if (info != null && info.isConnected())
        {
            Log.d("TAG", "<< Response online in getRegisterScreenResponse() in ScreenActivity "+ deviceID);
            RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
            Call<ScreenRegResponse> call = apiService.getScreenRegResponse(deviceID);
            call.enqueue(new retrofit2.Callback<ScreenRegResponse>() {
                @Override
                public void onResponse(Call<ScreenRegResponse> call, Response<ScreenRegResponse> response) {

                    Log.d("TAG", "<< Response of "+response.body());
                    if(response.isSuccessful()) {

                        Log.d("TAG", "<< Response of screen reg body ScreenActivity " + response.body());
                        ScreenRegResponse screenRegResponse = response.body();
                        screenCode.setText(screenRegResponse.getRandomNumber());
                        getUpdateScreenRegistrationTokenResponse(screenRegResponse.getRandomNumber());

                        Log.d("TAG", "<< Response of screen screenRegResponse.getRandomNumber() ScreenActivity " + screenRegResponse.getRandomNumber());
//                        getCompositionDetailByIDResponse(screenResponse.getScreen().get(0).getComposition_id(), DeviceID);
                    }
                }

                @Override
                public void onFailure(Call<ScreenRegResponse> call, Throwable t) {
                    Log.d("main", "onFailure: "+t.getMessage());
                    getRegisterScreenResponse(DeviceID);
                }
            });
        }
        else
        {
            Log.d("TAG", "<< Response offline ");
//            Toast.makeText(this, "please connect your device with Internet", Toast.LENGTH_LONG).show();
        }
    }

    public void getUpdateScreenRegistrationTokenResponse(String code)
    {
        System.out.println("code : "+code);
        Log.d("TAG", "<< Response of getUpdateScreenRegistrationTokenResponse ScreenActivity ");
        ConnectivityManager manager = (ConnectivityManager) ScreenActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
        if (info != null && info.isConnected())
        {
//            Log.d("TAG", "<< Response online ");
            RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
            Call<UpdateScreenRegiResponse> call = apiService.getUpdateScreenRegiToken(
                    Build.VERSION.RELEASE,
                    code,
                    DeviceID,
                    FirebaseInstanceId.getInstance().getToken(),
                    Build.MODEL,
                    "Android",
                    Build.MANUFACTURER,
                    APK_Version,
                    ""+Build.VERSION.SDK_INT,
                    privateIpAddress,
                    publicIpAddress,
                    macAddress);

            call.enqueue(new retrofit2.Callback<UpdateScreenRegiResponse>() {
                @Override
                public void onResponse(Call<UpdateScreenRegiResponse> call, Response<UpdateScreenRegiResponse> response) {

                    Log.d("TAG", "<< Response of screen inside response UpdateScreenRegiResponse ScreenActivity ");
                    if(response.isSuccessful()) {
                        UpdateScreenRegiResponse updateScreenRegiResponse = response.body();
                        System.out.println("<< Response of UpdateScreenRegiResponse records : " + updateScreenRegiResponse);
                        getCompositionStatusCheck(DeviceID);
                    }
                }

                @Override
                public void onFailure(Call<UpdateScreenRegiResponse> call, Throwable t) {
                    getUpdateScreenRegistrationTokenResponse(code);
                }
            });
        }
        else
        {
//            Log.d("TAG", "<< Response offline ");
//            Toast.makeText(this, "please connect your device with Internet", Toast.LENGTH_LONG).show();
        }
    }

    public void getCompositionStatusCheck(String deviceID)
    {
        System.out.println("DeviceID : "+deviceID);
        Log.d("TAG", "<< Response of screen getCompositionStatusCheck");
        ConnectivityManager manager = (ConnectivityManager) ScreenActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
        if (info != null && info.isConnected())
        {
            Log.d("TAG", "<< Response online getCompositionStatusCheck");
            RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
            Call<CompositionStatusCheckResponse> call = apiService.getCompositionStatusCheckResponse(DeviceID);
            call.enqueue(new retrofit2.Callback<CompositionStatusCheckResponse>() {
                @Override
                public void onResponse(Call<CompositionStatusCheckResponse> call, Response<CompositionStatusCheckResponse> response) {

                    Log.d("TAG", "<< Response of screen inside CompositionStatusCheckResponse getCompositionStatusCheck"+response.toString());
                    if(response.isSuccessful()) {
                        CompositionStatusCheckResponse compositionStatusCheckResponse = response.body();
                        if(compositionStatusCheckResponse.getStatus().equals("1")) {
                            ApplicationPreferences.getInstance(ScreenActivity.this).setIsLoggedin(true);
//                            Toast.makeText(ScreenActivity.this, "===== res screen component status "
//                                    +compositionStatusCheckResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ScreenActivity.this, CompositionDBActivity.class));
                            finish();
//                            getCompositionStatusCheck(DeviceID);
                        }
                        else
                        {
                            getCompositionStatusCheck(DeviceID);
                            System.out.println("<< Response of respobj else : " + compositionStatusCheckResponse.getMessage());
//                            Toast.makeText(ScreenActivity.this, compositionStatusCheckResponse.getMessage(),
//                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<CompositionStatusCheckResponse> call, Throwable t) {
//                    Log.d("main", "onFailure: "+t.getMessage());
                    getRegisterScreenResponse(DeviceID);
                }
            });
        }
        else
        {
            Log.d("TAG", "<< Response offline ");
//            Toast.makeText(this, "please connect your device with Internet", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //TODO
                }
                break;
            default:
                break;
        }
    }
}