package cn.cccxu.pomodoro.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import cn.cccxu.pomodoro.R;
import cn.cccxu.pomodoro.activity.MainActivity;

/**
 * created by cccxu CQUID 20161730
 */

public class CountDownTimerService extends Service {

    private IBinder mMyBinder = new MyBinder();
    private CountDownTimer mCountDownTimer;

    private String lengthOfTamato;
    private String lengthOfShortRest;
    private String tamatoBeforeLongRest;
    private String lengthOfLongRest;
    //flag = 0: tamato
    //flag = 1: short rest
    //flag = 2: long rest
    private int flag = 0;
    //caunt tamato
    private int count = 0;
    //whether the timer is ticking
    private boolean tick = false;

    private long totalTime;

    public static final String IN_RUNNING = "cn.cccxu.pomodoro.IN_RUNNING";
    public static final String END_RUNNING = "cn.cccxu.END_RUNNING";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel("myCountDown", "countdown channel", NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.createNotificationChannel(chan);

            Notification.Builder builder = new Notification.Builder(this, "myCountDown"); //获取一个Notification构造器
            Intent nfIntent = new Intent(this, MainActivity.class);

            builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.start))
                    .setContentTitle("pomodoro is running")
                    .setSmallIcon(R.drawable.app_icon)
                    .setWhen(System.currentTimeMillis());


            Notification notification = builder.build();
            startForeground(0, notification);
        }else{
            Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
            Intent nfIntent = new Intent(this, MainActivity.class);

            builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0))
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.start))
                    .setContentTitle("pomodoro is running")
                    .setSmallIcon(R.drawable.app_icon)
                    .setWhen(System.currentTimeMillis());

            Notification notification = builder.build();
            startForeground(0, notification);
        }

        return mMyBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //update action
    private void broadcastUpdate(final String action){
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    //update time
    private void broadcastUpdate(final String action, String time){
        final Intent intent = new Intent(action);
        intent.putExtra("time", time);
        intent.putExtra("totalTime", totalTime+"");
        sendBroadcast(intent);
    }

    public class MyBinder extends Binder {
        public void startTimer(){
            if(tick){
                Log.v("tick", "Ticking");
                return;
            }
            //read settings
             getSettings();
             switch(flag){
                 case 0:
                     totalTime = Long.parseLong(lengthOfTamato)* 60 * 1000;
                     break;
                 case 1:
                     totalTime = Long.parseLong(lengthOfShortRest)* 60 * 1000;
                     break;
                 case 2:
                     totalTime = Long.parseLong(lengthOfLongRest)* 60 * 1000;
                     break;
             }

             mCountDownTimer = new CountDownTimer(totalTime, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    broadcastUpdate(IN_RUNNING, millisUntilFinished / 1000 + "");
                }

                @Override
                public void onFinish() {
                    switch(flag){
                        case 0:
                            //finish a tamato
                            count++;
                            if(count % Integer.valueOf(tamatoBeforeLongRest) == 0){
                                flag = 2;
                            }else{
                                flag = 1;
                            }
                            //get one tamato
                            //write to reference file
                            SharedPreferences sharedPreferences = getSharedPreferences("tamato", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            //init on first time
                            if (sharedPreferences.getInt("tamatoCount", -1) == -1) {
                                editor.putInt("tamatoCount", 0);
                                editor.apply();
                            } else {
                                editor.putInt("tamatoCount", sharedPreferences.getInt("tamatoCount", -1) + 1);
                                editor.apply();
                            }
                            break;
                        case 1:
                            //finish short rest
                            flag = 0;
                            break;
                        case 2:
                            //finish long rest
                            flag = 0;
                            break;
                    }
                    broadcastUpdate(END_RUNNING);
                    tick = false;
                }
            };
            mCountDownTimer.start();
            tick = true;
        }

        //this is called by abadon button
        //whenever is called timer will be set to tamato
        public void stopTimer(){
            if(mCountDownTimer == null){
                return;
            }

            if(flag != 0){
                flag = 0;
            }

            mCountDownTimer.cancel();
            tick = false;
        }
    }

    private void getSettings(){
        final SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        lengthOfTamato = settings.getString("LengthOfTamato", "25");
        lengthOfShortRest = settings.getString("LengthOfShortRest", "5");
        tamatoBeforeLongRest = settings.getString("TamatoBeforeLongRest", "4");
        lengthOfLongRest = settings.getString("LengthOfLongRest", "15");
    }

    @Override
    public void onDestroy(){
        stopForeground(true);
        super.onDestroy();
    }
}
