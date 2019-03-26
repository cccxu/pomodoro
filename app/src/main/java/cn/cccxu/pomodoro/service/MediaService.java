package cn.cccxu.pomodoro.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cn.cccxu.pomodoro.R;
import cn.cccxu.pomodoro.activity.MainActivity;

/**
 * Created by cccxu CQUID 20161730
 */
public class MediaService extends Service {

    private static final String TAG = "MediaService";
    //number of songs
    private int songNum = 0;
    //init media player
    public MediaPlayer mMediaPlayer;
    Notification notification;
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


    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel("myMusic", "music channel", NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(chan);

            Notification.Builder builder = new Notification.Builder(this, "myMusic");
            Intent nfIntent = new Intent(this, MainActivity.class);

            builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.music))
                    .setContentTitle("pomodoro playing")
                    .setSmallIcon(R.drawable.app_icon)
                    .setWhen(System.currentTimeMillis());

            Notification notification = builder.build();
            startForeground(2, notification);
        }else{
            Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
            Intent nfIntent = new Intent(this, MainActivity.class);

            builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.music))
                    .setContentTitle("pomodoro playing")
                    .setSmallIcon(R.drawable.app_icon)
                    .setWhen(System.currentTimeMillis());

            Notification notification = builder.build();
            startForeground(2, notification);
        }

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
