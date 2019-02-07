package com.pitman2e.mutespeaker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.pitman2e.mutespeaker.constant.NotificationID;

public class HeadsetStateService extends Service {
    private VolumeSettingContentObserver mVolumeSettingContentObserver;

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

        mVolumeSettingContentObserver = new VolumeSettingContentObserver(this, new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mVolumeSettingContentObserver);
        createEnableMuteSpeakerNotification();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        getApplicationContext().getContentResolver().unregisterContentObserver(mVolumeSettingContentObserver);
        super.onDestroy();
    }

    private void createEnableMuteSpeakerNotification() {
        Intent enableMuteIntent = new Intent(this, MuteServiceToggleBroadcastReceiver.class);
        enableMuteIntent.putExtra(MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE, false);
        PendingIntent enableMutePendingIntent = PendingIntent.getBroadcast(this, 0, enableMuteIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

        CharSequence titleText = this.getString(R.string.notification_mute_service_enabled);
        CharSequence actionButtonText = this.getString(R.string.notification_action_disable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationID.createNotificationChannel(this, NotificationID.NOTIFICATION_CHANNEL_MISC);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NotificationID.NOTIFICATION_CHANNEL_MISC)
                    .setSmallIcon(R.drawable.ic_volume_off_black_24dp)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentTitle(titleText)
                    .setOngoing(true)
                    .addAction(R.drawable.ic_volume_up_black_24dp, actionButtonText, enableMutePendingIntent);

            notificationBuilder.setOngoing(true);
            Notification notification = notificationBuilder.build();
            this.startForeground(NotificationID.MUTE_SERVICE_RUNNING, notification);
        } else {
            Notification.Builder notificationBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_volume_off_black_24dp)
                    .setPriority(Notification.PRIORITY_LOW)
                    .setContentTitle(titleText)
                    .setOngoing(true)
                    .addAction(R.drawable.ic_volume_up_black_24dp, actionButtonText, enableMutePendingIntent);

            notificationBuilder.setOngoing(true);
            Notification notification = notificationBuilder.build();
            this.startForeground(NotificationID.MUTE_SERVICE_RUNNING, notification);
        }
    }
}
