package com.david.common.mode;

import com.david.R;
import com.david.common.util.ResourceUtil;

/**
 * author: Ling Lin
 * created on: 2017/7/17 20:12
 * email: 10525677@qq.com
 * description:
 */

public enum CtrlMode {

    Skin("Skin", 0), Air("Air", 1), Standby("Standby", 2), None("None", 3), Error("Error", 4),
    Manual("Manual", 11), Prewarm("Prewarm", 12);

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

    @Override
    public String toString() {
        switch (index) {
            case (0):
                return ResourceUtil.getString(R.string.skin);
            case (1):
                return ResourceUtil.getString(R.string.air);
            case (11):
                return ResourceUtil.getString(R.string.manual);
            case (12):
                return ResourceUtil.getString(R.string.prewarm);
            default:
                return name;
        }
    }
}