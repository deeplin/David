package com.david;

import android.app.Application;

public class TestApplication extends Application {

    private static TestApplication application;

    private TestComponent component;

    public static TestApplication getInstance(){
        return application;
    }

    public TestComponent getTestComponent() {
        return component;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        //Test
//        component = DaggerTe.builder().build();
    }
}
