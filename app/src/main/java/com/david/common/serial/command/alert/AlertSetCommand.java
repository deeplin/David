package com.david.common.serial.command.alert;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;
import com.david.common.util.Constant;

/**
 * author: Ling Lin
 * created on: 2017/7/22 21:55
 * email: 10525677@qq.com
 * description:
 */

public class AlertSetCommand extends BaseSerialMessage {

    public static final String COMMAND_SPO2 = "~ALERT SET %s %d" + CommandChar.ENTER;
    public static final String COMMAND_OFFSET = "~ALERT SET %s %d %d" + CommandChar.ENTER;

    /*Spo2 Pr*/
    /*Offset*/
    /*OverHeat*/
    String target;
    int value1;
    int value2;

    public AlertSetCommand(String target, int value) {
        this.target = target;
        this.value1 = value;
        this.value2 = Constant.SENSOR_NA_VALUE;
    }

    public AlertSetCommand(String target, int value1, int value2) {
        this.target = target;
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public byte[] getRequest() {
        if (value2 == Constant.SENSOR_NA_VALUE) {
            return (String.format(COMMAND_SPO2, target, value1)).getBytes();
        } else {
            return (String.format(COMMAND_OFFSET, target, value1, value2)).getBytes();
        }
    }
}

