package com.oktaysadoglu.gamification.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by oktaysadoglu on 28/12/15.
 */
public class DayStartService extends IntentService {

    private static final String TAG = "DayStartService";

    public DayStartService() {
        super("DayStartService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        NotificationService notificationService = new NotificationService();

        notificationService.setService(this,true);

    }

    public void startService(Context context,boolean isOn,int startHour,int startMinute){

        Intent intent = getIntent(context);

        PendingIntent pendingIntent;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        if(isOn){

            if (!isOnService(context)){

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(new Date());

                int nowHour = calendar.get(Calendar.HOUR_OF_DAY);

                if(nowHour < 24 && nowHour >= 10){

                    calendar.add(Calendar.DAY_OF_YEAR,1);

                }

                calendar.set(Calendar.HOUR_OF_DAY,startHour);

                calendar.set(Calendar.MINUTE,startMinute);

                pendingIntent = PendingIntent.getService(context,1,intent,0);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

                Log.e(TAG, "service Başlatıldı");

            }else {

                Log.e(TAG,"service zaten açık");

            }

        }else{

            if (isOnService(context)){

                pendingIntent = PendingIntent.getService(context,1,intent,0);

                alarmManager.cancel(pendingIntent);

                pendingIntent.cancel();

                Log.e(TAG, "service kapatıldı");

            }else {

                Log.e(TAG,"service zaten kapalı");

            }

        }

    }

    private boolean isOnService(Context context){

        Intent intent = getIntent(context);

        PendingIntent pendingIntent = PendingIntent.getService(context,1,intent,PendingIntent.FLAG_NO_CREATE);

        return pendingIntent!=null;

    }

    private Intent getIntent(Context context){

        Intent intent = new Intent(context,DayStartService.class);

        return intent;

    }
}
