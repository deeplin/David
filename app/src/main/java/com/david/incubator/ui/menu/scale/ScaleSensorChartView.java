package com.david.incubator.ui.menu.scale;

import android.content.Context;
import android.util.AttributeSet;

import com.david.incubator.ui.menu.chart.SensorChartView;

/**
 * author: Ling Lin
 * created on: 2018/1/1 10:46
 * email: 10525677@qq.com
 * description:
 */

public class ScaleSensorChartView extends SensorChartView {

    public ScaleSensorChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected int getLeftOffset() {
        return 70;
    }
}
