package com.david.incubator.ui.objective.cabin.temp;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.david.common.control.DaoControl;
import com.david.common.control.MessageSender;
import com.david.common.dao.SystemSetting;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.mode.SystemMode;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.objective.cabin.BaseObjectiveViewModel;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/29 19:25
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveTempViewModel extends BaseObjectiveViewModel implements IViewModel {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    DaoControl daoControl;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean valueChanged = new ObservableBoolean();
    public ObservableInt valueField = new ObservableInt();
    public ObservableBoolean select37 = new ObservableBoolean();

    private final Observable.OnPropertyChangedCallback ctrlModeCallback;

    private final Observable.OnPropertyChangedCallback above37Callback;
    protected final SystemSetting systemSetting;

    private int upperLimit = Constant.SENSOR_NA_VALUE;
    private int lowerLimit = Constant.SENSOR_NA_VALUE;

    @Inject
    public ObjectiveTempViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        systemSetting = daoControl.getSystemSetting();

        ctrlModeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (shareMemory.isAir()) {
                    selectFirst.set(true);
                    valueChanged.set(false);
                    valueField.set(shareMemory.airObjective.get());
                    upperLimit = systemSetting.getAirUpper();
                    lowerLimit = systemSetting.getAirLower();
                } else if (shareMemory.isSkin()) {
                    selectFirst.set(false);
                    valueChanged.set(false);
                    valueField.set(shareMemory.skinObjective.get());
                    upperLimit = systemSetting.getSkinUpper();
                    lowerLimit = systemSetting.getSkinLower();
                }
            }
        };

        above37Callback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                boolean status = ((ObservableBoolean) observable).get();
                select37.set(status);
            }
        };
    }

    @Override
    public synchronized void attach() {
        shareMemory.ctrlMode.addOnPropertyChangedCallback(ctrlModeCallback);
        shareMemory.above37.addOnPropertyChangedCallback(above37Callback);

        shareMemory.ctrlMode.notifyChange();
        shareMemory.above37.notifyChange();
    }

    @Override
    public synchronized void detach() {
        shareMemory.above37.removeOnPropertyChangedCallback(above37Callback);
        shareMemory.ctrlMode.removeOnPropertyChangedCallback(ctrlModeCallback);
    }

    public synchronized void increaseValue() {
        int value = valueField.get();
        if ((value > 0 && value < Constant.TEMP_370) ||
                (value >= Constant.TEMP_370 && shareMemory.above37.get())) {
            if (value < upperLimit) {
                valueChanged.set(true);
                valueField.set(value + 1);
            }
            selectOK.set(false);
        }
    }

    public synchronized void decreaseValue() {
        int value = valueField.get();
        if ((value > 0) && (value > lowerLimit)) {
            valueChanged.set(true);
            value--;
            valueField.set(value);
            if (value <= Constant.TEMP_370) {
                shareMemory.above37.set(false);
            }
            selectOK.set(false);
        }
    }

    public void setEnable(CtrlMode ctrlMode) {
        messageSender.setCtrlMode(ctrlMode.getName(), (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                valueChanged.set(false);
                messageSender.getCtrlGet(shareMemory);
                shareMemory.ctrlMode.set(ctrlMode);
                selectOK.set(false);
                selectFirst.set(ctrlMode.equals(CtrlMode.Air));
            }
        });
    }

    public void setObjective() {
        CtrlMode ctrlMode = shareMemory.ctrlMode.get();
        messageSender.setCtrlSet(SystemMode.Cabin.getName(), ctrlMode.getName(), valueField.get(), (aBoolean, baseSerialMessage) -> {
            selectOK.set(aBoolean);
            if (aBoolean) {
                valueChanged.set(false);
                messageSender.getCtrlGet(shareMemory);
            }
        });
    }
}