package ru.romanbrazhnikov.simplealarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.romanbrazhnikov.simplealarmclock.view.AlarmActivity;

/**
 * Created by roman on 29.09.17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentToStart = new Intent(context, AlarmActivity.class);
        // FIXME: can't normally close the activity
        intentToStart.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK
                //Intent.FLAG_ACTIVITY_CLEAR_TOP
        );
        intentToStart.putExtra("INTENT", intent);
        context.startActivity(intentToStart);
    }

}
