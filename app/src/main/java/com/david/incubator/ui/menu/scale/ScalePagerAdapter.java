package com.david.incubator.ui.menu.scale;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.david.incubator.ui.menu.chart.SensorChartView;
import com.david.incubator.ui.menu.chart.table.PageTurnTable;

/**
 * author: Ling Lin
 * created on: 2018/1/1 22:43
 * email: 10525677@qq.com
 * description:
 */
public class ScalePagerAdapter extends PagerAdapter {

    private SensorChartView sensorChartView;
    private PageTurnTable pageTurnTable;

    ScalePagerAdapter(SensorChartView sensorChartView, PageTurnTable pageTurnTable) {
        this.sensorChartView = sensorChartView;
        this.pageTurnTable = pageTurnTable;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * 实例化 一个 页卡
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;

        switch (position) {
//            case 0:
//                ScaleChartViewModel scaleChartViewModel = new ScaleChartViewModel(
//                        this.sensorChartView, this.pageTurnTable);
//                view = new ScaleChartLayout(container.getContext(), scaleChartViewModel);
//                break;
            default:
                view = new View(container.getContext());
                break;
        }

        container.addView(view);
        view.setTag(position);
        return view;
    }
}