package com.david.common.alert;

import com.david.common.util.ReflectionUtil;
import com.david.common.util.ResourceUtil;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:51
 * email: 10525677@qq.com
 * description:
 */
public class AlertModel extends Object {

    private String alertId;
    private int resourceId;

    private int alertPriority;

    private AlertPriorityMode alertPriorityMode;

    private int muteTime;

    public void setAlertId(String alertId) throws Exception{
        this.alertId = alertId;
        this.resourceId = ReflectionUtil.getResourceId(alertId, ReflectionUtil.ResourcesType.string);
    }

    public void setAlertPriority(int alertPriority) {
        this.alertPriority = alertPriority;
    }

    public void setAlertPriorityMode(AlertPriorityMode alertPriorityMode) {
        this.alertPriorityMode = alertPriorityMode;
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

    public AlertPriorityMode getAlertPriorityMode() {
        return alertPriorityMode;
    }

    public int getMuteTime() {
        return muteTime;
    }

    public AlertModel() {
        this.alertId = alertId;
        this.alertPriority = alertPriority;
        this.alertPriorityMode = alertPriorityMode;
        this.muteTime = muteTime;
    }

    @Override
    public String toString() {
        return ResourceUtil.getString(resourceId);
    }
}
