package com.baziiz.gametimer2;

import android.graphics.Color;

/**
 * Created by samn on 5/3/15.
 */
public class ColorUtility {

    public static int getBrightness(int red, int green, int blue) {
        //Color brightness is determined by the following formula:
        //((Red value X 299) + (Green value X 587) + (Blue value X 114)) / 1000
        return Math.round(((red * 299) + (green * 587) + (blue * 114)) / 1000);
    }

    public static int getBrightness(int color) {
        return getBrightness(Color.red(color), Color.green(color), Color.blue(color));
    }

    public static boolean isDark(int color) {
        if (getBrightness(color) < 128) return true;
        return false;
    }
}
