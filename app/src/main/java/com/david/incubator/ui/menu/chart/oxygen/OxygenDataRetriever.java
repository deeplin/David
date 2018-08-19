package com.david.incubator.ui.menu.chart.oxygen;

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

public class OxygenDataRetriever extends AnalogDataRetriever {

    @Override
    protected String getSensorTitle() {
        return ResourceUtil.getString(R.string.oxygen);
    }

    @Override
    protected String getValue(AnalogCommand analogCommand) {
        return ViewUtil.formatOxygenValue(analogCommand.getO2());
    }
}