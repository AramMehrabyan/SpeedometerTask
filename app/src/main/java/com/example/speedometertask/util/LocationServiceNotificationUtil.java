package com.example.speedometertask.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.speedometertask.R;
import com.example.speedometertask.di.service.ServiceScope;

import javax.inject.Inject;

@ServiceScope
public class LocationServiceNotificationUtil {

    @Inject
    LocationServiceNotificationUtil() {
    }

    public void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    Constants.CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }

    public Notification createNotification(Context context, PendingIntent pendingIntent, PendingIntent closePendingIntent,
                                           String speed, String maxSpeed, String distance) {

        return new NotificationCompat.Builder(context, Constants.CHANNEL_ID)
                .setSmallIcon(R.drawable.map_icon)
                .setContentTitle("Speedometer")
                .setContentText("Speed")
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Speed:  " + speed)
                        .addLine("Max Speed:  " + maxSpeed)
                        .addLine("Distance:  " + distance))
                .setContentIntent(pendingIntent)
                .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop",
                        closePendingIntent)
                .build();
    }
}
