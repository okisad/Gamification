package com.oktaysadoglu.gamification.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;

import com.oktaysadoglu.gamification.DAO.BaseWordDAO;
import com.oktaysadoglu.gamification.DAO.WordDAO;
import com.oktaysadoglu.gamification.R;
import com.oktaysadoglu.gamification.model.BaseWord;
import com.oktaysadoglu.gamification.model.Word;
import com.oktaysadoglu.gamification.preferences.UserStatus;
import com.oktaysadoglu.gamification.tools.GetColorFilter;


/**
 * Created by oktaysadoglu on 25/12/15.
 */
public class GetResposeFromNotificationService extends IntentService {

    private Handler handler;

    public GetResposeFromNotificationService() {

        super("GetResposeFromNotificationService");

        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String answer = intent.getExtras().getString("answer");

        int baseWordId = intent.getExtras().getInt("baseWordId");

        final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        notificationManagerCompat.cancel(0);

        BaseWordDAO baseWordDAO = new BaseWordDAO(this);

        WordDAO wordDAO = new WordDAO(this);

        BaseWord baseWord = baseWordDAO.getSpecificBaseWord(baseWordId);

        Word word1 = wordDAO.getSpecificWordFromBaseWordId(baseWordId);

        if(word1 == null){

            word1 = new Word(baseWord.getId(),0);

            long id = wordDAO.addWord(word1);

            word1.setId((int) id);

            Log.e("my","id şu olarak eklendi : "+id);

        }

        int icon;

        int color;

        String truth;

        if(answer.equals(baseWord.getMean())){

            truth = "Doğru Cevap";

            icon = R.drawable.ic_tick;

            color = ContextCompat.getColor(this,R.color.notification_tick_color);

            if (word1.getNumberOfNotKnow() == -1 || word1.getNumberOfNotKnow() == 0){

                word1.setNumberOfNotKnow(0);

            }else if (word1.getNumberOfNotKnow() == -2){

                word1.setNumberOfNotKnow(-2);

            } else {

                word1.setNumberOfNotKnow(word1.getNumberOfNotKnow()-1);

                if (word1.getNumberOfNotKnow() == 0){

                    word1.setNumberOfNotKnow(-2);

                }

            }

            customNotification(truth,true,baseWord,color,icon);

        }else {

            truth = "Yanlış Cevap";

            icon = R.drawable.ic_cross;

            color = ContextCompat.getColor(this,R.color.notification_cross_color);

            if (word1.getNumberOfNotKnow() == -1){

                word1.setNumberOfNotKnow(2);

            }else if (word1.getNumberOfNotKnow() == -2){

                word1.setNumberOfNotKnow(-2);

            } else {

                word1.setNumberOfNotKnow(word1.getNumberOfNotKnow()+1);

            }

            customNotification(truth,false,baseWord,color,icon);

        }

        wordDAO.updateWord(word1);


        /*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);*/

        /*android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(this)
                .setTicker(truth)
                .setSmallIcon(icon)
                .setContentTitle(truth)
                .setContentText(baseWord.getWord() + " : "+baseWord.getMean())
                .setColor(color)
                .setAutoCancel(true);

        Notification notification = notificationBuilder.build();

        notification.priority = Notification.PRIORITY_HIGH;

        notificationManagerCompat.notify(10, notification);*/

        /*Log.e("my", "handler dışı");*/

        int seenWords = wordDAO.getSeenWordNumber();

        int knownWords = wordDAO.getKnownWordsNumber();

        UserStatus userStatus = UserStatus.getInstance(this);

        userStatus.setSeenWords(seenWords);

        userStatus.setKnownWords(knownWords);

        userStatus.setNotKnownWords(seenWords - knownWords);


    }

    public void customNotification(String rightness,boolean answer,BaseWord baseWord,int color,int smallIcon){

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.get_response_notification_layout);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(smallIcon)
                .setTicker(rightness)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0))
                .setContent(remoteViews)
                .setColor(color);

        remoteViews.setTextViewText(R.id.get_response_notification_layout_title, baseWord.getWord());

        remoteViews.setTextColor(R.id.get_response_notification_layout_title, ContextCompat.getColor(this, R.color.black));

        remoteViews.setTextViewText(R.id.get_response_notification_layout_text, baseWord.getMean());

        remoteViews.setTextColor(R.id.get_response_notification_layout_text, ContextCompat.getColor(this, R.color.black));

        remoteViews.setImageViewResource(R.id.get_response_notification_layout_image, smallIcon);

        if (answer){

            remoteViews.setInt(R.id.get_response_notification_layout_image,"setBackgroundResource",R.drawable.circle_green);

        }else {

            remoteViews.setInt(R.id.get_response_notification_layout_image,"setBackgroundResource",R.drawable.circle_red);

        }

        Intent intent = new Intent(this,AgainSendNotificationService.class);

        PendingIntent pendingIntent = PendingIntent.getService(this,9,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        remoteViews.setOnClickPendingIntent(R.id.get_response_notification_layout_button, pendingIntent);

        final NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);

        Notification notification = notificationBuilder.build();

        notification.priority = Notification.PRIORITY_HIGH;

        notificationManagerCompat.notify(10, notification);

        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("my", "handler içi");

                notificationManagerCompat.cancel(10);
            }
        }, 4000);
*/
    }
}
