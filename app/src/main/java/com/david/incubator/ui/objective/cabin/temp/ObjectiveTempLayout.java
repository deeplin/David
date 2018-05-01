package com.david.incubator.ui.objective.cabin.temp;

import android.content.Context;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.mode.CtrlMode;
import com.david.common.ui.FastIncreaseConstraintLayout;
import com.david.common.util.Constant;
import com.david.databinding.IncubatorLayoutObjectiveTempBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/29 19:23
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveTempLayout extends FastIncreaseConstraintLayout<IncubatorLayoutObjectiveTempBinding> {

    @Inject
    ObjectiveTempViewModel objectiveViewModel;

    public ObjectiveTempLayout(Context context) {
        super(context);

        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(objectiveViewModel);

        setButton(binding.ibObjectiveUp, binding.ibObjectiveDown);

        RxView.clicks(binding.ibObjectiveFirst)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.setEnable(CtrlMode.Air);
                });

        RxView.clicks(binding.ibObjectiveSecond)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.setEnable(CtrlMode.Skin);
                });

        RxView.clicks(binding.ibObjectiveAbove37)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    stopDisposable();
                    objectiveViewModel.shareMemory.above37.set(true);
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
        return R.layout.incubator_layout_objective_temp;
    }

    @Override
    public void attach() {
        objectiveViewModel.attach();
    }

    @Override
    public void detach() {
        stopDisposable();
        objectiveViewModel.detach();
    }
}
