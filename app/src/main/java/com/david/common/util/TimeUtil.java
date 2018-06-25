package com.david.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/15 15:32
 * email: 10525677@qq.com
 * description:
 */

public class TimeUtil {
    public static final String HourMinute = "HH:mm";
    public static final String FullTime = "yy-MM-dd HH:mm:ss";
    public static final String DateTimeWithoutSecond = "yy-MM-dd HH:mm";
    public static final String Date = "yyyy-MM-dd";
    public static final String SimpleDate = "MM-dd";
    public static final String Time = "HH:mm:ss";

    public static String getCurrentDate(String format) {
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.US);
        return dateFormatter.format(date);
    }

    /*取得time的字符串*/
    public static String getTime(long time, String format) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format, Locale.US);
        return dateFormatter.format(time);
    }

    public static long getCurrentTimeInSecond() {
        return Math.round(System.currentTimeMillis() / 1000.0);
    }
}