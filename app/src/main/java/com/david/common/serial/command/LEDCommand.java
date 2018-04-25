package com.david.common.serial.command;

import com.david.common.serial.BaseSerialMessage;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:39
 * email: 10525677@qq.com
 * description:
 */
public class LEDCommand extends BaseSerialMessage {

    public static final String LED37 = "37";
    public static final String BLUE = "BLUE";

    private static final String COMMAND = "~LED %s %s" + CommandChar.ENTER;

    private String value;
    private String status;

    public LEDCommand(String value, boolean status) {
        this.value = value;
        if (status) {
            this.status = CommandChar.ON;
        } else {
            this.status = CommandChar.OFF;
        }
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, value, status)).getBytes();
    }
}
