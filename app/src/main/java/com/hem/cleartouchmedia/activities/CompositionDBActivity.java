package com.hem.cleartouchmedia.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.hem.cleartouchmedia.AppService;
import com.hem.cleartouchmedia.Constant;
import com.hem.cleartouchmedia.R;
import com.hem.cleartouchmedia.SplashActivity;
import com.hem.cleartouchmedia.model.CompositionDetailViewModel;
import com.hem.cleartouchmedia.model.CompositionLayoutDetailViewModel;
import com.hem.cleartouchmedia.networking.RetrofitClient;
import com.hem.cleartouchmedia.repository.CompositionRepository;
import com.hem.cleartouchmedia.repository.Repository;
import com.hem.cleartouchmedia.model.ScreenViewModel;
import com.hem.cleartouchmedia.model.Screen;
import com.hem.cleartouchmedia.networking.RetrofitAPIInerface;
import com.hem.cleartouchmedia.response.CompositionResponse;
import com.hem.cleartouchmedia.response.DeviceStatusCheckReponse;
import com.hem.cleartouchmedia.response.ScreenResponse;
import com.hem.cleartouchmedia.utilities.ApplicationConstants;
import com.hem.cleartouchmedia.utilities.ApplicationPreferences;
import com.hem.cleartouchmedia.utilities.FileUtils;

import java.io.File;

import retrofit2.Call;
import retrofit2.Response;

public class CompositionDBActivity extends AppCompatActivity {
    private static final String TAG = CompositionDBActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;
    private ScreenViewModel screenViewModel;
    private CompositionDetailViewModel compositionDetailViewModel;
    private CompositionLayoutDetailViewModel compositionLayoutDetailViewModel;
    ScreenResponse screenResponse;
    CompositionResponse compositionResponse;
    private Repository repository;
    private CompositionRepository compositionRepository;
    String DeviceID;
    private boolean hasLoaded = false;
    private String mLayoutType;
    private String mCompositionID;
    private String mLayoutOrientation;

    private Handler mHandler;
    private Runnable mRunnable;

    TextView responseMsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composition);

        responseMsg = findViewById(R.id.response_msg);

        startService(new Intent(this, AppService.class));

        TextView demoTxt = findViewById(R.id.demo_txt);
        if(ApplicationConstants.BUILT_STATUS.equalsIgnoreCase("DEMO"))
        {
            demoTxt.setVisibility(View.VISIBLE);
        }
        else
        {
            demoTxt.setVisibility(View.GONE);
        }
//        Toast.makeText(CompositionDBActivity.this, "app status: "+status, Toast.LENGTH_SHORT).show();
    }


    private void getScreenResponse() {
        Log.d(TAG, "<< Response of getScreenResponse in compositionDBActivity ");
        ConnectivityManager manager = (ConnectivityManager) CompositionDBActivity.this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
        Log.d(TAG, "<< Response of Network status "+info);
        if (info != null && info.isConnected())
        {
            Log.d(TAG, "<< Response online on getScreenResponse() in compositionDBActivity ");
            RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
            Call<ScreenResponse> call = apiService.getScreenResponse(DeviceID);
            call.enqueue(new retrofit2.Callback<ScreenResponse>() {
                @Override
                public void onResponse(Call<ScreenResponse> call, Response<ScreenResponse> response) {

                    Log.d(TAG, "<< Response of screen inside response in compositionDBActivity ");
                    if(response.isSuccessful()) {
                        Log.d(TAG, "<< Response of screen body  in compositionDBActivity " + response.body().getMessage());
                        screenResponse = response.body();
                        repository.insert(screenResponse.getScreen());
                        getCompositionResponse();
                    }
                    else {
//                        getScreenResponse();
                        responseMsg.setText("response null");
                        Intent in = new Intent(CompositionDBActivity.this, SplashActivity.class);
                        startActivity(in);
                        CompositionDBActivity.this.finish();
                    }
                }

                @Override
                public void onFailure(Call<ScreenResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: "+t.getMessage());
                    responseMsg.setText("response failure");
                    Intent in = new Intent(CompositionDBActivity.this, SplashActivity.class);
                    startActivity(in);
                    CompositionDBActivity.this.finish();
                }
            });
        }
        else
        {
            Log.d(TAG, "<< Response offline ");
            responseMsg.setText("device offline");
            getCompositionResponse();
        }
    }

    private void getCompositionResponse() {
        Log.d(TAG, "<< Response getCompositionResponse  in compositionDBActivity");
        screenViewModel = new ViewModelProvider(this).get(ScreenViewModel.class);
        screenViewModel.getAllCats().observe(this, new Observer<Screen>() {
            @Override
            public void onChanged(Screen screen) {
                if(screen != null) {
                    mLayoutType = screen.getLayoutType();
                    mCompositionID = screen.getComposition_id();
                    mLayoutOrientation = screen.getOrientation();
//                    if (!hasLoaded) {
//                        hasLoaded = true;
                    if(screen.getLayoutType().equalsIgnoreCase("custom_layout"))
                    {
                        System.out.println("<< Response screen component by layout type Custom in compositionDBActivity : "+ screen.getLayoutType());
                        Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutCustomDBActivity.class);
                        in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                        in.putExtra("ORIENTATION", screen.getOrientation());
                        startActivity(in);
                    }
                    else if(screen.getLayoutType().equalsIgnoreCase("layout4"))
                    {
                        System.out.println("<< Response screen component by layout type 4 in compositionDBActivity : "+ screen.getLayoutType());
                        Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutFourDBActivity.class);
                        in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                        in.putExtra("ORIENTATION", screen.getOrientation());
                        startActivity(in);
                    }
                    else if(screen.getLayoutType().equalsIgnoreCase("layout3"))
                    {
                        System.out.println("<< Response screen component by layout type 3 in compositionDBActivity : "+ screen.getLayoutType());
                        Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutThreeDBActivity.class);
                        in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                        in.putExtra("ORIENTATION", screen.getOrientation());
                        startActivity(in);
                    }
                    else if(screen.getLayoutType().equalsIgnoreCase("layout2"))
                    {
                        System.out.println("<< Response screen component by layout type 2 in compositionDBActivity: "+ screen.getLayoutType());
                        Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutTwoDBActivity.class);
                        in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                        in.putExtra("ORIENTATION", screen.getOrientation());
                        startActivity(in);
                    }
                    else if(screen.getLayoutType().equalsIgnoreCase("layout1"))
                    {
                        System.out.println("<< Response screen component by layout type 1 in compositionDBActivity : "+TAG + screen.getLayoutType());
                        Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutOneDBActivity.class);
                        in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                        in.putExtra("ORIENTATION", screen.getOrientation());
                        startActivity(in);
                    }
                    else if(screen.getLayoutType().equalsIgnoreCase("layout2horizontal"))
                    {
                        System.out.println("<< Response screen component by layout 2 horizontal in compositionDBActivity : "+TAG + screen.getLayoutType());
                        Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutTwoHorizontalDBActivity.class);
                        in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                        in.putExtra("ORIENTATION", screen.getOrientation());
                        startActivity(in);
                    }
                    else
                    {
                        System.out.println("<< Response screen component by layout type 1 in compositionDBActivity: "+ screen.getLayoutType());
                        Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutOneDBActivity.class);
                        in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                        in.putExtra("ORIENTATION", screen.getOrientation());
                        startActivity(in);
                    }
//                    }
                }
                else {
                    getScreenResponse();
                }
            }
        });
    }

    private void deviceStatus(String status, String method) {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
        if (info != null && info.isConnected())
        {
            RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
            Call<DeviceStatusCheckReponse> call = apiService.getCompositionLogResponse(
                    DeviceID,
                    method,
                    mCompositionID,
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
        // Example folder path
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            startActivity(new Intent(CompositionDBActivity.this, SplashActivity.class));
            finish();
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

    private void deleteFiles() {
        File directory = new File(Environment.getExternalStorageDirectory(), Constant.FOLDER_NAME);
        if (directory.exists() && directory.isDirectory()) {
            deleteDirectory(directory);
            Log.d("CTM", "All files and directories deleted.");
        } else {
            Log.d("CTM", "Directory does not exist or is not a directory.");
        }
    }

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            Log.d("CTM", "<<<<<<<<<<<<<<<<<<<<<<files length: "+files.length);
            if (files != null) {
                for (File file : files) {
                    Log.d("CTM", "<<<<<<<<<<<<<<<<<<<<<<file.getName(): "+file.getName());
                    file.delete();
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Initialize Handler
        mHandler = new Handler();

        // Initialize Runnable
        mRunnable = new Runnable() {
            @Override
            public void run() {
                // Call your method here
                deviceStatus("1", "onStart");

                // Call the same runnable again after 1 minute
                mHandler.postDelayed(this, Constant.DELAY_SECONDS); // 50 second = 50,000 milliseconds
            }
        };

        // Start the first execution of the runnable
        mHandler.post(mRunnable);

        Log.d(TAG, "<< onStart method in layout 0  ");
        DeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "<< DeviceID in compositionDBActivity "+  DeviceID);
        compositionDetailViewModel = new ViewModelProvider(this).get(CompositionDetailViewModel.class);
        screenViewModel = new ViewModelProvider(this).get(ScreenViewModel.class);
        if(ApplicationPreferences.getInstance(this).getNotificationReceive())
        {
            Log.d(TAG, "<< Notification status in compositionDBActivity:  "+ ApplicationPreferences.getInstance(this).getNotificationReceive());
            screenViewModel = new ViewModelProvider(this).get(ScreenViewModel.class);
            screenViewModel.deleteAll();

            compositionLayoutDetailViewModel = new ViewModelProvider(this).get(CompositionLayoutDetailViewModel.class);
            compositionLayoutDetailViewModel.deleteAll();
            ApplicationPreferences.getInstance(getApplicationContext()).setNotificationReceive(false);

            deleteCache(this);
            // Check for permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                deleteFiles();
            }
//            File directory = new File(getExternalFilesDir(null), Constant.FOLDER_NAME);
//            FileUtils.deleteDirectory(directory);
        }

        repository = new Repository(getApplication());
        screenViewModel.getAllCats().observe(this, new Observer<Screen>() {
            @Override
            public void onChanged(Screen screen) {
                Log.d(TAG, "<< Response onchange screen view model in compositionDBActivity screenViewModel: "+screen);
                if(screen != null)
                {
                    mLayoutType = screen.getLayoutType();
                    mCompositionID = screen.getComposition_id();
                    mLayoutOrientation = screen.getOrientation();
//                    if (!hasLoaded) {
//                        hasLoaded = true;
                        if(screen.getLayoutType().equalsIgnoreCase("custom_layout"))
                        {
                            System.out.println("<< Response screen component by layout type custom in compositionDBActivity : "+TAG + screen.getLayoutType());
                            Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutCustomDBActivity.class);
                            in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                            in.putExtra("ORIENTATION", screen.getOrientation());
                            startActivity(in);
                        }
                        else if(screen.getLayoutType().equalsIgnoreCase("layout4"))
                        {
                            System.out.println("<< Response screen component by layout type 4 in compositionDBActivity : "+TAG + screen.getLayoutType());
                            Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutFourDBActivity.class);
                            in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                            in.putExtra("ORIENTATION", screen.getOrientation());
                            startActivity(in);
                        }
                        else if(screen.getLayoutType().equalsIgnoreCase("layout3"))
                        {
                            System.out.println("<< Response screen component by layout type 3 in compositionDBActivity : "+TAG + screen.getLayoutType());
                            Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutThreeDBActivity.class);
                            in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                            in.putExtra("ORIENTATION", screen.getOrientation());
                            startActivity(in);
                        }
                        else if(screen.getLayoutType().equalsIgnoreCase("layout2"))
                        {
                            System.out.println("<< Response screen component by layout type 2 in compositionDBActivity : "+TAG + screen.getLayoutType());
                            Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutTwoDBActivity.class);
                            in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                            in.putExtra("ORIENTATION", screen.getOrientation());
                            startActivity(in);
                        }
                        else if(screen.getLayoutType().equalsIgnoreCase("layout1"))
                        {
                            System.out.println("<< Response screen component by layout type 1 in compositionDBActivity : "+TAG + screen.getLayoutType());
                            Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutOneDBActivity.class);
                            in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                            in.putExtra("ORIENTATION", screen.getOrientation());
                            startActivity(in);
                        }
                        else if(screen.getLayoutType().equalsIgnoreCase("layout2horizontal"))
                        {
                            System.out.println("<< Response screen component by layout 2 horizontal in compositionDBActivity : "+TAG + screen.getLayoutType());
                            Intent in = new Intent(CompositionDBActivity.this, CompositionLayoutTwoHorizontalDBActivity.class);
                            in.putExtra("COMPOSITION_ID", screen.getComposition_id());
                            in.putExtra("ORIENTATION", screen.getOrientation());
                            startActivity(in);
                        }
//                    }
                }
                else {
                    getScreenResponse();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                deleteFiles();
            } else {
                Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDestroy(){
        // Remove any pending callbacks to prevent memory leaks
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    @Override
    public void onStop(){
        // Remove any pending callbacks to prevent memory leaks
        mHandler.removeCallbacks(mRunnable);
        super.onStop();
    }

    @Override
    public void onPause(){
        // Remove any pending callbacks to prevent memory leaks
        mHandler.removeCallbacks(mRunnable);
        super.onPause();
    }
}