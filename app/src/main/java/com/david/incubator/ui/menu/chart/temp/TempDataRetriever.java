package com.david.incubator.ui.menu.chart.temp;

import com.david.R;
import com.david.common.dao.AnalogCommand;
import com.david.common.util.ResourceUtil;
import com.david.common.util.TimeUtil;
import com.david.incubator.ui.menu.chart.table.AnalogDataRetriever;
import com.david.common.ui.ViewUtil;

import java.util.List;

/**
 * author: Ling Lin
 * created on: 2017/7/31 16:55
 * email: 10525677@qq.com
 * description:
 */

public class TempDataRetriever extends AnalogDataRetriever {

    @Override
    protected void buildHead() {
        headList.add(ResourceUtil.getString(R.string.time));
        headList.add(ResourceUtil.getString(R.string.skin1));
        headList.add(ResourceUtil.getString(R.string.skin2));
        headList.add(ResourceUtil.getString(R.string.air));
    }

    @Override
    protected void addRecord(List<String> record, AnalogCommand analogCommand) {
        if (rowId == 1) {
            currentId = analogCommand.getId();
        }
        long time = analogCommand.getTimeStamp() * 1000;
        record.add(TimeUtil.getTime(time, TimeUtil.DateTimeWithoutSecond));
        record.add(ViewUtil.formatTempValue(analogCommand.getS1B()));
        record.add(ViewUtil.formatTempValue(analogCommand.getS2()));
        record.add(ViewUtil.formatTempValue(analogCommand.getA2()));
    }

    @Override
    protected String getSensorTitle() {
        return null;
    }

    @Override
    protected String getValue(AnalogCommand analogCommand) {
        return null;
    }
}
