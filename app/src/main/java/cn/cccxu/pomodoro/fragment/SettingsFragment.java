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

/**
 * created by cccxu CQUID 20161730
 */

public class SettingsFragment extends Fragment {

    private MaterialSpinner tamato_spinner;
    private MaterialSpinner shortrest_spinner;
    private MaterialSpinner longrest_spinner;
    private MaterialSpinner gap_spinner;

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

        //set spinner
        tamato_spinner = (MaterialSpinner) view.findViewById(R.id.tamato_spinner);
        shortrest_spinner = (MaterialSpinner) view.findViewById(R.id.shortrest_spinner);
        longrest_spinner = (MaterialSpinner) view.findViewById(R.id.longrest_spinner);
        gap_spinner = (MaterialSpinner) view.findViewById(R.id.gap_spinner);

        tamato_spinner.setItems("15", "25", "45");
        shortrest_spinner.setItems("5", "10", "15");
        longrest_spinner.setItems("10", "15", "25");
        gap_spinner.setItems("3", "4", "6");

        switch(tamatoBeforeLongRest){
            case "3":
                gap_spinner.setSelectedIndex(0);
                break;
            case "4":
                gap_spinner.setSelectedIndex(1);
                break;
            case "6":
                gap_spinner.setSelectedIndex(2);
                break;
        }

        switch(lengthOfLongRest){
            case "10":
                longrest_spinner.setSelectedIndex(0);
                break;
            case "15":
                longrest_spinner.setSelectedIndex(1);
                break;
            case "25":
                longrest_spinner.setSelectedIndex(2);
                break;
        }

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

        switch(lengthOfShortRest){
            case "5":
                shortrest_spinner.setSelectedIndex(0);
                break;
            case "10":
                shortrest_spinner.setSelectedIndex(1);
                break;
            case "15":
                shortrest_spinner.setSelectedIndex(2);
                break;
        }

        shortrest_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                editor.putString("LengthOfShortRest", item.toString());
                editor.commit();
                //Toast.makeText(view.getContext(), settings.getString("LengthOfTamato", "25"), Toast.LENGTH_LONG).show();
            }
        });

        longrest_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                editor.putString("LengthOfLongRest", item.toString());
                editor.commit();
                //Toast.makeText(view.getContext(), settings.getString("LengthOfTamato", "25"), Toast.LENGTH_LONG).show();
            }
        });

        gap_spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                editor.putString("TamatoBeforeLongRest", item.toString());
                editor.commit();
                //Toast.makeText(view.getContext(), settings.getString("LengthOfTamato", "25"), Toast.LENGTH_LONG).show();
            }
        });

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
