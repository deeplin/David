package com.david.incubator.ui.menu.scale.chart;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.dao.WeightModel;
import com.david.common.util.ResourceUtil;
import com.david.common.util.TimeUtil;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.menu.chart.table.BaseDataRetriever;
import com.david.incubator.util.ViewUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/31 16:39
 * email: 10525677@qq.com
 * description:
 */


public class ScaleDataRetriever extends BaseDataRetriever<WeightModel> {

    @Inject
    DaoControl daoControl;

    public ScaleDataRetriever() {
        super();
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    protected String getSensorTitle() {
        return ResourceUtil.getString(R.string.weight);
    }

    @Override
    protected String getValue(WeightModel weightModel) {
        return ViewUtil.formatScaleValue(weightModel.getSC());
    }

    @Override
    protected List<WeightModel> getList(int rowSize, long currentId) {
        return daoControl.getWeightModel(rowSize, currentId);
    }

    @Override
    protected void addRecord(List<String> record, WeightModel weightModel) {
        if (rowId == 1) {
            currentId = weightModel.getId();
        }
        long time = weightModel.getTimeStamp() * 1000;
        record.add(TimeUtil.getTime(time, TimeUtil.DateTimeWithoutSecond));
        record.add(getValue(weightModel));
    }
}
