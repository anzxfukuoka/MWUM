package jp.anzx.mywakeupmsg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class DeviceStateBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "DeviceStateBroadcastReceiver";
    private Context context;

    public void start(Context context){

        this.context = context;

        IntentFilter filter = new IntentFilter();
        filter.addAction(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(this, filter);
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
                notifUtil.showNotification("!", "добро пожаловать, снова.", 42);
                Log.i(TAG, "устройство выходит из спящего режима");
            }
        }

        /* test adb commands
        * adb shell dumpsys deviceidle enable
        * adb shell dumpsys deviceidle force-idle
        * adb shell dumpsys deviceidle unforce
        * adb shell dumpsys battery reset
        * */

        //Intent in = new Intent(context, DeviceStateMonitoringService.class);
        //context.startService(in);

        //Toast.makeText(context, log, Toast.LENGTH_LONG).show();

        //NotifUtil notifUtil = new NotifUtil(context);
        //notifUtil.showNotification("Action: " + intent.getAction(), "URI: " + intent.toUri(Intent.URI_INTENT_SCHEME), 42);
    }

    public void stop(){
        context.unregisterReceiver(this);
    }
}
