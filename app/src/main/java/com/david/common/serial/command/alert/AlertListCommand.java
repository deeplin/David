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
        this.alertCount = 0;
    }

    public void setAlert1(String alertId) {
        this.alarmArray[1] = alertId;
        setAlertCount(2);
    }

    public void setAlert2(String alertId) {
        this.alarmArray[2] = alertId;
        setAlertCount(3);
    }

    public void setAlert3(String alertId) {
        this.alarmArray[3] = alertId;
        setAlertCount(4);
    }

    public void setAlert4(String alertId) {
        this.alarmArray[4] = alertId;
        setAlertCount(5);
    }

    public void setAlert5(String alertId) {
        this.alarmArray[5] = alertId;
        setAlertCount(6);
    }

    public void setAlert6(String alertId) {
        this.alarmArray[6] = alertId;
        setAlertCount(7);
    }

    public void setAlert7(String alertId) {
        this.alarmArray[7] = alertId;
        setAlertCount(8);
    }

    public void setAlert8(String alertId) {
        this.alarmArray[8] = alertId;
        setAlertCount(9);
    }

    public void setAlert9(String alertId) {
        this.alarmArray[9] = alertId;
        setAlertCount(10);
    }

    public int getAlertCount() {
        return alertCount;
    }

    private void setAlertCount(int count) {
        alertCount = count;
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
