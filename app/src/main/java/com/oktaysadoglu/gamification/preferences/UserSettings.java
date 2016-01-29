package com.oktaysadoglu.gamification.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.oktaysadoglu.gamification.model.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by oktaysadoglu on 27/12/15.
 */


public class UserSettings {

    private static volatile UserSettings userSettings;

    private SharedPreferences sharedPreferences;

    public static final String REPEAT = "repeat";

    public static final String TEMP_REPEAT = "temp_repeat";

    public static final String INTERVAL = "interval";

    public static final String TEMP_INTERVAL = "temp_interval";

    public static final String NOTIFICATION_IS_ON = "notification_is_on";

    public static final String INSTANT_NOTIFICATION_IS_ON = "instant_notification_is_on";

    public static final String WEEK_OF_DAY = "week_of_day";

    public static final String DATE = "date";

    public static UserSettings getInstance(Context context){

        if (userSettings == null){

            synchronized (UserSettings.class){

                if (userSettings == null){

                    userSettings = new UserSettings(context);

                }

            }

        }

        return userSettings;

    }

    private UserSettings(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setRepeat(int repeat){

        sharedPreferences.edit().putInt(REPEAT,repeat).apply();

    }

    public int getRepeat(){

        return sharedPreferences.getInt(REPEAT, 0);

    }

    public void setTempRepeat(int tempRepeat){

        sharedPreferences.edit().putInt(TEMP_REPEAT,tempRepeat).apply();

    }

    public int getTempRepeat(){

        return sharedPreferences.getInt(TEMP_REPEAT, 0);

    }

    public void setInterval(int interval){

        sharedPreferences.edit().putInt(INTERVAL,interval).apply();

    }

    public int getInterval(){

        return  sharedPreferences.getInt(INTERVAL,-1);

    }

    public void setTempInterval(int tempInterval){

        sharedPreferences.edit().putInt(TEMP_INTERVAL,tempInterval).apply();
    }

    public int getTempInterval(){

        return sharedPreferences.getInt(TEMP_INTERVAL,-1);

    }

    public void setNotificationIsOn(boolean isOn){

        sharedPreferences.edit().putBoolean(NOTIFICATION_IS_ON,isOn).apply();

    }

    public boolean getNotificationIsOn(){

        return sharedPreferences.getBoolean(NOTIFICATION_IS_ON, false);

    }

    public void setInstantNotificationIsOn(boolean isOn){

        sharedPreferences.edit().putBoolean(INSTANT_NOTIFICATION_IS_ON,isOn).apply();

    }

    public boolean getInstantNotificationIsOn(){

        return sharedPreferences.getBoolean(INSTANT_NOTIFICATION_IS_ON, false);

    }

    public void setWeekOfDay( int weekOfDay){

        sharedPreferences.edit().putInt(WEEK_OF_DAY,weekOfDay).apply();

    }

    public int getWeekOfDay(){

        return sharedPreferences.getInt(WEEK_OF_DAY, Calendar.getInstance().get(Calendar.DAY_OF_WEEK));

    }

    private void setDate(){

        sharedPreferences.edit().putString(DATE, new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(Calendar.getInstance().getTime())).apply();

    }

    private String getDate(){

        return sharedPreferences.getString(DATE,"");

    }


    public String toString() {
        return "repeat : "+getRepeat()+", temp repeat : "+getTempRepeat()+", interval : "+getInterval()+", temp interval : "+getTempInterval()+", isOn : "+getNotificationIsOn()+ ", tempIsOn : "+getInstantNotificationIsOn()+", weekofday : "+getWeekOfDay();
    }
}
