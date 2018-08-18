package com.david.incubator.ui.system.range;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.dao.SystemSetting;
import com.david.common.data.ShareMemory;
import com.david.common.ui.IViewModel;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.david.incubator.ui.common.KeyValueViewModel;
import com.david.incubator.util.ViewUtil;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/14 11:50
 * email: 10525677@qq.com
 * description:
 */
public class SystemRangeViewModel implements IViewModel {

    @Inject
    DaoControl daoControl;
    @Inject
    ShareMemory shareMemory;

    private final int AIR_BOTTOM_UPPER = 340;
    private final int AIR_BOTTOM_LOWER = 200;

    private final int AIR_TOP_UPPER = 390;
    private final int AIR_TOP_LOWER = 370;

    private final int SKIN_BOTTOM_UPPER = 340;
    private final int SKIN_BOTTOM_LOWER = 300;

    private final int SKIN_TOP_UPPER = 390;
    private final int SKIN_TOP_LOWER = 370;

    private final int HUMIDITY_BOTTOM_UPPER = 500;
    private final int HUMIDITY_BOTTOM_LOWER = 0;

    private final int HUMIDITY_TOP_UPPER = 990;
    private final int HUMIDITY_TOP_LOWER = 300;

    private final int OXYGEN_BOTTOM_UPPER = 300;
    private final int OXYGEN_BOTTOM_LOWER = 0;

    private final int OXYGEN_TOP_UPPER = 650;
    private final int OXYGEN_TOP_LOWER = 300;

    public ObservableInt airValue = new ObservableInt();
    public ObservableInt oxygenValue = new ObservableInt();
    public ObservableInt skinValue = new ObservableInt();
    public ObservableInt humidityValue = new ObservableInt();
    public ObservableBoolean upperSelected = new ObservableBoolean(true);
    public ObservableBoolean valueChanged = new ObservableBoolean(false);

    KeyValueViewModel airKeyValueViewModel;
    KeyValueViewModel skinKeyValueViewModel;
    KeyValueViewModel humidityKeyValueViewModel;
    KeyValueViewModel oxygenKeyValueViewModel;
    ButtonControlViewModel buttonControlViewModel;

    SystemSetting systemSetting;
    private Observable.OnPropertyChangedCallback upperSelectedCallback;

    public SystemRangeViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        systemSetting = daoControl.getSystemSetting();

        airKeyValueViewModel = new KeyValueViewModel(R.string.air);
        airKeyValueViewModel.setUnitFigure(R.mipmap.celsius);
        airKeyValueViewModel.isSelected.set(true);

        skinKeyValueViewModel = new KeyValueViewModel(R.string.skin);
        skinKeyValueViewModel.setUnitFigure(R.mipmap.celsius);

        humidityKeyValueViewModel = new KeyValueViewModel(R.string.humidity);
        humidityKeyValueViewModel.setUnitFigure(R.mipmap.percentage);

        oxygenKeyValueViewModel = new KeyValueViewModel(R.string.oxygen);
        oxygenKeyValueViewModel.setUnitFigure(R.mipmap.percentage);

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
                airKeyValueViewModel.setValueField(ViewUtil.formatTempValue(airValue.get()));
            }
        });

        skinValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                skinKeyValueViewModel.setValueField(ViewUtil.formatTempValue(skinValue.get()));
            }
        });

        humidityValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                humidityKeyValueViewModel.setValueField(ViewUtil.formatHumidityValue(humidityValue.get()));
            }
        });

        oxygenValue.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                oxygenKeyValueViewModel.setValueField(ViewUtil.formatOxygenValue(oxygenValue.get()));
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

    void refresh() {
        valueChanged.set(false);
        if (upperSelected.get()) {
            airValue.set(systemSetting.getAirUpper());
            skinValue.set(systemSetting.getSkinUpper());
            humidityValue.set(systemSetting.getHumidityUpper() / 10 * 10);
            oxygenValue.set(systemSetting.getOxygenUpper() / 10 * 10);
        } else {
            airValue.set(systemSetting.getAirLower());
            skinValue.set(systemSetting.getSkinLower());
            humidityValue.set(systemSetting.getHumidityLower() / 10 * 10);
            oxygenValue.set(systemSetting.getOxygenLower() / 10 * 10);
        }
    }

    public void increaseValue() {
        if (airKeyValueViewModel.isSelected.get()) {
            increaseAir();
        } else if (skinKeyValueViewModel.isSelected.get()) {
            increaseSkin();
        } else if (humidityKeyValueViewModel.isSelected.get()) {
            increaseHumidity();
        } else if (oxygenKeyValueViewModel.isSelected.get()) {
            increaseOxygen();
        }
    }

    public void decreaseValue() {
        if (airKeyValueViewModel.isSelected.get()) {
            decreaseAir();
        } else if (skinKeyValueViewModel.isSelected.get()) {
            decreaseSkin();
        } else if (humidityKeyValueViewModel.isSelected.get()) {
            decreaseHumidity();
        } else if (oxygenKeyValueViewModel.isSelected.get()) {
            decreaseOxygen();
        }
    }

    void increaseAir() {
        int value = airValue.get() + 1;
        if (upperSelected.get()) {
            if (value <= AIR_TOP_UPPER && value >= shareMemory.airObjective.get()) {
                airValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        } else {
            if (value <= AIR_BOTTOM_UPPER && value < systemSetting.getAirUpper() && value <= shareMemory.airObjective.get()) {
                airValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        }
    }

    void decreaseAir() {
        int value = airValue.get() - 1;
        if (upperSelected.get()) {
            if (value >= AIR_TOP_LOWER && value > systemSetting.getAirLower() && value >= shareMemory.airObjective.get()) {
                airValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        } else {
            if (value >= AIR_BOTTOM_LOWER && value <= shareMemory.airObjective.get()) {
                airValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        }
    }

    void increaseSkin() {
        int value = skinValue.get() + 1;
        if (upperSelected.get()) {
            if (value <= SKIN_TOP_UPPER && value >= shareMemory.skinObjective.get()) {
                skinValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        } else {
            if (value <= SKIN_BOTTOM_UPPER && value < systemSetting.getSkinUpper() && value <= shareMemory.skinObjective.get()) {
                skinValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        }
    }

    void decreaseSkin() {
        int value = skinValue.get() - 1;
        if (upperSelected.get()) {
            if (value >= SKIN_TOP_LOWER && value > systemSetting.getSkinLower() && value >= shareMemory.skinObjective.get()) {
                skinValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        } else {
            if (value >= SKIN_BOTTOM_LOWER && value <= shareMemory.skinObjective.get()) {
                skinValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        }
    }

    void increaseHumidity() {
        int value = humidityValue.get() + 10;
        if (upperSelected.get()) {
            if (value <= HUMIDITY_TOP_UPPER && value >= shareMemory.humidityObjective.get()) {
                humidityValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        } else {
            if (value <= HUMIDITY_BOTTOM_UPPER && value < systemSetting.getHumidityUpper() && value <= shareMemory.humidityObjective.get()) {
                humidityValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        }
    }

    void decreaseHumidity() {
        int value = humidityValue.get() - 10;
        if (upperSelected.get()) {
            if (value >= HUMIDITY_TOP_LOWER && value > systemSetting.getHumidityLower() && value >= shareMemory.humidityObjective.get()) {
                humidityValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        } else {
            if (value >= HUMIDITY_BOTTOM_LOWER && value <= shareMemory.humidityObjective.get()) {
                humidityValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        }
    }

    void increaseOxygen() {
        int value = oxygenValue.get() + 10;
        if (upperSelected.get()) {
            if (value <= OXYGEN_TOP_UPPER && value >= shareMemory.oxygenObjective.get()) {
                oxygenValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        } else {
            if (value <= OXYGEN_BOTTOM_UPPER && value < systemSetting.getOxygenUpper() && value <= shareMemory.oxygenObjective.get()) {
                oxygenValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        }
    }

    void decreaseOxygen() {
        int value = oxygenValue.get() - 10;
        if (upperSelected.get()) {
            if (value >= OXYGEN_TOP_LOWER && value > systemSetting.getOxygenLower() && value >= shareMemory.oxygenObjective.get()) {
                oxygenValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        } else {
            if (value >= OXYGEN_BOTTOM_LOWER && value <= shareMemory.oxygenObjective.get()) {
                oxygenValue.set(value);
                valueChanged.set(true);
                buttonControlViewModel.okSelected.set(false);
            }
        }
    }

    void save() {
        if (upperSelected.get()) {
            systemSetting.setAirUpper(airValue.get());
            systemSetting.setSkinUpper(skinValue.get());
            systemSetting.setHumidityUpper(humidityValue.get());
            systemSetting.setOxygenUpper(oxygenValue.get());
        } else {
            systemSetting.setAirLower(airValue.get());
            systemSetting.setSkinLower(skinValue.get());
            systemSetting.setHumidityLower(humidityValue.get());
            systemSetting.setOxygenLower(oxygenValue.get());
        }
        daoControl.saveSystemSetting(systemSetting);

        valueChanged.set(false);
        buttonControlViewModel.okSelected.set(true);
    }

    void selectAir() {
        airKeyValueViewModel.isSelected.set(true);
        skinKeyValueViewModel.isSelected.set(false);
        humidityKeyValueViewModel.isSelected.set(false);
        oxygenKeyValueViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectSkin() {
        airKeyValueViewModel.isSelected.set(false);
        skinKeyValueViewModel.isSelected.set(true);
        humidityKeyValueViewModel.isSelected.set(false);
        oxygenKeyValueViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectHumidity() {
        airKeyValueViewModel.isSelected.set(false);
        skinKeyValueViewModel.isSelected.set(false);
        humidityKeyValueViewModel.isSelected.set(true);
        oxygenKeyValueViewModel.isSelected.set(false);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }

    void selectOxygen() {
        airKeyValueViewModel.isSelected.set(false);
        skinKeyValueViewModel.isSelected.set(false);
        humidityKeyValueViewModel.isSelected.set(false);
        oxygenKeyValueViewModel.isSelected.set(true);
        buttonControlViewModel.okSelected.set(false);
        refresh();
    }
}
