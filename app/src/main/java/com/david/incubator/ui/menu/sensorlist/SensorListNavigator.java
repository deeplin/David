package com.david.incubator.ui.menu.sensorlist;

import com.david.common.mode.CtrlMode;
import com.david.common.mode.SystemMode;

/**
 * author: Ling Lin
 * created on: 2017/12/31 17:59
 * email: 10525677@qq.com
 * description:
 */
public interface SensorListNavigator {
    void setSystemMode(boolean isCabin, int humidityObjective, int oxygenObjective, String timingMode);

    void setCtrlMode(SystemMode systemMode, CtrlMode ctrlMode, int airObjective, int skinObjective, int manObjective);

    void spo2ShowBorder(boolean status);

    void showHumidity(boolean isHardware, boolean isSoftware);

    void showOxygen(boolean isHardware, boolean isSoftware);

    void displayForthValue(String value);

    void displayThirdValue(String value);

    void displayFirstValue(String value);

    void displaySecondValue(String value);
}
