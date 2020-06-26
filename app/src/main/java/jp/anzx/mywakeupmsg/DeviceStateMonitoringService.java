package jp.anzx.mywakeupmsg;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DeviceStateMonitoringService extends Service {

    DeviceStateBroadcastReceiver staterec;

    public DeviceStateMonitoringService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //лепит пуш увед к сервесу
        NotifUtil notifUtil = new NotifUtil(getApplicationContext());
        Notification notification = notifUtil.createNotification("･｡･ﾟ★｡･*･　ﾟ☆ﾟ･", "MWUM is working");
        startForeground(1, notification);

        //
        staterec = new DeviceStateBroadcastReceiver();
        staterec.start(getApplicationContext());

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        staterec.stop();
    }
}
