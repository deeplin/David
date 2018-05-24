package com.david.common.alarm;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:51
 * email: 10525677@qq.com
 * description:
 */
public class AlarmModel {

    public int getAlarmNo() {
        return alarmNo;
    }

    public String getAlarmId() {
        return alarmId;
    }

    public AlarmPriorityMode getAlarmPriorityMode() {
        return alarmPriorityMode;
    }

    private final int alarmNo;
    private String alarmId;
    private final AlarmPriorityMode alarmPriorityMode;

    public AlarmModel(int alarmNo, String alarmId, AlarmPriorityMode alarmPriorityMode) {
        this.alarmNo = alarmNo;
        this.alarmId = alarmId;
        this.alarmPriorityMode = alarmPriorityMode;
    }

    @Override
    public String toString() {
        return AlarmControl.getAlertField(alarmId);
    }
}