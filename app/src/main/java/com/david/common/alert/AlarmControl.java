package com.david.common.alert;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.common.serial.command.alert.AlertListCommand;
import com.david.common.util.ReflectionUtil;
import com.david.common.util.ResourceUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:51
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class AlarmControl {

    public ObservableField<String> topAlarmId = new ObservableField<>();

    public ObservableInt alarmUpdated = new ObservableInt(0);
    public List<AlarmModel> alarmModelList = new ArrayList<>();

    @Inject
    public AlarmControl() {
        addAlert(null);
    }

    private int index = 0;

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
        if (alarmId.equals("SEN.O2DIF")
                || alarmId.equals("SEN.O2_1")
                || alarmId.equals("SEN.O2_2")
                || alarmId.equals("O2.DEVH")
                || alarmId.equals("O2.DEVL")) {
            return 115;
        } else {
            return 240;
        }
    }

    public void addAlert(AlertListCommand alertListCommand) {
        io.reactivex.Observable.interval(0, 1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (index >= 10) {
                        if (alarmModelList.size() > 0) {
                            alarmModelList.remove(0);
                            alarmUpdated.set(alarmUpdated.get() + 1);
                        } else {
                            index = 0;
                        }
                        return;
                    }
                    index++;

                    AlarmModel alarmModel = new AlarmModel();

                    if (aLong % 3 == 0) {
                        alarmModel.setAlarmId("FLOW.OVH");
                        alarmModel.setAlarmPriorityMode(AlarmPriorityMode.High);
                    } else if (aLong % 3 == 1) {
                        alarmModel.setAlarmId("SPO2.BIT12");
                        alarmModel.setAlarmPriorityMode(AlarmPriorityMode.Middle);
                    } else if (aLong % 3 == 2) {
                        alarmModel.setAlarmId("SPO2.BIT7");
                        alarmModel.setAlarmPriorityMode(AlarmPriorityMode.Low);
                    }
                    alarmModel.setMuteTime(240);
                    alarmModelList.add(alarmModel);
                    alarmUpdated.set(alarmUpdated.get() + 1);

//                    topAlarm.set(alarmModel);
                });
    }

    public boolean isAlert() {
        return topAlarmId.get() != null;
    }
}
