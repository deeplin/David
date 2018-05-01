package com.david.common.ui;

import android.databinding.ViewDataBinding;
import android.support.v4.view.ViewPager;
import android.view.ViewParent;

import com.david.common.util.Constant;

/**
 * author: Ling Lin
 * created on: 2017/7/29 14:50
 * email: 10525677@qq.com
 * description:
 */

public abstract class TabFragment<U extends ViewDataBinding> extends AutoAttachFragment<U> {

    public int currentPosition = Constant.SENSOR_NA_VALUE;
    protected IViewModel currentView = null;

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
        }
    }

    protected ViewPager.OnPageChangeListener getPageChangeListener(){
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

}
