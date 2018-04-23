package com.david.incubator.control;

import com.david.common.control.MainApplication;

public class IncubatorApplication extends MainApplication{

    public IncubatorApplication(){
        MainApplication.getInstance().getApplicationComponent().inject(this);

    }

    @Override
    protected void start() {

    }

    @Override
    protected void stop() {

    }
}
