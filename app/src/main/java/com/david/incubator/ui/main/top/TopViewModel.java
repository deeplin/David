package com.david.incubator.ui.main.top;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.common.control.AlertControl;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.data.ShareMemory;
import com.david.common.mode.BatteryMode;
import com.david.common.ui.IViewModel;
import com.david.common.util.TimeUtil;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopViewModel implements IViewModel{

    @Inject
    AlertControl alertControl;
    @Inject
    public ShareMemory shareMemory;
    @Inject
    DaoControl daoControl;

    public ObservableBoolean alertVisibility = new ObservableBoolean(false);
    public ObservableField<String> alertMessage = new ObservableField<>();
    public ObservableBoolean above37Visibility = new ObservableBoolean(false);
    public ObservableField<String> userId = new ObservableField<>();
    public ObservableInt batteryImageId = new ObservableInt();
    public ObservableBoolean overheatExperimentMode = new ObservableBoolean(false);
    public ObservableField<String> dateTime = new ObservableField<>();

    private Observable.OnPropertyChangedCallback vuCallback;
    private ObservableField<BatteryMode> batteryModeCallback = new ObservableField<>();

    private boolean batteryAlert = false;
    private long batteryStartTime;

    @Inject
    public TopViewModel(){
        MainApplication.getInstance().getApplicationComponent().inject(this);
        batteryStartTime = TimeUtil.getCurrentTimeInSecond() + 300;

        

        displayCurrentTime();
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

    public void displayCurrentTime() {
        this.dateTime.set(String.format(Locale.US, "%s\n%s",
                TimeUtil.getCurrentDate(TimeUtil.Date), TimeUtil.getCurrentEnglishDate(TimeUtil.Time)));
    }
}
