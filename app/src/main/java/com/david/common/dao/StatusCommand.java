package com.david.common.dao;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.utils.CommandChar;
import com.david.common.utils.Constant;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:30
 * email: 10525677@qq.com
 * description:
 */
@Entity
public class StatusCommand extends BaseSerialMessage {

    //    OK;Mode=Transit;Ctrl=Standby;Time=3735;CTime=3735;Warm=0;Inc=0;Hum=0;O2=0;Alert=SYS.PWR;
    @Transient
    public static final byte[] COMMAND = ("~STATUS" + CommandChar.ENTER).getBytes();

    @Id(autoincrement = true)
    private Long id;
    private long timeStamp;

    private String mode;
    private String ctrl;
    @Transient
    private int time;
    @Transient
    private int CTime;
    private int warm;
    private int inc;
    /*湿度阀门*/
    private int hum;
    /*氧气阀门*/
    private int o2;
    private String alert;

    @Transient
    private int ohtest;

    @Generated(hash = 377142358)
    public StatusCommand(Long id, long timeStamp, String mode, String ctrl, int warm, int inc, int hum,
            int o2, String alert) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.mode = mode;
        this.ctrl = ctrl;
        this.warm = warm;
        this.inc = inc;
        this.hum = hum;
        this.o2 = o2;
        this.alert = alert;
    }

    @Generated(hash = 1692704117)
    public StatusCommand() {
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMode() {
        return this.mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCtrl() {
        return this.ctrl;
    }

    public void setCtrl(String ctrl) {
        this.ctrl = ctrl;
    }

    public int getWarm() {
        return this.warm;
    }

    public void setWarm(int warm) {
        this.warm = warm;
    }

    public int getInc() {
        return this.inc;
    }

    public void setInc(int inc) {
        this.inc = inc;
    }

    public int getHum() {
        return this.hum;
    }

    public void setHum(int hum) {
        this.hum = hum;
    }

    public int getO2() {
        return this.o2;
    }

    public void setO2(int o2) {
        this.o2 = o2;
    }

    public String getAlert() {
        return this.alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public void setAlert(int alert) {
        this.alert = Constant.SENSOR_NA_STRING;
    }

    public int getCTime() {
        return CTime;
    }

    public void setCTime(int CTime) {
        this.CTime = CTime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getOhtest() {
        return ohtest;
    }

    public void setOhtest(int ohtest) {
        this.ohtest = ohtest;
    }
}

