package com.david.incubator.control;

import com.david.common.control.AutomationControl;
import com.david.common.control.MainApplication;
import com.david.incubator.ui.main.side.SideViewModel;
import com.david.incubator.ui.main.top.TopViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IncubatorAutomationControl extends AutomationControl {

    @Inject
    TopViewModel topViewModel;
    @Inject
    SideViewModel sideViewModel;

    @Inject
    public IncubatorAutomationControl() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Override
    protected void secondRefresh() {
        topViewModel.displayCurrentTime();
//        if (GPIOUtil.read()) {
//            sideViewModel.muteAlarm();
//        }
    }
}
