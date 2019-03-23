package cn.cccxu.pomodoro.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yalantis.beamazingtoday.interfaces.AnimationType;
import com.yalantis.beamazingtoday.interfaces.BatModel;
import com.yalantis.beamazingtoday.listeners.BatListener;
import com.yalantis.beamazingtoday.listeners.OnItemClickListener;
import com.yalantis.beamazingtoday.listeners.OnOutsideClickedListener;
import com.yalantis.beamazingtoday.ui.adapter.BatAdapter;
import com.yalantis.beamazingtoday.ui.animator.BatItemAnimator;
import com.yalantis.beamazingtoday.ui.callback.BatCallback;
import com.yalantis.beamazingtoday.ui.widget.BatRecyclerView;

import java.util.ArrayList;
import java.util.List;

import cn.cccxu.pomodoro.R;
import cn.cccxu.pomodoro.util.Goal;

public class TodoFragment extends Fragment implements BatListener, OnItemClickListener, OnOutsideClickedListener {

    private BatRecyclerView mRecyclerView;
    private BatAdapter mAdapter;
    private List<BatModel> mGoals;
    private BatItemAnimator mAnimator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.todo, container, false);
    }

    @Override
    public void onViewCreated(View container, @Nullable Bundle savedInstanceState)
    {
        mRecyclerView = (BatRecyclerView) getView().findViewById(R.id.todo_list);
        mAnimator = new BatItemAnimator();

        mRecyclerView.getView().setLayoutManager(new LinearLayoutManager(this.getActivity()));
        mRecyclerView.getView().setAdapter(mAdapter = new BatAdapter(mGoals = new ArrayList<BatModel>(){{}}, this, mAnimator)
                .setOnItemClickListener(this)
                .setOnOutsideClickListener(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new BatCallback(this));
        itemTouchHelper.attachToRecyclerView(mRecyclerView.getView());
        mRecyclerView.getView().setItemAnimator(mAnimator);
        mRecyclerView.setAddItemListener(this);

        getView().findViewById(R.id.todo_fragment).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mRecyclerView.revertAnimation();
            }
        });
    }

    @Override
    public void add(String s) {
        mGoals.add(0, new Goal(s));
        mAdapter.notify(AnimationType.ADD, 0);
    }

    @Override
    public void delete(int i) {
        mGoals.remove(i);
        mAdapter.notify(AnimationType.REMOVE, i);
    }

    @Override
    public void move(int from, int to) {
        if(from >= 0 && to >= 0){
            mAnimator.setPosition(to);
            BatModel model = mGoals.get(from);
            mGoals.remove(from);
            mGoals.add(to, model);
            mAdapter.notify(AnimationType.MOVE, from, to);
        }

        if(from ==0 || to == 0){
            mRecyclerView.getView().scrollToPosition(Math.min(from, to));
        }
    }

    @Override
    public void onClick(BatModel item, int position){
        Toast.makeText(this.getActivity(), item.getText(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onOutsideClicked(){
        mRecyclerView.revertAnimation();
    }

}
