package ru.romanbrazhnikov.simplealarmclock.view;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.romanbrazhnikov.simplealarmclock.AlarmReceiver;
import ru.romanbrazhnikov.simplealarmclock.R;
import ru.romanbrazhnikov.simplealarmclock.model.Alarm;


public class MainActivity
        extends AppCompatActivity
        implements TimePickerDialog.OnTimeSetListener {
    private static final String TIME_PICKER_DIALOG = "TIME_PICKER_DIALOG";

    private static final String SP_TIME = "SP_TIME";
    private static final String SP_STATE = "SP_STATE";

    private DateFormat mDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private DateFormat mTimeFormat = new SimpleDateFormat("HH:mm");

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Calendar calendar = Calendar.getInstance();
    private Alarm mAlarm;
    private SharedPreferences mSP;

    //
    // Listeners
    //
    private OnAlarmCheckedListener mOnOffListener = new OnAlarmCheckedListener();
    private OnTimeClickListener mTimeClickListener = new OnTimeClickListener();

    //
    // Widgets
    //
    private CheckBox cbOnOff;
    private TextView tvDate;
    private TextView tvTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSP = getPreferences(MODE_PRIVATE);

        // TODO: set from the state
        calendar.setTimeInMillis(System.currentTimeMillis());
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        mAlarm = new Alarm();


        mAlarm.mTime = mSP.getString(SP_TIME, "00:00");


        switch (mSP.getString("SP_STATE", "off")) {
            case "on":
                mAlarm.mState = Alarm.OnOff.ON;
                break;
            case "off":
                mAlarm.mState = Alarm.OnOff.OFF;
                break;
        }


        initWidgets();

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
    }

    private void initWidgets() {
        // checkbox
        cbOnOff = findViewById(R.id.cb_onOff);
        switch (mAlarm.mState) {
            case ON:
                cbOnOff.setChecked(true);
                break;
            case OFF:
                cbOnOff.setChecked(false);
                break;
        }
        cbOnOff.setOnCheckedChangeListener(mOnOffListener);

        tvTime = findViewById(R.id.tv_time);
        tvTime.setText(mAlarm.mTime);
        tvTime.setOnClickListener(mTimeClickListener);
    }


    @Override
    public void onTimeSet(TimePicker tp, int hours, int minutes) {
        tvTime.setText(String.format("%02d:%02d", hours, minutes));
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    class OnAlarmCheckedListener implements CheckBox.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            if (b) {
                Log.d("RUN: ", mDateFormat.format(calendar.getTime()));
                Log.d("RUN: ", mTimeFormat.format(calendar.getTime()));
                resetAlarm();

                saveAlarmState(Alarm.OnOff.ON);

            } else {
                cancelAlarm();

                saveAlarmState(Alarm.OnOff.OFF);
            }

        }
    }

    /**
     * Saves alarm state to shared preferences
     */
    private void saveAlarmState(Alarm.OnOff state) {
        SharedPreferences.Editor editor = mSP.edit();
        editor.putString(SP_TIME, tvTime.getText().toString());
        switch (state) {
            case ON:
                editor.putString(SP_STATE, "on");
                break;
            case OFF:
                editor.putString(SP_STATE, "off");
                break;
        }
    }

    /**
     * Cancels an old alarm and sets again a new one
     */
    private void resetAlarm() {
        // first trying to cancel
        cancelAlarm();
        //TODO: NEXT: calendar.setTime(new Date());

        // check if TIME less or more than the current time
        // add 1 day to the calendar then

        // then resetting
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmMgr.setExact(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    alarmIntent);
        } else {
            alarmMgr.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    alarmIntent);
        }
    }

    /**
     * Cancels current alarm
     */
    private void cancelAlarm() {
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }
    }

    /**
     * Shows TimerPickerDialog
     */
    class OnTimeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FragmentManager fm = getFragmentManager();
            TimePickerFragment dialog = TimePickerFragment.getInstance(calendar);
            dialog.show(fm, TIME_PICKER_DIALOG);
        }
    }
}
