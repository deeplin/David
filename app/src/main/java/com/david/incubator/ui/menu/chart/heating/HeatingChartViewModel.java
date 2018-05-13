package com.david.incubator.ui.menu.chart.heating;

import android.support.v4.content.ContextCompat;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.dao.StatusCommand;
import com.david.common.data.ShareMemory;
import com.david.common.util.Constant;
import com.david.incubator.ui.menu.chart.BaseChartViewModel;
import com.david.incubator.ui.menu.chart.SensorChartView;
import com.david.incubator.ui.menu.chart.chartview.StatusChartWriter;
import com.david.incubator.ui.menu.chart.table.PageTurnTable;

/**
 * author: Ling Lin
 * created on: 2018/1/1 11:29
 * email: 10525677@qq.com
 * description:
 */

public class HeatingChartViewModel extends BaseChartViewModel<StatusCommand> {

    ShareMemory shareMemory;

    public HeatingChartViewModel(SensorChartView sensorChartView, PageTurnTable pageTurnTable, ShareMemory shareMemory) {
        super(new StatusChartWriter(sensorChartView), pageTurnTable);
        this.shareMemory = shareMemory;
    }

    @Override
    public void initialize() {
        baseChartViewWriter.initializeYAxis(100.0, 0.0, 10.0, 2.0);
    }

    @Override
    public void initializePageTurnTable() {
        pageTurnTable.initialize(new HeatingDataRetriever(this.shareMemory.isCabin()));
    }

    @Override
    public int getYAxisTitle() {
        return R.mipmap.percentage;
    }

    @Override
    public int getColor() {
        return ContextCompat.getColor(MainApplication.getInstance(), R.color.warmer);
    }

    @Override
    public double getChartData(StatusCommand statusCommand) {
        if (statusCommand != null) {
            int value;
            if (shareMemory.isCabin()) {
                value = statusCommand.getInc();
            } else if (shareMemory.isWarmer()) {
                value = statusCommand.getWarm();
            } else {
                value = 1;
            }
            if (value < 1)
                value = 1;
            return value;
        }
        return 0.0;
    }

    @Override
    public double getObjective() {
        return Constant.SENSOR_NA_VALUE * 1.0;
    }
}
