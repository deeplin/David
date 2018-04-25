package com.david.common.serial.command.alert;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.utils.CommandChar;

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
    private boolean longMute;

    public AlertMuteCommand(String alertId, String option, boolean longMute) {
        this.alertId = alertId;
        this.option = option;
        this.longMute = longMute;
    }

    @Override
    public byte[] getRequest() {
        int time;
        if (longMute) {
            time = 240;
        } else {
            time = 115;
        }
        return (String.format(COMMAND, alertId, option, time)).getBytes();
    }
}
