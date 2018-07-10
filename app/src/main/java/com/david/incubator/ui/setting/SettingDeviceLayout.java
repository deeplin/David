package com.david.incubator.ui.setting;

import android.content.Context;
import android.databinding.ObservableField;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.serial.command.other.VersionCommand;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.ResourceUtil;
import com.david.common.util.UuidUtil;
import com.david.databinding.LayoutSettingDeviceBinding;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/3 19:32
 * email: 10525677@qq.com
 * description:
 */

public class SettingDeviceLayout extends BindingConstraintLayout<LayoutSettingDeviceBinding> {

    @Inject
    MessageSender messageSender;

    public ObservableField<String> hardware = new ObservableField<>();
    public ObservableField<String> master = new ObservableField<>();
    public ObservableField<String> slave = new ObservableField<>();
    public ObservableField<String> upper = new ObservableField<>();
    public ObservableField<String> deviceId = new ObservableField<>();

    public SettingDeviceLayout(Context context) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        binding.setViewModel(this);

        setHardware("");
        setMaster("");
        setSlave("");
        setUpper(ResourceUtil.getString(R.string.upper_version_id));
        setDeviceId(UuidUtil.getUuid(MainApplication.getInstance()).substring(0, 10));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_setting_device;
    }

    @Override
    public void attach() {
        binding.title.setTitle(R.string.software_information);

        messageSender.getVersion((aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                VersionCommand versionCommand = (VersionCommand) baseSerialMessage;
                setHardware(versionCommand.getHARDWARE());
                setMaster(versionCommand.getDAVE());
                setSlave(versionCommand.getFUSE());
            }
        });
    }

    @Override
    public void detach() {
    }

    private void setHardware(String hardwareVersion) {
        String hardwareTitle = ResourceUtil.getString(R.string.hardware_version);
        hardware.set(String.format("%s: %s", hardwareTitle, hardwareVersion));
    }

    private void setMaster(String masterVersion) {
        String masterTitle = ResourceUtil.getString(R.string.master_version);
        master.set(String.format("%s: %s", masterTitle, masterVersion));
    }

    private void setSlave(String slaveVersion) {
        String slaveTitle = ResourceUtil.getString(R.string.slave_version);
        slave.set(String.format("%s: %s", slaveTitle, slaveVersion));
    }

    private void setUpper(String upperVersion) {
        String upperTitle = ResourceUtil.getString(R.string.upper_version);
        upper.set(String.format("%s: %s", upperTitle, upperVersion));
    }

    private void setDeviceId(String deviceIdVersion) {
        String deviceIdTitle = ResourceUtil.getString(R.string.device_id);
        deviceId.set(String.format("%s: %s", deviceIdTitle, deviceIdVersion));
    }
}
