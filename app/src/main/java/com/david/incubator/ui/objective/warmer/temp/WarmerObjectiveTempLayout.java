package com.david.incubator.ui.objective.warmer.temp;

import android.content.Context;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.mode.CtrlMode;
import com.david.common.ui.FastIncreaseConstraintLayout;
import com.david.common.util.Constant;
import com.david.databinding.WarmerLayoutObjectiveTempBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/5 16:37
 * email: 10525677@qq.com
 * description:
 */
public class WarmerObjectiveTempLayout extends FastIncreaseConstraintLayout<WarmerLayoutObjectiveTempBinding> {

    @Inject
    WarmerObjectiveTempViewModel viewModel;

    public WarmerObjectiveTempLayout(Context context) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        binding.setViewModel(viewModel);

        setButton(binding.ibObjectiveUp, binding.ibObjectiveDown);

        RxView.clicks(binding.ibObjectiveFirst)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    viewModel.setEnable(CtrlMode.Skin);
                });

        RxView.clicks(binding.ibObjectiveSecond)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    viewModel.setEnable(CtrlMode.Manual);
                });

        RxView.clicks(binding.ibObjectiveThird)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    viewModel.setEnable(CtrlMode.Prewarm);
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
        return R.layout.warmer_layout_objective_temp;
    }

    @Override
    public void attach() {
        viewModel.attach();
    }

    @Override
    public void detach() {
        super.detach();
        viewModel.detach();
    }
}
