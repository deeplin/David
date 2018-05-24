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
    }

    public void setAlert1(String alertId) {
        this.alarmArray[1] = alertId;
    }

    public void setAlert2(String alertId) {
        this.alarmArray[2] = alertId;
    }

    public void setAlert3(String alertId) {
        this.alarmArray[3] = alertId;
    }

    public void setAlert4(String alertId) {
        this.alarmArray[4] = alertId;
    }

    public void setAlert5(String alertId) {
        this.alarmArray[5] = alertId;
    }

    public void setAlert6(String alertId) {
        this.alarmArray[6] = alertId;
    }

    public void setAlert7(String alertId) {
        this.alarmArray[7] = alertId;
    }

    public void setAlert8(String alertId) {
        this.alarmArray[8] = alertId;
    }

    public void setAlert9(String alertId) {
        this.alarmArray[9] = alertId;
    }

    public String[] getAlarmArray() {
        return alarmArray;
    }

    public int getACount() {
        return ACount;
    }

    public void setACount(int ACount) {
        this.ACount = ACount;
    }

    private String[] alarmArray;
    private int ACount;

    @Inject
    public AlertListCommand() {
        alarmArray = new String[10];
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
