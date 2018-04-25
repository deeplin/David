package com.lztek.toolkit;

/**
 * 屏幕开关工具类.
 */

public final class Screen {
    private Screen() {
    }

    /**
     * 开启屏幕
     */
    public static void on() {
        SU.exec("echo 0 > /sys/class/graphics/fb0/blank");
    }

    /**
     * 关闭屏幕
     */
    public static void off() {
        SU.exec("echo 1 > /sys/class/graphics/fb0/blank");
    }
}
