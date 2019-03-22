package cn.cccxu.pomodoro.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import cn.cccxu.pomodoro.R;

public class MusicFragment extends Fragment {
    private Button isPlay;
    private Button stop;
    private Button quit;

    private ImageView coverImage;
    // private ObjectAnimator animator;
    private int flag = 0;

    private TextView totalTime;
    private TextView playingTime;
    private TextView stateText;

    private SeekBar seekBar;
    private TextView pathText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.music, container, false);
    }

}
