package com.david.incubator.ui.objective.cabin.temp;

import android.content.Context;

import com.david.R;
import com.david.common.mode.CtrlMode;
import com.david.common.ui.FastUpdateLayout;
import com.david.common.util.Constant;
import com.david.databinding.LayoutObjectiveTempBinding;
import com.david.incubator.control.MainApplication;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/29 19:23
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveTempLayout extends FastUpdateLayout<LayoutObjectiveTempBinding> {

    @Inject
    ObjectiveTempViewModel viewModel;

    public ObjectiveTempLayout(Context context) {
        super(context);

        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(viewModel);

        setButton(binding.ibObjectiveUp, binding.ibObjectiveDown);

        RxView.clicks(binding.ibObjectiveFirst)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    viewModel.setEnable(CtrlMode.Air);
                });

        RxView.clicks(binding.ibObjectiveSecond)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    viewModel.setEnable(CtrlMode.Skin);
                });

        RxView.clicks(binding.ibObjectiveAbove37)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    viewModel.shareMemory.above37.set(true);
                });

        RxView.clicks(binding.btObjectiveOK)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    stopDisposable();
                    viewModel.setObjective();
                });
    }

    @Override
    protected void increaseValue() {
        viewModel.increaseValue();
    }

    @Override
    protected void decreaseValue() {
        viewModel.decreaseValue();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_objective_temp;
    }

    @Override
    public void attach() {
        viewModel.attach();
    }

    @Override
    public void detach() {
        super.stopDisposable();
        viewModel.detach();
    }
}
