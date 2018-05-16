package com.david.common.control;

import com.david.common.dao.CtrlGetCommand;
import com.david.common.dao.Spo2GetCommand;
import com.david.common.mode.AlarmSettingMode;
import com.david.common.serial.BaseSerialMessage;
import com.david.common.serial.SerialControl;
import com.david.common.serial.command.other.FactoryCommand;
import com.david.common.serial.command.other.LEDCommand;
import com.david.common.serial.command.other.VersionCommand;
import com.david.common.serial.command.alert.AlertDisableCommand;
import com.david.common.serial.command.alert.AlertGetCommand;
import com.david.common.serial.command.alert.AlertListCommand;
import com.david.common.serial.command.alert.AlertMuteCommand;
import com.david.common.serial.command.alert.AlertSetCommand;
import com.david.common.serial.command.calibration.CalibrateHumCommand;
import com.david.common.serial.command.calibration.CalibrateOxygenCommand;
import com.david.common.serial.command.calibration.CalibrateScaleCommand;
import com.david.common.serial.command.calibration.CalibrateTempCommand;
import com.david.common.serial.command.calibration.ShowCalibrationCommand;
import com.david.common.serial.command.ctrl.CtrlModeCommand;
import com.david.common.serial.command.ctrl.CtrlOverrideCommand;
import com.david.common.serial.command.ctrl.CtrlSetCommand;
import com.david.common.serial.command.module.ModuleGetHardwareCommand;
import com.david.common.serial.command.module.ModuleGetSoftwareCommand;
import com.david.common.serial.command.module.ModuleSetCommand;
import com.david.common.serial.command.spo2.Spo2SetCommand;

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
    AlertListCommand alertListCommand;

    @Inject
    public MessageSender() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    //    //读取下位机配置信息
    public void getHardwareModule(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        ModuleGetHardwareCommand moduleGetHardwareCommand = new ModuleGetHardwareCommand();
        moduleGetHardwareCommand.setOnCompleted(onComplete);
        serialControl.addSession(moduleGetHardwareCommand);
    }

    public void getSoftwareModule(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        ModuleGetSoftwareCommand moduleSoftwareGetCommand = new ModuleGetSoftwareCommand();
        moduleSoftwareGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(moduleSoftwareGetCommand);
    }

    public void setLED(String ledId, boolean status, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        LEDCommand ledCommand = new LEDCommand(ledId, status);
        ledCommand.setOnCompleted(onComplete);
        serialControl.addSession(ledCommand);
    }

    //    /*数据库相关命令*/
    public void getSpo2(boolean criticalCommand, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        Spo2GetCommand spo2GetCommand = new Spo2GetCommand();
        if (criticalCommand) {
            spo2GetCommand.setCritical();
        }

        spo2GetCommand.setOnCompleted((aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                onComplete.accept(true, baseSerialMessage);
                daoControl.saveCommand(spo2GetCommand);
            }
        });
        serialControl.addSession(spo2GetCommand);
    }

    public void setSpo2(boolean criticalCommand, String target, String value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        Spo2SetCommand spo2SetCommand = new Spo2SetCommand(target, value);
        if (criticalCommand) {
            spo2SetCommand.setCritical();
        }
        spo2SetCommand.setOnCompleted(onComplete);
        serialControl.addSession(spo2SetCommand);
    }

    public void setMute(String alertId, int time, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertMuteCommand alertMuteCommand = new AlertMuteCommand(alertId, "AB", time);
        alertMuteCommand.setOnCompleted(onComplete);
        serialControl.addSession(alertMuteCommand);
    }

    public void getCtrlGet(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        CtrlGetCommand ctrlGetCommand = new CtrlGetCommand();
        ctrlGetCommand.setOnCompleted((aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                onComplete.accept(true, baseSerialMessage);
                daoControl.saveCommand(ctrlGetCommand);
            }
        });
        serialControl.addSession(ctrlGetCommand);
    }

    public void getSpo2Alert(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertGetCommand spo2OvHAlertGetCommand = new AlertGetCommand(AlarmSettingMode.SPO2_OVH);
        spo2OvHAlertGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(spo2OvHAlertGetCommand);

        AlertGetCommand spo2OvLAlertGetCommand = new AlertGetCommand(AlarmSettingMode.SPO2_OVL);
        spo2OvLAlertGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(spo2OvLAlertGetCommand);
    }

    public void getPrAlert(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertGetCommand prOvHAlertGetCommand = new AlertGetCommand(AlarmSettingMode.PR_OVH);
        prOvHAlertGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(prOvHAlertGetCommand);

        AlertGetCommand prOvLAlertGetCommand = new AlertGetCommand(AlarmSettingMode.PR_OVL);
        prOvLAlertGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(prOvLAlertGetCommand);
    }

    public void setCtrlMode(String ctrlMode, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        CtrlModeCommand controlModeCommand = new CtrlModeCommand(ctrlMode);
        controlModeCommand.setOnCompleted(onComplete);
        serialControl.addSession(controlModeCommand);
    }

    public void setCtrlSet(String mode, String ctrlMode, int target, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        if (target >= 0) {
            CtrlSetCommand ctrlSetCommand =
                    new CtrlSetCommand(mode, ctrlMode, String.valueOf(target));
            ctrlSetCommand.setOnCompleted(onComplete);
            serialControl.addSession(ctrlSetCommand);
        }
    }

    public void setModule(boolean status, String sensorName, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        /*关闭开启模块*/
        ModuleSetCommand moduleSetCommand = new ModuleSetCommand(status, sensorName);
        moduleSetCommand.setOnCompleted(onComplete);
        serialControl.addSession(moduleSetCommand);
    }

    public void setAlarmConfig(String alertSetting, int value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertSetCommand alertSetCommand = new AlertSetCommand(alertSetting, value);
        alertSetCommand.setOnCompleted(onComplete);
        serialControl.addSession(alertSetCommand);
    }

    public void setAlarmConfig(String alertSetting, int value1, int value2, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertSetCommand alertSetCommand = new AlertSetCommand(alertSetting, value1, value2);
        alertSetCommand.setOnCompleted(onComplete);
        serialControl.addSession(alertSetCommand);
    }

    public void addAlarmList(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        alertListCommand.setOnCompleted(onComplete);
        serialControl.addRepeatSession(alertListCommand);
    }

    public void removeAlarmList() {
        serialControl.removeRepeatSession(AlertListCommand.class);
    }

    public void clearAlarm() {
        AlertDisableCommand alertDisableCommand = new AlertDisableCommand();
        serialControl.addSession(alertDisableCommand);
    }

    public void setOxygen(int value, int id, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        CalibrateOxygenCommand calibrateO2Command = new CalibrateOxygenCommand(id, value);
        calibrateO2Command.setOnCompleted(onComplete);
        serialControl.addSession(calibrateO2Command);
    }

    public void setScale(int value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        CalibrateScaleCommand calibrateScaleCommand = new CalibrateScaleCommand(value);
        calibrateScaleCommand.setOnCompleted(onComplete);
        serialControl.addSession(calibrateScaleCommand);
    }

    public void getVersion(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        VersionCommand versionCommand = new VersionCommand();
        versionCommand.setOnCompleted(onComplete);
        serialControl.addSession(versionCommand);
    }

    public void setTempCalibration(String id, String value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        CalibrateTempCommand calibrateTempCommand = new CalibrateTempCommand(id, value);
        calibrateTempCommand.setOnCompleted(onComplete);
        serialControl.addSession(calibrateTempCommand);
    }

    public void setHumCalibration(int value, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        CalibrateHumCommand calibrateHumCommand = new CalibrateHumCommand(value);
        calibrateHumCommand.setOnCompleted(onComplete);
        serialControl.addSession(calibrateHumCommand);
    }

    public void getCalibration(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        ShowCalibrationCommand showCalibrationCommand = new ShowCalibrationCommand();
        showCalibrationCommand.setOnCompleted(onComplete);
        serialControl.addSession(showCalibrationCommand);
    }

    public void getAlert(AlarmSettingMode alertSettingMode, BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        AlertGetCommand alertGetCommand = new AlertGetCommand(alertSettingMode);
        alertGetCommand.setOnCompleted(onComplete);
        serialControl.addSession(alertGetCommand);
    }

    public void setCtrlOverride(BiConsumer<Boolean, BaseSerialMessage> onComplete) {
        CtrlOverrideCommand ctrlOverrideCommand = new CtrlOverrideCommand();
        ctrlOverrideCommand.setOnCompleted(onComplete);
        serialControl.addSession(ctrlOverrideCommand);
    }

    public void Factory(BiConsumer<Boolean, BaseSerialMessage> onComplete){
        FactoryCommand factoryCommand = new FactoryCommand();
        factoryCommand.setOnCompleted(onComplete);
        serialControl.addSession(factoryCommand);
    }

//    public void resumeMuteAll() {
//        AlertResumeAllCommand alertResumeAllCommand = new AlertResumeAllCommand();
//        serialControl.addSession(alertResumeAllCommand);
//    }
//
}

