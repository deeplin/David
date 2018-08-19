package com.david.incubator.ui.objective.cabin.oxygen;

import com.david.common.mode.FunctionMode;
import com.david.incubator.ui.objective.cabin.humidity.ObjectiveHumidityViewModel;
import com.david.common.ui.ViewUtil;

/**
 * author: Ling Lin
 * created on: 2017/12/30 14:37
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveOxygenViewModel extends ObjectiveHumidityViewModel {

    public ObjectiveOxygenViewModel() {
        super();
    }

    @Override
    protected boolean getEnable() {
        return moduleSoftware.isO2();
    }

    @Override
    protected int getValue() {
        return shareMemory.oxygenObjective.get();
    }

    @Override
    protected void setLimit() {
        upperLimit = systemSetting.getOxygenUpper();
        lowerLimit = systemSetting.getOxygenLower();
    }

    @Override
    protected String getFunctionName() {
        return FunctionMode.Oxygen.getName();
    }

    @Override
    protected String getValueString() {
        return ViewUtil.formatOxygenValue(value);
    }
}
