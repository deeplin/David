package com.david.incubator.ui.home.cabin;

/**
 * author: Ling Lin
 * created on: 2017/12/27 20:54
 * email: 10525677@qq.com
 * description:
 */
public interface HomeNavigator {
    void setHeatStep(int step);

    void spo2ShowBorder(boolean status);

    void oxygenShowBorder(boolean status);

    void humidityShowBorder(boolean status);

    void showAir();

    void showSkin();
}
