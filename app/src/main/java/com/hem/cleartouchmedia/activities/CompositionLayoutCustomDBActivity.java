package com.hem.cleartouchmedia.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import com.hem.cleartouchmedia.R;
import com.hem.cleartouchmedia.model.CompositionCustomLayoutDetail;
import com.hem.cleartouchmedia.model.CompositionCustomLayoutModel;
import com.hem.cleartouchmedia.model.CompositionLayoutDetail;
import com.hem.cleartouchmedia.networking.RetrofitAPIInerface;
import com.hem.cleartouchmedia.networking.RetrofitClient;
import com.hem.cleartouchmedia.response.CompositionCustomLayoutResponse;
import com.hem.cleartouchmedia.response.CompositionLayoutResponse;
import com.hem.cleartouchmedia.response.DeviceStatusCheckReponse;
import com.hem.cleartouchmedia.utilities.ApplicationConstants;
import com.hem.cleartouchmedia.utilities.ApplicationPreferences;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CompositionLayoutCustomDBActivity extends AppCompatActivity {

    private static final String TAG = CompositionLayoutCustomDBActivity.class.getSimpleName();
    String DeviceID;
    private boolean hasLoaded = false;
    private String mLayoutType;
    private String mCompositionID;
    private String mLayoutOrientation;
    int downSpeed = 0;
    int upSpeed = 0;
    String networkStatus;
    String videoQuality;
    String temp_type = "℃";

    CompositionCustomLayoutResponse compositionCustomLayoutResponse;
    CompositionCustomLayoutModel compositionCustomLayoutModel;
    List<CompositionCustomLayoutDetail> compositionCustomLayoutDetail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String SCREEN_TYPE = (getIntent().getStringExtra("ORIENTATION") != null ?
                getIntent().getStringExtra("ORIENTATION") : "LANDSCAPE");
        if(SCREEN_TYPE.equalsIgnoreCase("PORTRAIT"))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_composition_layout_zone_custom_activity);
        }
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.activity_composition_layout_zone_custom_activity);
        }

        TextView demoTxt = findViewById(R.id.demo_txt);
        if(ApplicationConstants.BUILT_STATUS.equalsIgnoreCase("DEMO"))
        {
            demoTxt.setVisibility(View.VISIBLE);
        }
        else
        {
            demoTxt.setVisibility(View.GONE);
        }
        getCustomLayoutResponse();
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
                getIntent().getStringExtra("COMPOSITION_ID"),
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
            Log.d(TAG, "<< Response of compositionLayoutCustomResponse offline ");
        }
    }

    private void getCustomLayoutResponse() {
        Log.d(TAG, "<<<<< Composition custom layout");
        if (getIntent().getStringExtra("COMPOSITION_ID") != null) {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
            if (info != null && info.isConnected()) {
                RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
                Call<CompositionCustomLayoutResponse> call = apiService.getCustomCompositionResponse(
                        getIntent().getStringExtra("COMPOSITION_ID"));
                call.enqueue(new retrofit2.Callback<CompositionCustomLayoutResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<CompositionCustomLayoutResponse> call, Response<CompositionCustomLayoutResponse> response) {
                        if (response.isSuccessful()) {
                            CompositionCustomLayoutResponse compositionCustomLayoutResponse = response.body();
                            if (compositionCustomLayoutResponse != null) {
                                if(compositionCustomLayoutResponse.getStatus().equalsIgnoreCase("1"))
                                {
                                    Log.d(TAG, "<<<<< Composition custom layout response body is: "+compositionCustomLayoutResponse.getMessage());
                                    compositionCustomLayoutModel = compositionCustomLayoutResponse.getCompositionCustomLayoutModel();
                                    compositionCustomLayoutDetail = compositionCustomLayoutModel.getCompositionCustomLayoutDetail();
                                    Log.d(TAG, "<<<<< Composition custom layout response getCountZones : "
                                            +compositionCustomLayoutModel.getCountZones());

                                    Log.d(TAG, "<<<<< Composition custom layout response compositionCustomLayoutDetail size() : "
                                            +compositionCustomLayoutDetail.size());

                                    customLayoutCreate(compositionCustomLayoutModel.getCountZones(), compositionCustomLayoutDetail);
                                }
                                else
                                {
                                    Log.d(TAG, "<< Composition of compositionCustomLayoutDetail failed ");
                                }
                            } else {
                                // Handle null response
                                Log.d(TAG, "<<<<< Composition custom layout response body is null");
                            }
                        } else {
                            // Handle unsuccessful response
                            Log.d(TAG, "<<<<< Composition custom layout response unsuccessful");
                        }
                    }

                    @Override
                    public void onFailure(Call<CompositionCustomLayoutResponse> call, Throwable t) {
                        Log.e(TAG, "<<<<< Failed to get composition custom layout details: " + t.getMessage());
                    }
                });
            } else {
                Log.e(TAG, "No active network connection available");
            }
        } else {
            Log.e(TAG, "<<<<< Composition ID is null");
        }
    }

    private void customLayoutCreate(String countZones, List<CompositionCustomLayoutDetail> compositionCustomLayoutDetail)
    {
        TextView demo_txt = findViewById(R.id.demo_txt);
        LinearLayout linearLayout = findViewById(R.id.img_layout);
        demo_txt.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        LinearLayout llMain = findViewById(R.id.rlMain);
        LinearLayout llChild1 = new LinearLayout(CompositionLayoutCustomDBActivity.this);
        LinearLayout llChild2 = new LinearLayout(CompositionLayoutCustomDBActivity.this);
        TextView textView = new TextView(this);
        textView.setText("I am added dynamically to the view");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );

        llChild1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        llChild1.setOrientation(LinearLayout.VERTICAL);

        llChild2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        llChild2.setOrientation(LinearLayout.VERTICAL);

        textView.setLayoutParams(params);
        llMain.addView(textView);

        /*LinearLayout parent = new LinearLayout(CompositionLayoutCustomDBActivity.this);

        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        parent.setOrientation(LinearLayout.HORIZONTAL);

        //children of parent linearlayout

        ImageView iv = new ImageView(CompositionLayoutCustomDBActivity.this);
        iv.setImageResource(R.drawable.cloudy);

        LinearLayout layout1 = new LinearLayout(CompositionLayoutCustomDBActivity.this);
        LinearLayout layout2 = new LinearLayout(CompositionLayoutCustomDBActivity.this);
        LinearLayout layout3 = new LinearLayout(CompositionLayoutCustomDBActivity.this);
        LinearLayout layout4 = new LinearLayout(CompositionLayoutCustomDBActivity.this);

        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        layout2.setOrientation(LinearLayout.VERTICAL);

        parent.addView(iv);
        parent.addView(layout2);

        //children of layout2 LinearLayout

        TextView tv1 = new TextView(CompositionLayoutCustomDBActivity.this);
        TextView tv2 = new TextView(CompositionLayoutCustomDBActivity.this);
        TextView tv3 = new TextView(CompositionLayoutCustomDBActivity.this);
        TextView tv4 = new TextView(CompositionLayoutCustomDBActivity.this);

        tv1.setText("1");
        tv1.setTextColor(Color.WHITE);
        tv1.setTextSize(20);
        tv2.setText("2");
        tv2.setTextColor(Color.WHITE);
        tv2.setTextSize(20);
        tv3.setText("3");
        tv3.setTextColor(Color.WHITE);
        tv3.setTextSize(20);
        tv4.setText("4");
        tv4.setTextColor(Color.WHITE);
        tv4.setTextSize(20);

        layout2.addView(tv1);
        layout2.addView(tv2);
        layout2.addView(tv3);
        layout2.addView(tv4);*/
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "<< onStart method in custom layout 0  ");
        DeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "<< DeviceID in compositionLayoutCustomResponse " + DeviceID);
        deviceStatus("1", "onStart");

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm != null ? cm.getActiveNetworkInfo() : null;
        if (netinfo != null && netinfo.isConnected())
        {
            deviceStatus("1", "onStart");
            @SuppressLint({"NewApi", "LocalSuppress"})
            NetworkCapabilities nc = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            }
            downSpeed = nc.getLinkDownstreamBandwidthKbps();
            upSpeed = nc.getLinkUpstreamBandwidthKbps();
            System.out.println("<<<<<<<<<<< network is downSpeed: "+downSpeed);
            System.out.println("<<<<<<<<<<< network is upSpreed: "+upSpeed);
            if(downSpeed < 150)
            {
                networkStatus = "POOR";
                videoQuality = "hd144 ";
            }
            else if(downSpeed < 512)
            {
                networkStatus = "MODERATE";
                videoQuality = "hd240 ";
            }
            else if(downSpeed < 720)
            {
                networkStatus = "MODERATE";
                videoQuality = "hd360 ";
            }
            else if(downSpeed < 1480)
            {
                networkStatus = "MODERATE";
                videoQuality = "hd480 ";
            }
            else if (downSpeed < 2000)
            {
                networkStatus = "GOOD";
                videoQuality = "hd720 ";
            }
            else
            {
                networkStatus = "EXCELLENT";
                videoQuality = "hd1080 ";
            }
        }
    }

    @Override
    public void onDestroy(){
        deviceStatus("0", "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onStop(){
        deviceStatus("0", "onStop");
        super.onStop();
    }

    @Override
    public void onPause(){
        deviceStatus("0", "onPause");
        super.onPause();
    }
}