package com.david.incubator.ui.user;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableByte;
import android.databinding.ObservableInt;
import android.media.AudioManager;
import android.util.Log;
import android.view.LayoutInflater;

import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.SystemSetting;
import com.david.common.dao.gen.DaoSession;
import com.david.common.dao.gen.SystemSettingDao;
import com.david.common.ui.TabConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.david.databinding.LayoutUserWarningVolumeBinding;
import com.david.R;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:10
 * email: 10525677@qq.com
 * description:
 */

public class UserWarningVolumeLayout extends TabConstraintLayout<LayoutUserWarningVolumeBinding> {

    private final byte MAXIMUM = 100;
    private final byte MINIMUM = 30;

    @Inject
    DaoControl daoControl;

    ObservableInt navigationView;

    public ObservableByte valueField = new ObservableByte();

    AudioManager audioManager;
    SystemSetting systemSetting;

    public UserWarningVolumeLayout(Context context, ObservableInt navigationView) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        this.navigationView = navigationView;

        init();

        binding.setViewModel(this);

        binding.title.setTitle(R.string.warning_volume);

        ButtonControlViewModel buttonControlViewModel = new ButtonControlViewModel();
        buttonControlViewModel.showOK.set(false);
        binding.buttonControl.setViewModel(buttonControlViewModel);

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibUp))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (valueField.get() < MAXIMUM) {
                        valueField.set((byte) (valueField.get() + 10));
                    } else {
                        valueField.set(MAXIMUM);
                    }
                });

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibDown))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (valueField.get() > MINIMUM) {
                        valueField.set((byte) (valueField.get() - 10));
                    } else {
                        valueField.set(MINIMUM);
                    }
                });

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_HOME));

        valueField.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                byte volume = valueField.get();
                systemSetting.setVolume(volume);
                save();
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, AudioManager.FLAG_PLAY_SOUND);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_user_warning_volume;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }

    private void init() {
        audioManager = (AudioManager) MainApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        systemSetting = daoControl.getSystemSetting();
        valueField.set(systemSetting.getVolume());
    }

    private void save() {
        daoControl.saveSystemSetting(systemSetting);
    }
}
