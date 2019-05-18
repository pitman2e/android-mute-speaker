package com.pitman2e.mutespeaker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.pitman2e.mutespeaker.constant.NotificationID;

public class HeadsetStateService extends Service {
    private VolumeSettingContentObserver mVolumeSettingContentObserver;

    public static void startService(Context context) {
        Intent in = new Intent(context, HeadsetStateService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(in);
        } else {
            context.startService(in);
        }
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
        PendingIntent disableMutePendingIntent = PendingIntent.getBroadcast(this, 0, enableMuteIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

        Intent disableMuteIntentWithVolume = new Intent(this, MuteServiceToggleBroadcastReceiver.class)
                .putExtra(MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE, false)
                .putExtra(MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE_WITH_VOLUME, true);
        PendingIntent disableMuteWithVolumePendingIntent = PendingIntent.getBroadcast(this, 1, disableMuteIntentWithVolume, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);
        //Note that if the ReuqestCode is the same, android will consider these intent is the same.

        CharSequence titleText = this.getString(R.string.notification_mute_service_enabled);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationID.createNotificationChannel(this, NotificationID.NOTIFICATION_CHANNEL_MISC);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NotificationID.NOTIFICATION_CHANNEL_MISC)
                    .setSmallIcon(R.drawable.ic_volume_off_black_24dp)
                    .setPriority(NotificationCompat.PRIORITY_LOW)
                    .setContentTitle(titleText)
                    .setOngoing(true)
                    .addAction(R.drawable.ic_volume_up_black_24dp, getString(R.string.notification_action_disable), disableMutePendingIntent)
                    .addAction(R.drawable.ic_volume_up_black_24dp, getString(R.string.notification_action_disable_with_volume), disableMuteWithVolumePendingIntent)
                    ;

            notificationBuilder.setOngoing(true);
            Notification notification = notificationBuilder.build();
            this.startForeground(NotificationID.MUTE_SERVICE_RUNNING, notification);
        } else {
            Notification.Builder notificationBuilder = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_volume_off_black_24dp)
                    .setPriority(Notification.PRIORITY_LOW)
                    .setContentTitle(titleText)
                    .setOngoing(true)
                    .addAction(R.drawable.ic_volume_up_black_24dp, getString(R.string.notification_action_disable), disableMutePendingIntent)
                    .addAction(R.drawable.ic_volume_up_black_24dp, getString(R.string.notification_action_disable_with_volume), disableMuteWithVolumePendingIntent)
                    ;

            notificationBuilder.setOngoing(true);
            Notification notification = notificationBuilder.build();
            this.startForeground(NotificationID.MUTE_SERVICE_RUNNING, notification);
        }
    }
}
