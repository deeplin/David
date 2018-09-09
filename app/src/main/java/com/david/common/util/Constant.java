package com.david.common.util;

import android.graphics.Color;
import android.os.Environment;

/**
 * author: Ling Lin
 * created on: 2018/1/25 12:21
 * email: 10525677@qq.com
 * description:
 */

public class Constant {

    public static final boolean RELEASE_TO_DAVID = true;
    public static final boolean ENABLE_DEBUG = true;

    public static final int SCREEN_LOCK_SECOND = 30000; //second
    public static final int BUTTON_CLICK_TIMEOUT = 500; //millisecond
    public static final int LONG_CLICK_DELAY = 100; //millisecond

    public static final int SENSOR_SAVED_IN_DATABASE = 2592000; //3600*24*30 per month 存储一月，秒为单位

    public static final String SYSTEM_PASSWORD = "121212";
    public static final String USER_PASSWORD = "898989";

    public static final String HKN93S = "HKN-93S";
    public static final String YP2000S = "YP-2000S";
    public static final String YP970S = "YP-970S";

    public static final int AXIS_COLOR = Color.parseColor("#999999");
    public static int AXIS_X_DOT_PER_STEP = 10;

    public static final int SENSOR_NA_VALUE = -2;
    public static final byte NA_VALUE = -3;
    public static final String SENSOR_NA_STRING = "NA";
    public static final String SENSOR_DEFAULT_STRING = "--";
    public static final String SENSOR_TEMP_STRING = "--.-";
    public static final String SENSOR_SCALE_STRING = "----";
    public static final String SENSOR_PI_STRING = "----";

    public static final int TEMP_370 = 370;

    public static final int SERIAL_BUFFER_SIZE = 512;

    public static final String COM0 = "dev/ttyS0";
    public static final String COM1 = "dev/ttyS1";
    public static final String COM3 = "dev/ttyS3";
}