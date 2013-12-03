package com.ao.rememberus;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by joe on 24/11/13.
 */
public class ReminderBroadCastReceiver extends BroadcastReceiver {


    public void	onReceive(Context context,	Intent intent)	{
                                System.out.println("BroadcastReceiver: onRecive()");


        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Get taskId & message
        int taskId = intent.getIntExtra("taskId", 0);
        String notificationText = intent.getStringExtra("taskMessage");

        // Delete values from intent
        //intent.removeExtra("taskId");
        //intent.removeExtra("taskMessage");

                                System.out.println("taskId="+taskId+"notificationText="+notificationText);

        Intent myIntent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, 0);
                                System.out.println(notificationText);

        Notification notification = new Notification(R.drawable.ic_launcher, "New scheduled task", System.currentTimeMillis());
        notification.setLatestEventInfo( context,"RememberUs", notificationText, pendingIntent);
                                //notification.flags = Notification.FLAG
        notificationManager.notify(null, taskId, notification); //0 is id
                                //notificationManager.cancel(0);
    }
}
