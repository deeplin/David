package com.david.common.utils;

import android.graphics.Color;

/**
 * author: Ling Lin
 * created on: 2018/1/25 12:21
 * email: 10525677@qq.com
 * description:
 */

public class Constant {

    public static final boolean RELEASE_TO_DAVID = false;
    public static final boolean ENABLE_DEBUG = true;

    public static final int SCREEN_LOCK_SECOND = 30; //second
    public static final int BUTTON_CLICK_TIMEOUT = 500; //millisecond
    public static final int LONG_CLICK_DELAY = 100; //millisecond

    public static final int SENSOR_SAVED_IN_DATABASE = 86400; //43200 per month 存储二月
    public static final int SCALE_SAVED_IN_DATABASE = 1440; //720 per month 存储二月

    public static final String SYSTEM_PASSWORD = "121212";
    public static final String USER_PASSWORD = "898989";

    public static final String HKN93S = "HKN-93S";

    public static final int AXIS_COLOR = Color.parseColor("#999999");
    public static final int AXIS_X_DOT_PER_STEP = 10;

    public static final int SENSOR_NA_VALUE = -2;
    public static final String SENSOR_NA_STRING = "NA";
    public static final String SENSOR_DEFAULT_STRING = "--";
    public static final String SENSOR_TEMP_STRING = "--.-";
    public static final String SENSOR_SCALE_STRING = "----";
    public static final String SENSOR_PI_STRING = "----";

    public static final int TEMP_370 = 370;

    public static final int SERIAL_BUFFER_SIZE = 10240;
    public static final String COM3 = "dev/ttyS3";
    public static final String COM4 = "dev/ttyS4";

}
