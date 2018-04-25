package com.david.common.serial.command.alert;

import com.david.common.mode.AlertSettingMode;
import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

/**
 * filename: com.eternal.davidconsole.serial.command.alertString.AlertGetCommand.java
 * author: Ling Lin
 * created on: 2017/7/22 22:01
 * email: 10525677@qq.com
 * description:
 */

public class AlertGetCommand extends BaseSerialMessage {

    public static final String COMMAND = "~ALERT GET %s" + CommandChar.ENTER;

    AlertSettingMode alertSettingMode;

    /*Offset*/
    private int ADevH;
    private int ADevL;
    private int SDevH;
    private int SDevL;
    private int ODevH;
    private int ODevL;
    private int HDevH;
    private int HDevL;

    /*Overheat*/
    private int T1;
    private int T2;
    private int T;
    private int RPM;

    /*SPO2 PR*/
    private int limit;

    public AlertGetCommand(AlertSettingMode alertSettingMode) {
        this.alertSettingMode = alertSettingMode;
    }

    public int getADevH() {
        return ADevH;
    }

    public void setADevH(int ADevH) {
        this.ADevH = ADevH;
    }

    public int getADevL() {
        return ADevL;
    }

    public void setADevL(int ADevL) {
        this.ADevL = ADevL;
    }

    public int getSDevH() {
        return SDevH;
    }

    public void setSDevH(int SDevH) {
        this.SDevH = SDevH;
    }

    public int getSDevL() {
        return SDevL;
    }

    public void setSDevL(int SDevL) {
        this.SDevL = SDevL;
    }

    public int getODevH() {
        return ODevH;
    }

    public void setODevH(int ODevH) {
        this.ODevH = ODevH;
    }

    public int getODevL() {
        return ODevL;
    }

    public void setODevL(int ODevL) {
        this.ODevL = ODevL;
    }

    public int getHDevH() {
        return HDevH;
    }

    public void setHDevH(int HDevH) {
        this.HDevH = HDevH;
    }

    public int getHDevL() {
        return HDevL;
    }

    public void setHDevL(int HDevL) {
        this.HDevL = HDevL;
    }

    public AlertSettingMode getAlertSettingMode() {
        return alertSettingMode;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, alertSettingMode.getName())).getBytes();
    }

    public int getT1() {
        return T1;
    }

    public void setT1(int t1) {
        T1 = t1;
    }

    public int getT2() {
        return T2;
    }

    public void setT2(int t2) {
        T2 = t2;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getT() {
        return T;
    }

    public void setT(int t) {
        T = t;
    }

    public int getRPM() {
        return RPM;
    }

    public void setRPM(int RPM) {
        this.RPM = RPM;
    }
}

