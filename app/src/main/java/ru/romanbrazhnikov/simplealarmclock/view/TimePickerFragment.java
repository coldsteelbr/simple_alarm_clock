package ru.romanbrazhnikov.simplealarmclock.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ru.romanbrazhnikov.simplealarmclock.R;

/**
 * Created by roman on 29.09.17.
 */

public class TimePickerFragment extends DialogFragment {
    // todo: inject format
    private DateFormat mTimeFormat = new SimpleDateFormat("HH:mm");
    private TimePicker mTimePicker;
    private String mFormattedTime;

    public static TimePickerFragment getInstance(String formattedTime) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.mFormattedTime = formattedTime;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(mTimeFormat.parse(mFormattedTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);


        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = view.findViewById(R.id.dialog_time_time_picker);
        mTimePicker.setIs24HourView(android.text.format.DateFormat.is24HourFormat(getActivity()));
        mTimePicker.setCurrentHour(hours);
        mTimePicker.setCurrentMinute(minutes);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Choose time")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int hour = mTimePicker.getCurrentHour();
                        int minute = mTimePicker.getCurrentMinute();

                        TimePickerDialog.OnTimeSetListener timeReceiver
                                = (TimePickerDialog.OnTimeSetListener) getActivity();
                        timeReceiver.onTimeSet(mTimePicker, hour, minute);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
