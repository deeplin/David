package com.david.incubator.ui.menu;

import android.databinding.ObservableBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/26 19:13
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class MenuViewModel {

    public ObservableBoolean menuChart = new ObservableBoolean(false);
    public ObservableBoolean menuSpo2 = new ObservableBoolean(false);
    public ObservableBoolean menuScale = new ObservableBoolean(false);
    public ObservableBoolean menuCamera = new ObservableBoolean(false);
    public ObservableBoolean menuSetting = new ObservableBoolean(false);
    public ObservableBoolean lockScreen = new ObservableBoolean(false);

    public ObservableBoolean cameraVisible = new ObservableBoolean();

    @Inject
    public MenuViewModel() {
    }

    public void clearButtonBorder() {
        menuChart.set(false);
        menuSpo2.set(false);
        menuScale.set(false);
        menuCamera.set(false);
        menuSetting.set(false);
    }

    public void setScreenLock(boolean status) {
        lockScreen.set(status);
    }
}

