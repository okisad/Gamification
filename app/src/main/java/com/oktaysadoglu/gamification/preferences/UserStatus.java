package com.oktaysadoglu.gamification.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;


import com.oktaysadoglu.gamification.DAO.WordDAO;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by oktaysadoglu on 27/12/15.
 */


public class UserStatus {

    private static volatile UserStatus userStatus;

    private SharedPreferences sharedPreferences;

    public static final String SEEN_WORDS = "seen_words";

    public static final String KNOWN_WORDS = "known_words";

    public static final String NOT_KNOWN_WORDS = "not_known_words";

    public static final String LEVEL = "level";

    public static final String NOTIFICATION_OUT_BOUND_COUNT = "notification_out_bound_count";

    private static final String DATE = "date";

    public static UserStatus getInstance(Context context){

        if (userStatus == null){

            synchronized (UserStatus.class){

                if (userStatus == null){

                    userStatus = new UserStatus(context);

                }

            }

        }

        return userStatus;

    }

    private UserStatus(Context context) {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public int getSeenWords(){

        return sharedPreferences.getInt(SEEN_WORDS, 0);

    }

    public void setSeenWords(int seenWords){

        sharedPreferences.edit().putInt(SEEN_WORDS,seenWords).apply();

        setDate();

    }

    public int getNotificationOutBoundCount() {
        return sharedPreferences.getInt(NOTIFICATION_OUT_BOUND_COUNT,1);
    }

    public void setNotificationOutBoundCount(int count){

        sharedPreferences.edit().putInt(NOTIFICATION_OUT_BOUND_COUNT,count).apply();

    }

    public int getKnownWords(){

        return sharedPreferences.getInt(KNOWN_WORDS, 0);

    }

    public void setKnownWords(int knownWords){

        sharedPreferences.edit().putInt(KNOWN_WORDS,knownWords).apply();

        setDate();

    }

    public int getNotKnownWords(){

        return sharedPreferences.getInt(NOT_KNOWN_WORDS, 0);

    }

    public void setNotKnownWords(int notKnowWords){

        sharedPreferences.edit().putInt(NOT_KNOWN_WORDS,notKnowWords).apply();

        setDate();

    }

    public int getLevel(){

        return sharedPreferences.getInt(LEVEL, 1);

    }

    public void setLevel(int level){

        sharedPreferences.edit().putInt(LEVEL,level).apply();

        setDate();

    }

    private void setDate(){

        sharedPreferences.edit().putString(DATE,new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())).apply();

    }

    public void update(){

        int seenWords = getSeenWords();

        int knownWords = getKnownWords();

        int notNowWords = seenWords - knownWords;

        setNotKnownWords(notNowWords);

        if (knownWords == 0){

            setLevel(1);

        }else {

            int level = (knownWords+100)/100;

            setLevel(level);

        }

    }

    public class GetUserStatusFromDb extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }

    }


}
