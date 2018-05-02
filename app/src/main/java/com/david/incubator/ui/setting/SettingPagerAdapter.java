package com.david.incubator.ui.setting;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * author: Ling Lin
 * created on: 2018/1/3 17:51
 * email: 10525677@qq.com
 * description:
 */
public class SettingPagerAdapter extends PagerAdapter {

    /**
     * 返回 页卡 的数量
     */
    @Override
    public int getCount() {
        return 6;
    }

    /**
     * 判断 是 view 是否来自对象
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    /**
     * 实例化 一个 页卡
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        switch (position) {
//            case 0:
//                view = new SettingAddPatientLayout(container.getContext());
//                break;
//            case 1:
//                view = new SettingComfortZoneLayout(container.getContext());
//                break;
//            case 2:
//                view = new SettingCalibrationLayout(container.getContext());
//                break;
//            case 3:
//                view = new SettingIntroductionLayout(container.getContext());
//                break;
//            case 4:
//                view = new SettingDeviceLayout(container.getContext());
//                break;
//            case 5:
//                view = new SettingHomeLayout(container.getContext());
//                break;
            default:
                view = new View(container.getContext());
                break;
        }
        container.addView(view);
        view.setTag(position);
        return view;
    }

    /**
     * 销毁 一个 页卡
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
