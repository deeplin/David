package com.david.common.serial.command.calibration;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/8/4 10:10
 * email: 10525677@qq.com
 * description:
 */

public class ShowCalibrationCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~SHOW CAL" + CommandChar.ENTER).getBytes();
    private int S1A;
    private int S1B;
    private int S2;
    private int A1;
    private int A2;
    private int F1;
    private String O1;
    private String O2;
    private int HUM;
    private String SC;

    public ShowCalibrationCommand() {
        S1A = 0;
        S1B = 0;
        S2 = 0;
        A1 = 0;
        A2 = 0;
        F1 = 0;
        O1 = "";
        O2 = "";
        HUM = 0;
        SC = "";
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }

    public int getS1A() {
        return S1A;
    }

    public void setS1A(int s1A) {
        S1A = s1A;
    }

    public int getS1B() {
        return S1B;
    }

    public void setS1B(int s1B) {
        S1B = s1B;
    }

    public int getA1() {
        return A1;
    }

    public void setA1(int a1) {
        A1 = a1;
    }

    public int getF1() {
        return F1;
    }

    public void setF1(int f1) {
        F1 = f1;
    }

    public int getA2() {
        return A2;
    }

    public void setA2(int a2) {
        A2 = a2;
    }

    public String getO1() {
        return O1;
    }

    public void setO1(String o1) {
        O1 = o1;
    }

    public String getO2() {
        return O2;
    }

    public void setO2(String o2) {
        O2 = o2;
    }

    public String getSC() {
        return SC;
    }

    public void setSC(String SC) {
        this.SC = SC;
    }

    public int getS2() {
        return S2;
    }

    public void setS2(int s2) {
        S2 = s2;
    }

    public int getHUM() {
        return HUM;
    }

    public void setHUM(int HUM) {
        this.HUM = HUM;
    }
}

