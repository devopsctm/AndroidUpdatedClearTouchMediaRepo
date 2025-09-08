package com.hem.cleartouchmedia.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
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

import org.checkerframework.checker.units.qual.A;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.webkit.WebViewAssetLoader;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
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
import com.hem.cleartouchmedia.adapter.YoutubeZoneOneAdapter;
import com.hem.cleartouchmedia.bean.DevicePushData;
import com.hem.cleartouchmedia.download.ImageDownloadWorker;
import com.hem.cleartouchmedia.listener.LayoutOnePlayerListener;
import com.hem.cleartouchmedia.model.CompositionLayoutDetail;
import com.hem.cleartouchmedia.model.CompositionLayoutDetailViewModel;
import com.hem.cleartouchmedia.model.ScreenViewModel;
import com.hem.cleartouchmedia.networking.RetrofitAPIInerface;
import com.hem.cleartouchmedia.networking.RetrofitClient;
import com.hem.cleartouchmedia.persistance.CompositionDatabase;
import com.hem.cleartouchmedia.repository.CompositionLayoutRepository;
import com.hem.cleartouchmedia.response.CompositionLayoutResponse;
import com.hem.cleartouchmedia.response.DevicePushStatusReponse;
import com.hem.cleartouchmedia.response.DeviceStatusCheckReponse;
import com.hem.cleartouchmedia.response.TweetData;
import com.hem.cleartouchmedia.utilities.ApplicationConstants;
import com.hem.cleartouchmedia.utilities.ApplicationPreferences;

import com.hem.cleartouchmedia.utilities.FileUtils;
import com.hem.cleartouchmedia.utilities.ImageUtil;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.videolan.libvlc.interfaces.IVLCVout;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Response;

public class CompositionLayoutOneDBActivity extends AppCompatActivity
        implements IVLCVout.Callback {

    private static final String TAG = CompositionLayoutOneDBActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;

    CompositionLayoutResponse compositionLayoutResponse;

    private ScreenViewModel screenViewModel;
    private CompositionLayoutDetailViewModel compositionLayoutDetailViewModel;
    private CompositionLayoutRepository compositionLayoutRepository;

    LinearLayout mLoadingLayout;
    LinearLayout mZoneOneLayoutImg;
    LinearLayout mZoneOneLayoutDigitalClock;
    LinearLayout mZoneOneLayoutWeather;
    LinearLayout mZoneOneLayoutTwitter;
    LinearLayout mZoneOneLayoutVideo;
    LinearLayout mZoneOneLayoutYoutubeVideo;
    LinearLayout mZoneOneLayoutURL;
    LinearLayout mZoneOneLayoutText;
    LinearLayout mZoneOneLayoutSpreadSheet;
    LinearLayout mZoneOneLayoutGoogleSlide;
    LinearLayout mZoneOneLayoutRTSP;

    VideoView videoViewZoneOne;

    // we are creating array list for storing our image urls.
    ArrayList<TweetData> sliderDataArrayList;

    // initializing the slider view.
    SliderView sliderView_zone_one;
    SliderView mVideoSliderViewZoneOne;

    String DeviceID;

    public CompositionDatabase database;

    Handler handler = new Handler();
    Runnable runnable;
    int delayCount = 0;

    Handler handlerZoneOne = new Handler();
    Runnable runnableZoneOne;
    int delayZoneOne = 0;
    int countZoneOne = 0;
    int countOne = 1;
    int delayCountZoneOne = 0;

    Handler handlerLoginStatus = new Handler();
    Runnable runnableLoginStatus;
    int delayLoginStatus = 60000;

    int mFlag = 0;
    int isPlaySongStatus = 0;

    ArrayList<String> mZoneOneList;
    ArrayList<String> mZoneOneDurationList;

    List<CompositionLayoutDetail> compositionLayoutDetailsOne;

    DecimalFormat dtime;

    String mBGMediaUrl = "";
    String mIsBGMedia = "";
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
    private int mVideoWidth;
    private int mVideoHeight;
    private final static int VideoSizeChanged = -1;
    private MediaPlayer.EventListener mPlayerListenerZoneOne = new LayoutOnePlayerListener(this);
    boolean result;
    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    String fileVideo = null ;

    String SCREEN_TYPE;
    private Handler mHandler;
    private Runnable mRunnable;

    private PlayerView exoplayerView;
    private ExoPlayer exoPlayer;

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SCREEN_TYPE = (getIntent().getStringExtra("ORIENTATION") != null ?
                getIntent().getStringExtra("ORIENTATION") : "LANDSCAPE");
        if(SCREEN_TYPE.equalsIgnoreCase("PORTRAIT"))
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setContentView(R.layout.activity_composition_zone_one_portrait);
        }
        else
        {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            setContentView(R.layout.activity_composition_zone_one);
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

        mLoadingLayout = findViewById(R.id.loading_layout);
        // initializing the slider view.
        sliderView_zone_one = findViewById(R.id.slider_zone_one);
        mVideoSliderViewZoneOne = findViewById(R.id.youtube_slider_zone_one);
        mZoneOneLayoutImg = findViewById(R.id.zone_one_layout_img);
        mZoneOneLayoutDigitalClock = findViewById(R.id.zone_one_layout_digital_clock);
        mZoneOneLayoutWeather = findViewById(R.id.zone_one_layout_weather);
        mZoneOneLayoutTwitter = findViewById(R.id.zone_one_layout_twitter);
        mZoneOneLayoutVideo = findViewById(R.id.zone_one_layout_video);
        mZoneOneLayoutYoutubeVideo = findViewById(R.id.zone_one_layout_youtube_video);
        mZoneOneLayoutURL = findViewById(R.id.zone_one_layout_url);
        mZoneOneLayoutText = findViewById(R.id.zone_one_layout_text);
        mZoneOneLayoutSpreadSheet = findViewById(R.id.zone_one_layout_spreadsheets_url);
        mZoneOneLayoutGoogleSlide = findViewById(R.id.zone_one_layout_google_slides);
        mZoneOneLayoutRTSP = findViewById(R.id.zone_one_layout_rtsp);

        //exoplayerview
        exoplayerView = findViewById(R.id.exo_playerview);

    }

    private void zoneOneHandler() {
        /****      Zone One    ****/
        mFlag = 1;
        handlerZoneOne.postDelayed(runnableZoneOne = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void run() {
                if(mZoneOneList.size()>0 && mZoneOneList.size()>countZoneOne)
                {
                    int duration = mZoneOneDurationList.get(countZoneOne) != null
                            ? Integer.valueOf(mZoneOneDurationList.get(countZoneOne)) : 0;
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
//                        delayZoneOne = Integer.valueOf(mZoneOneDurationList.get(countZoneOne))*1000;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getZoneOne(String media_id) throws JSONException, ParseException {
        mLoadingLayout.setVisibility(View.GONE);
        CompositionLayoutDetail compositionLayoutDetail = compositionLayoutDetailsOne.get(countZoneOne);

        countZoneOne++;
        countOne++;
        // initializing media player
        if(compositionLayoutDetail.getType().equalsIgnoreCase("image"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
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
                Glide.get(CompositionLayoutOneDBActivity.this).clearDiskCache();
            }).start();
            Glide.get(CompositionLayoutOneDBActivity.this).clearMemory();

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



                boolean isDownloaded = ImageUtil.isImageDownloaded(CompositionLayoutOneDBActivity.this, Constant.FOLDER_NAME, imageArray[1]);
                if (isDownloaded) {
                    File imageFile = new File(getExternalFilesDir(Constant.FOLDER_NAME), imageArray[1]);
                    Glide.with(getApplicationContext())
                            .load(imageFile)
                            .fitCenter()
                            .override(width, height)
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                            .skipMemoryCache(false)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Load failed", e);
                                    new Thread(() -> {
                                        Glide.get(CompositionLayoutOneDBActivity.this).clearDiskCache();
                                    }).start();
                                    Glide.get(CompositionLayoutOneDBActivity.this).clearMemory();
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
//                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .skipMemoryCache(false)
//                            .placeholder(R.drawable.placeholder)
//                            .error(R.drawable.error_image)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    Log.e("GlideError", "Image Load failed", e);
                                    new Thread(() -> {
                                        Glide.get(CompositionLayoutOneDBActivity.this).clearDiskCache();
                                    }).start();
                                    Glide.get(CompositionLayoutOneDBActivity.this).clearMemory();
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

                if(SCREEN_TYPE.equalsIgnoreCase("LANDSCAPE"))
                {
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
                    startService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
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
                SimpleDateFormat tf = new SimpleDateFormat("HH:mm a");
                df.setTimeZone(TimeZone.getTimeZone(compositionLayoutDetail.getTimezone())); //format in given timezone
                String strDate = df.format(date);
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
                    startService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
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
                    startService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
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

            getTwitterAPILocalResponse(compositionLayoutDetail.getTwitterProfileData(),
                    compositionLayoutDetail.getTwitterFeedsList(),
                    1,
                    compositionLayoutDetail.getSlide_duration());

        }

        else if(compositionLayoutDetail.getType().equalsIgnoreCase("video"))
        //  neha code
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                    stopService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
            }

            //Constant.imgURL+compositionLayoutDetail.getUrl());
          //  setupZoneOneVideo(compositionLayoutDetail);
            setUpExoPlayerVideo(compositionLayoutDetail);

        }
        /// neha code end /////////////////////////////////////////////////
     /*   {
           /////////// /// //////////////////////////////////////////////////////////////////////////////////////////
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                    stopService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
            }
            Log.d("zone one area", "<<<<< mp4 video");
            Log.d("zone one area", "<<<<< mp4 video"+
            Constant.imgURL+compositionLayoutDetail.getUrl());
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


                boolean isDownloaded = ImageUtil.isImageDownloaded(this, Constant.FOLDER_NAME, videoArray[1]);
                if (isDownloaded) {
//                    Toast.makeText(this, "Image Downloaded", Toast.LENGTH_SHORT).show();
                    // The image is already downloaded
                    File imageFile = new File(getExternalFilesDir(Constant.FOLDER_NAME), videoArray[1]);

                } else {
                    // Enqueue the download worker
                    String imageUrl = compositionLayoutDetail.getUrl();
                    String fileName = videoArray[1];
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
                }




                File imgFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), videoArray[1]);
//                File imgFile = new  File("/storage/emulated/0/Movies/"+videoArray[1]);

                try {
                    if (imgFile.exists()) {
                        Log.i("ExternalStorage", "<<<<<<<<<< inside composition file exists: " + videoArray[1]);
                        Uri uri = Uri.fromFile(imgFile);
                        videoViewZoneOne.setVideoURI(uri);
                    } else {
                        if (info != null && info.isConnected()) {
                            newDownload(compositionLayoutDetail.getUrl(), "VIDEO");
                        }
                        Uri uri = Uri.parse(Constant.imgURL + compositionLayoutDetail.getUrl());
                        videoViewZoneOne.setVideoURI(uri);
                    }

                    videoViewZoneOne.setOnErrorListener((mp, what, extra) -> {
                        Toast.makeText(CompositionLayoutOneDBActivity.this, "Download result error\n Downloading again.. ", Toast.LENGTH_LONG).show();
                        if (info != null && info.isConnected()) {
                            newDownload(compositionLayoutDetail.getUrl(), "VIDEO");
                        }
                        return false;
                    });

                    videoViewZoneOne.setOnPreparedListener(mp -> {
                        mp.setLooping(true);
                        videoViewZoneOne.requestFocus();
                        videoViewZoneOne.start();
                        makeFullScreen();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }


                *//*if(imgFile.exists()){
                    Log.i("ExternalStorage", "<<<<<<<<<< inside composition file exists: " +videoArray[1]);
                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+"/Movies/"+videoArray[1]);
//                    Uri uri = Uri.parse(Constant.imgURL+compositionLayoutDetail.getUrl());
                    videoViewZoneOne.setVideoURI(uri);
                    videoViewZoneOne.setOnErrorListener((mp, what, extra) -> {
                        Toast.makeText(CompositionLayoutOneDBActivity.this, "Download result error\n Downloading again.. ", Toast.LENGTH_LONG).show();
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
                videoViewZoneOne.start();*//*
            }
        }*/
        /// //////////////////////////////////////////////////////////////////////////
        //  neha end code
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("url"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
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
                            stopService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                        }
                    }
                }

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
                    startService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
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
//          linearLayout.setBackgroundResource(Color.parseColor(mJsonObject.getString("background_color")));
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
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice stop ");
                    stopService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
            }
            if(videoViewZoneOne != null)
            {
                videoDestroyZoneOne();
            }
            Log.d("zone one area", "<<<<< youtube video");
            Log.d("zone one area", "<<<<< youtube channel data : "+
                    compositionLayoutDetail.getYoutube_channel_data());
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
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
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
                webView.setWebViewClient(new WebViewClientImpl());
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
                webView.loadUrl(compositionLayoutDetail.getApp_url());
            }
        }
        else if(compositionLayoutDetail.getType().equalsIgnoreCase("google_slides_url"))
        {
            if(mIsBGMedia.equalsIgnoreCase("1"))
            {
                if (isMyServiceRunning(MusicService.class)) {
                } else {
                    System.out.println("<<<<<<<<<<< media is on stop() sevice start: ");
                    startService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
                }
            }
            if(mMediaPlayerZoneOne != null)
            {
                releasePlayer();
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

            if(compositionLayoutDetail.getApp_url() != null)
            {
                WebView webView = (WebView) findViewById(R.id.zone_one_google_slides_webview);
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
                    stopService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
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
//                rtspUrl = "rtsp://70.26.15.213/0";
                Log.d(TAG, "Playing rtsp url: " + compositionLayoutDetail.getApp_url());

                String rtspUrl = "rtsp://70.26.15.213/0";
                Log.d(TAG, "Playing back " + rtspUrl);


                mSurfaceZoneOne = (SurfaceView) findViewById(R.id.zone_one_rtsp);
                WindowManager wm = this.getWindowManager();
                int width = wm.getDefaultDisplay().getWidth();
                int height = wm.getDefaultDisplay().getHeight();
                int iNewWidth = (int) (height * 3.0 / 4.0);
//                FrameLayout rCameraLayout = (FrameLayout) findViewById(R.id.frame_zone_one_rtsp);
                FrameLayout.LayoutParams layoutParams =
                        new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT);
                int iPos = width - iNewWidth;
                Log.d(TAG, "<<Playing back width " + width);
                Log.d(TAG, "<<Playing back height " + height);
                Log.d(TAG, "<<Playing back iNewWidth " + iNewWidth);
                Log.d(TAG, "<<Playing back iPos " + iPos);

                mSurfaceZoneOne.getHolder().setFixedSize(width/2, height/2);
                mSurfaceZoneOne.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                mSurfaceZoneOne.getHolder().setKeepScreenOn(true);
                mSurfaceZoneOne.setLayoutParams(layoutParams);
                holderZoneOne = mSurfaceZoneOne.getHolder();

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

                Media m = new Media(libvlcZoneOne, Uri.parse(rtspUrl));
                mMediaPlayerZoneOne.setMedia(m);
                mMediaPlayerZoneOne.play();
            }
        }
    }

    private void getYoutubeAPIZoneOneResponse(String[] youtubeVideoID, int slide_duration) {
        if(youtubeVideoID != null)
        {
            YoutubeZoneOneAdapter adapter = new YoutubeZoneOneAdapter(CompositionLayoutOneDBActivity.this,
                    youtubeVideoID);
            mVideoSliderViewZoneOne.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            mVideoSliderViewZoneOne.setScrollTimeInSec(slide_duration);
            mVideoSliderViewZoneOne.setAutoCycle(true);
            mVideoSliderViewZoneOne.startAutoCycle();
            mVideoSliderViewZoneOne.setSliderAdapter(adapter);
        }
    }

    private void getTwitterAPILocalResponse(String  twitterProfileData,
                                            String  twitterFeedsList, int zone,
                                            String slide_duration) throws JSONException  {
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
                                        .skipMemoryCache(false)
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
                TwitterZoneOneAdapter adapter = new TwitterZoneOneAdapter(CompositionLayoutOneDBActivity.this,
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

    private void getWeatherDetailByCity(String weatherResponse, int zone) throws JSONException, ParseException {
        if(weatherResponse != null)
        {
            JSONObject jsonResponse = new JSONObject(weatherResponse);
            if (jsonResponse != null) {

                Log.d("TAG", "<< Response of layout 3 weather jsonResponse "+jsonResponse);
                JSONObject myCityJson = new JSONObject(jsonResponse.getString("city"));
                JSONArray weatherListJsonArr = new JSONArray(jsonResponse.getString("list"));
//                        for (int index = 0; index < weatherListJsonArr.length(); index++)
//                        {
                JSONObject myJsonObject = weatherListJsonArr.getJSONObject(0);

                JSONObject myJson = new JSONObject(myJsonObject.getString("main"));
                JSONArray myJsonArr = new JSONArray(myJsonObject.getString("weather"));
                JSONObject mJsonObject = myJsonArr.getJSONObject(0);


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

                if(zone == 1)
                {
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

                    float temp = (Float.parseFloat(myJson.getString("temp")));
                    float feel = (Float.parseFloat(myJson.getString("feels_like")));

                    tv_weather_feel_zone_one.setText("feel like: "+Double.valueOf(dtime.format(feel))+temp_type);
                    tv_weather_city_zone_one.setText(myCityJson.getString("name"));
                    tv_weather_temp_zone_one.setText(Double.valueOf(dtime.format(temp))+temp_type);

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

                    tv_weather_feel_one_zone_one.setText("feel like: "+Double.valueOf(dtime.format(feel1))+temp_type);
                    tv_weather_date_one_zone_one.setText(weatherDateStrOne);
                    tv_weather_temp_one_zone_one.setText(Double.valueOf(dtime.format(temp1))+temp_type);

                    tv_weather_feel_two_zone_one.setText("feel like: "+Double.valueOf(dtime.format(feel2))+temp_type);
                    tv_weather_date_two_zone_one.setText(weatherDateStrTwo);
                    tv_weather_temp_two_zone_one.setText(Double.valueOf(dtime.format(temp2))+temp_type);

                    tv_weather_feel_three_zone_one.setText("feel like: "+Double.valueOf(dtime.format(feel3))+temp_type);
                    tv_weather_date_three_zone_one.setText(weatherDateStrThree);
                    tv_weather_temp_three_zone_one.setText(Double.valueOf(dtime.format(temp3))+temp_type);

                    tv_weather_feel_four_zone_one.setText("feel like: "+Double.valueOf(dtime.format(feel4))+temp_type);
                    tv_weather_date_four_zone_one.setText(weatherDateStrFour);
                    tv_weather_temp_four_zone_one.setText(Double.valueOf(dtime.format(temp4))+temp_type);
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

                if(mZoneOneList.size()>0)
                {
                    if(mFlag == 0)
                    {
                        Log.d(TAG, "<< onStart inside mflag");
                        zoneOneHandler();
                    }
                }
                else {
//                        updateCompositionLayoutDetail();
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

    public void getCompositionResponse(String deviceID, String compositionID) {
        if (compositionID != null) {

            if (isConnected()) {
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
                                if (layoutDetailList!=null && !layoutDetailList.isEmpty()) {
                                    for (int i = 0; i < layoutDetailList.size(); i++) {
                                        CompositionLayoutDetail compositionLayoutDetail = layoutDetailList.get(i);
                                        if (compositionLayoutDetail != null) {
                                            compositionLayoutRepository.insert(compositionLayoutDetail);
                                            if(compositionLayoutDetail.getBGMusicUrl() != null)
                                            {
                                                mBGMediaUrl = (compositionLayoutDetail.getBGMusicUrl() != null ?
                                                        compositionLayoutDetail.getBGMusicUrl() : "");
                                                ApplicationPreferences.getInstance(CompositionLayoutOneDBActivity.this)
                                                        .setBackgroundMusic(compositionLayoutDetail.getBGMusicUrl());
                                                mIsBGMedia = (compositionLayoutDetail.isBGMusicPause() != null
                                                        ? compositionLayoutDetail.isBGMusicPause()
                                                        : "0");
                                                ApplicationPreferences.getInstance(CompositionLayoutOneDBActivity.this)
                                                        .setBackgroundMusicStatus(mIsBGMedia);
                                                ApplicationPreferences.getInstance(CompositionLayoutOneDBActivity.this)
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
                                        }
                                    }
                                    // Update the LiveData object with the new data
//                                    updateCompositionLayoutDetail();
                                    // Trigger handler if data exists
                                    if (!mZoneOneList.isEmpty() && mFlag == 0) {
                                        zoneOneHandler();
                                    } else if (mZoneOneList.isEmpty()) {
                                        // retry fetching composition
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


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<< onStart Called");
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm != null ? cm.getActiveNetworkInfo() : null;
        if (netinfo != null && netinfo.isConnected())
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

        Log.d(TAG, "<< onStart method in layout 1  ");
        compositionLayoutDetailViewModel = new ViewModelProvider(this).get(CompositionLayoutDetailViewModel.class);
        screenViewModel = new ViewModelProvider(this).get(ScreenViewModel.class);
        if(ApplicationPreferences.getInstance(this).getNotificationReceive())
        {
            Log.d(TAG, "<< Notification status in compositionDBActivity:  "+ ApplicationPreferences.getInstance(this).getNotificationReceive());
            screenViewModel.deleteAll();
            compositionLayoutDetailViewModel.deleteAll();
            ApplicationPreferences.getInstance(getApplicationContext()).setNotificationReceive(false);

//            File directory = new File(getExternalFilesDir(null), Constant.FOLDER_NAME);
//            FileUtils.deleteDirectory(directory);
            deleteCache(this);
            //add by neha ====
            setDeleteDatabase();
            //========

            // Check for permissions
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                deleteFiles();
            }
        }


        if (isConnected())
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
                                Log.d(TAG, "<< Response of devicePushData success layout 1"+devicePushData.getPushStatus());
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

        handlerLoginStatus.postDelayed(runnableLoginStatus = new Runnable() {
            public void run() {
                handlerLoginStatus.postDelayed(runnableLoginStatus, delayLoginStatus);
                ConnectivityManager manager = (ConnectivityManager) CompositionLayoutOneDBActivity.this.getSystemService(CONNECTIVITY_SERVICE);
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
                                        Log.d(TAG, "<< Response of devicePushData success layout 1"+devicePushData.getPushStatus());
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
            }
        }, delayLoginStatus);

        database = CompositionDatabase.getInstance(this);
        mZoneOneList = new ArrayList<>();
        mZoneOneDurationList = new ArrayList<>();

        compositionLayoutDetailsOne = new ArrayList<>();

        dtime = new DecimalFormat("#.#");

        // we are creating array list for storing our image urls.
        sliderDataArrayList = new ArrayList<>();

        DeviceID = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        compositionLayoutRepository = new CompositionLayoutRepository(getApplication());
        getCompositionResponse(DeviceID, getIntent().getStringExtra("COMPOSITION_ID"));
        compositionLayoutDetailViewModel = new ViewModelProvider(this).get(CompositionLayoutDetailViewModel.class);

        compositionLayoutDetailViewModel.getCompositionLayoutDetail().observe(this, new Observer<List<CompositionLayoutDetail>>() {
            @Override
            public void onChanged(List<CompositionLayoutDetail> compositionLayoutDetail) {
                if(compositionLayoutDetail != null)
                {
                    System.out.println("<<<<<<< composition layout size(); "+compositionLayoutDetail.size());
                    for(int i = 0; i< compositionLayoutDetail.size(); i++)
                    {
                        if(compositionLayoutDetail.get(0).getBGMusicUrl() != null)
                        {
                            mBGMediaUrl = (compositionLayoutDetail.get(0).getBGMusicUrl() != null ?
                                    compositionLayoutDetail.get(0).getBGMusicUrl() : "");
                            ApplicationPreferences.getInstance(CompositionLayoutOneDBActivity.this)
                                    .setBackgroundMusic(compositionLayoutDetail.get(0).getBGMusicUrl());
                            mIsBGMedia = (compositionLayoutDetail.get(0).isBGMusicPause() != null
                                    ? compositionLayoutDetail.get(0).isBGMusicPause()
                                    : "0");
                            ApplicationPreferences.getInstance(CompositionLayoutOneDBActivity.this)
                                    .setBackgroundMusicStatus(mIsBGMedia);
                            ApplicationPreferences.getInstance(CompositionLayoutOneDBActivity.this)
                                    .setBackgroundMusicCount(0);
                        }
                        assert compositionLayoutDetail.get(i).getZone_type() != null;
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
                    }
                    if(mZoneOneList.size()>0)
                    {
                        if(mFlag == 0)
                        {
                            Log.d(TAG, "<< onStart inside mflag");
                            zoneOneHandler();
                        }
                    }
                    else {
//                        updateCompositionLayoutDetail();
                        getCompositionResponse(DeviceID, getIntent().getStringExtra("COMPOSITION_ID"));
                    }
                    /*if(mFlag == 0)
                    {
                        zoneOneHandler();
                    }*/
                }
                else {
                    getCompositionResponse(DeviceID, getIntent().getStringExtra("COMPOSITION_ID"));
                }
            }
        });
    }

    private void deviceStatus(String status, String method) {
        Log.d(TAG, "<<<<<< device status is: "+status +" << device method is: "+method+" << device layout is 1");


        if (isConnected())
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
                        Log.d(TAG, "<<<< Response of DeviceStatusCheckReponse "+deviceStatusCheckReponse.getMessage());

                        if(deviceStatusCheckReponse.getStatus().equalsIgnoreCase("1"))
                        {
                            Log.d(TAG, "<<<< Response of DeviceStatusCheckReponse success "+deviceStatusCheckReponse.getMessage());
                        }
                        else
                        {
                            Log.d(TAG, "<<<< Response of DeviceStatusCheckReponse failed ");
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                startActivity(new Intent(CompositionLayoutOneDBActivity.this, SplashActivity.class));
                finishAffinity();
            } else {
                ActivityCompat.finishAffinity(CompositionLayoutOneDBActivity.this);
                startActivity(new Intent(CompositionLayoutOneDBActivity.this,
                        SplashActivity.class));
            }
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
        Log.d(TAG, "<< push status in layout 1:  " + ApplicationPreferences.getInstance(getApplicationContext()).getNotificationReceive());
        Log.d(TAG, "<< push status in layout 1 COMPOSITION_ID:  " + getIntent().getStringExtra("COMPOSITION_ID"));
        Log.d(TAG, "<< push status in layout 1 ORIENTATION:  " + getIntent().getStringExtra("ORIENTATION"));
        compositionLayoutDetailViewModel = new ViewModelProvider(this).get(CompositionLayoutDetailViewModel.class);
        compositionLayoutDetailViewModel.deleteAll();
        ApplicationPreferences.getInstance(getApplicationContext()).setNotificationReceive(false);

        ScreenViewModel screenViewModel = new ViewModelProvider(this).get(ScreenViewModel.class);
        screenViewModel.deleteAll();

        File directory = new File(getExternalFilesDir(null), Constant.FOLDER_NAME);
        FileUtils.deleteDirectory(directory);
        deleteCache(this);

        // Delete all videos and images stored locally
        deleteMediaFiles();
    }


    private void deleteMediaFiles() {
        // Videos
        File moviesDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        deleteRecursive(moviesDir);

        // Images
        File picturesDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        deleteRecursive(picturesDir);
    }

    private void deleteRecursive(File fileOrDir) {
        if (fileOrDir != null && fileOrDir.exists()) {
            if (fileOrDir.isDirectory()) {
                for (File child : Objects.requireNonNull(fileOrDir.listFiles())) {
                    deleteRecursive(child);
                }
            }
            fileOrDir.delete();
        }
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

    @SuppressLint("SuspiciousIndentation")
    public void videoDestroyZoneOne() {
        if (videoViewZoneOne == null)
            return;

        videoViewZoneOne.stopPlayback();
    }

    public void releasePlayer() {
        //neha code to change video view to exo player ====

        //========== neha exo code end=========
        if (libvlcZoneOne == null)
            return;
        mMediaPlayerZoneOne.stop();
        final IVLCVout vout = (IVLCVout) mMediaPlayerZoneOne.getVLCVout();
        vout.removeCallback(this);
        vout.detachViews();
        holderZoneOne = null;
        libvlcZoneOne.release();
        libvlcZoneOne = null;

        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    // DownloadTask for downloding video from URL
    public class DownloadTask extends AsyncTask<String, Integer, String> {
        private Context context;

        private String error = null;
        AlertDialog alertDialog;
        private WeakReference<Activity> activityRef;


        public DownloadTask(Activity context) {
            //  this.context = context;
            this.activityRef = new WeakReference<>(context);

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
                File file;
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                if(Objects.equals(TYPE, "VIDEO"))
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
           /* alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Video Downloading....");
            Log.i("ExternalStorage", "<<<<<<<<<< onPreExecute+: ");*/
            Activity activity = activityRef.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                alertDialog = new AlertDialog.Builder(activity).create();
                alertDialog.setTitle("Video Downloading....");
                alertDialog.show();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
        }

        @Override
        protected void onPostExecute(String result) {

            //  alertDialog.dismiss();
            Activity activity = activityRef.get();
            if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                }

                Log.i("ExternalStorage", "<<<<<<<<<< onPostExecute result : " +result);
                if (result != null) {
//                Toast.makeText(context, "Download result error: " + result, Toast.LENGTH_LONG).show();
                } else {
                    ApplicationPreferences.getInstance(activity).setIsDownload(true);
//                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
//                Toast.makeText(CompositionLayoutThreeDBActivity.this, "Video Download complete.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //hare you can start downloding video
    public void newDownload(String url, String type) {
        final DownloadTask downloadTask = new DownloadTask(CompositionLayoutOneDBActivity.this);
        downloadTask.execute(url, type);
    }


    private void takePermission()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
        {
            try {
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
            }
        }
        else
        {
            /*ActivityCompat.requestPermissions(CompositionLayoutOneDBActivity.this, new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 101);*/
        }
    }

    // Method to make the activity full screen
    private void makeFullScreen() {
        // Hide the status bar and action bar
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Set the activity to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            makeFullScreen();
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
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                deleteFiles();
            } else {
                Toast.makeText(this, "Permission denied to write to storage", Toast.LENGTH_SHORT).show();
            }
        }
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
    protected void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            exoPlayer.pause();
        }
        if(mIsBGMedia.equalsIgnoreCase("1"))
        {
            if (isMyServiceRunning(MusicService.class)) {
                System.out.println("<<<<<<<<<<< media is onPause sevice stop: ");
                stopService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
            } else {
                System.out.println("<<<<<<<<<<< media is onPause sevice start: ");
                startService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
            }
        }
        handlerLoginStatus.removeCallbacks(runnableLoginStatus);
        handlerZoneOne.removeCallbacks(runnableZoneOne);
        if(mMediaPlayerZoneOne != null)
        {    releasePlayer();
        }
        if(videoViewZoneOne != null)
        {
            videoDestroyZoneOne();
        }
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<< onPause Called");
        // Remove any pending callbacks to prevent memory leaks
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        releaseExoPlayer();
        if(mIsBGMedia.equalsIgnoreCase("1"))
        {
            if (isMyServiceRunning(MusicService.class)) {
                System.out.println("<<<<<<<<<<< media is onDestroy() sevice stop: ");
                stopService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
            }
        }
        if(mMediaPlayerZoneOne != null)
        {
            releasePlayer();
        }
        if(videoViewZoneOne != null)
        {
            videoDestroyZoneOne();
        }
        releasePlayer();
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<< onDestroy Called"+MyApplication.isAppInForeground());
//        if(!hasWindowFocus())
        // Remove any pending callbacks to prevent memory leaks
        mHandler.removeCallbacks(mRunnable);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mIsBGMedia.equalsIgnoreCase("1"))
        {
            if (isMyServiceRunning(MusicService.class)) {
                System.out.println("<<<<<<<<<<< media is on stop() sevice stop: ");
                stopService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
            }
        }
        if(mMediaPlayerZoneOne != null)
        {
            releasePlayer();
        }
        if(videoViewZoneOne != null)
        {
            videoDestroyZoneOne();
        }
        Log.d(TAG, "<<<<<<<<<<<<<<<<<<<<<<<<< onStop Called"+MyApplication.isAppInForeground());
//        if(!hasWindowFocus())
        // Remove any pending callbacks to prevent memory leaks
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();

        handlerLoginStatus.postDelayed(runnableLoginStatus = new Runnable() {
            public void run() {
                handlerLoginStatus.postDelayed(runnableLoginStatus, delayLoginStatus);
                if (isConnected())
                {
                    RetrofitAPIInerface apiService = RetrofitClient.getClient().create(RetrofitAPIInerface.class);
                    Call<DevicePushStatusReponse> call = apiService.getDevicePushStatusResponse(
                            DeviceID);
                    call.enqueue(new retrofit2.Callback<DevicePushStatusReponse>() {
                        @Override
                        public void onResponse(@NonNull Call<DevicePushStatusReponse> call, Response<DevicePushStatusReponse> response) {
                            if(response.isSuccessful()) {
                                DevicePushStatusReponse devicePushStatusReponse = response.body();
                                if(devicePushStatusReponse.getStatus().equalsIgnoreCase("1"))
                                {
                                    DevicePushData devicePushData = devicePushStatusReponse.getDevicePushData();
                                    if(devicePushData != null)
                                    {
                                        Log.d(TAG, "<< Response of devicePushData success layout 1"+devicePushData.getPushStatus());
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
            }
        }, delayLoginStatus);

        // initializing media player
        if(mIsBGMedia.equalsIgnoreCase("1"))
        {
            if (isMyServiceRunning(MusicService.class)) {
                System.out.println("<<<<<<<<<<< media is on stop() sevice stop: ");
                stopService(new Intent(CompositionLayoutOneDBActivity.this, MusicService.class));
            }
        }
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


    private void deleteFiles() {
        Log.d("CTM", "<<<<<<<<<<<<<<<<<<<<<<files deleteFiles");
        File directory = new File(Environment.getExternalStorageDirectory(), Constant.FOLDER_NAME);
        Log.d("CTM", "<<<<<<<<<<<<<<<<<<<<<<files directory.getName()"+directory.getName());
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


    //==========neha ========================================================================
    private void setupZoneOneVideo(CompositionLayoutDetail compositionLayoutDetail) {
        hideAllZoneOneLayouts();
        mZoneOneLayoutVideo.setVisibility(View.VISIBLE);

        videoViewZoneOne = findViewById(R.id.zone_one_video);

        if (compositionLayoutDetail.getUrl() == null) return;

        String[] videoArray = compositionLayoutDetail.getUrl().split("/");
        String fileName = videoArray[1];
        File videoFile = new File(getExternalFilesDir("Movies"), fileName);

        if (videoFile.exists()) {
            // Play local file
            playVideoFromFile(videoFile);
        } else if (isConnected()) {
            // Stream from URL immediately
            playVideoFromURL(compositionLayoutDetail.getUrl());

            // Download in background
            enqueueAndObserveDownload(fileName, compositionLayoutDetail.getUrl());
        } else {
            Toast.makeText(this, "No internet and file not available", Toast.LENGTH_SHORT).show();
        }
    }
    private void playVideoFromFile(File file) {
        Log.d("VideoDebug", "Playing from file: " + file.getAbsolutePath());
        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        setupVideoPlayer(uri);
    }

    private void playVideoFromURL(String urlPath) {
        Uri uri = Uri.parse(Constant.imgURL + urlPath);
        Log.d("VideoDebug", "Streaming from URL: " + uri);
        setupVideoPlayer(uri);
    }

    private void setupVideoPlayer(Uri uri) {
        videoViewZoneOne.setVideoURI(uri);
        videoViewZoneOne.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoViewZoneOne.requestFocus();
            videoViewZoneOne.start();
            makeFullScreen();
        });
        videoViewZoneOne.setOnErrorListener((mp, what, extra) -> {
            Log.e("VideoError", "Can't play video. what=" + what + ", extra=" + extra);
            //  Toast.makeText(this, "Video play error. Trying again...", Toast.LENGTH_SHORT).show();
            return true;
        });
    }


    private void enqueueAndObserveDownload(String fileName, String urlPath) {
        Data data = new Data.Builder()
                .putString("imageUrl", Constant.imgURL + urlPath)
                .putString("fileName", fileName)
                .putString("folderName", "Movies")
                .build();

        OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(ImageDownloadWorker.class)
                .setInputData(data)
                .build();

        WorkManager.getInstance(this).enqueue(downloadRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(downloadRequest.getId())
                .observe(this, workInfo -> {
                    if (workInfo != null && workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                        File downloadedFile = new File(getExternalFilesDir("Movies"), fileName);
                        if (downloadedFile.exists()) {
                            MediaScannerConnection.scanFile(this,
                                    new String[]{downloadedFile.getAbsolutePath()},
                                    null,
                                    null);

                            // Switch from streaming to local file seamlessly
                            if (videoViewZoneOne.isPlaying()) {
                                playVideoFromFile(downloadedFile);
                            }
                        }
                    } else if (workInfo != null && workInfo.getState() == WorkInfo.State.FAILED) {
                        Log.e("DownloadWorker", "Download failed for " + fileName);
                    }
                });
    }

    private boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
        return info != null && info.isConnected();
    }

    private void hideAllZoneOneLayouts() {
        mZoneOneLayoutImg.setVisibility(View.GONE);
        mZoneOneLayoutWeather.setVisibility(View.GONE);
        mZoneOneLayoutDigitalClock.setVisibility(View.GONE);
        mZoneOneLayoutTwitter.setVisibility(View.GONE);
        mZoneOneLayoutText.setVisibility(View.GONE);
        mZoneOneLayoutURL.setVisibility(View.GONE);
        mZoneOneLayoutYoutubeVideo.setVisibility(View.GONE);
        mZoneOneLayoutSpreadSheet.setVisibility(View.GONE);
        mZoneOneLayoutGoogleSlide.setVisibility(View.GONE);
        mZoneOneLayoutRTSP.setVisibility(View.GONE);
    }


   private void setUpExoPlayerVideo(CompositionLayoutDetail compositionLayoutDetail){
       hideAllZoneOneLayouts();
       mZoneOneLayoutVideo.setVisibility(View.VISIBLE);
       exoplayerView.setVisibility(View.VISIBLE);

       if (compositionLayoutDetail.getUrl() == null) return;

       String[] videoArray = compositionLayoutDetail.getUrl().split("/");
       String fileName = videoArray[1];
       File videoFile = new File(getExternalFilesDir("Movies"), fileName);

       if (videoFile.exists()) {
           // Play local file
           playVideoFromFileExo(videoFile);
       } else if (isConnected()) {
           // Stream from URL immediately
           playVideoFromURLExo(compositionLayoutDetail.getUrl());

           // Download in background
           enqueueAndObserveDownload(fileName, compositionLayoutDetail.getUrl());
       } else {
           Toast.makeText(this, "No internet and file not available", Toast.LENGTH_SHORT).show();
       }
   }

    private void playVideoFromFileExo(File file) {
        Log.d("VideoDebug", "Playing from file: " + file.getAbsolutePath());
        Uri uri = Uri.fromFile(file);
        setupExoPlayer(uri);
    }

    private void playVideoFromURLExo(String urlPath) {
        Uri uri = Uri.parse(Constant.imgURL + urlPath);
        Log.d("VideoDebug", "Streaming from URL: " + uri);
        setupExoPlayer(uri);
    }

    private void setupExoPlayer(Uri uri) {
        releaseExoPlayer();
        exoPlayer = new ExoPlayer.Builder(this).build();
        exoplayerView.setPlayer(exoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(uri);
        exoPlayer.setMediaItem(mediaItem);
        exoPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ALL); // loop
        exoPlayer.prepare();
        exoPlayer.play();
    }

    private void releaseExoPlayer() {
        if (exoPlayer != null) {
            exoPlayer.release();
            exoPlayer = null;
        }
    }

}
