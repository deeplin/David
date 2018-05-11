package com.david.incubator.ui.main.side;

import android.databinding.Observable;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.ui.IViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:53
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class SideViewModel implements IViewModel {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    MessageSender messageSender;

    public ObservableInt lockScreenImage = new ObservableInt(R.mipmap.screen_unlocked);

    private Observable.OnPropertyChangedCallback lockScreenCallback;

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
    }

    @Override
    public void attach() {
        shareMemory.lockScreen.addOnPropertyChangedCallback(lockScreenCallback);
    }

    @Override
    public void detach() {
        shareMemory.lockScreen.removeOnPropertyChangedCallback(lockScreenCallback);
    }

    void clearAlarm() {
        messageSender.clearAlarm();
    }
}