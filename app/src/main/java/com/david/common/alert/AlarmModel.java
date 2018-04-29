package com.david.common.alert;

import com.david.common.util.ReflectionUtil;
import com.david.common.util.ResourceUtil;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:51
 * email: 10525677@qq.com
 * description:
 */
public class AlarmModel extends Object {

    private String alertId;
    private int resourceId;

    private int alertPriority;

    private AlarmPriorityMode alarmPriorityMode;

    private int muteTime;

    public void setAlertId(String alertId) throws Exception{
        this.alertId = alertId;
        this.resourceId = ReflectionUtil.getResourceId(alertId, ReflectionUtil.ResourcesType.string);
    }

    public void setAlertPriority(int alertPriority) {
        this.alertPriority = alertPriority;
    }

    public void setAlarmPriorityMode(AlarmPriorityMode alarmPriorityMode) {
        this.alarmPriorityMode = alarmPriorityMode;
    }

    public void setMuteTime(int muteTime) {
        this.muteTime = muteTime;
    }

    public String getAlertId() {
        return alertId;
    }

    public int getAlertPriority() {
        return alertPriority;
    }

    public AlarmPriorityMode getAlarmPriorityMode() {
        return alarmPriorityMode;
    }

    public int getMuteTime() {
        return muteTime;
    }

    public AlarmModel() {
    }

    @Override
    public String toString() {
        return ResourceUtil.getString(resourceId);
    }
}
