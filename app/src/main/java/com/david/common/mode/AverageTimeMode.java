package com.david.common.mode;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:28
 * email: 10525677@qq.com
 * description:
 */
public enum AverageTimeMode {

    None("", -1), Zero("2-4", 0), One("4-6", 1), Two("8", 2), Three("10", 3), Four("12", 4), Five("14", 5), Six("16", 6);

    private final String name;
    private final int index;

    AverageTimeMode(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static AverageTimeMode getMode(int index) throws Exception {
        for (AverageTimeMode mode : values()) {
            if (mode.getIndex() == index) {
                return mode;
            }
        }
        throw new Exception("Illegal average cTime index: " + index);
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
