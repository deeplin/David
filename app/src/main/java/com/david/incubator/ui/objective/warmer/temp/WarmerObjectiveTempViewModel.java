package com.david.incubator.ui.objective.warmer.temp;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.dao.SystemSetting;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.mode.SystemMode;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;
import com.david.incubator.util.ViewUtil;

import java.util.Objects;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/5 16:48
 * email: 10525677@qq.com
 * description:
 */

public class WarmerObjectiveTempViewModel implements IViewModel {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    DaoControl daoControl;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean valueChanged = new ObservableBoolean();
    public ObservableField<String> valueField = new ObservableField<>();
    public ObservableBoolean select37 = new ObservableBoolean();

    public ObservableBoolean skinSelected = new ObservableBoolean();
    public ObservableBoolean manualSelected = new ObservableBoolean();
    public ObservableBoolean prewarmSelected = new ObservableBoolean();
    public ObservableBoolean selectOK = new ObservableBoolean();

    private final Observable.OnPropertyChangedCallback ctrlModeCallback;

    private final Observable.OnPropertyChangedCallback above37Callback;
    protected final SystemSetting systemSetting;

    private int value = Constant.SENSOR_NA_VALUE;
    private int upperLimit = Constant.SENSOR_NA_VALUE;
    private int lowerLimit = Constant.SENSOR_NA_VALUE;

    @Inject
    public WarmerObjectiveTempViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        systemSetting = daoControl.getSystemSetting();

        ctrlModeCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                CtrlMode status = ((ObservableField<CtrlMode>) observable).get();
                if (Objects.equals(status, CtrlMode.Skin)) {
                    skinSelected.set(true);
                    manualSelected.set(false);
                    prewarmSelected.set(false);
                    valueChanged.set(false);
                    value = shareMemory.skinObjective.get();
                    valueField.set(ViewUtil.formatTempValue(value));
                    upperLimit = systemSetting.getSkinUpper();
                    lowerLimit = systemSetting.getSkinLower();
                } else if (Objects.equals(status, CtrlMode.Manual)) {
                    skinSelected.set(false);
                    manualSelected.set(true);
                    prewarmSelected.set(false);
                    valueChanged.set(false);
                    value = shareMemory.manObjective.get();
                    valueField.set(String.valueOf(value));
                    upperLimit = 100;
                    lowerLimit = 0;
                } else if (Objects.equals(status, CtrlMode.Prewarm)) {
                    skinSelected.set(false);
                    manualSelected.set(false);
                    prewarmSelected.set(true);
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
        CtrlMode ctrlMode = shareMemory.ctrlMode.get();
        if (Objects.equals(ctrlMode, CtrlMode.Skin)) {
            if ((value > 0 && value < Constant.TEMP_370) ||
                    (value >= Constant.TEMP_370 && shareMemory.above37.get())) {
                if (value < upperLimit) {
                    valueChanged.set(true);
                    value++;
                    valueField.set(ViewUtil.formatTempValue(value));
                }
            }
        } else if (Objects.equals(ctrlMode, CtrlMode.Manual)) {
            if (value < upperLimit) {
                valueChanged.set(true);
                value = (value + 5) / 5 * 5;
                valueField.set(String.valueOf(value));
            }
        }
        selectOK.set(false);
    }

    public synchronized void decreaseValue() {
        CtrlMode ctrlMode = shareMemory.ctrlMode.get();
        if (Objects.equals(ctrlMode, CtrlMode.Skin)) {
            if ((value > 0) && (value > lowerLimit)) {
                valueChanged.set(true);
                value--;
                valueField.set(ViewUtil.formatTempValue(value));
                if (value <= Constant.TEMP_370) {
                    shareMemory.above37.set(false);
                }
            }
        } else if (Objects.equals(ctrlMode, CtrlMode.Manual)) {
            if ((value > 0) && (value > lowerLimit)) {
                valueChanged.set(true);
                value = (value - 5) / 5 * 5;
                valueField.set(String.valueOf(value));
            }
        }
        selectOK.set(false);
    }

    public void setEnable(CtrlMode ctrlMode) {
        messageSender.setCtrlMode(ctrlMode.getName(), (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                valueChanged.set(false);
                messageSender.getCtrlGet(shareMemory);
                selectOK.set(false);
                if (ctrlMode.equals(CtrlMode.Skin)) {
                    shareMemory.ctrlMode.set(CtrlMode.Skin);
                    skinSelected.set(true);
                    manualSelected.set(false);
                    prewarmSelected.set(false);
                } else if (ctrlMode.equals(CtrlMode.Manual)) {
                    shareMemory.ctrlMode.set(CtrlMode.Manual);
                    skinSelected.set(false);
                    manualSelected.set(true);
                    prewarmSelected.set(false);
                } else if (ctrlMode.equals(CtrlMode.Prewarm)) {
                    shareMemory.ctrlMode.set(CtrlMode.Prewarm);
                    skinSelected.set(false);
                    manualSelected.set(false);
                    prewarmSelected.set(true);
                }
            }
        });
    }

    public void setObjective() {
        CtrlMode ctrlMode = shareMemory.ctrlMode.get();
        if (Objects.equals(ctrlMode, CtrlMode.Skin) || Objects.equals(ctrlMode, CtrlMode.Manual))
            messageSender.setCtrlSet(SystemMode.Warmer.getName(), ctrlMode.getName(), value, (aBoolean, baseSerialMessage) -> {
                selectOK.set(aBoolean);
                if (aBoolean) {
                    valueChanged.set(false);
                    messageSender.getCtrlGet(shareMemory);
                }
            });
    }
}
