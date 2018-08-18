package com.david.incubator.ui.menu.chart.spo2;

import android.support.v4.content.ContextCompat;
import com.david.R;
import com.david.common.dao.AnalogCommand;
import com.david.common.util.Constant;
import com.david.common.util.SensorRange;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.menu.chart.BaseChartViewModel;
import com.david.incubator.ui.menu.chart.SensorChartView;
import com.david.incubator.ui.menu.chart.chartview.AnalogChartWriter;
import com.david.incubator.ui.menu.chart.table.PageTurnTable;

/**
 * author: Ling Lin
 * created on: 2018/1/1 19:39
 * email: 10525677@qq.com
 * description:
 */
public class Spo2ChartViewModel extends BaseChartViewModel<AnalogCommand> {

    public Spo2ChartViewModel(SensorChartView sensorChartView, PageTurnTable pageTurnTable) {
        super(new AnalogChartWriter(sensorChartView), pageTurnTable);
    }

    @Override
    public void initialize() {
        baseChartViewWriter.initializeYAxis(100.0, 0.0, 10.0, 2.0);
    }

    @Override
    public void initializePageTurnTable() {
        pageTurnTable.initialize(new Spo2DataRetriever());
    }

    @Override
    public String getYAxisTitle() {
        return "%";
    }

    @Override
    public int getColor() {
        return ContextCompat.getColor(MainApplication.getInstance(), R.color.spo2);
    }

    @Override
    public double getChartData(AnalogCommand analogCommand) {
        if (analogCommand != null) {
            int value = analogCommand.getSP();
            if (value < SensorRange.SPO2_DISPLAY_LOWER) {
                value = SensorRange.SPO2_DISPLAY_LOWER;
            } else if (value > SensorRange.SPO2_DISPLAY_UPPER) {
                value = SensorRange.SPO2_DISPLAY_UPPER;
            }
            return (value / 10.0);
        }
        return 0.0;
    }

    @Override
    public double getObjective() {
        return Constant.SENSOR_NA_VALUE * 1.0;
    }
}