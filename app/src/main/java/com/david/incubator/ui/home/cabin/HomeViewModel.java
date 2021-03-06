package com.david.incubator.ui.home.cabin;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.david.common.control.MessageSender;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.ui.BaseNavigatorModel;
import com.david.incubator.control.MainApplication;
import com.david.common.ui.ViewUtil;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/27 20:54
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class HomeViewModel extends BaseNavigatorModel<HomeNavigator> {

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;
    @Inject
    public ShareMemory shareMemory;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean humidityVisible = new ObservableBoolean(true);
    public ObservableBoolean oxygenVisible = new ObservableBoolean(true);
    public ObservableBoolean spo2Visible = new ObservableBoolean(true);
    public ObservableField<String> incPower = new ObservableField<>();
    public ObservableBoolean jaundice = new ObservableBoolean(false);

    private Observable.OnPropertyChangedCallback incPowerCallback;
    private Observable.OnPropertyChangedCallback humidityPowerCallback;
    private Observable.OnPropertyChangedCallback oxygenPowerCallback;
    private Observable.OnPropertyChangedCallback settingUpdateCallback;
    private Observable.OnPropertyChangedCallback ctrlModeCallback;

    @Inject
    public HomeViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        incPowerCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                navigator.setHeatStep(shareMemory.incPower.get());
            }
        };

        humidityPowerCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                navigator.setHumidityPower(shareMemory.humidityPower.get());
            }
        };

        oxygenPowerCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                navigator.setOxygenPower(shareMemory.oxygenPower.get());
            }
        };

        settingUpdateCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setEnabledSensor();
            }
        };

        ctrlModeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                CtrlMode ctrlMode = shareMemory.ctrlMode.get();
                if (navigator != null) {
                    if (Objects.equals(ctrlMode, CtrlMode.Air)) {
                        navigator.showAir();
                    } else if (Objects.equals(ctrlMode, CtrlMode.Skin)) {
                        navigator.showSkin();
                    }
                }
            }
        };
    }

    @Override
    public void attach() {
        messageSender.getSpo2Alert(shareMemory);
        messageSender.getPrAlert(shareMemory);

        /*画动画*/
        moduleHardware.updated.addOnPropertyChangedCallback(settingUpdateCallback);
        moduleSoftware.updated.addOnPropertyChangedCallback(settingUpdateCallback);
        moduleHardware.updated.notifyChange();

        shareMemory.incPower.addOnPropertyChangedCallback(incPowerCallback);
        shareMemory.incPower.notifyChange();

        shareMemory.humidityPower.addOnPropertyChangedCallback(humidityPowerCallback);
        shareMemory.humidityPower.notifyChange();

        shareMemory.oxygenPower.addOnPropertyChangedCallback(oxygenPowerCallback);
        shareMemory.oxygenPower.notifyChange();

        shareMemory.ctrlMode.addOnPropertyChangedCallback(ctrlModeCallback);
        messageSender.getCtrlGet(shareMemory);
        shareMemory.ctrlMode.notifyChange();

        shareMemory.A2.notifyChange();
        shareMemory.airObjective.notifyChange();
        shareMemory.S1B.notifyChange();
        shareMemory.S2.notifyChange();
        shareMemory.skinObjective.notifyChange();
        shareMemory.H1.notifyChange();
        shareMemory.humidityObjective.notifyChange();
        shareMemory.O2.notifyChange();
        shareMemory.oxygenObjective.notifyChange();
        shareMemory.SPO2.notifyChange();
        shareMemory.spo2LowerLimit.notifyChange();
        shareMemory.spo2UpperLimit.notifyChange();
        shareMemory.PR.notifyChange();
        shareMemory.prLowerLimit.notifyChange();
        shareMemory.prUpperLimit.notifyChange();

        if(moduleHardware.is970SAndJaundice()){
            jaundice.set(true);
        }
    }

    @Override
    public void detach() {
        shareMemory.ctrlMode.removeOnPropertyChangedCallback(ctrlModeCallback);
        shareMemory.oxygenPower.removeOnPropertyChangedCallback(oxygenPowerCallback);
        shareMemory.humidityPower.removeOnPropertyChangedCallback(humidityPowerCallback);
        shareMemory.incPower.removeOnPropertyChangedCallback(incPowerCallback);
        moduleSoftware.updated.removeOnPropertyChangedCallback(settingUpdateCallback);
        moduleHardware.updated.addOnPropertyChangedCallback(settingUpdateCallback);
    }

    /*
     * 检测设备安装
     * */
    private void setEnabledSensor() {
        if (moduleHardware != null && moduleSoftware != null && navigator != null) {
            ViewUtil.displaySensor(moduleHardware.isSPO2(), moduleSoftware.isSPO2(),
                    spo2Visible, navigator::spo2ShowBorder);
            ViewUtil.displaySensor(moduleHardware.isO2(), moduleSoftware.isO2(),
                    oxygenVisible, navigator::oxygenShowBorder);
            ViewUtil.displaySensor(moduleHardware.isHUM(), moduleSoftware.isHUM(),
                    humidityVisible, navigator::humidityShowBorder);
        }
    }
}