package cn.cccxu.pomodoro.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.cccxu.pomodoro.R;

/**
 * Created by cccxu on 2019/3/23
 */
public class MediaService extends Service {

    private static final String TAG = "MediaService";
    //number of songs
    private int songNum = 0;
    //init media player
    public MediaPlayer mMediaPlayer;
    private MyBinder mBinder = new MyBinder();

    private void initMediaPlayer(){
            //create
            mMediaPlayer = MediaPlayer.create(this, R.raw.noise);
            //loop
            mMediaPlayer.setLooping(true);
    }

    @Override
    public void onCreate(){
        initMediaPlayer();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return mBinder;
    }

    public class MyBinder extends Binder{
        public void playMusic(){
            if(!mMediaPlayer.isPlaying()){
                mMediaPlayer.start();
            }
        }

        public void pauseMusic(){
            if(mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
            }
        }

        public void closeMedia(){
            if(mMediaPlayer != null){
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        }
    }
}
