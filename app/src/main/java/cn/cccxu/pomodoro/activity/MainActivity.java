package cn.cccxu.pomodoro.activity;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import cn.cccxu.pomodoro.R;
import cn.cccxu.pomodoro.fragment.HistoryFragment;
import cn.cccxu.pomodoro.fragment.MusicFragment;
import cn.cccxu.pomodoro.fragment.SettingsFragment;
import cn.cccxu.pomodoro.fragment.StartFragment;
import cn.cccxu.pomodoro.fragment.TodoFragment;
import cn.cccxu.pomodoro.service.CountDownTimerService;
import cn.cccxu.pomodoro.service.MediaService;

/**
 * create by cccxu CQUID 20161730
 */
public class MainActivity extends AppCompatActivity{
    //five fragment
    //todo : combine MusicFragment with StartFragment
    //todo : combine HistoryFragment with SettingsFragment
    private TodoFragment todoFragment;
    private MusicFragment musicFragment;
    private StartFragment startFragment;
    private HistoryFragment historyFragment;
    private SettingsFragment settingsFragment;

    //mHandler is for music service
    //mHandlerCount is for countdown service
    private Handler mHandler = new Handler();
    private Handler mHandlerCount = new Handler();
    //use binder to call method in service
    private static MediaService.MyBinder mMyBinder;
    private static CountDownTimerService.MyBinder mMyBinderCount;

    Intent mMediaServiceIntent;
    Intent mCountDownTimerIntent;

    //to achieve press two back to cancel app
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set title
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Pomodoro");

        //bnve is a third party BottomNavigationView
        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        bnve.setOnNavigationItemSelectedListener(mNavigationItemSelectedListener);

        //fragment init and bind
        todoFragment = new TodoFragment();
        musicFragment = new MusicFragment();
        startFragment = new StartFragment();
        historyFragment = new HistoryFragment();
        settingsFragment = new SettingsFragment();

        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        beginTransaction.add(R.id.content, todoFragment)
                .add(R.id.content, musicFragment)
                .add(R.id.content, startFragment)
                .add(R.id.content, historyFragment)
                .add(R.id.content, settingsFragment)
                .commit();

        hideAll();

        showFragment(R.id.todo_fragment);

        //bind service
        mMediaServiceIntent = new Intent(this, MediaService.class);
        bindService(mMediaServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);


        mCountDownTimerIntent = new Intent(this, CountDownTimerService.class);
        bindService(mCountDownTimerIntent, mServiceConnectionCount, Context.BIND_AUTO_CREATE);

        //use shared preference to save data
        SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        //check iif the preference exist
        if(settings.getString("LengthOfTamato", null) == null){
            //put default value
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("LengthOfTamato", "25");
            editor.putString("LengthOfShortRest", "5");
            editor.putString("TamatoBeforeLongRest", "4");
            editor.putString("LengthOfLongRest", "15");
            editor.apply();
        }
    }

    //set bottomNavagationView listener
    private BottomNavigationViewEx.OnNavigationItemSelectedListener mNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.todo:
                    showFragment(R.id.todo_fragment);
                    return true;
                case R.id.music:
                    showFragment(R.id.music_fragment);
                    return true;
                case R.id.start:
                    showFragment(R.id.start_fragment);
                    return true;
                case R.id.settings:
                    showFragment(R.id.settings_fragment);
                    return true;
                case R.id.history:
                    showFragment(R.id.history_fragment);
                    return true;
            }
            return false;
        }
    };

    //connect music service
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinder = (MediaService.MyBinder) service;
            mHandler.post(mRunnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    //count countdown service
    private ServiceConnection mServiceConnectionCount = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mMyBinderCount = (CountDownTimerService.MyBinder) service;
            mHandlerCount.post(mRunnable);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event){

        if( keyCode== KeyEvent.KEYCODE_HOME){
            return true;
        } else if( keyCode== KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()- exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    //stop and unbind service when cancel the app
    @Override
    public void onDestroy(){
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
        mMyBinder.closeMedia();
        mMyBinderCount.stopTimer();
        unbindService(mServiceConnection);
        unbindService(mServiceConnectionCount);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // regist receiver
        registerReceiver(mUpdateReceiver, updateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // remove receiver
        unregisterReceiver(mUpdateReceiver);
    }

    //update UI
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    //hide all fragment
    private void hideAll(){
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        beginTransaction.hide(todoFragment)
                .hide(musicFragment)
                .hide(startFragment)
                .hide(historyFragment)
                .hide(settingsFragment)
                .addToBackStack(null)
                .commit();
    }

    private void showFragment(int frag){
        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bnve);
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        switch(frag){
            case R.id.todo_fragment:
                hideAll();
                beginTransaction.show(todoFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                //make the color of bottom and status bar looks better
                //StatusBarCompat is a third party tool to make it easier
                //bnve.setBackgroundColor(Color.parseColor("#34A2DA"));
                //bnve.getIconAt(2).setColorFilter(ColorFilter);
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#ffffff"));
                break;
            case R.id.music_fragment:
                hideAll();
                beginTransaction.show(musicFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
            case R.id.start_fragment:
                hideAll();
                beginTransaction.show(startFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                mMyBinderCount.startTimer();
                break;
            case R.id.history_fragment:
                hideAll();
                beginTransaction.show(historyFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                //update the history tamato
                historyFragment.updateTamatoNum();
                break;
            case R.id.settings_fragment:
                hideAll();
                beginTransaction.show(settingsFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;

        }
    }

    //this is for MusicFragment to use
    //start play music
    public void play(){
        mMyBinder.playMusic();
    }
    //this is for MusicFragment to use
    //pause the music
    public void pause(){
        mMyBinder.pauseMusic();
    }

    private static IntentFilter updateIntentFilter(){
        final IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(CountDownTimerService.IN_RUNNING);
        mIntentFilter.addAction(CountDownTimerService.END_RUNNING);
        return mIntentFilter;
    }

    private final BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            switch(action){
                case CountDownTimerService.IN_RUNNING:
                    //do sth
                    String secondstr = intent.getStringExtra("time");
                    String maxstr = intent.getStringExtra("totalTime");
                    int seconds = Integer.valueOf(secondstr);
                    int minutes = seconds / 60;
                    seconds = seconds % 60;
                    //build timer text
                    //to make it looks better, when the number < 10, add blankspace(for minutes) or 0(for seconds)
                    String time;
                    if(seconds < 10){
                        time = String.valueOf(minutes) + " : 0" + String.valueOf(seconds);
                        if(minutes < 10){
                            time = " " + time;
                        }
                    }else {
                        time = String.valueOf(minutes) + " : " + String.valueOf(seconds);
                        if(minutes < 10){
                            time = " " + time;
                        }
                    }
                    //set timer text and progress
                    startFragment.setText(time);
                    startFragment.setProgress(Integer.valueOf(maxstr)/1000,Integer.valueOf(secondstr));
                    break;
                case CountDownTimerService.END_RUNNING:
                    //start the next auto
                    start();
                    break;
                default:
                    makeToast("No MSG");
                    break;
            }
        }
    };

    //to simplfy make toast
    private void makeToast(String text){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    //this is for MusicFragment to use
    //to start count
    public void start(){
        mMyBinderCount.startTimer();
    }

    //this is for MusicFragment to use
    //to abandon / stop timer
    public void stop(){
        mMyBinderCount.stopTimer();
        //make the timer text looks better
        SharedPreferences settings = getSharedPreferences("settings", Context.MODE_PRIVATE);
        startFragment.setText(settings.getString("LengthOfTamato", "25") + " : 00");
    }

}
