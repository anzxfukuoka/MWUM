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

    static SimpleDateFormat sdf;

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

        String formattedTime = formatTime(selectedHour, selectedMinute);

        //update view
        if(timeView != null){
            timeView.setText(formattedTime);
        }

        //save pref
        Context context = getContext();
        SharedPreferences sharedPref = context.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(pref_key, formattedTime);
        editor.apply();
    }

    public static String formatTime(int hours, int minutes) {
        sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);

        String strTime = sdf.format(cal.getTime());
        Log.i("♥", strTime);
        return strTime;
    }

    public static Calendar parseTime(String strTime){
        Calendar time = Calendar.getInstance();
        try {
            time.setTime(sdf.parse(strTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time; //.get(Calendar.HOUR_OF_DAY)
    }
}