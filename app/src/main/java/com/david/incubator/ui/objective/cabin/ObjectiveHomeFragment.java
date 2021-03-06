package com.david.incubator.ui.objective.cabin;

import android.view.ViewGroup;

import com.david.R;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ShareMemory;
import com.david.common.ui.TabHomeFragment;
import com.david.databinding.FragmentObjectiveBinding;
import com.david.incubator.control.MainApplication;

import javax.inject.Inject;

public class ObjectiveHomeFragment extends TabHomeFragment<FragmentObjectiveBinding> {
    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware moduleHardware;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_objective;
    }

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    public void attach() {
        shareMemory.layoutLockable.set(true);
        ViewGroup.LayoutParams layoutParams = binding.tlObjective.getLayoutParams();
        ObjectivePagerAdapter pagerAdapter = new ObjectivePagerAdapter();
        layoutParams.width = 120 * pagerAdapter.getCount();

        binding.vpObjective.setAdapter(pagerAdapter);
        binding.tlObjective.setupWithViewPager(binding.vpObjective);

        binding.tlObjective.removeAllTabs();
        binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.celsius_small));

        if (moduleHardware.isHUM()) {
            binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.humidity));
        }

        if (moduleHardware.isO2()) {
            binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.o2));
        }

        if (moduleHardware.isSPO2()) {
            binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.spo2));
            binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.pr));
        }

        if (moduleHardware.isJaundiceInstalled()) {
            binding.tlObjective.addTab(buildIcon(binding.tlObjective, R.mipmap.jaunedice));
        }

        binding.vpObjective.addOnPageChangeListener(super.getPageChangeListener(binding.vpObjective));

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