package com.david.incubator.ui.menu.chart.temp;

import android.app.AlertDialog;
import android.content.Context;

import com.david.R;
import com.david.common.ui.IViewModel;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.ResourceUtil;
import com.david.databinding.LayoutChartTempBinding;
import com.david.incubator.ui.menu.chart.IRefreshableViewModel;
import com.david.incubator.util.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

/**
 * author: Ling Lin
 * created on: 2018/1/1 20:08
 * email: 10525677@qq.com
 * description:
 */

public class ChartTempLayout extends BindingConstraintLayout<LayoutChartTempBinding> implements IRefreshableViewModel, IViewModel {

    TempChartViewModel chartTempViewModel;
    private AlertDialog alertDialog = null;

    public ChartTempLayout(Context context, TempChartViewModel chartTempViewModel) {
        super(context);

        this.chartTempViewModel = chartTempViewModel;

        binding.setViewModel(chartTempViewModel);

        RxView.clicks(binding.layoutChartBase.ibChartCycleUp)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> chartTempViewModel.increase());

        RxView.clicks(binding.layoutChartBase.ibChartCycleDown)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> chartTempViewModel.decrease());

        RxView.clicks(binding.layoutChartBase.btChartPrevious)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> chartTempViewModel.goPrevious());

        RxView.clicks(binding.layoutChartBase.btChartNext)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> chartTempViewModel.goNext());

        RxView.clicks(binding.btChartTempAir)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    synchronized (this) {
                        binding.btChartTempAir.setClickable(false);
                        boolean status = !chartTempViewModel.airSelected.get();
                        binding.btChartTempAir.setSelected(status);
                        chartTempViewModel.airSelected.set(status);
                        binding.btChartTempAir.setClickable(true);
                    }
                });

        RxView.clicks(binding.btChartTempSkin1)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    synchronized (this) {
                        binding.btChartTempSkin1.setClickable(false);
                        boolean status = !chartTempViewModel.skin1Selected.get();
                        binding.btChartTempSkin1.setSelected(status);
                        chartTempViewModel.skin1Selected.set(status);
                        binding.btChartTempSkin1.setClickable(true);
                    }
                });

        RxView.clicks(binding.btChartTempSkin2)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    synchronized (this) {
                        binding.btChartTempSkin2.setClickable(false);
                        boolean status = !chartTempViewModel.skin2Selected.get();
                        binding.btChartTempSkin2.setSelected(status);
                        chartTempViewModel.skin2Selected.set(status);
                        binding.btChartTempSkin2.setClickable(true);
                    }
                });

//        RxView.clicks(binding.btChartTempDelete)
//                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
//                .subscribe((aVoid) -> {
//                    alertDialog = ViewUtil.buildConfirmDialog(this.getContext(), R.string.delete,
//                            ResourceUtil.getString(R.string.delete_confirm),
//                            (dialog, which) -> {
//                                chartTempViewModel.delete();
//                            });
//                });

        binding.btChartTempSkin1.setSelected(chartTempViewModel.skin1Selected.get());
        binding.btChartTempSkin2.setSelected(chartTempViewModel.skin2Selected.get());
        binding.btChartTempAir.setSelected(chartTempViewModel.skin2Selected.get());
    }

    @Override
    public void attach() {
        binding.layoutChartBase.switchButton.setChecked(false);
        chartTempViewModel.attach();
    }

    @Override
    public void detach() {
        chartTempViewModel.detach();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Override
    public void refresh() {
        chartTempViewModel.refresh();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_chart_temp;
    }
}
