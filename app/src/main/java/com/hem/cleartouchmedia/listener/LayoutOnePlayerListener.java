package com.hem.cleartouchmedia.listener;

import android.content.Context;
import android.util.Log;

import com.hem.cleartouchmedia.activities.CompositionLayoutOneDBActivity;
import com.hem.cleartouchmedia.activities.CompositionLayoutThreeDBActivity;

import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;

public class LayoutOnePlayerListener implements MediaPlayer.EventListener {

    private static String TAG = "PlayerListener";
    private WeakReference<Context> mOwner;

    public LayoutOnePlayerListener(Context owner) {
        mOwner = new WeakReference<Context>(owner);
    }

    @Override
    public void onEvent(MediaPlayer.Event event) {
        CompositionLayoutOneDBActivity player = (CompositionLayoutOneDBActivity) mOwner.get();
        switch(event.type) {
            case MediaPlayer.Event.EndReached:
                Log.d(TAG, "MediaPlayerEndReached");
                player.releasePlayer();
                break;
            case MediaPlayer.Event.Playing:
            case MediaPlayer.Event.Paused:
            case MediaPlayer.Event.Stopped:
                player.releasePlayer();
                break;
            default:
                break;
        }
    }
}
