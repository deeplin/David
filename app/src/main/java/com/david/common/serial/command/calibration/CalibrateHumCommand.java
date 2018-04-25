package com.david.common.serial.command.calibration;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

/**
 * author: Ling Lin
 * created on: 2018/1/6 11:10
 * email: 10525677@qq.com
 * description:
 */
public class CalibrateHumCommand extends BaseSerialMessage {

    public static final String COMMAND = "~CAL HUM %s" + CommandChar.ENTER;

    private int value;

    public CalibrateHumCommand(int value) {
        this.value = value;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, value)).getBytes();
    }
}
