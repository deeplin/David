package com.david.common.data;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableByte;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;

import com.david.common.alarm.AlarmControl;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.dao.AnalogCommand;
import com.david.common.dao.CtrlGetCommand;
import com.david.common.dao.Spo2GetCommand;
import com.david.common.dao.StatusCommand;
import com.david.common.mode.AlarmSettingMode;
import com.david.common.mode.AverageTimeMode;
import com.david.common.mode.CtrlMode;
import com.david.common.mode.Spo2SensMode;
import com.david.common.mode.SystemMode;
import com.david.common.serial.BaseSerialMessage;
import com.david.common.serial.command.alert.AlertGetCommand;
import com.david.common.serial.command.alert.AlertListCommand;
import com.david.common.util.CommandChar;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.BiConsumer;

@Singleton
public class ShareMemory implements BiConsumer<Boolean, BaseSerialMessage> {

    @Inject
    AlarmControl alarmControl;
    @Inject
    MessageSender messageSender;

    /*Status Command*/
    public ObservableField<SystemMode> systemMode = new ObservableField<>(SystemMode.Init);
    public ObservableInt incPower = new ObservableInt(0);
    public ObservableInt warmPower = new ObservableInt(0);
    public ObservableBoolean humidityPower = new ObservableBoolean();
    public ObservableBoolean oxygenPower = new ObservableBoolean();
    public ObservableInt cTime = new ObservableInt(0);
    public ObservableInt ohTest = new ObservableInt(0);
    /*Analog Command*/
    public ObservableInt S1B = new ObservableInt();
    public ObservableInt S2 = new ObservableInt();
    public ObservableInt A2 = new ObservableInt();
    public ObservableInt SPO2 = new ObservableInt();
    public ObservableInt PR = new ObservableInt();
    public ObservableInt O2 = new ObservableInt();
    public ObservableInt H1 = new ObservableInt();
    public ObservableInt SC = new ObservableInt(0);
    public ObservableInt PI = new ObservableInt();
    public ObservableInt VU = new ObservableInt();
    /*Ctrl Get Command*/
    public ObservableField<CtrlMode> ctrlMode = new ObservableField<>(CtrlMode.Prewarm);
    public ObservableBoolean above37 = new ObservableBoolean(false);
    public ObservableInt skinObjective = new ObservableInt();
    public ObservableInt airObjective = new ObservableInt();
    public ObservableInt oxygenObjective = new ObservableInt();
    public ObservableInt humidityObjective = new ObservableInt();
    public ObservableInt manObjective = new ObservableInt();
    /*AlarmSettingMode Command*/
    public ObservableInt spo2UpperLimit = new ObservableInt();
    public ObservableInt spo2LowerLimit = new ObservableInt();
    public ObservableInt prUpperLimit = new ObservableInt();
    public ObservableInt prLowerLimit = new ObservableInt();
    /*System*/
    public ObservableBoolean lockScreen = new ObservableBoolean(false);

    public ObservableByte currentFragmentID = new ObservableByte(FragmentPage.MENU_NONE);
    /*Spo2 Get*/
    public ObservableField<Spo2SensMode> sensMode = new ObservableField<>(Spo2SensMode.Normal);
    public ObservableField<AverageTimeMode> averageTimeMode = new ObservableField<>(AverageTimeMode.Zero);
    public ObservableBoolean fastsatValue = new ObservableBoolean();

    /*Alarm*/
    public ObservableBoolean enableAlertList = new ObservableBoolean(false);
    public ObservableBoolean showAlertList = new ObservableBoolean(false);

    public ObservableInt functionTag = new ObservableInt(0);

    @Inject
    public ShareMemory() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        enableAlertList.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                boolean status = enableAlertList.get();
                if (status) {
                    messageSender.addAlarmList((aBoolean, serialMessage) -> {
                        if (aBoolean && enableAlertList.get()) {
                            AlertListCommand alertListCommand = (AlertListCommand) serialMessage;
                            if (alertListCommand.getACount() > 0) {
                                synchronized (this) {
                                    showAlertList.set(true);
                                    alarmControl.alarmListUpdated.notifyChange();
                                }
                            } else {
                                showAlertList.set(false);
                            }
                        }
                    });
                } else {
                    synchronized (this) {
                        messageSender.removeAlarmList();
                        showAlertList.set(false);
                    }
                }
            }
        });
    }

    @Override
    public void accept(Boolean aBoolean, BaseSerialMessage baseSerialMessage) throws Exception {
        if (aBoolean) {
            if (baseSerialMessage instanceof StatusCommand) {
                StatusCommand statusCommand = (StatusCommand) baseSerialMessage;

                SystemMode system = SystemMode.getMode(statusCommand.getMode());

                //todo to be removed
                if (!Constant.RELEASE_TO_DAVID)
                    system = SystemMode.Cabin;
                systemMode.set(system);

                CtrlMode ctrl = ctrlMode.get();
                //设置预热控制模式时间
                if (Objects.equals(ctrl, CtrlMode.Prewarm)) {
                    cTime.set(statusCommand.getCTime());
                } else {
                    cTime.set(0);
                }

                incPower.set(statusCommand.getInc());

                warmPower.set(statusCommand.getWarm());
                humidityPower.set(statusCommand.getHum() > 0);
                oxygenPower.set(statusCommand.getO2() > 0);
                if (Objects.equals(statusCommand.getAlert(), Constant.SENSOR_NA_STRING)) {
                    alarmControl.topAlarmId.set(null);
                    alarmControl.alarmCount.set(0);
                } else {
                    if (!Objects.equals(alarmControl.topAlarmId.get(), statusCommand.getAlert())) {
                        alarmControl.topAlarmId.set(statusCommand.getAlert());
                        if (Objects.equals(alarmControl.topAlarmId.get(), "CTRL.MAN")) {
                            manObjective.set(0);
                        }
                    }
                    alarmControl.alarmCount.set(statusCommand.getACount());
                }

                ohTest.set(statusCommand.getOhtest());
            } else if (baseSerialMessage instanceof AnalogCommand) {
                AnalogCommand analogCommand = (AnalogCommand) baseSerialMessage;
                S1B.set(analogCommand.getS1B());
                S2.set(analogCommand.getS2());
                A2.set(analogCommand.getA2());
                O2.set(analogCommand.getO2());
                H1.set(analogCommand.getH1());

                int sc = analogCommand.getSC();
                if (sc < -5) {
                    sc = 0;
                }
                int gap = sc - SC.get();
                if (gap > 5 || gap < -5) {
                    SC.set(sc);
                }

                int vu = analogCommand.getVU();
                if (VU.get() == vu) {
                    VU.notifyChange();
                } else {
                    VU.set(vu);
                }

                SPO2.set(analogCommand.getSP());
                PR.set(analogCommand.getPR());
                PI.set(analogCommand.getPI());
            } else if (baseSerialMessage instanceof CtrlGetCommand) {
                CtrlGetCommand ctrlGetCommand = (CtrlGetCommand) baseSerialMessage;
                SystemMode system = systemMode.get();
                CtrlMode ctrl = ctrlMode.get();
                CtrlMode newCtrl = CtrlMode.getMode(ctrlGetCommand.getCtrl());

                //todo to be removed
                if (!Constant.RELEASE_TO_DAVID)
                    newCtrl = CtrlMode.Air;

                if (Objects.equals(newCtrl, CtrlMode.Standby)) {
                    if (Objects.equals(system, SystemMode.Cabin)) {
                        skinObjective.set(ctrlGetCommand.getC_skin());
                        if (Objects.equals(ctrl, CtrlMode.Prewarm) || (Objects.equals(ctrl, CtrlMode.Manual))) {
                            ctrlMode.set(CtrlMode.Air);
                        }
                    } else if (Objects.equals(system, SystemMode.Warmer)) {
                        skinObjective.set(ctrlGetCommand.getW_skin());
                        if (Objects.equals(ctrl, CtrlMode.Air))
                            ctrlMode.set(CtrlMode.Manual);
                    }
                } else {
                    ctrlMode.set(newCtrl);
                    if (Objects.equals(system, SystemMode.Cabin)) {
                        skinObjective.set(ctrlGetCommand.getC_skin());
                    } else if (Objects.equals(system, SystemMode.Warmer)) {
                        skinObjective.set(ctrlGetCommand.getW_skin());
                    }
                }

                oxygenObjective.set(ctrlGetCommand.getC_o2());
                humidityObjective.set(ctrlGetCommand.getC_hum());
                manObjective.set(ctrlGetCommand.getW_man());

                airObjective.set(ctrlGetCommand.getC_air());

                if (isSkin()) {
                    if (Objects.equals(system, SystemMode.Cabin)) {
                        setAbove37(ctrlGetCommand.getC_skin());
                    } else if (Objects.equals(system, SystemMode.Warmer)) {
                        setAbove37(ctrlGetCommand.getW_skin());
                    }
                } else if (isAir()) {
                    setAbove37(ctrlGetCommand.getC_air());
                } else {
                    above37.set(false);
                }
            } else if (baseSerialMessage instanceof AlertGetCommand) {
                /*显示Spo2 PR目标值*/
                AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                AlarmSettingMode alarmSettingMode = alertGetCommand.getAlarmSettingMode();

                if (alarmSettingMode.equals(AlarmSettingMode.SPO2_OVH)) {
                    spo2UpperLimit.set(alertGetCommand.getLimit());
                } else if (alarmSettingMode.equals(AlarmSettingMode.SPO2_OVL)) {
                    spo2LowerLimit.set(alertGetCommand.getLimit());
                } else if (alarmSettingMode.equals(AlarmSettingMode.PR_OVH)) {
                    prUpperLimit.set(alertGetCommand.getLimit());
                } else if (alarmSettingMode.equals(AlarmSettingMode.PR_OVL)) {
                    prLowerLimit.set(alertGetCommand.getLimit());
                }
            } else if (baseSerialMessage instanceof Spo2GetCommand) {
                Spo2GetCommand spo2GetCommand = (Spo2GetCommand) baseSerialMessage;
                Spo2SensMode spo2SensMode = Spo2SensMode.getMode(spo2GetCommand.getSens());
                if (spo2SensMode != null) {
                    sensMode.set(spo2SensMode);
                }

                AverageTimeMode spo2AverageTimeMode = AverageTimeMode.getMode(spo2GetCommand.getAvg());
                if (spo2AverageTimeMode != null) {
                    averageTimeMode.set(spo2AverageTimeMode);
                }

                if (Objects.equals(spo2GetCommand.getFastsat(), CommandChar.ON)) {
                    fastsatValue.set(true);
                } else {
                    fastsatValue.set(false);
                }
            }
        }
    }

    private void setAbove37(int tempObjective) {
        if (tempObjective > Constant.TEMP_370) {
            above37.set(true);
        } else {
            above37.set(false);
        }
    }

    public boolean isCabin() {
        return Objects.equals(systemMode.get(), SystemMode.Cabin);
    }

    public boolean isWarmer() {
        return Objects.equals(systemMode.get(), SystemMode.Warmer);
    }

    public boolean isTransit() {
        return Objects.equals(systemMode.get(), SystemMode.Transit);
    }

    public boolean isAir() {
        return Objects.equals(ctrlMode.get(), CtrlMode.Air);
    }

    public boolean isSkin() {
        return Objects.equals(ctrlMode.get(), CtrlMode.Skin);
    }

    public boolean isManual() {
        return Objects.equals(ctrlMode.get(), CtrlMode.Manual);
    }

    public boolean isPrewarm() {
        return Objects.equals(ctrlMode.get(), CtrlMode.Prewarm);
    }

}