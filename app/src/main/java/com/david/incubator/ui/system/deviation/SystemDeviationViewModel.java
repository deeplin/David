package com.david.incubator.ui.system.deviation;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.control.MessageSender;
import com.david.common.mode.AlarmSettingMode;
import com.david.common.serial.command.alert.AlertGetCommand;
import com.david.common.ui.IViewModel;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.david.incubator.ui.common.KeyValueViewModel;
import com.david.incubator.util.ViewUtil;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/3 22:12
 * email: 10525677@qq.com
 * description:
 */

public class SystemDeviationViewModel implements IViewModel {

    @Inject
    MessageSender messageSender;

    private final int MAX_AIR_OFFSET = 50;
    private final int MAX_SKIN_OFFSET = 50;
    private final int MAX_OXYGEN_OFFSET = 100;
    private final int MAX_HUMIDITY_OFFSET = 200;

    public ObservableInt airValue = new ObservableInt();
    public ObservableInt oxygenValue = new ObservableInt();
    public ObservableInt skinValue = new ObservableInt();
    public ObservableInt humidityValue = new ObservableInt();
    public ObservableBoolean valueChanged = new ObservableBoolean(false);
    public ObservableBoolean upperSelected = new ObservableBoolean(true);

    KeyValueViewModel airViewModel;
    KeyValueViewModel skinViewModel;
    KeyValueViewModel humidityViewModel;
    KeyValueViewModel oxygenViewModel;
    ButtonControlViewModel buttonControlViewModel;

    private Observable.OnPropertyChangedCallback upperSelectedCallback;

    public SystemDeviationViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        airViewModel = new KeyValueViewModel(R.string.air);
        airViewModel.setUnitFigure(R.mipmap.celsius);
        airViewModel.isSelected.set(true);

        skinViewModel = new KeyValueViewModel(R.string.skin);
        skinViewModel.setUnitFigure(R.mipmap.celsius);

        humidityViewModel = new KeyValueViewModel(R.string.humidity);
        humidityViewModel.setUnitFigure(R.mipmap.percentage);

        oxygenViewModel = new KeyValueViewModel(R.string.oxygen);
        oxygenViewModel.setUnitFigure(R.mipmap.percentage);

        buttonControlViewModel = new ButtonControlViewModel();

        upperSelectedCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                buttonControlViewModel.okSelected.set(false);
                refresh();
            }
        };

        airValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                airViewModel.setValueField(ViewUtil.formatData(airValue.get()));
            }
        });

        skinValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                skinViewModel.setValueField(ViewUtil.formatData(skinValue.get()));
            }
        });

        humidityValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                humidityViewModel.setValueField(ViewUtil.formatData(humidityValue.get()));
            }
        });

        oxygenValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                oxygenViewModel.setValueField(ViewUtil.formatData(oxygenValue.get()));
            }
        });
    }

    @Override
    public void attach() {
        upperSelected.addOnPropertyChangedCallback(upperSelectedCallback);
        upperSelected.notifyChange();
    }

    @Override
    public void detach() {
        upperSelected.removeOnPropertyChangedCallback(upperSelectedCallback);
    }

    public void setValue() {
        if (airViewModel.isSelected.get()) {
            setTemp();
        } else if (skinViewModel.isSelected.get()) {
            setTemp();
        } else if (humidityViewModel.isSelected.get()) {
            setHum();
        } else if (oxygenViewModel.isSelected.get()) {
            setO2();
        }
    }

    private void setTemp() {
        if (upperSelected.get()) {
            messageSender.setAlarmConfig(AlarmSettingMode.TEMP_DEVH.getName(), airValue.get(), skinValue.get(),
                    (aBoolean, baseSerialMessage) -> {
                        if (aBoolean) {
                            getTempDevh();
                            valueChanged.set(false);
                            buttonControlViewModel.okSelected.set(true);
                        }
                    });
        } else {
            messageSender.setAlarmConfig(AlarmSettingMode.TEMP_DEVL.getName(), airValue.get(), skinValue.get(),
                    (aBoolean, baseSerialMessage) -> {
                        if (aBoolean) {
                            getTempDevl();
                            valueChanged.set(false);
                            buttonControlViewModel.okSelected.set(true);
                        }
                    });
        }
    }

    private void setHum() {
        if (upperSelected.get()) {
            messageSender.setAlarmConfig(AlarmSettingMode.HUM_DEVH.getName(), humidityValue.get(),
                    (aBoolean, baseSerialMessage) -> {
                        if (aBoolean) {
                            getHumDevh();
                            valueChanged.set(false);
                            buttonControlViewModel.okSelected.set(true);
                        }
                    });
        } else {
            messageSender.setAlarmConfig(AlarmSettingMode.HUM_DEVL.getName(), humidityValue.get(),
                    (aBoolean, baseSerialMessage) -> {
                        if (aBoolean) {
                            getHumDevl();
                            valueChanged.set(false);
                            buttonControlViewModel.okSelected.set(true);
                        }
                    });
        }
    }

    private void setO2() {
        if (upperSelected.get()) {
            messageSender.setAlarmConfig(AlarmSettingMode.O2_DEVH.getName(), oxygenValue.get(),
                    (aBoolean, baseSerialMessage) -> {
                        if (aBoolean) {
                            getO2Devh();
                            valueChanged.set(false);
                            buttonControlViewModel.okSelected.set(true);
                        }
                    });
        } else {
            messageSender.setAlarmConfig(AlarmSettingMode.O2_DEVL.getName(), oxygenValue.get(),
                    (aBoolean, baseSerialMessage) -> {
                        if (aBoolean) {
                            getO2Devl();
                            valueChanged.set(false);
                            buttonControlViewModel.okSelected.set(true);
                        }
                    });
        }
    }

    private void refresh() {
        valueChanged.set(false);
        if (upperSelected.get()) {
            getTempDevh();
            getHumDevh();
            getO2Devh();
        } else {
            getTempDevl();
            getHumDevl();
            getO2Devl();
        }
    }

    private void getTempDevh() {
        messageSender.getAlert(AlarmSettingMode.TEMP_DEVH, (aBoolean, baseSerialMessage) -> {
            if (aBoolean && upperSelected.get()) {
                AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                airValue.set(alertGetCommand.getADevH());
                skinValue.set(alertGetCommand.getSDevH());
            }
        });
    }

    private void getTempDevl() {
        messageSender.getAlert(AlarmSettingMode.TEMP_DEVL, (aBoolean, baseSerialMessage) -> {
            if (aBoolean && (!upperSelected.get())) {
                AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                airValue.set(alertGetCommand.getADevL());
                skinValue.set(alertGetCommand.getSDevL());
            }
        });
    }

    private void getO2Devh() {
        messageSender.getAlert(AlarmSettingMode.O2_DEVH, (aBoolean, baseSerialMessage) -> {
            if (aBoolean && upperSelected.get()) {
                AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                oxygenValue.set(alertGetCommand.getODevH());
            }
        });
    }

    private void getO2Devl() {
        messageSender.getAlert(AlarmSettingMode.O2_DEVL, (aBoolean, baseSerialMessage) -> {
            if (aBoolean && (!upperSelected.get())) {
                AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                oxygenValue.set(alertGetCommand.getODevL());
            }
        });
    }

    private void getHumDevh() {
        messageSender.getAlert(AlarmSettingMode.HUM_DEVH, (aBoolean, baseSerialMessage) -> {
            if (aBoolean && upperSelected.get()) {
                AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                humidityValue.set(alertGetCommand.getHDevH());
            }
        });
    }

    private void getHumDevl() {
        messageSender.getAlert(AlarmSettingMode.HUM_DEVL, (aBoolean, baseSerialMessage) -> {
            if (aBoolean && (!upperSelected.get())) {
                AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                humidityValue.set(alertGetCommand.getHDevL());
            }
        });
    }

    public void increaseValue() {
        if (airViewModel.isSelected.get()) {
            increaseAir();
        } else if (skinViewModel.isSelected.get()) {
            increaseSkin();
        } else if (humidityViewModel.isSelected.get()) {
            increaseHumidity();
        } else if (oxygenViewModel.isSelected.get()) {
            increaseOxygen();
        }
    }

    public void decreaseValue() {
        if (airViewModel.isSelected.get()) {
            decreaseAir();
        } else if (skinViewModel.isSelected.get()) {
            decreaseSkin();
        } else if (humidityViewModel.isSelected.get()) {
            decreaseHumidity();
        } else if (oxygenViewModel.isSelected.get()) {
            decreaseOxygen();
        }
    }

    void increaseAir() {
        int value = airValue.get() + 1;
        if (value <= MAX_AIR_OFFSET) {
            airValue.set(value);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseAir() {
        int value = airValue.get() - 1;
        if (value >= 0) {
            airValue.set(value);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseOxygen() {
        int value = oxygenValue.get() + 1;
        if (value <= MAX_OXYGEN_OFFSET) {
            oxygenValue.set(value);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseOxygen() {
        int value = oxygenValue.get() - 1;
        if (value >= 0) {
            oxygenValue.set(value);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseSkin() {
        int value = skinValue.get() + 1;
        if (value <= MAX_SKIN_OFFSET) {
            skinValue.set(value);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseSkin() {
        int value = skinValue.get() - 1;
        if (value >= 0) {
            skinValue.set(value);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseHumidity() {
        int value = humidityValue.get() + 10;
        if (value <= MAX_HUMIDITY_OFFSET) {
            humidityValue.set(value);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseHumidity() {
        int value = humidityValue.get() - 10;
        if (value >= 0) {
            humidityValue.set(value);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void selectAir() {
        airViewModel.isSelected.set(true);
        skinViewModel.isSelected.set(false);
        humidityViewModel.isSelected.set(false);
        oxygenViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectSkin() {
        airViewModel.isSelected.set(false);
        skinViewModel.isSelected.set(true);
        humidityViewModel.isSelected.set(false);
        oxygenViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectHumidity() {
        airViewModel.isSelected.set(false);
        skinViewModel.isSelected.set(false);
        humidityViewModel.isSelected.set(true);
        oxygenViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectOxygen() {
        airViewModel.isSelected.set(false);
        skinViewModel.isSelected.set(false);
        humidityViewModel.isSelected.set(false);
        oxygenViewModel.isSelected.set(true);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }
}
