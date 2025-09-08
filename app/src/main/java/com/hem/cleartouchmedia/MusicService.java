package com.hem.cleartouchmedia;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import com.hem.cleartouchmedia.utilities.ApplicationPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service {

    ArrayList<String> mBGMusicList;
    MediaPlayer musicPlayer = null;
    int nowPlaying = 0;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        nowPlaying = ApplicationPreferences.getInstance(this).getBackgroundMusicCount();

        musicPlayer = new MediaPlayer();
        String BG_Music_str = ApplicationPreferences.getInstance(this).getBackgroundMusic();
        System.out.println("===== res background music BG_Music_str url: " + BG_Music_str);
//          String audioUrls = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3";
        mBGMusicList = new ArrayList<>();
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(BG_Music_str);
            for (int i = 0; i < jsonArr.length(); i++)
            {
                JSONObject jsonObj = jsonArr.getJSONObject(i);
                mBGMusicList.add(jsonObj.getString("music_url"));
                System.out.println("<<<<<<<<<<<<<<< Response video url list service: "
                        +Constant.imgURL+""+jsonObj.getString("music_url"));
                System.out.println("<<<<<<<<<<<<<<< Response video playtime_seconds list service: "
                        +jsonObj.getString("playtime_seconds"));
            }
            System.out.println("<<<<<<<<<<<<<<< Response video list size() service: "
                    +mBGMusicList.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(mBGMusicList != null)
        {
            if(mBGMusicList.size()>0)
            {
                if(ApplicationPreferences.getInstance(this)
                        .getBackgroundMusicStatus().equalsIgnoreCase("1"))
                {
                    playSong();
                }
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        Toast.makeText(this, "Music Service started by user.", Toast.LENGTH_LONG).show();
        if(musicPlayer != null) {
            if(ApplicationPreferences.getInstance(this).getBackgroundMusicStatus().equalsIgnoreCase("1"))
            {
                musicPlayer.start();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(musicPlayer != null) {
            if(ApplicationPreferences.getInstance(this).getBackgroundMusicStatus().equalsIgnoreCase("1"))
            {
                musicPlayer.stop();
            }
        }
//        Toast.makeText(this, "Music Service destroyed by user.", Toast.LENGTH_LONG).show();
    }

    private void playSong()
    {
        musicPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        musicPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                try {
                    System.out.println("===== res background music___ inPlaySong onCompletion : "
                            +nowPlaying);
                    nextSong();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        try {
            firstSong();
            System.out.println("===== res background music___ inPlaySong outside");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void firstSong()
    {
        musicPlayer.reset();
//        musicPlayer.stop();
        Uri path = Uri.parse(Constant.imgURL+mBGMusicList.get(nowPlaying));
        try {
            musicPlayer.setDataSource(String.valueOf(path));
            musicPlayer.prepare();
            musicPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void nextSong()
    {
        nowPlaying = nowPlaying+1;
        ApplicationPreferences.getInstance(this)
                .setBackgroundMusicCount(nowPlaying);
        System.out.println("===== res background music___ inNextSong nowPlaying count: " + nowPlaying);
        System.out.println("===== res background music___ inNextSong mBGMusicList.size(): " + mBGMusicList.size());

        if (nowPlaying == mBGMusicList.size()) {
            nowPlaying = 0;
            ApplicationPreferences.getInstance(this)
                    .setBackgroundMusicCount(0);
        }
        musicPlayer.reset();
//        musicPlayer.stop();
        Uri path = Uri.parse(Constant.imgURL+mBGMusicList.get(nowPlaying));
        try {
            musicPlayer.setDataSource(String.valueOf(path));
            musicPlayer.prepare();
            musicPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
