package com.horadadiversao.util;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import com.horadadiversao.R;

/**
 * Created by $tenio nobre$ on 12/12/2015.
 */
public class BackgroundSoundService extends Service {

    private static MediaPlayer player;

    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        if(player == null){
            player = MediaPlayer.create(this, R.raw.backgroundsound);
            player.setLooping(true); // Set looping
            player.setVolume(100,100);
        }

        this.startMusic();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    public static void pauseMusic() {
        if(player != null && player.isPlaying()){
            player.pause();
        }
    }

    public static void startMusic() {
        if(player != null && !player.isPlaying()){
            player.start();
        }
    }

    public static boolean isPlaying(){
        if(player == null) return false;
        if(!player.isPlaying()) return false;

        return true;
    }

}