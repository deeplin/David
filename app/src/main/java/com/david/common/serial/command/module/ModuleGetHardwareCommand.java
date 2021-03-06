package com.david.common.serial.command.module;

import com.david.common.util.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/9/19 13:00
 * email: 10525677@qq.com
 * description:
 */

public class ModuleGetHardwareCommand extends ModuleGetSoftwareCommand {
    public static final byte[] HARDWARE_COMMAND = ("~MODULE HARDWARE" + CommandChar.ENTER).getBytes();

    private int USER;

    private int INST;

    protected int MAT;

    protected int ENV;

    @Override
    public byte[] getRequest() {
        return HARDWARE_COMMAND;
    }

    public void setO2(int o2) {
        O2 = o2;
    }

    public void setHUM(int HUM) {
        this.HUM = HUM;
    }

    public void setSPO2(int SPO2) {
        this.SPO2 = SPO2;
    }

    public void setSCALE(int SCALE) {
        this.SCALE = SCALE;
    }

    public void setMODEL(String MODEL) {
        this.MODEL = MODEL;
    }

    public void setBLUE(int BLUE) {
        this.BLUE = BLUE;
    }

    public void setCAMERA(int CAMERA) {
        this.CAMERA = CAMERA;
    }

    public int getUSER() {
        return USER;
    }

    public void setUSER(int USER) {
        this.USER = USER;
    }

    public int getINST() {
        return INST;
    }

    public void setINST(int INST) {
        this.INST = INST;
    }

    public int getMAT() {
        return MAT;
    }

    public void setMAT(int MAT) {
        this.MAT = MAT;
    }

    public int getENV() {
        return ENV;
    }

    public void setENV(int ENV) {
        this.ENV = ENV;
    }
}