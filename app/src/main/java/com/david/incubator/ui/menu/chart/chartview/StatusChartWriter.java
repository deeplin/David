package com.david.incubator.ui.menu.chart.chartview;

import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.StatusCommand;
import com.david.incubator.ui.menu.chart.IChartViewModel;
import com.david.incubator.ui.menu.chart.SensorChartView;

import java.util.List;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/7/31 16:55
 * email: 10525677@qq.com
 * description:
 */

public class StatusChartWriter extends BaseChartViewWriter {

    @Inject
    DaoControl daoControl;

    public StatusChartWriter(SensorChartView sensorChartView) {
        super(sensorChartView);
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    public void draw(IChartViewModel chartViewModel, int limit, int step, int cycleTime) {
        List<StatusCommand> statusCommandList = daoControl.getStatusCommand(limit);
        if (sensorChartView != null) {
            if (statusCommandList != null && statusCommandList.size() > 0) {
                long lastTimeStamp = statusCommandList.get(0).getTimeStamp();
                initializeXAxis(lastTimeStamp, step);

                fillData(chartViewModel, statusCommandList, lastTimeStamp, cycleTime);
                displayObjectiveLine(chartViewModel.getObjective());
            } else {
                initializeXAxis(0, step);
            }
        }
    }

    private void fillData(IChartViewModel chartViewModel, List<StatusCommand> statusCommandList,
                          long lastTimeStamp, int cycleTime) {
        /*设置数据*/
        dataSeries.clear();

        int statusListId = statusCommandList.size() - 1;
        for (int dateId = 119; dateId >= 0; dateId--) {
            long currentTimeStamp = lastTimeStamp - dateId * 60 * cycleTime;

            if (statusListId < 0) {
                double data = chartViewModel.getChartData(null);
                dataSeries.add(data);
                break;
            }

            while (statusListId >= 0) {
                StatusCommand analogCommand = statusCommandList.get(statusListId);
                if (analogCommand.getTimeStamp() == currentTimeStamp) {
//                    LogUtils.e(x + " match add " + listId + " " + new Date(currentTimeStamp * 1000).toString() + " "
//                            + new Date(analogCommand.getTimeStamp() * 1000).toString());
                    //找到数据点，跳出
                    double data = chartViewModel.getChartData(analogCommand);
//                    Log.e("deeplin2", "" + data);
                    dataSeries.add(data);
                    statusListId--;
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
                    statusListId--;
                }
            }
        }
    }
}
