package com.david.common.data;

import com.david.R;
import com.david.common.serial.BaseSerialMessage;
import com.david.common.serial.command.module.ModuleGetSoftwareCommand;
import com.david.common.util.Constant;
import com.david.common.util.ResourceUtil;

import java.net.InetAddress;

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
    private boolean enableCloud;
    private InetAddress cloudServerIP;
    private int cloudServerPort;

    @Inject
    public ModuleHardware() {
    }

    public void load() throws Exception {
        enableCloud = ResourceUtil.getBool(R.bool.enable_cloud);
        cloudServerIP = InetAddress.getByName(ResourceUtil.getString(R.string.cloud_ip));
        cloudServerPort = ResourceUtil.getInt(R.integer.cloud_port);
    }

    public boolean isCameraInstalled() {
        return cameraInstalled;
    }

    public boolean isEnableCloud() {
        return enableCloud;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public InetAddress getCloudServerIP() {
        return cloudServerIP;
    }

    public int getCloudServerPort() {
        return cloudServerPort;
    }

    public boolean isJaundiceInstalled() {
        return jaundiceInstalled;
    }

    @Override
    public void accept(Boolean aBoolean, BaseSerialMessage baseSerialMessage) {
        if (aBoolean) {
            ModuleGetSoftwareCommand moduleGetSoftwareCommand = (ModuleGetSoftwareCommand) baseSerialMessage;
            cameraInstalled = moduleGetSoftwareCommand.getCAMERA() == 1;
            jaundiceInstalled = moduleGetSoftwareCommand.getBLUE() == 1;
            deviceModel = moduleGetSoftwareCommand.getMODEL();

            super.accept(true, baseSerialMessage);
        }
    }

    public boolean showLED37() {
        if (deviceModel.equals(Constant.HKN93S)) {
            return false;
        } else {
            return true;
        }
    }
}