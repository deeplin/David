package com.david.common.mode;

/**
 * author: Ling Lin
 * created on: 2017/12/27 20:59
 * email: 10525677@qq.com
 * description:
 */
public enum FunctionMode {

    Humidity("HUM", 11), Oxygen("O2", 12), Spo2("SPO2", 13), Pr("PR", 14), Timing("TIMING", 21), Scale("SCALE", 22);

    private String name;
    private int index;

    FunctionMode(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
