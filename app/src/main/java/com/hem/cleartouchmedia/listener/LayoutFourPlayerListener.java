package com.hem.cleartouchmedia.listener;

import android.util.Log;

import com.hem.cleartouchmedia.activities.CompositionLayoutFourDBActivity;

import org.videolan.libvlc.MediaPlayer;

import java.lang.ref.WeakReference;

public class LayoutFourPlayerListener implements MediaPlayer.EventListener {

    private static String TAG = "PlayerListener";
    private WeakReference<CompositionLayoutFourDBActivity> mOwner;


    public LayoutFourPlayerListener(CompositionLayoutFourDBActivity owner) {
        mOwner = new WeakReference<CompositionLayoutFourDBActivity>(owner);
    }

    @Override
    public void onEvent(MediaPlayer.Event event) {
        CompositionLayoutFourDBActivity player = mOwner.get();

        switch(event.type) {
            case MediaPlayer.Event.EndReached:
                Log.d(TAG, "MediaPlayerEndReached");
                player.releasePlayerZoneOne();
                player.releasePlayerZoneTwo();
                player.releasePlayerZoneThree();
                player.releasePlayerZoneFour();
                break;
            case MediaPlayer.Event.Playing:
            case MediaPlayer.Event.Paused:
            case MediaPlayer.Event.Stopped:
                player.releasePlayerZoneOne();
                player.releasePlayerZoneTwo();
                player.releasePlayerZoneThree();
                player.releasePlayerZoneFour();
                break;
            default:
                break;
        }
    }
}
