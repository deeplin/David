package com.david.common.serial.command.alert;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

/**
 * author: Ling Lin
 * created on: 2017/7/20 21:59
 * email: 10525677@qq.com
 * description:
 */

public class AlertMuteCommand extends BaseSerialMessage {

    public static final String COMMAND = "~ALERT MUTE %s %s %s" + CommandChar.ENTER;

    private String alertId;
    private String option;
    private int time;

    public AlertMuteCommand(String alertId, String option, int time) {
        this.alertId = alertId;
        this.option = option;
        this.time = time;
    }

    @Override
    public byte[] getRequest() {
        return (String.format(COMMAND, alertId, option, time)).getBytes();
    }
}
