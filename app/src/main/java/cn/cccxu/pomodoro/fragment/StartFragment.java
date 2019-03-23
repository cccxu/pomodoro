package cn.cccxu.pomodoro.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import cn.cccxu.pomodoro.R;
import cn.cccxu.pomodoro.activity.MainActivity;

public class StartFragment extends Fragment {

    private View view;
    //ui
    private Button start;
    private Button pause;
    private TextView timer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        view = inflater.inflate(R.layout.start, container, false);
        start = view.findViewById(R.id.start);
        pause = view.findViewById(R.id.pause);
        timer = view.findViewById(R.id.timer);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).start();
            }
        });

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).stop();
            }
        });
    }

    public void setText(String text){
        timer.setText(text);
    }
}
