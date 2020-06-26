package jp.anzx.mywakeupmsg;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class Utils {

    private SharedPreferences sharedPref;
    private Context context;

    static SimpleDateFormat sdf;

    public Utils(Context context, String prefFileName){
        this.context = context;
        sharedPref = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    public void Save(String pref_key, String val){
        //save pref
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(pref_key, val);
        editor.apply();
    }

    public void Save(String pref_key, String[] val){
        //save pref
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(pref_key, new HashSet<>(Arrays.asList(val)));
        editor.apply();
    }

    public String getById(int key_id, String def){
        return sharedPref.getString(context.getString(key_id), def);
    }

    public String getById(int key_id){
        return getById(key_id, "");
    }

    public SharedPreferences getSharedPref() {
        return sharedPref;
    }



    public static String formatTime(int hours, int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hours);
        cal.set(Calendar.MINUTE, minutes);

        return formatTime(cal.getTime());
    }

    public static String formatTime(Date cal){

        sdf = new SimpleDateFormat("HH:mm");
        String strTime = sdf.format(cal);
        Log.i("♥", strTime);
        return strTime;
    }

    public static Date parseTime(String strTime){
        Date time = null;
        sdf = new SimpleDateFormat("HH:mm");
        try {
            time = sdf.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time; //.get(Calendar.HOUR_OF_DAY)
    }

    public static int compareTimes(Date d1, Date d2)
    {
        int t1;
        int t2;

        t1 = (int) (d1.getTime() % (24*60*60*1000L));
        t2 = (int) (d2.getTime() % (24*60*60*1000L));
        return (t1 - t2);
    }
}
