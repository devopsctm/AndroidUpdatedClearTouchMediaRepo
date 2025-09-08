package com.hem.cleartouchmedia.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.webkit.WebViewAssetLoader;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hem.cleartouchmedia.AppService;
import com.hem.cleartouchmedia.Constant;
import com.hem.cleartouchmedia.MusicService;
import com.hem.cleartouchmedia.MyApplication;
import com.hem.cleartouchmedia.R;
import com.hem.cleartouchmedia.SplashActivity;
import com.hem.cleartouchmedia.adapter.TwitterZoneOneAdapter;
import com.hem.cleartouchmedia.adapter.TwitterZoneThreeAdapter;
import com.hem.cleartouchmedia.adapter.TwitterZoneTwoAdapter;
import com.hem.cleartouchmedia.adapter.YoutubeZoneOneAdapter;
import com.hem.cleartouchmedia.adapter.YoutubeZoneThreeAdapter;
import com.hem.cleartouchmedia.adapter.YoutubeZoneTwoAdapter;
import com.hem.cleartouchmedia.bean.DevicePushData;
import com.hem.cleartouchmedia.download.ImageDownloadWorker;
import com.hem.cleartouchmedia.listener.LayoutThreeZoneOnePlayerListener;
import com.hem.cleartouchmedia.listener.LayoutThreeZoneThreePlayerListener;
import com.hem.cleartouchmedia.listener.LayoutThreeZoneTwoPlayerListener;
import com.hem.cleartouchmedia.model.CompositionLayoutDetail;
import com.hem.cleartouchmedia.model.CompositionLayoutDetailViewModel;
import com.hem.cleartouchmedia.model.ScreenViewModel;
import com.hem.cleartouchmedia.model.TwitterApiDataViewModel;
import com.hem.cleartouchmedia.networking.RetrofitAPIInerface;
import com.hem.cleartouchmedia.networking.RetrofitClient;
import com.hem.cleartouchmedia.persistance.CompositionDatabase;
import com.hem.cleartouchmedia.repository.CompositionLayoutRepository;
import com.hem.cleartouchmedia.repository.TwitterApiDataRepository;
import com.hem.cleartouchmedia.response.CompositionLayoutResponse;
import com.hem.cleartouchmedia.response.DevicePushStatusReponse;
import com.hem.cleartouchmedia.response.DeviceStatusCheckReponse;
import com.hem.cleartouchmedia.response.TweetData;
import com.hem.cleartouchmedia.response.TwitterMediaData;
import com.hem.cleartouchmedia.response.TwitterTweetData;
import com.hem.cleartouchmedia.utilities.ApplicationConstants;
import com.hem.cleartouchmedia.utilities.ApplicationPreferences;
import com.hem.cleartouchmedia.utilities.FileUtils;
import com.hem.cleartouchmedia.utilities.ImageUtil;
import com.hem.cleartouchmedia.utilities.Utils;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.interfaces.IVLCVout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

public class CompositionLayoutThreeDBActivity extends AppCompatActivity
        implements IVLCVout.Callback {

    public String TAG = CompositionLayoutThreeDBActivity.class.getSimpleName();

    public CompositionLayoutDetailViewModel compositionLayoutDetailViewModel;
    public CompositionLayoutRepository compositionLayoutRepository;
    public TwitterApiDataViewModel twitterApiDataViewModel;
    private ScreenViewModel screenViewModel;
    public TwitterApiDataRepository twitterApiDataRepository;

    public CompositionDatabase database;
    CompositionLayoutResponse compositionLayoutResponse;

    LinearLayout mLoadingLayout;

    LinearLayout mZoneOneLayoutImg;
    LinearLayout mZoneTwoLayoutImg;
    LinearLayout mZoneThreeLayoutImg;

    LinearLayout mZoneOneLayoutDigitalClock;
    LinearLayout mZoneTwoLayoutDigitalClock;
    LinearLayout mZoneThreeLayoutDigitalClock;

    LinearLayout mZoneOneLayoutWeather;
    LinearLayout mZoneTwoLayoutWeather;
    LinearLayout mZoneThreeLayoutWeather;

    LinearLayout mZoneOneLayoutTwitter;
    LinearLayout mZoneTwoLayoutTwitter;
    LinearLayout mZoneThreeLayoutTwitter;

    LinearLayout mZoneOneLayoutVideo;
    LinearLayout mZoneTwoLayoutVideo;
    LinearLayout mZoneThreeLayoutVideo;

    LinearLayout mZoneOneLayoutURL;
    LinearLayout mZoneTwoLayoutURL;
    LinearLayout mZoneThreeLayoutURL;

    LinearLayout mZoneOneLayoutText;
    LinearLayout mZoneTwoLayoutText;
    LinearLayout mZoneThreeLayoutText;

    LinearLayout mZoneOneLayoutYoutubeVideo;
    LinearLayout mZoneTwoLayoutYoutubeVideo;
    LinearLayout mZoneThreeLayoutYoutubeVideo;

    LinearLayout mZoneOneLayoutSpreadSheet;
    LinearLayout mZoneTwoLayoutSpreadSheet;
    LinearLayout mZoneThreeLayoutSpreadSheet;

    LinearLayout mZoneOneLayoutGoogleSlide;
    LinearLayout mZoneTwoLayoutGoogleSlide;
    LinearLayout mZoneThreeLayoutGoogleSlide;

    LinearLayout mZoneOneLayoutRTSP;
    LinearLayout mZoneTwoLayoutRTSP;
    LinearLayout mZoneThreeLayoutRTSP;

    public ArrayList<TweetData> sliderDataArrayList;

    SliderView sliderView_zone_one;
    SliderView sliderView_zone_two;
    SliderView sliderView_zone_three;

    String DeviceID;

    Handler handlerZoneOne = new Handler();
    Runnable runnableZoneOne;
    int delayZoneOne = 0;
    int countZoneOne = 0;
    int countOne = 0;
    int delayCountZoneOne = 0;

    Handler handlerZoneTwo = new Handler();
    Runnable runnableZoneTwo;
    int delayZoneTwo = 0;
    int countZoneTwo = 0;
    int countTwo = 0;
    int delayCountZoneTwo = 0;

    Handler handlerZoneThree = new Handler();
    Runnable runnableZoneThree;
    int delayZoneThree = 0;
    int countZoneThree = 0;
    int countThree = 0;
    int delayCountZoneThree = 0;

    Handler handlerLoginStatus = new Handler();
    Runnable runnableLoginStatus;
    int delayLoginStatus = 60000;

    int mFlag = 0;
    int isPlaySongStatus = 0;

    ArrayList<String> mZoneOneList;
    ArrayList<String> mZoneTwoList;
    ArrayList<String> mZoneThreeList;

    ArrayList<String> mZoneOneDurationList;
    ArrayList<String> mZoneTwoDurationList;
    ArrayList<String> mZoneThreeDurationList;

    List<CompositionLayoutDetail> compositionLayoutDetailsOne;
    List<CompositionLayoutDetail> compositionLayoutDetailsTwo;
    List<CompositionLayoutDetail> compositionLayoutDetailsThree;

    SliderView mVideoSliderViewZoneOne;
    SliderView mVideoSliderViewZoneTwo;
    SliderView mVideoSliderViewZoneThree;

    VideoView videoViewZoneOne;
    VideoView videoViewZoneTwo;
    VideoView videoViewZoneThree;

    List<TwitterTweetData> twitterTweetData;
    List<TwitterMediaData> twitterMediaData;
    ArrayList<View> imageViewsZoneOne;

    DecimalFormat dtime;

    String mBGMediaUrl = "";
    String mIsBGMedia = "";
    ArrayList<String> mBGMusicList;
    ArrayList<String> mBGMusicPlayTimeList;
    int nowPlaying = -1;

    int downSpeed = 0;
    int upSpeed = 0;
    String networkStatus;
    String videoQuality;

    String temp_type = "℃";

    // display surface
    private SurfaceView mSurfaceZoneOne;
    private SurfaceHolder holderZoneOne;
    // media player
    private LibVLC libvlcZoneOne;
    private MediaPlayer mMediaPlayerZoneOne = null;
    private MediaPlayer.EventListener mPlayerListenerZoneOne = new LayoutThreeZoneOnePlayerListener(this);

    // display surface
    private SurfaceView mSurfaceZoneTwo;
    private SurfaceHolder holderZoneTwo;
    // media player
    private LibVLC libvlcZoneTwo;
    private MediaPlayer mMediaPlayerZoneTwo = null;
    private MediaPlayer.EventListener mPlayerListenerZoneTwo = new LayoutThreeZoneTwoPlayerListener(this);

    // display surface
    private SurfaceView mSurfaceZoneThree;
    private SurfaceHolder holderZoneThree;
    // media player
    private LibVLC libvlcZoneThree;
    private MediaPlayer mMediaPlayerZoneThree = null;
    private MediaPlayer.EventListener mPlayerListenerZoneThree = new LayoutThreeZoneThreePlayerListener(this);
    boolean result;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    String fileVideo = null ;
    String SCREEN_TYPE;

    private static final int NEAR_2K_THRESHOLD = 100;
    private static final int NEAR_4K_THRESHOLD = 150;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if(savedInstanceState == null){
            // code to be executed only once
        }*/
        SCREEN_TYPE = (getIntent().getStringExtra("ORIENTATION") != null ?
                getIntent().getStringExtra("ORIENTATION") : "LANDSCAPE");
        if(SCREEN_TYPE.equalsIgnoreCase("PORTRAIT"))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_composition_zone_three_portrait);
        }
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.activity_composition_zone_three);
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

        startService(new Intent(this, AppService.class));

        Log.d(TAG, "<< onCreate method in layout 3  ");
        mLoadingLayout = findViewById(R.id.loading_layout);

        mVideoSliderViewZoneOne = findViewById(R.id.youtube_slider_zone_one);
        mVideoSliderViewZoneTwo = findViewById(R.id.youtube_slider_zone_two);
        mVideoSliderViewZoneThree = findViewById(R.id.youtube_slider_zone_three);

        // initializing the slider view.
        sliderView_zone_one = findViewById(R.id.slider_zone_one);
        sliderView_zone_two = findViewById(R.id.slider_zone_two);
        sliderView_zone_three = findViewById(R.id.slider_zone_three);

        mZoneOneLayoutImg = findViewById(R.id.zone_one_layout_img);
        mZoneTwoLayoutImg = findViewById(R.id.zone_two_layout_img);
        mZoneThreeLayoutImg = findViewById(R.id.zone_three_layout_img);

        mZoneOneLayoutDigitalClock = findViewById(R.id.zone_one_layout_digital_clock);
        mZoneTwoLayoutDigitalClock = findViewById(R.id.zone_two_layout_digital_clock);
        mZoneThreeLayoutDigitalClock = findViewById(R.id.zone_three_layout_digital_clock);

        mZoneOneLayoutWeather = findViewById(R.id.zone_one_layout_weather);
        mZoneTwoLayoutWeather = findViewById(R.id.zone_two_layout_weather);
        mZoneThreeLayoutWeather = findViewById(R.id.zone_three_layout_weather);

        mZoneOneLayoutTwitter = findViewById(R.id.zone_one_layout_twitter);
        mZoneTwoLayoutTwitter = findViewById(R.id.zone_two_layout_twitter);
        mZoneThreeLayoutTwitter = findViewById(R.id.zone_three_layout_twitter);

        mZoneOneLayoutVideo = findViewById(R.id.zone_one_layout_video);
        mZoneTwoLayoutVideo = findViewById(R.id.zone_two_layout_video);
        mZoneThreeLayoutVideo = findViewById(R.id.zone_three_layout_video);

        mZoneOneLayoutURL = findViewById(R.id.zone_one_layout_url);
        mZoneTwoLayoutURL = findViewById(R.id.zone_two_layout_url);
        mZoneThreeLayoutURL = findViewById(R.id.zone_three_layout_url);

        mZoneOneLayoutText = findViewById(R.id.zone_one_layout_text);
        mZoneTwoLayoutText = findViewById(R.id.zone_two_layout_text);
        mZoneThreeLayoutText = findViewById(R.id.zone_three_layout_text);

        mZoneOneLayoutYoutubeVideo = findViewById(R.id.zone_one_layout_youtube_video);
        mZoneTwoLayoutYoutubeVideo = findViewById(R.id.zone_two_layout_youtube_video);
        mZoneThreeLayoutYoutubeVideo = findViewById(R.id.zone_three_layout_youtube_video);

        mZoneOneLayoutSpreadSheet = findViewById(R.id.zone_one_layout_spreadsheets_url);
        mZoneTwoLayoutSpreadSheet = findViewById(R.id.zone_two_layout_spreadsheets_url);
        mZoneThreeLayoutSpreadSheet = findViewById(R.id.zone_three_layout_spreadsheets_url);

        mZoneOneLayoutGoogleSlide = findViewById(R.id.zone_one_layout_google_slides);
        mZoneTwoLayoutGoogleSlide = findViewById(R.id.zone_two_layout_google_slides);
        mZoneThreeLayoutGoogleSlide = findViewById(R.id.zone_three_layout_google_slides);

        mZoneOneLayoutRTSP = findViewById(R.id.zone_one_layout_rtsp);
        mZoneTwoLayoutRTSP = findViewById(R.id.zone_two_layout_rtsp);
        mZoneThreeLayoutRTSP = findViewById(R.id.zone_three_layout_rtsp);

        /*result = checkPermission();
        if(result){
            checkFolder();
        }*/
    }

    @SuppressLint("HardwareIds")
    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
        if (info != null && info.isConnected())
        {
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



        Log.d(TAG, "<< onStart method in layout 3  ");
        Log.d(TAG, "<< onStart flag status:  "+mFlag);
        Log.d(TAG, "<< onStart notification status:  "+ApplicationPreferences.getInstance(this).getNotificationReceive());
        compositionLayoutDetailViewModel = new ViewModelProvider(this).get(CompositionLayoutDetailViewModel.class);
        screenViewModel = new ViewModelProvider(this).get(ScreenViewModel.class);
        if(ApplicationPreferences.getInstance(this).getNotificationReceive())
        {
            Log.d(TAG, "<< Notification status in compositionDBActivity:  "+ ApplicationPreferences.getInstance(this).getNotificationReceive());
            screenViewModel.deleteAll();
            compositionLayoutDetailViewModel.deleteAll();
            ApplicationPreferences.getInstance(getApplicationContext()).setNotificationReceive(false);
            File directory = new File(getExternalFilesDir(null), Constant.FOLDER_NAME);
            FileUtils.deleteDirectory(directory);
            deleteCache(this);
        }

        handlerLoginStatus.postDelayed(runnableLoginStatus = new Runnable() {
            public void run() {
                handlerLoginStatus.postDelayed(runnableLoginStatus, delayLoginStatus);
                ConnectivityManager manager = (ConnectivityManager) CompositionLayoutThreeDBActivity.this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
                if (info != null && info.isConnected())
                {
                    RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
                    Call<DevicePushStatusReponse> call = apiService.getDevicePushStatusResponse(
                            DeviceID);
                    call.enqueue(new retrofit2.Callback<DevicePushStatusReponse>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(Call<DevicePushStatusReponse> call, Response<DevicePushStatusReponse> response) {
                            if(response.isSuccessful()) {
                                DevicePushStatusReponse devicePushStatusReponse = response.body();
                                if(devicePushStatusReponse.getStatus().equalsIgnoreCase("1"))
                                {
                                    DevicePushData devicePushData = devicePushStatusReponse.getDevicePushData();
                                    if(devicePushData != null)
                                    {
                                        Log.d(TAG, "<< Response of devicePushData success layout 3: "+devicePushData.getPushStatus());
                                        if(devicePushData.getPushStatus().equalsIgnoreCase("1"))
                                        {
                                            setDeleteDatabase();
                                        }
                                    }
                                    Log.d(TAG, "<< Response of DevicePushStatusReponse success "+devicePushStatusReponse.getMessage());
                                }
                                else
                                {
                                    Log.d(TAG, "<< Response of DevicePushStatusReponse failed ");
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<DevicePushStatusReponse> call, Throwable t) {
                            Log.d("main", "onFailure: "+t.getMessage());
                        }
                    });
                }
                else
                {
                    Log.d(TAG, "<< Response of compositionLayoutOneResponse offline ");
                }
                Log.d(TAG, "<< onDeviceLoginStatus in every 10 second ");
//                Toast.makeText(CompositionLayoutThreeDBActivity.this, "This method is run every 10 seconds",
//                        Toast.LENGTH_SHORT).show();
            }
        }, delayLoginStatus);

        database = CompositionDatabase.getInstance(this);

        mZoneOneList = new ArrayList<>();
        mZoneTwoList = new ArrayList<>();
        mZoneThreeList = new ArrayList<>();
        mZoneOneDurationList = new ArrayList<>();
        mZoneTwoDurationList = new ArrayList<>();
        mZoneThreeDurationList = new ArrayList<>();

        compositionLayoutDetailsOne = new ArrayList<>();
        compositionLayoutDetailsTwo = new ArrayList<>();
        compositionLayoutDetailsThree = new ArrayList<>();

        imageViewsZoneOne = new ArrayList<>();
        dtime = new DecimalFormat("#.#");

        twitterTweetData = new ArrayList<>();
        twitterMediaData = new ArrayList<>();

        DeviceID = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        compositionLayoutRepository = new CompositionLayoutRepository(getApplication());
        twitterApiDataRepository = new TwitterApiDataRepository(getApplication());
        getCompositionResponse(DeviceID, getIntent().getStringExtra("COMPOSITION_ID"));
        compositionLayoutDetailViewModel = new ViewModelProvider(this).get(CompositionLayoutDetailViewModel.class);

        compositionLayoutDetailViewModel.getCompositionLayoutDetail().observe(this, new Observer<List<CompositionLayoutDetail>>() {
            @Override
            public void onChanged(List<CompositionLayoutDetail> compositionLayoutDetail) {
                if(compositionLayoutDetail != null)
                {
                    for(int i = 0; i< compositionLayoutDetail.size(); i++)
                    {
                        if(compositionLayoutDetail.get(0).getBGMusicUrl() != null)
                        {
                            mBGMediaUrl = (compositionLayoutDetail.get(0).getBGMusicUrl() != null ?
                                    compositionLayoutDetail.get(0).getBGMusicUrl() : "");
                            ApplicationPreferences.getInstance(CompositionLayoutThreeDBActivity.this)
                                    .setBackgroundMusic(compositionLayoutDetail.get(0).getBGMusicUrl());
                            mIsBGMedia = (compositionLayoutDetail.get(0).isBGMusicPause() != null
                                    ? compositionLayoutDetail.get(0).isBGMusicPause()
                                    : "0");
                            ApplicationPreferences.getInstance(CompositionLayoutThreeDBActivity.this)
                                    .setBackgroundMusicStatus(mIsBGMedia);
                            ApplicationPreferences.getInstance(CompositionLayoutThreeDBActivity.this)
                                    .setBackgroundMusicCount(0);
                        }
                        if(compositionLayoutDetail.get(i).getZone_type().equalsIgnoreCase("zone1"))
                        {
                            CompositionLayoutDetail layoutDetail1 = compositionLayoutDetail.get(i);
                            if(layoutDetail1 != null)
                            {
                                compositionLayoutDetailsOne.add(layoutDetail1);
                                mZoneOneList.add(compositionLayoutDetail.get(i).getMedia_id());
                                mZoneOneDurationList.add(compositionLayoutDetail.get(i).getDuration());
                            }
                        }
                        else if(compositionLayoutDetail.get(i).getZone_type().equalsIgnoreCase("zone2"))
                        {
                            CompositionLayoutDetail layoutDetail2 = compositionLayoutDetail.get(i);
                            if(layoutDetail2 != null)
                            {
                                compositionLayoutDetailsTwo.add(layoutDetail2);
                                mZoneTwoList.add(compositionLayoutDetail.get(i).getMedia_id());
                                mZoneTwoDurationList.add(compositionLayoutDetail.get(i).getDuration());
                            }
                        }
                        else if(compositionLayoutDetail.get(i).getZone_type().equalsIgnoreCase("zone3"))
                        {
                            CompositionLayoutDetail layoutDetail3 = compositionLayoutDetail.get(i);
                            if(layoutDetail3 != null)
                            {
                                compositionLayoutDetailsThree.add(layoutDetail3);
                                mZoneThreeList.add(compositionLayoutDetail.get(i).getMedia_id());
                                mZoneThreeDurationList.add(compositionLayoutDetail.get(i).getDuration());
                            }
                        }
                    }

                    if(mZoneOneList.size()>0 || mZoneTwoList.size()>0 || mZoneThreeList.size()>0)
                    {
                        if(mFlag == 0)
                        {
                            Log.d(TAG, "<< onStart inside mflag");
                            zoneOneHandler();
                            zoneTwoHandler();
                            zoneThreeHandler();
                        }
                    }
                    else {
//                        updateCompositionLayoutDetail();
                        getCompositionResponse(DeviceID, getIntent().getStringExtra("COMPOSITION_ID"));
                    }
                }
                else {
                    getCompositionResponse(DeviceID, getIntent().getStringExtra("COMPOSITION_ID"));
                }
            }
        });
    }

    private void zoneOneHandler() {
        /****      Zone One    ****/
        mFlag = 1;
        Log.d(TAG, "<< onStart method in layout 3 zone one inside zoneOneHandler ");
        Log.d(TAG, "<< onStart method in layout 3 zone one inside zoneOneHandler and mZoneOneList count:  "+mZoneOneList.size());
        handlerZoneOne.postDelayed(runnableZoneOne = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                if(mZoneOneList.size()>0 && mZoneOneList.size()>countZoneOne)
                {
                    int duration = mZoneOneDurationList.get(countZoneOne) != null ? Integer.valueOf(mZoneOneDurationList.get(countZoneOne)) : 0;
                    delayZoneOne = duration * 1000;
                    delayCountZoneOne = delayZoneOne;
                    handlerZoneOne.postDelayed(runnableZoneOne, delayCountZoneOne);
                    try {
                        getZoneOne(mZoneOneList.get(0));
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
//                    System.out.println("===== res zone one else condition");
                    countZoneOne = 0;
                    countOne = 0;
                    if(mZoneOneList.size() > 0)
                    {
                        int duration = mZoneOneDurationList.get(countZoneOne) != null ? Integer.valueOf(mZoneOneDurationList.get(countZoneOne)) : 0;
                        delayZoneOne = duration * 1000;
                        delayCountZoneOne = delayZoneOne;
                        handlerZoneOne.postDelayed(runnableZoneOne, delayCountZoneOne);
                        try {
                            getZoneOne(mZoneOneList.get(0));
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, delayCountZoneOne);
        /****      Zone One End    ****/
    }

    private void zoneTwoHandler() {
        mFlag = 1;
        Log.d(TAG, "<< onStart method in layout 3 zone two inside zoneTwoHandler ");
        Log.d(TAG, "<< onStart method in layout 3 zone two inside zoneTwoHandler and " +
                "mZoneTwoList count:  "+mZoneTwoList.size());

        /****      Zone Two    ****/
        handlerZoneTwo.postDelayed(runnableZoneTwo = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                if(mZoneTwoList.size()>0 && mZoneTwoList.size()>countZoneTwo)
                {
                    int duration = mZoneTwoDurationList.get(countZoneTwo) != null
                            ? Integer.valueOf(mZoneTwoDurationList.get(countZoneTwo)) : 0;
                    delayZoneTwo = duration * 1000;
                    delayCountZoneTwo = delayZoneTwo;
                    handlerZoneTwo.postDelayed(runnableZoneTwo, delayCountZoneTwo);
                    try {
                        getZoneTwo(mZoneTwoList.get(countZoneTwo));
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    countZoneTwo = 0;
                    countTwo = 0;
                    delayCountZoneTwo = 0;
                    if(mZoneTwoList.size()>0)
                    {
                        int duration = mZoneTwoDurationList.get(countZoneTwo) != null
                                ? Integer.valueOf(mZoneTwoDurationList.get(countZoneTwo)) : 0;
                        delayZoneTwo = duration * 1000;
                        delayCountZoneTwo = delayZoneTwo;
                        handlerZoneTwo.postDelayed(runnableZoneTwo, delayCountZoneTwo);
                        try {
                            getZoneTwo(mZoneTwoList.get(countZoneTwo));
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, delayCountZoneTwo);
        /****      Zone Two End    ****/
    }

    private void zoneThreeHandler() {
        /****      Zone Three    ****/
        mFlag = 1;
        Log.d(TAG, "<< onStart method in layout 3 zone Three inside zoneThreeHandler ");
        Log.d(TAG, "<< onStart method in layout 3 zone Three inside zoneThreeHandler and " +
                "mZoneThreeList count:  "+mZoneThreeList.size());

        handlerZoneThree.postDelayed(runnableZoneThree = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                if(mZoneThreeList.size()>0 && mZoneThreeList.size()>countZoneThree)
                {
                    int duration = mZoneThreeDurationList.get(countZoneThree) != null
                            ? Integer.valueOf(mZoneThreeDurationList.get(countZoneThree)) : 0;
                    delayZoneThree = duration * 1000;
                    delayCountZoneThree = delayZoneThree;
                    handlerZoneThree.postDelayed(runnableZoneThree, delayCountZoneThree);
                    try {
                        getZoneThree(mZoneThreeList.get(countZoneThree));
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }
                else
                {
                    countZoneThree = 0;
                    countThree = 0;
                    delayCountZoneThree = 0;
                    if(mZoneThreeList.size() > 0)
                    {
                        int duration = mZoneThreeDurationList.get(countZoneThree) != null
                                ? Integer.valueOf(mZoneThreeDurationList.get(countZoneThree)) : 0;
                        delayZoneThree = duration * 1000;
                        delayCountZoneThree = delayZoneThree * countThree;
                        handlerZoneThree.postDelayed(runnableZoneThree, delayCountZoneThree);
                        try {
                            getZoneThree(mZoneThreeList.get(countZoneThree));
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, delayCountZoneThree);
        /****      Zone Three End    ****/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getZoneOne(String media_id) throws JSONException, ParseException {
        mLoadingLayout.setVisibility(View.GONE);
        CompositionLayoutDetail compositionLayoutDetail = compositionLayoutDetailsOne.get(countZoneOne);
        countZoneOne++;
        countOne++;
        if(compositionLayoutDetail.getType().equalsIgnoreCase("image"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayerZoneOne();
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            mZoneOneLayoutImg.setVisibility(View.VISIBLE);
            mZoneOneLayoutWeather.setVisibility(View.GONE);
            mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
            mZoneOneLayoutTwitter.setVisibility(View.GONE);
            mZoneOneLayoutVideo.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.GONE);
            mZoneOneLayoutURL.setVisibility(View.GONE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneOneLayoutRTSP.setVisibility(View.GONE);

            ImageView iv = findViewById(R.id.zone_one_img);

            new Thread(() -> {
                Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache();
            }).start();
            Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();

            if(compositionLayoutDetail.getUrl() != null)
            {
                int width = 0;
                int height = 0;
                if(compositionLayoutDetail.getProperties() != null)
                {
                    String property = compositionLayoutDetail.getProperties();
                    String[] propertyArr = property.split("px");
                    String[] parts = propertyArr[0].split("\\*"); // Splitting on asterisk (*), which is a special character in regex, so we need to escape it with \\
                    String propertyWidth = parts[0]; // Extracting the width
                    String propertyHeight = parts[1]; // Extracting the height

                    // Printing the width and height
                    System.out.println("<<<<< propertyArr width : "  + propertyWidth);
                    System.out.println("<<<<< propertyArr Height: " + propertyHeight);

                    width = Integer.parseInt(propertyWidth);
                    height = Integer.parseInt(propertyHeight);

                    Log.i(TAG, "<<<<< Width : " + String.valueOf(width) + "\n" + "Height : " + String.valueOf(height));
                    Log.i(TAG, "<<<<< Width imageview : " + String.valueOf(iv.getWidth()) + "\n" + "Height imageview : " + String.valueOf(iv.getHeight()));

                }
                else
                {
                    // on below line we are creating and initializing
                    // variable for display metrics.
                    DisplayMetrics displayMetrics = new DisplayMetrics();

                    // on below line we are getting metrics for display using window manager.
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                    // on below line we are getting height
                    // and width using display metrics.
                    height = displayMetrics.heightPixels;
                    width = displayMetrics.widthPixels;
                }

                ConnectivityManager cm = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
                String[] imageArray = compositionLayoutDetail.getUrl().split("/");

                boolean isDownloaded = ImageUtil.isImageDownloaded(this, Constant.FOLDER_NAME, imageArray[1]);
                if (isDownloaded) {
//                    Toast.makeText(this, "Image Downloaded", Toast.LENGTH_SHORT).show();
                    // The image is already downloaded
                    File imageFile = new File(getExternalFilesDir(Constant.FOLDER_NAME), imageArray[1]);
                    Glide.with(getApplicationContext())
//                            .load(imageFile)
                            .load(Constant.imgURL+compositionLayoutDetail.getUrl())
                            .fitCenter()
//                            .centerInside()
                            .override(width, height)
//                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Load failed", e);

                                    new Thread(() -> {
                                        Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache();
                                    }).start();
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();
                                    deleteImageFile(String.valueOf(imageFile));
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Handle the loaded resource
                                    Log.e("GlideSuccess", "Image Loaded");
                                    return false;
                                }
                            })
                            .into(iv);
                } else {
                    // Enqueue the download worker
                    String imageUrl = compositionLayoutDetail.getUrl();
                    String fileName = imageArray[1];
                    String folderName = Constant.FOLDER_NAME;

                    Data data = new Data.Builder()
                            .putString("imageUrl", Constant.imgURL+imageUrl)
                            .putString("fileName", fileName)
                            .putString("folderName", folderName)
                            .build();

                    OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(ImageDownloadWorker.class)
                            .setInputData(data)
                            .build();

                    WorkManager.getInstance(this).enqueue(downloadRequest);

//                    Toast.makeText(this, "Image Download Failed", Toast.LENGTH_SHORT).show();

                    Glide.with(getApplicationContext())
                            .load(Constant.imgURL+compositionLayoutDetail.getUrl())
                            .fitCenter()
//                            .centerInside()
                            .override(width, height)
//                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Image Load failed", e);

                                    new Thread(() -> {
                                        Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache();
                                    }).start();
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Handle the loaded resource
                                    Log.e("GlideSuccess", "Image Loaded");
                                    return false;
                                }
                            })
                            .into(iv);
                }




                /*File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageArray[1]);
//                File imgFile = new  File("/storage/emulated/0/Movies/"+videoArray[1]);
                if(imgFile.exists()) {
                    Log.i("ExternalStorage", "<<<<<<<<<< inside composition file exists: " +imageArray[1]);
//                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Pictures/"+imageArray[1]);
                    // Load the image using Glide
                    File imageFile = new File(Environment.getExternalStorageDirectory()+"/Pictures/"+imageArray[1]);
                    Log.i("ExternalStorage", "<<<<<<<<<< inside composition imageFile: " +imageFile);
                    Glide.with(getApplicationContext())
                            .load(imageFile)
                            .fitCenter()
                            .override(width, height)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Load failed", e);
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache(); // Call this in a background thread
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();
                                    deleteImageFile(Environment.getExternalStorageDirectory()+"/Pictures/"+imageArray[1]);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Handle the loaded resource
                                    Log.e("GlideSuccess", "Image Loaded");
                                    return false;
                                }
                            })
                            .into(iv);
                }
                else
                {
                    if (info != null && info.isConnected())
                    {
                        newDownload(compositionLayoutDetail.getUrl(), "IMAGE");
                    }
                    Glide.with(getApplicationContext())
                            .load(Constant.imgURL+compositionLayoutDetail.getUrl())
                            .fitCenter()
                            .override(width, height)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Load failed", e);
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache(); // Call this in a background thread
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Handle the loaded resource
                                    Log.e("GlideSuccess", "Image Loaded");
                                    return false;
                                }
                            })
                            .into(iv);
                }*/

                if(SCREEN_TYPE.equalsIgnoreCase("LANDSCAPE"))
                {
                    /*
                        1. HD (High Definition):
                        HD Ready: 1280x720 pixels (720p)
                        Full HD: 1920x1080 pixels (1080p)

                        2. 2K Ultra HD:
                        2K UHD: 2560x1440 pixels

                        2. 4K Ultra HD:
                        4K UHD: 3840x2160 pixels
                        True 4K: 4096x2160 pixels

                        3. 8K Ultra HD:
                        8K UHD: 7680x4320 pixels
                    */

                    /*if((width == 1920 && height == 1080)
                            || (width == 1280 && height == 720)
                            || (width == 2560 && height == 1440)
                            || (width == 3840 && height == 2160)
                            || (width == 4096 && height == 2160)
                            || (width == 7680 && height == 4320))
                    {
                        // Set layout parameters to make the image fullscreen
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    }*/

                    if (width > height) {
                        Log.i(TAG, "<<<<<<<<<< image width > height  if");
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    else
                    {
                        Log.i(TAG, "<<<<<<<<<< image width < height  else");
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    }
                }
                else
                {
                    if (width < height) {
                        Log.i(TAG, "<<<<<<<<<< image width < height  if");
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    else
                    {
                        Log.i(TAG, "<<<<<<<<<< image width > height  else");
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    }

                    /*if((width == 1080 && height == 1920)
                            || (width == 720 && height == 1280)
                            || (width == 1440 && height == 2560)
                            || (width == 2160 && height == 3840)
                            || (width == 2160 && height == 4096)
                            || (width == 4320 && height == 7680))
                    {
                        // Set layout parameters to make the image fullscreen
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    }*/
                }
            }
        }
        if(compositionLayoutDetail.getType().equalsIgnoreCase("digital_clock"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayerZoneOne();
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            mZoneOneLayoutImg.setVisibility(View.GONE);
            mZoneOneLayoutWeather.setVisibility(View.GONE);
            mZoneOneLayoutDigitalClock.setVisibility(View.VISIBLE);
            mZoneOneLayoutTwitter.setVisibility(View.GONE);
            mZoneOneLayoutVideo.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.GONE);
            mZoneOneLayoutURL.setVisibility(View.GONE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneOneLayoutRTSP.setVisibility(View.GONE);

            TextView tv_date = findViewById(R.id.zone_one_txt_digital_clock_date);
            TextView tv_time = findViewById(R.id.zone_one_txt_digital_clock_time);
            TextView tv_zone = findViewById(R.id.zone_one_txt_digital_clock_zone);

            if(compositionLayoutDetail.getTimezone() != null)
            {
//            Calendar c = Calendar.getInstance(TimeZone.getTimeZone(mJsonObject.getString("timezone")));
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone(compositionLayoutDetail.getTimezone()));
                Date date = c.getTime(); //current date and time in UTC
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
//                SimpleDateFormat df = new SimpleDateFormat("EEEE MMMM, dd yyyy hh:mma");
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm a");
                df.setTimeZone(TimeZone.getTimeZone(compositionLayoutDetail.getTimezone())); //format in given timezone
                String strDate = df.format(date);
//                tv_zone.setText(strDate);
                tf.setTimeZone(TimeZone.getTimeZone(compositionLayoutDetail.getTimezone())); //format in given timezone
                String strTime = tf.format(date);

                String[] days = new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
                String day = days[c.get(Calendar.DAY_OF_WEEK)-1];
                tv_zone.setText(day);
                tv_date.setText(strDate);
                tv_time.setText(strTime);
//                                    tv_zone.setText(mJsonObject.getString("timezone"));
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("weather"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayerZoneOne();
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            mZoneOneLayoutImg.setVisibility(View.GONE);
            mZoneOneLayoutWeather.setVisibility(View.VISIBLE);
            mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
            mZoneOneLayoutTwitter.setVisibility(View.GONE);
            mZoneOneLayoutVideo.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.GONE);
            mZoneOneLayoutURL.setVisibility(View.GONE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneOneLayoutRTSP.setVisibility(View.GONE);
            if(compositionLayoutDetail.getTemp_unit() != null)
            {
                if(compositionLayoutDetail.getTemp_unit().equalsIgnoreCase("fahrenheit"))
                {
                    temp_type = "°F";
                }
                else
                {
                    temp_type = "℃";
                }
            }
            else
            {
                temp_type = "℃";
            }
            if(compositionLayoutDetail.getWeatherData() != null)
            {
                getWeatherDetailByCity(compositionLayoutDetail.getWeatherData(), 1);
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("twitter"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayerZoneOne();
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            mZoneOneLayoutImg.setVisibility(View.GONE);
            mZoneOneLayoutWeather.setVisibility(View.GONE);
            mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
            mZoneOneLayoutTwitter.setVisibility(View.VISIBLE);
            mZoneOneLayoutVideo.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.GONE);
            mZoneOneLayoutURL.setVisibility(View.GONE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneOneLayoutRTSP.setVisibility(View.GONE);

            getTwitterAPILocalZoneOneResponse(compositionLayoutDetail.getTwitterProfileData(),
                    compositionLayoutDetail.getTwitterFeedsList(),
                    1,
                    compositionLayoutDetail.getSlide_duration());
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("video"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayerZoneOne();
            }
//            System.out.println("<<<<<<<<<<<< Video URL: "+Constant.imgURL+compositionLayoutDetail.getUrl());
            mZoneOneLayoutImg.setVisibility(View.GONE);
            mZoneOneLayoutWeather.setVisibility(View.GONE);
            mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
            mZoneOneLayoutTwitter.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.GONE);
            mZoneOneLayoutURL.setVisibility(View.GONE);
            mZoneOneLayoutVideo.setVisibility(View.VISIBLE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneOneLayoutRTSP.setVisibility(View.GONE);
            videoViewZoneOne = findViewById(R.id.zone_one_video);

            if(compositionLayoutDetail.getUrl() != null)
            {
                ConnectivityManager cm = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
                String[] videoArray = compositionLayoutDetail.getUrl().split("/");
                File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), videoArray[1]);
//                File imgFile = new  File("/storage/emulated/0/Movies/"+videoArray[1]);
                if(imgFile.exists()){
                    Log.i("ExternalStorage", "<<<<<<<<<< inside composition file exists: " +videoArray[1]);
                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Movies/"+videoArray[1]);
//                    Uri uri = Uri.parse(Constant.imgURL+compositionLayoutDetail.getUrl());
                    videoViewZoneOne.setVideoURI(uri);
                    videoViewZoneOne.setOnErrorListener((mp, what, extra) -> {
                        Toast.makeText(CompositionLayoutThreeDBActivity.this, "Download result error\n Downloading again.. ", Toast.LENGTH_LONG).show();
                        if (info != null && info.isConnected())
                        {
                            newDownload(compositionLayoutDetail.getUrl(), "VIDEO");
                        }
                        return false;
                    });
                }
                else
                {
                    if (info != null && info.isConnected())
                    {
                        newDownload(compositionLayoutDetail.getUrl(), "VIDEO");
                    }
                    Uri uri = Uri.parse(Constant.imgURL+compositionLayoutDetail.getUrl());
                    videoViewZoneOne.setVideoURI(uri);
                }
                videoViewZoneOne.requestFocus();
                videoViewZoneOne.start();
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("url"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayerZoneOne();
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            mZoneOneLayoutImg.setVisibility(View.GONE);
            mZoneOneLayoutWeather.setVisibility(View.GONE);
            mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
            mZoneOneLayoutTwitter.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.GONE);
            mZoneOneLayoutURL.setVisibility(View.VISIBLE);
            mZoneOneLayoutVideo.setVisibility(View.GONE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneOneLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getApp_url() != null)
            {
                if(compositionLayoutDetail.getApp_url().contains("youtube.com"))
                {
                    if(mIsBGMedia.equalsIgnoreCase("1"))
                    {
                        if (isMyServiceRunning(MusicService.class)) {
                        } else {
                            System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                            stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                        }
                    }
                    if(mMediaPlayerZoneOne != null)
                    {
                        releasePlayerZoneOne();
                    }
                    if(videoViewZoneOne != null)
                    {
                        videoDestroyZoneOne();
                    }
                }
                Log.d(TAG, "<<<<< URL is : "+compositionLayoutDetail.getApp_url());
                WebView webView = findViewById(R.id.zone_one_webview);
                String VideoEmbededAdress =
                    "<html>" +
                        "<body>" +
                            "<script type='text/javascript' src='http://www.youtube.com/iframe_api'></script><script type='text/javascript'>\n" +
                            "        var player;\n" +
                            "        function onYouTubeIframeAPIReady()\n" +
                            "        {player=new YT.Player('playerId',{events:{onReady:onPlayerReady}})}\n" +
                            "        function onPlayerReady(event){player.mute();player.setVolume(0);player.playVideo();}\n" +
                            "        </script>" +
                            "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 100%; " +
                            "padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" " +
                            "src="+compositionLayoutDetail.getApp_url()+
                            "?enablejsapi=1&rel=0&playsinline=1&autoplay=1&mute=1&showinfo=0&autohide=1&controls=0&modestbranding=1&vq="+videoQuality +
                            "&fs=0\" allow=\"autoplay;\" frameborder=\"0\">\n"+
                            "</iframe>\n "+
                        "</body>" +
                    "</html>";
                String mimeType = "text/html";
                String encoding = "UTF-8";//"base64";
                String USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";

                WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                        .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                        .build();

                webView.setWebViewClient(new WebViewClient() {
                    private WebView view;
                    private WebResourceRequest request;

                    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                                      WebResourceRequest request) {
                        Log.d("YoutubeLayout3", "shouldOverrideUrlLoading: Url = [" + request.getUrl()+"]");
                        this.view = view;
                        this.request = request;
                        return assetLoader.shouldInterceptRequest(request.getUrl());
                    }
                });

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setAllowContentAccess(true);
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                webView.getSettings().setUserAgentString(USERAGENT);//Important to auto play video
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(VideoEmbededAdress);
                webView.loadDataWithBaseURL("", VideoEmbededAdress, mimeType, encoding, "");

            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("text_app"))
        {

            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayerZoneOne();
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            mZoneOneLayoutImg.setVisibility(View.GONE);
            mZoneOneLayoutWeather.setVisibility(View.GONE);
            mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
            mZoneOneLayoutTwitter.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.VISIBLE);
            mZoneOneLayoutURL.setVisibility(View.GONE);
            mZoneOneLayoutVideo.setVisibility(View.GONE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneOneLayoutRTSP.setVisibility(View.GONE);

            TextView  txtMarquee = (TextView) findViewById(R.id.zone_one_text);
            LinearLayout linearLayout = findViewById(R.id.zone_one_text_layout);
//                                    linearLayout.setBackgroundResource(Color.parseColor(mJsonObject.getString("background_color")));

            if(compositionLayoutDetail.getBackground_color() != null)
            {
                linearLayout.setBackgroundColor(Color.parseColor(compositionLayoutDetail.getBackground_color()));
            }

            txtMarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            if(compositionLayoutDetail.getText() != null)
            {
                txtMarquee.setText("\t \t \t \t \t \t\t \t \t \t \t \t"+compositionLayoutDetail.getText()+"\t \t \t \t \t \t\t \t \t \t \t \t"+compositionLayoutDetail.getText()+"\t \t \t \t \t \t\t \t \t \t \t \t"+compositionLayoutDetail.getText()+"\t \t \t \t \t \t\t \t \t \t \t \t");
            }

            if(compositionLayoutDetail.getText_color() != null)
            {
                txtMarquee.setTextColor(Color.parseColor(compositionLayoutDetail.getText_color()));
            }

            txtMarquee.setSelected(true);
            txtMarquee.setSingleLine(true);
            if(compositionLayoutDetail.getScroll_direction().equalsIgnoreCase("0"))
            {
                txtMarquee.setTextDirection(View.TEXT_DIRECTION_LTR);
                txtMarquee.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            else
            {
                txtMarquee.setTextDirection(View.TEXT_DIRECTION_RTL);
                txtMarquee.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }

//            txtMarquee.setSpeed(100.0f);
            if(compositionLayoutDetail.getScroll_font_size() != null)
            {
                txtMarquee.setTextSize(Integer.parseInt(compositionLayoutDetail.getScroll_font_size()));
            }
            else
            {
                txtMarquee.setTextSize(45.0F);
            }
            try {
                Field field = txtMarquee.getClass().getDeclaredField("mMarquee");
                field.setAccessible(true);
                Object marquee = field.get(txtMarquee);
                if (marquee != null) {
                    Method method = marquee.getClass().getDeclaredMethod("setScrollSpeed", int.class);
                    method.setAccessible(true);
                    method.invoke(marquee, 120); // set the marquee speed to 60 pixels per second
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(compositionLayoutDetail.getText_style() != null)
            {
                if(compositionLayoutDetail.getText_style().equalsIgnoreCase("bold"))
                {
                    txtMarquee.setTypeface(null, Typeface.BOLD);
                }
                else if(compositionLayoutDetail.getText_style().equalsIgnoreCase("normal"))
                {
                    txtMarquee.setTypeface(null, Typeface.NORMAL);
                }
                else if(compositionLayoutDetail.getText_style().equalsIgnoreCase("regular"))
                {
                    txtMarquee.setTypeface(null, Typeface.NORMAL);
                }
                else if(compositionLayoutDetail.getText_style().equalsIgnoreCase("italic"))
                {
                    txtMarquee.setTypeface(null, Typeface.ITALIC);
                }
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("youtube"))
        {
            Log.d("zone one area 3layout", "<<<<< youtube video section");
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice stop: ");
                    stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayerZoneOne();
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            mZoneOneLayoutImg.setVisibility(View.GONE);
            mZoneOneLayoutWeather.setVisibility(View.GONE);
            mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
            mZoneOneLayoutTwitter.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.GONE);
            mZoneOneLayoutURL.setVisibility(View.GONE);
            mZoneOneLayoutVideo.setVisibility(View.GONE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.VISIBLE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneOneLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getYoutube_video_ids() != null)
            {
                String[] separated = compositionLayoutDetail.getYoutube_video_ids().split(",");
                getYoutubeAPIZoneOneResponse(separated,300);
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("spreadsheets_url"))
        {
            Log.d(TAG, "<<<<<<<<<<<<<<<<<<< "+compositionLayoutDetail.getType());
            Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<< google shhet url : "
                    +compositionLayoutDetail.getApp_url());

            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayerZoneOne();
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            mZoneOneLayoutImg.setVisibility(View.GONE);
            mZoneOneLayoutWeather.setVisibility(View.GONE);
            mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
            mZoneOneLayoutTwitter.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.GONE);
            mZoneOneLayoutURL.setVisibility(View.GONE);
            mZoneOneLayoutVideo.setVisibility(View.GONE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.VISIBLE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneOneLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getApp_url() != null)
            {
                WebView webView = (WebView) findViewById(R.id.zone_one_spreadsheets_webview);
                String VideoEmbededAdress =
                        "<html>" +
                            "<body>" +
                                "<script type='text/javascript' src='http://www.youtube.com/iframe_api'></script><script type='text/javascript'>\n" +
                                "        var player;\n" +
                                "        function onYouTubeIframeAPIReady()\n" +
                                "        {player=new YT.Player('playerId',{events:{onReady:onPlayerReady}})}\n" +
                                "        function onPlayerReady(event){player.mute();player.setVolume(0);player.playVideo();}\n" +
                                "        </script>" +
                                "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 100%; " +
                                "padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" " +
                                "src="+compositionLayoutDetail.getApp_url()+
                                "?enablejsapi=1&rel=0&playsinline=1&autoplay=1&mute=1&showinfo=0&autohide=1&controls=0&modestbranding=1&vq="+videoQuality +
                                "&fs=0\" allow=\"autoplay;\" frameborder=\"0\">\n"+
                                "</iframe>\n "+
                            "</body>" +
                        "</html>";
                String mimeType = "text/html";
                String encoding = "UTF-8";//"base64";
                String USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";

                WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                        .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                        .build();

                webView.setWebViewClient(new WebViewClient() {
                    private WebView view;
                    private WebResourceRequest request;

                    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                                      WebResourceRequest request) {
                        Log.d("YoutubeLayout3", "shouldOverrideUrlLoading: Url = [" + request.getUrl()+"]");
                        this.view = view;
                        this.request = request;
                        return assetLoader.shouldInterceptRequest(request.getUrl());
                    }
                });

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setAllowContentAccess(true);
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                webView.getSettings().setUserAgentString(USERAGENT);//Important to auto play video
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(VideoEmbededAdress);
                webView.loadDataWithBaseURL("", VideoEmbededAdress, mimeType, encoding, "");
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("google_slides_url"))
        {
            Log.d(TAG, "<<<<<<<<<<<<<<<<<<< "+compositionLayoutDetail.getType());
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayerZoneOne();
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            mZoneOneLayoutImg.setVisibility(View.GONE);
            mZoneOneLayoutWeather.setVisibility(View.GONE);
            mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
            mZoneOneLayoutTwitter.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.GONE);
            mZoneOneLayoutURL.setVisibility(View.GONE);
            mZoneOneLayoutVideo.setVisibility(View.GONE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.VISIBLE);
            mZoneOneLayoutRTSP.setVisibility(View.GONE);

            Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<<< google shhet url : "
                    +compositionLayoutDetail.getApp_url());
            if(compositionLayoutDetail.getApp_url() != null)
            {
                WebView webView = (WebView) findViewById(R.id.zone_one_google_slides_webview);
                String VideoEmbededAdress =
                        "<html>" +
                            "<body>" +
                                "<script type='text/javascript' src='http://www.youtube.com/iframe_api'></script><script type='text/javascript'>\n" +
                                "        var player;\n" +
                                "        function onYouTubeIframeAPIReady()\n" +
                                "        {player=new YT.Player('playerId',{events:{onReady:onPlayerReady}})}\n" +
                                "        function onPlayerReady(event){player.mute();player.setVolume(0);player.playVideo();}\n" +
                                "        </script>" +
                                "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 100%; " +
                                "padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" " +
                                "src="+compositionLayoutDetail.getApp_url()+
                                "?enablejsapi=1&rel=0&playsinline=1&autoplay=1&mute=1&showinfo=0&autohide=1&controls=0&modestbranding=1&vq="+videoQuality +
                                "&fs=0\" allow=\"autoplay;\" frameborder=\"0\">\n"+
                                "</iframe>\n "+
                            "</body>" +
                        "</html>";
                String mimeType = "text/html";
                String encoding = "UTF-8";//"base64";
                String USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";

                WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                        .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                        .build();

                webView.setWebViewClient(new WebViewClient() {
                    private WebView view;
                    private WebResourceRequest request;

                    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                                      WebResourceRequest request) {
                        Log.d("YoutubeLayout3", "shouldOverrideUrlLoading: Url = [" + request.getUrl()+"]");
                        this.view = view;
                        this.request = request;
                        return assetLoader.shouldInterceptRequest(request.getUrl());
                    }
                });

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setAllowContentAccess(true);
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                webView.getSettings().setUserAgentString(USERAGENT);//Important to auto play video
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(VideoEmbededAdress);
                webView.loadDataWithBaseURL("", VideoEmbededAdress, mimeType, encoding, "");
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("rtsp"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            mZoneOneLayoutImg.setVisibility(View.GONE);
            mZoneOneLayoutWeather.setVisibility(View.GONE);
            mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
            mZoneOneLayoutTwitter.setVisibility(View.GONE);
            mZoneOneLayoutText.setVisibility(View.GONE);
            mZoneOneLayoutURL.setVisibility(View.GONE);
            mZoneOneLayoutVideo.setVisibility(View.GONE);
            mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneOneLayoutRTSP.setVisibility(View.VISIBLE);

            if(compositionLayoutDetail.getApp_url() != null)
            {
                Log.d(TAG, "Playing rtsp url zone 1: " + compositionLayoutDetail.getApp_url());

                mSurfaceZoneOne = findViewById(R.id.zone_one_rtsp);
                WindowManager wm = this.getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                int iNewWidth = (int) (height * 3.0 / 4.0);
                FrameLayout.LayoutParams layoutParams =
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT);
                int iPos = width - iNewWidth;
                Log.d(TAG, "<<Playing back width " + width);
                Log.d(TAG, "<<Playing back height " + height);
                Log.d(TAG, "<<Playing back iNewWidth " + iNewWidth);
                Log.d(TAG, "<<Playing back iPos " + iPos);

//                layoutParams.setMargins(iPos, 0, 0, 0);
                mSurfaceZoneOne.getHolder().setFixedSize(width/2, height/2);
                mSurfaceZoneOne.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                mSurfaceZoneOne.getHolder().setKeepScreenOn(true);
//                mSurfaceZoneOne.getHolder().addCallback(new SurceCallBack());
                mSurfaceZoneOne.setLayoutParams(layoutParams);
                holderZoneOne = mSurfaceZoneOne.getHolder();
                //holder.addCallback(this);

                ArrayList<String> options = new ArrayList<String>();
                options.add("--aout=opensles");
                options.add("--audio-time-stretch"); // time stretching
                options.add("-vvv"); // verbosity
                options.add("--aout=opensles");
                options.add("--avcodec-codec=h264");
                options.add("--file-logging");
                options.add("--logfile=vlc-log.txt");

                libvlcZoneOne = new LibVLC(getApplicationContext(), options);
                holderZoneOne.setKeepScreenOn(true);

                // Create media player
                mMediaPlayerZoneOne = new MediaPlayer(libvlcZoneOne);
                mMediaPlayerZoneOne.setEventListener(mPlayerListenerZoneOne);

                // Set up video output
                final IVLCVout vout = (IVLCVout) mMediaPlayerZoneOne.getVLCVout();
                vout.setVideoView(mSurfaceZoneOne);
                //vout.setSubtitlesView(mSurfaceSubtitles);
                vout.addCallback(this);
                vout.attachViews();
                Media m = new Media(libvlcZoneOne, Uri.parse(compositionLayoutDetail.getApp_url()));
                mMediaPlayerZoneOne.setMedia(m);
                mMediaPlayerZoneOne.play();

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getZoneTwo(String media_id) throws JSONException, ParseException {
        mLoadingLayout.setVisibility(View.GONE);
        CompositionLayoutDetail compositionLayoutDetail = compositionLayoutDetailsTwo.get(countZoneTwo);
        countZoneTwo++;
        countTwo++;
        if(compositionLayoutDetail.getType().equalsIgnoreCase("image"))
        {
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            if(videoViewZoneTwo != null)
            {
                videoDestroyZoneTwo();
            }
            mZoneTwoLayoutImg.setVisibility(View.VISIBLE);
            mZoneTwoLayoutWeather.setVisibility(View.GONE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.GONE);
            mZoneTwoLayoutTwitter.setVisibility(View.GONE);
            mZoneTwoLayoutVideo.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.GONE);
            mZoneTwoLayoutURL.setVisibility(View.GONE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneTwoLayoutRTSP.setVisibility(View.GONE);

            new Thread(() -> {
                Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache();
            }).start();
            Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();

            if(compositionLayoutDetail.getUrl() != null)
            {
                int width = 0;
                int height = 0;
                ImageView iv = findViewById(R.id.zone_two_img);
                if(compositionLayoutDetail.getProperties() != null)
                {
                    String property = compositionLayoutDetail.getProperties();
                    String[] propertyArr = property.split("px");
                    String[] parts = propertyArr[0].split("\\*"); // Splitting on asterisk (*), which is a special character in regex, so we need to escape it with \\
                    String propertyWidth = parts[0]; // Extracting the width
                    String propertyHeight = parts[1]; // Extracting the height

                    // Printing the width and height
                    System.out.println("<<<<< propertyArr width : "  + propertyWidth);
                    System.out.println("<<<<< propertyArr Height: " + propertyHeight);

                    width = Integer.parseInt(propertyWidth);
                    height = Integer.parseInt(propertyHeight);

                    Log.i(TAG, "<<<<< Width : " + String.valueOf(width) + "\n" + "Height : " + String.valueOf(height));
                    Log.i(TAG, "<<<<< Width imageview : " + String.valueOf(iv.getWidth()) + "\n" + "Height imageview : " + String.valueOf(iv.getHeight()));

                }
                else
                {
                    // on below line we are creating and initializing
                    // variable for display metrics.
                    DisplayMetrics displayMetrics = new DisplayMetrics();

                    // on below line we are getting metrics for display using window manager.
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                    // on below line we are getting height
                    // and width using display metrics.
                    height = displayMetrics.heightPixels;
                    width = displayMetrics.widthPixels;

                }

                ConnectivityManager cm = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
                String[] imageArray = compositionLayoutDetail.getUrl().split("/");

                boolean isDownloaded = ImageUtil.isImageDownloaded(this, Constant.FOLDER_NAME, imageArray[1]);
                if (isDownloaded) {
//                    Toast.makeText(this, "Image Downloaded", Toast.LENGTH_SHORT).show();
                    // The image is already downloaded
                    File imageFile = new File(getExternalFilesDir(Constant.FOLDER_NAME), imageArray[1]);
                    Glide.with(getApplicationContext())
//                            .load(imageFile)
                            .load(Constant.imgURL+compositionLayoutDetail.getUrl())
                            .fitCenter()
//                            .centerInside()
                            .override(width, height)
//                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Load failed", e);

                                    new Thread(() -> {
                                        Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache();
                                    }).start();
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();
                                    deleteImageFile(String.valueOf(imageFile));
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Handle the loaded resource
                                    Log.e("GlideSuccess", "Image Loaded");
                                    return false;
                                }
                            })
                            .into(iv);
                } else {
                    // Enqueue the download worker
                    String imageUrl = compositionLayoutDetail.getUrl();
                    String fileName = imageArray[1];
                    String folderName = Constant.FOLDER_NAME;

                    Data data = new Data.Builder()
                            .putString("imageUrl", Constant.imgURL+imageUrl)
                            .putString("fileName", fileName)
                            .putString("folderName", folderName)
                            .build();

                    OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(ImageDownloadWorker.class)
                            .setInputData(data)
                            .build();

                    WorkManager.getInstance(this).enqueue(downloadRequest);

//                    Toast.makeText(this, "Image Download Failed", Toast.LENGTH_SHORT).show();

                    Glide.with(getApplicationContext())
                            .load(Constant.imgURL+compositionLayoutDetail.getUrl())
                            .fitCenter()
//                            .centerInside()
                            .override(width, height)
//                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Image Load failed", e);

                                    new Thread(() -> {
                                        Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache();
                                    }).start();
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Handle the loaded resource
                                    Log.e("GlideSuccess", "Image Loaded");
                                    return false;
                                }
                            })
                            .into(iv);
                }



                /*File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), imageArray[1]);
//                File imgFile = new  File("/storage/emulated/0/Movies/"+videoArray[1]);
                if(imgFile.exists()) {
                    Log.i("ExternalStorage", "<<<<<<<<<< inside composition file exists: " +imageArray[1]);
//                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Pictures/"+imageArray[1]);
                    // Load the image using Glide
                    File imageFile = new File(Environment.getExternalStorageDirectory()+"/Pictures/"+imageArray[1]);
                    Log.i("ExternalStorage", "<<<<<<<<<< inside composition imageFile: " +imageFile);
                    Glide.with(getApplicationContext())
                            .load(imageFile)
                            .fitCenter()
                            .override(width, height)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Load failed", e);
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache(); // Call this in a background thread
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();
                                    deleteImageFile(Environment.getExternalStorageDirectory()+"/Pictures/"+imageArray[1]);
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Handle the loaded resource
                                    Log.e("GlideSuccess", "Image Loaded");
                                    return false;
                                }
                            })
                            .into(iv);
                }
                else
                {
                    if (info != null && info.isConnected())
                    {
                        newDownload(compositionLayoutDetail.getUrl(), "IMAGE");
                    }
                    Glide.with(getApplicationContext())
                            .load(Constant.imgURL+compositionLayoutDetail.getUrl())
                            .fitCenter()
                            .override(width, height)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Load failed", e);
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache(); // Call this in a background thread
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Handle the loaded resource
                                    Log.e("GlideSuccess", "Image Loaded");
                                    return false;
                                }
                            })
                            .into(iv);
                }*/

                if(SCREEN_TYPE.equalsIgnoreCase("LANDSCAPE"))
                {
                    if (width < height) {
                        Log.i(TAG, "<<<<<<<<<< image width < height  if");
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    else
                    {
                        Log.i(TAG, "<<<<<<<<<< image width > height  else");
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    }
                    /*
                    *//*
                        1. HD (High Definition):
                        HD Ready: 1280x720 pixels (720p)
                        Full HD: 1920x1080 pixels (1080p)

                        2. 2K Ultra HD:
                        2K UHD: 2560x1440 pixels

                        2. 4K Ultra HD:
                        4K UHD: 3840x2160 pixels
                        True 4K: 4096x2160 pixels

                        3. 8K Ultra HD:
                        8K UHD: 7680x4320 pixels
                    *//*

                    if((width == 1920 && height == 1080)
                            || (width == 1280 && height == 720)
                            || (width == 2560 && height == 1440)
                            || (width == 3840 && height == 2160)
                            || (width == 4096 && height == 2160)
                            || (width == 7680 && height == 4320))
                    {
                        // Set layout parameters to make the image fullscreen
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    }*/
                }
                else
                {
                    if (width < height) {
                        Log.i(TAG, "<<<<<<<<<< image width < height  if");
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                        iv.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                    else
                    {
                        Log.i(TAG, "<<<<<<<<<< image width > height  else");
                        iv.setLayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));
                        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    }
                }
            }
        }
        if(compositionLayoutDetail.getType().equalsIgnoreCase("digital_clock"))
        {
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            if(videoViewZoneTwo != null)
            {
                videoDestroyZoneTwo();
            }
            mZoneTwoLayoutImg.setVisibility(View.GONE);
            mZoneTwoLayoutWeather.setVisibility(View.GONE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.VISIBLE);
            mZoneTwoLayoutTwitter.setVisibility(View.GONE);
            mZoneTwoLayoutVideo.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.GONE);
            mZoneTwoLayoutURL.setVisibility(View.GONE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneTwoLayoutRTSP.setVisibility(View.GONE);

            TextView tv_date = findViewById(R.id.zone_two_txt_digital_clock_date);
            TextView tv_time = findViewById(R.id.zone_two_txt_digital_clock_time);
            TextView tv_zone = findViewById(R.id.zone_two_txt_digital_clock_zone);

            if(compositionLayoutDetail.getTimezone() != null)
            {
                //            Calendar c = Calendar.getInstance(TimeZone.getTimeZone(mJsonObject.getString("timezone")));
                Calendar c = Calendar.getInstance(TimeZone.getTimeZone(compositionLayoutDetail.getTimezone()));
                Date date = c.getTime(); //current date and time in UTC
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
//                SimpleDateFormat df = new SimpleDateFormat("EEEE MMMM, dd yyyy hh:mma");
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm a");
                df.setTimeZone(TimeZone.getTimeZone(compositionLayoutDetail.getTimezone())); //format in given timezone
                String strDate = df.format(date);
//                tv_zone.setText(strDate);
                tf.setTimeZone(TimeZone.getTimeZone(compositionLayoutDetail.getTimezone())); //format in given timezone
                String strTime = tf.format(date);

                String[] days = new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
                String day = days[c.get(Calendar.DAY_OF_WEEK)-1];
                if (tv_date != null && tv_time != null && tv_zone != null) {
                    tv_zone.setText(day);
                    tv_time.setText(strTime);
                    tv_date.setText(strDate);
                } else {
                    Log.e("ZoneTwo", "Digital clock TextViews are NULL!");
                }

//            tv_zone.setText(mJsonObject.getString("timezone"));
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("weather"))
        {
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            if(videoViewZoneTwo != null)
            {
                videoDestroyZoneTwo();
            }
            mZoneTwoLayoutImg.setVisibility(View.GONE);
            mZoneTwoLayoutWeather.setVisibility(View.VISIBLE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.GONE);
            mZoneTwoLayoutTwitter.setVisibility(View.GONE);
            mZoneTwoLayoutVideo.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.GONE);
            mZoneTwoLayoutURL.setVisibility(View.GONE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneTwoLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getTemp_unit() != null)
            {
                if(compositionLayoutDetail.getTemp_unit().equalsIgnoreCase("fahrenheit"))
                {
                    temp_type = "°F";
                }
                else
                {
                    temp_type = "℃";
                }
            }
            else
            {
                temp_type = "℃";
            }
            if(compositionLayoutDetail.getWeatherData() != null)
            {
                getWeatherDetailByCity(compositionLayoutDetail.getWeatherData(), 2);
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("twitter"))
        {
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            if(videoViewZoneTwo != null)
            {
                videoDestroyZoneTwo();
            }
            mZoneTwoLayoutImg.setVisibility(View.GONE);
            mZoneTwoLayoutWeather.setVisibility(View.GONE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.GONE);
            mZoneTwoLayoutTwitter.setVisibility(View.VISIBLE);
            mZoneTwoLayoutVideo.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.GONE);
            mZoneTwoLayoutURL.setVisibility(View.GONE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneTwoLayoutRTSP.setVisibility(View.GONE);
            getTwitterAPILocalZoneTwoResponse(compositionLayoutDetail.getTwitterProfileData(),
                    compositionLayoutDetail.getTwitterFeedsList(),
                    2,
                    compositionLayoutDetail.getSlide_duration());
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("video"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice stop: ");
                    stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            mZoneTwoLayoutImg.setVisibility(View.GONE);
            mZoneTwoLayoutWeather.setVisibility(View.GONE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.GONE);
            mZoneTwoLayoutTwitter.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.GONE);
            mZoneTwoLayoutURL.setVisibility(View.GONE);
            mZoneTwoLayoutVideo.setVisibility(View.VISIBLE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneTwoLayoutRTSP.setVisibility(View.GONE);

            videoViewZoneTwo = findViewById(R.id.zone_two_video);

            if(compositionLayoutDetail.getUrl() != null)
            {
                ConnectivityManager cm = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
                String[] videoArray = compositionLayoutDetail.getUrl().split("/");
                File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), videoArray[1]);
//                File imgFile = new  File("/storage/emulated/0/Movies/"+videoArray[1]);
                if(imgFile.exists()){
                    Log.i("ExternalStorage", "<<<<<<<<<< inside composition file exists: " +videoArray[1]);
                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Movies/"+videoArray[1]);
//                    Uri uri = Uri.parse(Constant.imgURL+compositionLayoutDetail.getUrl());
                    videoViewZoneTwo.setVideoURI(uri);
                    videoViewZoneTwo.setOnErrorListener((mp, what, extra) -> {
                        Toast.makeText(CompositionLayoutThreeDBActivity.this, "Download result error\n Downloading again.. ", Toast.LENGTH_LONG).show();
                        if (info != null && info.isConnected())
                        {
                            newDownload(compositionLayoutDetail.getUrl(), "VIDEO");
                        }
                        return false;
                    });
                }
                else
                {
                    if (info != null && info.isConnected())
                    {
                        newDownload(compositionLayoutDetail.getUrl(), "VIDEO");
                    }
                    Uri uri = Uri.parse(Constant.imgURL+compositionLayoutDetail.getUrl());
                    videoViewZoneTwo.setVideoURI(uri);
                }
                videoViewZoneTwo.requestFocus();
                videoViewZoneTwo.start();
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("url"))
        {
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            if(videoViewZoneTwo != null)
            {
                videoDestroyZoneTwo();
            }
            mZoneTwoLayoutImg.setVisibility(View.GONE);
            mZoneTwoLayoutWeather.setVisibility(View.GONE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.GONE);
            mZoneTwoLayoutTwitter.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.GONE);
            mZoneTwoLayoutURL.setVisibility(View.VISIBLE);
            mZoneTwoLayoutVideo.setVisibility(View.GONE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneTwoLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getApp_url() != null)
            {
                if(compositionLayoutDetail.getApp_url().contains("youtube.com"))
                {
                    if(mIsBGMedia.equalsIgnoreCase("1"))
                    {
                        if (isMyServiceRunning(MusicService.class)) {
                        } else {
                            System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                            stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                        }
                    }
                }
                WebView webView = (WebView) findViewById(R.id.zone_two_webview);
                String VideoEmbededAdress =
                    "<html>" +
                        "<body>" +
                            "<script type='text/javascript' src='http://www.youtube.com/iframe_api'></script><script type='text/javascript'>\n" +
                            "        var player;\n" +
                            "        function onYouTubeIframeAPIReady()\n" +
                            "        {player=new YT.Player('playerId',{events:{onReady:onPlayerReady}})}\n" +
                            "        function onPlayerReady(event){player.mute();player.setVolume(0);player.playVideo();}\n" +
                            "        </script>" +
                            "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 100%; " +
                            "padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" " +
                            "src="+compositionLayoutDetail.getApp_url()+
                            "?enablejsapi=1&rel=0&playsinline=1&autoplay=1&mute=1&showinfo=0&autohide=1&controls=0&modestbranding=1&vq="+videoQuality +
                            "&fs=0\" allow=\"autoplay;\" frameborder=\"0\">\n"+
                            "</iframe>\n "+
                        "</body>" +
                    "</html>";
                String mimeType = "text/html";
                String encoding = "UTF-8";//"base64";
                String USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";

                WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                        .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                        .build();

                webView.setWebViewClient(new WebViewClient() {
                    private WebView view;
                    private WebResourceRequest request;

                    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                                      WebResourceRequest request) {
                        Log.d("YoutubeLayout3", "shouldOverrideUrlLoading: Url = [" + request.getUrl()+"]");
                        this.view = view;
                        this.request = request;
                        return assetLoader.shouldInterceptRequest(request.getUrl());
                    }
                });

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setAllowContentAccess(true);
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                webView.getSettings().setUserAgentString(USERAGENT);//Important to auto play video
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(VideoEmbededAdress);
                webView.loadDataWithBaseURL("", VideoEmbededAdress, mimeType, encoding, "");

            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("text_app"))
        {
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            if(videoViewZoneTwo != null)
            {
                videoDestroyZoneTwo();
            }
            mZoneTwoLayoutImg.setVisibility(View.GONE);
            mZoneTwoLayoutWeather.setVisibility(View.GONE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.GONE);
            mZoneTwoLayoutTwitter.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.VISIBLE);
            mZoneTwoLayoutURL.setVisibility(View.GONE);
            mZoneTwoLayoutVideo.setVisibility(View.GONE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneTwoLayoutRTSP.setVisibility(View.GONE);

            TextView  txtMarquee = (TextView) findViewById(R.id.zone_two_text);
            LinearLayout linearLayout = findViewById(R.id.zone_two_text_layout);
//                                    linearLayout.setBackgroundResource(Color.parseColor(mJsonObject.getString("background_color")));

            if(compositionLayoutDetail.getBackground_color() != null)
            {
                linearLayout.setBackgroundColor(Color.parseColor(compositionLayoutDetail.getBackground_color()));
            }

            txtMarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            if(compositionLayoutDetail.getText() != null)
            {
                txtMarquee.setText("\t \t \t \t \t \t\t \t \t \t \t \t"+compositionLayoutDetail.getText()+"\t \t \t \t \t \t\t \t \t \t \t \t"+compositionLayoutDetail.getText()+"\t \t \t \t \t \t\t \t \t \t \t \t"+compositionLayoutDetail.getText()+"\t \t \t \t \t \t\t \t \t \t \t \t");
            }

            if(compositionLayoutDetail.getText_color() != null)
            {
                txtMarquee.setTextColor(Color.parseColor(compositionLayoutDetail.getText_color()));
            }

            txtMarquee.setSelected(true);
            txtMarquee.setSingleLine(true);
            if(compositionLayoutDetail.getScroll_direction().equalsIgnoreCase("0"))
            {
                txtMarquee.setTextDirection(View.TEXT_DIRECTION_LTR);
                txtMarquee.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            else
            {
                txtMarquee.setTextDirection(View.TEXT_DIRECTION_RTL);
                txtMarquee.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }

//            txtMarquee.setSpeed(100.0f);
            if(compositionLayoutDetail.getScroll_font_size() != null)
            {
                txtMarquee.setTextSize(Integer.parseInt(compositionLayoutDetail.getScroll_font_size()));
            }
            else
            {
                txtMarquee.setTextSize(45.0F);
            }

            if(compositionLayoutDetail.getText() != null)
            {
                try {
                    Field field = txtMarquee.getClass().getDeclaredField("mMarquee");
                    field.setAccessible(true);
                    Object marquee = field.get(txtMarquee);
                    if (marquee != null) {
                        Method method = marquee.getClass().getDeclaredMethod("setScrollSpeed", int.class);
                        method.setAccessible(true);
                        method.invoke(marquee, 120); // set the marquee speed to 60 pixels per second
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if(compositionLayoutDetail.getText_style() != null)
            {
                if(compositionLayoutDetail.getText_style().equalsIgnoreCase("bold"))
                {
                    txtMarquee.setTypeface(null, Typeface.BOLD);
                }
                else if(compositionLayoutDetail.getText_style().equalsIgnoreCase("normal"))
                {
                    txtMarquee.setTypeface(null, Typeface.NORMAL);
                }
                else if(compositionLayoutDetail.getText_style().equalsIgnoreCase("regular"))
                {
                    txtMarquee.setTypeface(null, Typeface.NORMAL);
                }
                else if(compositionLayoutDetail.getText_style().equalsIgnoreCase("italic"))
                {
                    txtMarquee.setTypeface(null, Typeface.ITALIC);
                }
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("youtube"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice stop ");
                    stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            if(videoViewZoneTwo != null)
            {
                videoDestroyZoneTwo();
            }
            Log.d("zone one area 3layout", "<<<<< youtube video: "+compositionLayoutDetail.getYoutube_channel_data());
            mZoneTwoLayoutImg.setVisibility(View.GONE);
            mZoneTwoLayoutWeather.setVisibility(View.GONE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.GONE);
            mZoneTwoLayoutTwitter.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.GONE);
            mZoneTwoLayoutURL.setVisibility(View.GONE);
            mZoneTwoLayoutVideo.setVisibility(View.GONE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.VISIBLE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneTwoLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getYoutube_video_ids() != null)
            {
                String[] separated = compositionLayoutDetail.getYoutube_video_ids().split(",");
                getYoutubeAPIZoneTwoResponse(separated,300);
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("spreadsheets_url"))
        {
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            if(videoViewZoneTwo != null)
            {
                videoDestroyZoneTwo();
            }
            mZoneTwoLayoutImg.setVisibility(View.GONE);
            mZoneTwoLayoutWeather.setVisibility(View.GONE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.GONE);
            mZoneTwoLayoutTwitter.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.GONE);
            mZoneTwoLayoutURL.setVisibility(View.GONE);
            mZoneTwoLayoutVideo.setVisibility(View.GONE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.VISIBLE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneTwoLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getApp_url() != null)
            {
                WebView webView = (WebView) findViewById(R.id.zone_two_spreadsheets_webview);
                webView.setWebViewClient(new WebViewClientImpl());
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.loadUrl(compositionLayoutDetail.getApp_url());
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("google_slides_url"))
        {
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            if(videoViewZoneTwo != null)
            {
                videoDestroyZoneTwo();
            }
            mZoneTwoLayoutImg.setVisibility(View.GONE);
            mZoneTwoLayoutWeather.setVisibility(View.GONE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.GONE);
            mZoneTwoLayoutTwitter.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.GONE);
            mZoneTwoLayoutURL.setVisibility(View.GONE);
            mZoneTwoLayoutVideo.setVisibility(View.GONE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.VISIBLE);
            mZoneTwoLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getApp_url() != null)
            {
                WebView webView = (WebView) findViewById(R.id.zone_two_google_slides_webview);
                webView.setWebViewClient(new WebViewClientImpl());
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.loadUrl(compositionLayoutDetail.getApp_url());
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("rtsp"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(videoViewZoneTwo != null)
            {
                videoDestroyZoneTwo();
            }
            mZoneTwoLayoutImg.setVisibility(View.GONE);
            mZoneTwoLayoutWeather.setVisibility(View.GONE);
            mZoneTwoLayoutDigitalClock.setVisibility(View.GONE);
            mZoneTwoLayoutTwitter.setVisibility(View.GONE);
            mZoneTwoLayoutText.setVisibility(View.GONE);
            mZoneTwoLayoutURL.setVisibility(View.GONE);
            mZoneTwoLayoutVideo.setVisibility(View.GONE);
            mZoneTwoLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneTwoLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneTwoLayoutGoogleSlide.setVisibility(View.VISIBLE);
            mZoneTwoLayoutRTSP.setVisibility(View.VISIBLE);

            if(compositionLayoutDetail.getApp_url() != null) {
                Log.d(TAG, "Playing rtsp url zone 2: " + compositionLayoutDetail.getApp_url());

                mSurfaceZoneTwo = findViewById(R.id.zone_two_rtsp);
                WindowManager wm = this.getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                int iNewWidth = (int) (height * 3.0 / 4.0);
                FrameLayout.LayoutParams layoutParams =
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT);
                int iPos = width - iNewWidth;
                Log.d(TAG, "<<Playing back width " + width);
                Log.d(TAG, "<<Playing back height " + height);
                Log.d(TAG, "<<Playing back iNewWidth " + iNewWidth);
                Log.d(TAG, "<<Playing back iPos " + iPos);

//                layoutParams.setMargins(iPos, 0, 0, 0);
//                mSurfaceZoneTwo.getHolder().setFixedSize(width/2, height/2);
                mSurfaceZoneTwo.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                mSurfaceZoneTwo.getHolder().setKeepScreenOn(true);
//                mSurfaceZoneTwo.getHolder().addCallback(new SurceCallBack());
                mSurfaceZoneTwo.setLayoutParams(layoutParams);
                holderZoneTwo = mSurfaceZoneTwo.getHolder();
                //holder.addCallback(this);

                ArrayList<String> options = new ArrayList<String>();
                options.add("--aout=opensles");
                options.add("--audio-time-stretch"); // time stretching
                options.add("-vvv"); // verbosity
                options.add("--aout=opensles");
                options.add("--avcodec-codec=h264");
                options.add("--file-logging");
                options.add("--logfile=vlc-log.txt");

                libvlcZoneTwo = new LibVLC(getApplicationContext(), options);
                holderZoneTwo.setKeepScreenOn(true);

                // Create media player
                mMediaPlayerZoneTwo = new MediaPlayer(libvlcZoneTwo);
//                mMediaPlayerZoneTwo.setEventListener(mPlayerListenerZoneTwo);

                // Set up video output
                final IVLCVout vout = (IVLCVout) mMediaPlayerZoneTwo.getVLCVout();
                vout.setVideoView(mSurfaceZoneTwo);
                //vout.setSubtitlesView(mSurfaceSubtitles);
                vout.addCallback(this);
                vout.attachViews();
                Media m = new Media(libvlcZoneTwo, Uri.parse(compositionLayoutDetail.getApp_url()));
                mMediaPlayerZoneTwo.setMedia(m);
                mMediaPlayerZoneTwo.play();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getZoneThree(String media_id) throws JSONException, ParseException {
        mLoadingLayout.setVisibility(View.GONE);
        CompositionLayoutDetail compositionLayoutDetail = compositionLayoutDetailsThree.get(countZoneThree);
        countZoneThree++;
        countThree++;
        if(compositionLayoutDetail.getType().equalsIgnoreCase("image"))
        {
            if(mMediaPlayerZoneTwo != null)
            {
                releasePlayerZoneTwo();
            }
            if(videoViewZoneThree != null)
            {
                videoDestroyZoneThree();
            }
            mZoneThreeLayoutImg.setVisibility(View.VISIBLE);
            mZoneThreeLayoutWeather.setVisibility(View.GONE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.GONE);
            mZoneThreeLayoutTwitter.setVisibility(View.GONE);
            mZoneThreeLayoutVideo.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.GONE);
            mZoneThreeLayoutURL.setVisibility(View.GONE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneThreeLayoutRTSP.setVisibility(View.GONE);

            ImageView iv = findViewById(R.id.zone_three_img);

            new Thread(() -> {
                Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache();
            }).start();
            Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();

            if(compositionLayoutDetail.getUrl() != null)
            {
                int width = 0;
                int height = 0;
                if(compositionLayoutDetail.getProperties() != null)
                {
                    String property = compositionLayoutDetail.getProperties();
                    String[] propertyArr = property.split("px");
                    String[] parts = propertyArr[0].split("\\*"); // Splitting on asterisk (*), which is a special character in regex, so we need to escape it with \\
                    String propertyWidth = parts[0]; // Extracting the width
                    String propertyHeight = parts[1]; // Extracting the height

                    // Printing the width and height
                    System.out.println("<<<<< propertyArr width : "  + propertyWidth);
                    System.out.println("<<<<< propertyArr Height: " + propertyHeight);

                    width = Integer.parseInt(propertyWidth);
                    height = Integer.parseInt(propertyHeight);

                    Log.i(TAG, "<<<<< Width : " + String.valueOf(width) + "\n" + "Height : " + String.valueOf(height));
                    Log.i(TAG, "<<<<< Width imageview : " + String.valueOf(iv.getWidth()) + "\n" + "Height imageview : " + String.valueOf(iv.getHeight()));

                }
                else
                {
                    // on below line we are creating and initializing
                    // variable for display metrics.
                    DisplayMetrics displayMetrics = new DisplayMetrics();

                    // on below line we are getting metrics for display using window manager.
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                    // on below line we are getting height
                    // and width using display metrics.
                    height = displayMetrics.heightPixels;
                    width = displayMetrics.widthPixels;
                }

                ConnectivityManager cm = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
                String[] imageArray = compositionLayoutDetail.getUrl().split("/");

                boolean isDownloaded = ImageUtil.isImageDownloaded(this, Constant.FOLDER_NAME, imageArray[1]);
                if (isDownloaded) {
//                    Toast.makeText(this, "Image Downloaded", Toast.LENGTH_SHORT).show();
                    // The image is already downloaded
                    File imageFile = new File(getExternalFilesDir(Constant.FOLDER_NAME), imageArray[1]);
                    Glide.with(getApplicationContext())
//                            .load(imageFile)
                            .load(Constant.imgURL+compositionLayoutDetail.getUrl())
                            .fitCenter()
//                            .centerInside()
                            .override(width, height)
//                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Load failed", e);
                                    new Thread(() -> {
                                        Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache();
                                    }).start();
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();
                                    deleteImageFile(String.valueOf(imageFile));
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Handle the loaded resource
                                    Log.e("GlideSuccess", "Image Loaded");
                                    return false;
                                }
                            })
                            .into(iv);
                } else {
                    // Enqueue the download worker
                    String imageUrl = compositionLayoutDetail.getUrl();
                    String fileName = imageArray[1];
                    String folderName = Constant.FOLDER_NAME;

                    Data data = new Data.Builder()
                            .putString("imageUrl", Constant.imgURL+imageUrl)
                            .putString("fileName", fileName)
                            .putString("folderName", folderName)
                            .build();

                    OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(ImageDownloadWorker.class)
                            .setInputData(data)
                            .build();

                    WorkManager.getInstance(this).enqueue(downloadRequest);

                    Glide.with(getApplicationContext())
                            .load(Constant.imgURL+compositionLayoutDetail.getUrl())
                            .fitCenter()
//                            .centerInside()
                            .override(width, height)
//                            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Image Load failed", e);
                                    new Thread(() -> {
                                        Glide.get(CompositionLayoutThreeDBActivity.this).clearDiskCache();
                                    }).start();
                                    Glide.get(CompositionLayoutThreeDBActivity.this).clearMemory();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    // Handle the loaded resource
                                    Log.e("GlideSuccess", "Image Loaded");
                                    return false;
                                }
                            })
                            .into(iv);
                }

                if (width > height) {
                    Log.i(TAG, "<<<<<<<<<< image width > height  if");
                    iv.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT));
                    iv.setScaleType(ImageView.ScaleType.FIT_XY);
                }
                else
                {
                    Log.i(TAG, "<<<<<<<<<< image width < height  else");
                    iv.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            }
        }
        if(compositionLayoutDetail.getType().equalsIgnoreCase("digital_clock"))
        {
            if(mMediaPlayerZoneThree != null)
            {
                releasePlayerZoneThree();
            }
            if(videoViewZoneThree != null)
            {
                videoDestroyZoneThree();
            }
            mZoneThreeLayoutImg.setVisibility(View.GONE);
            mZoneThreeLayoutWeather.setVisibility(View.GONE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.VISIBLE);
            mZoneThreeLayoutTwitter.setVisibility(View.GONE);
            mZoneThreeLayoutVideo.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.GONE);
            mZoneThreeLayoutURL.setVisibility(View.GONE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneThreeLayoutRTSP.setVisibility(View.GONE);

            TextView tv_date = findViewById(R.id.zone_three_txt_digital_clock_date);
            TextView tv_time = findViewById(R.id.zone_three_txt_digital_clock_time);
            TextView tv_zone = findViewById(R.id.zone_three_txt_digital_clock_zone);

            if(compositionLayoutDetail.getTimezone() != null)
            {
                try {
                    //              Calendar c = Calendar.getInstance(TimeZone.getTimeZone(mJsonObject.getString("timezone")));
                    Calendar c = Calendar.getInstance(TimeZone.getTimeZone(compositionLayoutDetail.getTimezone()));
                    Date date = c.getTime(); //current date and time in UTC
                SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy");
//                    SimpleDateFormat df = new SimpleDateFormat("EEEE MMMM, dd yyyy hh:mma");
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm a");
                    df.setTimeZone(TimeZone.getTimeZone(compositionLayoutDetail.getTimezone())); //format in given timezone
                    String strDate = df.format(date);
                tf.setTimeZone(TimeZone.getTimeZone(compositionLayoutDetail.getTimezone())); //format in given timezone
                String strTime = tf.format(date);

                String[] days = new String[] { "SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY" };
                String day = days[c.get(Calendar.DAY_OF_WEEK)-1];
                tv_zone.setText(day);
//                                    tv_zone.setText(mJsonObject.getString("timezone"));
                tv_date.setText(strDate);
                tv_time.setText(strTime);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("weather"))
        {
            if(mMediaPlayerZoneThree != null)
            {
                releasePlayerZoneThree();
            }
            if(videoViewZoneThree != null)
            {
                videoDestroyZoneThree();
            }
            mZoneThreeLayoutImg.setVisibility(View.GONE);
            mZoneThreeLayoutWeather.setVisibility(View.VISIBLE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.GONE);
            mZoneThreeLayoutTwitter.setVisibility(View.GONE);
            mZoneThreeLayoutVideo.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.GONE);
            mZoneThreeLayoutURL.setVisibility(View.GONE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneThreeLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getTemp_unit() != null)
            {
                if(compositionLayoutDetail.getTemp_unit().equalsIgnoreCase("fahrenheit"))
                {
                    temp_type = "°F";
                }
                else
                {
                    temp_type = "℃";
                }
            }
            else
            {
                temp_type = "℃";
            }
            if(compositionLayoutDetail.getWeatherData() != null)
            {
                getWeatherDetailByCity(compositionLayoutDetail.getWeatherData(), 3);
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("twitter"))
        {
            if(mMediaPlayerZoneThree != null)
            {
                releasePlayerZoneThree();
            }
            if(videoViewZoneThree != null)
            {
                videoDestroyZoneThree();
            }
            mZoneThreeLayoutImg.setVisibility(View.GONE);
            mZoneThreeLayoutWeather.setVisibility(View.GONE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.GONE);
            mZoneThreeLayoutTwitter.setVisibility(View.VISIBLE);
            mZoneThreeLayoutVideo.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.GONE);
            mZoneThreeLayoutURL.setVisibility(View.GONE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneThreeLayoutRTSP.setVisibility(View.GONE);
            getTwitterAPILocalZoneThreeResponse(compositionLayoutDetail.getTwitterProfileData(),
                    compositionLayoutDetail.getTwitterFeedsList(),
                    3,
                    compositionLayoutDetail.getSlide_duration());
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("video"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneThree != null)
            {
                releasePlayerZoneThree();
            }
            mZoneThreeLayoutImg.setVisibility(View.GONE);
            mZoneThreeLayoutWeather.setVisibility(View.GONE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.GONE);
            mZoneThreeLayoutTwitter.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.GONE);
            mZoneThreeLayoutURL.setVisibility(View.GONE);
            mZoneThreeLayoutVideo.setVisibility(View.VISIBLE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneThreeLayoutRTSP.setVisibility(View.GONE);
            videoViewZoneThree = findViewById(R.id.zone_three_video);

            if(compositionLayoutDetail.getUrl() != null)
            {
                ConnectivityManager cm = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
                String[] videoArray = compositionLayoutDetail.getUrl().split("/");
                File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), videoArray[1]);
//                File imgFile = new  File("/storage/emulated/0/Movies/"+videoArray[1]);
                if(imgFile.exists()){
                    Log.i("ExternalStorage", "<<<<<<<<<< inside composition file exists: " +videoArray[1]);
                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Movies/"+videoArray[1]);
//                    Uri uri = Uri.parse(Constant.imgURL+compositionLayoutDetail.getUrl());
                    videoViewZoneThree.setVideoURI(uri);
                    videoViewZoneThree.setOnErrorListener((mp, what, extra) -> {
                        Toast.makeText(CompositionLayoutThreeDBActivity.this, "Download result error\n Downloading again.. ", Toast.LENGTH_LONG).show();
                        if (info != null && info.isConnected())
                        {
                            newDownload(compositionLayoutDetail.getUrl(), "VIDEO");
                        }
                        return false;
                    });
                }
                else
                {
                    if (info != null && info.isConnected())
                    {
                        newDownload(compositionLayoutDetail.getUrl(), "VIDEO");
                    }
                    Uri uri = Uri.parse(Constant.imgURL+compositionLayoutDetail.getUrl());
                    videoViewZoneThree.setVideoURI(uri);
                }
                videoViewZoneThree.requestFocus();
                videoViewZoneThree.start();
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("url"))
        {
            if(mMediaPlayerZoneThree != null)
            {
                releasePlayerZoneThree();
            }
            if(videoViewZoneThree != null)
            {
                videoDestroyZoneThree();
            }
            mZoneThreeLayoutImg.setVisibility(View.GONE);
            mZoneThreeLayoutWeather.setVisibility(View.GONE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.GONE);
            mZoneThreeLayoutTwitter.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.GONE);
            mZoneThreeLayoutURL.setVisibility(View.VISIBLE);
            mZoneThreeLayoutVideo.setVisibility(View.GONE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneThreeLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getApp_url() != null)
            {
                if(compositionLayoutDetail.getApp_url().contains("youtube.com"))
                {
                    if(mIsBGMedia.equalsIgnoreCase("1"))
                    {
                        if (isMyServiceRunning(MusicService.class)) {
                        } else {
                            System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                            stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                        }
                    }
                }
                WebView webView = (WebView) findViewById(R.id.zone_three_webview);
                String VideoEmbededAdress =
                    "<html>" +
                        "<body>" +
                            "<script type='text/javascript' src='http://www.youtube.com/iframe_api'></script><script type='text/javascript'>\n" +
                            "        var player;\n" +
                            "        function onYouTubeIframeAPIReady()\n" +
                            "        {player=new YT.Player('playerId',{events:{onReady:onPlayerReady}})}\n" +
                            "        function onPlayerReady(event){player.mute();player.setVolume(0);player.playVideo();}\n" +
                            "        </script>" +
                            "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 100%; " +
                            "padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" " +
                            "src="+compositionLayoutDetail.getApp_url()+
                            "?enablejsapi=1&rel=0&playsinline=1&autoplay=1&mute=1&showinfo=0&autohide=1&controls=0&modestbranding=1&vq="+videoQuality +
                            "&fs=0\" allow=\"autoplay;\" frameborder=\"0\">\n"+
                            "</iframe>\n "+
                        "</body>" +
                    "</html>";
                String mimeType = "text/html";
                String encoding = "UTF-8";//"base64";
                String USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";

                WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                        .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(this))
                        .build();

                webView.setWebViewClient(new WebViewClient() {
                    private WebView view;
                    private WebResourceRequest request;

                    public WebResourceResponse shouldInterceptRequest(WebView view,
                                                                      WebResourceRequest request) {
                        Log.d("YoutubeLayout3", "shouldOverrideUrlLoading: Url = [" + request.getUrl()+"]");
                        this.view = view;
                        this.request = request;
                        return assetLoader.shouldInterceptRequest(request.getUrl());
                    }
                });

                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setAllowContentAccess(true);
                webView.getSettings().setAllowFileAccess(true);
                webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                webView.getSettings().setUserAgentString(USERAGENT);//Important to auto play video
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.setWebChromeClient(new WebChromeClient());
                webView.setWebViewClient(new WebViewClient());
                webView.loadUrl(VideoEmbededAdress);
                webView.loadDataWithBaseURL("", VideoEmbededAdress, mimeType, encoding, "");
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("text_app"))
        {
            if(mMediaPlayerZoneThree != null)
            {
                releasePlayerZoneThree();
            }
            if(videoViewZoneThree != null)
            {
                videoDestroyZoneThree();
            }
            mZoneThreeLayoutImg.setVisibility(View.GONE);
            mZoneThreeLayoutWeather.setVisibility(View.GONE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.GONE);
            mZoneThreeLayoutTwitter.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.VISIBLE);
            mZoneThreeLayoutURL.setVisibility(View.GONE);
            mZoneThreeLayoutVideo.setVisibility(View.GONE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneThreeLayoutRTSP.setVisibility(View.GONE);

            LinearLayout ll_layout_one = findViewById(R.id.layout_one);
            LinearLayout ll_zone_three = findViewById(R.id.zone_three);
            LinearLayout ll_zone_three_text = findViewById(R.id.zone_three_layout_text);

            LinearLayout.LayoutParams childParam1 = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout.LayoutParams childParam2 = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            LinearLayout.LayoutParams childParam3 = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            /*if(compositionLayoutDetail.getScroll_font_size() != null)
            {
                if(Integer.parseInt(compositionLayoutDetail.getScroll_font_size()) > 18)
                {
                    childParam1.weight = 0.06f;
                    childParam2.weight = 0.94f;
                    childParam3.weight = 0.94f;
                }
                else if(Integer.parseInt(compositionLayoutDetail.getScroll_font_size()) > 26)
                {
                    childParam1.weight = 0.08f;
                    childParam2.weight = 0.92f;
                    childParam3.weight = 0.92f;
                }
                else if(Integer.parseInt(compositionLayoutDetail.getScroll_font_size()) > 36)
                {
                    childParam1.weight = 0.1f;
                    childParam2.weight = 0.9f;
                    childParam3.weight = 0.9f;
                }
                else if(Integer.parseInt(compositionLayoutDetail.getScroll_font_size()) > 48)
                {
                    childParam1.weight = 0.15f;
                    childParam2.weight = 0.85f;
                    childParam3.weight = 0.85f;
                }
            }
            else
            {
                childParam1.weight = 0.15f;
                childParam2.weight = 0.85f;
                childParam3.weight = 0.85f;
            }

            ll_layout_one.setLayoutParams(childParam3);
            ll_zone_three.setLayoutParams(childParam1);
            ll_zone_three_text.setLayoutParams(childParam1);*/

            TextView  txtMarquee = (TextView) findViewById(R.id.zone_three_text);
            LinearLayout linearLayout = findViewById(R.id.zone_three_text_layout);
//            linearLayout.setBackgroundResource(Color.parseColor(mJsonObject.getString("background_color")));
            if(compositionLayoutDetail.getBackground_color() != null)
            {
                linearLayout.setBackgroundColor(Color.parseColor(compositionLayoutDetail.getBackground_color()));
            }

            txtMarquee.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            if(compositionLayoutDetail.getText() != null)
            {
                txtMarquee.setText("\t \t \t \t \t \t\t \t \t \t \t \t"+compositionLayoutDetail.getText()+"\t \t \t \t \t \t\t \t \t \t \t \t"+compositionLayoutDetail.getText()+"\t \t \t \t \t \t\t \t \t \t \t \t"+compositionLayoutDetail.getText()+"\t \t \t \t \t \t\t \t \t \t \t \t");
            }

            if(compositionLayoutDetail.getText_color() != null)
            {
                txtMarquee.setTextColor(Color.parseColor(compositionLayoutDetail.getText_color()));
            }

            txtMarquee.setSelected(true);
            txtMarquee.setSingleLine(true);
            if(compositionLayoutDetail.getScroll_direction().equalsIgnoreCase("0"))
            {
                txtMarquee.setTextDirection(View.TEXT_DIRECTION_LTR);
                txtMarquee.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
            else
            {
                txtMarquee.setTextDirection(View.TEXT_DIRECTION_RTL);
                txtMarquee.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            }

//            txtMarquee.setSpeed(100.0f);
            if(compositionLayoutDetail.getScroll_font_size() != null)
            {
                txtMarquee.setTextSize(Integer.parseInt(compositionLayoutDetail.getScroll_font_size()));
            }
            else
            {
                txtMarquee.setTextSize(45.0F);
            }
//            txtMarquee.setTextSize(45.0F);
            try {
                Field field = txtMarquee.getClass().getDeclaredField("mMarquee");
                field.setAccessible(true);
                Object marquee = field.get(txtMarquee);
                if (marquee != null) {
                    Method method = marquee.getClass().getDeclaredMethod("setScrollSpeed", int.class);
                    method.setAccessible(true);
                    method.invoke(marquee, 120); // set the marquee speed to 120 pixels per second
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(compositionLayoutDetail.getText_style() != null)
            {
                if(compositionLayoutDetail.getText_style().equalsIgnoreCase("bold"))
                {
                    txtMarquee.setTypeface(null, Typeface.BOLD);
                }
                else if(compositionLayoutDetail.getText_style().equalsIgnoreCase("normal"))
                {
                    txtMarquee.setTypeface(null, Typeface.NORMAL);
                }
                else if(compositionLayoutDetail.getText_style().equalsIgnoreCase("regular"))
                {
                    txtMarquee.setTypeface(null, Typeface.NORMAL);
                }
                else if(compositionLayoutDetail.getText_style().equalsIgnoreCase("italic"))
                {
                    txtMarquee.setTypeface(null, Typeface.ITALIC);
                }
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("youtube"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice stop ");
                    stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneThree != null)
            {
                releasePlayerZoneThree();
            }
            if(videoViewZoneThree != null)
            {
                videoDestroyZoneThree();
            }
            Log.d("zone three area 3layout", "<<<<< youtube video: "+compositionLayoutDetail.getYoutube_channel_data());
            mZoneThreeLayoutImg.setVisibility(View.GONE);
            mZoneThreeLayoutWeather.setVisibility(View.GONE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.GONE);
            mZoneThreeLayoutTwitter.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.GONE);
            mZoneThreeLayoutURL.setVisibility(View.GONE);
            mZoneThreeLayoutVideo.setVisibility(View.GONE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.VISIBLE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneThreeLayoutRTSP.setVisibility(View.GONE);
            if(compositionLayoutDetail.getYoutube_video_ids() != null)
            {
                String[] separated = compositionLayoutDetail.getYoutube_video_ids().split(",");
                getYoutubeAPIZoneThreeResponse(separated,300);
            }
        }

        else if(compositionLayoutDetail.getType().equalsIgnoreCase("spreadsheets_url"))
        {
            mZoneThreeLayoutImg.setVisibility(View.GONE);
            mZoneThreeLayoutWeather.setVisibility(View.GONE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.GONE);
            mZoneThreeLayoutTwitter.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.GONE);
            mZoneThreeLayoutURL.setVisibility(View.GONE);
            mZoneThreeLayoutVideo.setVisibility(View.GONE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.VISIBLE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.GONE);
            mZoneThreeLayoutRTSP.setVisibility(View.GONE);
            if(mMediaPlayerZoneThree != null)
            {
                releasePlayerZoneThree();
            }
            if(videoViewZoneThree != null)
            {
                videoDestroyZoneThree();
            }
            if(compositionLayoutDetail.getApp_url() != null)
            {
                WebView webView = (WebView) findViewById(R.id.zone_three_spreadsheets_webview);
                webView.setWebViewClient(new WebViewClientImpl());
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.loadUrl(compositionLayoutDetail.getApp_url());
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("google_slides_url"))
        {
            if(mMediaPlayerZoneThree != null)
            {
                releasePlayerZoneThree();
            }
            if(videoViewZoneThree != null)
            {
                videoDestroyZoneThree();
            }
            mZoneThreeLayoutImg.setVisibility(View.GONE);
            mZoneThreeLayoutWeather.setVisibility(View.GONE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.GONE);
            mZoneThreeLayoutTwitter.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.GONE);
            mZoneThreeLayoutURL.setVisibility(View.GONE);
            mZoneThreeLayoutVideo.setVisibility(View.GONE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.VISIBLE);
            mZoneThreeLayoutRTSP.setVisibility(View.GONE);

            if(compositionLayoutDetail.getApp_url() != null)
            {
                WebView webView = (WebView) findViewById(R.id.zone_three_google_slides_webview);
                webView.setWebViewClient(new WebViewClientImpl());
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.loadUrl(compositionLayoutDetail.getApp_url());
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("rtsp"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
                }
            }
            if(videoViewZoneThree != null)
            {
                videoDestroyZoneThree();
            }
            mZoneThreeLayoutImg.setVisibility(View.GONE);
            mZoneThreeLayoutWeather.setVisibility(View.GONE);
            mZoneThreeLayoutDigitalClock.setVisibility(View.GONE);
            mZoneThreeLayoutTwitter.setVisibility(View.GONE);
            mZoneThreeLayoutText.setVisibility(View.GONE);
            mZoneThreeLayoutURL.setVisibility(View.GONE);
            mZoneThreeLayoutVideo.setVisibility(View.GONE);
            mZoneThreeLayoutYoutubeVideo.setVisibility(View.GONE);
            mZoneThreeLayoutSpreadSheet.setVisibility(View.GONE);
            mZoneThreeLayoutGoogleSlide.setVisibility(View.VISIBLE);
            mZoneThreeLayoutRTSP.setVisibility(View.VISIBLE);

            if(compositionLayoutDetail.getApp_url() != null)
            {

                Log.d(TAG, "Playing rtsp url zone 3: " + compositionLayoutDetail.getApp_url());

                mSurfaceZoneThree = findViewById(R.id.zone_three_rtsp);
                WindowManager wm = this.getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                int iNewWidth = (int) (height * 3.0 / 4.0);
                FrameLayout.LayoutParams layoutParams =
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT);
                int iPos = width - iNewWidth;
                Log.d(TAG, "<<Playing back width " + width);
                Log.d(TAG, "<<Playing back height " + height);
                Log.d(TAG, "<<Playing back iNewWidth " + iNewWidth);
                Log.d(TAG, "<<Playing back iPos " + iPos);

//                layoutParams.setMargins(iPos, 0, 0, 0);
                mSurfaceZoneThree.getHolder().setFixedSize(width/2, height/2);
                mSurfaceZoneThree.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                mSurfaceZoneThree.getHolder().setKeepScreenOn(true);
//                mSurfaceZoneThree.getHolder().addCallback(new SurceCallBack());
                mSurfaceZoneThree.setLayoutParams(layoutParams);
                holderZoneThree = mSurfaceZoneThree.getHolder();
                //holder.addCallback(this);

                ArrayList<String> options = new ArrayList<String>();
                options.add("--aout=opensles");
                options.add("--audio-time-stretch"); // time stretching
                options.add("-vvv"); // verbosity
                options.add("--aout=opensles");
                options.add("--avcodec-codec=h264");
                options.add("--file-logging");
                options.add("--logfile=vlc-log.txt");

                libvlcZoneThree = new LibVLC(getApplicationContext(), options);
                holderZoneThree.setKeepScreenOn(true);

                // Create media player
                mMediaPlayerZoneThree = new MediaPlayer(libvlcZoneThree);
//                mMediaPlayerZoneThree.setEventListener(mPlayerListenerZoneThree);

                // Set up video output
                final IVLCVout vout = (IVLCVout) mMediaPlayerZoneThree.getVLCVout();
                vout.setVideoView(mSurfaceZoneThree);
                //vout.setSubtitlesView(mSurfaceSubtitles);
                vout.addCallback(this);
                vout.attachViews();
                Media m = new Media(libvlcZoneThree, Uri.parse(compositionLayoutDetail.getApp_url()));
                mMediaPlayerZoneThree.setMedia(m);
                mMediaPlayerZoneThree.play();
            }
        }
    }

    private void getYoutubeAPIZoneOneResponse(String[] youtubeVideoID, int slide_duration) throws JSONException {
        if(youtubeVideoID != null)
        {
            YoutubeZoneOneAdapter adapter = new YoutubeZoneOneAdapter(CompositionLayoutThreeDBActivity.this,
                    youtubeVideoID);
            mVideoSliderViewZoneOne.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            mVideoSliderViewZoneOne.setScrollTimeInSec(slide_duration);
            mVideoSliderViewZoneOne.setAutoCycle(true);
            mVideoSliderViewZoneOne.startAutoCycle();
            mVideoSliderViewZoneOne.setSliderAdapter(adapter);
        }
    }

    private void getYoutubeAPIZoneTwoResponse(String[] youtubeVideo, int slide_duration) throws JSONException  {
        if(youtubeVideo != null)
        {
            YoutubeZoneTwoAdapter adapter = new YoutubeZoneTwoAdapter(CompositionLayoutThreeDBActivity.this,
                    youtubeVideo);
            mVideoSliderViewZoneTwo.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            mVideoSliderViewZoneTwo.setScrollTimeInSec(slide_duration);
            mVideoSliderViewZoneTwo.setAutoCycle(true);
            mVideoSliderViewZoneOne.startAutoCycle();
            mVideoSliderViewZoneTwo.setSliderAdapter(adapter);
        }
    }

    private void getYoutubeAPIZoneThreeResponse(String[] youtubeVideo, int slide_duration) throws JSONException  {
        if(youtubeVideo != null)
        {
            YoutubeZoneThreeAdapter adapter = new YoutubeZoneThreeAdapter(CompositionLayoutThreeDBActivity.this,
                    youtubeVideo);
            mVideoSliderViewZoneThree.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            mVideoSliderViewZoneThree.setScrollTimeInSec(slide_duration);
            mVideoSliderViewZoneThree.setAutoCycle(true);
            mVideoSliderViewZoneThree.startAutoCycle();
            mVideoSliderViewZoneThree.setSliderAdapter(adapter);
        }
    }


    private void getTwitterAPILocalZoneOneResponse(
            String  twitterProfileData,
            String  twitterFeedsList, int zone,
            String slide_duration) throws JSONException {

        if(twitterProfileData != null)
        {
            JSONObject jsonProfileDataResponse = new JSONObject(twitterProfileData);
            if(jsonProfileDataResponse != null)
            {
                JSONArray twitterArryaData = new JSONArray(jsonProfileDataResponse.getString("data"));
                if(twitterArryaData != null)
                {
                    for(int i = 0; i < twitterArryaData.length(); i++) {
                        JSONObject respTwitterObj = twitterArryaData.getJSONObject(i);
                        if(respTwitterObj != null)
                        {
                            ImageView imgProfile = findViewById(R.id.twitter_profile_img_zone_one);
                            TextView tvName = findViewById(R.id.twitter_user_name_txt_zone_one);
                            TextView tvUserName = findViewById(R.id.twitter_user_txt_zone_one);
                            tvName.setText((respTwitterObj.getString("name") != null) ?
                                    respTwitterObj.getString("name") : "");
                            tvUserName.setText((respTwitterObj.getString("username") != null) ?
                                    respTwitterObj.getString("username") : "");

                            if(respTwitterObj.getString("profile_image_url") != null)
                            {
                                Glide.with(getApplicationContext())
                                        .load(respTwitterObj.getString("profile_image_url"))
                                        .fitCenter()
                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                        .dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imgProfile);
                            }
                        }
                    }
                }
            }
        }

        if(twitterFeedsList != null)
        {
            JSONArray jsonTwitterListResponse = new JSONArray(twitterFeedsList);
            if(jsonTwitterListResponse != null)
            {
//            Log.d("TAG", "<< Response of twitterDetail "+twitterResponse.getMediaData().getSlideDuration());
                // passing this array list inside our adapter class.
                TwitterZoneOneAdapter adapter = new TwitterZoneOneAdapter(CompositionLayoutThreeDBActivity.this,
                        jsonTwitterListResponse);
                // below method is used to set auto cycle direction in left to
                // right direction you can change according to requirement.
                sliderView_zone_one.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

                // below method is use to set
                // scroll time in seconds.
                int duration = Integer.parseInt(slide_duration);
                sliderView_zone_one.setScrollTimeInSec(duration);

                // to set it scrollable automatically
                // we use below method.
                sliderView_zone_one.setAutoCycle(true);

                // to start autocycle below method is used.
                sliderView_zone_one.startAutoCycle();

                // below method is used to
                // setadapter to sliderview.
                sliderView_zone_one.setSliderAdapter(adapter);

            }
        }
    }


    private void getTwitterAPILocalZoneTwoResponse(String  twitterProfileData,
                                                   String  twitterFeedsList, int zone,
                                                   String slide_duration) throws JSONException  {

//        Log.d("TAG", "<< Response of json twitter local twitterProfileData>>>>> " +twitterProfileData);
//        Log.d("TAG", "<< Response of json twitter local twitterFeedsList>>>>> " +twitterFeedsList);
        if(twitterProfileData != null)
        {
            JSONObject jsonProfileDataResponse = new JSONObject(twitterProfileData);
            if(jsonProfileDataResponse != null)
            {
                JSONArray twitterArryaData = new JSONArray(jsonProfileDataResponse.getString("data"));
                if(twitterArryaData != null)
                {
                    for(int i = 0; i < twitterArryaData.length(); i++) {
                        JSONObject respTwitterObj = twitterArryaData.getJSONObject(i);
                        if(respTwitterObj != null)
                        {
                            ImageView imgProfile = findViewById(R.id.twitter_profile_img_zone_two);
                            TextView tvName = findViewById(R.id.twitter_user_name_txt_zone_two);
                            TextView tvUserName = findViewById(R.id.twitter_user_txt_zone_two);
                            tvName.setText((respTwitterObj.getString("name") != null) ?
                                    respTwitterObj.getString("name") : "");
                            tvUserName.setText((respTwitterObj.getString("username") != null) ?
                                    respTwitterObj.getString("username") : "");

                            if(respTwitterObj.getString("profile_image_url") != null)
                            {
                                Glide.with(getApplicationContext())
                                        .load(respTwitterObj.getString("profile_image_url"))
                                        .fitCenter()
                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                        .dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imgProfile);
                            }
                        }
                    }
                }
            }
        }

        if(twitterFeedsList != null)
        {
            JSONArray jsonTwitterListResponse = new JSONArray(twitterFeedsList);
            if(jsonTwitterListResponse != null)
            {
//            Log.d("TAG", "<< Response of twitterDetail "+twitterResponse.getMediaData().getSlideDuration());
                // passing this array list inside our adapter class.
                TwitterZoneTwoAdapter adapter = new TwitterZoneTwoAdapter(CompositionLayoutThreeDBActivity.this,
                        jsonTwitterListResponse);
                // below method is used to set auto cycle direction in left to
                // right direction you can change according to requirement.
                sliderView_zone_two.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

                // below method is use to set
                // scroll time in seconds.
                int duration = Integer.parseInt(slide_duration);
                sliderView_zone_two.setScrollTimeInSec(duration);

                // to set it scrollable automatically
                // we use below method.
                sliderView_zone_two.setAutoCycle(true);

                // to start autocycle below method is used.
                sliderView_zone_two.startAutoCycle();

                // below method is used to
                // setadapter to sliderview.
                sliderView_zone_two.setSliderAdapter(adapter);

            }
        }
    }


    private void getTwitterAPILocalZoneThreeResponse(String  twitterProfileData,
                                            String  twitterFeedsList, int zone,
                                            String slide_duration) throws JSONException  {

//        Log.d("TAG", "<< Response of json twitter local twitterProfileData>>>>> " +twitterProfileData);
//        Log.d("TAG", "<< Response of json twitter local twitterFeedsList>>>>> " +twitterFeedsList);
        if(twitterProfileData != null)
        {
            JSONObject jsonProfileDataResponse = new JSONObject(twitterProfileData);
            if(jsonProfileDataResponse != null)
            {
                JSONArray twitterArryaData = new JSONArray(jsonProfileDataResponse.getString("data"));
                if(twitterArryaData != null)
                {
                    for(int i = 0; i < twitterArryaData.length(); i++) {
                        JSONObject respTwitterObj = twitterArryaData.getJSONObject(i);
                        if(respTwitterObj != null)
                        {
                            ImageView imgProfile = findViewById(R.id.twitter_profile_img_zone_three);
                            TextView tvName = findViewById(R.id.twitter_user_name_txt_zone_three);
                            TextView tvUserName = findViewById(R.id.twitter_user_txt_zone_three);
                            tvName.setText((respTwitterObj.getString("name") != null) ?
                                    respTwitterObj.getString("name") : "");
                            tvUserName.setText((respTwitterObj.getString("username") != null) ?
                                    respTwitterObj.getString("username") : "");

                            if(respTwitterObj.getString("profile_image_url") != null)
                            {
                                Glide.with(getApplicationContext())
                                        .load(respTwitterObj.getString("profile_image_url"))
                                        .fitCenter()
                                        .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                        .dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(imgProfile);
                            }
                        }
                    }
                }
            }
        }

        if(twitterFeedsList != null)
        {
            JSONArray jsonTwitterListResponse = new JSONArray(twitterFeedsList);
            if(jsonTwitterListResponse != null)
            {
//            Log.d("TAG", "<< Response of twitterDetail "+twitterResponse.getMediaData().getSlideDuration());
                // passing this array list inside our adapter class.
                TwitterZoneThreeAdapter adapter = new TwitterZoneThreeAdapter(CompositionLayoutThreeDBActivity.this,
                        jsonTwitterListResponse);
                // below method is used to set auto cycle direction in left to
                // right direction you can change according to requirement.
                sliderView_zone_three.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);

                // below method is use to set
                // scroll time in seconds.
                int duration = Integer.parseInt(slide_duration);
                sliderView_zone_three.setScrollTimeInSec(duration);

                // to set it scrollable automatically
                // we use below method.
                sliderView_zone_three.setAutoCycle(true);

                // to start autocycle below method is used.
                sliderView_zone_three.startAutoCycle();

                // below method is used to
                // setadapter to sliderview.
                sliderView_zone_three.setSliderAdapter(adapter);

            }
        }
    }

    private void getWeatherDetailByCity(String weatherResponse, int zone) throws JSONException, ParseException {
        Log.d("TAG", "<< Response of layout 3 weather locationweatherResponse "+weatherResponse);
        if(weatherResponse != null) {
            JSONObject jsonResponse = new JSONObject(weatherResponse);
            if (jsonResponse != null) {

                JSONObject myCityJson = new JSONObject(jsonResponse.getString("city"));
                JSONArray weatherListJsonArr = new JSONArray(jsonResponse.getString("list"));
                JSONObject myJsonObject = weatherListJsonArr.getJSONObject(0);
                JSONObject myJson = new JSONObject(myJsonObject.getString("main"));
                JSONArray myJsonArr = new JSONArray(myJsonObject.getString("weather"));
                JSONObject mJsonObject = myJsonArr.getJSONObject(0);
//              System.out.println("===== res <<<<<<<<<<<< weather response : "+mJsonObject);

                /****************************************/
                JSONObject myJsonObject1 = weatherListJsonArr.getJSONObject(8);
                JSONObject myJson1 = new JSONObject(myJsonObject1.getString("main"));
                JSONArray myJsonArr1 = new JSONArray(myJsonObject1.getString("weather"));
                JSONObject mJsonObject1 = myJsonArr1.getJSONObject(0);

                JSONObject myJsonObject2 = weatherListJsonArr.getJSONObject(16);
                JSONObject myJson2 = new JSONObject(myJsonObject2.getString("main"));
                JSONArray myJsonArr2 = new JSONArray(myJsonObject2.getString("weather"));
                JSONObject mJsonObject2 = myJsonArr2.getJSONObject(0);

                JSONObject myJsonObject3 = weatherListJsonArr.getJSONObject(24);
                JSONObject myJson3 = new JSONObject(myJsonObject3.getString("main"));
                JSONArray myJsonArr3 = new JSONArray(myJsonObject3.getString("weather"));
                JSONObject mJsonObject3 = myJsonArr3.getJSONObject(0);

                JSONObject myJsonObject4 = weatherListJsonArr.getJSONObject(32);
                JSONObject myJson4 = new JSONObject(myJsonObject4.getString("main"));
                JSONArray myJsonArr4 = new JSONArray(myJsonObject4.getString("weather"));
                JSONObject mJsonObject4 = myJsonArr4.getJSONObject(0);

                TextView tv_weather_feel_zone_one = findViewById(R.id.zone_one_txt_weather_feel);
                ImageView icon_weather_zone_one = findViewById(R.id.zone_one_weather_icon);
                TextView tv_weather_city_zone_one = findViewById(R.id.zone_one_txt_weather_city);
                TextView tv_weather_temp_zone_one = findViewById(R.id.zone_one_txt_weather_temp);

                ImageView weather_icon_one_zone_one = findViewById(R.id.zone_one_weather_icon_one);
                TextView tv_weather_feel_one_zone_one = findViewById(R.id.zone_one_txt_weather_feel_one);
                TextView tv_weather_date_one_zone_one = findViewById(R.id.zone_one_txt_weather_date_one);
                TextView tv_weather_temp_one_zone_one = findViewById(R.id.zone_one_txt_weather_temp_one);

                ImageView weather_icon_two_zone_one = findViewById(R.id.zone_one_weather_icon_two);
                TextView tv_weather_feel_two_zone_one = findViewById(R.id.zone_one_txt_weather_feel_two);
                TextView tv_weather_date_two_zone_one = findViewById(R.id.zone_one_txt_weather_date_two);
                TextView tv_weather_temp_two_zone_one = findViewById(R.id.zone_one_txt_weather_temp_two);

                ImageView weather_icon_three_zone_one = findViewById(R.id.zone_one_weather_icon_three);
                TextView tv_weather_feel_three_zone_one = findViewById(R.id.zone_one_txt_weather_feel_three);
                TextView tv_weather_date_three_zone_one = findViewById(R.id.zone_one_txt_weather_date_three);
                TextView tv_weather_temp_three_zone_one = findViewById(R.id.zone_one_txt_weather_temp_three);

                ImageView weather_icon_four_zone_one = findViewById(R.id.zone_one_weather_icon_four);
                TextView tv_weather_feel_four_zone_one = findViewById(R.id.zone_one_txt_weather_feel_four);
                TextView tv_weather_date_four_zone_one = findViewById(R.id.zone_one_txt_weather_date_four);
                TextView tv_weather_temp_four_zone_one = findViewById(R.id.zone_one_txt_weather_temp_four);

                TextView tv_weather_feel_zone_two = findViewById(R.id.zone_two_txt_weather_feel);
                ImageView icon_weather_zone_two = findViewById(R.id.zone_two_weather_icon);
                TextView tv_weather_city_zone_two = findViewById(R.id.zone_two_txt_weather_city);
                TextView tv_weather_temp_zone_two = findViewById(R.id.zone_two_txt_weather_temp);

                ImageView weather_icon_one_zone_two = findViewById(R.id.zone_two_weather_icon_one);
                TextView tv_weather_feel_one_zone_two = findViewById(R.id.zone_two_txt_weather_feel_one);
                TextView tv_weather_date_one_zone_two = findViewById(R.id.zone_two_txt_weather_date_one);
                TextView tv_weather_temp_one_zone_two = findViewById(R.id.zone_two_txt_weather_temp_one);

                ImageView weather_icon_two_zone_two = findViewById(R.id.zone_two_weather_icon_two);
                TextView tv_weather_feel_two_zone_two = findViewById(R.id.zone_two_txt_weather_feel_two);
                TextView tv_weather_date_two_zone_two = findViewById(R.id.zone_two_txt_weather_date_two);
                TextView tv_weather_temp_two_zone_two = findViewById(R.id.zone_two_txt_weather_temp_two);

                ImageView weather_icon_three_zone_two = findViewById(R.id.zone_two_weather_icon_three);
                TextView tv_weather_feel_three_zone_two = findViewById(R.id.zone_two_txt_weather_feel_three);
                TextView tv_weather_date_three_zone_two = findViewById(R.id.zone_two_txt_weather_date_three);
                TextView tv_weather_temp_three_zone_two = findViewById(R.id.zone_two_txt_weather_temp_three);

                ImageView weather_icon_four_zone_two = findViewById(R.id.zone_two_weather_icon_four);
                TextView tv_weather_feel_four_zone_two = findViewById(R.id.zone_two_txt_weather_feel_four);
                TextView tv_weather_date_four_zone_two = findViewById(R.id.zone_two_txt_weather_date_four);
                TextView tv_weather_temp_four_zone_two = findViewById(R.id.zone_two_txt_weather_temp_four);

                TextView tv_weather_feel_zone_three = findViewById(R.id.zone_three_txt_weather_feel);
                ImageView icon_weather_zone_three = findViewById(R.id.zone_three_weather_icon);
                TextView tv_weather_city_zone_three = findViewById(R.id.zone_three_txt_weather_city);
                TextView tv_weather_temp_zone_three = findViewById(R.id.zone_three_txt_weather_temp);

                ImageView weather_icon_one_zone_three = findViewById(R.id.zone_three_weather_icon_one);
                TextView tv_weather_feel_one_zone_three = findViewById(R.id.zone_three_txt_weather_feel_one);
                TextView tv_weather_date_one_zone_three = findViewById(R.id.zone_three_txt_weather_date_one);
                TextView tv_weather_temp_one_zone_three = findViewById(R.id.zone_three_txt_weather_temp_one);

                ImageView weather_icon_two_zone_three = findViewById(R.id.zone_three_weather_icon_two);
                TextView tv_weather_feel_two_zone_three = findViewById(R.id.zone_three_txt_weather_feel_two);
                TextView tv_weather_date_two_zone_three = findViewById(R.id.zone_three_txt_weather_date_two);
                TextView tv_weather_temp_two_zone_three = findViewById(R.id.zone_three_txt_weather_temp_two);

                ImageView weather_icon_three_zone_three = findViewById(R.id.zone_three_weather_icon_three);
                TextView tv_weather_feel_three_zone_three = findViewById(R.id.zone_three_txt_weather_feel_three);
                TextView tv_weather_date_three_zone_three = findViewById(R.id.zone_three_txt_weather_date_three);
                TextView tv_weather_temp_three_zone_three = findViewById(R.id.zone_three_txt_weather_temp_three);

                ImageView weather_icon_four_zone_three = findViewById(R.id.zone_three_weather_icon_four);
                TextView tv_weather_feel_four_zone_three = findViewById(R.id.zone_three_txt_weather_feel_four);
                TextView tv_weather_date_four_zone_three = findViewById(R.id.zone_three_txt_weather_date_four);
                TextView tv_weather_temp_four_zone_three = findViewById(R.id.zone_three_txt_weather_temp_four);

                if (zone == 1) {
                    if(mJsonObject.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_one);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_one);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_one);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_one);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_one);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_one);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_one);
                    }

                    if(mJsonObject1.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_one);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_one);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_one);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_one);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_one);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_one);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_one);
                    }



                    if(mJsonObject2.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_one);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_one);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_one);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_one);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_one);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_one);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_one);
                    }



                    if(mJsonObject3.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_one);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_one);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_one);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_one);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_one);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_one);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_one);
                    }




                    if(mJsonObject4.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_one);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_one);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_one);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_one);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_one);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_one);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_one);
                    }

                    float temp = (float) (Float.parseFloat(myJson.getString("temp")));
                    float feel = (float) (Float.parseFloat(myJson.getString("feels_like")));

                    tv_weather_feel_zone_one.setText("feel like: " + Double.valueOf(dtime.format(feel)) + temp_type);
                    tv_weather_city_zone_one.setText(myCityJson.getString("name"));
                    tv_weather_temp_zone_one.setText(Double.valueOf(dtime.format(temp)) + temp_type);

                    String weatherDateOne = myJsonObject1.getString("dt_txt");
                    String weatherDateTwo = myJsonObject2.getString("dt_txt");
                    String weatherDateThree = myJsonObject3.getString("dt_txt");
                    String weatherDateFour = myJsonObject4.getString("dt_txt");
                    SimpleDateFormat dateFormatprev1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat dateFormatprev2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat dateFormatprev3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat dateFormatprev4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d1 = dateFormatprev1.parse(weatherDateOne);
                    Date d2 = dateFormatprev2.parse(weatherDateTwo);
                    Date d3 = dateFormatprev3.parse(weatherDateThree);
                    Date d4 = dateFormatprev4.parse(weatherDateFour);
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEE dd MMM");
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE dd MMM");
                    SimpleDateFormat dateFormat3 = new SimpleDateFormat("EEE dd MMM");
                    SimpleDateFormat dateFormat4 = new SimpleDateFormat("EEE dd MMM");
                    String weatherDateStrOne = dateFormat1.format(d1);
                    String weatherDateStrTwo = dateFormat2.format(d2);
                    String weatherDateStrThree = dateFormat3.format(d3);
                    String weatherDateStrFour = dateFormat4.format(d4);

                    float temp1 = (float) (Float.parseFloat(myJson1.getString("temp")));
                    float feel1 = (float) (Float.parseFloat(myJson1.getString("feels_like")));

                    float temp2 = (float) (Float.parseFloat(myJson2.getString("temp")));
                    float feel2 = (float) (Float.parseFloat(myJson2.getString("feels_like")));

                    float temp3 = (float) (Float.parseFloat(myJson3.getString("temp")));
                    float feel3 = (float) (Float.parseFloat(myJson3.getString("feels_like")));

                    float temp4 = (float) (Float.parseFloat(myJson4.getString("temp")));
                    float feel4 = (float) (Float.parseFloat(myJson4.getString("feels_like")));

                    tv_weather_feel_one_zone_one.setText("feel like: " + Double.valueOf(dtime.format(feel1)) + temp_type);
                    tv_weather_date_one_zone_one.setText(weatherDateStrOne);
                    tv_weather_temp_one_zone_one.setText(Double.valueOf(dtime.format(temp1)) + temp_type);

                    tv_weather_feel_two_zone_one.setText("feel like: " + Double.valueOf(dtime.format(feel2)) + temp_type);
                    tv_weather_date_two_zone_one.setText(weatherDateStrTwo);
                    tv_weather_temp_two_zone_one.setText(Double.valueOf(dtime.format(temp2)) + temp_type);

                    tv_weather_feel_three_zone_one.setText("feel like: " + Double.valueOf(dtime.format(feel3)) + temp_type);
                    tv_weather_date_three_zone_one.setText(weatherDateStrThree);
                    tv_weather_temp_three_zone_one.setText(Double.valueOf(dtime.format(temp3)) + temp_type);

                    tv_weather_feel_four_zone_one.setText("feel like: " + Double.valueOf(dtime.format(feel4)) + temp_type);
                    tv_weather_date_four_zone_one.setText(weatherDateStrFour);
                    tv_weather_temp_four_zone_one.setText(Double.valueOf(dtime.format(temp4)) + temp_type);
                } else if (zone == 2) {
                    if(mJsonObject.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_two);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_two);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_two);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_two);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_two);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_two);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_two);
                    }

                    if(mJsonObject1.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_two);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_two);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_two);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_two);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_two);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_two);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_two);
                    }



                    if(mJsonObject2.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_two);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_two);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_two);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_two);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_two);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_two);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_two);
                    }



                    if(mJsonObject3.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_two);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_two);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_two);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_two);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_two);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_two);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_two);
                    }




                    if(mJsonObject4.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_two);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_two);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_two);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_two);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_two);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_two);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_two);
                    }
                    float temp = (float) (Float.parseFloat(myJson.getString("temp")));
                    float feel = (float) (Float.parseFloat(myJson.getString("feels_like")));
                    tv_weather_feel_zone_two.setText("feel like: " + Double.valueOf(dtime.format(feel)) + temp_type);
                    tv_weather_city_zone_two.setText(myCityJson.getString("name"));
                    tv_weather_temp_zone_two.setText(Double.valueOf(dtime.format(temp)) + temp_type);

                    String weatherDateOne = myJsonObject1.getString("dt_txt");
                    String weatherDateTwo = myJsonObject2.getString("dt_txt");
                    String weatherDateThree = myJsonObject3.getString("dt_txt");
                    String weatherDateFour = myJsonObject4.getString("dt_txt");
                    SimpleDateFormat dateFormatprev1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat dateFormatprev2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat dateFormatprev3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat dateFormatprev4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d1 = dateFormatprev1.parse(weatherDateOne);
                    Date d2 = dateFormatprev2.parse(weatherDateTwo);
                    Date d3 = dateFormatprev3.parse(weatherDateThree);
                    Date d4 = dateFormatprev4.parse(weatherDateFour);
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEE dd MMM");
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE dd MMM");
                    SimpleDateFormat dateFormat3 = new SimpleDateFormat("EEE dd MMM");
                    SimpleDateFormat dateFormat4 = new SimpleDateFormat("EEE dd MMM");
                    String weatherDateStrOne = dateFormat1.format(d1);
                    String weatherDateStrTwo = dateFormat2.format(d2);
                    String weatherDateStrThree = dateFormat3.format(d3);
                    String weatherDateStrFour = dateFormat4.format(d4);

                    float temp1 = (float) (Float.parseFloat(myJson1.getString("temp")));
                    float feel1 = (float) (Float.parseFloat(myJson1.getString("feels_like")));

                    float temp2 = (float) (Float.parseFloat(myJson2.getString("temp")));
                    float feel2 = (float) (Float.parseFloat(myJson2.getString("feels_like")));

                    float temp3 = (float) (Float.parseFloat(myJson3.getString("temp")));
                    float feel3 = (float) (Float.parseFloat(myJson3.getString("feels_like")));

                    float temp4 = (float) (Float.parseFloat(myJson4.getString("temp")));
                    float feel4 = (float) (Float.parseFloat(myJson4.getString("feels_like")));

                    tv_weather_feel_one_zone_two.setText("feel like: " + Double.valueOf(dtime.format(feel1)) + temp_type);
                    tv_weather_date_one_zone_two.setText(weatherDateStrOne);
                    tv_weather_temp_one_zone_two.setText(Double.valueOf(dtime.format(temp1)) + temp_type);

                    tv_weather_feel_two_zone_two.setText("feel like: " + Double.valueOf(dtime.format(feel2)) + temp_type);
                    tv_weather_date_two_zone_two.setText(weatherDateStrTwo);
                    tv_weather_temp_two_zone_two.setText(Double.valueOf(dtime.format(temp2)) + temp_type);

                    tv_weather_feel_three_zone_two.setText("feel like: " + Double.valueOf(dtime.format(feel3)) + temp_type);
                    tv_weather_date_three_zone_two.setText(weatherDateStrThree);
                    tv_weather_temp_three_zone_two.setText(Double.valueOf(dtime.format(temp3)) + temp_type);

                    tv_weather_feel_four_zone_two.setText("feel like: " + Double.valueOf(dtime.format(feel4)) + temp_type);
                    tv_weather_date_four_zone_two.setText(weatherDateStrFour);
                    tv_weather_temp_four_zone_two.setText(Double.valueOf(dtime.format(temp4)) + temp_type);
                } else if (zone == 3) {
                    if(mJsonObject.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_three);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_three);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_three);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_three);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_three);
                    }
                    else if(mJsonObject.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_three);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(icon_weather_zone_three);
                    }



                    if(mJsonObject1.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_three);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_three);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_three);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_three);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_three);
                    }
                    else if(mJsonObject1.getString("main").equalsIgnoreCase("Thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_three);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_one_zone_three);
                    }



                    if(mJsonObject2.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_three);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_three);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_three);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_three);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_three);
                    }
                    else if(mJsonObject2.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_three);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_two_zone_three);
                    }

                    if(mJsonObject3.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_three);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_three);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_three);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_three);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_three);
                    }
                    else if(mJsonObject3.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_three);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_three_zone_three);
                    }




                    if(mJsonObject4.getString("main").equalsIgnoreCase("Clouds"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.cloudy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_three);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Rain"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.rainy)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_three);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Snow"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.snow)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_three);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Fog"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.mist)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_three);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("Clear"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_three);
                    }
                    else if(mJsonObject4.getString("main").equalsIgnoreCase("thunderstorm"))
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.thunderstorm)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_three);
                    }
                    else
                    {
                        Glide.with(getApplicationContext())
                                .load(R.drawable.clear)
                                .fitCenter()
                                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(weather_icon_four_zone_three);
                    }

                    float temp = (float) (Float.parseFloat(myJson.getString("temp")));
                    float feel = (float) (Float.parseFloat(myJson.getString("feels_like")));

                    tv_weather_feel_zone_three.setText("feel like: " + Double.valueOf(dtime.format(feel)) + temp_type);
                    tv_weather_city_zone_three.setText(myCityJson.getString("name"));
                    tv_weather_temp_zone_three.setText(Double.valueOf(dtime.format(temp)) + temp_type);

                    String weatherDateOne = myJsonObject1.getString("dt_txt");
                    String weatherDateTwo = myJsonObject2.getString("dt_txt");
                    String weatherDateThree = myJsonObject3.getString("dt_txt");
                    String weatherDateFour = myJsonObject4.getString("dt_txt");
                    SimpleDateFormat dateFormatprev1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat dateFormatprev2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat dateFormatprev3 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    SimpleDateFormat dateFormatprev4 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date d1 = dateFormatprev1.parse(weatherDateOne);
                    Date d2 = dateFormatprev2.parse(weatherDateTwo);
                    Date d3 = dateFormatprev3.parse(weatherDateThree);
                    Date d4 = dateFormatprev4.parse(weatherDateFour);
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("EEE dd MMM");
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("EEE dd MMM");
                    SimpleDateFormat dateFormat3 = new SimpleDateFormat("EEE dd MMM");
                    SimpleDateFormat dateFormat4 = new SimpleDateFormat("EEE dd MMM");
                    String weatherDateStrOne = dateFormat1.format(d1);
                    String weatherDateStrTwo = dateFormat2.format(d2);
                    String weatherDateStrThree = dateFormat3.format(d3);
                    String weatherDateStrFour = dateFormat4.format(d4);

                    float temp1 = (float) (Float.parseFloat(myJson1.getString("temp")));
                    float feel1 = (float) (Float.parseFloat(myJson1.getString("feels_like")));

                    float temp2 = (float) (Float.parseFloat(myJson2.getString("temp")));
                    float feel2 = (float) (Float.parseFloat(myJson2.getString("feels_like")));

                    float temp3 = (float) (Float.parseFloat(myJson3.getString("temp")));
                    float feel3 = (float) (Float.parseFloat(myJson3.getString("feels_like")));

                    float temp4 = (float) (Float.parseFloat(myJson4.getString("temp")));
                    float feel4 = (float) (Float.parseFloat(myJson4.getString("feels_like")));

                    tv_weather_feel_one_zone_three.setText("feel like: " + Double.valueOf(dtime.format(feel1)) + temp_type);
                    tv_weather_date_one_zone_three.setText(weatherDateStrOne);
                    tv_weather_temp_one_zone_three.setText(Double.valueOf(dtime.format(temp1)) + temp_type);

                    tv_weather_feel_two_zone_three.setText("feel like: " + Double.valueOf(dtime.format(feel2)) + temp_type);
                    tv_weather_date_two_zone_three.setText(weatherDateStrTwo);
                    tv_weather_temp_two_zone_three.setText(Double.valueOf(dtime.format(temp2)) + temp_type);

                    tv_weather_feel_three_zone_three.setText("feel like: " + Double.valueOf(dtime.format(feel3)) + temp_type);
                    tv_weather_date_three_zone_three.setText(weatherDateStrThree);
                    tv_weather_temp_three_zone_three.setText(Double.valueOf(dtime.format(temp3)) + temp_type);

                    tv_weather_feel_four_zone_three.setText("feel like: " + Double.valueOf(dtime.format(feel4)) + temp_type);
                    tv_weather_date_four_zone_three.setText(weatherDateStrFour);
                    tv_weather_temp_four_zone_three.setText(Double.valueOf(dtime.format(temp4)) + temp_type);

                }
            }
        }
    }

    public void updateCompositionLayoutDetail() {
        compositionLayoutDetailViewModel = new ViewModelProvider(this).get(CompositionLayoutDetailViewModel.class);
        compositionLayoutDetailViewModel.getCompositionLayoutDetail().observe(this, new Observer<List<CompositionLayoutDetail>>() {
            @Override
            public void onChanged(List<CompositionLayoutDetail> compositionLayoutDetail) {
                if (compositionLayoutDetail == null) {
                    getCompositionResponse(DeviceID, getIntent().getStringExtra("COMPOSITION_ID"));
                    return;
                }

                for(int i = 0; i< compositionLayoutDetail.size(); i++)
                {
                    if(compositionLayoutDetail.get(0).getBGMusicUrl() != null)
                    {
                        mBGMediaUrl = (compositionLayoutDetail.get(0).getBGMusicUrl() != null ?
                                compositionLayoutDetail.get(0).getBGMusicUrl() : "");
                        ApplicationPreferences.getInstance(CompositionLayoutThreeDBActivity.this)
                                .setBackgroundMusic(compositionLayoutDetail.get(0).getBGMusicUrl());
                        mIsBGMedia = (compositionLayoutDetail.get(0).isBGMusicPause() != null
                                ? compositionLayoutDetail.get(0).isBGMusicPause()
                                : "0");
                        ApplicationPreferences.getInstance(CompositionLayoutThreeDBActivity.this)
                                .setBackgroundMusicStatus(mIsBGMedia);
                        ApplicationPreferences.getInstance(CompositionLayoutThreeDBActivity.this)
                                .setBackgroundMusicCount(0);
                    }
                    if(compositionLayoutDetail.get(i).getZone_type().equalsIgnoreCase("zone1"))
                    {
                        CompositionLayoutDetail layoutDetail1 = compositionLayoutDetail.get(i);
                        if(layoutDetail1 != null)
                        {
                            compositionLayoutDetailsOne.add(layoutDetail1);
                            mZoneOneList.add(compositionLayoutDetail.get(i).getMedia_id());
                            mZoneOneDurationList.add(compositionLayoutDetail.get(i).getDuration());
                        }
                    }
                    else if(compositionLayoutDetail.get(i).getZone_type().equalsIgnoreCase("zone2"))
                    {
                        CompositionLayoutDetail layoutDetail2 = compositionLayoutDetail.get(i);
                        if(layoutDetail2 != null)
                        {
                            compositionLayoutDetailsTwo.add(layoutDetail2);
                            mZoneTwoList.add(compositionLayoutDetail.get(i).getMedia_id());
                            mZoneTwoDurationList.add(compositionLayoutDetail.get(i).getDuration());
                        }
                    }
                    else if(compositionLayoutDetail.get(i).getZone_type().equalsIgnoreCase("zone3"))
                    {
                        CompositionLayoutDetail layoutDetail3 = compositionLayoutDetail.get(i);
                        if(layoutDetail3 != null)
                        {
                            compositionLayoutDetailsThree.add(layoutDetail3);
                            mZoneThreeList.add(compositionLayoutDetail.get(i).getMedia_id());
                            mZoneThreeDurationList.add(compositionLayoutDetail.get(i).getDuration());
                        }
                    }
                }

                if(mZoneOneList.size()>0 || mZoneTwoList.size()>0 || mZoneThreeList.size()>0)
                {
                    if (mFlag == 0) {
                        zoneOneHandler();
                        zoneTwoHandler();
                        zoneThreeHandler();
                    }
                }
                else {
                    getCompositionResponse(DeviceID, getIntent().getStringExtra("COMPOSITION_ID"));
                }
            }
        });
    }

    private void handleCompositionLayoutDetails(List<CompositionLayoutDetail> compositionLayoutDetail) {
        // do something with the composition layout details
    }

    private void handleBackgroundMedia(List<CompositionLayoutDetail> compositionLayoutDetail) {
        if(compositionLayoutDetail.size()>0)
        {
            if (compositionLayoutDetail.get(0).getBGMusicUrl() != null) {
                mBGMediaUrl = compositionLayoutDetail.get(0).getBGMusicUrl();
                mIsBGMedia = compositionLayoutDetail.get(0).isBGMusicPause() != null ? compositionLayoutDetail.get(0).isBGMusicPause() : "0";
            }
        }
    }

    private void handleZoneOne(List<CompositionLayoutDetail> compositionLayoutDetail) {
        if(compositionLayoutDetail != null)
        {
            for (CompositionLayoutDetail detail : compositionLayoutDetail) {
                if (detail.getZone_type().equalsIgnoreCase("zone1")) {
                    compositionLayoutDetailsOne.add(detail);
                    mZoneOneList.add(detail.getMedia_id());
                    mZoneOneDurationList.add(detail.getDuration());
                }
            }
        }
    }

    private void handleZoneTwo(List<CompositionLayoutDetail> compositionLayoutDetail) {
        if(compositionLayoutDetail != null)
        {
            for (CompositionLayoutDetail detail : compositionLayoutDetail) {
                if (detail.getZone_type().equalsIgnoreCase("zone2")) {
                    compositionLayoutDetailsTwo.add(detail);
                    mZoneTwoList.add(detail.getMedia_id());
                    mZoneTwoDurationList.add(detail.getDuration());
                }
            }
        }
    }

    private void handleZoneThree(List<CompositionLayoutDetail> compositionLayoutDetail) {
        if(compositionLayoutDetail != null)
        {
            for (CompositionLayoutDetail detail : compositionLayoutDetail) {
                if (detail.getZone_type().equalsIgnoreCase("zone3")) {
                    compositionLayoutDetailsThree.add(detail);
                    mZoneThreeList.add(detail.getMedia_id());
                    mZoneThreeDurationList.add(detail.getDuration());
                }
            }
        }
    }


    public void getCompositionResponse(String deviceID, String compositionID) {
        Log.d(TAG, "<< Response inside getCompositionResponse ");
        if (compositionID != null) {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
            if (info != null && info.isConnected()) {
                RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
                Call<CompositionLayoutResponse> call = apiService.getCompositionLayoutResponse(DeviceID, compositionID);
                call.enqueue(new retrofit2.Callback<CompositionLayoutResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<CompositionLayoutResponse> call, Response<CompositionLayoutResponse> response) {
                        if (response.isSuccessful()) {
                            CompositionLayoutResponse compositionLayoutResponse = response.body();
                            if (compositionLayoutResponse != null) {
                                List<CompositionLayoutDetail> layoutDetailList = compositionLayoutResponse.getCompositionLayoutDetail();
                                Log.d(TAG, "<< Response layoutDetailList online "+layoutDetailList);
                                if (layoutDetailList != null) {
                                    for (int i = 0; i < layoutDetailList.size(); i++) {
                                        CompositionLayoutDetail compositionLayoutDetail = layoutDetailList.get(i);
                                        if (compositionLayoutDetail != null) {
                                            Log.d(TAG, "<< Response layoutDetail online "+compositionLayoutDetail);
                                            compositionLayoutRepository.insert(compositionLayoutDetail);
                                            if(compositionLayoutDetail.getBGMusicUrl() != null)
                                            {
                                                mBGMediaUrl = (compositionLayoutDetail.getBGMusicUrl() != null ?
                                                        compositionLayoutDetail.getBGMusicUrl() : "");
                                                ApplicationPreferences.getInstance(CompositionLayoutThreeDBActivity.this)
                                                        .setBackgroundMusic(compositionLayoutDetail.getBGMusicUrl());
                                                mIsBGMedia = (compositionLayoutDetail.isBGMusicPause() != null
                                                        ? compositionLayoutDetail.isBGMusicPause()
                                                        : "0");
                                                ApplicationPreferences.getInstance(CompositionLayoutThreeDBActivity.this)
                                                        .setBackgroundMusicStatus(mIsBGMedia);
                                                ApplicationPreferences.getInstance(CompositionLayoutThreeDBActivity.this)
                                                        .setBackgroundMusicCount(0);
                                            }
                                            if(compositionLayoutDetail.getZone_type().equalsIgnoreCase("zone1"))
                                            {
                                                CompositionLayoutDetail layoutDetail1 = compositionLayoutDetail;
                                                if(layoutDetail1 != null)
                                                {
                                                    compositionLayoutDetailsOne.add(layoutDetail1);
                                                    mZoneOneList.add(compositionLayoutDetail.getMedia_id());
                                                    mZoneOneDurationList.add(compositionLayoutDetail.getDuration());
                                                }
                                            }
                                            else if(compositionLayoutDetail.getZone_type().equalsIgnoreCase("zone2"))
                                            {
                                                CompositionLayoutDetail layoutDetail2 = compositionLayoutDetail;
                                                if(layoutDetail2 != null)
                                                {
                                                    compositionLayoutDetailsTwo.add(layoutDetail2);
                                                    mZoneTwoList.add(compositionLayoutDetail.getMedia_id());
                                                    mZoneTwoDurationList.add(compositionLayoutDetail.getDuration());
                                                }
                                            }
                                            else if(compositionLayoutDetail.getZone_type().equalsIgnoreCase("zone3"))
                                            {
                                                CompositionLayoutDetail layoutDetail3 = compositionLayoutDetail;
                                                if(layoutDetail3 != null)
                                                {
                                                    compositionLayoutDetailsThree.add(layoutDetail3);
                                                    mZoneThreeList.add(compositionLayoutDetail.getMedia_id());
                                                    mZoneThreeDurationList.add(compositionLayoutDetail.getDuration());
                                                }
                                            }
                                        }
                                    }
                                    // Update the LiveData object with the new data
//                                    updateCompositionLayoutDetail();
                                    if(mZoneOneList.size()>0 || mZoneTwoList.size()>0 || mZoneThreeList.size()>0)
                                    {
                                        if (mFlag == 0) {
                                            zoneOneHandler();
                                            zoneTwoHandler();
                                            zoneThreeHandler();
                                        }
                                    }
                                    else {
                                        getCompositionResponse(DeviceID, getIntent().getStringExtra("COMPOSITION_ID"));
                                    }
                                }
                            } else {
                                // Handle null response
                                Log.d(TAG, "Composition layout response body is null");
                            }
                        } else {
                            // Handle unsuccessful response
                            Log.d(TAG, "Composition layout response unsuccessful");
                        }
                    }

                    @Override
                    public void onFailure(Call<CompositionLayoutResponse> call, Throwable t) {
                        Log.e(TAG, "Failed to get composition layout details: " + t.getMessage());
                    }
                });
            } else {
                Log.e(TAG, "No active network connection available");
            }
        } else {
            Log.e(TAG, "Composition ID is null");
        }
    }

    private void deviceStatus(String status, String method) {
        Log.d(TAG, "<< device status is: "+status +" << device method is: "+method+" << device layout is 3");

        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
        if (info != null && info.isConnected())
        {
            RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
            Call<DeviceStatusCheckReponse> call = apiService.getDeviceStatusCheckResponse(
                    FirebaseInstanceId.getInstance().getToken(),
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
            Log.d(TAG, "<< Response of compositionLayoutOneResponse offline ");
        }
    }

    private class WebViewClientImpl  extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
            startActivity(new Intent(CompositionLayoutThreeDBActivity.this, SplashActivity.class));
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

    public void setDeleteDatabase()
    {
        Log.d(TAG, "<< push status in layout 3:  " + ApplicationPreferences.getInstance(getApplicationContext()).getNotificationReceive());
        Log.d(TAG, "<< push status in layout 3 COMPOSITION_ID:  " + getIntent().getStringExtra("COMPOSITION_ID"));
        Log.d(TAG, "<< push status in layout 3 ORIENTATION:  " + getIntent().getStringExtra("ORIENTATION"));

        compositionLayoutDetailViewModel = new ViewModelProvider(this).get(CompositionLayoutDetailViewModel.class);
        compositionLayoutDetailViewModel.deleteAll();

        ScreenViewModel screenViewModel = new ViewModelProvider(this).get(ScreenViewModel.class);
        screenViewModel.deleteAll();
        ApplicationPreferences.getInstance(getApplicationContext()).setNotificationReceive(false);
        File directory = new File(getExternalFilesDir(null), Constant.FOLDER_NAME);
        FileUtils.deleteDirectory(directory);
        deleteCache(this);
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSurfacesCreated(IVLCVout vlcVout) {

    }

    @Override
    public void onSurfacesDestroyed(IVLCVout vlcVout) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void videoDestroyZoneOne() {
        if (videoViewZoneOne == null)
            return;

        if (videoViewZoneOne != null) {
            videoViewZoneOne.stopPlayback();
        }
    }

    public void videoDestroyZoneTwo() {
        if (videoViewZoneTwo == null)
            return;

        if (videoViewZoneTwo != null) {
            videoViewZoneTwo.stopPlayback();
        }
    }

    public void videoDestroyZoneThree() {
        if (videoViewZoneThree == null)
            return;

        if (videoViewZoneThree != null) {
            videoViewZoneThree.stopPlayback();
        }
    }

    public void releasePlayerZoneOne() {
        if (libvlcZoneOne == null)
            return;
        mMediaPlayerZoneOne.stop();
        final IVLCVout vout = mMediaPlayerZoneOne.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        holderZoneOne = null;
        libvlcZoneOne.release();
        libvlcZoneOne = null;
    }

    public void releasePlayerZoneTwo() {
        if (libvlcZoneTwo == null)
            return;
        mMediaPlayerZoneTwo.stop();
        final IVLCVout vout = (IVLCVout) mMediaPlayerZoneTwo.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        holderZoneTwo = null;
        libvlcZoneTwo.release();
        libvlcZoneTwo = null;
    }

    public void releasePlayerZoneThree() {
        if (libvlcZoneThree == null)
            return;
        mMediaPlayerZoneThree.stop();
        final IVLCVout vout = (IVLCVout) mMediaPlayerZoneThree.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        holderZoneThree = null;
        libvlcZoneThree.release();
        libvlcZoneThree = null;
    }


    // DownloadTask for downloding video from URL
    public class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;
        private String content;
        private String error = null;
        AlertDialog alertDialog;

        private PowerManager.WakeLock mWakeLock;
        public DownloadTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
//            Log.i("ExternalStorage", "<<<<<<<<<< doInBackground sUrl[0] +: " +sUrl[0]);
            int count;
            String TYPE = sUrl[1];
            try {
                String[] videoArray = sUrl[0].split("/");

                Log.i("ExternalStorage", "<<<<<<<<<< doInBackground sUrl[0] +: " +videoArray[1]);
//                URL url = new URL("https://demo.cleartouchmedia.com/images/22_Amit/DutyManagement.mp4");
                URL url = new URL(Constant.imgURL+sUrl[0]);
                Log.i("ExternalStorage", "<<<<<<<<<< doInBackground url +: " +url);
                URLConnection connection = url.openConnection();
                connection.connect();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                File file;
                if(TYPE == "VIDEO")
                {
                    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), videoArray[1]);
                }
                else
                {
                    file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), videoArray[1]);
                }
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), videoArray[1]);
                OutputStream output = new FileOutputStream(file);
                byte data[] = new byte[10485760];
                while ((count = input.read(data)) != -1) {
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                error = e.getMessage();
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Video Downloading....");
            Log.i("ExternalStorage", "<<<<<<<<<< onPreExecute+: ");
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {
            alertDialog.dismiss();
            Log.i("ExternalStorage", "<<<<<<<<<< onPostExecute result : " +result);
            if (result != null) {
//                Toast.makeText(context, "Download result error: " + result, Toast.LENGTH_LONG).show();
            } else {
                ApplicationPreferences.getInstance(CompositionLayoutThreeDBActivity.this).setIsDownload(true);
//                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
//                Toast.makeText(CompositionLayoutThreeDBActivity.this, "Video Download complete.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //hare you can start downloding video
    public void newDownload(String url, String type) {
        final DownloadTask downloadTask = new DownloadTask(CompositionLayoutThreeDBActivity.this);
        downloadTask.execute(url, type);
    }

    private void takePermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            /*try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 101);
            }*/
        }
        else
        {
            /*ActivityCompat.requestPermissions(CompositionLayoutThreeDBActivity.this, new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 101);*/
        }
    }
    //Here you can check App Permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0)
        {
            if(requestCode == 101)
            {
                boolean readExt = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if(!readExt)
                {
                    takePermission();
                }
                else
                {
                    checkFolder();
                }
            }
        }
    }

    // Function to check if the image size is near a specified resolution within a threshold
    private boolean isNearResolution(int width, int height, int targetWidth, int targetHeight, int threshold) {
        return Math.abs(width - targetWidth) <= threshold && Math.abs(height - targetHeight) <= threshold;
    }

    //hare you can check folfer whare you want to store download Video
    public void checkFolder() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/ClearTouchMedia/";
        File dir = new File(path);
        boolean isDirectoryCreated = dir.exists();
        if (!isDirectoryCreated) {
            isDirectoryCreated = dir.mkdir();
        }
        if (isDirectoryCreated) {
            // do something\
            Log.d("Folder", "Already Created");
        }
    }


    @Override
    public void onDestroy(){
        if(mIsBGMedia.equalsIgnoreCase("1"))
        {
            if (isMyServiceRunning(MusicService.class)) {
                System.out.println("<<<<<<<<<<< media is onDestroy() sevice stop: ");
                stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
            }
        }

        handlerZoneOne.removeCallbacks(runnableZoneOne);
        handlerZoneTwo.removeCallbacks(runnableZoneTwo);
        handlerZoneThree.removeCallbacks(runnableZoneThree);if(mMediaPlayerZoneOne != null)
        {
            releasePlayerZoneOne();
        }
        if(mMediaPlayerZoneTwo != null)
        {
            releasePlayerZoneTwo();
        }
        if(mMediaPlayerZoneThree != null)
        {
            releasePlayerZoneThree();
        }
        if(videoViewZoneOne != null)
        {
            videoDestroyZoneOne();
        }
        if(videoViewZoneTwo != null)
        {
            videoDestroyZoneTwo();
        }
        if(videoViewZoneThree != null)
        {
            videoDestroyZoneThree();
        }

        // Remove any pending callbacks to prevent memory leaks
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    @Override
    protected void onResume() {

        deviceStatus("1", "onResume");
        /*if(!Utils.isPermissionGranted(CompositionLayoutThreeDBActivity.this))
        {
            new AlertDialog.Builder(CompositionLayoutThreeDBActivity.this)
                    .setTitle("All files Permission")
                    .setMessage("Due to Android 11 or heigher version restrictions, this app requires all files permission")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            takePermission();
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        else
        {
//            Toast.makeText(CompositionLayoutThreeDBActivity.this, "Permission already granted", Toast.LENGTH_LONG).show();
        }*/

        handlerLoginStatus.postDelayed(runnableLoginStatus = new Runnable() {
            public void run() {
                handlerLoginStatus.postDelayed(runnableLoginStatus, delayLoginStatus);
                ConnectivityManager manager = (ConnectivityManager) CompositionLayoutThreeDBActivity.this.getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo info = manager != null ? manager.getActiveNetworkInfo() : null;
                if (info != null && info.isConnected())
                {
                    RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
                    Call<DevicePushStatusReponse> call = apiService.getDevicePushStatusResponse(
                            DeviceID);
                    call.enqueue(new retrofit2.Callback<DevicePushStatusReponse>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onResponse(Call<DevicePushStatusReponse> call, Response<DevicePushStatusReponse> response) {
                            if(response.isSuccessful()) {
                                DevicePushStatusReponse devicePushStatusReponse = response.body();
                                if(devicePushStatusReponse.getStatus().equalsIgnoreCase("1"))
                                {
                                    DevicePushData devicePushData = devicePushStatusReponse.getDevicePushData();
                                    if(devicePushData != null)
                                    {
                                        Log.d(TAG, "<< Response of devicePushData success layout 3 "+devicePushData.getPushStatus());
                                        if(devicePushData.getPushStatus().equalsIgnoreCase("1"))
                                        {
                                            setDeleteDatabase();
                                        }
                                    }
                                    Log.d(TAG, "<< Response of DevicePushStatusReponse success "+devicePushStatusReponse.getMessage());
                                }
                                else
                                {
                                    Log.d(TAG, "<< Response of DevicePushStatusReponse failed ");
                                }
                            }
                        }
                        @Override
                        public void onFailure(Call<DevicePushStatusReponse> call, Throwable t) {
                            Log.d("main", "onFailure: "+t.getMessage());
                        }
                    });
                }
                else
                {
                    Log.d(TAG, "<< Response of compositionLayoutOneResponse offline ");
                }
                Log.d(TAG, "<< onDeviceLoginStatus in every 10 second ");
//                Toast.makeText(CompositionLayoutThreeDBActivity.this, "This method is run every 10 seconds",
//                        Toast.LENGTH_SHORT).show();
            }
        }, delayLoginStatus);

        if(mIsBGMedia.equalsIgnoreCase("1"))
        {
            if (isMyServiceRunning(MusicService.class)) {
                System.out.println("<<<<<<<<<<< media is onDestroy() sevice stop: ");
                stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
            }
        }

        super.onResume();
    }

    @Override
    protected void onStop() {
        if(mIsBGMedia.equalsIgnoreCase("1"))
        {
            if (isMyServiceRunning(MusicService.class)) {
                System.out.println("<<<<<<<<<<< media is onDestroy() sevice stop: ");
                stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
            }
        }
//        mediaPlayer.stop();
//        mediaPlayer.detachViews();

        handlerZoneOne.removeCallbacks(runnableZoneOne);
        handlerZoneTwo.removeCallbacks(runnableZoneTwo);
        handlerZoneThree.removeCallbacks(runnableZoneThree);
        if(mMediaPlayerZoneOne != null)
        {
            releasePlayerZoneOne();
        }
        if(mMediaPlayerZoneTwo != null)
        {
            releasePlayerZoneTwo();
        }
        if(mMediaPlayerZoneThree != null)
        {
            releasePlayerZoneThree();
        }
        if(videoViewZoneOne != null)
        {
            videoDestroyZoneOne();
        }
        if(videoViewZoneTwo != null)
        {
            videoDestroyZoneTwo();
        }
        if(videoViewZoneThree != null)
        {
            videoDestroyZoneThree();
        }

        // Remove any pending callbacks to prevent memory leaks
        mHandler.removeCallbacks(mRunnable);
        super.onStop();
    }


    @Override
    protected void onPause() {
        if(mIsBGMedia.equalsIgnoreCase("1"))
        {
            if (isMyServiceRunning(MusicService.class)) {
                System.out.println("<<<<<<<<<<< media is onPause sevice stop: ");
                stopService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
            } else {
                System.out.println("<<<<<<<<<<< media is onPause sevice start: ");
                startService(new Intent(CompositionLayoutThreeDBActivity.this, MusicService.class));
            }
        }
        handlerZoneOne.removeCallbacks(runnableZoneOne);
        handlerZoneTwo.removeCallbacks(runnableZoneTwo);
        handlerZoneThree.removeCallbacks(runnableZoneThree);
        handlerLoginStatus.removeCallbacks(runnableLoginStatus); //stop handler when activity not visible super.onPause();
        if(mMediaPlayerZoneOne != null)
        {
            releasePlayerZoneOne();
        }
        if(mMediaPlayerZoneTwo != null)
        {
            releasePlayerZoneTwo();
        }
        if(mMediaPlayerZoneThree != null)
        {
            releasePlayerZoneThree();
        }
        if(videoViewZoneOne != null)
        {
            videoDestroyZoneOne();
        }
        if(videoViewZoneTwo != null)
        {
            videoDestroyZoneTwo();
        }
        if(videoViewZoneThree != null)
        {
            videoDestroyZoneThree();
        }

        // Remove any pending callbacks to prevent memory leaks
        mHandler.removeCallbacks(mRunnable);
        super.onPause();
    }

    private void deleteImageFile(String path)
    {
        if (deleteImage(path)) {
            Log.d(TAG, "<< deleteImageFile Image deleted successfully ");
//            Toast.makeText(this, "Image deleted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "<< deleteImageFile Image deletion failed ");
//            Toast.makeText(this, "Image deletion failed", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean deleteImage(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        } else {
            return false;
        }
    }
}