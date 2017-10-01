package ru.romanbrazhnikov.simplealarmclock.view;

import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ru.romanbrazhnikov.simplealarmclock.R;
import ru.romanbrazhnikov.simplealarmclock.model.Alarm;


public class MainActivity
        extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener {
    private static final String TIME_PICKER_DIALOG = "TIME_PICKER_DIALOG";


    private DateFormat mDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private DateFormat mTimeFormat = new SimpleDateFormat("HH:mm");

    private Alarm mAlarm;

    //
    // Listeners
    //
    private OnAlarmCheckedListener mOnOffListener = new OnAlarmCheckedListener();
    private OnTimeClickListener mTimeClickListener = new OnTimeClickListener();

    //
    // Widgets
    //
    private CheckBox cbOnOff;
    private TextView tvTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAlarm = new Alarm(this);

        initWidgets();
    }

    private void initWidgets() {
        // checkbox
        cbOnOff = findViewById(R.id.cb_onOff);
        switch (mAlarm.getState()) {
            case ON:
                cbOnOff.setChecked(true);
                break;
            case OFF:
                cbOnOff.setChecked(false);
                break;
        }
        cbOnOff.setOnCheckedChangeListener(mOnOffListener);

        tvTime = findViewById(R.id.tv_time);
        tvTime.setText(mAlarm.getFormattedTime());
        tvTime.setOnClickListener(mTimeClickListener);
    }


    @Override
    public void onTimeSet(TimePicker tp, int hours, int minutes) {
        mAlarm.setTime(hours, minutes);
        tvTime.setText(String.format("%02d:%02d", hours, minutes));
    }


    class OnAlarmCheckedListener implements CheckBox.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if (b) {
                mAlarm.on();
            } else {
                mAlarm.off();
            }

        }
    }


    /**
     * Shows TimerPickerDialog
     */
    class OnTimeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FragmentManager fm = getFragmentManager();
            TimePickerFragment dialog = TimePickerFragment.getInstance(mAlarm.getFormattedTime());
            dialog.show(fm, TIME_PICKER_DIALOG);
        }
    }
}
