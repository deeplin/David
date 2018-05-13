package com.david.common.serial.command.other;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/7/30 16:01
 * email: 10525677@qq.com
 * description:
 */
public class VersionCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~VERSION" + CommandChar.ENTER).getBytes();

    public String getHARDWARE() {
        return HARDWARE;
    }

    public void setHARDWARE(String HARDWARE) {
        this.HARDWARE = HARDWARE;
    }

    public String getDAVE() {
        return DAVE;
    }

    public void setDAVE(String DAVE) {
        this.DAVE = DAVE;
    }

    public String getFUSE() {
        return FUSE;
    }

    public void setFUSE(String FUSE) {
        this.FUSE = FUSE;
    }

    private String HARDWARE;
    private String DAVE;
    private String FUSE;

    public VersionCommand() {
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}