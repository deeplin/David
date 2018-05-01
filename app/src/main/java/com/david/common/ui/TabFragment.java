package com.david.common.ui;

import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.david.common.util.Constant;

/**
 * author: Ling Lin
 * created on: 2017/7/29 14:50
 * email: 10525677@qq.com
 * description:
 */

public class TabFragment extends Fragment {

    protected int oldPosition = Constant.SENSOR_NA_VALUE;
    protected IViewModel oldViewModel = null;
    protected boolean firstTag = true;

    protected void detach(ViewPager viewPager) {
        int childCount = viewPager.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View view = viewPager.getChildAt(index);
            if (view != null && ((int) view.getTag() == oldPosition)) {
                if (view instanceof ITabConstraintLayout) {
                    ITabConstraintLayout tabLayout = (ITabConstraintLayout) view;
                    tabLayout.stopDisposable();
                    tabLayout.detach();
                } else if (view instanceof IViewModel) {
                    IViewModel viewModel = (IViewModel) view;
                    viewModel.detach();
                }
            }
        }
    }

    protected ViewPager.OnPageChangeListener getPageChangeListener(ViewPager viewPager) {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (firstTag) {
                    firstTag = false;
                    oldPosition = position;
                    View view = viewPager.getChildAt(0);
                    if (view instanceof IViewModel) {
                        oldViewModel = (IViewModel) view;
                        oldViewModel.attach();
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                int childCount = viewPager.getChildCount();
                if (oldPosition != position) {
                    for (int index = 0; index < childCount; index++) {
                        View view = viewPager.getChildAt(index);
                        if (view != null) {
                            int tag = (int) view.getTag();
                            if (tag == oldPosition && view instanceof IViewModel) {
                                IViewModel viewModel = (IViewModel) view;
                                viewModel.detach();
                                oldViewModel = null;
                            }
                            if (tag == position && view instanceof IViewModel) {
                                IViewModel viewModel = (IViewModel) view;
                                viewModel.attach();
                                oldViewModel = viewModel;
                            }
                        }
                    }
                    oldPosition = position;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    int childCount = viewPager.getChildCount();
                    for (int index = 0; index < childCount; index++) {
                        View view = viewPager.getChildAt(index);
                        if (view != null) {
                            if (view instanceof ITabConstraintLayout) {
                                ((ITabConstraintLayout) view).stopDisposable();
                            }
                        }
                    }
                }
            }
        };
    }
}
