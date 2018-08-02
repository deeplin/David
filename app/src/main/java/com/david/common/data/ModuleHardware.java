package com.david.common.data;

import com.david.R;
import com.david.common.serial.BaseSerialMessage;
import com.david.common.serial.command.module.ModuleGetHardwareCommand;
import com.david.common.serial.command.module.ModuleGetSoftwareCommand;
import com.david.common.util.Constant;
import com.david.common.util.ResourceUtil;

import java.net.InetAddress;
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
    private String deviceModel;

    public boolean isUser() {
        return user;
    }

    public boolean isInst() {
        return inst;
    }

    private boolean user;
    private boolean inst;

//    private boolean enableCloud;
//    private InetAddress cloudServerIP;
//    private int cloudServerPort;

    @Inject
    public ModuleHardware() {
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
            ModuleGetHardwareCommand moduleGetSoftwareCommand = (ModuleGetHardwareCommand) baseSerialMessage;
            cameraInstalled = moduleGetSoftwareCommand.getCAMERA() == 1;
            jaundiceInstalled = moduleGetSoftwareCommand.getBLUE() == 1;
            user = moduleGetSoftwareCommand.getUSER() == 1;
            inst = moduleGetSoftwareCommand.getINST() == 1;

            //todo
            if(Constant.RELEASE_TO_DAVID) {
                deviceModel = moduleGetSoftwareCommand.getMODEL();
            }else{
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

    public boolean is2000S(){
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
}