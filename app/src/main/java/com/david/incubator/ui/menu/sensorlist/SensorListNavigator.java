package com.david.incubator.ui.menu.sensorlist;

/**
 * author: Ling Lin
 * created on: 2017/12/31 17:59
 * email: 10525677@qq.com
 * description:
 */
public interface SensorListNavigator {
    void setBackground(boolean isCabin);

    void spo2ShowBorder(boolean status);

    void oxygenShowBorder(boolean status);

    void scaleShowBorder(boolean status);

    void humidityShowBorder(boolean status);

    void displayOxygenValue(String value);

    void displayHumidityValue(String value);

    void setTimingValue(String value);

    void displayTemp1Value(String value);

    void displayTemp1Objective(String value);

    void displayTemp2Value(String value);

    void displayTemp2Objective(String value);

    void displayAirAnimation(boolean isCabin);

    void displaySkinAnimation();

    void clearSkinAnimation();
}
