package com.david.incubator.ui.system.overheat;

import android.content.Context;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.ui.FastIncreaseConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.databinding.LayoutSystemOverheatWarningBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2018/1/10 21:15
 * email: 10525677@qq.com
 * description:
 */
public class SystemOverheatWarningLayout extends FastIncreaseConstraintLayout<LayoutSystemOverheatWarningBinding> {

    SystemOverheatViewModel systemOverheatViewModel;

    ObservableInt navigationView;

    public SystemOverheatWarningLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        systemOverheatViewModel = new SystemOverheatViewModel();

        binding.setViewModel(systemOverheatViewModel);

        init();

        binding.overheatAirBelow37.setListener(o ->
                systemOverheatViewModel.selectAirBelow37());

        binding.overheatAirAbove37.setListener(o ->
                systemOverheatViewModel.selectAirAbove37());

        binding.overheatAirFlowBelow37.setListener(o ->
                systemOverheatViewModel.selectAirFlowBelow37());

        binding.overheatAirFlowAbove37.setListener(o ->
                systemOverheatViewModel.selectAirFlowAbove37());

        binding.overheatSkin.setListener(o ->
                systemOverheatViewModel.selectSkin());

        binding.overheatFanSpeed.setListener(o ->
                systemOverheatViewModel.selectFanSpeed());

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibOK))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> systemOverheatViewModel.setValue());

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_HOME));
    }

    @Override
    protected void increaseValue() {
        systemOverheatViewModel.increaseValue();
    }

    @Override
    protected void decreaseValue() {
        systemOverheatViewModel.decreaseValue();
    }

    @Override
    public void attach() {
        systemOverheatViewModel.attach();
    }

    @Override
    public void detach() {
        stopDisposable();
        systemOverheatViewModel.detach();
    }

    private void init() {
        setButton(binding.buttonControl.findViewById(R.id.ibUp),
                binding.buttonControl.findViewById(R.id.ibDown));

        binding.title.setTitle(R.string.overheat_warning);

        binding.overheatAirBelow37
                .setViewModel(systemOverheatViewModel.airBelow37ViewModel);
        binding.overheatAirAbove37
                .setViewModel(systemOverheatViewModel.airAbove37ViewModel);
        binding.overheatAirFlowBelow37
                .setViewModel(systemOverheatViewModel.airFlowBelow37ViewModel);
        binding.overheatAirFlowAbove37
                .setViewModel(systemOverheatViewModel.airFlowAbove37ViewModel);
        binding.overheatSkin
                .setViewModel(systemOverheatViewModel.skinViewModel);
        binding.overheatFanSpeed
                .setViewModel(systemOverheatViewModel.fanSpeedViewModel);

        binding.buttonControl.setViewModel(
                systemOverheatViewModel.buttonControlViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_system_overheat_warning;
    }
}
