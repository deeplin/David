package com.david.common.util;

import android.util.Log;

import com.apkfuns.log2file.LogFileEngineFactory;
import com.apkfuns.logutils.LogUtils;

/**
 * author: Ling Lin
 * created on: 2017/7/15 17:16
 * email: 10525677@qq.com
 * description:
 */

public class LogUtil {

    private static String TAG_INFO = "async";

    public static void EnableLog() {
        LogUtils.getLogConfig()
                .configAllowLog(Constant.ENABLE_DEBUG)
                .configTagPrefix(TAG_INFO)
                .configShowBorders(true)
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}");
    }

    public static void EnableLogToFile() {
        LogUtils.getLog2FileConfig().configLog2FileEnable(true)
                .configLog2FilePath("/sdcard/logs/")
                .configLog2FileNameFormat("%d{yyyyMMdd}.txt")
                .configLogFileEngine(new LogFileEngineFactory());
    }

    public static void i(Object object, String msg) {
        if (Constant.ENABLE_DEBUG) {
            String tag;
            if (TAG_INFO == null) {
                tag = object.getClass().getSimpleName();
            } else {
                tag = TAG_INFO;
            }
            Log.i(tag, msg);
        }
    }

    public static void w(Object object, String msg) {
        if (Constant.ENABLE_DEBUG) {
            String tag;
            if (TAG_INFO == null) {
                tag = object.getClass().getSimpleName();
            } else {
                tag = TAG_INFO;
            }
            Log.w(tag, msg);
        }
    }

    public static void LogFull(String tag, String msg) {
        if (tag == null || tag.length() == 0
                || msg == null || msg.length() == 0)
            return;

        int segmentSize = 3 * 1024;
        long length = msg.length();
        if (length <= segmentSize ) {// 长度小于等于限制直接打印
            Log.e(tag, msg);
        }else {
            while (msg.length() > segmentSize ) {// 循环分段打印日志
                String logContent = msg.substring(0, segmentSize );
                msg = msg.replace(logContent, "");
                Log.e(tag, logContent);
            }
            Log.e(tag, msg);// 打印剩余日志
        }
    }
}