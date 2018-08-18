package com.david.incubator.ui.menu.chart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.ui.TabHomeLayout;
import com.david.common.util.Constant;
import com.david.databinding.LayoutChartBinding;
import com.david.common.data.SelectedUser;
import com.david.incubator.control.MainApplication;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class ChartLayout extends TabHomeLayout<LayoutChartBinding> {

    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;
    @Inject
    DaoControl daoControl;
    @Inject
    public SelectedUser selectedUser;

    protected Disposable refreshDisposable;

    public ChartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(this);

        RxView.clicks(binding.btReturn)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    selectedUser.showDetail.set(false);
                    ChartLayout.this.setVisibility(View.GONE);
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_chart;
    }

    @Override
    public void attach() {
        boolean isCabin = shareMemory.isCabin();
        ChartPagerAdapter pagerAdapter = new ChartPagerAdapter(isCabin,
                binding.scvChart, binding.pttChart,
                moduleHardware, shareMemory, moduleSoftware, daoControl);

        ViewGroup.LayoutParams layoutParams = binding.tlChart.getLayoutParams();
        layoutParams.width = 100 * pagerAdapter.getCount();
        binding.vpChart.setAdapter(pagerAdapter);
        binding.tlChart.setupWithViewPager(binding.vpChart);

        binding.tlChart.removeAllTabs();
        binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.celsius_small));

        if (isCabin) {
            binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.heating));
            if (moduleHardware.isHUM())
                binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.humidity));
            if (moduleHardware.isO2())
                binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.o2));
        }else{
            binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.radiation));
        }

        if (moduleHardware.isSPO2()) {
            binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.spo2));
            binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.pr));
        }
        binding.vpChart.addOnPageChangeListener(super.getPageChangeListener(binding.vpChart));
        binding.tlChart.getTabAt(0).select();

        //refresh
        Calendar calendar = Calendar.getInstance();
        int firstDelay = 60 - calendar.get(Calendar.SECOND) + 1;
        io.reactivex.Observable<Long> observable = io.reactivex.Observable.interval(firstDelay, 60, TimeUnit.SECONDS);
        refreshDisposable = observable
                .subscribe((aLong) -> {
                    if (currentView != null && currentView instanceof IRefreshableViewModel) {
                        ((IRefreshableViewModel) currentView).refresh();
                    }
                });
    }

    public void detach() {
        super.detach();
        if (refreshDisposable != null) {
            refreshDisposable.dispose();
            refreshDisposable = null;
        }
    }
}