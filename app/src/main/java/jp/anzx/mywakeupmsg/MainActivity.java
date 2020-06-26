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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Intent service;

    NotifUtil notifUtil;
    Utils utils;

    Button btnStart,
            btnStop,
            btnChangeWakeStart,
            btnChangeWakeEnd,
            btnSaveText;

    TextView wakeTimeStart,
            wakeTimeEnd;

    ViewFlipper vf;

    EditText editText;

    //хуита
    EditText editTextMon;
    EditText editTextTue;
    EditText editTextWed;
    EditText editTextThu;
    EditText editTextFri;
    EditText editTextSat;
    EditText editTextSun;

    TextView logView;

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

        btnSaveText = findViewById(R.id.button_save_text);

        vf = findViewById(R.id.vf);

        editText = findViewById(R.id.edit_text);

        //хуита
        editTextMon = findViewById(R.id.edit_text_mon);
        editTextTue = findViewById(R.id.edit_text_tue);
        editTextWed = findViewById(R.id.edit_text_wed);
        editTextThu = findViewById(R.id.edit_text_thu);
        editTextFri = findViewById(R.id.edit_text_fri);
        editTextSat = findViewById(R.id.edit_text_sat);
        editTextSun = findViewById(R.id.edit_text_sun);

        logView = findViewById(R.id.log_text);

        //
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnChangeWakeEnd.setOnClickListener(this);
        btnChangeWakeStart.setOnClickListener(this);
        btnSaveText.setOnClickListener(this);

        //
        notifUtil = new NotifUtil(this);
        service = new Intent(getApplicationContext(), DeviceStateMonitoringService.class);

        utils = new Utils(this, getString(R.string.preference_file_key));
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
        SharedPreferences sharedPref = utils.getSharedPref();

        String wakeTimeStartStr = sharedPref.getString(getString(R.string.time_wake_start_key), "09:00");
        String wakeTimeEndStr = sharedPref.getString(getString(R.string.time_wake_end_key), "13:00");

        String msgText = sharedPref.getString(getString(R.string.msg_text_key), "");

        //хуита
        String msgTextMon = sharedPref.getString(getString(R.string.msg_text_mon_key), "");
        String msgTextTue = sharedPref.getString(getString(R.string.msg_text_tue_key), "");
        String msgTextWed = sharedPref.getString(getString(R.string.msg_text_wed_key), "");
        String msgTextThu = sharedPref.getString(getString(R.string.msg_text_thu_key), "");
        String msgTextFri = sharedPref.getString(getString(R.string.msg_text_fri_key), "");
        String msgTextSat = sharedPref.getString(getString(R.string.msg_text_sat_key), "");
        String msgTextSun = sharedPref.getString(getString(R.string.msg_text_sun_key), "");

        String logText = sharedPref.getString(getString(R.string.log_text_key), "");

        String mode = sharedPref.getString(getString(R.string.mode_key), Reminder.SIMPLE_MODE);

        wakeTimeStart.setText(wakeTimeStartStr);
        wakeTimeEnd.setText(wakeTimeEndStr);

        editText.setText(msgText);

        //хуита
        editTextMon.setText(msgTextMon);
        editTextTue.setText(msgTextTue);
        editTextWed.setText(msgTextWed);
        editTextThu.setText(msgTextThu);
        editTextFri.setText(msgTextFri);
        editTextSat.setText(msgTextSat);
        editTextSun.setText(msgTextSun);

        logView.setText(logText);

        switch (mode){
            case Reminder.SIMPLE_MODE:
                vf.setDisplayedChild(0);
                break;
            case Reminder.ONCE_MODE:
                vf.setDisplayedChild(0);
                break;
            case Reminder.WEEKLY_MODE:
                vf.setDisplayedChild(1);
                break;
        }

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
                timePicker.setPref_key(getString(R.string.time_wake_end_key)); //и это тоже

                timePicker.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.button_wake_start_change:
                timePicker.setTimeView(wakeTimeStart);
                timePicker.setPref_key(getString(R.string.time_wake_start_key));

                timePicker.show(getSupportFragmentManager(), "timePicker");
                break;
            case R.id.button_save_text:
                String msgText = editText.getText().toString();

                utils.Save(getString(R.string.msg_text_key), msgText);

                //хуита
                utils.Save(getString(R.string.msg_text_mon_key), editTextMon.getText().toString());
                utils.Save(getString(R.string.msg_text_tue_key), editTextTue.getText().toString());
                utils.Save(getString(R.string.msg_text_wed_key), editTextWed.getText().toString());
                utils.Save(getString(R.string.msg_text_thu_key), editTextThu.getText().toString());
                utils.Save(getString(R.string.msg_text_fri_key), editTextFri.getText().toString());
                utils.Save(getString(R.string.msg_text_sat_key), editTextSat.getText().toString());
                utils.Save(getString(R.string.msg_text_sun_key), editTextSun.getText().toString());

                Toast.makeText(this, "сохранено", Toast.LENGTH_SHORT).show();
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
        if (id == R.id.action_simple) {
            Toast.makeText(this, "simple", Toast.LENGTH_SHORT).show();
            vf.setDisplayedChild(0);
            utils.Save(getString(R.string.mode_key), Reminder.SIMPLE_MODE);
            return true;
        }
        if (id == R.id.action_once) {
            Toast.makeText(this, "once", Toast.LENGTH_SHORT).show();
            vf.setDisplayedChild(0);
            utils.Save(getString(R.string.mode_key), Reminder.ONCE_MODE);
            return true;
        }
        if (id == R.id.action_weekly) {
            Toast.makeText(this, "weekly", Toast.LENGTH_SHORT).show();
            vf.setDisplayedChild(1);
            utils.Save(getString(R.string.mode_key), Reminder.WEEKLY_MODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
