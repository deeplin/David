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
    private List<AlarmModel> alarmModelList = new ArrayList<>();

    @Inject
    public AlarmControl() {
        alarmModel = new AlarmModel();
        topAlarm.set(alarmModel);
        addAlert("abc");
    }

    AlarmModel alarmModel;
    public void addAlert(String alert) {
        io.reactivex.Observable.interval(0, 2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong % 4 == 0) {
                        topAlarm.get().setAlertId("SYS.CON");
                        topAlarm.notifyChange();
                    } else if (aLong % 4 == 1){
                        topAlarm.get().setAlertId("SYS.FAN");
                        topAlarm.notifyChange();
                    } else if (aLong % 4 == 2){
                        topAlarm.get().setAlertId("SKIN.OVH");
                        topAlarm.notifyChange();
                    } else{
//                        topAlarm.set(null);
                    }
                });
    }

    public boolean isAlert(){
        return topAlarm.get() != null;
    }
}
