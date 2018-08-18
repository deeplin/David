package com.david.incubator.ui.objective.cabin.humidity;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.david.common.control.DaoControl;
import com.david.common.control.MessageSender;
import com.david.common.dao.SystemSetting;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.mode.FunctionMode;
import com.david.common.mode.SystemMode;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.objective.cabin.BaseObjectiveViewModel;
import com.david.incubator.util.ViewUtil;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/30 12:08
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveHumidityViewModel extends BaseObjectiveViewModel implements IViewModel {

    @Inject
    public ShareMemory shareMemory;
    @Inject
    protected ModuleSoftware moduleSoftware;
    @Inject
    DaoControl daoControl;
    @Inject
    MessageSender messageSender;

    public ObservableBoolean valueChanged = new ObservableBoolean();
    public ObservableField<String> valueField = new ObservableField<>();
    protected int value = Constant.SENSOR_NA_VALUE;

    private final Observable.OnPropertyChangedCallback enableCallback;

    protected final SystemSetting systemSetting;

    protected int upperLimit = Constant.SENSOR_NA_VALUE;
    protected int lowerLimit = Constant.SENSOR_NA_VALUE;

    public ObjectiveHumidityViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        systemSetting = daoControl.getSystemSetting();
        setLimit();

        enableCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                boolean enable = getEnable();
                selectFirst.set(enable);
                valueChanged.set(false);
                value = getValue();
                valueField.set(getValueString());
            }
        };
    }

    @Override
    public synchronized void attach() {
        moduleSoftware.updated.addOnPropertyChangedCallback(enableCallback);
        moduleSoftware.updated.notifyChange();
    }

    @Override
    public synchronized void detach() {
        moduleSoftware.updated.removeOnPropertyChangedCallback(enableCallback);
    }

    protected boolean getEnable() {
        return moduleSoftware.isHUM();
    }

    protected int getValue() {
        return shareMemory.humidityObjective.get();
    }

    protected void setLimit() {
        upperLimit = systemSetting.getHumidityUpper();
        lowerLimit = systemSetting.getHumidityLower();
    }

    protected String getFunctionName() {
        return FunctionMode.Humidity.getName();
    }

    protected String getValueString() {
        return ViewUtil.formatHumidityValue(value);
    }

    public synchronized void increaseValue() {
        if (getEnable()) {
            if (value < upperLimit) {
                valueChanged.set(true);
                value += 10;
                valueField.set(getValueString());

                selectOK.set(false);
            }
        }
    }

    public synchronized void decreaseValue() {
        if (getEnable()) {
            if (value > lowerLimit) {
                valueChanged.set(true);
                value -= 10;
                valueField.set(getValueString());

                selectOK.set(false);
            }
        }
    }

    public void setEnable(boolean status) {
        messageSender.setModule(status, getFunctionName(),
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
            messageSender.setCtrlSet(SystemMode.Cabin.getName(), getFunctionName(), value, (aBoolean, baseSerialMessage) -> {
                selectOK.set(aBoolean);
                if (aBoolean) {
                    valueChanged.set(false);
                    messageSender.getCtrlGet(shareMemory);
                }
            });
        }
    }
}
