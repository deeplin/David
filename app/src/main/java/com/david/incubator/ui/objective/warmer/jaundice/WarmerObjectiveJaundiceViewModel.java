package com.david.incubator.ui.objective.warmer.jaundice;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.dao.SystemSetting;
import com.david.common.ui.IViewModel;
import com.david.common.util.ResourceUtil;
import com.david.incubator.control.MainApplication;
import com.david.incubator.ui.home.warmer.JaunediceData;
import com.david.common.ui.ViewUtil;

import java.util.Locale;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/30 14:59
 * email: 10525677@qq.com
 * description:
 */
public class WarmerObjectiveJaundiceViewModel implements IViewModel {

    @Inject
    DaoControl daoControl;
    @Inject
    JaunediceData jaunediceData;

    public ObservableField<String> blueTime = new ObservableField<>();
    public ObservableBoolean selectUpper = new ObservableBoolean();
    public ObservableField<String> lowerValueField = new ObservableField<>();
    public ObservableBoolean selectOK = new ObservableBoolean();
    public ObservableBoolean valueChanged = new ObservableBoolean();

    private int lowerValue;
    private int lowerTopLimit = 99 * 60;
    private int lowerBottomLimit = 30;

    @Inject
    public WarmerObjectiveJaundiceViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    public synchronized void attach() {
        SystemSetting systemSetting = daoControl.getSystemSetting();
        int blueTimeValue = systemSetting.getBlueTime() / 60;
        blueTime.set(String.format(Locale.US, "%s %s h", ResourceUtil.getString(R.string.blue_time),
                String.format(Locale.US, "%d:%02d", blueTimeValue / 60, blueTimeValue % 60)));

        selectUpper.set(jaunediceData.isClockwiseSelected());
        lowerValue = jaunediceData.getCountdownMinute();
        lowerValueField.set(ViewUtil.formatJaunediceTime(lowerValue, true));
    }

    @Override
    public synchronized void detach() {
    }

    public synchronized void increaseValue() {
        if (!selectUpper.get()) {
            if (lowerValue < lowerTopLimit) {
                valueChanged.set(true);
                lowerValue += 30;
                lowerValueField.set(ViewUtil.formatJaunediceTime(lowerValue, true));
                selectOK.set(false);
            }
        }
    }

    public synchronized void decreaseValue() {
        if (!selectUpper.get()) {
            if (lowerValue > lowerBottomLimit) {
                valueChanged.set(true);
                lowerValue -= 30;
                lowerValueField.set(ViewUtil.formatJaunediceTime(lowerValue, true));
                selectOK.set(false);
            }
        }
    }

    public void setObjective() {
        jaunediceData.setClockwise(selectUpper.get(), lowerValue);
        valueChanged.set(false);
        selectOK.set(true);
    }
}