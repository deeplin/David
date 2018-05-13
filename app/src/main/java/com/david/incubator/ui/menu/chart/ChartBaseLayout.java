package com.david.incubator.ui.menu.chart;

import android.content.Context;

import com.david.R;
import com.david.common.ui.TabConstraintLayout;
import com.david.common.util.Constant;
import com.david.databinding.LayoutChartBaseBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2018/1/1 11:27
 * email: 10525677@qq.com
 * description:
 */

public class ChartBaseLayout extends TabConstraintLayout<LayoutChartBaseBinding> implements IRefreshableViewModel {

    BaseChartViewModel viewModel;

    public ChartBaseLayout(Context context, BaseChartViewModel viewModel) {
        super(context);
        this.viewModel = viewModel;

        binding.setViewModel(viewModel);

        RxView.clicks(binding.ibChartCycleUp)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> viewModel.increase());

        RxView.clicks(binding.ibChartCycleDown)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> viewModel.decrease());

        RxView.clicks(binding.btChartPrevious)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> viewModel.goPrevious());

        RxView.clicks(binding.btChartNext)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> viewModel.goNext());
    }

    @Override
    public void attach() {
        viewModel.attach();
    }

    @Override
    public void detach() {
        viewModel.detach();
    }

    @Override
    public void refresh() {
        viewModel.refresh();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_chart_base;
    }
}
