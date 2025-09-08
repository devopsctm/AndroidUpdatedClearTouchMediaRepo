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

import com.hem.cleartouchmedia.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class TwitterItemAdapter extends SliderViewAdapter<TwitterItemAdapter.TwitterAdapterViewHolder> {

    Context mContext;
    List<String> itemList;

    // Constructor
    public TwitterItemAdapter(Context context, List<String> itemList) {
        this.mContext = context;
        this.itemList = itemList;
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
        Log.d("TAG", "<< Response of within twitter list size(); "+ itemList.size());

    }

    // this method will return
    // the count of our list.
    @Override
    public int getCount() {
//        return twitterDetailByIDS.size();
        return itemList.size();
    }

    static class TwitterAdapterViewHolder extends ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView imageViewTwitter;
        VideoView videoViewTwitter;
        TextView txtTitle;
//        TextView txtDate;

        public TwitterAdapterViewHolder(View itemView) {
            super(itemView);
            imageViewTwitter = itemView.findViewById(R.id.twitter_img);
            videoViewTwitter = itemView.findViewById(R.id.twitter_video);
            txtTitle = itemView.findViewById(R.id.twitter_title);
//            txtDate = itemView.findViewById(R.id.twitter_date);
            this.itemView = itemView;
        }
    }
}
