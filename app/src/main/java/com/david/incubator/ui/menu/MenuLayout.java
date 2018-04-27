package com.david.incubator.ui.menu;

import android.content.Context;
import android.databinding.Observable;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.ui.AutoAttachConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.databinding.IncubatorLayoutMenuBinding;
import com.david.incubator.ui.main.MainViewModel;
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

public class MenuLayout extends AutoAttachConstraintLayout {

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

    IncubatorLayoutMenuBinding binding;

    Observable.OnPropertyChangedCallback hardwareCallback;
    Observable.OnPropertyChangedCallback softwareCallback;

    public MenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        binding = IncubatorLayoutMenuBinding.inflate(layoutInflater, this, true);
        binding.setViewModel(menuViewModel);

        softwareCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                io.reactivex.Observable.just(this)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(aObject ->
                                binding.btMenuSpo2.setEnabled(moduleHardware.isSPO2() && moduleSoftware.isSPO2()));
            }
        };

        hardwareCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                menuViewModel.cameraVisible.set(moduleHardware.isCameraInstalled());
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

        /*SpO2按钮切换到监护仪*/
//        RxView.clicks(binding.btMenuSpo2)
//                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
//                .subscribe((aVoid) -> mainViewModel.switchToMonitor());

        RxView.clicks(binding.btMenuScale)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    if (moduleHardware.isSCALE() && moduleSoftware.isSCALE()) {
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
