package com.david.common.ui;

import android.databinding.ViewDataBinding;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

/**
 * author: Ling Lin
 * created on: 2017/7/29 14:50
 * email: 10525677@qq.com
 * description:
 */

public abstract class TabHomeFragment<U extends ViewDataBinding> extends BindingFragment<U> {

    protected int currentPosition = 0;
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
            currentPosition = 0;
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

    protected TabLayout.Tab buildIcon(TabLayout tabLayout, int icon) {
        TabLayout.Tab tab = tabLayout.newTab();
        ImageView imageView = new ImageView(this.getContext());
        imageView.setImageResource(icon);
        tab.setCustomView(imageView);
        return tab;
    }
}