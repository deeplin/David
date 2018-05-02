package com.david.incubator.ui.objective.warmer.jaundice;

import android.content.Context;

import com.david.R;
import com.david.common.ui.FastIncreaseConstraintLayout;
import com.david.common.util.Constant;
import com.david.databinding.WarmerLayoutObjectiveJaunediceBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2017/12/30 14:58
 * email: 10525677@qq.com
 * description:
 */
public class WarmerObjectiveJaundiceLayout extends FastIncreaseConstraintLayout<WarmerLayoutObjectiveJaunediceBinding> {
    WarmerObjectiveJaundiceViewModel viewModel;

    public WarmerObjectiveJaundiceLayout(Context context,
                                         WarmerObjectiveJaundiceViewModel warmerObjectiveJaundiceViewModel) {
        super(context);
        this.viewModel = warmerObjectiveJaundiceViewModel;

        binding.setViewModel(warmerObjectiveJaundiceViewModel);

        setButton(binding.ibObjectiveUp, binding.ibObjectiveDown);

        RxView.clicks(binding.ibObjectiveUpper)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    viewModel.selectUpper.set(true);
                });

        RxView.clicks(binding.ibObjectiveLower)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    viewModel.selectUpper.set(false);
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
        return R.layout.warmer_layout_objective_jaunedice;
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