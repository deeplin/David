package com.david.incubator.mode;

/**
 * author: Ling Lin
 * created on: 2017/7/17 20:12
 * email: 10525677@qq.com
 * description:
 */

public enum CtrlMode {

    Standby("Standby", 0), Prewarm("Prewarm", 1), Skin("Skin", 2), Air("Air", 3), Manual("Manual", 4), None("None", 5), Error("Error", 6);

    private String name;
    private int index;

    CtrlMode(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static CtrlMode getMode(String ctrlModeString) throws Exception {
        for (CtrlMode mode : values()) {
            if (mode.getName().equals(ctrlModeString)) {
                return mode;
            }
        }
        throw new Exception("Illegal Ctrl mode: " + ctrlModeString);
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
