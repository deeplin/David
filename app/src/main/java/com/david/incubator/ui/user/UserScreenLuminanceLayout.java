package com.david.incubator.ui.user;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableByte;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.SystemSetting;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.databinding.LayoutUserScreenLuminanceBinding;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.david.incubator.ui.main.MainViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:10
 * email: 10525677@qq.com
 * description:
 */

public class UserScreenLuminanceLayout extends BindingConstraintLayout<LayoutUserScreenLuminanceBinding> {

    private final byte MAXIMUM = 100;
    private final byte MINIMUM = 20;

    @Inject
    MainViewModel mainViewModel;
    @Inject
    DaoControl daoControl;


    ObservableInt navigationView;
    SystemSetting systemSetting;

    public ObservableByte valueField = new ObservableByte();

    public UserScreenLuminanceLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        MainApplication.getInstance().getApplicationComponent().inject(this);

        init();

        binding.setViewModel(this);

        binding.title.setTitle(R.string.screen_luminance);

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
                byte luminance = valueField.get();
                systemSetting.setLuminance(luminance);
                save();
                mainViewModel.setLight(luminance);
            }
        });
    }

    private void init() {
        systemSetting = daoControl.getSystemSetting();
        valueField.set(systemSetting.getLuminance());
    }

    private void save() {
        daoControl.saveSystemSetting(systemSetting);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_user_screen_luminance;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }
}
