package com.katbutler.bebop.alarm;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.katbutler.bebop.R;

/**
 * Created by kat on 15-09-16.
 */
public class AlarmOffFragment extends Fragment {

    private Handler mHandler = new Handler();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AlarmOffFragment.this.getActivity().finish();
            }
        }, 2000);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm_off, container, false);

        return view;
    }
}
