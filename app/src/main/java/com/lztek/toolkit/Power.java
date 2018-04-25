package com.lztek.toolkit;

/**
 * 开关机工具类.
 */

public final class Power {
    private Power() {
    }

    /**
     * 重启
     */
    public static void reboot() {
        SU.exec(new String[]{
                "echo +15 > /sys/class/rtc/rtc0/wakealarm",
                "reboot -p",
        });
    }

    /**
     * 设置下一次开机时间(目前只精确到分钟)
     *
     * @param minutes 下一次开机时间距离当时间差(单位: 分钟)，必须大于零
     */
    public static void nextPowerOn(int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Invalid parametes");
        }
        SU.exec("echo +" + (minutes * 60) + " > /sys/class/rtc/rtc0/wakealarm");
    }

    /**
     * 关机
     *
     * @param seconds 下一次开机时间距离当时间差(单位: 分钟)，必须大于零
     */
    public static void shutdown(int minutes) {
        if (minutes <= 0) {
            throw new IllegalArgumentException("Invalid parametes");
        }
        SU.exec(new String[]{
                "echo +" + (minutes * 60) + " > /sys/class/rtc/rtc0/wakealarm",
                "reboot -p",
        });
    }
}
