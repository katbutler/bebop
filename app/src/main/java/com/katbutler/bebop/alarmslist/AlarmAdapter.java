package com.katbutler.bebop.alarmslist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.katbutler.bebop.R;
import com.katbutler.bebop.model.Alarm;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by kat on 15-09-05.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private final List<Alarm> mAlarms;

    public AlarmAdapter(List<Alarm> alarms) {
        mAlarms = alarms;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Alarm alarm = mAlarms.get(position);

        holder.setTime(alarm.getAlarmTime());
    }

    @Override
    public int getItemCount() {
        return mAlarms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTimeTextView;
        private final TextView mAmPmText;
        private final Switch mAlarmStateSwitch;

        public ViewHolder(View itemView) {
            super(itemView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.time_text);
            mAmPmText = (TextView) itemView.findViewById(R.id.ampm_text);
            mAlarmStateSwitch = (Switch) itemView.findViewById(R.id.alarm_state_switch);
        }

        public void setTime(LocalTime time) {
            DateTimeFormatter fmtTime = DateTimeFormat.forPattern("h:mm");
            DateTimeFormatter fmtAmPm = DateTimeFormat.forPattern("a");

            mTimeTextView.setText(fmtTime.print(time));
            mAmPmText.setText(fmtAmPm.print(time));
        }
    }
}
