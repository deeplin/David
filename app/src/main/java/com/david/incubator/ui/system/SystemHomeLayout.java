package com.david.incubator.ui.system;

import android.content.Context;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.ui.TabConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.databinding.LayoutSystemHomeBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:26
 * email: 10525677@qq.com
 * description:
 */

public class SystemHomeLayout extends TabConstraintLayout<LayoutSystemHomeBinding> {

    ObservableInt navigationView;

    public SystemHomeLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;

        binding.title.setTitle(R.string.system_setting);

        RxView.clicks(binding.systemDeviationWarning)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_DEVIATION_WARNING));

        RxView.clicks(binding.systemOverheatWarning)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_OVERHEAT_WARNING));

        RxView.clicks(binding.systemRange)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_RANGE));

        RxView.clicks(binding.systemCalibration)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_CALIBRATION));

        RxView.clicks(binding.systemOtherParameter)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_OTHER_PARAMETER));

        RxView.clicks(binding.systemDataPrint)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_DATA_PRINT));

        RxView.clicks(binding.systemOverheatExperiment)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_OVERHEAT_EXPERIMENT));

        RxView.clicks(binding.systemFactorySetting)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_FACTORY));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_system_home;
    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }
}
