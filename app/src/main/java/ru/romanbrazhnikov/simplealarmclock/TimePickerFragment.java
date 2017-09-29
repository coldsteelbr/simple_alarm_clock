package ru.romanbrazhnikov.simplealarmclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by roman on 29.09.17.
 */

public class TimePickerFragment extends DialogFragment {

    private TimePicker mTimePicker;
    private Calendar mInitTimeCalendar;

    public static TimePickerFragment getInstance(Calendar timeCalendar) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.mInitTimeCalendar = timeCalendar;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int hours = mInitTimeCalendar.get(Calendar.HOUR_OF_DAY);
        int minutes = mInitTimeCalendar.get(Calendar.MINUTE);


        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = view.findViewById(R.id.dialog_time_time_picker);
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

                        SelectedTimeInterface timeReceiver = (SelectedTimeInterface) getActivity();
                        timeReceiver.onTimeSet(hour, minute);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
