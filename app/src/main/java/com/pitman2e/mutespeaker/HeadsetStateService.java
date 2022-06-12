package com.pitman2e.mutespeaker;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.pitman2e.mutespeaker.constant.NotificationID;

public class HeadsetStateService extends Service {
    private VolumeSettingContentObserver mVolumeSettingContentObserver;
    private BroadcastReceiver receiver;

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
        registerIntent();

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
        unregisterIntent();
        getApplicationContext().getContentResolver().unregisterContentObserver(mVolumeSettingContentObserver);
        super.onDestroy();
    }

    private void createEnableMuteSpeakerNotification() {
        Intent enableMuteIntent = new Intent(this, MuteServiceToggleBroadcastReceiver.class);
        enableMuteIntent.putExtra(MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE, false);
        PendingIntent disableMutePendingIntent =
                PendingIntent.getBroadcast(this, 0, enableMuteIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent disableMuteIntentWithVolume = new Intent(this, MuteServiceToggleBroadcastReceiver.class)
                .putExtra(MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE, false)
                .putExtra(MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE_WITH_VOLUME, true);
        PendingIntent disableMuteWithVolumePendingIntent =
                PendingIntent.getBroadcast(this, 1, disableMuteIntentWithVolume, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        //Note that if the Request Code is the same, android will consider these intent is the same.

        CharSequence titleText = this.getString(R.string.notification_mute_service_enabled);
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
    }

    public void registerIntent() {
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        receiver = new HeadsetPlugIntentReceiver();
        this.registerReceiver(receiver, receiverFilter);
    }

    public void unregisterIntent() {
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }
    }
}
