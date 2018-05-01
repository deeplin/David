package com.david.incubator.ui.objective.cabin;

import android.view.View;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ShareMemory;
import com.david.common.ui.PreloadFragment;
import com.david.databinding.IncubatorFragmentObjectiveBinding;

import javax.inject.Inject;

public class ObjectiveFragment extends PreloadFragment<IncubatorFragmentObjectiveBinding> {
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

    }

    @Override
    public void detach() {

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
