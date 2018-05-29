package com.david.incubator.ui.menu.sensorlist;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.util.AttributeSet;

import com.david.R;
import com.david.common.ui.BindingConstraintLayout;
import com.david.databinding.ViewSensorListSpo2Binding;

public class SensorListSpo2View extends BindingConstraintLayout<ViewSensorListSpo2Binding> {

    public ObservableBoolean showView = new ObservableBoolean(true);

    public ObservableField<String> value = new ObservableField<>();

    public ObservableField<String> upperLimit = new ObservableField<>();

    public ObservableField<String> lowerLimit = new ObservableField<>();

    public SensorListSpo2View(Context context, AttributeSet attrs) {
        super(context, attrs);
        binding.setViewModel(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_sensor_list_spo2;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }

    public void setBackground(int imageId) {
        binding.getRoot().setBackgroundResource(imageId);
    }
}
