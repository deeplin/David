package com.david.common.serial.command.ctrl;


import com.david.common.serial.BaseSerialMessage;
import com.david.common.utils.CommandChar;

/**
 * filename: com.eternal.davidconsole.serial.command.ctrl.CtrlStandbyCommand.java
 * author: Ling Lin
 * created on: 2017/9/18 13:43
 * email: 10525677@qq.com
 * description:
 */

public class CtrlStandbyCommand extends BaseSerialMessage {

    public static final String COMMAND = "~CTRL STANDBY %s" + CommandChar.ENTER;

    private String value;

    public CtrlStandbyCommand(boolean status) {
        if (status) {
            value = CommandChar.OFF;
        } else {
            value = CommandChar.ON;
        }
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, value)).getBytes();
    }
}
