package com.pitman2e.mutespeaker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.os.Handler;

import com.pitman2e.mutespeaker.constant.NotificationID;

public class HeadsetStateService extends Service {
    public static void startService(Context context) {
        Intent in = new Intent(context, HeadsetStateService.class);
        context.startService(in);
    }

    public static void stopService(Context context) {
        Intent in = new Intent(context, HeadsetStateService.class);
        context.stopService(in);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        HeadsetPlugIntentReceiver.registerIntent(this);

        VolumeSettingContentObserver volumeSettingContentObserver = new VolumeSettingContentObserver(this, new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, volumeSettingContentObserver);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NotificationID.NOTIFICATION_CHANNEL_MISC, this.getString(R.string.notification_channel_miscellaneous), NotificationManager.IMPORTANCE_NONE);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NotificationID.NOTIFICATION_CHANNEL_MISC);
            Notification notification = notificationBuilder.setOngoing(true).build();
            startForeground(NotificationID.MUTE_SERVICE_RUNNING, notification);
        }
        else {
            Notification notification = new Notification.Builder(this).build();
            startForeground(NotificationID.MUTE_SERVICE_RUNNING, notification);
        }

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {


        super.onDestroy();
    }
}
