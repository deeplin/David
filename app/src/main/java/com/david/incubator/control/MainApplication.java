package com.david.incubator.control;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.david.common.control.DaoControl;
import com.david.common.data.ModuleHardware;
import com.david.common.serial.SerialControl;
import com.david.common.serial.SerialMessageParser;
import com.david.common.util.Constant;
import com.david.common.util.FileUtil;
import com.david.common.util.LogUtil;
import com.wanjian.cockroach.Cockroach;

import javax.inject.Inject;

public class MainApplication extends Application{

    @Inject
    ModuleHardware moduleHardware;
    @Inject
    DaoControl daoControl;
    @Inject
    SerialControl serialControl;
    @Inject
    SerialMessageParser serialMessageHandler;
//    @Inject
//    LocationControl locationControl;
//    @Inject
//    CloudClient cloudClient;
//    @Inject
//    CloudMessageParser cloudMessageParser;

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
        MainApplication.getInstance().getApplicationComponent().inject(this);
        start();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    private void start() {
        Cockroach.install((thread, throwable) -> new Handler(Looper.getMainLooper()).post(() -> {
            try {
                LogUtils.e("--->CockroachException:" + thread + "<---", throwable);
                LogUtils.e(throwable);
                Toast.makeText(MainApplication.this, "Exception \n" + thread + "\n" + throwable.toString(), Toast.LENGTH_SHORT).show();
            } catch (Throwable e) {
            }
        }));

        try {
            LogUtil.EnableLog();
            //todo
            if (Constant.RELEASE_TO_DAVID)
                LogUtil.EnableLogToFile();

            moduleHardware.load();
            daoControl.start(this);

            FileUtil.setLanguage(this, daoControl);
        } catch (Exception e) {
            LogUtils.e(e);
            System.exit(-1);
        }
        try {
//            if (moduleHardware.isEnableCloud()) {
//                locationControl.start();
//                cloudClient.start(moduleHardware.getCloudServerIP(),
//                        moduleHardware.getCloudServerPort(), cloudMessageParser);
//            }

            serialControl.start(Constant.SERIAL_BUFFER_SIZE, serialMessageHandler);
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    public void stop() {
        try {
            serialControl.stop();
            daoControl.stop();
            Cockroach.uninstall();
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }
}