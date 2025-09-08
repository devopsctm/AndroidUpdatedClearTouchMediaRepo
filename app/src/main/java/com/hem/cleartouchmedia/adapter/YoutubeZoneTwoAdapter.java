package com.hem.cleartouchmedia.adapter;

import static com.hem.cleartouchmedia.adapter.YoutubeZoneTwoAdapter.*;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.webkit.WebViewAssetLoader;

import com.hem.cleartouchmedia.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;

public class YoutubeZoneTwoAdapter extends SliderViewAdapter<YoutubeAdapterViewHolder> {

    Context mContext;
    JSONArray jsonYoutubeListResponse;
    String[] youtubeVideoID;

    // Constructor
    public YoutubeZoneTwoAdapter(Context context, JSONArray jsonYoutubeListResponse) {
        this.mContext = context;
        this.jsonYoutubeListResponse = jsonYoutubeListResponse;
    }

    public YoutubeZoneTwoAdapter(Context context, String[] youtubeVideoID) {
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
    @Override
    public void onBindViewHolder(YoutubeAdapterViewHolder viewHolder, final int position) {
        /*JSONObject respYoutubeDataObj = null;
        JSONObject respYoutubeObj = null;
        String videoId = null;
        try {
            respYoutubeDataObj = jsonYoutubeListResponse.getJSONObject(position);
            respYoutubeObj = new JSONObject(respYoutubeDataObj.getString("id"));
            videoId = respYoutubeObj.getString("videoId");
            Log.d("TAG", "<< Response of json youtube local respYoutubeObj >>>>> "+videoId);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        if(youtubeVideoID != null)
        {
            String videoId = youtubeVideoID[position];
            if(videoId != null)
            {
                Log.d("TAG", "<< Response of youtube local videoId >>>>> "+videoId);

                //Embeded Youtube Video Address
//                String VideoEmbededAdress = "<iframe width=\"400\" height=\"225\" src=\"https://www.youtube.com/embed/th9WMMJuOFU?&autoplay=1\" title=\"YouTube video player\" frameborder=\"0\" allow=\"autoplay;\" allowfullscreen></iframe>";
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
                            "src=\"http://www.youtube.com/embed/"+videoId +
                            "?enablejsapi=1&rel=0&playsinline=1&autoplay=1&mute=1&showinfo=0&autohide=1&controls=0&modestbranding=1" +
                            "&fs=0\" allow=\"autoplay;\" frameborder=\"0\">\n"+
                            "</iframe>\n "+
                        "</body>" +
                    "</html>";
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

                /*String dataUrl =
                        "<html>" +
                                "<body>" +
                                "<script type='text/javascript' src='http://www.youtube.com/iframe_api'></script><script type='text/javascript'>\n" +
                                "                var player;\n" +
                                "        function onYouTubeIframeAPIReady()\n" +
                                "        {player=new YT.Player('playerId',{events:{onReady:onPlayerReady}})}\n" +
                                "        function onPlayerReady(event){player.mute();player.setVolume(0);player.playVideo();}\n" +
                                "        </script>" +
                                "<iframe class=\"youtube-player\" style=\"border: 0; width: 100%; height: 100%; " +
                                "padding:0px; margin:0px\" id=\"ytplayer\" type=\"text/html\" " +
                                "src=\"http://www.youtube.com/embed/"+videoId +
                                "?enablejsapi=1&rel=0&playsinline=1&autoplay=1&mute=1&showinfo=0&autohide=1&controls=0&modestbranding=1" +
                                "&fs=0\" frameborder=\"0\">\n"+
                                "</iframe>\n "+
                                "</body>" +
                                "</html>";

                WebSettings webSettings = viewHolder.myWebView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                viewHolder.myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                viewHolder.myWebView.getSettings().setLoadWithOverviewMode(true);
                viewHolder.myWebView.getSettings().setUseWideViewPort(true);
                viewHolder.myWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
                viewHolder.myWebView.loadData(dataUrl, "text/html", "utf-8");*/
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

        public YoutubeAdapterViewHolder(View itemView) {
            super(itemView);
            myWebView = itemView.findViewById(R.id.mWebView);
            this.itemView = itemView;
        }
    }
}
