package cn.cccxu.pomodoro.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jaredrummler.materialspinner.MaterialSpinner;

import cn.cccxu.pomodoro.R;

public class SettingsFragment extends Fragment {

    private MaterialSpinner tamato_spinner;
    private String lengthOfTamato;
    private String lengthOfShortRest;
    private String tamatoBeforeLongRest;
    private String lengthOfLongRest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.settings, container, false);

        //get settings
        final SharedPreferences settings = this.getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = settings.edit();
        lengthOfTamato = settings.getString("LengthOfTamato", "25");
        lengthOfShortRest = settings.getString("LengthOfShortRest", "5");
        tamatoBeforeLongRest = settings.getString("TamatoBeforeLongRest", "4");
        lengthOfLongRest = settings.getString("LengthOfLongRest", "15");

        tamato_spinner = (MaterialSpinner) view.findViewById(R.id.tamato_spinner);
        tamato_spinner.setItems("15", "25", "45");

        switch(lengthOfTamato){
            case "15":
                tamato_spinner.setSelectedIndex(0);
                break;
            case "25":
                tamato_spinner.setSelectedIndex(1);
                break;
            case "45":
                tamato_spinner.setSelectedIndex(2);
                break;
        }


        tamato_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                editor.putString("LengthOfTamato", item.toString());
                editor.commit();
                //Toast.makeText(view.getContext(), settings.getString("LengthOfTamato", "25"), Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

}
