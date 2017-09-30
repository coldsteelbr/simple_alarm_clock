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

    private static final String SH_TIME = "SH_TIME";
    private static final String SH_STATE = "SH_STATE";

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

        initWidgets();

        mAlarm = new Alarm();

        if (mSP.contains(SH_TIME)) {
            mAlarm.mTime = mSP.getString(SH_TIME, "00:00");
        }

        if (mSP.contains(SH_STATE)) {
            switch (mSP.getString("SH_STATE", "off")) {
                case "on":
                    mAlarm.mState = Alarm.OnOff.ON;
                    break;
                case "off":
                    mAlarm.mState = Alarm.OnOff.OFF;
                    break;
            }
        }

        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
    }

    private void initWidgets() {
        // checkbox
        cbOnOff = findViewById(R.id.cb_onOff);
        cbOnOff.setOnCheckedChangeListener(mOnOffListener);

        tvTime = findViewById(R.id.tv_time);
        tvTime.setText(mTimeFormat.format(calendar.getTime()));
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmMgr.setExact(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            alarmIntent);
                } else {
                    alarmMgr.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.getTimeInMillis(),
                            60000,
                            alarmIntent);
                }
            } else {
                if (alarmMgr != null) {
                    alarmMgr.cancel(alarmIntent);
                }
            }

        }
    }

    class OnTimeClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            FragmentManager fm = getFragmentManager();

            TimePickerFragment dialog = TimePickerFragment.getInstance(calendar);
            dialog.show(fm, TIME_PICKER_DIALOG);
        }
    }
}
