package com.david.incubator.ui.menu.scale;

import com.david.R;
import com.david.common.data.ShareMemory;
import com.david.common.ui.TabHomeFragment;
import com.david.databinding.FragmentScaleBinding;
import com.david.incubator.control.MainApplication;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/26 19:13
 * email: 10525677@qq.com
 * description:
 */

public class ScaleFragment extends TabHomeFragment<FragmentScaleBinding> {

    @Inject
    ScaleViewModel scaleViewModel;
    @Inject
    ShareMemory shareMemory;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_scale;
    }

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(scaleViewModel);
    }

    @Override
    public void attach() {
        shareMemory.layoutLockable.set(false);
        binding.sllLeft.attach();
        scaleViewModel.attach();

        ScalePagerAdapter pagerAdapter = new ScalePagerAdapter(binding.scvScale, binding.pttScale);
        binding.vpScale.setAdapter(pagerAdapter);
        binding.tlScale.setupWithViewPager(binding.vpScale);

        binding.tlScale.removeAllTabs();
        binding.tlScale.addTab(buildIcon(binding.tlScale, R.mipmap.chart));
        binding.vpScale.addOnPageChangeListener(super.getPageChangeListener(binding.vpScale));
        binding.tlScale.getTabAt(0).select();
    }

    @Override
    public void detach() {
        super.detach();
        scaleViewModel.detach();
        binding.sllLeft.detach();
    }
}
