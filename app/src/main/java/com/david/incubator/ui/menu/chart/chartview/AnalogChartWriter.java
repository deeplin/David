package com.david.incubator.ui.menu.chart.chartview;

import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.AnalogCommand;
import com.david.incubator.ui.menu.chart.IChartViewModel;
import com.david.incubator.ui.menu.chart.SensorChartView;

import java.util.List;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/1 10:45
 * email: 10525677@qq.com
 * description:
 */

public class AnalogChartWriter extends BaseChartViewWriter {

    @Inject
    DaoControl daoControl;

    public AnalogChartWriter(SensorChartView sensorChartView) {
        super(sensorChartView);
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    public void draw(IChartViewModel chartViewModel, int limit, int step, int cycleTime) {
        List<AnalogCommand> analogCommandList = daoControl.getAnalogCommand(limit);
        if (sensorChartView != null) {
            if (analogCommandList != null && analogCommandList.size() > 0) {
                long lastTimeStamp = analogCommandList.get(0).getTimeStamp();
                initializeXAxis(lastTimeStamp, step);

                fillData(chartViewModel, analogCommandList, lastTimeStamp, cycleTime);
                displayObjectiveLine(chartViewModel.getObjective());
            } else {
                initializeXAxis(0, step);
            }
        }
    }

    private void fillData(IChartViewModel chartViewModel, List<AnalogCommand> analogCommandList,
                          long lastTimeStamp, int cycleTime) {
        /*设置数据*/
        dataSeries.clear();

        int analogListId = analogCommandList.size() - 1;
        for (int dateId = 119; dateId >= 0; dateId--) {
            long currentTimeStamp = lastTimeStamp - dateId * 60 * cycleTime;

            if (analogListId < 0) {
                double data = chartViewModel.getChartData(null);
                dataSeries.add(data);
                break;
            }

            while (analogListId >= 0) {
                AnalogCommand analogCommand = analogCommandList.get(analogListId);
                if (analogCommand.getTimeStamp() == currentTimeStamp) {
//                    LogUtils.e(x + " match add " + listId + " " + new Date(currentTimeStamp * 1000).toString() + " "
//                            + new Date(analogCommand.getTimeStamp() * 1000).toString());
                    //找到数据点，跳出
                    double data = chartViewModel.getChartData(analogCommand);
//                    Log.e("deeplin2", "" + data);
                    dataSeries.add(data);
                    analogListId--;
                    break;
                } else if (analogCommand.getTimeStamp() > currentTimeStamp) {
//                    LogUtils.e(x + " less add " + listId + " " + new Date(currentTimeStamp * 1000).toString() + " "
//                            + new Date(analogCommand.getTimeStamp() * 1000).toString());
                    //不符合，插入0点，跳出
                    double data = chartViewModel.getChartData(null);
                    dataSeries.add(data);
                    break;
                } else {
//                    LogUtils.e(x + " great " + listId + " " + new Date(currentTimeStamp * 1000).toString() + " "
//                            + new Date(analogCommand.getTimeStamp() * 1000).toString());
                    analogListId--;
                }
            }
        }
    }
}