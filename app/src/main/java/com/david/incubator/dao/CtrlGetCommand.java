package com.david.incubator.dao;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.utils.CommandChar;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

/**
 * author: Ling Lin
 * created on: 2017/12/26 16:21
 * email: 10525677@qq.com
 * description:
 */
@Entity
public class CtrlGetCommand extends BaseSerialMessage {
    @Transient
    public static final byte[] COMMAND = ("~CTRL GET" + CommandChar.ENTER).getBytes();

    @Id(autoincrement = true)
    private Long id;
    private long timeStamp;

    private String ctrl;
    private int c_air;
    private int c_hum;
    private int c_o2;
    private int c_skin;
    private int w_skin;
    private int w_man;
    private int w_inc;

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }

    public String getCtrl() {
        return ctrl;
    }

    public void setCtrl(String ctrl) {
        this.ctrl = ctrl;
    }

    public int getC_air() {
        return c_air;
    }

    public void setC_air(int c_air) {
        this.c_air = c_air;
    }

    public int getC_hum() {
        return c_hum;
    }

    public void setC_hum(int c_hum) {
        this.c_hum = c_hum;
    }

    public int getC_o2() {
        return c_o2;
    }

    public void setC_o2(int c_o2) {
        this.c_o2 = c_o2;
    }

    public int getC_skin() {
        return c_skin;
    }

    public void setC_skin(int c_skin) {
        this.c_skin = c_skin;
    }

    public int getW_skin() {
        return w_skin;
    }

    public void setW_skin(int w_skin) {
        this.w_skin = w_skin;
    }

    public int getW_man() {
        return w_man;
    }

    public void setW_man(int w_man) {
        this.w_man = w_man;
    }

    public int getW_inc() {
        return w_inc;
    }

    public void setW_inc(int w_inc) {
        this.w_inc = w_inc;
    }

    @Transient
    private CtrlGetCommand ctrlGetCommand;

    @Generated(hash = 194615124)
    public CtrlGetCommand(Long id, long timeStamp, String ctrl, int c_air, int c_hum,
                          int c_o2, int c_skin, int w_skin, int w_man, int w_inc) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.ctrl = ctrl;
        this.c_air = c_air;
        this.c_hum = c_hum;
        this.c_o2 = c_o2;
        this.c_skin = c_skin;
        this.w_skin = w_skin;
        this.w_man = w_man;
        this.w_inc = w_inc;
    }

    @Generated(hash = 1730074060)
    public CtrlGetCommand() {
    }

    public void setCtrlGetCommand(CtrlGetCommand ctrlGetCommand) {
        this.ctrlGetCommand = ctrlGetCommand;
    }

    public boolean isChanged() {
        if (ctrlGetCommand != null) {
            if (!ctrl.equals(ctrlGetCommand.ctrl)) {
                return true;
            } else if (c_air != ctrlGetCommand.c_air) {
                return true;
            } else if (c_hum != ctrlGetCommand.c_hum) {
                return true;
            } else if (c_o2 != ctrlGetCommand.c_o2) {
                return true;
            } else if (c_skin != ctrlGetCommand.c_skin) {
                return true;
            } else if (w_skin != ctrlGetCommand.w_skin) {
                return true;
            } else if (w_man != ctrlGetCommand.w_man) {
                return true;
            } else if (w_inc != ctrlGetCommand.w_inc) {
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
}
