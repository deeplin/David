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

        if (activity == null || designWidth < 1 || designHeight < 1)
            return;

        Display display = activity.getWindowManager().getDefaultDisplay();
//        int width = display.getWidth();
//        int height = display.getHeight();
//
//        if (hasStatusBar) {
//            height -= getStatusBarHeight(act);
//        }
//
//        AutoUtil.displayWidth = width;
//        AutoUtil.displayHeight = height;

        Point point = new Point();
        display.getRealSize(point);

        double displayHeight = point.y;

        AutoUtil.heightRadio = displayHeight / designHeight;
    }

    public static int getHeight(int height) {
        return (int) (height * AutoUtil.heightRadio);
    }
}
