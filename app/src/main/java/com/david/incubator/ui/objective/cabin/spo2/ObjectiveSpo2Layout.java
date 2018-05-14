package com.david.incubator.ui.objective.cabin.spo2;

import android.content.Context;

import com.david.R;
import com.david.common.ui.FastIncreaseConstraintLayout;
import com.david.common.util.Constant;
import com.david.databinding.LayoutObjectiveSpo2Binding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2017/12/30 14:58
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveSpo2Layout extends FastIncreaseConstraintLayout<LayoutObjectiveSpo2Binding> {
    ObjectiveSpo2ViewModel objectiveViewModel;

    public ObjectiveSpo2Layout(Context context, ObjectiveSpo2ViewModel objectiveViewModel) {
        super(context);
        this.objectiveViewModel = objectiveViewModel;

        binding.setViewModel(objectiveViewModel);

        setButton(binding.ibObjectiveUp, binding.ibObjectiveDown);

        RxView.clicks(binding.ibObjectiveFirst)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.setEnable(true);
                });

        RxView.clicks(binding.ibObjectiveSecond)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.setEnable(false);
                });

        RxView.clicks(binding.ibObjectiveUpper)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.selectUpper.set(true);
                });

        RxView.clicks(binding.ibObjectiveLower)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.selectUpper.set(false);
                });

        RxView.clicks(binding.ibObjectiveUpperValue)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.selectUpper.set(true);
                });

        RxView.clicks(binding.ibObjectiveLowerValue)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.selectUpper.set(false);
                });

        RxView.clicks(binding.btObjectiveOK)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.setObjective();
                });
    }

    @Override
    protected void increaseValue() {
        objectiveViewModel.increaseValue();
    }

    @Override
    protected void decreaseValue() {
        objectiveViewModel.decreaseValue();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_objective_spo2;
    }

    @Override
    public void attach() {
        objectiveViewModel.attach();
    }

    @Override
    public void detach() {
        super.stopDisposable();
        objectiveViewModel.detach();
    }
}
