package com.david.incubator.ui.main.top;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.alert.AlarmControl;
import com.david.common.alert.AlarmModel;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.UserModel;
import com.david.common.data.ShareMemory;
import com.david.common.mode.BatteryMode;
import com.david.common.mode.SystemMode;
import com.david.common.ui.IViewModel;
import com.david.common.util.ResourceUtil;
import com.david.common.util.TimeUtil;
import com.david.incubator.ui.main.side.SideViewModel;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TopViewModel implements IViewModel {

    @Inject
    public AlarmControl alarmControl;
    @Inject
    public ShareMemory shareMemory;
    @Inject
    SideViewModel sideViewModel;
    @Inject
    DaoControl daoControl;

    public ObservableField<String> userId = new ObservableField<>();
    public ObservableField<String> alarmField = new ObservableField<>();
    public ObservableInt batteryImageId = new ObservableInt();
    public ObservableBoolean overheatExperimentMode = new ObservableBoolean(false);
    public ObservableField<String> dateTime = new ObservableField<>();
    private ObservableField<BatteryMode> batteryModeCallback = new ObservableField<>();

    private Observable.OnPropertyChangedCallback vuCallback;

    private boolean batteryAlert = false;
    private long batteryStartTime;

    @Inject
    public TopViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        batteryStartTime = TimeUtil.getCurrentTimeInSecond() + 300;

        alarmControl.topAlarm.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String alarmId = null;
                AlarmModel alarmModel = alarmControl.topAlarm.get();
                if (alarmModel != null) {
                    alarmId = alarmModel.getAlertId();
                    alarmField.set(alarmModel.toString());
                } else {
                    alarmField.set(null);
                }
                if (alarmId != null) {
                    if (alarmId.equals("SYS.UPS") || alarmId.equals("SYS.BAT")) {
                        batteryAlert = true;
                    } else {
                        batteryAlert = false;
                    }

                    if (alarmId.equals("SYS.TANK")) {
                        sideViewModel.muteAlarm();
                    }
                } else {
                    batteryAlert = false;
                }
            }
        });

        batteryModeCallback.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                BatteryMode batteryMode = ((ObservableField<BatteryMode>) observable).get();
                if (Objects.equals(batteryMode, BatteryMode.Full)) {
                    batteryImageId.set(R.mipmap.battery5);
                } else if (Objects.equals(batteryMode, BatteryMode.Failure)) {
                    batteryImageId.set(R.mipmap.battery_fault);
                }
            }
        });

        shareMemory.ohTest.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                setOverheatExperiment(shareMemory.ohTest.get() == 1);
            }
        });

        //显示电池电量
        vuCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                if (Objects.equals(shareMemory.systemMode.get(), SystemMode.Transit)) {
                    return;
                }

                long now = TimeUtil.getCurrentTimeInSecond();
                if (now < batteryStartTime) {
                    batteryModeCallback.set(BatteryMode.Charging);
                    setBatteryCharging();
                    return;
                }

                if (batteryAlert) {
                    batteryModeCallback.set(BatteryMode.Failure);
                    return;
                }

                int vu = ((ObservableInt) observable).get();

                if (vu < 9000) {
                    batteryModeCallback.set(BatteryMode.Charging);
                    setBatteryCharging();
                } else if (vu > 9400) {
                    batteryModeCallback.set(BatteryMode.Full);
                } else {
                    if (Objects.equals(batteryModeCallback.get(), BatteryMode.Failure)) {
                        batteryModeCallback.set(BatteryMode.Charging);
                    }
                }
            }
        };

        displayCurrentTime();
    }

    @Override
    public void attach() {
        shareMemory.VU.addOnPropertyChangedCallback(vuCallback);
        loadUserId();
    }

    @Override
    public void detach() {
        shareMemory.VU.removeOnPropertyChangedCallback(vuCallback);
    }

    private void loadUserId() {
        UserModel userModel = daoControl.getLastUserModel();
        String userString;
        if (userModel != null && userModel.getEndTimeStamp() == 0) {
            userString = String.format("No. %s", userModel.getName());
        } else {
            userString = ResourceUtil.getString(R.string.default_user);
        }
        userId.set(userString);
    }

    public void displayCurrentTime() {
        this.dateTime.set(String.format(Locale.US, "%s\n%s",
                TimeUtil.getCurrentDate(TimeUtil.Date), TimeUtil.getCurrentEnglishDate(TimeUtil.Time)));
    }

    private void setOverheatExperiment(boolean status) {
        if (status) {
            userId.set(ResourceUtil.getString(R.string.overheat_experiment));
        } else {
            userId.set("");
        }
        overheatExperimentMode.set(status);
    }

    private int sequenceId = -1;

    private void setBatteryCharging() {
        sequenceId++;
        int imageId;
        switch (sequenceId % 6) {
            case (0):
                imageId = R.mipmap.battery0;
                break;
            case (1):
                imageId = R.mipmap.battery1;
                break;
            case (2):
                imageId = R.mipmap.battery2;
                break;
            case (3):
                imageId = R.mipmap.battery3;
                break;
            case (4):
                imageId = R.mipmap.battery4;
                break;
            default:
                imageId = R.mipmap.battery5;
                break;
        }
        batteryImageId.set(imageId);
    }
}
