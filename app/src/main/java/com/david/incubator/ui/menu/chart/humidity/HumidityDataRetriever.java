package com.david.incubator.ui.menu.chart.humidity;

import com.david.R;
import com.david.common.dao.AnalogCommand;
import com.david.common.util.ResourceUtil;
import com.david.incubator.ui.menu.chart.table.AnalogDataRetriever;
import com.david.common.ui.ViewUtil;

/**
 * author: Ling Lin
 * created on: 2017/7/31 16:55
 * email: 10525677@qq.com
 * description:
 */

public class HumidityDataRetriever extends AnalogDataRetriever {

    public HumidityDataRetriever() {
        super();
    }

    @Override
    protected String getSensorTitle() {
        return ResourceUtil.getString(R.string.humidity);
    }

    @Override
    protected String getValue(AnalogCommand analogCommand) {
        return ViewUtil.formatHumidityValue(analogCommand.getH1());
    }
}
