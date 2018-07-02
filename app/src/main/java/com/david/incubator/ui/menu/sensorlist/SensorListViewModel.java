package com.david.incubator.ui.menu.sensorlist;

import android.databinding.Observable;

import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.mode.SystemMode;
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
                        navigator.displayFirstValue(ViewUtil.formatTempValue(shareMemory.A2.get()));
                    }
                }
            }
        });

        shareMemory.S1B.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    if (shareMemory.isCabin()) {
                        navigator.displaySecondValue(ViewUtil.formatTempValue(shareMemory.S1B.get()));
                    } else if (shareMemory.isWarmer()) {
                        navigator.displayFirstValue(ViewUtil.formatTempValue(shareMemory.S1B.get()));
                    }
                }
            }
        });

        shareMemory.O2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    if (shareMemory.isCabin()) {
                        navigator.displayForthValue(ViewUtil.formatOxygenValue(shareMemory.O2.get()));
                    }
                }
            }
        });

        shareMemory.H1.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    if (shareMemory.isCabin()) {
                        navigator.displayThirdValue(ViewUtil.formatHumidityValue(shareMemory.H1.get()));
                    }
                }
            }
        });

        shareMemory.manObjective.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    if (shareMemory.isWarmer()) {
                        navigator.setManObjective(shareMemory.manObjective.get());
                    }
                }
            }
        });

        shareMemory.warmPower.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    if (shareMemory.isWarmer()) {
                        navigator.displaySecondValue(String.valueOf(shareMemory.warmPower.get()));
                    }
                }
            }
        });

        shareMemory.SPO2.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    navigator.displaySpo2Value(ViewUtil.formatSpo2Value(shareMemory.SPO2.get()));
                }
            }
        });

        shareMemory.PR.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    navigator.displayPrValue(ViewUtil.formatPrValue(shareMemory.PR.get()));
                }
            }
        });

        objectiveCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (navigator != null) {
                    CtrlMode ctrlMode = shareMemory.ctrlMode.get();
                    SystemMode systemMode = shareMemory.systemMode.get();
                    navigator.setCtrlMode(systemMode, ctrlMode, shareMemory.airObjective.get(),
                            shareMemory.skinObjective.get(), shareMemory.manObjective.get());
                }
            }
        };

        jaunediceConsumer = s -> {
            if (navigator != null) {
                navigator.displayForthValue(s);
            }
        };
    }

    public void attach() {
        if (navigator != null)
            navigator.displayThirdValue("--.--");

        messageSender.getCtrlGet(shareMemory);
        messageSender.getSpo2Alert(shareMemory);
        messageSender.getPrAlert(shareMemory);

        setSensorConfig();

        shareMemory.ctrlMode.addOnPropertyChangedCallback(objectiveCallback);
        shareMemory.A2.addOnPropertyChangedCallback(objectiveCallback);
        shareMemory.S1B.addOnPropertyChangedCallback(objectiveCallback);

        shareMemory.ctrlMode.notifyChange();
        shareMemory.A2.notifyChange();
        shareMemory.S1B.notifyChange();
        shareMemory.O2.notifyChange();
        shareMemory.H1.notifyChange();
        shareMemory.warmPower.notifyChange();
        shareMemory.SPO2.notifyChange();
        shareMemory.PR.notifyChange();

        if (moduleHardware.is93S()) {
            navigator.displayForthValue("--:--");
            jaunediceData.setConsumer(jaunediceConsumer);
        }
    }

    public void detach() {
        jaunediceData.setConsumer(null);
        timingData.setConsumer(null);

        shareMemory.S1B.removeOnPropertyChangedCallback(objectiveCallback);
        shareMemory.A2.removeOnPropertyChangedCallback(objectiveCallback);
        shareMemory.ctrlMode.removeOnPropertyChangedCallback(objectiveCallback);
    }

    /*
     * 检测设备安装
     * */
    private void setSensorConfig() {
        if (moduleHardware != null && moduleSoftware != null && navigator != null) {
            if (shareMemory.isCabin()) {
                navigator.setSystemMode(true, shareMemory.humidityObjective.get(), shareMemory.oxygenObjective.get(), null);
                navigator.showHumidity(moduleHardware.isHUM(), moduleSoftware.isHUM());
                navigator.showOxygen(moduleHardware.isO2(), moduleSoftware.isO2());

                timingData.setConsumer(null);
                timingData.stop();
            } else if (shareMemory.isWarmer()) {
                String timingMode = null;
                if (timingData.isApgarStarted()) {
                    timingMode = TimingData.APGAR;
                } else if (timingData.isCprStarted()) {
                    timingMode = TimingData.CPR;
                }
                navigator.setSystemMode(false, 0, 0, timingMode);

                timingData.setConsumer(this);

                if (!moduleHardware.is93S()) {
                    navigator.showOxygen(false, false);
                }
            }
            navigator.showSpo2(moduleHardware.isSPO2(), moduleSoftware.isSPO2());
            navigator.setSpo2Limit(ViewUtil.formatSpo2Value(shareMemory.spo2UpperLimit.get()), ViewUtil.formatSpo2Value(shareMemory.spo2LowerLimit.get()));
            navigator.setPrLimit(ViewUtil.formatPrValue(shareMemory.prUpperLimit.get()), ViewUtil.formatPrValue(shareMemory.prLowerLimit.get()));
        }
    }

    @Override
    public void accept(String timing) {
        if (navigator != null && timing != null)
            navigator.displayThirdValue(timing);
    }
}
