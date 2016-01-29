package com.oktaysadoglu.gamification.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.oktaysadoglu.gamification.DAO.BaseWordDAO;
import com.oktaysadoglu.gamification.DAO.WordDAO;
import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.activities.MainActivity;
import com.oktaysadoglu.gamification.model.BaseWord;
import com.oktaysadoglu.gamification.model.Word;
import com.oktaysadoglu.gamification.preferences.UserSettings;
import com.oktaysadoglu.gamification.preferences.UserStatus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by oktaysadoglu on 25/12/15.
 */
public class NotificationService extends IntentService {

    private static final String TAG = "notification_service";

    public static final short START_HOUR = 10;

    public static final short STOP_HOUR = 22;

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        UserSettings userSettings = UserSettings.getInstance(this);

        if (userSettings.getNotificationIsOn()){

            Log.e(TAG,"notification isteği açık");

            if (hourIsInInterval()){

                Log.e(TAG,"Notification için uygun zaman aralığı");

                if(userSettings.getWeekOfDay() != Calendar.getInstance().get(Calendar.DAY_OF_WEEK)){

                    Log.e(TAG, "Günün ilk notificationı");

                    resetTempValues(userSettings);

                    reduceTempRepeat(userSettings,1);

                }else {

                    Log.e(TAG,"Günün bir sonraki notificationı");

                    if(userSettings.getTempInterval() > 1){

                        reduceTempInterval(userSettings,1);

                    }else {

                        reduceTempRepeat(userSettings,1);

                    }

                }

            }else {

                Log.e(TAG,"notification için yanlış zaman aralığı");

                setService(this, false);

                resetTempValues(userSettings);

                Calendar calendar = Calendar.getInstance();

                int now = calendar.get(Calendar.HOUR_OF_DAY);

                if (now >= 0 && now <START_HOUR){

                    userSettings.setWeekOfDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK)-1);

                }

            }

        }else {

            Log.e(TAG,"notification isteği kapalı");

            setService(this,false);

        }

    }

    private void resetTempValues(UserSettings userSettings){

        userSettings.setTempRepeat(userSettings.getRepeat());

        userSettings.setTempInterval(userSettings.getInterval());

        userSettings.setWeekOfDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

        Log.e(TAG,"UserSetting değerleri resetlendi");

    }

    private void reduceTempRepeat(UserSettings userSettings,int value){

        int tempRepeat = userSettings.getTempRepeat();

        if(tempRepeat >= value && tempRepeat != 0){

            tempRepeat = tempRepeat - value;

            Log.e(TAG, "repeat " + value + " kadar azaltıldı");

            if (tempRepeat == 0){

                Calendar calendar = Calendar.getInstance();

                userSettings.setWeekOfDay(calendar.get(Calendar.DAY_OF_WEEK));

                setService(this, false);

                Log.e(TAG,"Günlük gönderilecek notification sayısı tamamlandı");

            }

            sendNotification(this);

            userSettings.setTempRepeat(tempRepeat);

            userSettings.setTempInterval(userSettings.getInterval());

        }else {

            Log.e(TAG, "Gösterilecek notification kalmamış, service kapanıyor");


            setService(this, false);
        }

    }

    private void reduceTempInterval(UserSettings userSettings,int value){

        int interval = userSettings.getTempInterval();

        if(interval >= value && interval != 0){

            interval = interval - value;

            userSettings.setTempInterval(interval);

            Log.e(TAG, "interval " + value + " kadar azaltıldı");

        }



    }

    public void sendNotification(Context context){

        Intent intent12 = new Intent(context, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getService(context, 3, intent12, 0);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_layout);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setTicker("ticker")
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("Başlık")
                .setContentText("Expandable")
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        Word word = getProperWord(context);

        if(word == null){

            return;

        }

        Log.e("my",word.toString());

        prepareOfNotification(remoteViews, notificationBuilder, context, word.getId());

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);

        Notification notification = notificationBuilder.build();

        notification.bigContentView = remoteViews;

        notification.priority = Notification.PRIORITY_HIGH;

        notificationManagerCompat.notify(0, notification);

        Log.e(TAG, "notification yollandi");


    }

    private void prepareOfNotification(RemoteViews views, NotificationCompat.Builder notificationBuilder, Context context, int id){

        BaseWordDAO baseWordDAO = new BaseWordDAO(context);

        BaseWord baseWord = baseWordDAO.getSpecificBaseWord(id);

        notificationBuilder
                .setTicker(baseWord.getWord())
                .setContentTitle(baseWord.getWord())
                .setContentText("Cevaplamak için genişletin")
                .setSmallIcon(R.drawable.ic_tick)
                .setColor(ContextCompat.getColor(context,R.color.notification_question_color));

        List<BaseWord> mBaseWordsForOptions = baseWordDAO.getWordOptionsForTest(baseWord);

        List<Integer> sequenceOptions = new ArrayList<>();

        for(int i = 0;i<4;i++){

            sequenceOptions.add(i);

        }

        Collections.shuffle(sequenceOptions);

        views.setTextViewText(R.id.notification_layout_word_name,baseWord.getWord());

        views.setTextViewText(R.id.first_answer, mBaseWordsForOptions.get(sequenceOptions.get(0)).getMean());
        views.setTextViewText(R.id.second_answer , mBaseWordsForOptions.get(sequenceOptions.get(1)).getMean());
        views.setTextViewText(R.id.third_answer, mBaseWordsForOptions.get(sequenceOptions.get(2)).getMean());
        views.setTextViewText(R.id.fourth_answer , mBaseWordsForOptions.get(sequenceOptions.get(3)).getMean());

        String correctAnswer = baseWord.getMean();

        Intent intent1 = new Intent(context,GetResposeFromNotificationService.class);
        intent1.putExtra("answer", mBaseWordsForOptions.get(sequenceOptions.get(0)).getMean());
        intent1.putExtra("correctAnswer", correctAnswer);
        intent1.putExtra("word",baseWord.getWord());
        intent1.putExtra("baseWordId",baseWord.getId());
        PendingIntent pendingIntent1 =PendingIntent.getService(context,5,intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.first_answer, pendingIntent1);

        Intent intent2= new Intent(context,GetResposeFromNotificationService.class);
        intent2.putExtra("answer", mBaseWordsForOptions.get(sequenceOptions.get(1)).getMean());
        intent2.putExtra("correctAnswer", correctAnswer);
        intent2.putExtra("word",baseWord.getWord());
        intent2.putExtra("baseWordId",baseWord.getId());
        PendingIntent pendingIntent2 =PendingIntent.getService(context, 6, intent2, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.second_answer, pendingIntent2);

        Intent intent3 = new Intent(context,GetResposeFromNotificationService.class);
        intent3.putExtra("answer", mBaseWordsForOptions.get(sequenceOptions.get(2)).getMean());
        intent3.putExtra("correctAnswer", correctAnswer);
        intent3.putExtra("word",baseWord.getWord());
        intent3.putExtra("baseWordId",baseWord.getId());
        PendingIntent pendingIntent3 =PendingIntent.getService(context,7,intent3,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.third_answer, pendingIntent3);

        Intent intent4 = new Intent(context,GetResposeFromNotificationService.class);
        intent4.putExtra("answer", mBaseWordsForOptions.get(sequenceOptions.get(3)).getMean());
        intent4.putExtra("correctAnswer", correctAnswer);
        intent4.putExtra("word",baseWord.getWord());
        intent4.putExtra("baseWordId",baseWord.getId());
        PendingIntent pendingIntent4 =PendingIntent.getService(context,8,intent4,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.fourth_answer,pendingIntent4);

    }

    //Test etmelisin
    public Word getProperWord(Context context){

        WordDAO wordDAO = new WordDAO(this);

        UserStatus userStatus = UserStatus.getInstance(context);

        int count = userStatus.getNotificationOutBoundCount();

        Word word = null;

        List<Word> notKnownWordsList = wordDAO.getNotKnownWords();

        if(notKnownWordsList.size() > 0){

            if(count != 10){

                word = notKnownWordsList.get(new Random().nextInt(notKnownWordsList.size()));

                userStatus.setNotificationOutBoundCount(++count);

                Log.e("my",String.valueOf(userStatus.getNotificationOutBoundCount()));

            }else {

                word = wordDAO.getNotSeenWordForNotification();

                userStatus.setNotificationOutBoundCount(1);

                Log.e("my", String.valueOf(userStatus.getNotificationOutBoundCount()));

            }

        }else {

            word = wordDAO.getNotSeenWordForNotification();

            if(word == null){

                List<Word> wordList = wordDAO.getSpecialNotKnownWords();

                if (wordList.size() > 0){

                    word = wordList.get(new Random().nextInt(wordList.size()));

                }else {

                    word = wordDAO.getSpecificWord((new Random().nextInt(4998)) + 1);

                }

            }

            Log.e("my",String.valueOf(userStatus.getNotificationOutBoundCount()));

        }

        return word;

    }

    public void setService(Context context,boolean isOn){

        Intent intent = getIntent(context);

        PendingIntent pendingIntent;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        UserSettings userSettings = UserSettings.getInstance(context);

        if (isOn){

            if (isServiceAlarmOn(context)){

                Log.e(TAG, "Service zaten açık");

            }else {

                pendingIntent = PendingIntent.getService(context, 0, intent, 0);

                alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), AlarmManager.INTERVAL_HOUR, pendingIntent);

                Log.e(TAG, "Service açıldı");

            }

            userSettings.setNotificationIsOn(true);

            userSettings.setInstantNotificationIsOn(true);

            Log.e(TAG,userSettings.toString());

        }else {

            if (isServiceAlarmOn(context)){

                pendingIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_NO_CREATE);

                alarmManager.cancel(pendingIntent);

                pendingIntent.cancel();

                Log.e(TAG, "Service kapatıldı");

            }else {

                Log.e(TAG, "Service zaten kapalı");

            }

            userSettings.setInstantNotificationIsOn(false);

            Log.e(TAG, userSettings.toString());
        }

    }

    private boolean hourIsInInterval() {

        Calendar calendar = Calendar.getInstance();

        int now = calendar.get(Calendar.HOUR_OF_DAY);

        int stop = STOP_HOUR;

        int start = START_HOUR;

        if (start <= now && now < stop) {

            return true;

        } else {

            return false;

        }


    }

    private Intent getIntent(Context context){

        Intent intent = new Intent(context,NotificationService.class);

        return intent;

    }

    public boolean isServiceAlarmOn(Context context){

        Intent intent = getIntent(context);

        PendingIntent pendingIntent = PendingIntent.getService(context,0,intent,PendingIntent.FLAG_NO_CREATE);

        return pendingIntent!=null;

    }
}
