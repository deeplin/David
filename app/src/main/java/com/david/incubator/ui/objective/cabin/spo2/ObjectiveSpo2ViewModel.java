package com.david.incubator.ui.objective.cabin.spo2;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.dao.SystemSetting;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.mode.AlarmSettingMode;
import com.david.common.mode.FunctionMode;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;
import com.david.incubator.ui.objective.cabin.BaseObjectiveViewModel;
import com.david.incubator.util.ViewUtil;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/30 14:59
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveSpo2ViewModel extends BaseObjectiveViewModel implements IViewModel {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    protected ModuleSoftware moduleSoftware;
    @Inject
    DaoControl daoControl;
    @Inject
    protected MessageSender messageSender;

    public ObservableBoolean isSpo2 = new ObservableBoolean(true);
    public ObservableBoolean valueChanged = new ObservableBoolean();
    public ObservableBoolean selectUpper = new ObservableBoolean(true);
    public ObservableField<String> upperValueField = new ObservableField<>();
    public ObservableField<String> lowerValueField = new ObservableField<>();
    protected int upperValue = Constant.SENSOR_NA_VALUE;
    protected int lowerValue = Constant.SENSOR_NA_VALUE;

    protected final Observable.OnPropertyChangedCallback enableCallback;
    protected Observable.OnPropertyChangedCallback upperValueCallback;
    protected Observable.OnPropertyChangedCallback lowerValueCallback;

    protected int upperTopLimit;
    protected int upperBottomLimit;
    protected int lowerTopLimit;
    protected int lowerBottomLimit;

    protected SystemSetting systemSetting;

    public ObjectiveSpo2ViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        systemSetting = daoControl.getSystemSetting();
        setLimit();

        enableCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                boolean enable = getEnable();
                selectFirst.set(enable);
                valueChanged.set(false);
                setValue();
            }
        };

        selectUpper.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                valueChanged.set(false);
                setValue();
            }
        });
    }

    @Override
    public synchronized void attach() {
        upperValueCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                upperValue = shareMemory.spo2UpperLimit.get();
                upperValueField.set(getValueString(upperValue));
            }
        };

        lowerValueCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                lowerValue = shareMemory.spo2LowerLimit.get();
                lowerValueField.set(getValueString(lowerValue));
            }
        };

        moduleSoftware.updated.addOnPropertyChangedCallback(enableCallback);
        shareMemory.spo2UpperLimit.addOnPropertyChangedCallback(upperValueCallback);
        shareMemory.spo2LowerLimit.addOnPropertyChangedCallback(lowerValueCallback);

        moduleSoftware.updated.notifyChange();
        shareMemory.spo2UpperLimit.notifyChange();
        shareMemory.spo2LowerLimit.notifyChange();
        selectUpper.notifyChange();
    }

    @Override
    public synchronized void detach() {
        shareMemory.spo2LowerLimit.removeOnPropertyChangedCallback(lowerValueCallback);
        shareMemory.spo2UpperLimit.removeOnPropertyChangedCallback(upperValueCallback);
        moduleSoftware.updated.removeOnPropertyChangedCallback(enableCallback);
    }

    protected boolean getEnable() {
        return moduleSoftware.isSPO2();
    }

    protected String getValueString(int value) {
        return ViewUtil.formatSpo2Value(value);
    }

    protected void setLimit() {
        upperTopLimit = systemSetting.getSpo2UpperTop();
        upperBottomLimit = systemSetting.getSpo2UpperBottom();
        lowerTopLimit = systemSetting.getSpo2LowerTop();
        lowerBottomLimit = systemSetting.getSpo2LowerBottom();
    }

    protected void setValue() {
        upperValue = shareMemory.spo2UpperLimit.get() / 10 * 10;
        lowerValue = shareMemory.spo2LowerLimit.get() / 10 * 10;
        upperValueField.set(getValueString(upperValue));
        lowerValueField.set(getValueString(lowerValue));
    }

    protected int getStep() {
        return 10;
    }

    public synchronized void increaseValue() {
        if (getEnable()) {
            valueChanged.set(true);
            if (selectUpper.get()) {
                if (upperValue < upperTopLimit) {
                    upperValue += getStep();
                    upperValueField.set(getValueString(upperValue));
                    selectOK.set(false);
                }
            } else {
                if ((lowerValue + getStep()) < upperValue && lowerValue < lowerTopLimit) {
                    lowerValue += getStep();
                    lowerValueField.set(getValueString(lowerValue));
                    selectOK.set(false);
                }
            }
        }
    }

    public synchronized void decreaseValue() {
        if (getEnable()) {
            valueChanged.set(true);
            if (selectUpper.get()) {
                if ((upperValue - getStep()) > lowerValue && upperValue > upperBottomLimit) {
                    upperValue -= getStep();
                    upperValueField.set(getValueString(upperValue));
                    selectOK.set(false);
                }
            } else {
                if (lowerValue > lowerBottomLimit) {
                    lowerValue -= getStep();
                    lowerValueField.set(getValueString(lowerValue));
                    selectOK.set(false);
                }
            }
        }
    }

    public void setEnable(boolean status) {
        messageSender.setModule(status, FunctionMode.Spo2.getName(),
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        valueChanged.set(false);
                        messageSender.getSoftwareModule(moduleSoftware);
                        selectOK.set(false);
                    }
                });
    }

    public void setObjective() {
        if (getEnable()) {
            if (selectUpper.get()) {
                messageSender.setAlarmConfig(AlarmSettingMode.SPO2_OVH.getName(), upperValue, (aBoolean, baseSerialMessage) -> {
                    selectOK.set(aBoolean);
                    if (aBoolean) {
                        valueChanged.set(false);
                        messageSender.getSpo2Alert(shareMemory);
                    }
                });
            } else {
                messageSender.setAlarmConfig(AlarmSettingMode.SPO2_OVL.getName(), lowerValue, (aBoolean, baseSerialMessage) -> {
                    selectOK.set(aBoolean);
                    if (aBoolean) {
                        valueChanged.set(false);
                        messageSender.getSpo2Alert(shareMemory);
                    }
                });
            }
        }
    }
}
