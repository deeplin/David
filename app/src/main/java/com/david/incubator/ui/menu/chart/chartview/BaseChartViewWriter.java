package com.david.incubator.ui.menu.chart.chartview;

import android.view.View;

import com.david.common.util.Constant;
import com.david.common.util.TimeUtil;
import com.david.incubator.ui.menu.chart.IChartViewModel;
import com.david.incubator.ui.menu.chart.SensorChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Ling Lin
 * created on: 2018/1/1 10:45
 * email: 10525677@qq.com
 * description:
 */

public abstract class BaseChartViewWriter {

    protected final List<Double> dataSeries;
    protected SensorChartView sensorChartView;

    public BaseChartViewWriter(SensorChartView sensorChartView) {
        dataSeries = new ArrayList<>();
        this.sensorChartView = sensorChartView;
    }

    public abstract void draw(IChartViewModel chartViewModel, int limit, int step, int cycleTime);

    public void initializeYAxis(double max, double min, double step, double detailStep) {
        sensorChartView.setYAxisLabels(max, min, step, detailStep);
    }

    public void clearXAxis(){
        sensorChartView.resetXAxis();
    }

    protected void initializeXAxis(long lastTimeStamp, int step) {
        sensorChartView.resetXAxis();
        int count = 5;
        for (int index = 1; index <= 120; index++) {
            if (index % (2 * Constant.AXIS_X_DOT_PER_STEP) == 0) {
                if (lastTimeStamp > 0) {
                    String timeString = TimeUtil.getTime((lastTimeStamp - count * step) * 1000, TimeUtil.HourMinute);
                    count--;
                    sensorChartView.addXAxisLabel(timeString);
                } else {
                    sensorChartView.addXAxisLabel("");
                }
            } else {
                sensorChartView.addXAxisLabel("");
            }
        }
    }

    public void removeAllLine() {
        sensorChartView.clearAllDataSet();
    }

    public void addLine(int color) {
        sensorChartView.addDataSet(dataSeries, color);
    }

    public void addLine(int color, List<Double> series) {
        sensorChartView.addDataSet(series, color);
    }

    protected void displayObjectiveLine(double objective) {
        sensorChartView.setObjectiveLines(objective);
    }

    public void setVisibility(boolean visibility) {
        if (visibility)
            sensorChartView.setVisibility(View.VISIBLE);
        else
            sensorChartView.setVisibility(View.GONE);
    }

    public void postInvalidate() {
        sensorChartView.postInvalidate();
    }
}