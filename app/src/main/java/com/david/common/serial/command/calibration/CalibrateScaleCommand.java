package com.david.common.serial.command.calibration;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/7/28 9:54
 * email: 10525677@qq.com
 * description:
 */

public class CalibrateScaleCommand extends BaseSerialMessage {
    public static final String COMMAND = "~CAL SCALE %d" + CommandChar.ENTER;

    private int value;

    public CalibrateScaleCommand(int value) {
        this.value = value;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, value)).getBytes();
    }
}

