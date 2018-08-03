package com.david.common.control;

import android.app.Application;

import com.david.common.dao.SystemSetting;
import com.david.common.mode.LanguageMode;
import com.david.common.util.ResourceUtil;
import com.david.incubator.control.ApplicationComponent;
import com.david.incubator.control.DaggerApplicationComponent;

import java.util.Locale;

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

    protected void setLanguage(DaoControl daoControl) {
        SystemSetting sensorRange = daoControl.getSystemSetting();
        if (sensorRange.getLanguageIndex() == LanguageMode.English.getIndex()) {
            ResourceUtil.setLocalLanguage(this, Locale.ENGLISH);
        } else if (sensorRange.getLanguageIndex() == LanguageMode.Chinese.getIndex()) {
            ResourceUtil.setLocalLanguage(this, Locale.SIMPLIFIED_CHINESE);
        } else if (sensorRange.getLanguageIndex() == LanguageMode.Turkish.getIndex()) {
            Locale turkish = new Locale("tr", "TR");
            ResourceUtil.setLocalLanguage(this, turkish);
        } else if (sensorRange.getLanguageIndex() == LanguageMode.Polish.getIndex()) {
            Locale spanish = new Locale("pl", "PL");
            ResourceUtil.setLocalLanguage(this, spanish);
        }  else if (sensorRange.getLanguageIndex() == LanguageMode.Russia.getIndex()) {
            Locale russia = new Locale("ru", "RU");
            ResourceUtil.setLocalLanguage(this, russia);
        }
    }
}