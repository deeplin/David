package com.david.incubator.ui.menu.chart.humidity;

import android.support.v4.content.ContextCompat;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.dao.AnalogCommand;
import com.david.common.dao.UserModel;
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
 * created on: 2018/1/1 11:29
 * email: 10525677@qq.com
 * description:
 */
public class HumidityChartViewModel extends BaseChartViewModel<AnalogCommand> {

    ShareMemory shareMemory;
    ModuleSoftware moduleSoftware;

    public HumidityChartViewModel(SensorChartView sensorChartView, PageTurnTable pageTurnTable,
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
        pageTurnTable.initialize(new HumidityDataRetriever());
    }

    @Override
    public int getYAxisTitle() {
        return R.mipmap.percentage_relative;
    }

    @Override
    public int getColor() {
        return ContextCompat.getColor(MainApplication.getInstance(), R.color.humidity);
    }

    @Override
    public double getChartData(AnalogCommand analogCommand) {
        if (analogCommand != null) {
            int value = analogCommand.getH1();
            if (value < SensorRange.HUMIDITY_DISPLAY_LOWER + 10) {
                value = SensorRange.HUMIDITY_DISPLAY_LOWER + 10;
            } else if (value > SensorRange.HUMIDITY_DISPLAY_UPPER) {
                value = SensorRange.HUMIDITY_DISPLAY_UPPER;
            }
            return (value / 10.0);
        }
        return 0.0;
    }

    @Override
    public double getObjective() {
        if (moduleSoftware.isHUM()) {
            return shareMemory.humidityObjective.get() / 10.0;
        }
        return Constant.SENSOR_NA_VALUE * 1.0;
    }
}