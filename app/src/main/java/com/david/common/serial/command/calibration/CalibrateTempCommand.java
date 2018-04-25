package com.david.common.serial.command.calibration;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.utils.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/7/28 9:55
 * email: 10525677@qq.com
 * description:
 */

public class CalibrateTempCommand extends BaseSerialMessage {

    public static final String PLUS = "+";
    public static final String MINUS = "-";
    public static final String COMMAND = "~CAL TEMP %s %s" + CommandChar.ENTER;

    private String id;
    private String value;
    private int offset;

    public CalibrateTempCommand(String id, String value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, id, value)).getBytes();
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
