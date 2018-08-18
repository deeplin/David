package com.david.common.control;

import android.app.Application;

import com.david.incubator.control.ApplicationComponent;
import com.david.incubator.control.DaggerApplicationComponent;

/**
 * author: Ling Lin
 * created on: 2017/8/2 16:39
 * email: 10525677@qq.com
 * description:
 */

public abstract class MainApplication extends Application {

    private static MainApplication application;

    private ApplicationComponent applicationComponent;

    public static MainApplication getInstance() {
        return application;
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        applicationComponent = DaggerApplicationComponent.builder().build();
        start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    protected abstract void start();

    public abstract void stop();
}