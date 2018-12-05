package com.david.incubator.ui.objective.cabin.humidity;

import android.content.Context;

import com.david.R;
import com.david.common.ui.FastUpdateLayout;
import com.david.common.util.Constant;
import com.david.databinding.LayoutObjectiveHumidityBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2017/12/30 12:07
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveHumidityLayout extends FastUpdateLayout<LayoutObjectiveHumidityBinding> {

    ObjectiveHumidityViewModel objectiveViewModel;

    public ObjectiveHumidityLayout(Context context, ObjectiveHumidityViewModel objectiveViewModel) {
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

        RxView.clicks(binding.btObjectiveOK)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.setObjective();
                });
    }

    public void setUnit(String value) {
        binding.tvObjectiveUnit.setText(value);
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
        return R.layout.layout_objective_humidity;
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