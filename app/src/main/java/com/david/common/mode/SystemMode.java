package com.david.common.mode;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:27
 * email: 10525677@qq.com
 * description:
 */
public enum SystemMode {
    Init("Init", 0), Cabin("Cabin", 1), Warmer("Warmer", 2), Transit("Transit", 3), Error("Error", 4);

    private final String name;
    private final int index;

    SystemMode(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static SystemMode getMode(String systemModeString) throws Exception {
        for (SystemMode SystemMode : values()) {
            if (SystemMode.getName().equals(systemModeString)) {
                return SystemMode;
            }
        }
        throw new Exception("Illegal system mode: " + systemModeString);
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
