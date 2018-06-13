package com.david.common.control;

import com.apkfuns.logutils.LogUtils;
import com.david.common.dao.AnalogCommand;
import com.david.common.dao.Spo2GetCommand;
import com.david.common.dao.StatusCommand;
import com.david.common.dao.SystemSetting;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.mode.LanguageMode;
import com.david.common.mode.Spo2SensMode;
import com.david.common.serial.SerialControl;
import com.david.common.serial.command.other.LEDCommand;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;
import com.david.common.util.TimeUtil;
import com.david.incubator.ui.main.top.TopViewModel;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author: Ling Lin
 * created on: 2017/7/15 13:35
 * email: 10525677@qq.com
 * description: 自动控制类
 */

@Singleton
public class AutomationControl implements IViewModel {

    @Inject
    MessageSender messageSender;
    @Inject
    SerialControl serialControl;
    @Inject
    ShareMemory shareMemory;
    @Inject
    DaoControl daoControl;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;
    @Inject
    TopViewModel topViewModel;

    private Disposable ioDisposable;

    /*
     * 0: 屏幕解锁，开始计时
     * Constant.SCREEN_LOCK_SECOND: 时间到，自动锁屏
     * */
    private int lockTimeOut = 0;

    @Inject
    public AutomationControl() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        ioDisposable = null;
    }

    @Override
    public void attach() {
        /*读取配置文件*/
        messageSender.getHardwareModule((aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                moduleHardware.accept(true, baseSerialMessage);
                startRefresh();
            }
        });
    }

    private void startRefresh() {
        messageSender.setStandBy(false, true, null);

        /*读取传感器*/
        if (ioDisposable == null) {
            Observable<Long> observable = Observable.interval(1, 1, TimeUnit.SECONDS);
            ioDisposable = observable
                    .observeOn(Schedulers.io())
                    .subscribe((aLong) -> {
                        serialControl.refresh();
                        checkLockScreen();
                        long currentTime = TimeUtil.getCurrentTimeInSecond();
                        topViewModel.displayCurrentTime();
                        if (currentTime % 60 == 0) {
                            daoControl.deleteStale();
                        }
                    }, LogUtils::e);
        }

        SystemSetting sensorRange = daoControl.getSystemSetting();
        if (moduleHardware.is2000S()) {
            messageSender.setLanguage(LanguageMode.values()[sensorRange.getLanguageIndex()].getName(), null);
        }

        /*配置37度灯*/
        if (!moduleHardware.is93S()) {
            shareMemory.above37.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
                @Override
                public void onPropertyChanged(android.databinding.Observable observable, int i) {
                    messageSender.setLED(LEDCommand.LED37, shareMemory.above37.get(), null);
                }
            });
            shareMemory.above37.notifyChange();
        }

        /*如果Spo2开机时灵敏度设置是Max，需要改成normal*/
        if (moduleHardware.isSPO2()) {
            messageSender.getSpo2(true, (bBoolean, serialMessage) -> {
                if (bBoolean) {
                    Spo2GetCommand spo2GetCommand = (Spo2GetCommand) serialMessage;
                    Spo2SensMode spo2SensMode = Spo2SensMode.getMode(spo2GetCommand.getSens());
                    if (spo2SensMode != null && spo2SensMode.equals(Spo2SensMode.MAX)) {
                        messageSender.setSpo2(true, "SENS",
                                Spo2SensMode.Normal.getName(), null);
                    }
                }
            });
        }

        messageSender.getSoftwareModule(moduleSoftware);

        /*连续发送AnalogCommand*/
        AnalogCommand analogCommand = new AnalogCommand();
        analogCommand.setOnCompleted((aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                shareMemory.accept(true, analogCommand);
                daoControl.saveCommand(analogCommand);
            }
        });
        serialControl.addRepeatSession(analogCommand);

        /*连续发送StatusCommand*/
        StatusCommand statusCommand = new StatusCommand();
        statusCommand.setOnCompleted((aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                shareMemory.accept(true, statusCommand);
                daoControl.saveCommand(statusCommand);
            }
        });
        serialControl.addRepeatSession(statusCommand);
    }

    @Override
    public void detach() {
        if (ioDisposable != null) {
            ioDisposable.dispose();
            ioDisposable = null;
        }
    }

    public synchronized void initializeTimeOut() {
        lockTimeOut = 0;
    }

    /* 检测是否锁屏*/
    public synchronized void checkLockScreen() {
        /*锁屏不检测*/
        if (!shareMemory.lockScreen.get()) {
            /*刷新屏幕时间*/
            lockTimeOut++;
            if (lockTimeOut == Constant.SCREEN_LOCK_SECOND) {
                shareMemory.lockScreen.set(true);
            }
        } else {
            lockTimeOut++;
            if (lockTimeOut == Constant.SCREEN_LOCK_SECOND) {
                shareMemory.enableAlertList.set(false);
            }
        }
    }
}