package com.david.incubator.ui.menu.scale.chart;

import android.content.Context;
import android.databinding.Observable;

import com.david.R;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;
import com.david.databinding.LayoutScaleChartBinding;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.menu.chart.IRefreshableViewModel;
import com.david.incubator.ui.menu.scale.ScaleViewModel;
import com.david.common.ui.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

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
                    binding.btScale2.setEnabled(false);
                    io.reactivex.Observable
                            .interval(0, 1, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .take(6)
                            .subscribe(bLong -> {
                                switch (bLong.intValue()) {
                                    case 0:
                                        scaleChartViewModel.setLastWeight("----");
                                        break;
                                    case 1:
                                        scaleChartViewModel.setLastWeight("");
                                        break;
                                    case 2:
                                        scaleViewModel.saveConstantWeight(0);
                                        scaleChartViewModel.setLastWeight("----");
                                        break;
                                    case 3:
                                        scaleViewModel.saveConstantWeight(1);
                                        scaleChartViewModel.setLastWeight("");
                                        break;
                                    case 4:
                                        scaleViewModel.saveConstantWeight(2);
                                        scaleChartViewModel.setLastWeight("----");
                                        break;
                                    case 5:
                                        scaleViewModel.saveConstantWeight(3);
                                        scaleChartViewModel.refresh();
                                        binding.btScale2.setEnabled(true);
                                        break;
                                }
                            });
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
