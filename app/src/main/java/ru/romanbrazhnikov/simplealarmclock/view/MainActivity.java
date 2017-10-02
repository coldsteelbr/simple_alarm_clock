package ru.romanbrazhnikov.simplealarmclock.view;

import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
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
    public static final String SHARED_ALARM_FILE = "ru.romanbrazhnikov.simplealarmclock.SHARED_ALARM_FILE";
    private static final String SHARED_TIME = "SHARED_TIME";
    private static final String SHARED_STATE = "SHARED_STATE";


    private DateFormat mDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private DateFormat mTimeFormat = new SimpleDateFormat("HH:mm");

    private Alarm mAlarm;
    private SharedPreferences mSharedPreferences;

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
        mSharedPreferences = getSharedPreferences(SHARED_ALARM_FILE, MODE_PRIVATE);
        mAlarm = new Alarm(this);

        initWidgets();
    }

    @Override
    protected void onResume() {
        super.onResume();
        restoreAlarmState();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveAlarmState();
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

    private void restoreAlarmState() {
        // time
        String time = mSharedPreferences.getString(SHARED_TIME, "00:00");
        tvTime.setText(time);
        // state
        String stateString = mSharedPreferences.getString(SHARED_STATE, "off");
        cbOnOff.setOnCheckedChangeListener(null);
        switch (stateString) {
            case "on":
                cbOnOff.setChecked(true);
                break;
            case "off":
                cbOnOff.setChecked(false);
                break;
        }
        cbOnOff.setOnCheckedChangeListener(mOnOffListener);
    }

    private void saveAlarmState() {
        mSharedPreferences.edit()
                .putString(SHARED_TIME, tvTime.getText().toString())
                .putString(SHARED_STATE, mAlarm.getState().asString())
                .apply();
    }

    @Override
    public void onTimeSet(TimePicker tp, int hours, int minutes) {
        mAlarm.setTime(hours, minutes);
        tvTime.setText(String.format("%02d:%02d", hours, minutes));
    }


    /**
     * Alarm On/Off listener
     */
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
