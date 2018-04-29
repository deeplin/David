package com.david.incubator.ui.main.side;

import android.databinding.Observable;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.alert.AlarmControl;
import com.david.common.alert.AlarmModel;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.ui.IViewModel;

import java.util.concurrent.TimeUnit;

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
    AlarmControl alarmControl;
    @Inject
    public ShareMemory shareMemory;
    @Inject
    MessageSender messageSender;

    public ObservableInt lockScreenImage = new ObservableInt(R.mipmap.screen_unlocked);
    public ObservableInt clearAlarmImage = new ObservableInt(R.mipmap.alarm_cleared);
    public ObservableInt muteAlarmImage = new ObservableInt(R.mipmap.alarm_unmuted);

    private Observable.OnPropertyChangedCallback lockScreenCallback;
    private Observable.OnPropertyChangedCallback clearAlarmCallback;

    private Disposable muteDisposable = null;

    @Inject
    public SideViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        lockScreenCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (shareMemory.lockScreen.get()) {
                    lockScreenImage.set(R.mipmap.screen_locked);
                } else {
                    lockScreenImage.set(R.mipmap.screen_unlocked);
                }
            }
        };

        clearAlarmCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (alarmControl.isAlert()) {
                    clearAlarmImage.set(R.mipmap.alarm_started);
                } else {
                    clearAlarmImage.set(R.mipmap.alarm_cleared);
                }
            }
        };
    }

    @Override
    public void attach() {
        shareMemory.lockScreen.addOnPropertyChangedCallback(lockScreenCallback);
        alarmControl.topAlarm.addOnPropertyChangedCallback(clearAlarmCallback);
    }

    @Override
    public void detach() {
        alarmControl.topAlarm.removeOnPropertyChangedCallback(clearAlarmCallback);
        shareMemory.lockScreen.removeOnPropertyChangedCallback(lockScreenCallback);
    }

    void clearAlarm() {
        //send clear alarm command
        clearAlarmImage.set(R.mipmap.alarm_cleared);
    }

    public void muteAlarm() {
        AlarmModel alarmModel = alarmControl.topAlarm.get();
        if (alarmModel != null) {
            messageSender.setMute(alarmModel.getAlertId(), alarmModel.getMuteTime(), (aBoolean, baseSerialMessage) -> {
                if (aBoolean) {
                    /*静音成功*/
                    muteAlarmImage.set(R.mipmap.alarm_muted);
                    if (muteDisposable != null) {
                        muteDisposable.dispose();
                    }
                    muteDisposable = io.reactivex.Observable.timer(alarmModel.getMuteTime(), TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(aLong -> muteAlarmImage.set(R.mipmap.alarm_started));
                }
            });
        }
    }
}
