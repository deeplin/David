package com.david.common.dao;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;
import com.david.common.util.Constant;
import com.david.common.mode.AverageTimeMode;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * author: Ling Lin
 * created on: 2017/12/26 16:29
 * email: 10525677@qq.com
 * description:
 */
@Entity
public class Spo2GetCommand extends BaseSerialMessage {

    @Transient
    public static final byte[] COMMAND = ("~SPO2 GET" + CommandChar.ENTER).getBytes();

    @Id(autoincrement = true)
    private Long id;
    private long timeStamp;

    private String status;
    private int avg;
    private String sens;
    private String fastsat;

    public Spo2GetCommand() {
        status = "";
        avg = AverageTimeMode.None.getIndex();
        sens = "";
        fastsat = "";
    }

    @Generated(hash = 449286130)
    public Spo2GetCommand(Long id, long timeStamp, String status, int avg, String sens,
            String fastsat) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.status = status;
        this.avg = avg;
        this.sens = sens;
        this.fastsat = fastsat;
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }

    @Transient
    private Spo2GetCommand spo2GetCommand;

    public void setSpo2GetCommand(Spo2GetCommand spo2GetCommand) {
        this.spo2GetCommand = spo2GetCommand;
    }

    public boolean isChanged() {
        if (spo2GetCommand != null) {
            if (!status.equals(spo2GetCommand.status)) {
                return true;
            } else if (avg != spo2GetCommand.avg) {
                return true;
            } else if (!sens.equals(spo2GetCommand.sens)) {
                return true;
            } else if (!fastsat.equals(spo2GetCommand.fastsat)) {
                return true;
            }
        } else {
            return true;
        }
        return false;
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

    public String getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = Constant.SENSOR_NA_STRING;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAvg() {
        return this.avg;
    }

    public void setAvg(int avg) {
        this.avg = avg;
    }

    public String getSens() {
        return this.sens;
    }

    public void setSens(String sens) {
        this.sens = sens;
    }

    public String getFastsat() {
        return this.fastsat;
    }

    public void setFastsat(String fastsat) {
        this.fastsat = fastsat;
    }
}