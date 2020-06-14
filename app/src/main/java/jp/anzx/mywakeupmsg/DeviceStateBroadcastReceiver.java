package jp.anzx.mywakeupmsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class DeviceStateBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "DeviceStateBroadcastReceiver";
    private Context context;
    private Utils utils;

    public void start(Context context){

        this.context = context;

        IntentFilter filter = new IntentFilter();
        filter.addAction(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(this, filter);

        utils = new Utils(context, context.getString(R.string.preference_file_key));
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        NotifUtil notifUtil = new NotifUtil(context);

        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.i(TAG,"Screen went OFF");
            //Toast.makeText(context, "screen OFF",Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            Log.i(TAG,"Screen went ON");
            //Toast.makeText(context, "screen ON",Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED)){

            Log.i(TAG,"IDLE MODE CHANGED");

            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

            if (pm.isDeviceIdleMode()) {
                // the device is now in doze mode
                Log.i(TAG, "устройство входит в спящий режим");
            } else {
                // the device just woke up from doze mode

                String msgText = utils.getById(R.string.msg_text_key);

                //Calendar now = Calendar.getInstance();
                //now.set(1970 ,0 ,1);

                Date now = new Date();

                String msgTitle = Utils.formatTime(now);

                String startStr = utils.getById(R.string.time_wake_start_key);
                String endStr = utils.getById(R.string.time_wake_end_key);

                Date start = Utils.parseTime(startStr);
                Date end = Utils.parseTime(endStr);

                if(Utils.compareTimes(now, start) > 0 && Utils.compareTimes(now, end) < 0){
                    notifUtil.showNotification(msgTitle, msgText, 42);
                }
                else{
                    notifUtil.showNotification(msgTitle, "снова ты здесь?", 43);
                }
                Log.i(TAG, "устройство выходит из спящего режима");
                //Log.i(TAG, now.get(Calendar.HOUR_OF_DAY) + " " + start.get(Calendar.HOUR_OF_DAY) + " " + end.get(Calendar.HOUR_OF_DAY));
                //Log.i(TAG, now.get(Calendar.MINUTE) + " " + start.get(Calendar.MINUTE) + " " + end.get(Calendar.MINUTE));
                Log.i(TAG, now.getTime() + " ! " + start.getTime() + " ! " + end.getTime());
            }
        }

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
