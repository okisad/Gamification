package com.oktaysadoglu.gamification.tools;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.oktaysadoglu.gamification.R;

/**
 * Created by oktaysadoglu on 15/01/16.
 */
public class CustomToasts {

    public static void correctAnswerToast(Activity activity){

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View layout = layoutInflater.inflate(R.layout.correct_answer_toast, (ViewGroup) activity.findViewById(R.id.green_toast_layout));

        Toast toast = new Toast(activity);

        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,500);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

    public static void falseAnswerToast(Activity activity){

        LayoutInflater layoutInflater = activity.getLayoutInflater();

        View layout = layoutInflater.inflate(R.layout.false_answer_toast, (ViewGroup) activity.findViewById(R.id.red_toast_layout));

        Toast toast = new Toast(activity);

        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,500);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();



    }

}
