package com.example.btl_android;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {

    final String CHANNEL_ID = "101";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("myAction")
                && intent.getStringExtra("Title") != null
                && intent.getStringExtra("Description") != null) {

            Log.e("Rev", "rev");

            NotificationManager manager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel1 = new NotificationChannel(
                        CHANNEL_ID,
                        "Channel 1",
                        NotificationManager.IMPORTANCE_HIGH
                );
                channel1.setDescription("This is channel 1");
                channel1.enableLights(true);
                channel1.setLightColor(Color.RED);
                channel1.enableVibration(true);
                channel1.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                                .build());

                if (manager != null) {
                    manager.createNotificationChannel(channel1);
                }
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(intent.getStringExtra("Title"))
                    .setContentText(intent.getStringExtra("Description"))
                    .setColor(Color.RED)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            if (manager != null) {
                manager.notify(12345, builder.build());
            }
        }
    }
}

