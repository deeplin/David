package com.david.common.alarm;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:51
 * email: 10525677@qq.com
 * description:
 */
public class AlarmModel {
    private final int alarmNo;
    private final String alarmId;
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

    public AlarmPriorityMode getAlarmPriorityMode() {
        return alarmPriorityMode;
    }
}