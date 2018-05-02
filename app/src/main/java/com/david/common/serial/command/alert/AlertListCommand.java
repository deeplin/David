package com.david.common.serial.command.alert;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

public class AlertListCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~ALERT" + CommandChar.ENTER).getBytes();

    public AlertListCommand(){
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
