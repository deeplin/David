package com.david.incubator.control;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.david.common.control.AutomationControl;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.serial.SerialControl;
import com.david.common.serial.SerialMessageParser;
import com.david.common.util.Constant;
import com.david.common.util.LogUtil;
import com.wanjian.cockroach.Cockroach;

import javax.inject.Inject;

public class IncubatorApplication extends MainApplication {

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

    @Override
    protected void start() {
        Cockroach.install((thread, throwable) -> new Handler(Looper.getMainLooper()).post(() -> {
            try {
                LogUtils.e("--->CockroachException:" + thread + "<---", throwable);
                LogUtils.e(throwable);
                Toast.makeText(IncubatorApplication.this, "Exception \n" + thread + "\n" + throwable.toString(), Toast.LENGTH_SHORT).show();
            } catch (Throwable e) {
            }
        }));
        MainApplication.getInstance().getApplicationComponent().inject(this);

        try {
            LogUtil.EnableLog();
            //todo
            if (Constant.RELEASE_TO_DAVID)
                LogUtil.EnableLogToFile();

            moduleHardware.load();
            daoControl.start(this);

            super.setLanguage(daoControl);
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

    @Override
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
