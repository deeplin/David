package com.lztek.toolkit;

import android.util.Log;

/**
 * 看门狗操作类.
 */
public final class WatchDog {
    static {
        java.io.File file = new java.io.File("/dev/watchdog");
        if (!file.exists() || !file.canRead() || !file.canWrite()) {
            SU.exec("chmod 666 /dev/watchdog");
            long times = 0;
            while ((!file.exists() || !file.canRead() || !file.canWrite()) && times < 2500) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
                times += 50;
            }
            if (!file.exists() || !file.canRead() || !file.canWrite()) {
                Log.e("#ERROR#", "!!! change watchdog mode failed !!!!");
            }
        }

        try {
            System.loadLibrary("watchdog");
        } catch (Throwable t) {
            Log.e("#ERROR#", "load library[libwatchdog.so] failed", t);
        }
    }

    private WatchDog() {
    }

    private static native boolean _enable();

    private static native boolean _disable();

    private static native boolean _feed();

    /**
     * 打开看门狗,使能看门狗的功能
     *
     * @param
     * @return {@code true} 成功, {@code false} 失败.
     */
    public static boolean enable() {
        try {
            return _enable();
        } catch (Throwable t) {
            Log.e("#ERROR#", "#WATCH_DOG# cannot find watchdog fuction[_enable]");
            return false;
        }
    }

    /**
     * 关闭看门狗，停止看门狗功能
     *
     * @param
     * @return {@code true} 成功, {@code false} 失败.
     */
    public static boolean disable() {
        try {
            return _disable();
        } catch (Throwable t) {
            Log.e("#ERROR#", "#WATCH_DOG# cannot find watchdog fuction[_disable]");
            return false;
        }
    }

    /**
     * 喂狗，使能看门狗功能后，需定时喂狗，若不喂狗超过一定时间后系统将自动重启
     *
     * @param
     * @return {@code true} 成功, {@code false} 失败.
     */
    public static boolean feed() {
        try {
            return _feed();
        } catch (Throwable t) {
            Log.e("#ERROR#", "#WATCH_DOG# cannot find watchdog fuction[_feed]");
            return false;
        }
    }
}
