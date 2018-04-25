package com.david.common.serial.command.module;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.utils.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/9/19 13:00
 * email: 10525677@qq.com
 * description:
 */

public class ModuleGetSoftwareCommand extends BaseSerialMessage {
    public static final byte[] SOFTWARE_COMMAND = ("~MODULE GET" + CommandChar.ENTER).getBytes();

    protected int O2;
    protected int HUM;
    protected int SPO2;
    protected int SCALE;
    protected int CAMERA;
    protected int BLUE;
    protected String MODEL;

    @Override
    public byte[] getRequest() {
        return SOFTWARE_COMMAND;
    }

    public int getO2() {
        return O2;
    }

    public void setO2(int o2) {
        O2 = o2;
    }

    public int getHUM() {
        return HUM;
    }

    public void setHUM(int HUM) {
        this.HUM = HUM;
    }

    public int getSPO2() {
        return SPO2;
    }

    public void setSPO2(int SPO2) {
        this.SPO2 = SPO2;
    }

    public int getSCALE() {
        return SCALE;
    }

    public void setSCALE(int SCALE) {
        this.SCALE = SCALE;
    }

    public int getCAMERA() {
        return CAMERA;
    }

    public void setCAMERA(int CAMERA) {
        this.CAMERA = CAMERA;
    }

    public int getBLUE() {
        return BLUE;
    }

    public void setBLUE(int BLUE) {
        this.BLUE = BLUE;
    }

    public String getMODEL() {
        return MODEL;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }
}
