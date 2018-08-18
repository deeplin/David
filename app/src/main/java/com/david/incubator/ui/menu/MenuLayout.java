package com.david.incubator.ui.menu;

import android.content.Context;
import android.databinding.Observable;
import android.util.AttributeSet;

import com.david.R;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.databinding.LayoutMenuBinding;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.main.MainViewModel;
import com.david.incubator.util.FragmentPage;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/26 19:13
 * email: 10525677@qq.com
 * description:
 */

public class MenuLayout extends BindingConstraintLayout<LayoutMenuBinding> {

    @Inject
    MenuViewModel menuViewModel;
    @Inject
    MainViewModel mainViewModel;
    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;

    Observable.OnPropertyChangedCallback softwareCallback;
    Observable.OnPropertyChangedCallback hardwareCallback;

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(menuViewModel);

        hardwareCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                menuViewModel.spo2Visible.set(moduleHardware.isSPO2());
                menuViewModel.scaleVisible.set(moduleHardware.isSCALE());
                menuViewModel.cameraVisible.set(moduleHardware.isCameraInstalled());
            }
        };

        softwareCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                io.reactivex.Observable.just(this)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aObject ->
                                binding.btMenuSpo2.setEnabled(moduleHardware.isSPO2() && moduleSoftware.isSPO2()));
            }
        };

        RxView.clicks(binding.btMenuSetting)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    menuViewModel.menuChart.set(false);
                    menuViewModel.menuSpo2.set(false);
                    menuViewModel.menuScale.set(false);
                    menuViewModel.menuCamera.set(false);
                    menuViewModel.menuSetting.set(true);
                    shareMemory.currentFragmentID.set(FragmentPage.SETTING_FRAGMENT);
                });

        RxView.clicks(binding.btMenuChart)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    menuViewModel.menuChart.set(true);
                    menuViewModel.menuSpo2.set(false);
                    menuViewModel.menuScale.set(false);
                    menuViewModel.menuCamera.set(false);
                    menuViewModel.menuSetting.set(false);
                    shareMemory.currentFragmentID.set(FragmentPage.CHART_FRAGMENT);
                });


        RxView.clicks(binding.btMenuSpo2)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (moduleHardware.isSPO2() && moduleSoftware.isSPO2()) {
                        menuViewModel.menuChart.set(false);
                        menuViewModel.menuSpo2.set(true);
                        menuViewModel.menuScale.set(false);
                        menuViewModel.menuCamera.set(false);
                        menuViewModel.menuSetting.set(false);
                        shareMemory.currentFragmentID.set(FragmentPage.SPO2_FRAGMENT);
                    }
                });

        RxView.clicks(binding.btMenuScale)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (moduleHardware.isSCALE()) {
                        menuViewModel.menuChart.set(false);
                        menuViewModel.menuSpo2.set(false);
                        menuViewModel.menuScale.set(true);
                        menuViewModel.menuCamera.set(false);
                        menuViewModel.menuSetting.set(false);
                        shareMemory.currentFragmentID.set(FragmentPage.SCALE_FRAGMENT);
                    }
                });

        RxView.clicks(binding.btMenuCamera)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (moduleHardware.isCameraInstalled()) {
                        menuViewModel.menuChart.set(false);
                        menuViewModel.menuSpo2.set(false);
                        menuViewModel.menuScale.set(false);
                        menuViewModel.menuCamera.set(true);
                        menuViewModel.menuSetting.set(false);
                        shareMemory.currentFragmentID.set(FragmentPage.CAMERA_FRAGMENT);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_menu;
    }

    @Override
    public void attach() {
        moduleHardware.updated.addOnPropertyChangedCallback(hardwareCallback);
        moduleSoftware.updated.addOnPropertyChangedCallback(softwareCallback);
        moduleHardware.updated.notifyChange();
        moduleSoftware.updated.notifyChange();
    }

    @Override
    public void detach() {
        moduleSoftware.updated.removeOnPropertyChangedCallback(softwareCallback);
        moduleHardware.updated.removeOnPropertyChangedCallback(hardwareCallback);
    }
}