package com.david.incubator.ui.main;

import android.databinding.Observable;

import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.ui.BaseNavigatorModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.disposables.Disposable;

/**
 * author: Ling Lin
 * created on: 2017/12/26 18:48
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class MainViewModel extends BaseNavigatorModel<MainNavigator> {

    //    @Inject
//    SideViewModel sideViewModel;
//    @Inject
//    MenuViewModel menuViewModel;
    @Inject
    public ShareMemory shareMemory;
    //    @Inject
//    AlarmControl alarmControl;
    @Inject
    MessageSender messageSender;

    private Observable.OnPropertyChangedCallback systemModeCallback;
    private Observable.OnPropertyChangedCallback lockScreenCallback;
    private Observable.OnPropertyChangedCallback currentFragmentIDCallback;

    /*
     * 0: 屏幕解锁，开始计时
     * Constant.SCREEN_LOCK_SECOND: 时间到，自动锁屏
     * */
    private int lockTimeOut = 0;

    private Disposable uiDisposable;

    @Inject
    public MainViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

//        systemModeCallback = new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                SystemMode system = shareMemory.systemMode.get();
//                if (system.equals(SystemMode.Cabin)) {
//                    shareMemory.currentFragmentID.set(FragmentPage.HOME_FRAGMENT);
//                    shareMemory.lockScreen.set(false);
//                } else if (system.equals(SystemMode.Warmer)) {
//                    shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
//                    shareMemory.lockScreen.set(false);
//                } else if (system.equals(SystemMode.Transit)) {
//                    shareMemory.currentFragmentID.set(FragmentPage.MENU_NONE);
//                    shareMemory.lockScreen.set(true);
//                }
//                initializeTimeOut();
//            }
//        };
//
//        lockScreenCallback = new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                //Status 是否锁屏
//                boolean status = ((ObservableBoolean) observable).get();
//                sideViewModel.setLockScreen(status);
//                menuViewModel.setScreenLock(status);
//
//                if (!navigator.isLockableFragment()) {
//                    if (shareMemory.isCabin()) {
//                        shareMemory.currentFragmentID.set(FragmentPage.HOME_FRAGMENT);
//                    } else if (shareMemory.isWarmer()) {
//                        shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
//                    }
//                } else {
//                    CtrlMode ctrlMode = shareMemory.ctrlMode.get();
//                    if (shareMemory.currentFragmentID.get() == FragmentPage.CHART_FRAGMENT) {
//                        if (shareMemory.isWarmer() && (!Objects.equals(ctrlMode, CtrlMode.Skin))) {
//                            shareMemory.currentFragmentID.set(FragmentPage.WARMER_HOME_FRAGMENT);
//                        }
//                    }
//                }
//
//                if (!status) {
//                    //取消报警
//                    alarmControl.clearAlert();
//                    //重新计数
//                    initializeTimeOut();
//                }
//
//                //改变standby模式
//                messageSender.setStandBy(status);
//
//                //刷新系统状态
//                messageSender.getCtrlGet(shareMemory);
//            }
//        };
//
//        currentFragmentIDCallback = new Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(Observable observable, int i) {
//                byte position = ((ObservableByte) observable).get();
//                navigator.changeFragment(position);
//            }
//        };
    }

    @Override
    public void attach() {
//        shareMemory.systemMode.addOnPropertyChangedCallback(systemModeCallback);
//        shareMemory.lockScreen.addOnPropertyChangedCallback(lockScreenCallback);
//        shareMemory.currentFragmentID.addOnPropertyChangedCallback(currentFragmentIDCallback);
//        //to be removed
//        shareMemory.currentFragmentID.notifyChange();
//
//        /*刷新屏幕信息*/
//        io.reactivex.Observable<Long> observable = io.reactivex.Observable.interval(1, 1, TimeUnit.SECONDS);
//        if (uiDisposable == null) {
//            uiDisposable = observable
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe((aLong) -> {
//                        checkScreenLock();
//                        sideViewModel.displayCurrentTime();
//                    });
//        }
    }

    @Override
    public void detach() {
        if (uiDisposable != null) {
            uiDisposable.dispose();
            uiDisposable = null;
        }
//        shareMemory.currentFragmentID.removeOnPropertyChangedCallback(currentFragmentIDCallback);
//        shareMemory.lockScreen.removeOnPropertyChangedCallback(lockScreenCallback);
//        shareMemory.systemMode.removeOnPropertyChangedCallback(systemModeCallback);
    }

//    public void switchToMonitor(){
//        navigator.switchToMonitor();
//    }
//

//    /* 检测是否锁屏*/
//    public synchronized void checkScreenLock() {
//        /*锁屏不检测*/
//        if (shareMemory.lockScreen.get()) {
//            return;
//        }
//        /*刷新屏幕时间*/
//        lockTimeOut++;
//        if (lockTimeOut >= SystemConfig.SCREEN_LOCK_SECOND) {
//            shareMemory.lockScreen.set(true);
//        }
//    }
//
//    void initializeTimeOut() {
//        lockTimeOut = 0;
//    }
//
//    public void setLight(int brightness) {
//        if (navigator != null)
//            navigator.setLight(brightness);
//    }
}
