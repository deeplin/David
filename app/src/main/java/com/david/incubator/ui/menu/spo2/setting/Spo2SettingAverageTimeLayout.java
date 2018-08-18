package com.david.incubator.ui.menu.spo2.setting;

import android.content.Context;
import android.databinding.ObservableField;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.widget.Button;

import com.david.R;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.mode.AverageTimeMode;
import com.david.common.ui.IViewModel;
import com.david.incubator.control.MainApplication;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/3 10:42
 * email: 10525677@qq.com
 * description:
 */
public class Spo2SettingAverageTimeLayout extends ConstraintLayout implements IViewModel {

    @Inject
    ShareMemory shareMemory;
    @Inject
    MessageSender messageSender;

    android.databinding.Observable.OnPropertyChangedCallback averageTimeCallback;

    public Spo2SettingAverageTimeLayout(Context context) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.layout_spo2_setting_average_time, this, true);

        Button time0 = findViewById(R.id.btSpo2SettingAverageTime0);
        time0.setOnClickListener(v -> sendCommand(0));

        Button time1 = findViewById(R.id.btSpo2SettingAverageTime1);
        time1.setOnClickListener(v -> sendCommand(1));

        Button time2 = findViewById(R.id.btSpo2SettingAverageTime2);
        time2.setOnClickListener(v -> sendCommand(2));

        Button time3 = findViewById(R.id.btSpo2SettingAverageTime3);
        time3.setOnClickListener(v -> sendCommand(3));

        Button time4 = findViewById(R.id.btSpo2SettingAverageTime4);
        time4.setOnClickListener(v -> sendCommand(4));

        Button time5 = findViewById(R.id.btSpo2SettingAverageTime5);
        time5.setOnClickListener(v -> sendCommand(5));

        Button time6 = findViewById(R.id.btSpo2SettingAverageTime6);
        time6.setOnClickListener(v -> sendCommand(6));

        averageTimeCallback = new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable observable, int i) {

                Observable.just(this)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((o) -> {
                            time0.setSelected(false);
                            time1.setSelected(false);
                            time2.setSelected(false);
                            time3.setSelected(false);
                            time4.setSelected(false);
                            time5.setSelected(false);
                            time6.setSelected(false);

                            AverageTimeMode averageTimeMode = ((ObservableField<AverageTimeMode>) observable).get();
                            switch (averageTimeMode.getIndex()) {
                                case (0):
                                    time0.setSelected(true);
                                    break;
                                case (1):
                                    time1.setSelected(true);
                                    break;
                                case (2):
                                    time2.setSelected(true);
                                    break;
                                case (3):
                                    time3.setSelected(true);
                                    break;
                                case (4):
                                    time4.setSelected(true);
                                    break;
                                case (5):
                                    time5.setSelected(true);
                                    break;
                                case (6):
                                    time6.setSelected(true);
                                    break;
                            }
                        });
            }
        };
    }

    @Override
    public void attach() {
        shareMemory.averageTimeMode.addOnPropertyChangedCallback(averageTimeCallback);
        shareMemory.averageTimeMode.notifyChange();
    }

    @Override
    public void detach() {
        shareMemory.averageTimeMode.removeOnPropertyChangedCallback(averageTimeCallback);
    }

    private void sendCommand(int averageTime) {
        if (!shareMemory.lockScreen.get()) {
            messageSender.setSpo2(false, "AVG", String.valueOf(averageTime), (aBoolean, baseSerialMessage) -> {
                if (aBoolean) {
                    messageSender.getSpo2(false, shareMemory);
                }
            });
        }
    }
}
