package com.david.incubator.ui.home.warmer;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.ui.BaseNavigatorModel;
import com.david.common.util.Constant;
import com.david.incubator.util.ViewUtil;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/4 22:17
 * email: 10525677@qq.com
 * description:
 */

public class WarmerViewModel extends BaseNavigatorModel<WarmerHomeNavigator> {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware sensorConfig;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean spo2Visible = new ObservableBoolean(true);
    public ObservableField<String> manualValue = new ObservableField<>();
    public ObservableBoolean jaundice = new ObservableBoolean(false);

    private Observable.OnPropertyChangedCallback warmCallback;
    private Observable.OnPropertyChangedCallback ctrlModeCallback;
    private Observable.OnPropertyChangedCallback preWarmCallback;
    private Observable.OnPropertyChangedCallback manObjectiveCallback;

    @Inject
    public WarmerViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        warmCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                int warm = ((ObservableInt) observable).get();
                if (navigator != null) {
                    navigator.setHeatStep(warm);
                }
            }
        };

        manObjectiveCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                int manObjective = ((ObservableInt) observable).get();
                if (shareMemory.ctrlMode.get().equals(CtrlMode.Manual)) {
                    manualValue.set(String.valueOf(manObjective));
                }
            }
        };

        ctrlModeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                CtrlMode ctrlMode = ((ObservableField<CtrlMode>) observable).get();
                if (ctrlMode != null) {
                    if (ctrlMode.equals(CtrlMode.Skin)) {
                        shareMemory.cTime.removeOnPropertyChangedCallback(preWarmCallback);

                        if (navigator != null)
                            navigator.showSkin();
                    } else if (ctrlMode.equals(CtrlMode.Prewarm)) {
                        shareMemory.cTime.addOnPropertyChangedCallback(preWarmCallback);
                        shareMemory.cTime.notifyChange();

                        if (navigator != null)
                            navigator.showManual();
                    } else if (ctrlMode.equals(CtrlMode.Manual)) {
                        shareMemory.cTime.removeOnPropertyChangedCallback(preWarmCallback);
                        manualValue.set(String.valueOf(shareMemory.manObjective.get()));
                        if (navigator != null) {
                            navigator.showManual();
                        }
                    }
                }
            }
        };

        preWarmCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                int heatValue = ((ObservableInt) observable).get();
                String heatString = String.format(Locale.US, "%02d:%02d ", (heatValue / 60) % 60, heatValue % 60);
                manualValue.set(heatString);
            }
        };
    }

    @Override
    public void attach() {
        messageSender.getCtrlGet(shareMemory);
        messageSender.getSpo2Alert(shareMemory);
        messageSender.getPrAlert(shareMemory);

        setEnableSensor();

        shareMemory.ctrlMode.addOnPropertyChangedCallback(ctrlModeCallback);
        shareMemory.warm.addOnPropertyChangedCallback(warmCallback);
        shareMemory.manObjective.addOnPropertyChangedCallback(manObjectiveCallback);

        shareMemory.warm.notifyChange();
        shareMemory.ctrlMode.notifyChange();
        shareMemory.manObjective.notifyChange();

        if (Objects.equals(moduleHardware.getDeviceModel(), Constant.HKN93S)) {
            jaundice.set(true);
        }
    }

    @Override
    public void detach() {
        shareMemory.manObjective.removeOnPropertyChangedCallback(manObjectiveCallback);
        shareMemory.warm.removeOnPropertyChangedCallback(warmCallback);
        shareMemory.ctrlMode.removeOnPropertyChangedCallback(ctrlModeCallback);
    }

    @Override
    public void setNavigator(WarmerHomeNavigator warmerHomeNavigator) {
        super.setNavigator(warmerHomeNavigator);
        shareMemory.cTime.removeOnPropertyChangedCallback(preWarmCallback);
    }

    /*
     * 检测设备安装
     * */
    private void setEnableSensor() {
        if (moduleHardware != null && sensorConfig != null && navigator != null) {
            ViewUtil.displaySensor(moduleHardware.isSPO2(), sensorConfig.isSPO2(),
                    spo2Visible, navigator::spo2ShowBorder);
        }
    }
}