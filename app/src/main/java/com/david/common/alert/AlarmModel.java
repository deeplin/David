package com.david.common.alert;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:51
 * email: 10525677@qq.com
 * description:
 */
public class AlarmModel extends Object {

    private String alarmId;

    private int alertPriority;

    private AlarmPriorityMode alarmPriorityMode;

    public void setAlarmId(String alarmId){
        this.alarmId = alarmId;
    }

    public void setAlertPriority(int alertPriority) {
        this.alertPriority = alertPriority;
    }

    public void setAlarmPriorityMode(AlarmPriorityMode alarmPriorityMode) {
        this.alarmPriorityMode = alarmPriorityMode;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public int getAlertPriority() {
        return alertPriority;
    }

    public AlarmPriorityMode getAlarmPriorityMode() {
        return alarmPriorityMode;
    }

    public AlarmModel() {
    }

    @Override
    public String toString() {
        return AlarmControl.getAlertField(alarmId);
    }
}
