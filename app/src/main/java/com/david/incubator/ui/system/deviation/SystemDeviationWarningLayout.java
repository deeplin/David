package com.david.incubator.ui.system.deviation;

import android.content.Context;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.ui.FastUpdateLayout;
import com.david.common.util.Constant;
import com.david.incubator.util.FragmentPage;
import com.david.databinding.LayoutSystemDeviationWarningBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2018/1/3 22:02
 * email: 10525677@qq.com
 * description:
 */
public class SystemDeviationWarningLayout extends FastUpdateLayout<LayoutSystemDeviationWarningBinding> {

    SystemDeviationViewModel systemDeviationViewModel;

    ObservableInt navigationView;

    public SystemDeviationWarningLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        systemDeviationViewModel = new SystemDeviationViewModel();
        binding.setViewModel(systemDeviationViewModel);

        init();

        RxView.clicks(binding.ibDeviationTopLimit)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> systemDeviationViewModel.upperSelected.set(true));

        RxView.clicks(binding.ibDeviationBottomLimit)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> systemDeviationViewModel.upperSelected.set(false));

        binding.deviationAir.setListener(p ->
                systemDeviationViewModel.selectAir());

        binding.deviationSkin.setListener(o ->
                systemDeviationViewModel.selectSkin());

        binding.deviationHumidity.setListener(o ->
                systemDeviationViewModel.selectHumidity());

        binding.deviationOxygen.setListener(o ->
                systemDeviationViewModel.selectOxygen());

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibOK))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> systemDeviationViewModel.setValue());

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_HOME));
    }

    private void init() {
        setButton(binding.buttonControl.findViewById(R.id.ibUp),
                binding.buttonControl.findViewById(R.id.ibDown));

        binding.title.setTitle(R.string.deviation_warning);

        binding.deviationAir
                .setViewModel(systemDeviationViewModel.airViewModel);
        binding.deviationSkin
                .setViewModel(systemDeviationViewModel.skinViewModel);
        binding.deviationHumidity
                .setViewModel(systemDeviationViewModel.humidityViewModel);
        binding.deviationOxygen
                .setViewModel(systemDeviationViewModel.oxygenViewModel);

        binding.buttonControl.setViewModel(
                systemDeviationViewModel.buttonControlViewModel);
    }

    @Override
    protected void increaseValue() {
        systemDeviationViewModel.increaseValue();
    }

    @Override
    protected void decreaseValue() {
        systemDeviationViewModel.decreaseValue();
    }

    @Override
    public void attach() {
        systemDeviationViewModel.attach();
    }

    @Override
    public void detach() {
        stopDisposable();
        systemDeviationViewModel.detach();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_system_deviation_warning;
    }
}
