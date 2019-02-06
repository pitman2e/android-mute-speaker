package com.pitman2e.mutespeaker.constant;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.pitman2e.mutespeaker.R;

public class NotificationID {
    public static final String NOTIFICATION_CHANNEL_MISC = "NOTIFICATION_CHANNEL_MISC";
    public static final int MUTE_SERVICE_RUNNING = 1;

    public static void createNotificationChannel(Context context, String notificationId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (notificationManager.getNotificationChannel(notificationId) != null) {
                return;
            }

            switch (notificationId) {
                case NOTIFICATION_CHANNEL_MISC:
                    NotificationChannel notificationChannel = new NotificationChannel(NotificationID.NOTIFICATION_CHANNEL_MISC, context.getString(R.string.notification_channel_miscellaneous), NotificationManager.IMPORTANCE_NONE);
                    notificationChannel.setLightColor(Color.BLUE);
                    notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                    notificationManager.createNotificationChannel(notificationChannel);
                    break;
            }
        }
    }
}
