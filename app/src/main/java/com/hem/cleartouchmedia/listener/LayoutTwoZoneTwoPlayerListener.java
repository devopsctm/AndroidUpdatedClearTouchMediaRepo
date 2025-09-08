package com.hem.cleartouchmedia.listener;

import android.content.Context;
import android.util.Log;

import com.hem.cleartouchmedia.activities.CompositionLayoutTwoDBActivity;

import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;

public class LayoutTwoZoneTwoPlayerListener implements MediaPlayer.EventListener {

    private static String TAG = "PlayerListener";
    private WeakReference<Context> mOwner;


    public LayoutTwoZoneTwoPlayerListener(Context owner) {
        mOwner = new WeakReference<Context>(owner);
    }

    @Override
    public void onEvent(MediaPlayer.Event event) {
        CompositionLayoutTwoDBActivity player = (CompositionLayoutTwoDBActivity) mOwner.get();

        switch(event.type) {
            case MediaPlayer.Event.EndReached:
                Log.d(TAG, "MediaPlayerEndReached");
                player.releasePlayerZoneTwo();
                break;
            case MediaPlayer.Event.Playing:
            case MediaPlayer.Event.Paused:
            case MediaPlayer.Event.Stopped:
                player.releasePlayerZoneTwo();
                break;
            default:
                break;
        }
    }
}
