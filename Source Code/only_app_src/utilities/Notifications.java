package com.labdesignpattern.sensorstracking.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.labdesignpattern.sensorstracking.R;

public class Notifications {
    protected NotificationManager mNotificationManager;
    private Context mainContext;
    private int nid;

    public Notifications(Context mainContext, NotificationManager mNotificationManager){
        if(mNotificationManager == null)
            throw new  NullPointerException("Notification Manager must be exists");
        this.mNotificationManager = mNotificationManager;
        this.mainContext = mainContext;
        nid = 0;
    }

    public String pushNotification(Intent ii, String text, String title, boolean removable){
        String notifyID = "notifysensors_" + nid;
        nid++;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mainContext, notifyID);
        PendingIntent pendingIntent = PendingIntent.getActivity(mainContext, 0, ii, 0);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_baseline_track_changes_24);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(text);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setOngoing(!removable);

        // === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    notifyID,
                    mainContext.getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(notifyID);
        }
        mNotificationManager.notify(0, mBuilder.build());
        return notifyID;
    }

    public void popNotification(String notifyID){
        mNotificationManager.deleteNotificationChannel(notifyID);
    }
}
