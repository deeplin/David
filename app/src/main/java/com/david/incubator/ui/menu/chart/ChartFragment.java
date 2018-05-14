package com.david.incubator.ui.menu.chart;

import android.view.ViewGroup;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.ui.TabHomeFragment;
import com.david.databinding.FragmentChartBinding;
import com.david.incubator.ui.main.IFragmentLockable;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

/**
 * author: Ling Lin
 * created on: 2017/12/31 15:34
 * email: 10525677@qq.com
 * description:
 */
public class ChartFragment extends TabHomeFragment<FragmentChartBinding> implements IFragmentLockable {

    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    ModuleSoftware moduleSoftware;
    @Inject
    DaoControl daoControl;

    protected Disposable refreshDisposable;

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart;
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

    @Override
    public void detach() {
        super.detach();
        if (refreshDisposable != null) {
            refreshDisposable.dispose();
            refreshDisposable = null;
        }
    }
}
