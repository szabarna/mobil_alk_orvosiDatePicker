package com.example.orvosidatapicker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHandler {

    private static final String CHANNEL_ID = "notification_channel";
    private final int NOTIFICATION_ID = 0;
    private NotificationManager mnotificationManager;
    private Context mContext;

    public NotificationHandler(Context context) {
            this.mContext = context;
            this.mnotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            createChannel();
    }

    private void createChannel() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)   return;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "DatePicker Notification", NotificationManager.IMPORTANCE_DEFAULT);

        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(Color.BLUE);
        channel.setDescription("Notification from OrvosiDatePicker");
        this.mnotificationManager.createNotificationChannel(channel);
    }

    public void send(String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                .setContentTitle("Date Picker Application")
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_date);

        this.mnotificationManager.notify(NOTIFICATION_ID, builder.build());

    }

}
