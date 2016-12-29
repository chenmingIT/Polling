package qukuai.com.polling;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;

public class PollingService extends Service {
    public PollingService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this,MainActivity.class) ;
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0) ;
        Notification notification = new Notification.Builder(PollingService.this)
                .setTicker("等待接单")
                .setContentTitle("趣快打车")
                .setContentText("等待接单中")
                .setSmallIcon(R.drawable.arrow)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .getNotification() ;
        startForeground(1,notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run(){
                int i = 0 ;
                while (i<10) {
                    try {
                        Thread.sleep(Timer.POLLINGTIMER * 1000);
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //完成订单
                NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE) ;
                Intent in = new Intent(PollingService.this,MainActivity.class) ;
                PendingIntent pi = PendingIntent.getActivity(PollingService.this,0,in,0) ;
                Notification notification = new Notification.Builder(PollingService.this)
                        .setTicker("司机已接单")
                        .setContentTitle("趣快打车")
                        .setContentText("司机已接单")
                        .setSmallIcon(R.drawable.arrow)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pi)
                        .getNotification() ;
                nm.notify(2,notification);
                stopSelf();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);

    }
}
