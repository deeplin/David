package com.david.incubator.ui.system.calibration;

import android.content.Context;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.ui.FastIncreaseConstraintLayout;
import com.david.common.util.Constant;
import com.david.incubator.util.FragmentPage;
import com.david.databinding.LayoutSystemCalibrationBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2018/1/4 20:06
 * email: 10525677@qq.com
 * description:
 */
public class SystemCalibrationLayout extends FastIncreaseConstraintLayout<LayoutSystemCalibrationBinding> {

    SystemCalibrationViewModel systemCalibrationViewModel;

    ObservableInt navigationView;

    public SystemCalibrationLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        systemCalibrationViewModel = new SystemCalibrationViewModel();
        binding.setViewModel(systemCalibrationViewModel);

        init();

        binding.calibrationAir.setListener(p ->
                systemCalibrationViewModel.selectAir());

        binding.calibrationISOAir.setListener(p ->
                systemCalibrationViewModel.selectIsoAir());

        binding.calibrationSkin1.setListener(p ->
                systemCalibrationViewModel.selectSkin1());

        binding.calibrationISOSkin1.setListener(p ->
                systemCalibrationViewModel.selectIsoSkin1());

        binding.calibrationSkin2.setListener(p ->
                systemCalibrationViewModel.selectSkin2());

        binding.calibrationAirFlow.setListener(p ->
                systemCalibrationViewModel.selectAirFlow());

        binding.calibrationHumidity.setListener(p ->
                systemCalibrationViewModel.selectHumidity());


        RxView.clicks(binding.buttonControl.findViewById(R.id.ibOK))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> systemCalibrationViewModel.setValue());

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_HOME));
    }

    @Override
    protected void increaseValue() {
        systemCalibrationViewModel.increaseValue();
    }

    @Override
    protected void decreaseValue() {
        systemCalibrationViewModel.decreaseValue();
    }

    @Override
    public void attach() {
        systemCalibrationViewModel.attach();
    }

    @Override
    public void detach() {
        stopDisposable();
        systemCalibrationViewModel.detach();
    }

    private void init() {
        setButton(binding.buttonControl.findViewById(R.id.ibUp),
                binding.buttonControl.findViewById(R.id.ibDown));

        binding.title.setTitle(R.string.temp_calibration);

        binding.calibrationAir
                .setViewModel(systemCalibrationViewModel.airViewModel);
        binding.calibrationISOAir
                .setViewModel(systemCalibrationViewModel.isoAirViewModel);
        binding.calibrationSkin1
                .setViewModel(systemCalibrationViewModel.skin1ViewModel);
        binding.calibrationISOSkin1
                .setViewModel(systemCalibrationViewModel.isoSkin1ViewModel);
        binding.calibrationSkin2
                .setViewModel(systemCalibrationViewModel.skin2ViewModel);
        binding.calibrationAirFlow
                .setViewModel(systemCalibrationViewModel.airFlowViewModel);
        binding.calibrationHumidity
                .setViewModel(systemCalibrationViewModel.humidityViewModel);

        binding.buttonControl.setViewModel(
                systemCalibrationViewModel.buttonControlViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_system_calibration;
    }
}
