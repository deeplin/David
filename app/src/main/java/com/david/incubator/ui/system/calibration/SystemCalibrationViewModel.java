package com.david.incubator.ui.system.calibration;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.serial.command.calibration.CalibrateTempCommand;
import com.david.common.serial.command.calibration.ShowCalibrationCommand;
import com.david.common.ui.IViewModel;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.david.incubator.ui.common.KeyValueViewModel;
import com.david.incubator.util.ViewUtil;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/4 20:08
 * email: 10525677@qq.com
 * description:
 */
public class SystemCalibrationViewModel implements IViewModel {

    private static final int TEMP_MAX = 50;

    @Inject
    MessageSender messageSender;
    @Inject
    ShareMemory shareMemory;

    public ObservableInt airValue = new ObservableInt();
    public ObservableInt isoAirValue = new ObservableInt();
    public ObservableInt skin1Value = new ObservableInt();
    public ObservableInt isoSkin1Value = new ObservableInt();
    public ObservableInt skin2Value = new ObservableInt();
    public ObservableInt airFlowValue = new ObservableInt();
    public ObservableInt humidityValue = new ObservableInt();
    public ObservableBoolean valueChanged = new ObservableBoolean(false);

    KeyValueViewModel airViewModel;
    KeyValueViewModel isoAirViewModel;
    KeyValueViewModel skin1ViewModel;
    KeyValueViewModel isoSkin1ViewModel;
    KeyValueViewModel skin2ViewModel;
    KeyValueViewModel airFlowViewModel;
    KeyValueViewModel humidityViewModel;
    ButtonControlViewModel buttonControlViewModel;

    private int offset;

    public SystemCalibrationViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        airViewModel = new KeyValueViewModel(R.string.air);
        airViewModel.setUnitFigure(R.mipmap.celsius);
        airViewModel.isSelected.set(true);

        isoAirViewModel = new KeyValueViewModel(R.string.iso_air);
        isoAirViewModel.setUnitFigure(R.mipmap.celsius);

        skin1ViewModel = new KeyValueViewModel(R.string.skin1);
        skin1ViewModel.setUnitFigure(R.mipmap.celsius);

        isoSkin1ViewModel = new KeyValueViewModel(R.string.iso_skin1);
        isoSkin1ViewModel.setUnitFigure(R.mipmap.celsius);

        skin2ViewModel = new KeyValueViewModel(R.string.skin2);
        skin2ViewModel.setUnitFigure(R.mipmap.celsius);

        airFlowViewModel = new KeyValueViewModel(R.string.air_flow);
        airFlowViewModel.setUnitFigure(R.mipmap.celsius);

        humidityViewModel = new KeyValueViewModel(R.string.humidity);
        humidityViewModel.setUnitFigure(R.mipmap.percentage);

        buttonControlViewModel = new ButtonControlViewModel();

        airValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                airViewModel.setValueField(ViewUtil.formatData(airValue.get()));
            }
        });

        isoAirValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                isoAirViewModel.setValueField(ViewUtil.formatData(isoAirValue.get()));
            }
        });

        skin1Value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                skin1ViewModel.setValueField(ViewUtil.formatData(skin1Value.get()));
            }
        });

        isoSkin1Value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                isoSkin1ViewModel.setValueField(ViewUtil.formatData(isoSkin1Value.get()));
            }
        });

        skin2Value.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                skin2ViewModel.setValueField(ViewUtil.formatData(skin2Value.get()));
            }
        });

        airFlowValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                airFlowViewModel.setValueField(ViewUtil.formatData(airFlowValue.get()));
            }
        });

        humidityValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                humidityViewModel.setValueField(ViewUtil.formatHumidityValue(humidityValue.get()));
            }
        });
    }

    @Override
    public void attach() {
        refresh();

        airValue.notifyChange();
        isoAirValue.notifyChange();
        skin1Value.notifyChange();
        isoSkin1Value.notifyChange();
        skin2Value.notifyChange();
        airFlowValue.notifyChange();
        humidityValue.notifyChange();
    }

    @Override
    public void detach() {
    }

    public void increaseValue() {
        if (airViewModel.isSelected.get()) {
            increaseAir();
        } else if (isoAirViewModel.isSelected.get()) {
            increaseIsoAir();
        } else if (skin1ViewModel.isSelected.get()) {
            increaseSkin1();
        } else if (isoSkin1ViewModel.isSelected.get()) {
            increaseIsoSkin1();
        } else if (skin2ViewModel.isSelected.get()) {
            increaseSkin2();
        } else if (airFlowViewModel.isSelected.get()) {
            increaseAirFlow();
        } else if (humidityViewModel.isSelected.get()) {
            increaseHumidity();
        }
    }

    public void decreaseValue() {
        if (airViewModel.isSelected.get()) {
            decreaseAir();
        } else if (isoAirViewModel.isSelected.get()) {
            decreaseIsoAir();
        } else if (skin1ViewModel.isSelected.get()) {
            decreaseSkin1();
        } else if (isoSkin1ViewModel.isSelected.get()) {
            decreaseIsoSkin1();
        } else if (skin2ViewModel.isSelected.get()) {
            decreaseSkin2();
        } else if (airFlowViewModel.isSelected.get()) {
            decreaseAirFlow();
        } else if (humidityViewModel.isSelected.get()) {
            decreaseHumidity();
        }
    }

    void increaseAir() {
        if (airValue.get() < TEMP_MAX) {
            offset++;
            airValue.set(airValue.get() + 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseAir() {
        if (airValue.get() > -TEMP_MAX) {
            offset--;
            airValue.set(airValue.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseIsoAir() {
        if (isoAirValue.get() < TEMP_MAX) {
            offset++;
            isoAirValue.set(isoAirValue.get() + 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseIsoAir() {
        if (isoAirValue.get() > -TEMP_MAX) {
            offset--;
            isoAirValue.set(isoAirValue.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseSkin1() {
        if (skin1Value.get() < TEMP_MAX) {
            offset++;
            skin1Value.set(skin1Value.get() + 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseSkin1() {
        if (skin1Value.get() > -TEMP_MAX) {
            offset--;
            skin1Value.set(skin1Value.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseIsoSkin1() {
        if (isoSkin1Value.get() < TEMP_MAX) {
            offset++;
            isoSkin1Value.set(isoSkin1Value.get() + 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseIsoSkin1() {
        if (isoSkin1Value.get() > -TEMP_MAX) {
            offset--;
            isoSkin1Value.set(isoSkin1Value.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseSkin2() {
        if (skin2Value.get() < TEMP_MAX) {
            offset++;
            skin2Value.set(skin2Value.get() + 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseSkin2() {
        if (skin2Value.get() > -TEMP_MAX) {
            offset--;
            skin2Value.set(skin2Value.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseAirFlow() {
        if (airFlowValue.get() < TEMP_MAX) {
            offset++;
            airFlowValue.set(airFlowValue.get() + 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseAirFlow() {
        if (airFlowValue.get() > -TEMP_MAX) {
            offset--;
            airFlowValue.set(airFlowValue.get() - 1);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void increaseHumidity() {
        if (humidityValue.get() < 1000) {
            humidityValue.set(humidityValue.get() + 10);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    void decreaseHumidity() {
        if (humidityValue.get() > 0) {
            humidityValue.set(humidityValue.get() - 10);
            valueChanged.set(true);
            buttonControlViewModel.okSelected.set(false);
        }
    }

    private void sendCommand(String sensor) {
        int count;
        String sign;
        if (offset > 0) {
            count = offset;
            sign = CalibrateTempCommand.PLUS;
        } else if (offset < 0) {
            count = -offset;
            sign = CalibrateTempCommand.MINUS;
        } else {
            return;
        }

        for (int index = 0; index < count; index++) {
            messageSender.setTempCalibration(sensor, sign, (aBoolean, baseSerialMessage) -> {
                if (aBoolean) {
                    refresh();
                    buttonControlViewModel.okSelected.set(true);
                }
            });
        }
    }

    public void setValue() {
        if (airViewModel.isSelected.get()) {
            setAir();
        } else if (isoAirViewModel.isSelected.get()) {
            setIsoAir();
        } else if (skin1ViewModel.isSelected.get()) {
            setSkin1();
        } else if (isoSkin1ViewModel.isSelected.get()) {
            setIsoSkin1();
        } else if (skin2ViewModel.isSelected.get()) {
            setSkin2();
        } else if (airFlowViewModel.isSelected.get()) {
            setAirFlow();
        } else if (humidityViewModel.isSelected.get()) {
            setHumidity();
        }
    }

    void setAir() {
        sendCommand("A1");
    }

    void setIsoAir() {
        sendCommand("A2");
    }

    void setSkin1() {
        sendCommand("S1A");
    }

    void setIsoSkin1() {
        sendCommand("S1B");
    }

    void setSkin2() {
        sendCommand("S2");
    }

    void setAirFlow() {
        sendCommand("F1");
    }

    void setHumidity() {
        messageSender.setHumCalibration(humidityValue.get(), (aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                buttonControlViewModel.okSelected.set(true);
            }
        });
    }

    public void refresh() {
        messageSender.getCalibration(
                (aBoolean, baseSerialMessage) -> {
                    if (aBoolean) {
                        valueChanged.set(false);

                        ShowCalibrationCommand showCalibrationCommand = (ShowCalibrationCommand) baseSerialMessage;
                        int air = showCalibrationCommand.getA1();
                        airValue.set(air);
                        int isoAir = showCalibrationCommand.getA2();
                        isoAirValue.set(isoAir);
                        int skin1 = showCalibrationCommand.getS1A();
                        skin1Value.set(skin1);
                        int isoSkin1 = showCalibrationCommand.getS1B();
                        isoSkin1Value.set(isoSkin1);
                        int skin2 = showCalibrationCommand.getS2();
                        skin2Value.set(skin2);
                        int airFlow = showCalibrationCommand.getF1();
                        airFlowValue.set(airFlow);

                        int humidity = shareMemory.H1.get() / 10 * 10;
                        humidityValue.set(humidity);

                        offset = 0;
                    }
                });
    }

    void selectAir() {
        airViewModel.isSelected.set(true);
        isoAirViewModel.isSelected.set(false);
        skin1ViewModel.isSelected.set(false);
        isoSkin1ViewModel.isSelected.set(false);
        skin2ViewModel.isSelected.set(false);
        airFlowViewModel.isSelected.set(false);
        humidityViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectIsoAir() {
        airViewModel.isSelected.set(false);
        isoAirViewModel.isSelected.set(true);
        skin1ViewModel.isSelected.set(false);
        isoSkin1ViewModel.isSelected.set(false);
        skin2ViewModel.isSelected.set(false);
        airFlowViewModel.isSelected.set(false);
        humidityViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectSkin1() {
        airViewModel.isSelected.set(false);
        isoAirViewModel.isSelected.set(false);
        skin1ViewModel.isSelected.set(true);
        isoSkin1ViewModel.isSelected.set(false);
        skin2ViewModel.isSelected.set(false);
        airFlowViewModel.isSelected.set(false);
        humidityViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectIsoSkin1() {
        airViewModel.isSelected.set(false);
        isoAirViewModel.isSelected.set(false);
        skin1ViewModel.isSelected.set(false);
        isoSkin1ViewModel.isSelected.set(true);
        skin2ViewModel.isSelected.set(false);
        airFlowViewModel.isSelected.set(false);
        humidityViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectSkin2() {
        airViewModel.isSelected.set(false);
        isoAirViewModel.isSelected.set(false);
        skin1ViewModel.isSelected.set(false);
        isoSkin1ViewModel.isSelected.set(false);
        skin2ViewModel.isSelected.set(true);
        airFlowViewModel.isSelected.set(false);
        humidityViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectAirFlow() {
        airViewModel.isSelected.set(false);
        isoAirViewModel.isSelected.set(false);
        skin1ViewModel.isSelected.set(false);
        isoSkin1ViewModel.isSelected.set(false);
        skin2ViewModel.isSelected.set(false);
        airFlowViewModel.isSelected.set(true);
        humidityViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectHumidity() {
        airViewModel.isSelected.set(false);
        isoAirViewModel.isSelected.set(false);
        skin1ViewModel.isSelected.set(false);
        isoSkin1ViewModel.isSelected.set(false);
        skin2ViewModel.isSelected.set(false);
        airFlowViewModel.isSelected.set(false);
        humidityViewModel.isSelected.set(true);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }
}
