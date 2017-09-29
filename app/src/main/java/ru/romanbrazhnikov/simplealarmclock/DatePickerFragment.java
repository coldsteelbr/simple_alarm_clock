package ru.romanbrazhnikov.simplealarmclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by roman on 29.09.17.
 */

public class DatePickerFragment extends DialogFragment {

    private DatePicker mDatePicker;
    private Date mInitDate;

    public static DatePickerFragment getInstance(Date date) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.mInitDate = date;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mInitDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        mDatePicker = view.findViewById(R.id.dialog_date_date_picker);
        mDatePicker.init(year, month, day, null);


        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle("Choose date")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int year = mDatePicker.getYear();
                        int month = mDatePicker.getMonth();
                        int day = mDatePicker.getDayOfMonth();

                        SelectedDateInterface dateReceiver = (SelectedDateInterface)getActivity();
                        dateReceiver.onDateSet(year, month, day);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .create();
    }
}
