package jp.anzx.mywakeupmsg;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimePickerFragment extends DialogFragment
        implements TimePickerDialog.OnTimeSetListener {

    public int selectedHour;
    public int selectedMinute;

    public TextView timeView;
    public String pref_key;

    Utils utils;

    public void setTimeView(TextView timeView) {
        this.timeView = timeView;
    }

    public void setPref_key(String pref_key) {
        this.pref_key = pref_key;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        selectedHour = hourOfDay;
        selectedMinute = minute;

        String formattedTime = Utils.formatTime(selectedHour, selectedMinute);

        //update view
        if(timeView != null){
            timeView.setText(formattedTime);
        }

        utils = new Utils(getContext(), getString(R.string.preference_file_key));
        utils.Save(pref_key, formattedTime);

    }
}