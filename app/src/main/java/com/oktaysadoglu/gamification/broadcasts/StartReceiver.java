package com.oktaysadoglu.gamification.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.oktaysadoglu.gamification.services.DayStartService;
import com.oktaysadoglu.gamification.services.NotificationService;

/**
 * Created by oktaysadoglu on 26/12/15.
 */
public class StartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        DayStartService dayStartService = new DayStartService();

        NotificationService notificationService = new NotificationService();

        dayStartService.startService(context,true,10,0);

        notificationService.setService(context,true);

    }

}
