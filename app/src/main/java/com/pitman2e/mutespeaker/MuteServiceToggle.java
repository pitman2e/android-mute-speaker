package com.pitman2e.mutespeaker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.pitman2e.mutespeaker.constant.NotificationID;

public class MuteServiceToggle {
    public static void EnforceByPref(Context context) {
        if (Prefs.getIsEnableNotification(context)) {
            MuteServiceToggle.setEnable(context);
        } else {
            MuteServiceToggle.setDisable(context);
        }
    }

    private static void setEnable(Context context) {
        Prefs.setIsEnableNotification(context, true);
        HeadsetStateService.startService(context);
    }

    private static void setDisable(Context context) {
        Prefs.setIsEnableNotification(context, false);
        HeadsetStateService.stopService(context);
        createDisableMuteSpeakerNotification(context);
    }

    private static void createDisableMuteSpeakerNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent enableMuteIntent = new Intent(context, MuteServiceToggleBroadcastReceiver.class);
        enableMuteIntent.putExtra(MuteServiceToggleBroadcastReceiver.EXTRA_IS_ENABLED_MUTE_SERVICE, true);
        PendingIntent enableMutePendingIntent = PendingIntent.getBroadcast(context, 0, enableMuteIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);

        CharSequence actionButtonText = context.getString(R.string.notification_action_enable);
        CharSequence titleText = context.getString(R.string.notification_mute_service_disabled);

        NotificationID.createNotificationChannel(context, NotificationID.NOTIFICATION_CHANNEL_MISC);

        boolean isPrefPersistDisabledNotification =
                PreferenceManager.getDefaultSharedPreferences(context).getBoolean(context.getString(R.string.PREFERENCES_ID_DISABLE_NOTIFICATION_PERSIST), false);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NotificationID.NOTIFICATION_CHANNEL_MISC)
                .setSmallIcon(R.drawable.ic_volume_up_black_24dp)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentTitle(titleText)
                .setOngoing(isPrefPersistDisabledNotification)
                .addAction(R.drawable.ic_volume_off_black_24dp, actionButtonText, enableMutePendingIntent);

        Notification notification = notificationBuilder.build();
        notificationManager.notify(NotificationID.MUTE_SERVICE_RUNNING, notification);
    }
}
