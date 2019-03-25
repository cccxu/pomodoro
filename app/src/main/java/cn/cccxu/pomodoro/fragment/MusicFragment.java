package cn.cccxu.pomodoro.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import cn.cccxu.pomodoro.R;
import cn.cccxu.pomodoro.activity.MainActivity;
import cn.cccxu.view.PlayView;

public class MusicFragment extends Fragment{

    private View view;
    private Button playButton;
    private Button pauseButton;
    private PlayView play;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.music, container, false);
        playButton = view.findViewById(R.id.play);
        pauseButton = view.findViewById(R.id.pause);
        play = view.findViewById(R.id.play_view);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).play();
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).pause();
            }
        });

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(play.isPlaying()){
                    ((MainActivity) getActivity()).pause();
                }else {
                    ((MainActivity) getActivity()).play();
                }
            }
        });

    }

}