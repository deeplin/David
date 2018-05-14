package com.david.incubator.ui.menu.spo2.setting;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.mode.Spo2SensMode;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/3 9:24
 * email: 10525677@qq.com
 * description:
 */
public class Spo2SettingSensLayout extends ConstraintLayout implements IViewModel {

    @Inject
    ShareMemory shareMemory;
    @Inject
    MessageSender messageSender;

    Button settingSensNormal;
    Button settingSensApod;
    Button settingSensMax;

    android.databinding.Observable.OnPropertyChangedCallback sensCallback;

    public Spo2SettingSensLayout(Context context) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_spo2_setting_sens, this, true);

        settingSensNormal = view.findViewById(R.id.btSpo2SettingSensNormal);
        settingSensApod = view.findViewById(R.id.btSpo2SettingSensApod);
        settingSensMax = view.findViewById(R.id.btSpo2SettingSensMax);

        sensCallback = new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable observable, int i) {
                Spo2SensMode spo2SensMode = ((ObservableField<Spo2SensMode>) observable).get();
                Observable.just(spo2SensMode).observeOn(AndroidSchedulers.mainThread())
                        .subscribe((mode) -> {
                            if (mode.equals(Spo2SensMode.MAX)) {
                                selectMax();
                            } else if (mode.equals(Spo2SensMode.APOD)) {
                                selectApod();
                            } else if (mode.equals(Spo2SensMode.Normal)) {
                                selectNormal();
                            }
                        });
            }
        };

        RxView.clicks(settingSensNormal)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Void) -> sendCommand(Spo2SensMode.Normal.getName()));

        RxView.clicks(settingSensApod)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Void) -> sendCommand(Spo2SensMode.APOD.getName()));

        RxView.clicks(settingSensMax)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Void) -> sendCommand(Spo2SensMode.MAX.getName()));
    }

    @Override
    public void attach() {
        shareMemory.sensMode.addOnPropertyChangedCallback(sensCallback);
        shareMemory.sensMode.notifyChange();
    }

    @Override
    public void detach() {
        shareMemory.sensMode.removeOnPropertyChangedCallback(sensCallback);
    }

    private void selectNormal() {
        settingSensNormal.setSelected(true);
        settingSensApod.setSelected(false);
        settingSensMax.setSelected(false);
    }

    private void selectApod() {
        settingSensNormal.setSelected(false);
        settingSensApod.setSelected(true);
        settingSensMax.setSelected(false);
    }

    private void selectMax() {
        settingSensNormal.setSelected(false);
        settingSensApod.setSelected(false);
        settingSensMax.setSelected(true);
    }

    private void sendCommand(String sens) {
        if (!shareMemory.lockScreen.get()) {
            messageSender.setSpo2(false, "SENS", sens, (aBoolean, baseSerialMessage) -> {
                if (aBoolean) {
                    messageSender.getSpo2(false, shareMemory);
                }
            });
        }
    }
}