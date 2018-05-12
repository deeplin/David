package com.david.incubator.ui.common;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.common.util.ResourceUtil;

/**
 * author: Ling Lin
 * created on: 2017/7/8 11:35
 * email: 10525677@qq.com
 * description: 主活动
 */

public class KeyValueViewModel {

    public ObservableField<String> buttonText = new ObservableField<>();

    public ObservableField<String> valueField = new ObservableField<>();

    public ObservableBoolean unitTextVisibility = new ObservableBoolean();

    public ObservableField<String> unitText = new ObservableField<>();

    public ObservableBoolean unitFigureVisibility = new ObservableBoolean();

    public ObservableInt unitFigure = new ObservableInt();

    public ObservableBoolean isSelected = new ObservableBoolean(false);

    public ObservableBoolean valueChanged = new ObservableBoolean(false);

    public KeyValueViewModel(int buttonText) {
        this.buttonText.set(ResourceUtil.getString(buttonText));
    }

    public void setValueField(String valueField) {
        this.valueField.set(valueField);
    }

    public void setUnitText(int unitText) {
        this.unitTextVisibility.set(true);
        this.unitFigureVisibility.set(false);
        this.unitText.set(ResourceUtil.getString(unitText));
    }

    public void setUnitText(String unitText) {
        this.unitTextVisibility.set(true);
        this.unitFigureVisibility.set(false);
        this.unitText.set(unitText);
    }

    public void setUnitFigure(int unitFigure) {
        this.unitTextVisibility.set(false);
        this.unitFigureVisibility.set(true);
        this.unitFigure.set(unitFigure);
    }
}
