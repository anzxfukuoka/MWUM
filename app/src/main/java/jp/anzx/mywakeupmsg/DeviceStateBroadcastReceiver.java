package jp.anzx.mywakeupmsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class DeviceStateBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "DeviceStateBroadcastReceiver";
    private Context context;
    private Utils utils;

    private String lastEvent = "";

    public void start(Context context){

        this.context = context;

        IntentFilter filter = new IntentFilter();
        filter.addAction(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(this, filter);

        utils = new Utils(context, context.getString(R.string.preference_file_key));
    }

    void saveLog(String ev){
        Date now = new Date();
        //save test log
        utils.Save(context.getString(R.string.log_text_key),
                utils.getById(R.string.log_text_key, "") +
                        "[" + Utils.formatTime(now) + "]" + ev + "\n");
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Reminder reminder = new Reminder(context);


        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i(TAG,"Screen went OFF");
            //saveLog("SCREEN OFF");

            reminder.onScreenOn();

        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i(TAG,"Screen went ON");
            //saveLog("SCREEN ON");

            reminder.onScreenOff();

        } else if (intent.getAction().equals(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED)){

            Log.i(TAG,"IDLE MODE CHANGED");

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            if (pm.isDeviceIdleMode()) {
                // the device is now in doze mode
                Log.i(TAG, "устройство входит в спящий режим");
                //saveLog("IDLE MODE");

                reminder.onIDLEMode();

            } else {
                // the device just woke up from doze mode
                //saveLog("ACTIVE MODE");

                reminder.onActiveMode();

                Log.i(TAG, "устройство выходит из спящего режима");
            }
        }

        lastEvent = intent.getAction();

        /* test adb commands
        * adb shell dumpsys deviceidle enable
        * adb shell dumpsys deviceidle force-idle
        * adb shell dumpsys deviceidle unforce
        * adb shell dumpsys battery reset
        * */
    }

    public void stop(){
        context.unregisterReceiver(this);
    }
}
