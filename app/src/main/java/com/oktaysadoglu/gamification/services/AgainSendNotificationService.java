package com.oktaysadoglu.gamification.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by oktaysadoglu on 17/01/16.
 */
public class AgainSendNotificationService extends IntentService {

    public AgainSendNotificationService() {
        super("AgainSendNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        notificationManagerCompat.cancel(10);

        NotificationService notificationService = new NotificationService();

        notificationService.sendNotification(this);

    }
}
