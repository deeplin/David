package com.david.incubator.ui.user.usermodel;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.ui.TabHomeLayout;
import com.david.common.util.Constant;
import com.david.databinding.LayoutChartBinding;
import com.david.incubator.ui.menu.chart.ChartPagerAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

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
    UserModelDetailViewModel userModelDetailViewModel;

    public ChartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        RxView.clicks(binding.btReturn)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> userModelDetailViewModel.showSignsOfData.set(false));
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
        binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.heating));

        if (isCabin) {
            if (moduleHardware.isHUM())
                binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.humidity));
            if (moduleHardware.isO2())
                binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.o2));
        }

        if (moduleHardware.isSPO2()) {
            binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.spo2));
            binding.tlChart.addTab(buildIcon(binding.tlChart, R.mipmap.o2));
        }

        binding.vpChart.addOnPageChangeListener(super.getPageChangeListener(binding.vpChart));
        binding.tlChart.getTabAt(0).select();
    }

    public void detach() {
        super.detach();
    }
}
