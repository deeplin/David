package com.david.common.serial.command.alert;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/7/20 19:31
 * email: 10525677@qq.com
 * description:
 */

public class AlertDisableCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~ALERT DISABLE ALL 30" + CommandChar.ENTER).getBytes();

    public AlertDisableCommand() {
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}

