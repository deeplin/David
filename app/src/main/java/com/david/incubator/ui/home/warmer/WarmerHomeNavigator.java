package com.david.incubator.ui.home.warmer;

/**
 * author: Ling Lin
 * created on: 2018/1/4 22:17
 * email: 10525677@qq.com
 * description:
 */
public interface WarmerHomeNavigator {

    void setHeatStep(int step);

    void spo2ShowBorder(boolean status);

    void showSkin();

    void showManual();
}