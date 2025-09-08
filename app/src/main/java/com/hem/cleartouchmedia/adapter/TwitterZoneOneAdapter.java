package com.hem.cleartouchmedia.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.hem.cleartouchmedia.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwitterZoneOneAdapter extends SliderViewAdapter<TwitterZoneOneAdapter.TwitterAdapterViewHolder> {

    Context mContext;
    JSONArray jsonTwitterListResponse;

    // Constructor
    public TwitterZoneOneAdapter(Context context, JSONArray jsonTwitterListResponse) {
        this.mContext = context;
        this.jsonTwitterListResponse = jsonTwitterListResponse;
    }

    // We are inflating the slider_layout
    // inside on Create View Holder method.
    @Override
    public TwitterAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_twitter_item, null);
        return new TwitterAdapterViewHolder(inflate);
    }

    // Inside on bind view holder we will
    // set data to item of Slider View.
    @Override
    public void onBindViewHolder(TwitterAdapterViewHolder viewHolder, final int position) {
        if(jsonTwitterListResponse != null)
        {
            String tweet_url = "";
            JSONObject respTwitterObj = null;
            JSONObject myJson1 = null;
            JSONArray myJsonTweets = null;
            JSONObject respTwitterData = null;
            JSONArray myJsonMedia = null;
            JSONObject respMediaData = null;
            try {
                if(jsonTwitterListResponse.getJSONObject(position) != null)
                {
                    respTwitterObj = jsonTwitterListResponse.getJSONObject(position);
                    if(respTwitterObj != null)
                    {
                        myJson1 = new JSONObject(respTwitterObj.getString("includes"));
                        if(myJson1 != null)
                        {
                            myJsonTweets = new JSONArray(myJson1.getString("tweets"));
                            if(myJsonTweets != null)
                            {
                                respTwitterData = myJsonTweets.getJSONObject(0);

                                if(respTwitterData != null)
                                {
                                    String tweet = respTwitterData.getString("text");
                                    viewHolder.txtTitle.setText((tweet!=null)?tweet:"");
                                    myJsonMedia = new JSONArray(myJson1.getString("media"));

                                    if(myJsonMedia != null){
                                        respMediaData = myJsonMedia.getJSONObject(0);
                                        Log.d("TAG", "<< Response of within twitter respMediaData "+respMediaData);
                                        if(respMediaData.getString("type").equalsIgnoreCase("photo"))
                                        {
                                            tweet_url = respMediaData.getString("url");
                                            viewHolder.imageViewTwitter.setVisibility(View.VISIBLE);
                                            viewHolder.videoViewTwitter.setVisibility(View.GONE);
                                            if(tweet_url != null)
                                            {
                                                Glide.with(mContext)
                                                        .load(tweet_url)
                                                        .into(viewHolder.imageViewTwitter);
                                            }
                                        }
                                        else {
                                            try {
                                                if(respMediaData.getString("type").equalsIgnoreCase("video"))
                                                {
                                                    JSONArray myJsonVideo = new JSONArray(respMediaData.getString("variants"));
                                                    if(myJsonVideo != null) {
                                                        JSONObject respVideoData = myJsonMedia.getJSONObject(0);
                                                        Log.d("TAG", "<< Response of within twitter variants respVideoData "+respVideoData);
                                                        viewHolder.imageViewTwitter.setVisibility(View.GONE);
                                                        viewHolder.videoViewTwitter.setVisibility(View.VISIBLE);

                                                        if(respVideoData.getString("url") != null)
                                                        {
                                                            Uri uri = Uri.parse(respVideoData.getString("url"));
//                                                          itemList.add(respVideoData.getString("url"));
                                                            viewHolder.videoViewTwitter.setVideoURI(uri);
                                                            viewHolder.videoViewTwitter.start();
                                                        }
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
//        return twitterDetailByIDS.size();
        return (jsonTwitterListResponse.length()>0)?jsonTwitterListResponse.length():0;
    }

    static class TwitterAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
//        SliderView sliderView;
        ImageView imageViewTwitter;
        VideoView videoViewTwitter;
        TextView txtTitle;
//        TextView txtDate;

        public TwitterAdapterViewHolder(View itemView) {
            super(itemView);
//            sliderView = itemView.findViewById(R.id.slider_twitter);
            imageViewTwitter = itemView.findViewById(R.id.twitter_img);
            videoViewTwitter = itemView.findViewById(R.id.twitter_video);
            txtTitle = itemView.findViewById(R.id.twitter_title);
//            txtDate = itemView.findViewById(R.id.twitter_date);
            this.itemView = itemView;
        }
    }
}
