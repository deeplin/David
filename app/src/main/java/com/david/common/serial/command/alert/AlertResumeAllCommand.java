package com.david.common.serial.command.alert;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.utils.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/7/20 21:59
 * email: 10525677@qq.com
 * description:
 */
public class AlertResumeAllCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~ALERT RESUME ALL" + CommandChar.ENTER).getBytes();

    public AlertResumeAllCommand() {
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
