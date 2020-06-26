package jp.anzx.mywakeupmsg;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;

import java.util.Date;

public class Reminder {

    private final static String TAG = "Reminder";

    private final int delay = 10 * 1000; //10 sec

    private Utils utils;
    private String lastAction = "";
    private NotifUtil notifUtil;


    Reminder(Context context){
        utils = new Utils(context, context.getString(R.string.preference_file_key));
        notifUtil = new NotifUtil(context);
    }

    void onScreenOn(){
        Log.i(TAG, "ScreenOn");

        if(lastAction.equals(PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED)){
            if(isWakeTime()){
                show();
            }
        }

        lastAction = Intent.ACTION_SCREEN_ON;
    }

    void onScreenOff(){
        Log.i(TAG, "onScreenOff");

        lastAction = Intent.ACTION_SCREEN_OFF;
    }

    void onIDLEMode(){
        Log.i(TAG, "IDLEMode");

        lastAction = PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED;
    }

    void onActiveMode(){
        Log.i(TAG, "ActiveMode");

        if(isWakeTime()){
            show();
        }

        lastAction = PowerManager.ACTION_DEVICE_IDLE_MODE_CHANGED;
    }

    void show(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after
                String msgText = getMsgText();
                String msgTitle = "♂";
                notifUtil.showNotification(msgTitle, msgText, 42);
            }
        }, delay);
    }

    String getMsgText(){
        String msgText = utils.getById(R.string.msg_text_key);
        return msgText;
    }

    boolean isWakeTime(){

        Date now = new Date();

        String startStr = utils.getById(R.string.time_wake_start_key);
        String endStr = utils.getById(R.string.time_wake_end_key);

        Date start = Utils.parseTime(startStr);
        Date end = Utils.parseTime(endStr);

        if(Utils.compareTimes(now, start) > 0 && Utils.compareTimes(now, end) < 0){
            return true;
        }
        return false;
    }
}
