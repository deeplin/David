package com.david.incubator.ui.objective.warmer;

import android.view.ViewGroup;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ShareMemory;
import com.david.common.ui.TabHomeFragment;
import com.david.databinding.FragmentObjectiveBinding;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/5 15:49
 * email: 10525677@qq.com
 * description:
 */
public class WarmerObjectiveHomeFragment extends TabHomeFragment<FragmentObjectiveBinding> {

    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_objective;
    }

    @Override
    public void attach() {
        ViewGroup.LayoutParams layoutParams = binding.tlObjective.getLayoutParams();
        WarmerObjectivePagerAdapter pagerAdapter = new WarmerObjectivePagerAdapter();
        layoutParams.width = 150 * pagerAdapter.getCount();

        binding.vpObjective.setAdapter(pagerAdapter);
        binding.tlObjective.setupWithViewPager(binding.vpObjective);

        binding.tlObjective.removeAllTabs();
        binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.celsius_small));
        binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.beep));
        if (moduleHardware.isSPO2()) {
            binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.spo2));
            binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.pr));
        }
        if (moduleHardware.isJaundiceInstalled()) {
            binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.jaunedice));
        }
        binding.vpObjective.addOnPageChangeListener(super.getPageChangeListener(binding.vpObjective));

        currentPosition = 0;
        int tagId = shareMemory.functionTag.get();
        if (tagId >= 10) {
            /*根据按键设置页面*/
            tagId = pagerAdapter.getPosition(tagId % 10);
            if (tagId > 0)
                currentPosition = tagId;
        }
        binding.tlObjective.getTabAt(currentPosition).select();
    }
}
