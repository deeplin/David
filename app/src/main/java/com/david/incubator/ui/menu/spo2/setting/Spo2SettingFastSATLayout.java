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
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.ui.IViewModel;
import com.david.common.util.CommandChar;
import com.david.common.util.Constant;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/3 11:29
 * email: 10525677@qq.com
 * description:
 */
public class Spo2SettingFastSATLayout extends ConstraintLayout implements IViewModel {

    @Inject
    ShareMemory shareMemory;
    @Inject
    MessageSender messageSender;

    Button fastSATOn;
    Button fastSATOff;

    android.databinding.Observable.OnPropertyChangedCallback fastSATCallback;

    public Spo2SettingFastSATLayout(Context context) {
        super(context);

        MainApplication.getInstance().getApplicationComponent().inject(this);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_spo2_setting_fast_sat, this, true);

        fastSATOn = view.findViewById(R.id.btSpo2SettingFastSATOn);
        fastSATOff = view.findViewById(R.id.btSpo2SettingFastSATOff);

        fastSATCallback = new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable observable, int i) {
                String fastSAT = ((ObservableField<String>) observable).get();
                Observable.just(fastSAT)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((text -> {
                            if (text.equals(CommandChar.ON)) {
                                fastSATOn.setSelected(true);
                                fastSATOff.setSelected(false);
                            } else if (text.equals(CommandChar.OFF)) {
                                fastSATOn.setSelected(false);
                                fastSATOff.setSelected(true);
                            }
                        }));
            }
        };

        RxView.clicks(fastSATOn)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Void) -> sendCommand(true));

        RxView.clicks(fastSATOff)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Void) -> sendCommand(false));
    }

    @Override
    public void attach() {
        shareMemory.fastsatValue.addOnPropertyChangedCallback(fastSATCallback);
        shareMemory.fastsatValue.notifyChange();
    }

    @Override
    public void detach() {
        shareMemory.fastsatValue.removeOnPropertyChangedCallback(fastSATCallback);
    }

    private void sendCommand(boolean on) {
        if (!shareMemory.lockScreen.get()) {
            String value;
            if (on) {
                value = CommandChar.ON;
            } else {
                value = CommandChar.OFF;
            }
            messageSender.setSpo2(false, "FASTSAT", value, (aBoolean, baseSerialMessage) -> {
                if (aBoolean) {
                    messageSender.getSpo2(false, shareMemory);
                }
            });
        }
    }
}
