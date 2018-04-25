package com.david.common.serial.command.ctrl;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

/**
 * filename: com.eternal.davidconsole.serial.command.ctrl.CtrlSetCommand.java
 * author: Ling Lin
 * created on: 2017/7/22 12:46
 * email: 10525677@qq.com
 * description:
 */

public class CtrlSetCommand extends BaseSerialMessage {
    public static final String COMMAND = "~CTRL SET %s %s %s" + CommandChar.ENTER;

    private String mode;
    private String target;
    private String value;

    public CtrlSetCommand(String mode, String target, String value) {
        this.mode = mode.toUpperCase();
        this.target = target.toUpperCase();
        this.value = value;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, mode, target, value)).getBytes();
    }
}
