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

    @Override
    protected void init() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chart;
    }

    @Override
    public void attach() {
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
