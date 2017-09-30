package ru.romanbrazhnikov.simplealarmclock.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.romanbrazhnikov.simplealarmclock.R;

public class AlarmActivity extends AppCompatActivity {

    private Intent mAlarmIntent;
    private Button mStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        mStop = findViewById(R.id.b_stop);
        mStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                PendingIntent pi = PendingIntent.getBroadcast(AlarmActivity.this, 0, mAlarmIntent, 0);
                am.cancel(pi);
                finish();
            }
        });
        mAlarmIntent = getIntent().getParcelableExtra("INTENT");
    }
}
