package com.david.incubator.ui.menu.chart;

import com.david.R;
import com.david.common.data.ShareMemory;
import com.david.common.ui.TabHomeFragment;
import com.david.databinding.FragmentChartBinding;
import com.david.incubator.control.MainApplication;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/31 15:34
 * email: 10525677@qq.com
 * description:
 */
public class ChartFragment extends TabHomeFragment<FragmentChartBinding> {

    @Inject
    ShareMemory shareMemory;

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
        shareMemory.layoutLockable.set(false);
        binding.sllLeft.attach();
        binding.clChart.attach();
    }

    @Override
    public void detach() {
        super.detach();
        binding.clChart.detach();
        binding.sllLeft.detach();
    }
}
