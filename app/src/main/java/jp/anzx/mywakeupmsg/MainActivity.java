package jp.anzx.mywakeupmsg;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Intent service;

    NotifUtil notifUtil;

    Button btnStart,
            btnStop,
            btnChangeWakeStart,
            btnChangeWakeEnd;

    TextView wakeTimeStart,
            wakeTimeEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //
        btnStart = findViewById(R.id.button_start);
        btnStop = findViewById(R.id.button_stop);

        btnChangeWakeStart = findViewById(R.id.button_wake_start_change);
        btnChangeWakeEnd = findViewById(R.id.button_wake_end_change);

        wakeTimeStart = findViewById(R.id.text_time_wake_start);
        wakeTimeEnd = findViewById(R.id.text_time_wake_end);

        //
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnChangeWakeEnd.setOnClickListener(this);
        btnChangeWakeStart.setOnClickListener(this);

        //
        notifUtil = new NotifUtil(this);
        service = new Intent(getApplicationContext(), DeviceStateMonitoringService.class);

        //

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "эта кнопка ничего не делает", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                //notifUtil.showNotification("♥", "##########", 0);
            }
        });

        LoadPrefs();
    }

    void LoadPrefs(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String wakeTimeStartStr = sharedPref.getString(getString(R.string.time_wake_start_key), "09:00");
        String wakeTimeEndStr = sharedPref.getString(getString(R.string.time_wake_end_key), "13:00");

        wakeTimeStart.setText(wakeTimeStartStr);
        wakeTimeEnd.setText(wakeTimeEndStr);
    }

    @Override
    public void onClick(View view) {
        TimePickerFragment timePicker = new TimePickerFragment();

        switch (view.getId()){
            case R.id.button_start:
                startService(service);

                break;
            case R.id.button_stop:
                stopService(service);

                break;
            case R.id.button_wake_end_change:
                timePicker.setTimeView(wakeTimeEnd);
                timePicker.setPref_key(getString(R.string.time_wake_end_key));

                timePicker.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.button_wake_start_change:
                timePicker.setTimeView(wakeTimeStart);
                timePicker.setPref_key(getString(R.string.time_wake_start_key));

                timePicker.show(getSupportFragmentManager(), "timePicker");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
