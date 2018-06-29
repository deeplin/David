package com.david.incubator.util;

import com.apkfuns.logutils.LogUtils;
import com.lztek.toolkit.SU;

public class GPIOUtil {

    public static void init() {
        SU.exec("cat /sys/debuggpio/debug_tp1");
    }

    public static boolean read() {
        java.io.FileInputStream stream = null;
        try {
            stream = new java.io.FileInputStream("/sys/debuggpio/debug_tp1");
            return '0' == stream.read() ? false : true;
        } catch (Exception e) {
            LogUtils.e(e);
            return false;
        }
    }
}