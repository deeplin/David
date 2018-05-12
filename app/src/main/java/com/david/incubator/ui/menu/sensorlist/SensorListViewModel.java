package com.david.incubator.ui.menu.sensorlist;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;

import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.ui.BaseNavigatorModel;
import com.david.incubator.ui.home.warmer.JaunediceData;
import com.david.incubator.util.TimingData;
import com.david.incubator.util.ViewUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2017/12/31 17:59
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class SensorListViewModel extends BaseNavigatorModel<SensorListNavigator> implements Consumer<String> {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;
    @Inject
    TimingData timingData;
    @Inject
    JaunediceData jaunediceData;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean spo2Visible = new ObservableBoolean(true);
    public ObservableBoolean oxygenVisible = new ObservableBoolean(true);
    public ObservableBoolean humidityVisible = new ObservableBoolean(true);

    Observable.OnPropertyChangedCallback objectiveCallback;

    Consumer<String> jaunediceConsumer;

    @Inject
    public SensorListViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        shareMemory.systemMode.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                setSensorConfig();
            }
        });

        shareMemory.A2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (shareMemory.isCabin()) {
                    if (navigator != null) {
                        navigator.displayTemp1Value(ViewUtil.formatTempValue(shareMemory.A2.get()));
                    }
                }
            }
        });

        shareMemory.S1B.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    if (shareMemory.isCabin()) {
                        navigator.displayTemp2Value(ViewUtil.formatTempValue(shareMemory.S1B.get()));
                    } else if (shareMemory.isWarmer()) {
                        navigator.displayTemp1Value(ViewUtil.formatTempValue(shareMemory.S1B.get()));
                    }
                }
            }
        });

        shareMemory.S2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    if (shareMemory.isWarmer()) {
                        navigator.displayTemp2Value(ViewUtil.formatTempValue(shareMemory.S2.get()));
                    }
                }
            }
        });

        shareMemory.O2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    if (shareMemory.isCabin()) {
                        navigator.displayOxygenValue(ViewUtil.formatOxygenValue(shareMemory.O2.get()));
                    }
                }
            }
        });

        shareMemory.H1.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    if (shareMemory.isCabin()) {
                        navigator.displayHumidityValue(ViewUtil.formatHumidityValue(shareMemory.H1.get()));
                    }
                }
            }
        });

        objectiveCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    CtrlMode ctrlMode = shareMemory.ctrlMode.get();
                    if (ctrlMode.equals(CtrlMode.Skin)) {
                        if (shareMemory.isCabin()) {
                            navigator.displaySkinAnimation();
                            navigator.displayTemp1Objective(null);
                            navigator.displayTemp2Objective(ViewUtil.formatTempValue(shareMemory.skinObjective.get()));
                        } else if (shareMemory.isWarmer()) {
                            navigator.displayAirAnimation(false);
                            navigator.displayTemp1Objective(ViewUtil.formatTempValue(shareMemory.skinObjective.get()));
                            navigator.displayTemp2Objective(null);
                        }
                    } else if (ctrlMode.equals(CtrlMode.Air)) {
                        navigator.displayAirAnimation(true);
                        navigator.displayTemp1Objective(ViewUtil.formatTempValue(shareMemory.airObjective.get()));
                        navigator.displayTemp2Objective(null);
                    } else {
                        navigator.clearSkinAnimation();
                        navigator.displayTemp1Objective(null);
                        navigator.displayTemp2Objective(null);
                    }
                }
            }
        };

        jaunediceConsumer = s -> {
            if (navigator != null) {
                navigator.displayOxygenValue(s);
            }
        };

//        shareMemory.SC.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                if (navigator != null) {
//                    SystemMode systemMode = shareMemory.systemMode.get();
//                    if (systemMode.equals(SystemMode.Warmer)) {
//                        navigator.displayOxygenValue(ViewUtil.formatScaleValue(shareMemory.SC.get()));
//                    }
//                }
//            }
//        });
    }

    public void attach() {
        if (navigator != null)
            navigator.displayHumidityValue("--.--");

        messageSender.getCtrlGet(shareMemory);
        messageSender.getSpo2Alert(shareMemory);
        messageSender.getPrAlert(shareMemory);

        setSensorConfig();

        shareMemory.ctrlMode.addOnPropertyChangedCallback(objectiveCallback);
        shareMemory.A2.addOnPropertyChangedCallback(objectiveCallback);
        shareMemory.S1B.addOnPropertyChangedCallback(objectiveCallback);
        shareMemory.S2.addOnPropertyChangedCallback(objectiveCallback);

        shareMemory.ctrlMode.notifyChange();
        shareMemory.A2.notifyChange();
        shareMemory.S1B.notifyChange();
        shareMemory.S2.notifyChange();
        shareMemory.O2.notifyChange();
        shareMemory.H1.notifyChange();
        shareMemory.SC.notifyChange();

        if (moduleHardware.is93S()) {
            jaunediceData.setConsumer(jaunediceConsumer);
        }
    }

    public void detach() {
        jaunediceData.setConsumer(null);
        timingData.setConsumer(null);

        shareMemory.S2.removeOnPropertyChangedCallback(objectiveCallback);
        shareMemory.S1B.removeOnPropertyChangedCallback(objectiveCallback);
        shareMemory.A2.removeOnPropertyChangedCallback(objectiveCallback);
        shareMemory.ctrlMode.removeOnPropertyChangedCallback(objectiveCallback);
    }

    /*
     * 检测设备安装
     * */
    private void setSensorConfig() {
        if (moduleHardware != null && moduleSoftware != null && navigator != null) {
            ViewUtil.displaySensor(moduleHardware.isSPO2(), moduleSoftware.isSPO2(), spo2Visible, navigator::spo2ShowBorder);

            if (shareMemory.isCabin()) {
                navigator.setBackground(true);
                ViewUtil.displaySensor(moduleHardware.isO2(), moduleSoftware.isO2(),
                        oxygenVisible, navigator::oxygenShowBorder);
                ViewUtil.displaySensor(moduleHardware.isHUM(), moduleSoftware.isHUM(),
                        humidityVisible, navigator::humidityShowBorder);
            } else if (shareMemory.isWarmer()) {
                navigator.setBackground(false);
                ViewUtil.displaySensor(true, true,
                        oxygenVisible, navigator::oxygenShowBorder);
                ViewUtil.displaySensor(true, true,
                        humidityVisible, navigator::humidityShowBorder);

                timingData.setConsumer(this);

                if (timingData.isApgarStarted()) {
                    navigator.setTimingValue(TimingData.APGAR);
                } else if (timingData.isCprStarted()) {
                    navigator.setTimingValue(TimingData.CPR);
                } else {
                    navigator.setTimingValue("");
                }
            }
        }
    }

    @Override
    public void accept(String timing) {
        if (navigator != null && timing != null)
            navigator.displayHumidityValue(timing);
    }
}
