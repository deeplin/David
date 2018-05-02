package com.david.common.ui;

import android.databinding.ViewDataBinding;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.david.common.util.Constant;

/**
 * author: Ling Lin
 * created on: 2017/7/29 14:50
 * email: 10525677@qq.com
 * description:
 */

public abstract class TabFragment<U extends ViewDataBinding> extends AutoAttachFragment<U> {

    protected int currentPosition = Constant.SENSOR_NA_VALUE;
    protected IViewModel currentView = null;
    protected boolean firstTag = true;

    @Override
    public void detach() {
        if (currentView != null) {
            if (currentView instanceof ITabConstraintLayout) {
                ITabConstraintLayout tabLayout = (ITabConstraintLayout) currentView;
                tabLayout.stopDisposable();
                tabLayout.detach();
            } else {
                currentView.detach();
            }
            currentView = null;
            currentPosition = Constant.SENSOR_NA_VALUE;
            firstTag = true;
        }
    }

    protected ViewPager.OnPageChangeListener getPageChangeListener(ViewPager viewPager) {
        viewPager.setOffscreenPageLimit(1);
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (firstTag) {
                    firstTag = false;
                    attachView(viewPager, position);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (currentPosition != position) {
                    if (currentView != null) {
                        currentView.detach();
                        currentView = null;
                    }
                    attachView(viewPager, position);
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

    private void attachView(ViewPager viewPager, int position) {
        int childCount = viewPager.getChildCount();
        for (int index = 0; index < childCount; index++) {
            View view = viewPager.getChildAt(index);
            if (view != null) {
                int tag = (int) view.getTag();
                if (tag == position && view instanceof IViewModel) {
                    currentView = (IViewModel) view;
                    currentView.attach();
                }
            }
        }
        currentPosition = position;
    }
}
