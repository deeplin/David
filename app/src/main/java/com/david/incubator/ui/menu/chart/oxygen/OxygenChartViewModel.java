package com.david.incubator.ui.menu.chart.oxygen;

import android.support.v4.content.ContextCompat;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.dao.AnalogCommand;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.common.util.Constant;
import com.david.common.util.SensorRange;
import com.david.incubator.ui.menu.chart.BaseChartViewModel;
import com.david.incubator.ui.menu.chart.SensorChartView;
import com.david.incubator.ui.menu.chart.chartview.AnalogChartWriter;
import com.david.incubator.ui.menu.chart.table.PageTurnTable;

/**
 * author: Ling Lin
 * created on: 2018/1/1 19:24
 * email: 10525677@qq.com
 * description:
 */

public class OxygenChartViewModel extends BaseChartViewModel<AnalogCommand> {

    ShareMemory shareMemory;
    ModuleSoftware moduleSoftware;

    public OxygenChartViewModel(SensorChartView sensorChartView, PageTurnTable pageTurnTable,
                                ShareMemory shareMemory, ModuleSoftware moduleSoftware) {
        super(new AnalogChartWriter(sensorChartView), pageTurnTable);
        this.shareMemory = shareMemory;
        this.moduleSoftware = moduleSoftware;
    }

    @Override
    public void initialize() {
        baseChartViewWriter.initializeYAxis(100.0, 0.0, 10.0, 2.0);
    }

    @Override
    public void initializePageTurnTable() {
        pageTurnTable.initialize(new OxygenDataRetriever());
    }

    @Override
    public String getYAxisTitle() {
        return "%";
    }

    @Override
    public int getColor() {
        return ContextCompat.getColor(MainApplication.getInstance(), R.color.oxygen);
    }

    @Override
    public double getChartData(AnalogCommand analogCommand) {
        if (analogCommand != null) {
            int value = analogCommand.getO2();
            if (value < SensorRange.OXYGEN_DISPLAY_LOWER + 10) {
                value = SensorRange.OXYGEN_DISPLAY_LOWER + 10;
            } else if (value > SensorRange.OXYGEN_DISPLAY_UPPER) {
                value = SensorRange.OXYGEN_DISPLAY_UPPER;
            }
            return (value / 10.0);
        }
        return 0.0;
    }

    @Override
    public double getObjective() {
        if (moduleSoftware.isO2()) {
            return shareMemory.oxygenObjective.get() / 10.0;
        }
        return Constant.SENSOR_NA_VALUE * 1.0;
    }
}
