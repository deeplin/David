package com.david.incubator.ui.objective.cabin;

import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ShareMemory;
import com.david.common.ui.IViewModel;
import com.david.common.ui.TabFragment;
import com.david.databinding.IncubatorFragmentObjectiveBinding;

import javax.inject.Inject;

public class ObjectiveFragment extends TabFragment<IncubatorFragmentObjectiveBinding> {
    @Inject
    ShareMemory shareMemory;
    @Inject
    ModuleHardware hardwareConfig;

    @Override
    protected int getLayoutId() {
        return R.layout.incubator_fragment_objective;
    }

    @Override
    protected void init() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    public void attach() {
        ViewGroup.LayoutParams layoutParams = binding.tlObjective.getLayoutParams();
        ObjectivePagerAdapter pagerAdapter = new ObjectivePagerAdapter();
        layoutParams.width = 150 * pagerAdapter.getCount();

        binding.vpObjective.setAdapter(pagerAdapter);
        binding.tlObjective.setupWithViewPager(binding.vpObjective);

        binding.tlObjective.removeAllTabs();
        binding.tlObjective.addTab(buildIcon(R.mipmap.celsius_small));

        if (hardwareConfig.isHUM()) {
            binding.tlObjective.addTab(buildIcon(R.mipmap.humidity));
        }

        if (hardwareConfig.isO2()) {
            binding.tlObjective.addTab(buildIcon((R.mipmap.o2)));
        }

        if (hardwareConfig.isSPO2()) {
            binding.tlObjective.addTab(buildIcon((R.mipmap.spo2)));
            binding.tlObjective.addTab(buildIcon((R.mipmap.pr)));
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

        super.firstTag = true;
    }

    private TabLayout.Tab buildIcon(int icon) {
        TabLayout.Tab tab = binding.tlObjective.newTab();
        ImageView imageView = new ImageView(this.getContext());
        imageView.setImageResource(icon);
        tab.setCustomView(imageView);
        return tab;
    }
}
