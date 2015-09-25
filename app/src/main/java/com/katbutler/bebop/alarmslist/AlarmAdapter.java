package com.katbutler.bebop.alarmslist;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.katbutler.bebop.R;
import com.katbutler.bebop.model.Alarm;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by kat on 15-09-05.
 */
public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private Cursor mCursor;
    private DataSetObserver mDataSetObserver;
    private boolean mDataValid;
    private int mRowIdColumn;
    private OnAlarmStateChangeListener onAlarmStateChangeListener;
    private OnAlarmDeleteListener onAlarmDeleteListener;
    private LayoutInflater mFactory;



    public interface OnAlarmStateChangeListener {
        void onAlarmStateChange(Alarm alarm);
    }

    public interface OnAlarmDeleteListener {
        void onAlarmDeleted(Alarm alarm);
    }


    public AlarmAdapter(Cursor cursor) {
        mCursor = cursor;
        mDataSetObserver = new NotifyingDataSetObserver();
        if (mCursor != null) {
            mCursor.registerDataSetObserver(mDataSetObserver);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mFactory == null) {
            setLayoutInflater(parent.getContext());
        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alarm_list_item, parent, false);
        return new ViewHolder(v);
    }

    private void setLayoutInflater(Context context) {
        mFactory = LayoutInflater.from(context);
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
        holder.setAlarmStateOn(alarm.isEnabled());
        holder.setDeleteBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getOnAlarmDeleteListener() != null) {
                    getOnAlarmDeleteListener().onAlarmDeleted(alarm);
                }
            }
        });
        holder.setOnCheckedListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                alarm.setEnabled(isChecked);
                if (getOnAlarmStateChangeListener() != null)
                    getOnAlarmStateChangeListener().onAlarmStateChange(alarm);
            }
        });

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

    public OnAlarmDeleteListener getOnAlarmDeleteListener() {
        return onAlarmDeleteListener;
    }

    public void setOnAlarmDeleteListener(OnAlarmDeleteListener onAlarmDeleteListener) {
        this.onAlarmDeleteListener = onAlarmDeleteListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTimeTextView;
        private final TextView mAmPmText;
        private final Switch mAlarmStateSwitch;
        private final LinearLayout mRepeatDays;
        private final ImageButton mDeleteBtn;
        private final Button[] dayButtons = new Button[7];

        public ViewHolder(View itemView) {
            super(itemView);
            mTimeTextView = (TextView) itemView.findViewById(R.id.time_text);
            mAmPmText = (TextView) itemView.findViewById(R.id.ampm_text);
            mAlarmStateSwitch = (Switch) itemView.findViewById(R.id.alarm_state_switch);
            mDeleteBtn = (ImageButton) itemView.findViewById(R.id.delete_btn);
            mRepeatDays = (LinearLayout) itemView.findViewById(R.id.repeat_days);

            // Build button for each day.
            for (int i = 0; i < 7; i++) {
                final Button dayButton = (Button) mFactory.inflate(R.layout.day_button, mRepeatDays, false /* attachToRoot */);
                dayButton.setText("S"); //mShortWeekDayStrings[DAY_ORDER[i]]
                dayButton.setContentDescription("Sunday"); //mLongWeekDayStrings[DAY_ORDER[i]]
                mRepeatDays.addView(dayButton);
                dayButtons[i] = dayButton;
            }
        }

        public void setTime(LocalTime time) {
            DateTimeFormatter fmtTime = DateTimeFormat.forPattern("h:mm");
            DateTimeFormatter fmtAmPm = DateTimeFormat.forPattern("a");

            mTimeTextView.setText(fmtTime.print(time));
            mAmPmText.setText(fmtAmPm.print(time));
        }

        public void setDeleteBtnOnClickListener(View.OnClickListener listener) {
            mDeleteBtn.setOnClickListener(listener);
        }

        public void setOnCheckedListener(CompoundButton.OnCheckedChangeListener listener) {
            mAlarmStateSwitch.setOnCheckedChangeListener(listener);
        }

        public void setAlarmStateOn(boolean alarmStateOn) {
            mAlarmStateSwitch.setChecked(alarmStateOn);
        }
    }
}
