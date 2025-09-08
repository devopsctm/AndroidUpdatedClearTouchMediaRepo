package com.hem.cleartouchmedia;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import static android.Manifest.permission.READ_MEDIA_IMAGES;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.FirebaseApp;
import com.hem.cleartouchmedia.activities.CompositionDBActivity;
import com.hem.cleartouchmedia.model.CompositionLayoutDetailViewModel;
import com.hem.cleartouchmedia.model.ScreenViewModel;
import com.hem.cleartouchmedia.networking.RetrofitAPIInerface;
import com.hem.cleartouchmedia.networking.RetrofitClient;
import com.hem.cleartouchmedia.response.DeviceStatusCheckReponse;
import com.hem.cleartouchmedia.utilities.ApplicationPreferences;
import com.hem.cleartouchmedia.utilities.ImeiUtil;
import com.hem.cleartouchmedia.utilities.MacAddressUtil;
import com.hem.cleartouchmedia.utilities.PrivateIPUtil;
import com.hem.cleartouchmedia.utilities.PublicIPUtil;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private static final int MY_REQUEST_CODE = 1000;
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private static final int PERMISSION_REQUEST_CODE_IMEI = 1002;
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1003;
    String DeviceID;
    Context context;
    private String TAG = SplashActivity.class.getCanonicalName();
//    private AppUpdateManager appUpdateManager;
    private ScreenViewModel screenViewModel;
    private CompositionLayoutDetailViewModel compositionLayoutDetailViewModel;
    private String[] permissions = new String[]{
            READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_PHONE_STATE};
    private boolean mediaImage, mediaVideo, mediaAudio, storageAccepted, accessNetState, read_phone_state;
    private Handler handler = new Handler();

    private String macAddress;
    private String IMEI;
    String privateIpAddress;
    private String publicIpAddress;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        context = SplashActivity.this;

//        new GooglePlayStoreAppVersionNameLoader().execute();

//        appUpdateManager = AppUpdateManagerFactory.create(this);
//        checkForAppUpdate();

        startService(new Intent(this, AppService.class));
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<< onCreate");

        deviceStatus("1", "onCreate");

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

        DeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Check if an update is available
        /*Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            this,
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });*/

        if(ApplicationPreferences.getInstance(this).getNotificationReceive())
        {
            Log.d(TAG, "<< Notification status in compositionDBActivity:  "
                    + ApplicationPreferences.getInstance(this).getNotificationReceive());
            screenViewModel = new ViewModelProvider(this).get(ScreenViewModel.class);
            screenViewModel.deleteAll();

            compositionLayoutDetailViewModel =
                    new ViewModelProvider(this).get(CompositionLayoutDetailViewModel.class);
            compositionLayoutDetailViewModel.deleteAll();

            ApplicationPreferences.getInstance(getApplicationContext()).setNotificationReceive(false);
            deleteCache(this);
        }

        FirebaseApp.initializeApp(this);

        /*Log.d("<<<<token:", FirebaseInstanceId.getInstance().getToken());
        System.out.println("<<<<<<<token: "+ FirebaseInstanceId.getInstance().getToken());*/
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.hem.cleartouchmedia",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("<<<<KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                //bx1JklH4zZJb6kd+bvcD1xenr8w=   hash key
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        if (!hasPermissions(SplashActivity.this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
            handler.postDelayed(mTask, SPLASH_TIME_OUT);
        }
    }


    private void deviceStatus(String status, String method) {
        Log.d(TAG, "<< device status is: "+status +" << device method is: "+method+" << splash screen");

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
        if (info != null && info.isConnected())
        {
            RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
            Call<DeviceStatusCheckReponse> call = apiService.getDeviceStatusCheckResponse(
                    "",
                    DeviceID,
                    method,
                    "Loading Screen",
                    status);
            call.enqueue(new retrofit2.Callback<DeviceStatusCheckReponse>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<DeviceStatusCheckReponse> call, Response<DeviceStatusCheckReponse> response) {
                    if(response.isSuccessful()) {
                        DeviceStatusCheckReponse deviceStatusCheckReponse = response.body();
                        if(deviceStatusCheckReponse.getStatus().equalsIgnoreCase("1"))
                        {
                            Log.d(TAG, "<< Response of DevicePushStatusReponse success "+deviceStatusCheckReponse.getMessage());
                        }
                        else
                        {
                            Log.d(TAG, "<< Response of DevicePushStatusReponse failed ");
                        }
                    }
                }
                @Override
                public void onFailure(Call<DeviceStatusCheckReponse> call, Throwable t) {
                    Log.d("main", "onFailure: "+t.getMessage());
                }
            });
        }
        else
        {
            Log.d(TAG, "<< Response of compositionLayoutOneResponse offline ");
        }
    }

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            startActivity(new Intent(SplashActivity.this, SplashActivity.class));
            SplashActivity.this.finish();
        } catch (Exception e) { e.printStackTrace();}
    }

    public boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                try {

                    mediaImage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    ApplicationPreferences.getInstance(this).setBooleanValue(Constant.MEDIA_IMAGE, mediaImage);

                    mediaVideo = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    ApplicationPreferences.getInstance(this).setBooleanValue(Constant.MEDIA_VIDEO, mediaVideo);

                    mediaAudio = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    ApplicationPreferences.getInstance(this).setBooleanValue(Constant.MEDIA_AUDIO, mediaAudio);

                    storageAccepted = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    ApplicationPreferences.getInstance(this).setBooleanValue(Constant.STORAGE_ACCEPTED, storageAccepted);

                    accessNetState = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    ApplicationPreferences.getInstance(this).setBooleanValue(Constant.NETWORK_STATE, accessNetState);

                    read_phone_state = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    ApplicationPreferences.getInstance(this).setBooleanValue(Constant.READ_PHONE_STATE, read_phone_state);

                    handler.postDelayed(mTask, SPLASH_TIME_OUT);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            break;
            case PERMISSION_REQUEST_CODE_IMEI:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    IMEI = ImeiUtil.getImeiNumber(this);
                    Log.d(TAG, "<<<<<<<<<<<<<<<<<<Android IMEI: " + IMEI);
                } else {
                    // Permission denied
                    Log.e("MainActivity", "Permission denied to read IMEI number");
                }
            break;
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    Runnable mTask = new Runnable() {
        @Override
        public void run() {
            if(ApplicationPreferences.getInstance(SplashActivity.this).getIsLoggedin())
            {
                Log.d("<<<<KeyHash:", "inside handler if");
                Intent intent = new Intent(SplashActivity.this, CompositionDBActivity.class);
//                      Intent intent = new Intent(SplashActivity.this, RTSPActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Log.d("<<<<KeyHash:", "inside handler else");
                Intent intent = new Intent(SplashActivity.this, ScreenActivity.class);
                startActivity(intent);
                finish();
            }
        }
    };

    /*private void checkForAppUpdate() {

        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<< check for app update");
        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {

                Log.d(TAG, "<<<<<<<<<<<<<<<<<<<< check for app if");
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.IMMEDIATE,
                            this,
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.d(TAG, "<<<<<<<<<<<<<<<<<<<< check for app update else ");
            }
        });
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                // If the update is cancelled or fails, notify the user
//                Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
                Log.e("Update", "Update flow failed! Result code: " + resultCode);
            }
        }
    }

    protected void onResume() {
        super.onResume();
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<< onResume");
        deviceStatus("1", "onResume");
//        Toast.makeText(SplashActivity.this, "<<<<<<<<<<<<< onResume",Toast.LENGTH_LONG).show();
        // Start an update check
        /*appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // If an in-app update is already in progress, resume it.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            AppUpdateType.FLEXIBLE,
                            this,
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });*/
    }
}
