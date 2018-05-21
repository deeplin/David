package com.david.common.serial.command.alert;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AlertListCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~ALERT" + CommandChar.ENTER).getBytes();

    public void setAlert0(String alertId) {
        this.alarmArray[0] = alertId;
        this.alertCount = 1;
    }

    public void setAlert1(String alertId) {
        this.alarmArray[1] = alertId;
        this.alertCount = 2;
    }

    public void setAlert2(String alertId) {
        this.alarmArray[2] = alertId;
        this.alertCount = 3;
    }

    public void setAlert3(String alertId) {
        this.alarmArray[3] = alertId;
        this.alertCount = 4;
    }

    public void setAlert4(String alertId) {
        this.alarmArray[4] = alertId;
        this.alertCount = 5;
    }

    public void setAlert5(String alertId) {
        this.alarmArray[5] = alertId;
        this.alertCount = 6;
    }

    public void setAlert6(String alertId) {
        this.alarmArray[6] = alertId;
        this.alertCount = 7;
    }

    public void setAlert7(String alertId) {
        this.alarmArray[7] = alertId;
        this.alertCount = 8;
    }

    public void setAlert8(String alertId) {
        this.alarmArray[8] = alertId;
        this.alertCount = 9;
    }

    public void setAlert9(String alertId) {
        this.alarmArray[9] = alertId;
        this.alertCount = 10;
    }

    public int getAlertCount() {
        return alertCount;
    }

    public String[] getAlarmArray() {
        return alarmArray;
    }

    private String[] alarmArray;
    private int alertCount;

    @Inject
    public AlertListCommand() {
        alertCount = 0;
        alarmArray = new String[10];
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
