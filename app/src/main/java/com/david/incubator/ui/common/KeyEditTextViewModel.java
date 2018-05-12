package com.david.incubator.ui.common;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.common.util.ResourceUtil;

public class KeyEditTextViewModel {

    public ObservableField<String> buttonText = new ObservableField<>();

    public ObservableField<String> valueField = new ObservableField<>();

    public ObservableBoolean isSelected = new ObservableBoolean(false);

    public ObservableBoolean valueChanged = new ObservableBoolean(false);

    public ObservableInt maxLength = new ObservableInt(30);

    public KeyEditTextViewModel(int buttonText) {
        this.buttonText.set(ResourceUtil.getString(buttonText));
    }
}
