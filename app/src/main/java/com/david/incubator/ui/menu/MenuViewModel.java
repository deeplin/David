package com.david.incubator.ui.menu;

import android.databinding.ObservableBoolean;

import com.david.common.data.ModuleHardware;
import com.david.common.data.ShareMemory;
import com.david.incubator.control.MainApplication;

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

    @Inject
    public ShareMemory shareMemory;
    @Inject
    public ModuleHardware moduleHardware;

    public ObservableBoolean menuChart = new ObservableBoolean(false);
    public ObservableBoolean menuSpo2 = new ObservableBoolean(false);
    public ObservableBoolean menuScale = new ObservableBoolean(false);
    public ObservableBoolean menuCamera = new ObservableBoolean(false);
    public ObservableBoolean menuSetting = new ObservableBoolean(false);

    public ObservableBoolean spo2Visible = new ObservableBoolean();
    public ObservableBoolean scaleVisible = new ObservableBoolean();
    public ObservableBoolean cameraVisible = new ObservableBoolean();

    @Inject
    public MenuViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    public void clearButtonBorder() {
        menuChart.set(false);
        menuSpo2.set(false);
        menuScale.set(false);
        menuCamera.set(false);
        menuSetting.set(false);
    }
}

