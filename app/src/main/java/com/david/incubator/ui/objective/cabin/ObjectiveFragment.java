package com.david.incubator.ui.objective.cabin;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ShareMemory;
import com.david.common.ui.AutoAttachFragment;
import com.david.databinding.IncubatorFragmentObjectiveBinding;

import javax.inject.Inject;

public class ObjectiveFragment extends AutoAttachFragment<IncubatorFragmentObjectiveBinding> {
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

        binding.vpObjective.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("2", "" + position);
                binding.vpObjective.getChildAt(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

//        fragmentObjectiveBinding.tabObjective.setOnPageChangeListener(
//                super.getPageChangeListener(fragmentObjectiveBinding.vpObjective));
//
//        oldPosition = 0;
//        int tagId = shareMemory.functionTag.get();
//        if (tagId >= 10) {
//            /*根据按键设置页面*/
//            int tabId = pagerAdapter.getPosition(tagId % 10);
//            if (tabId > 0)
//                oldPosition = tabId;
//        }
//
//        fragmentObjectiveBinding.tabObjective.setViewPager(fragmentObjectiveBinding.vpObjective, oldPosition);
//        fragmentObjectiveBinding.vpObjective.setOffscreenPageLimit(1);

//        binding.tlObjective.getTabAt(0).select();
    }

    private TabLayout.Tab buildIcon(int icon) {
        TabLayout.Tab tab = binding.tlObjective.newTab();
        ImageView imageView = new ImageView(this.getContext());
        imageView.setImageResource(icon);
        tab.setCustomView(imageView);
        return tab;
    }

    @Override
    public void detach() {
        Log.e("2", "detach");
    }
}
