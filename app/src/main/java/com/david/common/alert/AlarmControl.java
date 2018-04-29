package com.david.common.alert;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

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

    public ObservableField<AlarmModel> topAlarm = new ObservableField<>();

    public ObservableInt alarmUpdated = new ObservableInt(0);
    public List<AlarmModel> alarmModelList = new ArrayList<>();

    @Inject
    public AlarmControl() {

        addAlert("abc");
    }

    private int index = 0;

    public void addAlert(String alert) {
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
                        alarmModel.setAlertId("FLOW.OVH");
                        alarmModel.setAlarmPriorityMode(AlarmPriorityMode.High);
                    } else if (aLong % 3 == 1) {
                        alarmModel.setAlertId("SPO2.BIT12");
                        alarmModel.setAlarmPriorityMode(AlarmPriorityMode.Middle);
                    } else if (aLong % 3 == 2) {
                        alarmModel.setAlertId("SPO2.BIT7");
                        alarmModel.setAlarmPriorityMode(AlarmPriorityMode.Low);
                    }
                    alarmModel.setMuteTime(240);
                    alarmModelList.add(alarmModel);
                    alarmUpdated.set(alarmUpdated.get() + 1);

                    topAlarm.set(alarmModel);
                });
    }

    public boolean isAlert() {
        return topAlarm.get() != null;
    }

}
