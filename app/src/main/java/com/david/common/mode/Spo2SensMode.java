package com.david.common.mode;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:27
 * email: 10525677@qq.com
 * description:
 */
public enum Spo2SensMode {
    Normal("NORM", "Normal"), APOD("APOD", "APOD"), MAX("MAX", "Max");

    private final String name;
    private final String display;

    Spo2SensMode(String name, String display) {
        this.name = name;
        this.display = display;
    }

    public static Spo2SensMode getMode(String modeString) throws Exception {
        for (Spo2SensMode mode : values()) {
            if (mode.getName().equals(modeString)) {
                return mode;
            }
        }
        throw new Exception("Illegal spo2 sens mode: " + modeString);
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }
}