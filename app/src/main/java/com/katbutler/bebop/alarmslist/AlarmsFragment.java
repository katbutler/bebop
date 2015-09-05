package com.katbutler.bebop.alarmslist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katbutler.bebop.R;
import com.katbutler.bebopcommon.BaseFragment;

import java.util.List;

/**
 * Created by kat on 15-09-05.
 */
public class AlarmsFragment extends BaseFragment<AlarmsPresenter, AlarmsPresenter.AlarmsUi> implements AlarmsPresenter.AlarmsUi {


    private RecyclerView mAlarmRecyclerView;

    @Override
    public AlarmsPresenter createPresenter() {
        return new AlarmsPresenter();
    }

    @Override
    public AlarmsPresenter.AlarmsUi getUi() {
        return this;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarms, container);

        mAlarmRecyclerView = findViewOnView(view,R.id.alarm_recyclerview);
        return view;
    }

    @Override
    public List<String> getAlarmList() {
        return null;
    }
}
