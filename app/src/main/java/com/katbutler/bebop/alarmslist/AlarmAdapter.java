package com.katbutler.bebop.alarmslist;

import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.katbutler.bebop.R;
import com.katbutler.bebop.model.Alarm;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collections;
import java.util.List;

/**
 * Created by kat on 15-09-05.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private Cursor mCursor;
    private DataSetObserver mDataSetObserver;
    private boolean mDataValid;
    private int mRowIdColumn;
    private OnAlarmStateChangeListener onAlarmStateChangeListener;

    public AlarmAdapter(Cursor cursor) {
        mCursor = cursor;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_item, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Change the underlying cursor to a new cursor. If there is an existing cursor it will be
     * closed.
     */
    public void changeCursor(Cursor cursor) {
        Cursor old = swapCursor(cursor);
        if (old != null) {
            old.close();
        }
    }

    /**
     * Swap in a new Cursor, returning the old Cursor.  Unlike
     * {@link #changeCursor(Cursor)}, the returned old Cursor is <em>not</em>
     * closed.
     */
    public Cursor swapCursor(Cursor newCursor) {
        if (newCursor == mCursor) {
            return null;
        }
        final Cursor oldCursor = mCursor;
        if (oldCursor != null && mDataSetObserver != null) {
            oldCursor.unregisterDataSetObserver(mDataSetObserver);
        }
        mCursor = newCursor;
        if (mCursor != null) {
            if (mDataSetObserver != null) {
                mCursor.registerDataSetObserver(mDataSetObserver);
            }
            mRowIdColumn = newCursor.getColumnIndexOrThrow("_id");
            mDataValid = true;
            notifyDataSetChanged();
        } else {
            mRowIdColumn = -1;
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
        return oldCursor;
    }

    private class NotifyingDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            mDataValid = true;
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
            mDataValid = false;
            notifyDataSetChanged();
            //There is no notifyDataSetInvalidated() method in RecyclerView.Adapter
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Cursor cur = getCursorAtPosition(position);
        final Alarm alarm = Alarm.createFromCursor(cur);

        holder.setTime(alarm.getAlarmTime());
        holder.setAlarmStateOn(alarm.isAlarmStateOn());
        holder.setOnCheckedListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.setAlarmStateOn(isChecked);
                getOnAlarmStateChangeListener().onAlarmStateChange(alarm);
            }
        });
    }

    public interface OnAlarmStateChangeListener {
        void onAlarmStateChange(Alarm alarm);
    }

    @Override
    public int getItemCount() {
        if (mDataValid && mCursor != null) {
            return mCursor.getCount();
        }
        return 0;
    }

    @Override
    public long getItemId(int position) {
        if (mDataValid && mCursor != null && mCursor.moveToPosition(position)) {
            return mCursor.getLong(mRowIdColumn);
        }
        return 0;
    }

    public Cursor getCursor() {
        return mCursor;
    }

    private Cursor getCursorAtPosition(int pos) {
        if (getCursor() != null && getCursor().moveToPosition(pos)) {
            return getCursor();
        }
        return null;
    }

    public boolean isDataValid() {
        return mDataValid;
    }

    public OnAlarmStateChangeListener getOnAlarmStateChangeListener() {
        return onAlarmStateChangeListener;
    }

    public void setOnAlarmStateChangeListener(OnAlarmStateChangeListener onAlarmStateChangeListener) {
        this.onAlarmStateChangeListener = onAlarmStateChangeListener;
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

        public void setOnCheckedListener(CompoundButton.OnCheckedChangeListener listener) {
            mAlarmStateSwitch.setOnCheckedChangeListener(listener);
        }

        public void setAlarmStateOn(boolean alarmStateOn) {
            mAlarmStateSwitch.setChecked(alarmStateOn);
        }
    }
}
