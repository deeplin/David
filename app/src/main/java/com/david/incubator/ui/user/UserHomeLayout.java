package com.david.incubator.ui.user;

import android.content.Context;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.databinding.LayoutUserHomeBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:26
 * email: 10525677@qq.com
 * description:
 */

public class UserHomeLayout extends BindingConstraintLayout<LayoutUserHomeBinding> {

    ObservableInt navigationView;

    public UserHomeLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;

        binding.title.setTitle(R.string.user_setting);

        RxView.clicks(binding.userPatientInfo)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_MODEL));

        RxView.clicks(binding.userTimeSetup)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_TIME_SETUP));

        RxView.clicks(binding.userLanguageSetup)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_LANGUAGE_SETUP));

        RxView.clicks(binding.userWarningVolume)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_WARNING_VOLUME));

        RxView.clicks(binding.userScreenLuminance)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_SCREEN_LUMINANCE));

        RxView.clicks(binding.systemOverheatExperiment)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_OVERHEAT_EXPERIMENT));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_user_home;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }
}