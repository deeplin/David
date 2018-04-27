package com.david.incubator.ui.main.side;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.david.R;
import com.david.common.control.AlertControl;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.mode.SystemMode;
import com.david.common.ui.IViewModel;
import com.david.common.util.TimeUtil;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:53
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class SideViewModel implements IViewModel {

    @Inject
    AlertControl alertControl;
    @Inject
    ShareMemory shareMemory;
    @Inject
    MessageSender messageSender;

    private Disposable disposable= null;

    public ObservableField<String> date = new ObservableField<>("0000-00-00");
    public ObservableField<String> time = new ObservableField<>("--:--");
    public ObservableBoolean lockScreen = new ObservableBoolean(false);
    public ObservableInt lockScreenImage = new ObservableInt(R.mipmap.screen_unlock);
    public ObservableBoolean stopAlarm = new ObservableBoolean(false);
    public ObservableInt stopAlarmImage = new ObservableInt(R.mipmap.alarm_started);

    private Observable.OnPropertyChangedCallback alertCallback;

    @Inject
    public SideViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

//        alertCallback = new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                if (alertControl.isAlert()) {
//                    lockScreenImage.set(R.mipmap.stop_alert);
//                } else {
//                    if (shareMemory.lockScreen.get()) {
//                        lockScreenImage.set(R.mipmap.screen_lock);
//                    } else {
//                        lockScreenImage.set(R.mipmap.screen_unlock);
//                    }
//                    stopAlarmImage.set(R.mipmap.alarm_started);
//                }
//            }
//        };
    }

    @Override
    public void attach() {
        alertControl.alertStringField.addOnPropertyChangedCallback(alertCallback);
    }

    @Override
    public void detach() {
        alertControl.alertStringField.removeOnPropertyChangedCallback(alertCallback);
    }

    public void displayCurrentTime() {
        this.date.set(TimeUtil.getCurrentDate(TimeUtil.Date));
        this.time.set(TimeUtil.getCurrentEnglishDate(TimeUtil.Time));
    }

    public void setLockScreen(boolean status) {
        lockScreen.set(status);
        stopAlarm.set(status);
        if (status) {
            lockScreenImage.set(R.mipmap.screen_lock);
        } else {
            lockScreenImage.set(R.mipmap.screen_unlock);
        }
    }

    public void muteAlarm(){
        SystemMode systemMode = shareMemory.systemMode.get();
        if (!Objects.equals(systemMode, SystemMode.Transit)) {
//            String alertID = alertControl.getAlertID();
//            if (alertID != null) {
//                boolean longMute;
//                if (alertID.equals("SEN.O2DIF")
//                        || alertID.equals("SEN.O2_1")
//                        || alertID.equals("SEN.O2_2")
//                        || alertID.equals("O2.DEVH")
//                        || alertID.equals("O2.DEVL")) {
//                    longMute = false;
//                } else {
//                    longMute = true;
//                }
//                messageSender.setMute(alertID, longMute, (aBoolean, baseSerialMessage) -> {
//                    if (aBoolean) {
//                        /*静音成功*/
//                        stopAlarmImage.set(R.mipmap.alarm_stopped);
//                        int delay;
//                        if (longMute)
//                            delay = 240;
//                        else
//                            delay = 115;
//
//                        if(disposable != null){
//                            disposable.dispose();
//                        }
//                        disposable = io.reactivex.Observable.timer(delay, TimeUnit.SECONDS)
//                                .observeOn(AndroidSchedulers.mainThread())
//                                .subscribe(aLong -> stopAlarmImage.set(R.mipmap.alarm_started));
//                    }
//                });
//            }
        }
    }
}
