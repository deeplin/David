package com.david.common.serial.command.ctrl;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

/**
 * author: Ling Lin
 * created on: 2018/1/19 20:21
 * email: 10525677@qq.com
 * description:
 */
public class CtrlOverrideCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~CTRL OVERRIDE 100" + CommandChar.ENTER).getBytes();

    public CtrlOverrideCommand() {
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
