package cn.cccxu.pomodoro.util;

import com.yalantis.beamazingtoday.interfaces.BatModel;

public class TodoListUtil implements BatModel {

    private String name;

    private boolean isChecked;

    public TodoListUtil(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setChecked(boolean checked){
        isChecked = checked;
    }

    @Override
    public boolean isChecked(){
        return isChecked;
    }

    @Override
    public String getText(){
        return getName();
    }
}
