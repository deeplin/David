package com.david.common.data;

import com.david.R;
import com.david.common.serial.BaseSerialMessage;
import com.david.common.serial.command.module.ModuleGetHardwareCommand;
import com.david.common.util.Constant;
import com.david.common.util.ResourceUtil;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;


/**
 * author: Ling Lin
 * created on: 2017/7/17 11:56
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class ModuleHardware extends ModuleSoftware {

    private boolean cameraInstalled;
    private boolean jaundiceInstalled;
    private boolean matInstalled;
    private String deviceModel;
    private boolean user;
    private boolean inst;

//    private boolean enableCloud;
//    private InetAddress cloudServerIP;
//    private int cloudServerPort;

    @Inject
    public ModuleHardware() {
        //todo
        user = true;
    }

    public void load() throws Exception {
//        enableCloud = ResourceUtil.getBool(R.bool.enable_cloud);
//        cloudServerIP = InetAddress.getByName(ResourceUtil.getString(R.string.cloud_ip));
//        cloudServerPort = ResourceUtil.getInt(R.integer.cloud_port);
    }

    public boolean isCameraInstalled() {
        return cameraInstalled;
    }

    public boolean isSPO2() {
        return super.SPO2;
    }

    public boolean isSCALE() {
        return super.SCALE;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

//    public boolean isEnableCloud() {
//        return enableCloud;
//    }
//
//    public InetAddress getCloudServerIP() {
//        return cloudServerIP;
//    }
//
//    public int getCloudServerPort() {
//        return cloudServerPort;
//    }

    public boolean isJaundiceInstalled() {
        return jaundiceInstalled;
    }

    @Override
    public void accept(Boolean aBoolean, BaseSerialMessage baseSerialMessage) {
        if (aBoolean) {
            ModuleGetHardwareCommand moduleGetHardwareCommand = (ModuleGetHardwareCommand) baseSerialMessage;
            cameraInstalled = moduleGetHardwareCommand.getCAMERA() == 1;
            jaundiceInstalled = moduleGetHardwareCommand.getBLUE() == 1;
            matInstalled = moduleGetHardwareCommand.getMAT() == 1;
            user = moduleGetHardwareCommand.getUSER() == 1;
            inst = moduleGetHardwareCommand.getINST() == 1;

            //todo
            user = true;

            if (moduleGetHardwareCommand instanceof Object) {
                Object getHardwareCommand = (Object) moduleGetHardwareCommand;

            }
            //todo
            if (Constant.RELEASE_TO_DAVID) {
                deviceModel = moduleGetHardwareCommand.getMODEL();
            } else {
                deviceModel = ResourceUtil.getString(R.string.device_model);
            }
            super.accept(true, baseSerialMessage);
        }
    }

    public boolean is93S() {
        if (Objects.equals(deviceModel, Constant.HKN93S)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean is2000S() {
        if (Objects.equals(deviceModel, Constant.YP2000S)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean is970SAndJaundice() {
        if (Objects.equals(deviceModel, Constant.YP970S) && jaundiceInstalled) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isUser() {
        return user;
    }

    public boolean isInst() {
        return inst;
    }

    public boolean isMatInstalled() {
        return matInstalled;
    }
}