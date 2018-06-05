package com.david.common.serial.command.other;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

public class LanguageCommand extends BaseSerialMessage {

    private static final String COMMAND = "~LANGUAGE %s" + CommandChar.ENTER;

    private String value;

    public LanguageCommand(String value) {
        this.value = value;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, value)).getBytes();
    }
}
