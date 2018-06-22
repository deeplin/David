package com.david.common.alarm;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.apkfuns.logutils.LogUtils;
import com.david.common.control.MainApplication;
import com.david.common.util.FileUtil;
import com.david.common.util.ReflectionUtil;
import com.david.common.util.ResourceUtil;

import java.util.HashMap;
import java.util.Map;
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

    private static Map<String, AlarmModel> alarmMap = new HashMap<>();

    public ObservableField<String> topAlarmId = new ObservableField<>();
    public ObservableInt alarmCount = new ObservableInt(0);
    public ObservableInt alarmListUpdated = new ObservableInt(0);

    @Inject
    public AlarmControl() {
        loadAssets();
    }

    private void loadAssets() {
        try {
            String GESTATION_DATA_FILE = "Alarm.txt";
            String alarmString = FileUtil.readTextFileFromAssets(
                    MainApplication.getInstance(), GESTATION_DATA_FILE);

            String[] alarmArray = alarmString.split("\t");
            for (int index = 0; index < alarmArray.length; ) {
                int no = Integer.parseInt(alarmArray[index++]);
                String id = alarmArray[index++];
                String priorityString = alarmArray[index++];
                AlarmPriorityMode priorityMode;
                if (Objects.equals(priorityString, "3")) {
                    priorityMode = AlarmPriorityMode.High;
                } else if (Objects.equals(priorityString, "2")) {
                    priorityMode = AlarmPriorityMode.Middle;
                } else {
                    priorityMode = AlarmPriorityMode.Low;
                }
                AlarmModel alarmModel = new AlarmModel(no, id, priorityMode);
                alarmMap.put(id, alarmModel);
            }
        } catch (Exception e) {
            LogUtils.e(e);
        }
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

    public static int getMuteTime(String alarmId) {
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

    public static AlarmModel getAlarmMode(String alarmId) {
        return alarmMap.get(alarmId);
    }

    public boolean isAlert() {
        return topAlarmId.get() != null;
    }
}