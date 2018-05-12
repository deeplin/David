package com.david.common.serial.command.alert;

import com.david.common.alarm.AlarmModel;
import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AlertListCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~ALERT" + CommandChar.ENTER).getBytes();

    public void setAlert0(String alertId) {
        this.alarmModelList.get(0).setAlarmId(alertId);
        setAlertCount(1);
    }

    public void setAlert1(String alertId) {
        this.alarmModelList.get(1).setAlarmId(alertId);
        setAlertCount(2);
    }

    public void setAlert2(String alertId) {
        this.alarmModelList.get(2).setAlarmId(alertId);
        setAlertCount(3);
    }

    public void setAlert3(String alertId) {
        this.alarmModelList.get(3).setAlarmId(alertId);
        setAlertCount(4);
    }

    public void setAlert4(String alertId) {
        this.alarmModelList.get(4).setAlarmId(alertId);
        setAlertCount(5);
    }

    public void setAlert5(String alertId) {
        this.alarmModelList.get(5).setAlarmId(alertId);
        setAlertCount(6);
    }

    public void setAlert6(String alertId) {
        this.alarmModelList.get(6).setAlarmId(alertId);
        setAlertCount(7);
    }

    public void setAlert7(String alertId) {
        this.alarmModelList.get(7).setAlarmId(alertId);
        setAlertCount(8);
    }

    public void setAlert8(String alertId) {
        this.alarmModelList.get(8).setAlarmId(alertId);
        setAlertCount(9);
    }

    public void setAlert9(String alertId) {
        this.alarmModelList.get(9).setAlarmId(alertId);
        setAlertCount(10);
    }

    public int getAlertCount() {
        return alertCount;
    }

    private void setAlertCount(int count) {
        if (alertCount < count) {
            alertCount = count;
        }
    }

    public List<AlarmModel> getAlertList() {
        return alarmModelList;
    }

    private List<AlarmModel> alarmModelList;
    private int alertCount;

    @Inject
    public AlertListCommand() {
        alertCount = 0;
        alarmModelList = new ArrayList<>();
        for (int index = 0; index < 10; index++) {
            AlarmModel alarmModel = new AlarmModel();
            alarmModelList.add(alarmModel);
        }
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
