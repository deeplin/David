package com.david.incubator.ui.main;

import android.databinding.Observable;

import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.ui.BaseNavigatorModel;
import com.david.incubator.control.IncubatorAutomationControl;
import com.david.incubator.control.MainApplication;
import com.david.incubator.util.FragmentPage;
import com.david.incubator.util.TimingData;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:48
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class MainViewModel extends BaseNavigatorModel<MainNavigator> {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    MessageSender messageSender;
    @Inject
    IncubatorAutomationControl incubatorAutomationControl;
    @Inject
    TimingData timingData;

    private Observable.OnPropertyChangedCallback systemModeCallback;
    private Observable.OnPropertyChangedCallback lockScreenCallback;
    private Observable.OnPropertyChangedCallback currentFragmentIDCallback;

    @Inject
    public MainViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        systemModeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (shareMemory.isCabin()) {
                    shareMemory.currentFragmentId.set(FragmentPage.HOME_FRAGMENT);
                    shareMemory.lockScreen.set(false);
                    timingData.stop();
                } else if (shareMemory.isWarmer()) {
                    shareMemory.currentFragmentId.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    shareMemory.lockScreen.set(false);
                } else if (shareMemory.isTransit()) {
                    shareMemory.currentFragmentId.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    shareMemory.lockScreen.set(true);
                    timingData.stop();
                }
                incubatorAutomationControl.initializeTimeOut();
            }
        };

        lockScreenCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //Status 是否锁屏
                boolean status = shareMemory.lockScreen.get();

                if (!navigator.isLockableFragment()) {
                    if (shareMemory.isCabin()) {
                        shareMemory.currentFragmentId.set(FragmentPage.HOME_FRAGMENT);
                    } else if (shareMemory.isWarmer()) {
                        shareMemory.currentFragmentId.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    }
                }

                if (status) {
                    shareMemory.enableAlertList.set(false);
                } else {
                    incubatorAutomationControl.initializeTimeOut();
                }

                //刷新系统状态
                messageSender.getCtrlGet(shareMemory);
            }
        };

        currentFragmentIDCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                navigator.changeFragment(shareMemory.currentFragmentId.get());
            }
        };
    }

    @Override
    public void attach() {
        shareMemory.systemMode.addOnPropertyChangedCallback(systemModeCallback);
        shareMemory.lockScreen.addOnPropertyChangedCallback(lockScreenCallback);
        shareMemory.currentFragmentId.addOnPropertyChangedCallback(currentFragmentIDCallback);
    }

    @Override
    public void detach() {
        shareMemory.currentFragmentId.removeOnPropertyChangedCallback(currentFragmentIDCallback);
        shareMemory.lockScreen.removeOnPropertyChangedCallback(lockScreenCallback);
        shareMemory.systemMode.removeOnPropertyChangedCallback(systemModeCallback);
    }

    public void setLight(int brightness) {
        if (navigator != null)
            navigator.setLight(brightness);
    }
}