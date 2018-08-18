package com.david.incubator.ui.system.range;

import android.content.Context;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.ui.FastIncreaseConstraintLayout;
import com.david.common.util.Constant;
import com.david.incubator.util.FragmentPage;
import com.david.databinding.LayoutSystemRangeBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2018/1/5 21:02
 * email: 10525677@qq.com
 * description:
 */

public class SystemRangeLayout extends FastIncreaseConstraintLayout<LayoutSystemRangeBinding> {

    SystemRangeViewModel systemRangeViewModel;

    ObservableInt navigationView;

    public SystemRangeLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        systemRangeViewModel = new SystemRangeViewModel();

        binding.setViewModel(systemRangeViewModel);

        init();

        RxView.clicks(binding.ibRangeTopLimit)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> systemRangeViewModel.upperSelected.set(true));

        RxView.clicks(binding.ibRangeBottomLimit)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> systemRangeViewModel.upperSelected.set(false));

        binding.rangeAir.setListener(p ->
                systemRangeViewModel.selectAir());

        binding.rangeSkin.setListener(o ->
                systemRangeViewModel.selectSkin());

        binding.rangeHumidity.setListener(o ->
                systemRangeViewModel.selectHumidity());

        binding.rangeOxygen.setListener(o ->
                systemRangeViewModel.selectOxygen());

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibOK))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> systemRangeViewModel.save());

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_HOME));
    }

    @Override
    protected void increaseValue() {
        systemRangeViewModel.increaseValue();
    }

    @Override
    protected void decreaseValue() {
        systemRangeViewModel.decreaseValue();
    }

    @Override
    public void attach() {
        systemRangeViewModel.attach();
    }

    @Override
    public void detach() {
        stopDisposable();
        systemRangeViewModel.detach();
    }

    private void init() {
        setButton(binding.buttonControl.findViewById(R.id.ibUp),
                binding.buttonControl.findViewById(R.id.ibDown));

        binding.title.setTitle(R.string.setting_range);

        binding.rangeAir
                .setViewModel(systemRangeViewModel.airKeyValueViewModel);
        binding.rangeSkin
                .setViewModel(systemRangeViewModel.skinKeyValueViewModel);
        binding.rangeHumidity
                .setViewModel(systemRangeViewModel.humidityKeyValueViewModel);
        binding.rangeOxygen
                .setViewModel(systemRangeViewModel.oxygenKeyValueViewModel);

        binding.buttonControl.setViewModel(
                systemRangeViewModel.buttonControlViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_system_range;
    }
}
