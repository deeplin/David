package com.david.common.data;

import android.databinding.ObservableInt;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.serial.command.module.ModuleGetSoftwareCommand;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.BiConsumer;


/**
 * author: Ling Lin
 * created on: 2017/12/26 15:37
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class ModuleSoftware implements BiConsumer<Boolean, BaseSerialMessage> {

    public ObservableInt updated = new ObservableInt(0);

    private boolean O2;
    private boolean HUM;
    private boolean SPO2;
    private boolean SCALE;

    @Inject
    public ModuleSoftware() {
    }

    public boolean isO2() {
        return O2;
    }

    public boolean isHUM() {
        return HUM;
    }

    public boolean isSPO2() {
        return SPO2;
    }

    public boolean isSCALE() {
        return SCALE;
    }

    @Override
    public void accept(Boolean aBoolean, BaseSerialMessage baseSerialMessage) {
        if (aBoolean) {
            ModuleGetSoftwareCommand moduleGetSoftwareCommand = (ModuleGetSoftwareCommand) baseSerialMessage;
            SPO2 = moduleGetSoftwareCommand.getSPO2() == 1;
            O2 = moduleGetSoftwareCommand.getO2() == 1;
            HUM = moduleGetSoftwareCommand.getHUM() == 1;
            SCALE = moduleGetSoftwareCommand.getSCALE() == 1;
            updated.set(updated.get() + 1);
        }
    }
}