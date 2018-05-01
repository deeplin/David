package com.david.incubator.ui.objective.cabin;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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
        Log.e("objective", "init");
    }

    @Override
    public void attach() {
        ViewGroup.LayoutParams layoutParams = binding.tlObjective.getLayoutParams();

        ObjectivePagerAdapter pagerAdapter = new ObjectivePagerAdapter();
        layoutParams.width = 150 * pagerAdapter.getCount();

        binding.vpObjective.setAdapter(pagerAdapter);
        binding.tlObjective.setupWithViewPager(binding.vpObjective);
//        binding.tlObjective.setTabMode(TabLayout.MODE_FIXED);
//        binding.tlObjective.setTabGravity(TabLayout.GRAVITY_CENTER);

        binding.tlObjective.removeAllTabs();
        binding.tlObjective.addTab(binding.tlObjective.newTab().setIcon(R.mipmap.celsius_small));
        binding.tlObjective.addTab(binding.tlObjective.newTab().setIcon(R.mipmap.humidity));
        binding.tlObjective.addTab(binding.tlObjective.newTab().setIcon(R.mipmap.o2));
        binding.tlObjective.addTab(binding.tlObjective.newTab().setIcon(R.mipmap.spo2));
        binding.tlObjective.addTab(binding.tlObjective.newTab().setIcon(R.mipmap.pr));

        Log.e("objective", "attach");
    }

    @Override
    public void detach() {
        Log.e("objective", "detach");
    }

    private void buildTabPage(View view) {
//        ViewGroup.LayoutParams layoutParams = fragmentObjectiveBinding.tabObjective.getLayoutParams();
//        layoutParams.width = 150 * pagerAdapter.getCount();
//
//        fragmentObjectiveBinding.vpObjective.setAdapter(pagerAdapter);
//        final List<NavigationTabBar.Model> models = new ArrayList<>();
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        ContextCompat.getDrawable(view.getContext(), R.mipmap.celsius_small),
//                        ContextCompat.getColor(view.getContext(), R.color.button))
//                        .build());
//
//        if (hardwareConfig.isHUM()) {
//            models.add(
//                    new NavigationTabBar.Model.Builder(
//                            ContextCompat.getDrawable(view.getContext(), R.mipmap.humidity),
//                            ContextCompat.getColor(view.getContext(), R.color.button))
//                            .build()
//            );
//        }
//
//        if (hardwareConfig.isO2()) {
//            models.add(
//                    new NavigationTabBar.Model.Builder(
//                            ContextCompat.getDrawable(view.getContext(), R.mipmap.o2),
//                            ContextCompat.getColor(view.getContext(), R.color.button))
//                            .build()
//            );
//        }
//
//        if (hardwareConfig.isSPO2()) {
//            models.add(
//                    new NavigationTabBar.Model.Builder(
//                            ContextCompat.getDrawable(view.getContext(), R.mipmap.spo2),
//                            ContextCompat.getColor(view.getContext(), R.color.button))
//                            .build()
//            );
//            models.add(
//                    new NavigationTabBar.Model.Builder(
//                            ContextCompat.getDrawable(view.getContext(), R.mipmap.pr),
//                            ContextCompat.getColor(view.getContext(), R.color.button))
//                            .build()
//            );
//        }
//
//        fragmentObjectiveBinding.tabObjective.setModels(models);
//        fragmentObjectiveBinding.tabObjective.setBgColor(ContextCompat.getColor(view.getContext(), R.color.background));
//
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
    }


}
