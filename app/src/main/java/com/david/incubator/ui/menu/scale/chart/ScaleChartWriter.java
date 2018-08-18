package com.david.incubator.ui.menu.scale.chart;


import com.david.common.control.DaoControl;
import com.david.common.dao.WeightModel;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;
import com.david.common.util.TimeUtil;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.menu.chart.IChartViewModel;
import com.david.incubator.ui.menu.chart.SensorChartView;
import com.david.incubator.ui.menu.chart.chartview.BaseChartViewWriter;

import java.util.List;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/1 10:45
 * email: 10525677@qq.com
 * description:
 */

public class ScaleChartWriter extends BaseChartViewWriter implements IViewModel {

    @Inject
    DaoControl daoControl;

    private int lastWeight = Constant.SENSOR_NA_VALUE;

    public ScaleChartWriter(SensorChartView sensorChartView) {
        super(sensorChartView);
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    public void draw(IChartViewModel chartViewModel, int limit, int step, int cycleTime) {
        List<WeightModel> weightModelList = daoControl.getWeightModel();
        if (sensorChartView != null) {
            if (weightModelList != null && weightModelList.size() > 0) {
                lastWeight = weightModelList.get(0).getSC();
                long lastTimeStamp = weightModelList.get(0).getTimeStamp();

                initializeXAxis(lastTimeStamp);

                fillData(chartViewModel, weightModelList, lastTimeStamp);
            } else {
                initializeXAxis(0);
            }
        }
    }

    protected void initializeXAxis(long lastTimeStamp) {
        sensorChartView.resetXAxis();
        int count = 5;
        for (int index = 1; index <= 12; index++) {
            if (index % 2 == 0) {
                if (lastTimeStamp > 0) {
                    String timeString = TimeUtil.getTime((lastTimeStamp - count * 2 * 24 * 3600) * 1000, TimeUtil.SimpleDate);
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

    private void fillData(IChartViewModel chartViewModel, List<WeightModel> weightModelList,
                          long lastTimeStamp) {
        /*设置数据*/
        dataSeries.clear();

        int weightModelId = weightModelList.size() - 1;
        for (int dateId = 11; dateId >= 0; dateId--) {
            long currentTimeStamp = lastTimeStamp - dateId * 24 * 3600;

            dataSeries.add(0.0);

            if (weightModelId < 0) {
                break;
            }

            while (weightModelId >= 0) {
                WeightModel weightModel = weightModelList.get(weightModelId);
                if (isSameDate(currentTimeStamp, weightModel.getTimeStamp())) {
                    //找到数据点，跳出
                    double data = chartViewModel.getChartData(weightModel);
                    dataSeries.set(11 - dateId, data);
                    weightModelId--;
                } else if (currentTimeStamp < weightModel.getTimeStamp()) {
                    //不符合，weightModel太新, 读取下一个weightModel,继续判断
                    break;
                } else {
                    //不符合，找不到当日数据
                    weightModelId --;
                    break;
                }
            }
        }
    }

    /*timeStamp默认单位为秒*/
    private boolean isSameDate(long timeStamp1, long timeStamp2) {
        String time1 = TimeUtil.getTime(timeStamp1 * 1000, TimeUtil.Date);
        String time2 = TimeUtil.getTime(timeStamp2 * 1000, TimeUtil.Date);
        return time1.equals(time2);
    }

    public int getLastWeight() {
        return lastWeight;
    }

    @Override
    public void attach() {
        Constant.AXIS_X_DOT_PER_STEP = 1;
    }

    @Override
    public void detach() {
        Constant.AXIS_X_DOT_PER_STEP = 10;
    }
}


