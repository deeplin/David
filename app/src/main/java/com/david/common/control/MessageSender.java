package com.david.common.control;

import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.serial.SerialControl;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.BiConsumer;

/**
 * author: Ling Lin
 * created on: 2017/7/8 12:23
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class MessageSender {

    @Inject
    SerialControl serialControl;
    @Inject
    DaoControl daoControl;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;

    @Inject
    public MessageSender() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

//    /*数据库相关命令*/
//    public void getSpo2Config(boolean criticalCommand, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        Spo2GetCommand spo2GetCommand = new Spo2GetCommand();
//
//        spo2GetCommand.setCriticalCommand(criticalCommand);
//        spo2GetCommand.setOnCompleted((aBoolean, baseSerialMessage) -> {
//            if (aBoolean) {
//                onComplete.accept(true, baseSerialMessage);
//                daoControl.saveCommand(spo2GetCommand);
//            }
//        });
//        serialControl.addSession(spo2GetCommand);
//    }
//
//    public void setSpo2(boolean criticalCommand, String target, String value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        Spo2SetCommand spo2SetCommand = new Spo2SetCommand(target, value);
//        spo2SetCommand.setCriticalCommand(criticalCommand);
//        spo2SetCommand.setOnCompleted(onComplete);
//        serialControl.addSession(spo2SetCommand);
//    }
//
//    //读取下位机配置信息
//    public void getModule() {
//        ModuleGetHardwareCommand moduleGetHardwareCommand = new ModuleGetHardwareCommand();
//        moduleGetHardwareCommand.setCriticalCommand(true);
//        moduleGetHardwareCommand.setOnCompleted(moduleHardware);
//        serialControl.addSession(moduleGetHardwareCommand);
//        ModuleGetSoftwareCommand moduleSoftwareGetCommand = new ModuleGetSoftwareCommand();
//        moduleSoftwareGetCommand.setCriticalCommand(true);
//        moduleSoftwareGetCommand.setOnCompleted(moduleSoftware);
//        serialControl.addSession(moduleSoftwareGetCommand);
//    }
//
//    public void getCtrlGet(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        CtrlGetCommand ctrlGetCommand = new CtrlGetCommand();
//        ctrlGetCommand.setOnCompleted((aBoolean, baseSerialMessage) -> {
//            if (aBoolean) {
//                onComplete.accept(true, baseSerialMessage);
//                daoControl.saveCommand(ctrlGetCommand);
//            }
//        });
//
//        serialControl.addSession(ctrlGetCommand);
//    }
//
//    public void getSpo2Alert(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        AlertGetCommand spo2OvHAlertGetCommand = new AlertGetCommand(AlertSettingMode.SPO2_OVH);
//        spo2OvHAlertGetCommand.setOnCompleted(onComplete);
//        serialControl.addSession(spo2OvHAlertGetCommand);
//
//        AlertGetCommand spo2OvLAlertGetCommand = new AlertGetCommand(AlertSettingMode.SPO2_OVL);
//        spo2OvLAlertGetCommand.setOnCompleted(onComplete);
//        serialControl.addSession(spo2OvLAlertGetCommand);
//    }
//
//    public void getPrAlert(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        AlertGetCommand prOvHAlertGetCommand = new AlertGetCommand(AlertSettingMode.PR_OVH);
//        prOvHAlertGetCommand.setOnCompleted(onComplete);
//        serialControl.addSession(prOvHAlertGetCommand);
//
//        AlertGetCommand prOvLAlertGetCommand = new AlertGetCommand(AlertSettingMode.PR_OVL);
//        prOvLAlertGetCommand.setOnCompleted(onComplete);
//        serialControl.addSession(prOvLAlertGetCommand);
//    }
//
//    public void getCalibration(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        ShowCalibrationCommand showCalibrationCommand = new ShowCalibrationCommand();
//        showCalibrationCommand.setOnCompleted(onComplete);
//        serialControl.addSession(showCalibrationCommand);
//    }
//
//    public void getAlert(AlertSettingMode alertSettingMode, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        AlertGetCommand alertGetCommand = new AlertGetCommand(alertSettingMode);
//        alertGetCommand.setOnCompleted(onComplete);
//        serialControl.addSession(alertGetCommand);
//    }
//
//    public void getVersion(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        VersionCommand versionCommand = new VersionCommand();
//        versionCommand.setOnCompleted(onComplete);
//        serialControl.addSession(versionCommand);
//    }
//
//    public void setLED(String ledId, boolean status, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        LEDCommand ledCommand = new LEDCommand(ledId, status);
//        ledCommand.setCriticalCommand(true);
//        ledCommand.setOnCompleted(onComplete);
//        serialControl.addSession(ledCommand);
//    }
//
//    public void setStandBy(boolean status) {
//        CtrlStandbyCommand ctrlStandbyCommand = new CtrlStandbyCommand(status);
//        serialControl.addSession(ctrlStandbyCommand);
//    }
//
//    public void setMute(String alertId, boolean longMute, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        AlertMuteCommand alertMuteCommand = new AlertMuteCommand(alertId, "AB", longMute);
//        alertMuteCommand.setOnCompleted(onComplete);
//        serialControl.addSession(alertMuteCommand);
//    }
//
//    public void resumeMuteAll() {
//        AlertResumeAllCommand alertResumeAllCommand = new AlertResumeAllCommand();
//        serialControl.addSession(alertResumeAllCommand);
//    }
//
//    public void setCtrlMode(String ctrlMode, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        CtrlModeCommand controlModeCommand = new CtrlModeCommand(ctrlMode);
//        controlModeCommand.setOnCompleted(onComplete);
//        serialControl.addSession(controlModeCommand);
//    }
//
//    public void setCtrlSet(String mode, String ctrlMode, int target, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        if (target >= 0) {
//            CtrlSetCommand ctrlSetCommand =
//                    new CtrlSetCommand(mode, ctrlMode, String.valueOf(target));
//            ctrlSetCommand.setOnCompleted(onComplete);
//            serialControl.addSession(ctrlSetCommand);
//        }
//    }
//
//    public void setModule(boolean status, String sensorName, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        /*关闭开启模块*/
//        ModuleSetCommand moduleSetCommand = new ModuleSetCommand(status, sensorName);
//        moduleSetCommand.setOnCompleted(onComplete);
//        serialControl.addSession(moduleSetCommand);
//    }
//
//    public void setAlertConfig(String alertSetting, int value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        AlertSetCommand alertSetCommand = new AlertSetCommand(alertSetting, value);
//        alertSetCommand.setOnCompleted(onComplete);
//        serialControl.addSession(alertSetCommand);
//    }
//
//    public void setAlertConfig(String alertSetting, int value1, int value2, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        AlertSetCommand alertSetCommand = new AlertSetCommand(alertSetting, value1, value2);
//        alertSetCommand.setOnCompleted(onComplete);
//        serialControl.addSession(alertSetCommand);
//    }
//
//
//    public void setOxygen(int value, int id, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        CalibrateOxygenCommand calibrateO2Command = new CalibrateOxygenCommand(id, value);
//        calibrateO2Command.setOnCompleted(onComplete);
//        serialControl.addSession(calibrateO2Command);
//    }
//
//    public void setScale(int value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        CalibrateScaleCommand calibrateScaleCommand = new CalibrateScaleCommand(value);
//        calibrateScaleCommand.setOnCompleted(onComplete);
//        serialControl.addSession(calibrateScaleCommand);
//    }
//
//    public void setTempCalibration(String id, String value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        CalibrateTempCommand calibrateTempCommand = new CalibrateTempCommand(id, value);
//        calibrateTempCommand.setOnCompleted(onComplete);
//        serialControl.addSession(calibrateTempCommand);
//    }
//
//    public void setHumCalibration(int value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        CalibrateHumCommand calibrateHumCommand = new CalibrateHumCommand(value);
//        calibrateHumCommand.setOnCompleted(onComplete);
//        serialControl.addSession(calibrateHumCommand);
//    }
//
//    public void setCtrlOverride(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
//        CtrlOverrideCommand ctrlOverrideCommand = new CtrlOverrideCommand();
//        ctrlOverrideCommand.setOnCompleted(onComplete);
//        serialControl.addSession(ctrlOverrideCommand);
//    }
}

