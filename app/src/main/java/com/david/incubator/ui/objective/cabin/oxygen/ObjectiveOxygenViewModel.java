package com.david.incubator.ui.objective.cabin.oxygen;

import com.david.common.mode.FunctionMode;
import com.david.incubator.ui.objective.cabin.humidity.ObjectiveHumidityViewModel;
import com.david.incubator.util.ViewUtil;

/**
 * author: Ling Lin
 * created on: 2017/12/30 14:37
 * email: 10525677@qq.com
 * description:
 */
public class ObjectiveOxygenViewModel extends ObjectiveHumidityViewModel {

    public ObjectiveOxygenViewModel(){
        super();
    }

    protected boolean getEnable() {
        return moduleSoftware.isO2();
    }

    protected int getValue() {
        return shareMemory.oxygenObjective.get();
    }

    protected void setLimit() {
        upperLimit = systemSetting.getOxygenUpper();
        lowerLimit = systemSetting.getOxygenLower();
    }

    protected String getFunctionName() {
        return FunctionMode.Oxygen.getName();
    }

    protected String getValueString() {
        return ViewUtil.formatOxygenValue(value);
    }
}
