package com.david.common.alert;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.Log;

import com.david.common.control.MainApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:51
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class AlertControl {

    public ObservableField<String> topAlert = new ObservableField<>();

    public ObservableInt alertUpdated = new ObservableInt(0);
    private List<AlertModel> alertModelList = new ArrayList<>();

    @Inject
    public AlertControl() {
        addAlert("abc");
    }


    public void addAlert(String alert) {
        io.reactivex.Observable.interval(0, 2, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong % 4 == 0) {
                        topAlert.set("SYS.CON");
                    } else if (aLong % 4 == 1){
                        topAlert.set("SYS.FAN");
                    } else if (aLong % 4 == 2){
                        topAlert.set("SYS.FAN");
                    } else{
                        topAlert.set(null);
                    }
                });
    }
}
