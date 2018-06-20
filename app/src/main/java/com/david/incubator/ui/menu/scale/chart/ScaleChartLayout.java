package com.david.incubator.ui.menu.scale.chart;

import android.content.Context;
import android.databinding.Observable;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.ui.IViewModel;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.incubator.ui.menu.chart.IRefreshableViewModel;
import com.david.incubator.ui.menu.scale.ScaleViewModel;
import com.david.incubator.util.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;
import com.david.databinding.LayoutScaleChartBinding;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class ScaleChartLayout extends BindingConstraintLayout<LayoutScaleChartBinding> implements IViewModel, IRefreshableViewModel {

    @Inject
    ScaleViewModel scaleViewModel;

    ScaleChartViewModel scaleChartViewModel;

    public ScaleChartLayout(Context context, ScaleChartViewModel scaleChartViewModel) {
        super(context);
        this.scaleChartViewModel = scaleChartViewModel;
        MainApplication.getInstance().getApplicationComponent().inject(this);

        scaleViewModel.recordedWeight.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                scaleChartViewModel.setLastWeight(ViewUtil.formatScaleValue(scaleViewModel.recordedWeight.get()));
            }
        });

        binding.setViewModel(scaleChartViewModel);

        RxView.clicks(binding.btScalePrevious)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> scaleChartViewModel.goPrevious());

        RxView.clicks(binding.btScaleNext)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> scaleChartViewModel.goNext());

        RxView.clicks(binding.btScale)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> scaleViewModel.raiseBaby());

        RxView.clicks(binding.btScale2)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    scaleViewModel.saveWeight();
                    scaleChartViewModel.refresh();
                });
    }

    @Override
    public void attach() {
        scaleChartViewModel.attach();
    }

    @Override
    public void detach() {
        scaleChartViewModel.detach();
    }

    @Override
    public void refresh() {
        scaleChartViewModel.refresh();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_scale_chart;
    }
}
