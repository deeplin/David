package com.david.common.control;

import android.app.Application;
import com.apkfuns.logutils.LogUtils;
import com.david.common.dao.SystemSetting;
import com.david.common.mode.LanguageMode;
import com.david.common.util.ResourceUtil;

import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/8/2 16:39
 * email: 10525677@qq.com
 * description:
 */

public abstract class MainApplication extends Application{

    private static MainApplication application;

    private ApplicationComponent applicationComponent;

    public static MainApplication getInstance() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        applicationComponent = DaggerApplicationComponent.builder().build();
        applicationComponent.inject(this);

        start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        try {
            stop();
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    protected abstract void start();

    protected  abstract void stop();

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

    protected void setLanguage(DaoControl daoControl) {
        SystemSetting sensorRange = daoControl.getSystemSetting();
        if (sensorRange.getLanguageIndex() == LanguageMode.English.getIndex()) {
            ResourceUtil.setLocalLanguage(this, Locale.ENGLISH);
        } else if (sensorRange.getLanguageIndex() == LanguageMode.Chinese.getIndex()) {
            ResourceUtil.setLocalLanguage(this, Locale.SIMPLIFIED_CHINESE);
        }
    }
}
