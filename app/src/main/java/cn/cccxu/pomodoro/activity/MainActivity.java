package cn.cccxu.pomodoro.activity;

import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.githang.statusbar.StatusBarCompat;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import cn.cccxu.pomodoro.R;
import cn.cccxu.pomodoro.fragment.HistoryFragment;
import cn.cccxu.pomodoro.fragment.MusicFragment;
import cn.cccxu.pomodoro.fragment.SettingsFragment;
import cn.cccxu.pomodoro.fragment.StartFragment;
import cn.cccxu.pomodoro.fragment.TodoFragment;

public class MainActivity extends AppCompatActivity {
    private TodoFragment todoFragment;
    private MusicFragment musicFragment;
    private StartFragment startFragment;
    private HistoryFragment historyFragment;
    private SettingsFragment settingsFragment;

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

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Promodoro");

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
    }

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
                bnve.setBackgroundColor(Color.parseColor("#344FDA"));
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#34A2DA"));
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
                break;
            case R.id.history_fragment:
                hideAll();
                beginTransaction.show(historyFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;
            case R.id.settings_fragment:
                hideAll();
                beginTransaction.show(settingsFragment);
                beginTransaction.addToBackStack(null);
                beginTransaction.commit();
                break;

        }
    }
}
