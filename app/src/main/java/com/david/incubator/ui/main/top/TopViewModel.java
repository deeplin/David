package com.david.incubator.ui.main.top;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.common.ui.IViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopViewModel implements IViewModel{

    public ObservableBoolean alertVisibility = new ObservableBoolean(false);
    public ObservableField<String> alertMessage = new ObservableField<>();
    public ObservableBoolean above37Visibility = new ObservableBoolean(false);
    public ObservableField<String> userId = new ObservableField<>();
    public ObservableInt batteryImageId = new ObservableInt();
    public ObservableBoolean overheatExperimentMode = new ObservableBoolean(false);
    public ObservableField<String> dateTime = new ObservableField<>();

    @Inject
    public TopViewModel(){

    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }
}
