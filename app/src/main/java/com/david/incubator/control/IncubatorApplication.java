package com.david.incubator.control;

import com.apkfuns.logutils.LogUtils;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.common.serial.SerialControl;
import com.david.common.serial.SerialMessageParser;
import com.david.common.util.Constant;
import com.david.common.util.LogUtil;

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
        MainApplication.getInstance().getApplicationComponent().inject(this);
        try {
            LogUtil.EnableLog();
            //todo
//            if (SystemConfig.RELEASE_TO_DAVID)
//                LogUtil.EnableLogToFile();

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
    protected void stop() {
        try {
            serialControl.stop();
            daoControl.stop();
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }
}
