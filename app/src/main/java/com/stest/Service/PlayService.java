package com.stest.Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by ldy on 6/12/16.
 */
public class PlayService extends Service {

    private static final int PLAY_STOP = 3;
    private static final int PLAY_PAUSE = 2;
    private static final int PLAY_NEW_SONG = 1;
    private static final int PLAY_CONTINUE=0;
    private String path;
    private boolean isPause;
    private MediaPlayer myMediaplayer = new MediaPlayer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (myMediaplayer.isPlaying()) {
            myMediaplayer.stop();
        }
        path = intent.getStringExtra("url");
        int msg = intent.getIntExtra("message", 0);

        if (msg == PLAY_NEW_SONG) {
            play(0);
        } else if (msg == PLAY_PAUSE) {
            if (myMediaplayer != null && myMediaplayer.isPlaying()) {
                myMediaplayer.pause();
                isPause = true;
            }

        } else if (msg == PLAY_STOP) {
            if (myMediaplayer != null)
                myMediaplayer.stop();
            try {
                myMediaplayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(msg==PLAY_CONTINUE)
        {
            if(myMediaplayer!=null&&isPause==true)
            {
                play(myMediaplayer.getCurrentPosition());
                isPause=false;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void play(int i) {
        try {
            myMediaplayer.reset();
            myMediaplayer.setDataSource(path);
            myMediaplayer.prepare();
            myMediaplayer.setOnPreparedListener(new MyOnPreparedListener(i));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener {
        private int position;

        public MyOnPreparedListener(int position) {
            this.position = position;
        }

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            myMediaplayer.start();
            if (position > 0) {
                myMediaplayer.seekTo(position);

            }
        }
    }
}

