package ru.romanbrazhnikov.simplealarmclock;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity
        extends AppCompatActivity
        implements SelectedDateInterface,
        SelectedTimeInterface {
    private static final String DATE_PICKER_DIALOG = "DATE_PICKER_DIALOG";
    private static final String TIME_PICKER_DIALOG = "TIME_PICKER_DIALOG";

    private DateFormat mDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    private DateFormat mTimeFormat = new SimpleDateFormat("HH:mm");

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Calendar calendar = Calendar.getInstance();

    //
    // Listeners
    //
    private OnAlarmCheckedListener mOnOffListener = new OnAlarmCheckedListener();
    private OnDateClickListener mDateClickListener = new OnDateClickListener();
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

        calendar.setTimeInMillis(System.currentTimeMillis());
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        // checkbox
        cbOnOff = findViewById(R.id.cb_onOff);
        cbOnOff.setOnCheckedChangeListener(mOnOffListener);

        // text fields
        tvDate = findViewById(R.id.tv_date);
        tvDate.setText(mDateFormat.format(calendar.getTime()));
        tvDate.setOnClickListener(mDateClickListener);

        tvTime = findViewById(R.id.tv_time);
        tvTime.setText(mTimeFormat.format(calendar.getTime()));
        tvTime.setOnClickListener(mTimeClickListener);


        Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        tvDate.setText(day + "." + month + "." + year);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    @Override
    public void onTimeSet(int hours, int minutes) {
        tvTime.setText(hours + ":" + minutes);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    class OnAlarmCheckedListener implements CheckBox.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
                alarmMgr.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        60000,
                        alarmIntent);
            } else {
                if (alarmMgr != null) {
                    alarmMgr.cancel(alarmIntent);
                }
            }

        }
    }

    class OnDateClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // TODO: send string and formatter, not Date
            FragmentManager fm = getFragmentManager();
            Date date = null;
            try {
                date = mDateFormat.parse(tvDate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date == null) {
                date = new Date();
            }
            DatePickerFragment dialog = DatePickerFragment.getInstance(date);
            dialog.show(fm, DATE_PICKER_DIALOG);
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
