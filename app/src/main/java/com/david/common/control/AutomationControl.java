package com.david.common.control;

import com.apkfuns.logutils.LogUtils;
import com.david.common.dao.AnalogCommand;
import com.david.common.dao.Spo2GetCommand;
import com.david.common.dao.StatusCommand;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.mode.Spo2SensMode;
import com.david.common.serial.BaseSerialMessage;
import com.david.common.serial.SerialControl;
import com.david.common.serial.command.LEDCommand;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
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

    private Disposable ioDisposable;
    private Disposable staleDisposable;

    @Inject
    public AutomationControl() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        ioDisposable = null;
        staleDisposable = null;
    }

    @Override
    public void attach() {
        /*读取配置文件*/
        messageSender.getHardwareModule((aBoolean, baseSerialMessage) -> {
            if(aBoolean){
                moduleHardware.accept(true, baseSerialMessage);
                /*配置37度灯*/
                if (moduleHardware.showLED37()) {
                    shareMemory.above37.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
                        @Override
                        public void onPropertyChanged(android.databinding.Observable observable, int i) {
                            messageSender.setLED(LEDCommand.LED37, shareMemory.above37.get(), null);
                        }
                    });
                    shareMemory.above37.notifyChange();
                }

                /*如果Spo2开机时灵敏度设置是Max，需要改成normal*/
                if(moduleHardware.isSPO2()) {
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
            }
        });

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

        //todo
        if (Constant.RELEASE_TO_DAVID) {
            /*读取传感器*/
            if (ioDisposable == null) {
                Observable<Long> observable = Observable.interval(1, 1, TimeUnit.SECONDS);
                ioDisposable = observable
                        .observeOn(Schedulers.io())
                        .subscribe((aLong) -> serialControl.refresh(), LogUtils::e);
            }

            if (staleDisposable == null) {
                Observable<Long> staleObservable = Observable.interval(1, 1, TimeUnit.MINUTES);
                staleDisposable = staleObservable
                        .observeOn(Schedulers.io())
                        .subscribe((aLong) -> daoControl.deleteStale(), LogUtils::e);
            }
        }
    }

    @Override
    public void detach() {
        if (ioDisposable != null) {
            ioDisposable.dispose();
            ioDisposable = null;
        }
        if (staleDisposable != null) {
            staleDisposable.dispose();
            staleDisposable = null;
        }
    }
}