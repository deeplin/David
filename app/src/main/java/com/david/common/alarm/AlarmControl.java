package com.david.common.alarm;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.common.util.ReflectionUtil;
import com.david.common.util.ResourceUtil;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:51
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class AlarmControl {

    public ObservableField<String> topAlarmId = new ObservableField<>();

    public ObservableInt alarmListUpdated = new ObservableInt(0);

    @Inject
    public AlarmControl() {
    }

    public static String getAlertField(String alarmId) {
        String alertDetail;
        try {
            int resourceID = ReflectionUtil.getResourceId(alarmId, ReflectionUtil.ResourcesType.string);
            alertDetail = ResourceUtil.getString(resourceID);
        } catch (Exception e) {
            alertDetail = alarmId;
        }
        return alertDetail;
    }
    public static int getMuteTime(String alarmId){
        if (Objects.equals(alarmId, "SEN.O2DIF")
                || Objects.equals(alarmId, "SEN.O2_1")
                || Objects.equals(alarmId, "SEN.O2_2")
                || Objects.equals(alarmId, "O2.DEVH")
                || Objects.equals(alarmId, "O2.DEVL")) {
            return 115;
        } else {
            return 240;
        }
    }

    public static AlarmPriorityMode getPriorityMode(String alertId){
        return AlarmPriorityMode.High;
    }

    public boolean isAlert() {
        return topAlarmId.get() != null;
    }
}
