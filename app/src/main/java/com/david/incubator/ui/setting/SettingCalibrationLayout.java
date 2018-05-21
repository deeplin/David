package com.david.incubator.ui.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.david.common.alarm.AlarmControl;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.util.Constant;
import com.david.common.util.ResourceUtil;
import com.david.databinding.LayoutSettingCalibrationBinding;
import com.david.common.ui.BindingConstraintLayout;
import com.david.incubator.util.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import com.david.R;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/3 19:32
 * email: 10525677@qq.com
 * description:
 */
public class SettingCalibrationLayout extends BindingConstraintLayout<LayoutSettingCalibrationBinding> {

    @Inject
    MessageSender messageSender;
    @Inject
    ShareMemory shareMemory;
    @Inject
    AlarmControl alarmControl;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;

    public ObservableBoolean showO2 = new ObservableBoolean();
    public ObservableBoolean selectO2 = new ObservableBoolean(false);
    public ObservableBoolean selectLeft = new ObservableBoolean(false);
    public ObservableBoolean selectRight = new ObservableBoolean(false);

    public ObservableField<String> result = new ObservableField<>();
    Observable.OnPropertyChangedCallback alarmCallback;

    private AlertDialog alertDialog;

    public SettingCalibrationLayout(Context context) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        binding.setViewModel(this);

        binding.title.setTitle(R.string.calibration);
        showO2.set(!moduleHardware.is93S());

        alarmCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                updateEnable();
            }
        };

        RxView.clicks(binding.userCalibrationO2)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    selectO2.set(true);
                    updateEnable();
                    selectLeft.set(false);
                    selectRight.set(false);
                    result.set("");
                });

        RxView.clicks(binding.userCalibrationScale)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    selectO2.set(false);
                    updateEnable();
                    selectLeft.set(false);
                    selectRight.set(false);
                    result.set("");
                });

        RxView.clicks(binding.userCalibrationLeft)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    if (selectO2.get()) {
                        alertDialog = ViewUtil.buildConfirmDialog(this.getContext(), R.string.calibration_o2,
                                ResourceUtil.getString(R.string.calibration_confirm_o2_21),
                                (dialog, which) -> {
                                    alertDialog = null;
                                    messageSender.setOxygen(210, 1,
                                            (aBoolean, baseSerialMessage) -> {
                                                if (aBoolean) {
                                                    messageSender.setOxygen(210, 2,
                                                            (aaBoolean, abaseSerialMessage) -> io.reactivex.Observable
                                                                    .just(this)
                                                                    .observeOn(AndroidSchedulers.mainThread())
                                                                    .subscribe((obj) -> {
                                                                        if (aaBoolean) {
                                                                            selectLeft.set(true);
                                                                            selectRight.set(false);
                                                                            result.set(ResourceUtil.getString(R.string.calibration_successful));
                                                                        } else {
                                                                            selectLeft.set(false);
                                                                            selectRight.set(false);
                                                                            result.set(ResourceUtil.getString(R.string.calibration_failed));
                                                                        }
                                                                    }));
                                                } else {
                                                    selectLeft.set(false);
                                                    selectRight.set(false);
                                                    result.set(ResourceUtil.getString(R.string.calibration_failed));
                                                }
                                            });
                                });
                    } else {
                        alertDialog = ViewUtil.buildConfirmDialog(this.getContext(), R.string.calibration_scale,
                                String.format(ResourceUtil.getString(R.string.calibration_confirm_scale_0), shareMemory.SC.get()),
                                (dialog, which) -> {
                                    alertDialog = null;
                                    messageSender.setScale(0,
                                            (aBoolean, baseSerialMessage) -> io.reactivex.Observable
                                                    .just(this)
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe((obj) -> {
                                                        if (aBoolean) {
                                                            selectLeft.set(true);
                                                            selectRight.set(false);
                                                            result.set(ResourceUtil.getString(R.string.calibration_successful));
                                                        } else {
                                                            selectLeft.set(false);
                                                            selectRight.set(false);
                                                            result.set(ResourceUtil.getString(R.string.calibration_failed));
                                                        }
                                                    }));
                                });
                    }
                });

        RxView.clicks(binding.userCalibrationRight)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    if (selectO2.get()) {
                        alertDialog = ViewUtil.buildConfirmDialog(this.getContext(), R.string.calibration_o2,
                                ResourceUtil.getString(R.string.calibration_confirm_o2_100),
                                (dialog, which) -> {
                                    alertDialog = null;
                                    messageSender.setOxygen(999, 1,
                                            (aBoolean, baseSerialMessage) -> {
                                                if (aBoolean) {
                                                    messageSender.setOxygen(999, 2,
                                                            (aaBoolean, abaseSerialMessage) -> io.reactivex.Observable
                                                                    .just(this)
                                                                    .observeOn(AndroidSchedulers.mainThread())
                                                                    .subscribe((obj) -> {
                                                                        if (aaBoolean) {
                                                                            selectLeft.set(false);
                                                                            selectRight.set(true);
                                                                            result.set(ResourceUtil.getString(R.string.calibration_successful));
                                                                        } else {
                                                                            selectLeft.set(false);
                                                                            selectRight.set(false);
                                                                            result.set(ResourceUtil.getString(R.string.calibration_failed));
                                                                        }
                                                                    }));
                                                } else {
                                                    selectLeft.set(false);
                                                    selectRight.set(false);
                                                    result.set(ResourceUtil.getString(R.string.calibration_failed));
                                                }
                                            });
                                });
                    } else {
                        alertDialog = ViewUtil.buildConfirmDialog(this.getContext(), R.string.calibration_scale,
                                String.format(ResourceUtil.getString(R.string.calibration_confirm_scale_5000), shareMemory.SC.get()),
                                (dialog, which) -> {
                                    alertDialog = null;
                                    messageSender.setScale(5000,
                                            (aBoolean, baseSerialMessage) -> io.reactivex.Observable
                                                    .just(this)
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe((obj) -> {
                                                        if (aBoolean) {
                                                            selectLeft.set(false);
                                                            selectRight.set(true);
                                                            result.set(ResourceUtil.getString(R.string.calibration_successful));
                                                        } else {
                                                            selectLeft.set(false);
                                                            selectRight.set(false);
                                                            result.set(ResourceUtil.getString(R.string.calibration_failed));
                                                        }
                                                    }));
                                });
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_setting_calibration;
    }

    @Override
    public void attach() {
        alarmControl.topAlarmId.addOnPropertyChangedCallback(alarmCallback);
        updateEnable();
    }

    @Override
    public void detach() {
        alarmControl.topAlarmId.removeOnPropertyChangedCallback(alarmCallback);
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public void updateEnable() {
//        String text = alarmControl.topAlarmId.get();
        String text = null;
        io.reactivex.Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    if (text == null) {
                        if (selectO2.get()) {
                            if (moduleHardware.isO2() && moduleSoftware.isO2()) {
                                binding.userCalibrationLeft.setEnabled(true);
                                binding.userCalibrationRight.setEnabled(true);
                            } else {
                                binding.userCalibrationLeft.setEnabled(false);
                                binding.userCalibrationRight.setEnabled(false);
                            }
                        } else {
                            if (moduleHardware.isSCALE() && moduleSoftware.isSCALE()) {
                                binding.userCalibrationLeft.setEnabled(true);
                                binding.userCalibrationRight.setEnabled(true);
                            } else {
                                binding.userCalibrationLeft.setEnabled(false);
                                binding.userCalibrationRight.setEnabled(false);
                            }
                        }
                    } else {
                        binding.userCalibrationLeft.setEnabled(false);
                        binding.userCalibrationRight.setEnabled(false);
                    }
                });
    }
}

