package com.david.common.serial.command.other;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

public class FactoryCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~FACTORY" + CommandChar.ENTER).getBytes();

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
