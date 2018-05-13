package com.david.incubator.ui.menu.chart.table;


import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.AnalogCommand;
import com.david.common.util.TimeUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/31 16:39
 * email: 10525677@qq.com
 * description:
 */

public abstract class AnalogDataRetriever extends BaseDataRetriever<AnalogCommand> {

    @Inject
    DaoControl daoControl;

    public AnalogDataRetriever() {
        super();
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    protected List<AnalogCommand> getList(int rowSize, long currentId) {
        return daoControl.getAnalogCommand(rowSize, currentId);
    }

    @Override
    protected void addRecord(List<String> record, AnalogCommand analogCommand) {
        if (rowId == 1) {
            currentId = analogCommand.getId();
        }
        long time = analogCommand.getTimeStamp() * 1000;
        record.add(TimeUtil.getTime(time, TimeUtil.DateTimeWithoutSecond));
        record.add(getValue(analogCommand));
    }
}
