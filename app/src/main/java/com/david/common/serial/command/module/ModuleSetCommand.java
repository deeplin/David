package com.david.common.serial.command.module;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/9/19 13:41
 * email: 10525677@qq.com
 * description:
 */

public class ModuleSetCommand extends BaseSerialMessage {

    public static final String COMMAND = "~MODULE %s %s" + CommandChar.ENTER;

    private String statusString;
    private String sensorID;

    public ModuleSetCommand(boolean status, String sensorID) {
        if (status) {
            statusString = CommandChar.ON;
        } else {
            statusString = CommandChar.OFF;
        }
        this.sensorID = sensorID;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, statusString, sensorID)).getBytes();
    }
}
