package com.hem.cleartouchmedia.adapter;

import static com.hem.cleartouchmedia.adapter.YoutubeZoneOneAdapter.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.webkit.WebViewAssetLoader;

import com.hem.cleartouchmedia.Constant;
import com.hem.cleartouchmedia.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;


public class YoutubeZoneOneAdapter extends
        SliderViewAdapter<YoutubeAdapterViewHolder>{

    Context mContext;
    JSONArray jsonYoutubeListResponse;
    String[] youtubeVideoID;

    // Constructor
    public YoutubeZoneOneAdapter(Context context, JSONArray jsonYoutubeListResponse) {
        this.mContext = context;
        this.jsonYoutubeListResponse = jsonYoutubeListResponse;
    }

    public YoutubeZoneOneAdapter(Context context, String[] youtubeVideoID) {
        this.mContext = context;
        this.youtubeVideoID = youtubeVideoID;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public YoutubeAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_youtube_item, null);
        return new YoutubeAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(YoutubeAdapterViewHolder viewHolder, final int position) {
        if(youtubeVideoID != null)
        {
            String videoId = youtubeVideoID[position];
            if(videoId != null)
            {
                Log.d("TAG", "<< Response of youtube local videoId >>>>> "+videoId);
                String VideoEmbededAdress =
                    "<iframe  style=\"border: 0; width: 100%; height: 100%;\" +\n" +
                            "\"padding:0px; margin:0px; border: none;\" " +
                            "src=\"https://www.youtube.com/embed/" + videoId +
                            "?autoplay=1&mute=1&controls=1;\" " +
                            "title=\"YouTube video player\" frameborder=\"0\" " +
                            "allow=\"accelerometer; autoplay; clipboard-write; " +
                            "encrypted-media; gyroscope; picture-in-picture; " +
                            "web-share\" allowfullscreen></iframe>";

                String mimeType = "text/html";
                String encoding = "UTF-8";//"base64";
                String USERAGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36";

                WebViewAssetLoader assetLoader = new WebViewAssetLoader.Builder()
                        .addPathHandler("/assets/", new WebViewAssetLoader.AssetsPathHandler(mContext))
                        .build();

                viewHolder.myWebView.setWebViewClient(new WebViewClient() {
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

                viewHolder.myWebView.getSettings().setJavaScriptEnabled(true);
                viewHolder.myWebView.getSettings().setAllowContentAccess(true);
                viewHolder.myWebView.getSettings().setAllowFileAccess(true);
                viewHolder.myWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                viewHolder.myWebView.getSettings().setUserAgentString(USERAGENT);//Important to auto play video
                viewHolder.myWebView.getSettings().setLoadsImagesAutomatically(true);
                viewHolder.myWebView.setWebChromeClient(new WebChromeClient());
                viewHolder.myWebView.setWebViewClient(new WebViewClient());
                viewHolder.myWebView.loadUrl(VideoEmbededAdress);
                viewHolder.myWebView.loadDataWithBaseURL("", VideoEmbededAdress, mimeType, encoding, "");

                viewHolder.myWebView.setOnTouchListener(new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });

                viewHolder.myWebView.setClickable(false);
                viewHolder.myWebView.setEnabled(false);
                viewHolder.myWebView.setFocusableInTouchMode(false);
                viewHolder.myWebView.setFocusable(false);
            }
        }
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
//        return youtubeDetailByIDS.size();
//        return jsonYoutubeListResponse.length();
        return (youtubeVideoID.length>0) ? youtubeVideoID.length : 0;
    }

    static class YoutubeAdapterViewHolder extends ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        WebView myWebView;
//        YoutubeTvView youtubeTvView;

        public YoutubeAdapterViewHolder(View itemView) {
            super(itemView);
            myWebView = itemView.findViewById(R.id.mWebView);
//            youtubeTvView = itemView.findViewById(R.id.youtube_video);
            this.itemView = itemView;
        }
    }
}
