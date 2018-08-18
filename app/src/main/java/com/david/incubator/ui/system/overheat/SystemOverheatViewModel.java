package com.david.incubator.ui.system.overheat;

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
 * created on: 2018/1/10 21:15
 * email: 10525677@qq.com
 * description:
 */

public class SystemOverheatViewModel implements IViewModel {

    @Inject
    MessageSender messageSender;

    private final int AIR_BELOW_37_TOP = 390;
    private final int AIR_BELOW_37_BOTTOM = 375;
    private final int AIR_ABOVE_37_TOP = 400;
    private final int AIR_ABOVE_37_BOTTOM = 385;
    private final int AIR_FLOW_BELOW_37_TOP = 700;
    private final int AIR_FLOW_BELOW_37_BOTTOM = 375;
    private final int AIR_FLOW_ABOVE_37_TOP = 700;
    private final int AIR_FLOW_ABOVE_37_BOTTOM = 385;
    private final int SKIN_TOP = 410;
    private final int SKIN_BOTTOM = 370;
    private final int FAN_TOP = 5100;
    private final int FAN_BOTTOM = 400;

    public ObservableInt airValueBelow37 = new ObservableInt();
    public ObservableInt airValueAbove37 = new ObservableInt();
    public ObservableInt airFlowValueBelow37 = new ObservableInt();
    public ObservableInt airFlowValueAbove37 = new ObservableInt();
    public ObservableInt skinValue = new ObservableInt();
    public ObservableInt fanSpeedValue = new ObservableInt();
    public ObservableBoolean valueChanged = new ObservableBoolean(false);

    KeyValueViewModel airBelow37ViewModel;
    KeyValueViewModel airAbove37ViewModel;
    KeyValueViewModel airFlowBelow37ViewModel;
    KeyValueViewModel airFlowAbove37ViewModel;
    KeyValueViewModel skinViewModel;
    KeyValueViewModel fanSpeedViewModel;
    ButtonControlViewModel buttonControlViewModel;

    public SystemOverheatViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        airBelow37ViewModel = new KeyValueViewModel(R.string.air);
        airBelow37ViewModel.setUnitFigure(R.mipmap.celsius);
        airBelow37ViewModel.isSelected.set(true);

        airAbove37ViewModel = new KeyValueViewModel(R.string.overheat_air_above_37);
        airAbove37ViewModel.setUnitFigure(R.mipmap.celsius);

        airFlowBelow37ViewModel = new KeyValueViewModel(R.string.air_flow);
        airFlowBelow37ViewModel.setUnitFigure(R.mipmap.celsius);

        airFlowAbove37ViewModel = new KeyValueViewModel(R.string.overheat_airflow_above_37);
        airFlowAbove37ViewModel.setUnitFigure(R.mipmap.celsius);

        skinViewModel = new KeyValueViewModel(R.string.skin);
        skinViewModel.setUnitFigure(R.mipmap.celsius);

        fanSpeedViewModel = new KeyValueViewModel(R.string.fan_speed);
        fanSpeedViewModel.setUnitText("RPM");

        buttonControlViewModel = new ButtonControlViewModel();

        airValueBelow37.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                airBelow37ViewModel.setValueField(ViewUtil.formatTempValue(airValueBelow37.get()));
            }
        });

        airValueAbove37.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                airAbove37ViewModel.setValueField(ViewUtil.formatTempValue(airValueAbove37.get()));
            }
        });

        airFlowValueBelow37.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                airFlowBelow37ViewModel.setValueField(ViewUtil.formatTempValue(airFlowValueBelow37.get()));
            }
        });

        airFlowValueAbove37.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                airFlowAbove37ViewModel.setValueField(ViewUtil.formatTempValue(airFlowValueAbove37.get()));
            }
        });

        skinValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                skinViewModel.setValueField(ViewUtil.formatTempValue(skinValue.get()));
            }
        });

        fanSpeedValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                fanSpeedViewModel.setValueField(String.valueOf(fanSpeedValue.get()));
            }
        });
    }

    @Override
    public void attach() {
        refresh();
    }

    @Override
    public void detach() {
    }

    public void increaseValue() {
        if (airBelow37ViewModel.isSelected.get()) {
            increaseAirBelow37();
        } else if (airAbove37ViewModel.isSelected.get()) {
            increaseAirAbove37();
        } else if (airFlowBelow37ViewModel.isSelected.get()) {
            increaseAirFlowBelow37();
        } else if (airFlowAbove37ViewModel.isSelected.get()) {
            increaseAirFlowAbove37();
        } else if (skinViewModel.isSelected.get()) {
            increaseSkin();
        } else if (fanSpeedViewModel.isSelected.get()) {
            increaseFanSpeed();
        }
    }

    public void decreaseValue() {
        if (airBelow37ViewModel.isSelected.get()) {
            decreaseAirBelow37();
        } else if (airAbove37ViewModel.isSelected.get()) {
            decreaseAirAbove37();
        } else if (airFlowBelow37ViewModel.isSelected.get()) {
            decreaseAirFlowBelow37();
        } else if (airFlowAbove37ViewModel.isSelected.get()) {
            decreaseAirFlowAbove37();
        } else if (skinViewModel.isSelected.get()) {
            decreaseSkin();
        } else if (fanSpeedViewModel.isSelected.get()) {
            decreaseFanSpeed();
        }
    }

    void increaseAirBelow37() {
        if (airValueBelow37.get() < AIR_BELOW_37_TOP) {
            airValueBelow37.set(airValueBelow37.get() + 1);
            valueChanged.set(true);

        }
    }

    void decreaseAirBelow37() {
        if (airValueBelow37.get() > AIR_BELOW_37_BOTTOM) {
            airValueBelow37.set(airValueBelow37.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseAirAbove37() {
        if (airValueAbove37.get() < AIR_ABOVE_37_TOP) {
            airValueAbove37.set(airValueAbove37.get() + 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseAirAbove37() {
        if (airValueAbove37.get() > AIR_ABOVE_37_BOTTOM) {
            airValueAbove37.set(airValueAbove37.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseAirFlowBelow37() {
        if (airFlowValueBelow37.get() < AIR_FLOW_BELOW_37_TOP) {
            airFlowValueBelow37.set(airFlowValueBelow37.get() + 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseAirFlowBelow37() {
        if (airFlowValueBelow37.get() > AIR_FLOW_BELOW_37_BOTTOM) {
            airFlowValueBelow37.set(airFlowValueBelow37.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseAirFlowAbove37() {
        if (airFlowValueAbove37.get() < AIR_FLOW_ABOVE_37_TOP) {
            airFlowValueAbove37.set(airFlowValueAbove37.get() + 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseAirFlowAbove37() {
        if (airFlowValueAbove37.get() > AIR_FLOW_ABOVE_37_BOTTOM) {
            airFlowValueAbove37.set(airFlowValueAbove37.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseSkin() {
        if (skinValue.get() < SKIN_TOP) {
            skinValue.set(skinValue.get() + 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseSkin() {
        if (skinValue.get() > SKIN_BOTTOM) {
            skinValue.set(skinValue.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseFanSpeed() {
        if (fanSpeedValue.get() < FAN_TOP) {
            fanSpeedValue.set(fanSpeedValue.get() + 10);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseFanSpeed() {
        if (fanSpeedValue.get() > FAN_BOTTOM) {
            fanSpeedValue.set(fanSpeedValue.get() - 10);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void setValue() {
        if (airBelow37ViewModel.isSelected.get()) {
            setAir();
        } else if (airAbove37ViewModel.isSelected.get()) {
            setAir();
        } else if (airFlowBelow37ViewModel.isSelected.get()) {
            setAirFlow();
        } else if (airFlowAbove37ViewModel.isSelected.get()) {
            setAirFlow();
        } else if (skinViewModel.isSelected.get()) {
            setSkin();
        } else if (fanSpeedViewModel.isSelected.get()) {
            setFanSpeed();
        }
    }

    void setAir() {
        messageSender.setAlarmConfig(AlarmSettingMode.AIR_OVH.getName(), airValueBelow37.get(),
                airValueAbove37.get(), (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        refresh();
                        buttonControlViewModel.okSelected.set(true);
                    }
                });
    }

    void setAirFlow() {
        messageSender.setAlarmConfig(AlarmSettingMode.FLOW_OVH.getName(), airFlowValueBelow37.get(),
                airFlowValueAbove37.get(), (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        refresh();
                        buttonControlViewModel.okSelected.set(true);
                    }
                });
    }

    void setSkin() {
        messageSender.setAlarmConfig(AlarmSettingMode.SKIN_OVH.getName(), skinValue.get(),
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        refresh();
                        buttonControlViewModel.okSelected.set(true);
                    }
                });
    }

    void setFanSpeed() {
        messageSender.setAlarmConfig(AlarmSettingMode.SYS_FAN.getName(), fanSpeedValue.get(),
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        refresh();
                    }
                });
    }


    public void refresh() {
        valueChanged.set(false);
        messageSender.getAlert(AlarmSettingMode.AIR_OVH,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                        int airBelow37 = alertGetCommand.getT1();
                        airValueBelow37.set(airBelow37);
                        int airAbove37 = alertGetCommand.getT2();
                        airValueAbove37.set(airAbove37);
                    }
                });
        messageSender.getAlert(AlarmSettingMode.FLOW_OVH,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                        int airFlowBelow37 = alertGetCommand.getT1();
                        airFlowValueBelow37.set(airFlowBelow37);
                        int airFlowAbove37 = alertGetCommand.getT2();
                        airFlowValueAbove37.set(airFlowAbove37);
                    }
                });
        messageSender.getAlert(AlarmSettingMode.SKIN_OVH,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                        int skin = alertGetCommand.getT();
                        skinValue.set(skin);
                    }
                });
        messageSender.getAlert(AlarmSettingMode.SYS_FAN,
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        AlertGetCommand alertGetCommand = (AlertGetCommand) baseSerialMessage;
                        int fanSpeed = alertGetCommand.getRPM();
                        fanSpeedValue.set(fanSpeed / 10 * 10);
                    }
                });
    }

    void selectAirBelow37() {
        airBelow37ViewModel.isSelected.set(true);
        airAbove37ViewModel.isSelected.set(false);
        airFlowBelow37ViewModel.isSelected.set(false);
        airFlowAbove37ViewModel.isSelected.set(false);
        skinViewModel.isSelected.set(false);
        fanSpeedViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }


    public void selectAirAbove37() {
        airBelow37ViewModel.isSelected.set(false);
        airAbove37ViewModel.isSelected.set(true);
        airFlowBelow37ViewModel.isSelected.set(false);
        airFlowAbove37ViewModel.isSelected.set(false);
        skinViewModel.isSelected.set(false);
        fanSpeedViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    public void selectAirFlowBelow37() {
        airBelow37ViewModel.isSelected.set(false);
        airAbove37ViewModel.isSelected.set(false);
        airFlowBelow37ViewModel.isSelected.set(true);
        airFlowAbove37ViewModel.isSelected.set(false);
        skinViewModel.isSelected.set(false);
        fanSpeedViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    public void selectAirFlowAbove37() {
        airBelow37ViewModel.isSelected.set(false);
        airAbove37ViewModel.isSelected.set(false);
        airFlowBelow37ViewModel.isSelected.set(false);
        airFlowAbove37ViewModel.isSelected.set(true);
        skinViewModel.isSelected.set(false);
        fanSpeedViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    public void selectSkin() {
        airBelow37ViewModel.isSelected.set(false);
        airAbove37ViewModel.isSelected.set(false);
        airFlowBelow37ViewModel.isSelected.set(false);
        airFlowAbove37ViewModel.isSelected.set(false);
        skinViewModel.isSelected.set(true);
        fanSpeedViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    public void selectFanSpeed() {
        airBelow37ViewModel.isSelected.set(false);
        airAbove37ViewModel.isSelected.set(false);
        airFlowBelow37ViewModel.isSelected.set(false);
        airFlowAbove37ViewModel.isSelected.set(false);
        skinViewModel.isSelected.set(false);
        fanSpeedViewModel.isSelected.set(true);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }
}
