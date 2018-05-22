package com.david.incubator.ui.main;

import android.databinding.Observable;

import com.david.common.control.AutomationControl;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.ui.BaseNavigatorModel;
import com.david.common.util.FragmentPage;
import com.david.common.util.TimeUtil;
import com.david.incubator.util.TimingData;

import java.util.Objects;

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
    AutomationControl automationControl;
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
                    shareMemory.currentFragmentID.set(FragmentPage.HOME_FRAGMENT);
                    shareMemory.lockScreen.set(false);
                    timingData.stop();
                } else if (shareMemory.isWarmer()) {
                    shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    shareMemory.lockScreen.set(false);
                } else if (shareMemory.isTransit()) {
                    shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    shareMemory.lockScreen.set(true);
                }
                automationControl.initializeTimeOut();
            }
        };

        lockScreenCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                //Status 是否锁屏
                boolean status = shareMemory.lockScreen.get();

                if (!navigator.isLockableFragment()) {
                    if (shareMemory.isCabin()) {
                        shareMemory.currentFragmentID.set(FragmentPage.HOME_FRAGMENT);
                    } else if (shareMemory.isWarmer()) {
                        shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
                    }
                } else {
                    CtrlMode ctrlMode = shareMemory.ctrlMode.get();
                    if (shareMemory.currentFragmentID.get() == FragmentPage.CHART_FRAGMENT) {
                        if (shareMemory.isWarmer() && (!Objects.equals(ctrlMode, CtrlMode.Skin))) {
                            shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
                        }
                    }
                }

                if (status) {
                    shareMemory.enableAlertList.set(false);
                } else {
                    automationControl.initializeTimeOut();
                }

                //刷新系统状态
                messageSender.getCtrlGet(shareMemory);
            }
        };

        currentFragmentIDCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                navigator.changeFragment(shareMemory.currentFragmentID.get());
            }
        };
    }

    @Override
    public void attach() {
        shareMemory.systemMode.addOnPropertyChangedCallback(systemModeCallback);
        shareMemory.lockScreen.addOnPropertyChangedCallback(lockScreenCallback);
        shareMemory.currentFragmentID.addOnPropertyChangedCallback(currentFragmentIDCallback);
    }

    @Override
    public void detach() {
        shareMemory.currentFragmentID.removeOnPropertyChangedCallback(currentFragmentIDCallback);
        shareMemory.lockScreen.removeOnPropertyChangedCallback(lockScreenCallback);
        shareMemory.systemMode.removeOnPropertyChangedCallback(systemModeCallback);
    }

    public void setLight(int brightness) {
        if (navigator != null)
            navigator.setLight(brightness);
    }
}
