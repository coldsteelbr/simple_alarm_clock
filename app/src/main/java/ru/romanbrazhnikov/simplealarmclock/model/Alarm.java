package ru.romanbrazhnikov.simplealarmclock.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Pair;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.romanbrazhnikov.simplealarmclock.AlarmReceiver;

/**
 * Created by roman on 30.09.17.
 */

public class Alarm {
    // State
    public enum OnOff {

        ON("on"),
        OFF("off");
        private String mAsString;

        OnOff(String asString) {
            mAsString = asString;
        }

        public String asString() {
            return mAsString;
        }
    }

    //private final Context mContext;
    private DateFormat mTimeFormat = new SimpleDateFormat("HH:mm");
    private OnOff mState = OnOff.OFF;
    private Calendar mCalendar = Calendar.getInstance();
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private SharedPreferences mSharedPreferences;

    public Alarm(Context context) {
        // Init AlarmManager and Pending alarm intent
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    public void setTime(String time) throws ParseException {
        mCalendar.setTime(mTimeFormat.parse(time));
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
    }

    public void setTime(int hour, int minute) {
        mCalendar = Calendar.getInstance(); // to reset the calendar
        mCalendar.set(Calendar.HOUR_OF_DAY, hour);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, 0);
        mCalendar.set(Calendar.MILLISECOND, 0);
    }

    public Pair<Integer, Integer> getTimeAsPair() {
        return new Pair<>(mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));
    }

    public String getFormattedTime() {
        return mTimeFormat.format(mCalendar.getTime());
    }

    public OnOff getState() {
        return mState;
    }


    /**
     * Cancels an old alarm and sets again a new one
     */
    public void on() {
        // first trying to cancel
        off();

        // check if TIME less or more than the current time
        Calendar currentTimeCalendar = Calendar.getInstance();

        // if current time is later (or same), then one day must be added to the alarm
        if (currentTimeCalendar.get(Calendar.HOUR_OF_DAY) * 60 + currentTimeCalendar.get(Calendar.MINUTE)
                >= mCalendar.get(Calendar.HOUR_OF_DAY) * 60 + mCalendar.get(Calendar.MINUTE)) {
            mCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }


        // then resetting
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmMgr.setExact(
                    AlarmManager.RTC_WAKEUP,
                    mCalendar.getTimeInMillis(),
                    alarmIntent);
        } else {
            alarmMgr.set(
                    AlarmManager.RTC_WAKEUP,
                    mCalendar.getTimeInMillis(),
                    alarmIntent);
        }
        mState = OnOff.ON;
    }

    /**
     * Cancels current alarm
     */
    public void off() {
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }
        mState = OnOff.OFF;
    }

}
