package com.hem.cleartouchmedia.listener;

import android.util.Log;

import com.hem.cleartouchmedia.activities.CompositionLayoutTwoHorizontalDBActivity;

import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;

public class LayoutTwoHorizontalPlayerListener implements MediaPlayer.EventListener {

    private static String TAG = "PlayerListener";
    private WeakReference<CompositionLayoutTwoHorizontalDBActivity> mOwner;


    public LayoutTwoHorizontalPlayerListener(CompositionLayoutTwoHorizontalDBActivity owner) {
        mOwner = new WeakReference<CompositionLayoutTwoHorizontalDBActivity>(owner);
    }

    @Override
    public void onEvent(MediaPlayer.Event event) {
        CompositionLayoutTwoHorizontalDBActivity player = mOwner.get();

        switch(event.type) {
            case MediaPlayer.Event.EndReached:
                Log.d(TAG, "MediaPlayerEndReached");
                player.releasePlayerZoneOne();
                player.releasePlayerZoneTwo();
                break;
            case MediaPlayer.Event.Playing:
            case MediaPlayer.Event.Paused:
            case MediaPlayer.Event.Stopped:
                player.releasePlayerZoneOne();
                player.releasePlayerZoneTwo();
                break;
            default:
                break;
        }
    }
}
