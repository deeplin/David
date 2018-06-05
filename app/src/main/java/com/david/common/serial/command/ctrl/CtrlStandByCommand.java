package com.david.common.serial.command.ctrl;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

public class CtrlStandByCommand extends BaseSerialMessage {
    public static final String COMMAND = "~CTRL STANDBY %s" + CommandChar.ENTER;

    private String status;

    public CtrlStandByCommand(boolean on) {
        if (on) {
            status = CommandChar.ON;
        } else {
            status = CommandChar.OFF;
        }
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, status)).getBytes();
    }
}
