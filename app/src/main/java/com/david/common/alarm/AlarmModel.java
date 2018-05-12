package com.david.common.alarm;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:51
 * email: 10525677@qq.com
 * description:
 */
public class AlarmModel extends Object {

    private String alarmId;

    public void setAlarmId(String alarmId){
        this.alarmId = alarmId;
    }

    public AlarmPriorityMode getAlarmPriorityMode() {
        return AlarmControl.getPriorityMode(alarmId);
    }

    public int getAlarmPriority() {
        return 1;
    }

    public AlarmModel() {
    }

    @Override
    public String toString() {
        return AlarmControl.getAlertField(alarmId);
    }
}
