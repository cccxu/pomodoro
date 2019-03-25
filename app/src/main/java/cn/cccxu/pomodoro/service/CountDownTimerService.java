package cn.cccxu.pomodoro.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
    //abadon
    private boolean abandon = false;

    private long totalTime;
    public static final String IN_RUNNING = "cn.cccxu.pomodoro.IN_RUNNING";
    public static final String END_RUNNING = "cn.cccxu.END_RUNNING";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
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
                            count++;
                            if(count % 4 == 0){
                                flag = 2;
                            }else{
                                flag = 1;
                            }

                            if(!abandon) {
                                //get one tamato
                                //write to reference file
                                SharedPreferences sharedPreferences = getSharedPreferences("tamato", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                //init on first time
                                if (sharedPreferences.getInt("tamatoCount", -1) == -1) {
                                    editor.putInt("tamatoCount", 0);
                                } else {
                                    editor.putInt("tamatoCount", sharedPreferences.getInt("tamatoCount", -1) + 1);
                                }
                            }

                            abandon = false;
                            break;
                        case 1:
                            flag = 0;
                            break;
                        case 2:
                            flag = 0;
                            break;
                    }
                    broadcastUpdate(END_RUNNING);
                }
            };
            mCountDownTimer.start();
            tick = true;
        }

        public void stopTimer(){
            if(mCountDownTimer == null){
                return;
            }

            if(flag == 0){
                abandon = true;
            }

            if(flag != 0){
                flag = 0;
            }


//            switch(flag){
//                case 0:
//                    count++;
//                    if(count % Integer.valueOf(tamatoBeforeLongRest) == 0){
//                        flag = 2;
//                    }else{
//                        flag = 1;
//                    }
//                    break;
//                case 1:
//                    flag = 0;
//                    break;
//                case 2:
//                    flag = 0;
//                    break;
//            }
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
}
