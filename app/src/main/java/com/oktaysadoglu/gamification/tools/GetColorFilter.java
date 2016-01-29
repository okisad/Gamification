package com.oktaysadoglu.gamification.tools;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;

/**
 * Created by oktaysadoglu on 17/01/16.
 */
public class GetColorFilter {

    public static ColorFilter getColorFilter(String color){

        int iColor = Color.parseColor(color);

        int red   = (iColor & 0xFF0000) / 0xFFFF;
        int green = (iColor & 0xFF00) / 0xFF;
        int blue  = iColor & 0xFF;

        float[] matrix = { 0, 0, 0, 0, red,
                0, 0, 0, 0, green,
                0, 0, 0, 0, blue,
                0, 0, 0, 1, 0 };

        ColorFilter colorFilter = new ColorMatrixColorFilter(matrix);

        return colorFilter;

    }

}
