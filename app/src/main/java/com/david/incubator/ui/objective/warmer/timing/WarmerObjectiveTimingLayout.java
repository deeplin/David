package com.david.incubator.ui.objective.warmer.timing;

import android.content.Context;

import com.david.R;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.databinding.WarmerLayoutObjectiveTimingBinding;
import com.david.incubator.control.MainApplication;
import com.david.common.util.TimingData;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/5 18:34
 * email: 10525677@qq.com
 * description:
 */
public class WarmerObjectiveTimingLayout extends BindingConstraintLayout<WarmerLayoutObjectiveTimingBinding> {

    @Inject
    TimingData timingData;

    public WarmerObjectiveTimingLayout(Context context) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        RxView.clicks(binding.ibObjectiveApgar)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    synchronized (this) {
                        if (!binding.ibObjectiveApgar.isSelected()) {
                            binding.ibObjectiveApgar.setSelected(true);
                            binding.ibObjectiveCpr.setSelected(false);
                            timingData.setApgar();
                        }
                    }
                });

        RxView.clicks(binding.ibObjectiveCpr)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((Object aVoid) -> {
                    synchronized (this) {
                        if (!binding.ibObjectiveCpr.isSelected()) {
                            binding.ibObjectiveApgar.setSelected(false);
                            binding.ibObjectiveCpr.setSelected(true);
                            timingData.setCpr();
                        }
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.warmer_layout_objective_timing;
    }

    @Override
    public void attach() {
        binding.ibObjectiveApgar.setSelected(timingData.isApgarSelected());
        binding.ibObjectiveCpr.setSelected(!timingData.isApgarSelected());
    }

    @Override
    public void detach() {
    }
}