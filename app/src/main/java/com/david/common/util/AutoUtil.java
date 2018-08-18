package com.david.common.util;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;

/**
 * author: Ling Lin
 * created on: 2018/1/25 12:21
 * email: 10525677@qq.com
 * description:
 */

public class AutoUtil {

    public static double heightRadio;

    public static void setSize(Activity activity, int designWidth, int designHeight) {

        Display display = activity.getWindowManager().getDefaultDisplay();

        Point point = new Point();
        display.getRealSize(point);

        double displayHeight = point.y;

        AutoUtil.heightRadio = displayHeight / designHeight;
    }

    public static int getHeight(int height) {
        return (int) (height * AutoUtil.heightRadio);
    }
}
