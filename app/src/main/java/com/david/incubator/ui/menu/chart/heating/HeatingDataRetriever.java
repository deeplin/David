package com.david.incubator.ui.menu.chart.heating;

import com.david.R;
import com.david.common.dao.StatusCommand;
import com.david.common.util.ResourceUtil;
import com.david.incubator.ui.menu.chart.table.StatusDataRetriever;

/**
 * author: Ling Lin
 * created on: 2017/7/31 16:55
 * email: 10525677@qq.com
 * description:
 */

public class HeatingDataRetriever extends StatusDataRetriever {

    private boolean isCabin;

    public HeatingDataRetriever(boolean isCabin) {
        super();
        this.isCabin = isCabin;
    }

    @Override
    protected String getSensorTitle() {
        return ResourceUtil.getString(R.string.heating_power);
    }

    @Override
    protected String getValue(StatusCommand statusCommand) {
        if (isCabin) {
            return String.valueOf(statusCommand.getInc());
        } else {
            return String.valueOf(statusCommand.getWarm());
        }
    }
}
