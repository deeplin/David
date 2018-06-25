package com.david.incubator.ui.menu.chart.pr;

import android.support.v4.content.ContextCompat;
import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.dao.AnalogCommand;
import com.david.common.util.Constant;
import com.david.common.util.SensorRange;
import com.david.incubator.ui.menu.chart.BaseChartViewModel;
import com.david.incubator.ui.menu.chart.SensorChartView;
import com.david.incubator.ui.menu.chart.chartview.AnalogChartWriter;
import com.david.incubator.ui.menu.chart.table.PageTurnTable;

/**
 * author: Ling Lin
 * created on: 2018/1/1 19:47
 * email: 10525677@qq.com
 * description:
 */
public class PrChartViewModel extends BaseChartViewModel<AnalogCommand> {

    public PrChartViewModel(SensorChartView sensorChartView, PageTurnTable pageTurnTable) {
        super(new AnalogChartWriter(sensorChartView), pageTurnTable);
    }

    @Override
    public void initialize() {
        baseChartViewWriter.initializeYAxis(SensorRange.PR_DISPLAY_UPPER, 0.0, 20.0, 2.0);
    }

    @Override
    public void initializePageTurnTable() {
        pageTurnTable.initialize(new PrDataRetriever());
    }

    @Override
    public String getYAxisTitle() {
        return "bpm";
    }

    @Override
    public int getColor() {
        return ContextCompat.getColor(MainApplication.getInstance(), R.color.spo2);
    }

    @Override
    public double getChartData(AnalogCommand analogCommand) {
        if (analogCommand != null) {
            double value = analogCommand.getPR();
            if (value < SensorRange.PR_DISPLAY_LOWER) {
                value = SensorRange.PR_DISPLAY_LOWER;
            } else if (value > SensorRange.PR_DISPLAY_UPPER) {
                value = SensorRange.PR_DISPLAY_UPPER;
            }
            return (value / 1.0);
        }
        return 0.0;
    }

    @Override
    public double getObjective() {
        return Constant.SENSOR_NA_VALUE * 1.0;
    }
}