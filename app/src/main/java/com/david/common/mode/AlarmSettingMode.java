package com.david.common.mode;

/**
 * author: Ling Lin
 * created on: 2017/12/26 16:29
 * email: 10525677@qq.com
 * description:
 */
public enum AlarmSettingMode {
    TEMP_DEVH("TEMP.DEVH", 0), TEMP_DEVL("TEMP.DEVL", 1),

    O2_DEVH("O2.DEVH", 2), O2_DEVL("O2.DEVL", 3),

    HUM_DEVH("HUM.DEVH", 4), HUM_DEVL("HUM.DEVL", 5),

    AIR_OVH("AIR.OVH", 6), FLOW_OVH("FLOW.OVH", 7),

    SKIN_OVH("SKIN.OVH", 8), SYS_FAN("SYS.FAN", 9),

    SPO2_OVH("SPO2.OVH", 10), SPO2_OVL("SPO2.OVL", 11),

    PR_OVH("PR.OVH", 12), PR_OVL("PR.OVL", 13);

    private final String name;
    private final int index;

    AlarmSettingMode(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public static AlarmSettingMode getMode(String alertModeString) throws Exception {
        for (AlarmSettingMode mode : values()) {
            if (mode.getName().equals(alertModeString)) {
                return mode;
            }
        }
        throw new Exception("Illegal alertString mode: " + alertModeString);
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
