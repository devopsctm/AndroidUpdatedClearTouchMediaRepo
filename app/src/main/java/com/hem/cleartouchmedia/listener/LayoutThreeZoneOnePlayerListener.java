package com.hem.cleartouchmedia.listener;

import android.content.Context;
import android.util.Log;
import com.hem.cleartouchmedia.activities.CompositionLayoutThreeDBActivity;
import org.videolan.libvlc.MediaPlayer;
import java.lang.ref.WeakReference;

public class LayoutThreeZoneOnePlayerListener implements MediaPlayer.EventListener {

    private static String TAG = "PlayerListener";
    private WeakReference<Context> mOwner;

    public LayoutThreeZoneOnePlayerListener(Context owner) {
        mOwner = new WeakReference<Context>(owner);
    }

    @Override
    public void onEvent(MediaPlayer.Event event) {
        CompositionLayoutThreeDBActivity player = (CompositionLayoutThreeDBActivity) mOwner.get();

        switch(event.type) {
            case MediaPlayer.Event.EndReached:
                Log.d(TAG, "MediaPlayerEndReached");
                player.releasePlayerZoneOne();
                break;
            case MediaPlayer.Event.Playing:
            case MediaPlayer.Event.Paused:
            case MediaPlayer.Event.Stopped:
            default:
                break;
        }
    }
}
