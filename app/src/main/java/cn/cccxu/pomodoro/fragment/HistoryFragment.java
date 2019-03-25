package cn.cccxu.pomodoro.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.cccxu.pomodoro.R;

public class HistoryFragment extends Fragment {

    private TextView tamatoNum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.history, container, false);
        tamatoNum = (TextView) view.findViewById(R.id.tamatoNum);
        return view;
    }

    public void updateTamatoNum(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("tamato", Context.MODE_PRIVATE);
        tamatoNum.setText(sharedPreferences.getInt("tamatoCount", 0)+"");
    }
}
