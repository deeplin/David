package com.david.incubator.ui.menu.chart.table;

import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.StatusCommand;
import com.david.common.util.TimeUtil;

import java.util.List;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/31 16:39
 * email: 10525677@qq.com
 * description:
 */


public abstract class StatusDataRetriever extends BaseDataRetriever<StatusCommand> {

    @Inject
    DaoControl daoControl;

    public StatusDataRetriever() {
        super();
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    protected List<StatusCommand> getList(int rowSize, long currentId) {
        return daoControl.getStatusCommand(rowSize, currentId);
    }

    @Override
    protected void addRecord(List<String> record, StatusCommand statusCommand) {
        if (rowId == 1) {
            currentId = statusCommand.getId();
        }
        long time = statusCommand.getTimeStamp() * 1000;
        record.add(TimeUtil.getTime(time, TimeUtil.DateTimeWithoutSecond));
        record.add(getValue(statusCommand));
    }
}
