package com.david.incubator.ui.menu.spo2.setting;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: Ling Lin
 * created on: 2018/1/2 22:31
 * email: 10525677@qq.com
 * description:
 */
public class Spo2SettingPagerAdapter extends PagerAdapter {

    Spo2SettingPagerAdapter() {
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;

        switch (position) {
            case 0:
                view = new Spo2SettingSensLayout(container.getContext());
                break;
            case 1:
                view = new Spo2SettingAverageTimeLayout(container.getContext());
                break;
            case 2:
                view = new Spo2SettingFastSATLayout(container.getContext());
                break;
            default:
                view = new View(container.getContext());
                break;
        }
        container.addView(view);
        view.setTag(position);
        return view;
    }
}