package cn.cccxu.pomodoro.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class CountDownTimerService extends Service {

    private IBinder mMyBinder = new MyBinder();
    private CountDownTimer mCountDownTimer = new CountDownTimer(600000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            broadcastUpdate(IN_RUNNING, millisUntilFinished / 1000 + "");
        }

        @Override
        public void onFinish() {
            broadcastUpdate(END_RUNNING);
        }
    };
    private long totalTime = 600000;
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
        intent.putExtra("totalTime", totalTime);
        sendBroadcast(intent);
    }

    public class MyBinder extends Binder {
        public void startTimer(){
            mCountDownTimer.start();
        }

        public void stopTimer(){
            mCountDownTimer.cancel();
        }
    }
}
